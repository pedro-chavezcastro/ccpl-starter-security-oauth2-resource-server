package com.ccplsolutions.startersecurityoauth2resourceserver.config;

import com.ccplsolutions.startersecurityoauth2resourceserver.convertor.CustomJwtAuthenticationConverter;
import com.ccplsolutions.startersecurityoauth2resourceserver.model.HttpSecurityProperties;
import com.ccplsolutions.startersecurityoauth2resourceserver.web.BearerTokenAuthenticationEntryPoint;
import com.ccplsolutions.startersecurityoauth2resourceserver.web.access.BearerTokenAccessDeniedHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(HttpSecurityProperties.class)
public class HttpSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private HttpSecurityProperties properties;
    @Autowired
    private CustomJwtAuthenticationConverter jwtAuthenticationConverter;
    @Autowired
    private BearerTokenAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private BearerTokenAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private MethodInterceptor methodInterceptor;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        if (properties.isEnable()) {
            http.authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .oauth2ResourceServer()
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
                    .jwt()
                    .jwkSetUri(properties.getJwkSetUri())
                    .jwtAuthenticationConverter(jwtAuthenticationConverter);
        } else {
            http.authorizeRequests()
                    .anyRequest()
                    .permitAll();
        }
    }

    @PostConstruct
    public void configureAuthorization() {

        if (!properties.isEnable()) {
            Optional.ofNullable(methodInterceptor)
                    .map(MethodSecurityInterceptor.class::cast)
                    .map(MethodSecurityInterceptor::getAccessDecisionManager)
                    .map(AffirmativeBased.class::cast)
                    .map(mapper -> {
                        mapper.setAllowIfAllAbstainDecisions(true);
                        return mapper;
                    })
                    .map(AffirmativeBased::getDecisionVoters)
                    .ifPresent(accessDecisionVoters -> accessDecisionVoters.removeIf(voter -> voter instanceof PreInvocationAuthorizationAdviceVoter));
        }

    }

}
