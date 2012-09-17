package com.discogs.model;

import java.util.ArrayList;
import java.util.List;

public class Track 
{
	private String duration;
	private String position;
	private String title;
	private List<Artist> artists = new ArrayList<Artist>();
	
	public List<Artist> getArtists() 
	{
		return artists;
	}

	public void setArtists(List<Artist> artists) 
	{
		this.artists = artists;
	}

	public String getDuration() 
	{
		return duration;
	}
	
	public void setDuration(String duration) 
	{
		this.duration = duration;
	}
	
	public String getPosition() 
	{
		return position;
	}
	
	public void setPosition(String position) 
	{
		this.position = position;
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
