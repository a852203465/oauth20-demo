package cn.darkjrong.authserver.config;

import cn.darkjrong.oauth.common.vo.ResponseVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.cors.CorsUtils;

/**
 * Security配置
 *
 * @author Rong.Jia
 * @date 2022/08/13
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .antMatchers("/rsa/publicKey", "/oauth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .permitAll()
                .failureHandler(authenticationFailureHandler())
                .and().headers().frameOptions().disable();

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        auth.eraseCredentials(true);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 常规登录失败处理器
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (httpServletRequest, httpServletResponse, e) -> {
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            ResponseVO resp = new ResponseVO(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(resp));
        };
    }

}
