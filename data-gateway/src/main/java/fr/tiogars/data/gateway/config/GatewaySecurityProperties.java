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
	private String authBaseUrl = "https://auth2.tiogars.fr/";
	private String realm = "data";
	private String clientId = "data-gateway";
	private List<String> allowedRoles = List.of("gateway-admin", "gateway-user");

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
		if (allowedRoles == null) {
			return Set.of();
		}

		return allowedRoles.stream()
				.filter(StringUtils::hasText)
				.map(String::trim)
				.map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	private String normalizeAuthBaseUrl() {
		String normalized = authBaseUrl == null ? "" : authBaseUrl.trim();
		while (normalized.endsWith("/")) {
			normalized = normalized.substring(0, normalized.length() - 1);
		}
		return normalized;
	}
}
