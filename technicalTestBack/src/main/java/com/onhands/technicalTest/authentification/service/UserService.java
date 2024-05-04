package com.onhands.technicaltest.authentification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onhands.technicaltest.authentification.controller.UserSigninRequest;
import com.onhands.technicaltest.authentification.controller.UserSigninResponse;
import com.onhands.technicaltest.authentification.controller.UserSignupRequest;
import com.onhands.technicaltest.authentification.controller.UserSignupResponse;
import com.onhands.technicaltest.authentification.jwt.JwtTokenGenerator;
import com.onhands.technicaltest.authentification.model.User;
import com.onhands.technicaltest.authentification.repository.UserRepository;
import com.onhands.technicaltest.errorhandler.BadRequestException;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository; // Autowired UserRepository
    
    @Autowired
    AuthenticationManager authenticationManager; // Autowired AuthenticationManager
    
    @Autowired
    PasswordEncoder passwordEncoder; // Autowired PasswordEncoder
    
    @Autowired
    JwtTokenGenerator jwtTokenGenerator; // Autowired JwtTokenGenerator
    
    // Method to handle user signup
    public UserSignupResponse signup(UserSignupRequest userSignupRequest) {
        try {
            String username = userSignupRequest.getUsername(); // Get username from request
            String password = userSignupRequest.getPassword(); // Get password from request
            
            User user =  userRepository.findByUsername(username); // Find user by username
            if(user != null) {
                throw new BadRequestException("Username is already exist"); // Throw exception if username already exists
            }
            
            User _user = new User(username, passwordEncoder.encode(password)); // Create new user with encoded password
            _user = userRepository.save(_user); // Save user
            
            String token = jwtTokenGenerator.createToken(_user.getUsername(), _user.getRoleAsList()); // Create JWT token
            
            return new UserSignupResponse(username, token); // Return signup response
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password"); // Handle authentication exception
        }
    }
    
    // Method to handle user signin
    public UserSigninResponse signin(UserSigninRequest userSigninRequest) {
        try {
            String username = userSigninRequest.getUsername(); // Get username from request
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userSigninRequest.getPassword())); // Authenticate user
            
            String token = jwtTokenGenerator.createToken(username, this.userRepository.findByUsername(username).getRoleAsList()); // Create JWT token
            
            return new UserSigninResponse(username, token); // Return signin response
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password"); // Handle authentication exception
        }
    }
}