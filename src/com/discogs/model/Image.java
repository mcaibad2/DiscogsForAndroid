package com.discogs.model;

public class Image 
{
	private String type;
	private String uri;
	private String uri150;
	private String resourceUrl;
	
	public String getType() 
	{
		return type;
	}
	
	public void setType(String type) 
	{
		this.type = type;
	}
	
	public String getUri() 
	{
		return uri;
	}
	
	public void setUri(String uri) 
	{
		this.uri = uri;
	}
	
	public String getUri150() 
	{
		return uri150;
	}
	
	public void setUri150(String uri150) 
	{
		this.uri150 = uri150;
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
