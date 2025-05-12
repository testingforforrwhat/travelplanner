package com.test.travelplanner.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";


    private final JwtHandler jwtHandler;
    private final UserDetailsService userDetailsService;


    public JwtAuthenticationFilter(
            JwtHandler jwtHandler,
            UserDetailsService userDetailsService
    ) {
        this.jwtHandler = jwtHandler;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String jwt = getJwtFromRequest(request);
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }
        final String username = jwtHandler.parsedUsername(jwt);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }


    /**
     *
     * 收到请求自动拽取并转发 token,校验 header 里的 token 是否有效
     * 若 token 非法/过期/用户已被拉黑，直接返回 401/403
     * 若 token 合法，再进一步做权限验证
     *
     * @param request
     * @return
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER);
        if (bearerToken == null || !bearerToken.startsWith(PREFIX)) {
            return null;
        }
        return bearerToken.substring(7);
    }
}