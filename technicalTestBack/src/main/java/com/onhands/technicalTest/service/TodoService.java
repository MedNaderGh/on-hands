package com.onhands.technicaltest.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.onhands.technicaltest.controller.CountResponse;
import com.onhands.technicaltest.controller.TodoCreateRequest;
import com.onhands.technicaltest.controller.TodoUpdateRequest;
import com.onhands.technicaltest.errorhandler.BadRequestException;
import com.onhands.technicaltest.errorhandler.InvalidPageException;
import com.onhands.technicaltest.errorhandler.ResourceNotFoundException;
import com.onhands.technicaltest.model.Todo;
import com.onhands.technicaltest.repository.TodoRepository;
import com.onhands.technicaltest.repository.TodoPagingRepository;

@Service
public class TodoService {
	@Autowired
	private TodoRepository todoRepository;
	
	@Autowired
	private TodoPagingRepository todoPagingRepository;
	// Method to create a new todo
	public Todo create(TodoCreateRequest todoCreateRequest, String username) {
		Todo todo = new Todo(todoCreateRequest.getTitle(), todoCreateRequest.getDescription(), username);
		return todoRepository.save(todo);
	}
	// Method to read a todo by id and username
	public Todo readById(long id, String username) {
		Todo todo = todoRepository.findByUsernameAndId(username, id);
		if(todo == null) {
			throw new ResourceNotFoundException("Todo not found");
		}
		return todo;
	}
	// Method to read all todos by username
	public List<Todo> readAll(String username) {
		return todoRepository.findAllByUsername(username);
	}
	// Method to read all todos by username with pagination
	public List<Todo> readAllPageable(String username, String pageNumber, String pageSize) {
		int _pageNumber = pageNumberStringToInteger(pageNumber);
		int _pageSize = pageSizeStringToInteger(pageSize);
		
		Pageable pageable = PageRequest.of(_pageNumber, _pageSize, Sort.by(Sort.Direction.ASC, "title"));
		return todoPagingRepository.findAllByUsername(username, pageable);
	}
	// Method to read all todos by completion status
	public List<Todo> readAllByIsCompleted(String username, String isCompleted) {
		boolean _isCompleted = isCompletedStringToBoolean(isCompleted);
		return todoRepository.findAllByUsernameAndIsCompleted(username, _isCompleted);
	}
	// Method to read all todos by completion status with pagination
	public List<Todo> readAllByIsCompletedPageable(String username, String isCompleted, String pageNumber, String pageSize) {
		boolean _isCompleted = isCompletedStringToBoolean(isCompleted);
		int _pageNumber = pageNumberStringToInteger(pageNumber);
		int _pageSize = pageSizeStringToInteger(pageSize);
		
		Pageable pageable = PageRequest.of(_pageNumber, _pageSize, Sort.by(Sort.Direction.ASC, "title"));
		return todoPagingRepository.findAllByUsernameAndIsCompleted(username, _isCompleted, pageable);
	}
	// Method to delete a todo by id and username
	public void deleteById(long id, String username) {
		Todo todo = todoRepository.findByUsernameAndId(username, id);
		if(todo == null) {
			throw new ResourceNotFoundException("Todo not found");
		}
		todoRepository.deleteById(id);
	}
	// Method to update a todo by id and username
	public Todo updateById(long id, TodoUpdateRequest todoUpdateRequest, String username) {
		Todo todo = todoRepository.findByUsernameAndId(username, id);
		if(todo == null) {
			throw new ResourceNotFoundException("Todo not found");
		}
		
		todo.setTitle(todoUpdateRequest.getTitle());
		todo.setDescription(todoUpdateRequest.getDescription());
		return todoRepository.save(todo);
	}
	// Method to mark a todo as complete or incomplete by id and username
	public Todo markCompleteById(long id, String username) {
		Todo todo = todoRepository.findByUsernameAndId(username, id);
		if(todo == null) {
			throw new ResourceNotFoundException("Todo not found");
		}
		
		todo.setIsCompleted(!todo.getIsCompleted());
		return todoRepository.save(todo);
	}
	// Method to count all todos by username
	public CountResponse countAll(String username) {
		return new CountResponse(todoRepository.countByUsername(username));
	}
	// Method to count all todos by completion status
	public CountResponse countAllByIsCompleted(String username, String isCompleted) {
		boolean _isCompleted = isCompletedStringToBoolean(isCompleted);
		return new CountResponse(todoRepository.countByUsernameAndIsCompleted(username, _isCompleted));
	}
	// Utility method to convert string to boolean for isCompleted parameter
	private boolean isCompletedStringToBoolean(String isCompleted) {
		try {
			return Boolean.parseBoolean(isCompleted);  
		} catch (Exception e) {
			throw new BadRequestException("Invalid isCompleted");
		}
	}
	// Utility method to convert string to integer for page number parameter
	private int pageNumberStringToInteger(String pageNumber) {
		int _pageNumber;
		
		try {
			_pageNumber = Integer.parseInt(pageNumber);
		} catch(Exception e) {
			throw new InvalidPageException("Invalid Page Number");
		}
		
		if(_pageNumber < 0) {
			throw new InvalidPageException("Invalid page number");
		}
		
		return _pageNumber;
	}
	// Utility method to convert string to integer for page size parameter
	private int pageSizeStringToInteger(String pageSize) {
		int _pageSize;
		
		try {
			_pageSize = Integer.parseInt(pageSize);
		} catch(Exception e) {
			throw new InvalidPageException("Invalid Page Size");
		}
		
		if(_pageSize < 1) {
			throw new InvalidPageException("Invalid page size");
		}
		
		return _pageSize;
	}
}