package com.onhands.technicaltest.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
public class Todo {
	
	@Id
	@GeneratedValue
	private long id;
	
	@NotEmpty(message = "Title is required")
	private String title;

	private String description;
	
	private String username;
	
	private boolean isCompleted;
	
	protected Todo() {
		
	}
	
	public Todo(String title, String description, String username) {
		super();
		this.title = title;
		this.description = description;
		this.username = username;
		this.isCompleted = false;
	}
	
	public long getId() {
		return id;
	}

	public void setIdd(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public boolean getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	@Override
	public String toString() {
		return "Todo [id=" + id + ", title=" + title + ", description=" + description + ", username=" + username
				+ ", isCompleted=" + isCompleted + "]";
	}
	
}