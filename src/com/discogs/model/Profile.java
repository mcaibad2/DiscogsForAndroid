package com.discogs.model;

public class Profile 
{
	private long id;
	private String username;
	private String resourceUrl;
	private String inventoryUrl;
	private String collectionFoldersUrl;
	private String collectionFieldsUrl;
	private String wantlistUrl;
	private String name;
	private String profile;
	private String homePage;
	private String location;
	private String registered;
	private int numLists;
	private int numForSale;
	private int numCollection;
	private int numWantlist;
	private int numPending;
	private int releasesContributed;
	private float rank;
	private int releasesRated;
	private float ratingAvg;
	private boolean friend;
	
	public long getId() 
	{
		return id;
	}
	
	public void setId(long id) 
	{
		this.id = id;
	}
	
	public String getUsername() 
	{
		return username;
	}
	
	public void setUsername(String username) 
	{
		this.username = username;
	}
	
	public String getResourceUrl() 
	{
		return resourceUrl;
	}
	
	public void setResourceUrl(String resourceUrl) 
	{
		this.resourceUrl = resourceUrl;
	}
	
	public String getInventoryUrl() 
	{
		return inventoryUrl;
	}
	
	public void setInventoryUrl(String inventoryUrl) 
	{
		this.inventoryUrl = inventoryUrl;
	}
	
	public String getCollectionFoldersUrl() 
	{
		return collectionFoldersUrl;
	}
	
	public void setCollectionFoldersUrl(String collectionFoldersUrl) 
	{
		this.collectionFoldersUrl = collectionFoldersUrl;
	}
	
	public String getCollectionFieldsUrl() 
	{
		return collectionFieldsUrl;
	}
	
	public void setCollectionFieldsUrl(String collectionFieldsUrl) 
	{
		this.collectionFieldsUrl = collectionFieldsUrl;
	}
	
	public String getWantlistUrl() 
	{
		return wantlistUrl;
	}
	
	public void setWantlistUrl(String wantlistUrl) 
	{
		this.wantlistUrl = wantlistUrl;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getProfile() 
	{
		return profile;
	}
	
	public void setProfile(String profile)
	{
		this.profile = profile;
	}
	
	public String getHomePage()
	{
		return homePage;
	}
	
	public void setHomePage(String homePage) 
	{
		this.homePage = homePage;
	}
	
	public String getLocation() 
	{
		return location;
	}
	
	public void setLocation(String location) 
	{
		this.location = location;
	}
	
	public String getRegistered() 
	{
		return registered;
	}
	
	public void setRegistered(String registered) 
	{
		this.registered = registered;
	}
	
	public int getNumLists() 
	{
		return numLists;
	}
	
	public void setNumLists(int numLists) 
	{
		this.numLists = numLists;
	}
	
	public int getNumForSale()
	{
		return numForSale;
	}
	
	public void setNumForSale(int numForSale)
	{
		this.numForSale = numForSale;
	}
	
	public int getNumCollection() 
	{
		return numCollection;
	}
	
	public void setNumCollection(int numCollection) 
	{
		this.numCollection = numCollection;
	}
	
	public int getNumWantlist()
	{
		return numWantlist;
	}
	
	public void setNumWantlist(int numWantlist) 
	{
		this.numWantlist = numWantlist;
	}
	
	public int getNumPending() 
	{
		return numPending;
	}
	
	public void setNumPending(int numPending) 
	{
		this.numPending = numPending;
	}
	
	public int getReleasesContributed()
	{
		return releasesContributed;
	}
	
	public void setReleasesContributed(int releasesContributed) 
	{
		this.releasesContributed = releasesContributed;
	}
	
	public int getReleasesRated() 
	{
		return releasesRated;
	}
	
	public void setReleasesRated(int releasesRated) 
	{
		this.releasesRated = releasesRated;
	}
	
	public float getRank() 
	{
		return rank;
	}

	public void setRank(float rank) 
	{
		this.rank = rank;
	}

	public float getRatingAvg() 
	{
		return ratingAvg;
	}

	public void setRatingAvg(float ratingAvg) 
	{
		this.ratingAvg = ratingAvg;
	}

	public boolean isFriend() 
	{
		return friend;
	}
	
	public void setFriend(boolean friend) 
	{
		this.friend = friend;
	}
}
