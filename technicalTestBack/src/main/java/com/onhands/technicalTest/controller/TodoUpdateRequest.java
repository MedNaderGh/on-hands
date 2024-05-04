package com.onhands.technicaltest.controller;


import javax.validation.constraints.NotEmpty;

public class TodoUpdateRequest {
	@NotEmpty(message = "Title is required")
	private String title;

	private String description;
	
	protected TodoUpdateRequest() {
		
	}

	public TodoUpdateRequest(String title, String description) {
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

	public void setDescription(String description) {
		this.description = description;
	}
}
