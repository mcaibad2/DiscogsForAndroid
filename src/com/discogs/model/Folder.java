package com.discogs.model;

public class Folder 
{
	private long id;
	private int count;
	private String name;
	private String resourceUrl;
	
	public long getId() 
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public int getCount() 
	{
		return count;
	}
	
	public void setCount(int count) 
	{
		this.count = count;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getResourceUrl() 
	{
		return resourceUrl;
	}
	
	public void setResourceUrl(String resourceUrl) 
	{
		this.resourceUrl = resourceUrl;
	}
}
