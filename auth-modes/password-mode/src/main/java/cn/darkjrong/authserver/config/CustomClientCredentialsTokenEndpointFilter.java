package cn.darkjrong.authserver.config;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 自定义客户端异常处理
 *
 * @author Rong.Jia
 * @date 2022/08/28
 */
@AllArgsConstructor
public class CustomClientCredentialsTokenEndpointFilter extends ClientCredentialsTokenEndpointFilter {

    private final AuthorizationServerSecurityConfigurer configurer;
    private final AuthenticationEntryPoint entryPoint;

    @Override
    protected AuthenticationManager getAuthenticationManager() {
        return configurer.and().getSharedObject(AuthenticationManager.class);
    }

    @Override
    public void afterPropertiesSet() {
        setAuthenticationFailureHandler(entryPoint::commence);
        setAuthenticationSuccessHandler((request, response, authentication) -> {});
    }
}
