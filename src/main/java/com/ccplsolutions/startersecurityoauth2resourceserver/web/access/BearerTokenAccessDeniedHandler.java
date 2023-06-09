package com.ccplsolutions.startersecurityoauth2resourceserver.web.access;

import com.ccplsolutions.startersecurityoauth2resourceserver.model.ErrorDto;
import com.ccplsolutions.startersecurityoauth2resourceserver.model.ResourceServerErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

public class BearerTokenAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ErrorDto errorDto = ErrorDto.builder()
                .code(ResourceServerErrorCode.ACCESS_DENIED.getCode())
                .message(ResourceServerErrorCode.ACCESS_DENIED.getDescription())
                .timeStamp(LocalDateTime.now().toString())
                .metadata(Arrays.asList(request.getMethod(), request.getRequestURI()))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String errorJson = objectMapper.writeValueAsString(errorDto);

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(errorJson);
    }

}
