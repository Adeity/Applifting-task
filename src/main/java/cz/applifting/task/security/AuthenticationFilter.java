package cz.applifting.task.security;

import cz.applifting.task.exceptions.AuthenticationException;
import cz.applifting.task.security.model.AuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String appliftingAuth = request.getHeader("Applifting-Authorization");
        if (appliftingAuth == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Missing authorization header");
            return;
        }

        logger.debug(appliftingAuth);


        // Create our Authentication and let Spring know about it
        Authentication auth = new AuthenticationToken(new ArrayList<>(), appliftingAuth);
        auth.setAuthenticated(true);

        final SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(auth);

        SecurityContextHolder.setContext(context);

        filterChain.doFilter(request, response);
    }
}
