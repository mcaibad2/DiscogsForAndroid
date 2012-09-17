package com.discogs.model;

import java.util.ArrayList;
import java.util.List;

public class Artist
{
	private String join;
	private String name;
	private String anv;
	private String role;
	private String resourceUrl;
	private long id;
	private String releasesUrl;
	private String uri;
	private String realName;
	private String profile;
	private String dataQuality;
	private List<String> nameVariations;
	private List<Alias> aliases;
	private List<String> urls;
	private List<Image> images;
	
	public List<Image> getImages() 
	{
		return images;
	}

	public void setImages(List<Image> images) 
	{
		this.images = images;
	}

	public List<String> getUrls() 
	{
		return urls;
	}

	public void setUrls(List<String> urls) 
	{
		this.urls = urls;
	}

	public List<Alias> getAliases() 
	{
		return aliases;
	}

	public void setAliases(List<Alias> aliases) 
	{
		this.aliases = aliases;
	}

	public List<String> getNameVariations()
	{
		return nameVariations;
	}

	public void setNameVariations(List<String> nameVariations) 
	{
		this.nameVariations = nameVariations;
	}

	public String getDataQuality() 
	{
		return dataQuality;
	}

	public void setDataQuality(String dataQuality)
	{
		this.dataQuality = dataQuality;
	}

	public String getProfile()
	{
		return profile;
	}

	public void setProfile(String profile) 
	{
		this.profile = profile;
	}

	public String getRealName() 
	{
		return realName;
	}

	public void setRealName(String realName) 
	{
		this.realName = realName;
	}

	public String getUri() 
	{
		return uri;
	}

	public void setUri(String uri) 
	{
		this.uri = uri;
	}
	
	public String getReleasesUrl() 
	{
		return releasesUrl;
	}

	public void setReleasesUrl(String releasesUrl) 
	{
		this.releasesUrl = releasesUrl;
	}

	public String getJoin() 
	{
		return join;
	}
	
	public void setJoin(String join) 
	{
		this.join = join;
	}
	
	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getAnv() 
	{
		return anv;
	}
	
	public void setAnv(String anv) 
	{
		this.anv = anv;
	}
	
	public String getRole() 
	{
		return role;
	}
	
	public void setRole(String role) 
	{
		this.role = role;
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
