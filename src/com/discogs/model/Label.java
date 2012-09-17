package com.discogs.model;

import java.util.List;

public class Label 
{
	private String resourceUrl;
	private String entityType;
	private String entityTypeName;
	private String catNo;
	private long id;
	private String name;
	private String profile;
	private String releasesUrl;
	private String uri;
	private String dataQuality;
	private String contactInfo;
	private List<String> parentLabels;
	private List<Label> subLabels;
	private List<String> urls;
	private List<Image> images;
	
	public String getProfile() 
	{
		return profile;
	}

	public void setProfile(String profile) 
	{
		this.profile = profile;
	}

	public String getReleasesUrl() 
	{
		return releasesUrl;
	}

	public void setReleasesUrl(String releasesUrl) 
	{
		this.releasesUrl = releasesUrl;
	}

	public String getUri() 
	{
		return uri;
	}

	public void setUri(String uri) 
	{
		this.uri = uri;
	}

	public String getDataQuality() 
	{
		return dataQuality;
	}

	public void setDataQuality(String dataQuality) 
	{
		this.dataQuality = dataQuality;
	}

	public String getContactInfo() 
	{
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) 
	{
		this.contactInfo = contactInfo;
	}

	public List<String> getParentLabels() 
	{
		return parentLabels;
	}

	public void setParentLabels(List<String> parentLabels) 
	{
		this.parentLabels = parentLabels;
	}

	public List<Label> getSubLabels() 
	{
		return subLabels;
	}

	public void setSubLabels(List<Label> subLabels) 
	{
		this.subLabels = subLabels;
	}

	public List<String> getUrls() 
	{
		return urls;
	}

	public void setUrls(List<String> urls) 
	{
		this.urls = urls;
	}
	
	public List<Image> getImages() 
	{
		return images;
	}

	public void setImages(List<Image> images) 
	{
		this.images = images;
	}

	public String getEntityTypeName() 
	{
		return entityTypeName;
	}

	public void setEntityTypeName(String entityTypeName) 
	{
		this.entityTypeName = entityTypeName;
	}
	
	public String getResourceUrl() 
	{
		return resourceUrl;
	}
	
	public void setResourceUrl(String resourceUrl) 
	{
		this.resourceUrl = resourceUrl;
	}
	
	public String getEntityType() 
	{
		return entityType;
	}
	
	public void setEntityType(String entityType) 
	{
		this.entityType = entityType;
	}
	
	public String getCatNo() 
	{
		return catNo;
	}
	
	public void setCatNo(String catNo) 
	{
		this.catNo = catNo;
	}
	
	public long getId() 
	{
		return id;
	}
	
	public void setId(long id) 
	{
		this.id = id;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
}
