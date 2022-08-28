package cn.darkjrong.gateway.common.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.autoconfigure.TimeLimiterProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * gateway使用resilience4j作为断路器时进行的配置
 *
 * @author Rong.Jia
 * @date 2022/02/08
 */
@Configuration
public class Resilience4jConfig {

    @Autowired
    private TimeLimiterProperties timeLimiterProperties;

    /**
     * 初始化断路器
     * @param circuitBreakerRegistry
     * @return
     */
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer(CircuitBreakerRegistry circuitBreakerRegistry) {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(circuitBreakerRegistry.getDefaultConfig())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                        .timeoutDuration(timeLimiterProperties.getConfigs().get(("default")).getTimeoutDuration())
                        .cancelRunningFuture(timeLimiterProperties.getConfigs().get(("default")).getCancelRunningFuture())
                        .build())
                .build());
    }


}
