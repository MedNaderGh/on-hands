package com.onhands.technicaltest.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.todolist.model.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    // Method to find all todos by username
    List<Todo> findAllByUsername(String username);

    // Method to find all todos by username and completion status
    List<Todo> findAllByUsernameAndIsCompleted(String username, boolean isCompleted);
    
    // Method to find a todo by username and id
    Todo findByUsernameAndId(String username, long Id);
    
    // Method to count todos by username
    Long countByUsername(String username);
    
    // Method to count todos by username and completion status
    Long countByUsernameAndIsCompleted(String username, boolean isCompleted);
}