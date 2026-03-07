package com.baby_io.baby_io_app.component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");
    String token = null;
    Long userId = null;

    // Extract token from Authorization header
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7);
      try {
        if (jwtUtil.validateToken(token)) {
          userId = jwtUtil.getUserIdFromToken(token);
        }
      } catch (Exception e) {
        logger.error("Cannot set user authentication: {}", e);
      }
    }

    // Set user ID in request attribute for controllers to use
    if (userId != null) {
      request.setAttribute("userId", userId);
    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();
    return path.equals("/api/auth/login") ||
        path.equals("/api/auth/signup") ||
        path.startsWith("/status"); // Allow ESP32 status endpoint without auth
  }
}