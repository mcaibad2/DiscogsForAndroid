package com.discogs.model;

import java.util.List;

public class Result 
{
	private String thumb;
	private String title;
	private String country;
	private String uri;
	private String label;
	private String year;
	private String type;
	private String catno;
	private long id;
	private String resourceUrl;
	private List<String> styles;
	private List<String> genres;
	private List<String> formats;
	
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
	
	public String getThumb() 
	{
		return thumb;
	}

	public void setThumb(String thumb) 
	{
		this.thumb = thumb;
	}
	
	public List<String> getGenres() 
	{
		return genres;
	}

	public void setGenres(List<String> genres) 
	{
		this.genres = genres;
	}

	public List<String> getStyles() 
	{
		return styles;
	}

	public void setStyles(List<String> styles)
	{
		this.styles = styles;
	}

	public String getTitle() 
	{
		return title;
	}
	
	public void setTitle(String title) 
	{
		this.title = title;
	}

	public String getCountry() 
	{
		return country;
	}
	
	public void setCountry(String country) 
	{
		this.country = country;
	}
	
	public List<String> getFormats() 
	{
		return formats;
	}

	public void setFormats(List<String> formats) 
	{
		this.formats = formats;
	}

	public String getUri() 
	{
		return uri;
	}
	
	public void setUri(String uri) 
	{
		this.uri = uri;
	}
	
	public String getLabel() 
	{
		return label;
	}
	
	public void setLabel(String label) 
	{
		this.label = label;
	}
	
	public String getYear() 
	{
		return year;
	}
	
	public void setYear(String year) 
	{
		this.year = year;
	}
	
	public String getType() 
	{
		return type;
	}
	
	public void setType(String type) 
	{
		this.type = type;
	}

	public long getId() 
	{
		return id;
	}

	public void setId(long id) 
	{
		this.id = id;
	}
}
