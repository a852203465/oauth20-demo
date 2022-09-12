package cn.darkjrong.authserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 身份验证属性
 *
 * @author Rong.Jia
 * @date 2022/09/12
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2")
public class AuthProperties {

    /**
     * 重定向uri
     */
    private List<String> redirectUris;

    /**
     * 自动批准
     */
    private Boolean autoApprove = Boolean.FALSE;







}
