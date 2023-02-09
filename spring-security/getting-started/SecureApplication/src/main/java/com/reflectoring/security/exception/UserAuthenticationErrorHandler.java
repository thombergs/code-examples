package com.reflectoring.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UserAuthenticationErrorHandler extends BasicAuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    public UserAuthenticationErrorHandler() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException ex) throws IOException {
        /*response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println(objectMapper.writeValueAsString(CommonException.unauthorized()));
    */
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");

        final PrintWriter writer = response.getWriter();
        writer.println("HTTP Status 401 : " + ex.getMessage());
    }

}
