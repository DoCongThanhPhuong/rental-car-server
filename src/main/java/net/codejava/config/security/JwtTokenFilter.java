package net.codejava.config.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.codejava.utility.JwtTokenUtil;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final CustomUserDetailService customUserDetailService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final String BEARER_TOKEN = "Bearer ";
    private final List<String> byPassEndpoints = Arrays.asList(SecurityConfig.PUBLIC_ENDPOINTS);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER_TOKEN)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (this.isByPassRequest(request)) {
            filterChain.doFilter(request, response);
        }
        final String jwt = authHeader.substring(BEARER_TOKEN.length());
        if (SecurityContextHolder.getContext().getAuthentication() == null && jwtTokenUtil.validateToken(jwt)) {
            final String email = jwtTokenUtil.getSubject(jwt);
            if (email != null) {
                UserDetails userDetails = customUserDetailService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private Boolean isByPassRequest(HttpServletRequest request) {
        return byPassEndpoints.contains(request.getRequestURI());
    }
}
