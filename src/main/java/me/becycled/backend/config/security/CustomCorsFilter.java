package me.becycled.backend.config.security;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.*;

/**
 * @author I1yi4
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class CustomCorsFilter extends OncePerRequestFilter {

    private static final String ALLOW_ORIGIN = "*";
    private static final String ALLOW_METHODS = "GET, POST, PUT, DELETE, OPTIONS";
    private static final String ALLOW_HEADERS = "*";
    private static final String ALLOW_CREDENTIALS = "true";
    private static final String MAX_AGE = "3600";

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        response.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ALLOW_ORIGIN);

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            response.addHeader(ACCESS_CONTROL_ALLOW_METHODS, ALLOW_METHODS);
            response.addHeader(ACCESS_CONTROL_ALLOW_HEADERS, ALLOW_HEADERS);
            response.addHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, ALLOW_CREDENTIALS);
            response.addHeader(ACCESS_CONTROL_MAX_AGE, MAX_AGE);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
