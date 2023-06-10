package com.ccplsolutions.startersecurityoauth2resourceserver.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String ROLE_AUTHORITY_PREFIX = "ROLE_";
    private static final String SCOPE_ATTRIBUTE_NAME = "scope";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = getScopes(jwt)
                .stream()
                .map(role -> ROLE_AUTHORITY_PREFIX + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new JwtAuthenticationToken(jwt, simpleGrantedAuthorities);

    }

    private Collection<String> getScopes(Jwt jwt) {
        Object scopes = jwt.getClaims().get(SCOPE_ATTRIBUTE_NAME);
        return Arrays.asList(((String) scopes).split(" "));
    }
}
