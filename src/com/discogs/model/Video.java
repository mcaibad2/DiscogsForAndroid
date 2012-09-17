package com.discogs.model;

public class Video 
{
	private String uri;
	private int duration;
	private String description;
	private String title;
	
	public String getUri() 
	{
		return uri;
	}
	
	public void setUri(String uri) 
	{
		this.uri = uri;
	}
	
	public int getDuration() 
	{
		return duration;
	}
	
	public void setDuration(int duration) 
	{
		this.duration = duration;
	}
	
	public String getDescription() 
	{
		return description;
	}
	
	public void setDescription(String description) 
	{
		this.description = description;
	}
	
	public String getTitle() 
	{
		return title;
	}
	
	public void setTitle(String title) 
	{
		this.title = title;
	}
}
