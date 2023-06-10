package com.ccplsolutions.startersecurityoauth2resourceserver.web;

import com.ccplsolutions.startersecurityoauth2resourceserver.model.ErrorDto;
import com.ccplsolutions.startersecurityoauth2resourceserver.model.ResourceServerErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class BearerTokenAuthenticationEntryPointTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private BearerTokenAuthenticationEntryPoint entryPoint;

    @Test
    void testCommence() throws IOException {
        // Given
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        //When
        Mockito.when(request.getMethod()).thenReturn("GET");
        Mockito.when(request.getRequestURI()).thenReturn("/api/resource");
        Mockito.when(response.getWriter()).thenReturn(printWriter);
        entryPoint.commence(request, response, null);

        // Then
        Mockito.verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        Mockito.verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);

        printWriter.flush();
        String responseContent = stringWriter.toString();
        assertNotNull(responseContent);

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(responseContent, ErrorDto.class);

        assertNotNull(errorDto);
        assertEquals(ResourceServerErrorCode.AUTHENTICATION_ERROR.getCode(), errorDto.getCode());
        assertEquals(ResourceServerErrorCode.AUTHENTICATION_ERROR.getDescription(), errorDto.getMessage());
        assertNotNull(errorDto.getTimeStamp());
        assertEquals(Arrays.asList("GET", "/api/resource"), errorDto.getMetadata());
    }
}