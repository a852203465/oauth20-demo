package cn.darkjrong.authserver.config;

import cn.darkjrong.oauth.common.enums.AuthMode;
import cn.darkjrong.oauth.common.utils.JwksUtils;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.security.KeyPair;
import java.util.Arrays;

/**
 * 认证服务器配置
 *
 * @author Rong.Jia
 * @date 2022/08/13
 */
@Configuration
@AllArgsConstructor
public class AuthorizationServerConfig  extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenEnhancer jwtTokenEnhancer;
    private final TokenConfig tokenConfig;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomWebResponseExceptionTranslator webResponseExceptionTranslator;

    /**
     * 配置认证客户端
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(AuthMode.CLIENT_ID.getValue())
                .secret(passwordEncoder.encode(AuthMode.CLIENT_SECRET.getValue()))
                .scopes(AuthMode.SCOPES.getValue())
                .authorizedGrantTypes(AuthMode.PASSWORD.getValue(), AuthMode.REFRESH_TOKEN.getValue());
    }

    /**
     * 自定义授权服务配置
     * 使用密码模式需要配置
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

        endpoints.tokenServices(tokenServices());
        endpoints.authenticationManager(authenticationManager);

        //自定义异常转换类(处理grant_type, username, password错误的异常)
        endpoints.exceptionTranslator(webResponseExceptionTranslator);

    }

    /**
     * 自定义授权令牌端点的安全约束
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {

        CustomClientCredentialsTokenEndpointFilter endpointFilter = new CustomClientCredentialsTokenEndpointFilter(security, authenticationEntryPoint);

        //初始化的时候执行
        endpointFilter.afterPropertiesSet();

        //默认"denyAll()"，不允许访问/oauth/check_token；
        //"isAuthenticated()"需要携带auth信息认证访问；
        //"permitAll()"可直接访问

        //添加一个客户端认证之前的过滤器
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                //.allowFormAuthenticationForClients()
                .addTokenEndpointAuthenticationFilter(endpointFilter);

        /*
         * allowFormAuthenticationForClients 的作用:
         * 允许表单认证(申请令牌), 而不仅仅是Basic Auth方式提交, 且url中有client_id和client_secret的会走 ClientCredentialsTokenEndpointFilter 来保护，
         * 也就是在 BasicAuthenticationFilter 之前添加 ClientCredentialsTokenEndpointFilter，使用 ClientDetailsService 来进行 client 端登录的验证。
         * 但是，在使用自定义的 CustomClientCredentialsTokenEndpointFilter 时,
         * 会导致 oauth2 仍然使用 allowFormAuthenticationForClients 中默认的 ClientCredentialsTokenEndpointFilter 进行过滤，致使我们的自定义 CustomClientCredentialsTokenEndpointFilter 不生效。
         * 因此在使用 CustomClientCredentialsTokenEndpointFilter 时，不再需要开启 allowFormAuthenticationForClients() 功能。
         */

//        security.allowFormAuthenticationForClients();
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair());
        return jwtAccessTokenConverter;
    }

    @Bean
    public KeyPair keyPair() {
        return JwksUtils.generateRsaKey();
    }

    /**
     * tokenService 配置
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        // 允许支持refreshToken
        tokenServices.setSupportRefreshToken(true);
        // refreshToken 不重用策略
        tokenServices.setReuseRefreshToken(false);
        tokenServices.setAccessTokenValiditySeconds(tokenConfig.getAccessTokenValiditySeconds());
        tokenServices.setRefreshTokenValiditySeconds(tokenConfig.getRefreshTokenValiditySeconds());
        //设置Token存储方式
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setTokenEnhancer(tokenEnhancerChain());
        return tokenServices;
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    /**
     * 自定义TokenEnhancerChain 由多个TokenEnhancer组成
     */
    @Bean
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter(), jwtTokenEnhancer));
        return tokenEnhancerChain;
    }





}
