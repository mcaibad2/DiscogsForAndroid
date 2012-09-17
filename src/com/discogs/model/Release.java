package com.discogs.model;

import java.util.ArrayList;
import java.util.List;

public class Release 
{
	private long id;
	private long instanceId;
	private long folderId;
	private int rating;
	private BasicInformation basicInformation;
	private String title;
	private String country;
	private String year;
	private String released;
	private String notes;
	private List<Image> images;
	private List<Label> labels;
	private List<String> styles;
	private List<String> genres;
	private List<Track> tracks;
	private List<Format> formats;
	private List<Artist> artists;
	private List<Label> companies;
	private List<Video> videos;
	private String thumb;
	private String mainRelease;
	private String role;
	private String type;
	private String resourceUrl;
	private boolean notesPublic;
	private List<Field> fields = new ArrayList<Field>();
	
	public List<Field> getFields() 
	{
		return fields;
	}

	public void setFields(List<Field> fields) 
	{
		this.fields = fields;
	}

	public boolean isNotesPublic()
	{
		return notesPublic;
	}

	public void setNotesPublic(boolean notesPublic) 
	{
		this.notesPublic = notesPublic;
	}

	public String getRole() 
	{
		return role;
	}

	public void setRole(String role) 
	{
		this.role = role;
	}

	public String getType() 
	{
		return type;
	}

	public void setType(String type) 
	{
		this.type = type;
	}

	public String getResourceUrl() 
	{
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) 
	{
		this.resourceUrl = resourceUrl;
	}

	public String getMainRelease() 
	{
		return mainRelease;
	}

	public void setMainRelease(String mainRelease) 
	{
		this.mainRelease = mainRelease;
	}

	public String getThumb() 
	{
		return thumb;
	}

	public void setThumb(String thumb) 
	{
		this.thumb = thumb;
	}

	public List<Video> getVideos() 
	{
		return videos;
	}

	public void setVideos(List<Video> videos) 
	{
		this.videos = videos;
	}

	public List<Label> getCompanies() 
	{
		return companies;
	}

	public void setCompanies(List<Label> companies) 
	{
		this.companies = companies;
	}

	public List<Artist> getArtists() 
	{
		return artists;
	}

	public void setArtists(List<Artist> artists) 
	{
		this.artists = artists;
	}

	public List<Format> getFormats()
	{
		return formats;
	}

	public void setFormats(List<Format> formats) 
	{
		this.formats = formats;
	}

	public List<Track> getTracks() 
	{
		return tracks;
	}

	public void setTracks(List<Track> tracks) 
	{
		this.tracks = tracks;
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

	public List<Label> getLabels() 
	{
		return labels;
	}

	public void setLabels(List<Label> labels) 
	{
		this.labels = labels;
	}

	public String getCountry() 
	{
		return country;
	}

	public void setCountry(String country) 
	{
		this.country = country;
	}

	public String getYear() 
	{
		return year;
	}

	public void setYear(String year) 
	{
		this.year = year;
	}

	public String getReleased() 
	{
		return released;
	}

	public void setReleased(String released) 
	{
		this.released = released;
	}

	public List<Image> getImages() 
	{
		return images;
	}

	public void setImages(List<Image> images) 
	{
		this.images = images;
	}

	public String getTitle() 
	{
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}

	public long getId() 
	{
		return id;
	}

	public void setId(long id) 
	{
		this.id = id;
	}

	public long getInstanceId() 
	{
		return instanceId;
	}

	public void setInstanceId(long instanceId) 
	{
		this.instanceId = instanceId;
	}

	public long getFolderId() 
	{
		return folderId;
	}

	public void setFolderId(long folderId) 
	{
		this.folderId = folderId;
	}

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
}
