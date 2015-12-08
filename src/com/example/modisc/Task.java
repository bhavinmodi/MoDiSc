package com.example.modisc;

public class Task {
	String type;
	int id;
	String name;
	String description;
	int priority;
	String owner;
	String status;

	public Task(String type, int id, String name, String description,
			int priority, String owner, String status) {
		super();
		this.type = type;
		this.id = id;
		this.name = name;
		this.description = description;
		this.priority = priority;
		this.owner = owner;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

}
