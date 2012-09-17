package com.discogs.model;

public class Want 
{
	private int rating;
	private BasicInformation basicInformation;
	private String notes;
	private boolean notesPublic;
	private String resourceUrl;
	private long id;
	
	public int getRating() 
	{
		return rating;
	}
	
	public void setRating(int rating) 
	{
		this.rating = rating;
	}
	
	public BasicInformation getBasicInformation() 
	{
		return basicInformation;
	}
	
	public void setBasicInformation(BasicInformation basicInformation) 
	{
		this.basicInformation = basicInformation;
	}
	
	public String getNotes() 
	{
		return notes;
	}
	
	public void setNotes(String notes) 
	{
		this.notes = notes;
	}
	
	public boolean isNotesPublic() 
	{
		return notesPublic;
	}

	public void setNotesPublic(boolean notesPublic) 
	{
		this.notesPublic = notesPublic;
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
}