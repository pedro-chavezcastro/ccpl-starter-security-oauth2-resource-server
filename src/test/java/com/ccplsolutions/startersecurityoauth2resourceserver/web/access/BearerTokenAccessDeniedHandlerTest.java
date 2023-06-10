package com.ccplsolutions.startersecurityoauth2resourceserver.web.access;

import com.ccplsolutions.startersecurityoauth2resourceserver.model.ErrorDto;
import com.ccplsolutions.startersecurityoauth2resourceserver.model.ResourceServerErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BearerTokenAccessDeniedHandlerTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private BearerTokenAccessDeniedHandler accessDeniedHandler;

    @Test
    void testHandle() throws IOException, ServletException {
        // Given
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        //When
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/resource");
        when(response.getWriter()).thenReturn(printWriter);
        accessDeniedHandler.handle(request, response, null);

        // Then
        verify(response).setStatus(HttpStatus.FORBIDDEN.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);

        printWriter.flush();
        String responseContent = stringWriter.toString();
        assertNotNull(responseContent);

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = objectMapper.readValue(responseContent, ErrorDto.class);

        assertNotNull(errorDto);
        assertEquals(ResourceServerErrorCode.ACCESS_DENIED.getCode(), errorDto.getCode());
        assertEquals(ResourceServerErrorCode.ACCESS_DENIED.getDescription(), errorDto.getMessage());
        assertNotNull(errorDto.getTimeStamp());
        assertEquals(Arrays.asList("GET", "/api/resource"), errorDto.getMetadata());
    }
}