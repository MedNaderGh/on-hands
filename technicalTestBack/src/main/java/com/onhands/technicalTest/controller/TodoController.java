package com.onhands.technicaltest.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.onhands.technicaltest.errorhandler.CustomException;
import com.onhands.technicaltest.model.Todo;
import com.onhands.technicaltest.service.TodoService;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin(origins = "*", allowCredentials = "true")
@ApiResponses(value = {
        @ApiResponse(code=400, message = "Bad Request", response = CustomException.class),
        @ApiResponse(code=401, message = "Unauthorized", response = CustomException.class),
        @ApiResponse(code=403, message = "Forbidden", response = CustomException.class),
        @ApiResponse(code=404, message = "Not Found", response = CustomException.class)
 })
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // Create a new todo
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Todo> create(@Valid @RequestBody TodoCreateRequest todoCreateRequest, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.create(todoCreateRequest, principal.getName()));
    }

    // Get all todos
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Todo>> readAll(Principal principal, @RequestParam(required = false) String isCompleted) {
        List<Todo> todos;
        if (isCompleted != null) {
            todos = todoService.readAllByIsCompleted(principal.getName(), isCompleted);
        } else {
            todos = todoService.readAll(principal.getName());
        }
        return ResponseEntity.status(HttpStatus.OK).body(todos);
    }

    // Get count of all todos
    @GetMapping("/count")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CountResponse> countAll(Principal principal, @RequestParam(required = false) String isCompleted) {
        CountResponse countResponse;
        if (isCompleted != null) {
            countResponse = todoService.countAllByIsCompleted(principal.getName(), isCompleted);
        } else {
            countResponse = todoService.countAll(principal.getName());
        }
        return ResponseEntity.status(HttpStatus.OK).body(countResponse);
    }

    // Get all todos in a pageable format
    @GetMapping("/{pageNumber}/{pageSize}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Todo>> readAllPageable(Principal principal, @PathVariable String pageNumber, @PathVariable String pageSize, @RequestParam(required = false) String isCompleted) {
        List<Todo> todos;
        if (isCompleted != null) {
            todos = todoService.readAllByIsCompletedPageable(principal.getName(), isCompleted, pageNumber, pageSize);
        } else {
            todos = todoService.readAllPageable(principal.getName(), pageNumber, pageSize);
        }
        return ResponseEntity.status(HttpStatus.OK).body(todos);
    }

    // Get a specific todo by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Todo> read(@PathVariable long id, Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.readById(id, principal.getName()));
    }

    // Mark a todo as complete
    @PutMapping("/{id}/markcomplete")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Todo> markComplete(@PathVariable long id, Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.markCompleteById(id, principal.getName()));
    }

    // Update a todo by id
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Todo> update(@PathVariable long id, @Valid @RequestBody TodoUpdateRequest todoUpdateRequest, Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.updateById(id, todoUpdateRequest, principal.getName()));
    }

    // Delete a todo by id
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> delete(@PathVariable long id, Principal principal) {
        todoService.deleteById(id, principal.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
