package com.onhands.technicaltest.repository;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.backend.todolist.model.Todo;

@Repository
public interface TodoPagingRepository extends PagingAndSortingRepository<Todo, Long> {
// Method to find all todos by username with pagination
List<Todo> findAllByUsername(String username, Pageable pageable);

// Method to find all todos by username and completion status with pagination
List<Todo> findAllByUsernameAndIsCompleted(String username, boolean isCompleted, Pageable pageable);
}