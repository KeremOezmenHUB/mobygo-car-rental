package com.mobygo.carrental.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Reads a "Authorization: Bearer &lt;jwt&gt;" header, validates the token, and
 * populates the SecurityContext with the user's role authority. If no Bearer
 * token is present the chain continues, so HTTP Basic Auth still works as a
 * fallback (required baseline per the assessment).
 *
 * Deliberately NOT a @Component: it is wired into the security filter chain
 * manually in SecurityConfig, which avoids Spring Boot also auto-registering it
 * as a plain servlet filter (double registration breaks the security context).
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtService.isValid(token)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = jwtService.getUsername(token);
                String role = jwtService.getRole(token);
                var authority = new SimpleGrantedAuthority("ROLE_" + role);
                var authentication = new UsernamePasswordAuthenticationToken(
                    username, null, List.of(authority));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Also run on the container's ERROR dispatch (e.g. when an access-denied
     * triggers response.sendError -> /error). Without this the token would be
     * ignored on that re-dispatch and an authenticated-but-forbidden user would
     * wrongly get 401 instead of 403. Mirrors BasicAuthenticationFilter.
     */
    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }
}
