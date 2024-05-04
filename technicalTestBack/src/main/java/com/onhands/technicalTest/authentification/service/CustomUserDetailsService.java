package com.onhands.technicaltest.authentification.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.onhands.technicaltest.authentification.model.User;
import com.onhands.technicaltest.authentification.repository.UserRepository;

@Component
// CustomUserDetailsService class that implements UserDetailsService interface
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository; // UserRepository instance
    
    // Constructor for CustomUserDetailsService
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository; // Initialize UserRepository
    }
    
    // Method to load user by username
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username); // Find user by username
        if (user == null) throw new UsernameNotFoundException("Expired or invalid JWT token"); // Throw exception if user not found

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(); // Initialize list of granted authorities
        for (String role : user.getRoleAsList()){ // Loop through user roles
            grantedAuthorities.add(new SimpleGrantedAuthority(role)); // Add each role as a granted authority
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities); // Return UserDetails object
    }
}