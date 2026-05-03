package fr.tiogars.data.gateway.config;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(GatewaySecurityProperties.class)
public class SecurityConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, GatewaySecurityProperties properties) throws Exception {
		http
				.cors(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		if (!properties.isEnabled()) {
			LOGGER.warn("Gateway security disabled by property data.gateway.security.enabled=false");
			http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
			return http.build();
		}

		AuthorizationManager<RequestAuthorizationContext> roleAuthorizationManager = roleAuthorizationManager(properties);
		JwtAuthenticationConverter jwtAuthenticationConverter = jwtAuthenticationConverter(properties);

		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.OPTIONS, "/actuator/**").permitAll()
						.requestMatchers("/actuator/health", "/actuator/info", "/error").permitAll()
						.anyRequest().access(roleAuthorizationManager))
				.oauth2ResourceServer(oauth2 -> oauth2
						.jwt(jwt -> jwt
								.decoder(jwtDecoder(properties))
								.jwtAuthenticationConverter(jwtAuthenticationConverter)));

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(false);
		config.setAllowedOriginPatterns(java.util.List.of("*"));
		config.setAllowedMethods(java.util.List.of(HttpMethod.GET.name(), HttpMethod.OPTIONS.name()));
		config.setAllowedHeaders(java.util.List.of("*"));
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/actuator/**", config);
		return source;
	}

	private JwtDecoder jwtDecoder(GatewaySecurityProperties properties) {
		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(properties.jwkSetUri()).build();
		OAuth2TokenValidator<Jwt> defaultValidator = JwtValidators.createDefaultWithIssuer(properties.issuerUri());
		jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(defaultValidator));
		return jwtDecoder;
	}

	private JwtAuthenticationConverter jwtAuthenticationConverter(GatewaySecurityProperties properties) {
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
		Set<String> requiredAuthorities = properties.normalizedAllowedAuthorities();
		if (requiredAuthorities.isEmpty()) {
			LOGGER.warn("No allowed roles configured. Access will require authentication only.");
		}

		return (authenticationSupplier, context) -> {
			Authentication authentication = authenticationSupplier.get();
			boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
			if (!isAuthenticated) {
				return new AuthorizationDecision(false);
			}

			Set<String> userAuthorities = authentication.getAuthorities().stream()
					.map(GrantedAuthority::getAuthority)
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
				.map(Object::toString)
				.map(String::trim)
				.filter(value -> !value.isEmpty())
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}
}
