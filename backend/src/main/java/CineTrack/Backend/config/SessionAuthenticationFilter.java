package CineTrack.Backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class SessionAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String sessionUserId = null;

        if (session != null) {
            Object storedId = session.getAttribute("authenticatedUserId");
            if (storedId instanceof String) {
                sessionUserId = (String) storedId;
            }
        }

        if (sessionUserId != null && !sessionUserId.isBlank()) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            sessionUserId,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        String path = request.getServletPath();

        if (path.startsWith("/api/users/") && sessionUserId != null) {
            String remainder = path.substring("/api/users/".length());
            int slash = remainder.indexOf('/');
            String requestedUserId = slash >= 0
                    ? remainder.substring(0, slash)
                    : remainder;

            if (!sessionUserId.equals(requestedUserId)) {
                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "You cannot access another user's data."
                );
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}