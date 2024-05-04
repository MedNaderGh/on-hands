package com.onhands.technicaltest.authentification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onhands.technicaltest.authentification.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}