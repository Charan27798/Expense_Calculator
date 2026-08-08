package Expense_Calculator.Exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        System.out.println("JwtAccessDeniedHandler called");


        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        response.setContentType("application/json");

        Map<String, Object> error = new HashMap<>();

        error.put("status", 403);
        error.put("message", "Access Denied");
        error.put("path", request.getRequestURI());

        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}