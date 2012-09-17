package com.discogs.model;

public class MasterRelease 
{
	private String title;
	private String thumb;
	private String country;
	private String format;
	private String label;
	private String released;
	private String catno;
	private String resourceUrl;
	private String artist;
	
	public String getArtist() 
	{
		return artist;
	}

	public void setArtist(String artist) 
	{
		this.artist = artist;
	}

	public String getTitle() 
	{
		return title;
	}
	
	public void setTitle(String title) 
	{
		this.title = title;
	}
	
	public String getThumb() 
	{
		return thumb;
	}
	
	public void setThumb(String thumb) 
	{
		this.thumb = thumb;
	}
	
	public String getCountry() 
	{
		return country;
	}
	
	public void setCountry(String country) 
	{
		this.country = country;
	}
	
	public String getFormat() 
	{
		return format;
	}
	
	public void setFormat(String format) 
	{
		this.format = format;
	}
	
	public String getLabel() 
	{
		return label;
	}
	
	public void setLabel(String label) 
	{
		this.label = label;
	}
	
	public String getReleased() 
	{
		return released;
	}
	
	public void setReleased(String released) 
	{
		this.released = released;
	}
	
	public String getCatno() 
	{
		return catno;
	}
	
	public void setCatno(String catno) 
	{
		this.catno = catno;
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
