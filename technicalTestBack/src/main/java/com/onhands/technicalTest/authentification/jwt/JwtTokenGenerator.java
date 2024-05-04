package com.onhands.technicaltest.authentification.jwt;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.onhands.technicaltest.authentification.service.CustomUserDetailsService;
import com.onhands.technicaltest.errorhandler.InvalidJwtAuthenticationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Generates and processes JWT tokens.
 */
@Component
public class JwtTokenGenerator {
    private String secretKey = "onhandssecret";
    
    private long validityInMilliseconds = 3600000; // 1h
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    
    /**
     * Creates a JWT token for the specified username and roles.
     * 
     * @param username the username
     * @param roles the roles
     * @return the JWT token
     */
    public String createToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }
    
    /**
     * Retrieves the authentication details from the token.
     * 
     * @param token the JWT token
     * @return the authentication object
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    
    /**
     * Extracts the username from the token.
     * 
     * @param token the JWT token
     * @return the username
     */
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
    
    /**
     * Resolves the JWT token from the request.
     * 
     * @param req the HTTP servlet request
     * @return the JWT token
     */
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    /**
     * Validates the JWT token.
     * 
     * @param token the JWT token
     * @return true if the token is valid, false otherwise
     * @throws InvalidJwtAuthenticationException if the token is expired or invalid
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return claims.getBody().getExpiration().after(new Date());
            
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
        }
    }
}