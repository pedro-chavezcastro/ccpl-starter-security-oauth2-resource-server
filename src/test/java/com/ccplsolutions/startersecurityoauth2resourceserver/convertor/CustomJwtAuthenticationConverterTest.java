package com.ccplsolutions.startersecurityoauth2resourceserver.convertor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomJwtAuthenticationConverterTest {

    @Mock
    private Jwt jwt;
    @InjectMocks
    private CustomJwtAuthenticationConverter converter;

    @Test
    void convertShouldReturnJwtAuthenticationTokenWithAuthorities() {
        // When
        when(jwt.getClaims()).thenReturn(Collections.singletonMap("scope", "read write"));
        AbstractAuthenticationToken authenticationToken = converter.convert(jwt);

        assertEquals(JwtAuthenticationToken.class, authenticationToken.getClass());

        Collection<? extends GrantedAuthority> authorities = authenticationToken.getAuthorities();
        assertEquals(2, authorities.size());
        assertEquals("ROLE_read", authorities.stream().findFirst().get().getAuthority());
        assertEquals("ROLE_write", authorities.stream().skip(1).findFirst().get().getAuthority());
    }
}