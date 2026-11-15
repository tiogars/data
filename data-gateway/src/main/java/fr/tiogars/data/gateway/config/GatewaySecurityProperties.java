package fr.tiogars.data.gateway.config;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "data.gateway.security")
public class GatewaySecurityProperties {

	private boolean enabled = true;
	private boolean oidcEnabled = true;
	private String authBaseUrl = "https://auth2.tiogars.fr/";
	private String realm = "data";
	private String clientId = "data-gateway";
	private List<String> allowedRoles = List.of("gateway-admin", "gateway-user");
	private final Local local = new Local();

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isOidcEnabled() {
		return oidcEnabled;
	}

	public void setOidcEnabled(boolean oidcEnabled) {
		this.oidcEnabled = oidcEnabled;
	}

	public Local getLocal() {
		return local;
	}

	public String getAuthBaseUrl() {
		return authBaseUrl;
	}

	public void setAuthBaseUrl(String authBaseUrl) {
		this.authBaseUrl = authBaseUrl;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public List<String> getAllowedRoles() {
		return allowedRoles;
	}

	public void setAllowedRoles(List<String> allowedRoles) {
		this.allowedRoles = allowedRoles;
	}

	public String issuerUri() {
		return normalizeAuthBaseUrl() + "/realms/" + realm;
	}

	public String jwkSetUri() {
		return issuerUri() + "/protocol/openid-connect/certs";
	}

	public Set<String> normalizedAllowedAuthorities() {
		return toAuthorities(allowedRoles);
	}

	private static Set<String> toAuthorities(List<String> roles) {
		if (roles == null) {
			return Set.of();
		}

		return roles.stream()
				.filter(StringUtils::hasText)
				.map(GatewaySecurityProperties::trimRole)
				.map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	private static String trimRole(String role) {
		return role.trim();
	}

	private String normalizeAuthBaseUrl() {
		String normalized = authBaseUrl == null ? "" : authBaseUrl.trim();
		while (normalized.endsWith("/")) {
			normalized = normalized.substring(0, normalized.length() - 1);
		}
		return normalized;
	}

	/**
	 * Authentification par compte utilisateur enregistre en base : les jetons sont emis par le data-server.
	 */
	public static class Local {

		private boolean enabled = true;
		private String issuer = "data-server";
		private String jwkSetUri;
		private List<String> allowedRoles = List.of("ADMIN", "USER");

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public String getIssuer() {
			return issuer;
		}

		public void setIssuer(String issuer) {
			this.issuer = issuer;
		}

		/**
		 * URL du JWKS du data-server. Par defaut : {@code <downstream-base-url>/auth/jwks}.
		 */
		public String getJwkSetUri() {
			return jwkSetUri;
		}

		public void setJwkSetUri(String jwkSetUri) {
			this.jwkSetUri = jwkSetUri;
		}

		public List<String> getAllowedRoles() {
			return allowedRoles;
		}

		public void setAllowedRoles(List<String> allowedRoles) {
			this.allowedRoles = allowedRoles;
		}

		public Set<String> normalizedAllowedAuthorities() {
			return toAuthorities(allowedRoles);
		}
	}
}
