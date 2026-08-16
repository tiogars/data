package fr.tiogars.data.gateway.routes;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.filter.Bucket4jFilterFunctions.rateLimit;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.path;

@Configuration
public class GatewayRoutesConfiguration {

	@Bean
	public RouterFunction<ServerResponse> dataServerRoutes(
			DomainPathRegistry domainPathRegistry,
			@Value("${data.gateway.downstream-base-url}") String downstreamBaseUrl,
			@Value("${data.gateway.rate-limit.capacity:120}") int capacity,
			@Value("${data.gateway.rate-limit.period:PT1M}") Duration period,
			@Value("${data.gateway.rate-limit.tokens:1}") int tokens
	) {
		var builder = route("data_server_routes")
				.route(path("/api/**"), http());

		builder = builder.route(request -> domainPathRegistry.matches(request.path()), http());

		return builder
				.route(path("/server-info/**"), http())
				.route(path("/actuator/**"), http())
				.route(path("/v3/api-docs/**"), http())
				.route(path("/swagger-ui/**"), http())
				.route(path("/swagger-ui.html"), http())
				.before(uri(downstreamBaseUrl))
				.filter(rateLimit(c -> c
						.setCapacity(capacity)
						.setPeriod(period)
						.setTokens(tokens)
						.setStatusCode(HttpStatus.TOO_MANY_REQUESTS)
						.setKeyResolver(this::resolveClientKey)))
				.build();
	}

	private String resolveClientKey(ServerRequest request) {
		String forwardedFor = request.headers().firstHeader("X-Forwarded-For");
		if (StringUtils.hasText(forwardedFor)) {
			int separatorIndex = forwardedFor.indexOf(',');
			return separatorIndex >= 0 ? forwardedFor.substring(0, separatorIndex).trim() : forwardedFor.trim();
		}

		if (request.remoteAddress().isPresent()) {
			return request.remoteAddress().get().getAddress().getHostAddress();
		}

		return "anonymous";
	}
}
