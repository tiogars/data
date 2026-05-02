package fr.tiogars.data.gateway.config;

import java.time.Duration;

import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.caffeine.CaffeineProxyManager;
import io.github.bucket4j.distributed.proxy.AsyncProxyManager;
import io.github.bucket4j.distributed.remote.RemoteBucketState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfiguration {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public AsyncProxyManager<String> asyncProxyManager() {
		Caffeine<String, RemoteBucketState> cacheBuilder = (Caffeine) Caffeine.newBuilder().maximumSize(20_000);
		return new CaffeineProxyManager<>(cacheBuilder, Duration.ofMinutes(5)).asAsync();
	}
}
