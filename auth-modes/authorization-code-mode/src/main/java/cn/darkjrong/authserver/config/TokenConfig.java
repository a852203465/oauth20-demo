package cn.darkjrong.authserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 令牌配置
 *
 * @author Rong.Jia
 * @date 2022/08/13
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.security")
public class TokenConfig {

    /**
     *  accessToken 超时时间， 单位：秒
     */
    private Integer accessTokenValiditySeconds = 3600;

    /**
     *  refreshToken 超时时间 单位：秒
     */
    private Integer refreshTokenValiditySeconds = 86400;





}
