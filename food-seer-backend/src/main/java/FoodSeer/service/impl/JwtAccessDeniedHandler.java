package FoodSeer.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Handles access denied (authorization) errors by returning a 403 Forbidden status.
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {
        try {
            response.resetBuffer();
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(String.format(
                "{\"timestamp\":\"%s\",\"status\":403,\"error\":\"Forbidden\",\"message\":\"%s\",\"path\":\"%s\"}",
                java.time.OffsetDateTime.now(),
                accessDeniedException.getMessage(),
                request.getRequestURI()
            ));
            response.flushBuffer();
        } catch (Exception ex) {
            // If we can't write the response, log the error and rethrow
            ex.printStackTrace();
            throw new IOException("Failed to write access denied response", ex);
        }
    }
}
