package com.ccplsolutions.startersecurityoauth2resourceserver.web;

import com.ccplsolutions.startersecurityoauth2resourceserver.model.ErrorDto;
import com.ccplsolutions.startersecurityoauth2resourceserver.model.ResourceServerErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

public class BearerTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ErrorDto errorDto = ErrorDto.builder()
                .code(ResourceServerErrorCode.AUTHENTICATION_ERROR.getCode())
                .message(ResourceServerErrorCode.AUTHENTICATION_ERROR.getDescription())
                .timeStamp(LocalDateTime.now().toString())
                .metadata(Arrays.asList(request.getMethod(), request.getRequestURI()))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String errorJson = objectMapper.writeValueAsString(errorDto);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(errorJson);
    }
}

