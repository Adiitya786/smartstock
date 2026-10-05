package com.smartstock.config;

import com.smartstock.Service.RateLimitService;
import com.smartstock.model.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;

    public RateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Authentication endpoints are handled separately
        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String resource;
        int maxRequests;

        if (path.startsWith("/api/products")) {
            resource = "products";
            maxRequests = 60;

        } else if (path.startsWith("/api/orders")) {
            resource = "orders";
            maxRequests = 20;

        } else if (path.startsWith("/api/payment")) {
            resource = "payment";
            maxRequests = 10;

        } else {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication != null &&
                authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof User) {

            User user =
                    (User) authentication.getPrincipal();

            Long userId = user.getId();

            boolean allowed =
                    rateLimitService.isAllowed(
                            userId,
                            resource,
                            maxRequests,
                            60
                    );

            if (!allowed) {

                response.setStatus(429);

                response.getWriter().write(
                        "Too many requests. Please try again later."
                );

                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}