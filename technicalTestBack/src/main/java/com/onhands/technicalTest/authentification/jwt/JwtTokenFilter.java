package com.onhands.technicaltest.authentification.jwt;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onhands.technicaltest.errorhandler.CustomException;

/**
 * Filter for processing JWT tokens in the request.
 */
public class JwtTokenFilter extends GenericFilterBean {
    private JwtTokenGenerator jwtTokenGenerator;

    /**
     * Constructor for JwtTokenFilter.
     * 
     * @param jwtTokenGenerator the JWT token generator
     */
    public JwtTokenFilter(JwtTokenGenerator jwtTokenGenerator) {
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    /**
     * Performs the JWT token validation and sets the authentication in the SecurityContextHolder.
     * 
     * @param req the servlet request
     * @param res the servlet response
     * @param filterChain the filter chain
     * @throws IOException if an I/O error occurs
     * @throws ServletException if a servlet-specific error occurs
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            String token = jwtTokenGenerator.resolveToken((HttpServletRequest) req);
            if (token != null && jwtTokenGenerator.validateToken(token)) {
                Authentication auth = jwtTokenGenerator.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(req, res);
        } catch (Exception ex) {
            sendErrorResponse(HttpStatus.BAD_REQUEST, (HttpServletResponse) res, ex);
        }

    }

    /**
     * Sends an error response with the specified status and exception details.
     * 
     * @param status the HTTP status
     * @param response the HTTP servlet response
     * @param ex the exception
     */
    public void sendErrorResponse(HttpStatus status, HttpServletResponse response, Exception ex) {
        response.setStatus(status.value());
        response.setContentType("application/json");

        CustomException customException = new CustomException(LocalDateTime.now(), status, ex.getMessage());

        try {
            response.getWriter().write(new ObjectMapper().writeValueAsString(customException));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}