package fr.tiogars.data.gateway.config;

import java.time.Duration;

import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.caffeine.Bucket4jCaffeine;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.AsyncProxyManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfiguration {

	@Bean
	public AsyncProxyManager<String> asyncProxyManager() {
		Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder().maximumSize(20_000);
		return Bucket4jCaffeine.<String>builderFor(cacheBuilder)
			.expirationAfterWrite(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofMinutes(5)))
			.build()
			.asAsync();
	}
}
