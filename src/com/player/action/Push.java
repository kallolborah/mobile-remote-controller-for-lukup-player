package com.player.action;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Player")
public class Push extends ParseObject{
	public Push(){
	}
	
	public boolean isCompleted(){
		return getBoolean("completed");
	}
	
	public void setCompleted(boolean complete){
		put("completed", complete);
	}
	
	public String getDevice(){
		return getString("channel");
	}
	
	public void setDevice(String model){
		put("channel", model);
	}
	
	public String getUser(){
		return getString("subscriberid");
	}

	public void setUser(String currentUser) {
		put("subscriberid", currentUser);
	}
	
	public String getEnvironment(){
		return getString("environment");
	}
	
	public void setEnvironment(String env){
		put("environment", env);
	}
}
