package com.discogs.model;

public class Master extends Release
{
	private String versionsUrl;

	public String getVersionsUrl() 
	{
		return versionsUrl;
	}

	public void setVersionsUrl(String versionsUrl) 
	{
		this.versionsUrl = versionsUrl;
	}
}
