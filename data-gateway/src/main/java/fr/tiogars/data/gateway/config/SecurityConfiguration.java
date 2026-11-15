package fr.tiogars.data.gateway.config;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(GatewaySecurityProperties.class)
public class SecurityConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);
	private static final String LOGIN_PATH = "/auth/login";
	private static final String JWKS_PATH = "/auth/jwks";

	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			GatewaySecurityProperties properties,
			@Value("${data.gateway.downstream-base-url}") String downstreamBaseUrl) throws Exception {
		http
				.cors(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		if (!properties.isEnabled()) {
			LOGGER.warn("Gateway security disabled by property data.gateway.security.enabled=false");
			http.authorizeHttpRequests(auth -> auth
					.requestMatchers("/server-info/domain-paths", "/auth/jwks").denyAll()
					.anyRequest().permitAll());
			return http.build();
		}

		GatewaySecurityProperties.Local local = properties.getLocal();
		if (!properties.isOidcEnabled() && !local.isEnabled()) {
			LOGGER.warn("Both OIDC and local authentication are disabled: every protected request will be rejected.");
		}

		AuthorizationManager<RequestAuthorizationContext> roleAuthorizationManager = roleAuthorizationManager(properties);

		http
				.authorizeHttpRequests(auth -> {
					auth
							.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
							.requestMatchers("/actuator/health", "/actuator/info", "/error").permitAll()
							.requestMatchers("/server-info/domain-paths", "/auth/jwks").denyAll();
					if (local.isEnabled()) {
						auth.requestMatchers(HttpMethod.POST, LOGIN_PATH).permitAll();
					} else {
						auth.requestMatchers(LOGIN_PATH).denyAll();
					}
					auth.anyRequest().access(roleAuthorizationManager);
				})
				.oauth2ResourceServer(oauth2 -> oauth2
						.authenticationManagerResolver(authenticationManagerResolver(properties, downstreamBaseUrl)));

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(false);
		config.setAllowedOriginPatterns(java.util.List.of("*"));
		config.setAllowedMethods(java.util.List.of(
				HttpMethod.GET.name(),
				HttpMethod.POST.name(),
				HttpMethod.PUT.name(),
				HttpMethod.PATCH.name(),
				HttpMethod.DELETE.name(),
				HttpMethod.OPTIONS.name()));
		config.setAllowedHeaders(java.util.List.of("*"));
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	private AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver(
			GatewaySecurityProperties properties,
			String downstreamBaseUrl) {
		Map<String, AuthenticationManager> managersByIssuer = new LinkedHashMap<>();

		if (properties.isOidcEnabled()) {
			managersByIssuer.put(
					properties.issuerUri(),
					authenticationManager(oidcJwtDecoder(properties), oidcAuthenticationConverter(properties)));
		}

		GatewaySecurityProperties.Local local = properties.getLocal();
		if (local.isEnabled()) {
			managersByIssuer.put(
					local.getIssuer(),
					authenticationManager(localJwtDecoder(local, downstreamBaseUrl), localAuthenticationConverter()));
		}

		return new JwtIssuerAuthenticationManagerResolver(managersByIssuer::get);
	}

	private AuthenticationManager authenticationManager(JwtDecoder decoder, JwtAuthenticationConverter converter) {
		JwtAuthenticationProvider provider = new JwtAuthenticationProvider(decoder);
		provider.setJwtAuthenticationConverter(converter);
		return provider::authenticate;
	}

	private JwtDecoder oidcJwtDecoder(GatewaySecurityProperties properties) {
		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(properties.jwkSetUri()).build();
		OAuth2TokenValidator<Jwt> defaultValidator = JwtValidators.createDefaultWithIssuer(properties.issuerUri());
		jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(defaultValidator));
		return jwtDecoder;
	}

	private JwtDecoder localJwtDecoder(GatewaySecurityProperties.Local local, String downstreamBaseUrl) {
		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(localJwkSetUri(local, downstreamBaseUrl)).build();
		jwtDecoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(local.getIssuer()));
		return jwtDecoder;
	}

	private String localJwkSetUri(GatewaySecurityProperties.Local local, String downstreamBaseUrl) {
		if (StringUtils.hasText(local.getJwkSetUri())) {
			return local.getJwkSetUri().trim();
		}
		String baseUrl = downstreamBaseUrl.trim();
		while (baseUrl.endsWith("/")) {
			baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
		}
		return baseUrl + JWKS_PATH;
	}

	private JwtAuthenticationConverter localAuthenticationConverter() {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(jwt -> {
			Object roles = jwt.getClaim("roles");
			if (!(roles instanceof Collection<?> roleCollection)) {
				return Set.<GrantedAuthority>of();
			}
			return roleCollection.stream()
					.filter(Objects::nonNull)
					.map(String::valueOf)
					.map(String::trim)
					.filter(role -> !role.isEmpty())
					.map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
					.collect(Collectors.toCollection(LinkedHashSet::new));
		});
		return converter;
	}

	private JwtAuthenticationConverter oidcAuthenticationConverter(GatewaySecurityProperties properties) {
		JwtGrantedAuthoritiesConverter scopesConverter = new JwtGrantedAuthoritiesConverter();
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(jwt -> {
			Set<GrantedAuthority> authorities = new LinkedHashSet<>();
			Collection<GrantedAuthority> scopeAuthorities = scopesConverter.convert(jwt);
			if (scopeAuthorities != null) {
				authorities.addAll(scopeAuthorities);
			}
			authorities.addAll(extractRealmRoles(jwt));
			authorities.addAll(extractClientRoles(jwt, properties.getClientId()));
			return authorities;
		});
		return converter;
	}

	private AuthorizationManager<RequestAuthorizationContext> roleAuthorizationManager(GatewaySecurityProperties properties) {
		Set<String> oidcAuthorities = properties.normalizedAllowedAuthorities();
		Set<String> localAuthorities = properties.getLocal().normalizedAllowedAuthorities();
		String localIssuer = properties.getLocal().getIssuer();
		if (oidcAuthorities.isEmpty() || localAuthorities.isEmpty()) {
			LOGGER.warn("No allowed roles configured for an authentication source. Access will require authentication only.");
		}

		return (authenticationSupplier, context) -> {
			Authentication authentication = authenticationSupplier.get();
			if (authentication == null || !authentication.isAuthenticated()) {
				return new AuthorizationDecision(false);
			}

			// Les roles autorises dependent de la source du jeton : un role "USER" Keycloak ne doit pas ouvrir l'acces.
			Set<String> requiredAuthorities = isLocalToken(authentication, localIssuer) ? localAuthorities : oidcAuthorities;

			Set<String> userAuthorities = authentication.getAuthorities().stream()
					.map(SecurityConfiguration::getAuthority)
					.filter(authority -> authority.startsWith("ROLE_"))
					.collect(Collectors.toCollection(LinkedHashSet::new));

			boolean granted = requiredAuthorities.isEmpty() || requiredAuthorities.stream().anyMatch(userAuthorities::contains);

			LOGGER.info(
					"Auth user={}, roles={}, requiredRoles={}, granted={}, path={}",
					authentication.getName(),
					userAuthorities,
					requiredAuthorities,
					granted,
					context.getRequest().getRequestURI()
			);

			return new AuthorizationDecision(granted);
		};
	}

	private static boolean isLocalToken(Authentication authentication, String localIssuer) {
		return authentication.getPrincipal() instanceof Jwt jwt && localIssuer.equals(jwt.getClaimAsString("iss"));
	}

	private static String getAuthority(@org.jspecify.annotations.NonNull GrantedAuthority authority) {
		return authority.getAuthority();
	}

	private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
		Set<String> roles = extractRolesFromMap(jwt.getClaim("realm_access"));
		return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toSet());
	}

	private Collection<GrantedAuthority> extractClientRoles(Jwt jwt, String clientId) {
		Object resourceAccessClaim = jwt.getClaim("resource_access");
		if (!(resourceAccessClaim instanceof Map<?, ?> resourceAccessMap)) {
			return Set.of();
		}
		Object clientAccess = resourceAccessMap.get(clientId);
		Set<String> roles = extractRolesFromMap(clientAccess);
		return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toSet());
	}

	private Set<String> extractRolesFromMap(Object claimValue) {
		if (!(claimValue instanceof Map<?, ?> claimMap)) {
			return Set.of();
		}
		Object roles = claimMap.get("roles");
		if (!(roles instanceof Collection<?> roleCollection)) {
			return Set.of();
		}
		return roleCollection.stream()
				.filter(Objects::nonNull)
				.map(String::valueOf)
				.map(value -> value.trim())
				.filter(value -> !value.isEmpty())
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}
}
