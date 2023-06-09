package com.ccplsolutions.startersecurityoauth2resourceserver.autoconfigure;

import com.ccplsolutions.startersecurityoauth2resourceserver.convertor.CustomJwtAuthenticationConverter;
import com.ccplsolutions.startersecurityoauth2resourceserver.web.BearerTokenAuthenticationEntryPoint;
import com.ccplsolutions.startersecurityoauth2resourceserver.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResourceServerAutoConfiguration {

    @Bean
    public CustomJwtAuthenticationConverter jwtAuthenticationConverter() {
        return new CustomJwtAuthenticationConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public BearerTokenAuthenticationEntryPoint authenticationEntryPoint() {
        return new BearerTokenAuthenticationEntryPoint();
    }

    @Bean
    @ConditionalOnMissingBean
    public BearerTokenAccessDeniedHandler accessDeniedHandler() {
        return new BearerTokenAccessDeniedHandler();
    }

}
