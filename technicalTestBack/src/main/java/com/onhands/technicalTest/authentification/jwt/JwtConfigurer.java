package com.onhands.technicaltest.authentification.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures JWT authentication in the security filter chain.
 */
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private JwtTokenGenerator jwtTokenGenerator;
    
    /**
     * Constructor for JwtConfigurer.
     * 
     * @param jwtTokenGenerator the JWT token generator
     */
    public JwtConfigurer(JwtTokenGenerator jwtTokenGenerator) {
        this.jwtTokenGenerator = jwtTokenGenerator;
    }
    
    /**
     * Configures JWT token filter in the security filter chain.
     * 
     * @param httpSecurity the http security object
     */
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenGenerator);
        httpSecurity.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}