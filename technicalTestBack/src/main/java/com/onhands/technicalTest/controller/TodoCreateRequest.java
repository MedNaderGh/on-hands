package com.onhands.technicaltest.controller;


import javax.validation.constraints.NotEmpty;

public class TodoCreateRequest {
	@NotEmpty(message = "Title is required")
	private String title;

	private String description;
	
	protected TodoCreateRequest() {
		
	}

	public TodoCreateRequest(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setTargetDate(String description) {
		this.description = description;
	}
}
