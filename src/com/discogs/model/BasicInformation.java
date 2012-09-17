package com.discogs.model;

import java.util.List;

public class BasicInformation 
{
	private String thumb;
	private String title;
	private String year;
	private String resourceUrl;
	private long id;
	private List<Format> formats;
	private List<Label> labels;
	private List<Artist> artists;
	
	public String getThumb() 
	{
		return thumb;
	}
	
	public void setThumb(String thumb) 
	{
		this.thumb = thumb;
	}
	
	public String getTitle() 
	{
		return title;
	}
	
	public void setTitle(String title) 
	{
		this.title = title;
	}
	
	public String getYear() 
	{
		return year;
	}
	
	public void setYear(String year) 
	{
		this.year = year;
	}
	
	public String getResourceUrl() 
	{
		return resourceUrl;
	}
	
	public void setResourceUrl(String resourceUrl) 
	{
		this.resourceUrl = resourceUrl;
	}
	
	public long getId() 
	{
		return id;
	}
	
	public void setId(long id) 
	{
		this.id = id;
	}
	
	public List<Format> getFormats() 
	{
		return formats;
	}
	
	public void setFormats(List<Format> formats) 
	{
		this.formats = formats;
	}
	
	public List<Label> getLabels() 
	{
		return labels;
	}
	
	public void setLabels(List<Label> labels) 
	{
		this.labels = labels;
	}
	
	public List<Artist> getArtists() 
	{
		return artists;
	}
	
	public void setArtists(List<Artist> artists) 
	{
		this.artists = artists;
	}
}
