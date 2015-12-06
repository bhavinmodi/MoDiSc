package com.example.modisc;

public class DeveloperObject {

	private String email, name, goal, todaysGoal, obstacle;
	private int group, status;
	
	public DeveloperObject(String email, String name, int group, String goal, String todaysGoal, String obstacle, int status){
		this.email = email;
		this.name = name;
		this.group = group;
		this.goal = goal;
		this.todaysGoal = todaysGoal;
		this.obstacle = obstacle;
		this.status = status;
	}
	
	protected void setEmail(String email){
		this.email = email;
	}
	
	protected String getEmail(){
		return this.email;
	}
	
	protected void setName(String name){
		this.name = name;
	}
	
	protected String getName(){
		return this.name;
	}
	
	protected void setGroup(int group){
		this.group = group;
	}
	
	protected int getGroup(){
		return this.group;
	}
	
	protected void setGoal(String goal){
		this.goal = goal;
	}
	
	protected String getGoal(){
		return this.goal;
	}
	
	protected void setTodaysGoal(String todaysGoal){
		this.todaysGoal = todaysGoal;
	}
	
	protected String getTodaysGoal(){
		return this.todaysGoal;
	}
	
	protected void setObstacle(String obstacle){
		this.obstacle = obstacle;
	}
	
	protected String getObstacle(){
		return this.obstacle;
	}
	
	protected void setStatus(int status){
		this.status = status;
	}
	
	protected int getStatus(){
		return this.status;
	}
	
}

