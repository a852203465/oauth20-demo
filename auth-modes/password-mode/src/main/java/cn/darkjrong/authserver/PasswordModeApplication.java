package cn.darkjrong.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@EnableAuthorizationServer
@EnableWebSecurity
@EnableEurekaClient
@EnableCaching
@SpringBootApplication
public class PasswordModeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PasswordModeApplication.class, args);
    }

}
