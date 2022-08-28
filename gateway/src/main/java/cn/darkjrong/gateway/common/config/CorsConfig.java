package cn.darkjrong.gateway.common.config;

import cn.hutool.core.comparator.VersionComparator;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Collections;

/**
 * 跨域配置
 *
 * @author Rong.Jia
 * @date 2022/02/08
 */
@Configuration
public class CorsConfig {

    private static final String SPRING_VERSION = "2.4.0";

    @Bean
    public CorsWebFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedHeaders(Collections.singletonList("Authorization,Origin, X-Requested-With, Content-Type, Accept,WWW-Authenticate"));
        config.setAllowCredentials(true);
        if (this.compareVersion()) {
            config.addAllowedOriginPattern("*");
        } else {
            config.addAllowedOrigin("*");
        }
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        config.addExposedHeader("Location");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }

    private boolean compareVersion() {
        String version = SpringBootVersion.getVersion();
        return VersionComparator.INSTANCE.compare(version, SPRING_VERSION) >= 0;
    }

}
