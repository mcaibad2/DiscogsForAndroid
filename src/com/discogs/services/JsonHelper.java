package com.discogs.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.discogs.model.Alias;
import com.discogs.model.Artist;
import com.discogs.model.BasicInformation;
import com.discogs.model.Field;
import com.discogs.model.Folder;
import com.discogs.model.Format;
import com.discogs.model.Image;
import com.discogs.model.Label;
import com.discogs.model.Master;
import com.discogs.model.MasterRelease;
import com.discogs.model.Profile;
import com.discogs.model.Release;
import com.discogs.model.Result;
import com.discogs.model.Track;
import com.discogs.model.Video;
import com.discogs.model.Want;

/**
 * JSON parser helper class 
 * @author dASKAS
 */
public class JsonHelper 
{
	/***********
	 * Database
	 ************/
	
	public List<Result> getResults(String json) 
	{
		List<Result> results = new ArrayList<Result>();
		
		try 
		{
			JSONObject jsonObject = new JSONObject(json);
			JSONArray resultsJsonArray = jsonObject.getJSONArray("results");

			for (int i = 0; i < resultsJsonArray.length(); i++) 
			{
				Result result = new Result();
				JSONObject resultJsonObject = resultsJsonArray.getJSONObject(i);
				result.setId(resultJsonObject.getLong("id"));
				result.setTitle(resultJsonObject.getString("title"));
				result.setUri(resultJsonObject.getString("uri"));
				
				if (resultJsonObject.has("thumb"))
				{
					String thumb = resultJsonObject.getString("thumb");
					
					if (StringUtils.isNotEmpty(thumb) && !thumb.equals("null"))
					{
						result.setThumb(thumb);
					}
				}
				else
				{
					result.setThumb(null);
				}
				
				if (resultJsonObject.has("country"))
				{
					result.setCountry(resultJsonObject.getString("country"));
				}
				
				if (resultJsonObject.has("format"))
				{
					JSONArray formatJsonArray = resultJsonObject.getJSONArray("format");
		    		List<String> formats = new ArrayList<String>();
		    		
		    		for (int w = 0; w < formatJsonArray.length(); w++) 
		    		{
		    			formats.add(formatJsonArray.getString(w));
		    		}
		    		
		    		result.setFormats(formats);
				}
				
				if (resultJsonObject.has("label"))
				{
					result.setLabel(resultJsonObject.getString("label"));
				}
				
				if (resultJsonObject.has("resource_url"))
				{
					result.setResourceUrl(resultJsonObject.getString("resource_url"));
				}
				
				if (resultJsonObject.has("type"))
				{
					result.setType(resultJsonObject.getString("type"));
				}
				
				if (resultJsonObject.has("year"))
				{
					result.setYear(resultJsonObject.getString("year"));
				}
				
				if (resultJsonObject.has("catno"))
				{
					result.setCatno(resultJsonObject.getString("catno"));
				}
				
				// Styles
				if (resultJsonObject.has("style"))
				{
					JSONArray stylesJsonArray = resultJsonObject.getJSONArray("style");
		    		List<String> styles = new ArrayList<String>();
		    		
		    		for (int x = 0; x < stylesJsonArray.length(); x++) 
		    		{
		    			styles.add(stylesJsonArray.getString(x));
		    		}
		    		
		    		result.setStyles(styles);
				}
				
				// Genres
				if (resultJsonObject.has("genre"))
				{
					JSONArray genresJsonArray = resultJsonObject.getJSONArray("genre");
		    		List<String> genres = new ArrayList<String>();
		    		
		    		for (int y = 0; y < genresJsonArray.length(); y++) 
		    		{
		    			genres.add(genresJsonArray.getString(y));
		    		}
		    		
		    		result.setGenres(genres);
				}
				
				results.add(result);
			}
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return results;
	}
	
	/*************
	 * Identity
	 *************/
	
	/**
	 * Parses user name (eg. mcaibad) after an identity request.
	 * @param json JSON String. See more at http://www.discogs.com/developers/resources/user/identity.html for example json.
	 * @return	User name String value 
	 */
	public String getUserName(String json)
	{
		String username = null;
		
		try 
		{
			JSONObject jsonObject = new JSONObject(json);
			username = jsonObject.getString("username");
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return username;
	}
	
	/*************
	 * Profile
	 *************/
	
	/**
	 * Parses user profile, after a user profie request.
	 * @param JSON string. See more at http://www.discogs.com/developers/resources/user/profile.html for example json.
	 * @return	User profile represented by a Profile object.
	 */
	public Profile getProfile(String json) 
	{
		Profile profile = new Profile();
		
		try 
		{
			JSONObject jsonObject = new JSONObject(json);
			profile.setId(jsonObject.getLong("id"));
			profile.setUsername(jsonObject.getString("username"));
			profile.setResourceUrl(jsonObject.getString("resource_url"));
			profile.setInventoryUrl(jsonObject.getString("inventory_url"));
			profile.setCollectionFoldersUrl(jsonObject.getString("collection_folders_url"));
			profile.setCollectionFieldsUrl(jsonObject.getString("collection_fields_url"));
			profile.setWantlistUrl(jsonObject.getString("wantlist_url"));
			profile.setName(jsonObject.getString("name"));
			profile.setProfile(jsonObject.getString("profile"));
			profile.setHomePage(jsonObject.getString("home_page"));
			profile.setLocation(jsonObject.getString("location"));
			profile.setRegistered(jsonObject.getString("registered"));
			profile.setNumLists(jsonObject.getInt("num_lists"));
			profile.setNumForSale(jsonObject.getInt("num_for_sale"));
			profile.setNumCollection(jsonObject.getInt("num_collection"));
			profile.setNumWantlist(jsonObject.getInt("num_wantlist"));
			profile.setNumPending(jsonObject.getInt("num_pending"));
			profile.setReleasesContributed(jsonObject.getInt("releases_contributed"));
			profile.setRank((float) jsonObject.getDouble("rank"));
			profile.setReleasesRated(jsonObject.getInt("releases_rated"));
			profile.setRatingAvg((float) jsonObject.getDouble("rating_avg"));
			
			if (jsonObject.has("is_friend"))
			{
				profile.setFriend(jsonObject.getBoolean("is_friend"));
			}
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return profile;
	}
	
	/*************
	 * Collection
	 *************/
	
	public List<Folder> listFolders(String json) 
	{
		List<Folder> folders = new ArrayList<Folder>();

		try 
		{
			JSONObject jsonObject = new JSONObject(json);
			JSONArray foldersJsonArray = jsonObject.getJSONArray("folders");

			for (int i = 0; i < foldersJsonArray.length(); i++) 
			{
				Folder folder = new Folder();
				JSONObject folderJsonObject = foldersJsonArray.getJSONObject(i);
				folder.setId(folderJsonObject.getInt("id"));
				folder.setCount(folderJsonObject.getInt("count"));
				folder.setName(folderJsonObject.getString("name"));
				folder.setResourceUrl(folderJsonObject.getString("resource_url"));
				folders.add(folder);
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return folders;
	}
	
	public Folder getFolder(String json) 
	{
		Folder retFolder = new Folder();

		try 
		{
			JSONObject jsonObject = new JSONObject(json);
			retFolder.setId(jsonObject.getInt("id"));
			retFolder.setCount(jsonObject.getInt("count"));
			retFolder.setName(jsonObject.getString("name"));
			retFolder.setResourceUrl(jsonObject.getString("resource_url"));
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return retFolder;
	}
	
	public List<Release> listReleasesInFolder(String json) 
	{
		List<Release> releases = new ArrayList<Release>();
		
		try 
		{
			JSONObject jsonObject = new JSONObject(json);
			
			if (jsonObject.has("releases"))
			{
				JSONArray releaseJsonArray = jsonObject.getJSONArray("releases");

				for (int i = 0; i < releaseJsonArray.length(); i++) 
				{
					Release release = new Release();
					
					JSONObject releaseJsonObject = releaseJsonArray.getJSONObject(i);
					release.setId(releaseJsonObject.getLong("id"));

					if (releaseJsonObject.has("notes"))
					{
						JSONArray notesJsonArray = releaseJsonObject.getJSONArray("notes");
						List<Field> fields = new ArrayList<Field>();
						
						for (int j = 0; j < notesJsonArray.length(); j++) 
						{
							JSONObject noteJsonObject = notesJsonArray.getJSONObject(j);
							Field field = new Field();
							field.setId(noteJsonObject.getLong("field_id"));
							field.setValue(noteJsonObject.getString("value"));
							fields.add(field);
						}
						
						release.setFields(fields);
					}
					
					// instance id
					if (releaseJsonObject.has("instance_id"))
					{
						release.setInstanceId(releaseJsonObject.getLong("instance_id"));
					}
					
					// folder id
					if (releaseJsonObject.has("folder_id"))
					{
						release.setFolderId(releaseJsonObject.getLong("folder_id"));
					}
					
					// Basic information
		            BasicInformation basicInformation = new BasicInformation();
		            JSONObject basicInformationJsonObject = releaseJsonObject.getJSONObject("basic_information");
		    		
		            // Resource url
		            if (basicInformationJsonObject.has("resource_url"))
					{
		            	basicInformation.setResourceUrl(basicInformationJsonObject.getString("resource_url"));
					}
		            
		            // Title
		            basicInformation.setTitle(basicInformationJsonObject.getString("title"));
		            
		            // Thumb
		            basicInformation.setThumb(basicInformationJsonObject.getString("thumb"));
		            
		            // Labels
		            JSONArray labelsJsonArray = basicInformationJsonObject.getJSONArray("labels");
		    		List<Label> labels = new ArrayList<Label>();
		    		
		    		for (int j = 0; j < labelsJsonArray.length(); j++) 
		    		{
		    			Label label = new Label();
		    			JSONObject labelJsonObject = labelsJsonArray.getJSONObject(j);
		    			label.setId(labelJsonObject.getLong("id"));
		    			label.setCatNo(labelJsonObject.getString("catno"));
		    			label.setEntityType(labelJsonObject.getString("entity_type"));
		    			label.setName(labelJsonObject.getString("name"));
		    			labels.add(label);
		    		}
		    		
		    		basicInformation.setLabels(labels);
		    		
		    		// Artists
		    		JSONArray artistsJsonArray = basicInformationJsonObject.getJSONArray("artists");
		    		List<Artist> artists = new ArrayList<Artist>();
		    		
		    		for (int v = 0; v < artistsJsonArray.length(); v++) 
		    		{
		    			Artist artist = new Artist();
		    			JSONObject artistJsonObject = artistsJsonArray.getJSONObject(v);
		    			artist.setAnv(artistJsonObject.getString("anv"));
		    			artist.setId(artistJsonObject.getLong("id"));
		    			artist.setJoin(artistJsonObject.getString("join"));
		    			artist.setName(artistJsonObject.getString("name"));
		    			artist.setResourceUrl(artistJsonObject.getString("resource_url"));
		    			artist.setRole(artistJsonObject.getString("role"));
		    			artists.add(artist);
		    		}
		    		
		    		basicInformation.setArtists(artists);
					release.setBasicInformation(basicInformation);
					releases.add(release);
				}
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return releases;
	}
	
	public List<Field> listFields(String json) 
	{
		List<Field> fields = new ArrayList<Field>();
		
		try 
		{
			JSONObject jsonObject = new JSONObject(json);
			
			if (jsonObject.has("fields"))
			{
				JSONArray fieldsJsonArray = jsonObject.getJSONArray("fields");
				Field field = null;
				
				for (int i = 0; i < fieldsJsonArray.length(); i++) 
				{
					JSONObject fieldJsonObject = fieldsJsonArray.getJSONObject(i);
					field = new Field();
					
					field.setId(fieldJsonObject.getInt("id"));
					field.setLines(fieldJsonObject.getInt("lines"));
					field.setName(fieldJsonObject.getString("name"));
					field.setPosition(fieldJsonObject.getInt("position"));
					field.setPublic(fieldJsonObject.getBoolean("public"));
					
		            // Options
		            JSONArray optionsJsonArray = fieldJsonObject.getJSONArray("options");
		    		List<String> options = new ArrayList<String>();
		    		
		    		for (int j = 0; j < optionsJsonArray.length(); j++) 
		    		{
		    			String option = optionsJsonArray.getString(j);
		    			options.add(option);
		    		}
		    		
		    		field.setOptions(options);
		    		fields.add(field);
				}
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return fields;
	}
	
	/*************
	 * Wantlist
	 *************/
	
	public List<Want> listReleasesInWantlist(String json) 
	{
		List<Want> wants = new ArrayList<Want>();
		
		try
		{
			JSONObject jsonObject = new JSONObject(json);
			JSONArray wantsJsonArray = jsonObject.getJSONArray("wants");
			
			for (int i = 0; i < wantsJsonArray.length(); i++) 
			{
	            Want want = new Want();
	            JSONObject wantJsonObject = wantsJsonArray.getJSONObject(i);
	            want.setRating(wantJsonObject.getInt("rating"));
	            want.setId(wantJsonObject.getLong("id"));
	            want.setNotes(wantJsonObject.getString("notes"));
	            want.setNotesPublic(wantJsonObject.getBoolean("notes_public"));
	            want.setResourceUrl(wantJsonObject.getString("resource_url"));
	            
	            // Want basic iformation
	            BasicInformation basicInformation = new BasicInformation();
	            JSONObject basicInformationJsonObject = wantJsonObject.getJSONObject("basic_information");
	    		
	            // Basic info Labels
	            JSONArray labelsJsonArray = basicInformationJsonObject.getJSONArray("labels");
	    		List<Label> labels = new ArrayList<Label>();
	    		
	    		for (int j = 0; j < labelsJsonArray.length(); j++) 
	    		{
	    			Label label = new Label();
	    			JSONObject labelJsonObject = labelsJsonArray.getJSONObject(j);
	    			label.setId(labelJsonObject.getLong("id"));
	    			label.setCatNo(labelJsonObject.getString("catno"));
	    			label.setEntityType(labelJsonObject.getString("entity_type"));
	    			label.setName(labelJsonObject.getString("name"));
	    			labels.add(label);
	    		}
	    		
	    		basicInformation.setLabels(labels);
	    		
	    		// Basic info Formats
	    		JSONArray formatsJsonArray = basicInformationJsonObject.getJSONArray("formats");
	    		List<Format> formats = new ArrayList<Format>();
	    		
	    		for (int x = 0; x < formatsJsonArray.length(); x++) 
	    		{
	    			Format format = new Format();
	    			JSONObject formatJsonObject = formatsJsonArray.getJSONObject(x);
	    			format.setName(formatJsonObject.getString("name"));
	    			format.setQty(formatJsonObject.getInt("qty"));
	    			
	    			// Basic info format descriptions
	    			if (formatJsonObject.has("descriptions")) 
	    			{
	    				JSONArray formatDescriptionsJsonArray = formatJsonObject.getJSONArray("descriptions");
		    			List<String> descriptions = new ArrayList<String>();
		    			descriptions.add(formatDescriptionsJsonArray.toString());
		    			
		    			format.setDescriptions(descriptions);
	    	        }
	    		
	    			formats.add(format);
	    		}
	    		
	    		basicInformation.setFormats(formats);
	    		
	    		// Basic info thumb
	    		basicInformation.setThumb(basicInformationJsonObject.getString("thumb"));
	    		
	    		// Basic info title
	    		basicInformation.setTitle(basicInformationJsonObject.getString("title"));
	    		
	    		// Basic info artists
	    		JSONArray artistsJsonArray = basicInformationJsonObject.getJSONArray("artists");
	    		List<Artist> artists = new ArrayList<Artist>();
	    		
	    		for (int v = 0; v < artistsJsonArray.length(); v++) 
	    		{
	    			Artist artist = new Artist();
	    			JSONObject artistJsonObject = artistsJsonArray.getJSONObject(v);
	    			artist.setAnv(artistJsonObject.getString("anv"));
	    			artist.setId(artistJsonObject.getLong("id"));
	    			artist.setJoin(artistJsonObject.getString("join"));
	    			artist.setName(artistJsonObject.getString("name"));
	    			artist.setResourceUrl(artistJsonObject.getString("resource_url"));
	    			artist.setRole(artistJsonObject.getString("role"));
	    			artists.add(artist);
	    		}
	    		
	    		basicInformation.setArtists(artists);
	    		
	    		// Basic info resource url
	    		basicInformation.setResourceUrl(basicInformationJsonObject.getString("resource_url"));
	    		
	    		// Basic info year
	    		basicInformation.setYear(basicInformationJsonObject.getString("year"));
	    		
	    		// Basic info id
	    		basicInformation.setId(basicInformationJsonObject.getLong("id"));
	    		
	    		want.setBasicInformation(basicInformation);
	            
	            wants.add(want);
			}
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return wants;
	}
	
	public Artist getArtist(String json) 
	{
		Artist artist = new Artist();
		
		try 
		{
			JSONObject jsonObject = new JSONObject(json);
			artist.setId(jsonObject.getLong("id"));
			artist.setName(jsonObject.getString("name"));
			artist.setResourceUrl(jsonObject.getString("resource_url"));
			artist.setReleasesUrl(jsonObject.getString("releases_url"));
			artist.setUri(jsonObject.getString("uri"));
			
			if (jsonObject.has("realname"))
			{
				artist.setRealName(jsonObject.getString("realname"));
			}
			
			if (jsonObject.has("profile"))
			{
				artist.setProfile(jsonObject.getString("profile"));
			}
			
			if (jsonObject.has("data_quality"))
			{
				artist.setDataQuality(jsonObject.getString("data_quality"));
			}
			
			// Name variations
			if (jsonObject.has("namevariations"))
			{
				JSONArray nameVariationsJsonArray = jsonObject.getJSONArray("namevariations");
	    		List<String> nameVariations = new ArrayList<String>();
	    		
	    		for (int i = 0; i < nameVariationsJsonArray.length(); i++) 
	    		{
					nameVariations.add(nameVariationsJsonArray.getString(i));
	    		}
	    		
	    		artist.setNameVariations(nameVariations);
			}
			
			// Aliases
			if (jsonObject.has("aliases"))
			{
				JSONArray aliasesJsonArray = jsonObject.getJSONArray("aliases");
	    		List<Alias> aliases = new ArrayList<Alias>();
	    		
	    		for (int i = 0; i < aliasesJsonArray.length(); i++) 
	    		{
	    			Alias alias = new Alias();
	    			JSONObject aliasJsonObject = aliasesJsonArray.getJSONObject(i);
	    			alias.setId(aliasJsonObject.getString("id"));
	    			alias.setName(aliasJsonObject.getString("name"));
	    			alias.setResourceUrl(aliasJsonObject.getString("resource_url"));
	    			aliases.add(alias);
	    		}
	    		
	    		artist.setAliases(aliases);
			}
			
			// Urls
			if (jsonObject.has("urls"))
			{
				JSONArray urlsJsonArray = jsonObject.getJSONArray("urls");
	    		List<String> urls = new ArrayList<String>();
	    		
	    		for (int i = 0; i < urlsJsonArray.length(); i++) 
	    		{
	    			urls.add(urlsJsonArray.getString(i));
	    		}
	    		
	    		artist.setUrls(urls);
			}
			
			// Images
			if (jsonObject.has("images"))
			{
				JSONArray imagesJsonArray = jsonObject.getJSONArray("images");
				List<Image> images = new ArrayList<Image>();
				    		
				for (int i = 0; i < imagesJsonArray.length(); i++) 
				{
					Image image = new Image();
					JSONObject imageJsonObject = imagesJsonArray.getJSONObject(i);
					image.setResourceUrl(imageJsonObject.getString("resource_url"));
					image.setUri(imageJsonObject.getString("uri"));
					image.setUri150(imageJsonObject.getString("uri150"));
					image.setType(imageJsonObject.getString("type"));
					images.add(image);
				}
				    		
				artist.setImages(images);
			}
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return artist;
	}
	
	public List<Release> getArtistReleases(String json) 
	{
		List<Release> releases = null;
		
		try 
		{
			JSONObject jsonObject = new JSONObject(json);
			
			if (jsonObject.has("releases"))
			{
				releases = new ArrayList<Release>();
				JSONArray releasesJsonArray = jsonObject.getJSONArray("releases");
				
				for (int i = 0; i < releasesJsonArray.length(); i++) 
				{
					Release release = new Release();
					JSONObject releaseJsonObject = releasesJsonArray.getJSONObject(i);
					
					if (releaseJsonObject.has("thumb"))
					{
						release.setThumb(releaseJsonObject.getString("thumb"));
					}
					
					release.setTitle(releaseJsonObject.getString("title"));
					
					if (releaseJsonObject.has("main_release"))
					{
						release.setMainRelease(releaseJsonObject.getString("main_release"));
					}
					
					if (releaseJsonObject.has("role"))
					{
						release.setRole(releaseJsonObject.getString("role"));
					}
					
					if (releaseJsonObject.has("year"))
					{
						release.setYear(releaseJsonObject.getString("year"));
					}
					
					release.setId(releaseJsonObject.getLong("id"));
					release.setResourceUrl(releaseJsonObject.getString("resource_url"));
					releases.add(release);
				}
			}
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return releases;
	}
	
	public Release getRelease(String json) 
	{
		Release release = new Release();
		
		try 
		{
			JSONObject jsonObject = new JSONObject(json);
			release.setId(jsonObject.getLong("id"));
			release.setTitle(jsonObject.getString("title"));
			release.setResourceUrl(jsonObject.getString("resource_url"));
			
			if (jsonObject.has("notes_public"))
			{
				release.setNotesPublic(jsonObject.getBoolean("notes_public"));
			}
			
			if (jsonObject.has("reating"))
			{
				release.setRating(jsonObject.getInt("rating"));
			}
			
			if (jsonObject.has("country"))
			{
				release.setCountry(jsonObject.getString("country"));
			}
			
			if (jsonObject.has("notes"))
			{
				release.setNotes(jsonObject.getString("notes"));
			}
			
			if (jsonObject.has("released"))
			{
				release.setReleased(jsonObject.getString("released"));
			}
			
			if (jsonObject.has("year"))
			{
				release.setYear(jsonObject.getString("year"));
			}
			
			// Artists
			if (jsonObject.has("artists"))
			{
				JSONArray artistsJsonArray = jsonObject.getJSONArray("artists");
	    		List<Artist> artists = new ArrayList<Artist>();
	    		
	    		for (int i = 0; i < artistsJsonArray.length(); i++) 
	    		{
	    			Artist artist = new Artist();
	    			JSONObject artistJsonObject = artistsJsonArray.getJSONObject(i);
	    			artist.setName(artistJsonObject.getString("name"));
	    			artists.add(artist);
	    		}
	    		
	    		release.setArtists(artists);
			}
			
			// Labels
			if (jsonObject.has("labels"))
			{
				JSONArray labelsJsonArray = jsonObject.getJSONArray("labels");
	    		List<Label> labels = new ArrayList<Label>();
	    		
	    		for (int i = 0; i < labelsJsonArray.length(); i++) 
	    		{
	    			Label label = new Label();
	    			JSONObject imageJsonObject = labelsJsonArray.getJSONObject(i);
	    			label.setCatNo(imageJsonObject.getString("catno"));
	    			label.setName(imageJsonObject.getString("name"));
	    			label.setResourceUrl(imageJsonObject.getString("resource_url"));
	    			labels.add(label);
	    		}
	    		
	    		release.setLabels(labels);
			}
			
			// Images
			if (jsonObject.has("images"))
			{
				JSONArray imagesJsonArray = jsonObject.getJSONArray("images");
				List<Image> images = new ArrayList<Image>();
				    		
				for (int i = 0; i < imagesJsonArray.length(); i++) 
				{
					Image image = new Image();
					JSONObject imageJsonObject = imagesJsonArray.getJSONObject(i);
					image.setResourceUrl(imageJsonObject.getString("resource_url"));
					image.setUri(imageJsonObject.getString("uri"));
					image.setUri150(imageJsonObject.getString("uri150"));
					image.setType(imageJsonObject.getString("type"));
					images.add(image);
				}
				    		
				release.setImages(images);
			}
			
			// Styles
			if (jsonObject.has("styles"))
			{
				JSONArray stylesJsonArray = jsonObject.getJSONArray("styles");
	    		List<String> styles = new ArrayList<String>();
	    		
	    		for (int i = 0; i < stylesJsonArray.length(); i++) 
	    		{
	    			styles.add(stylesJsonArray.getString(i));
	    		}
	    		
	    		release.setStyles(styles);
			}
			
			// Styles
			if (jsonObject.has("genres"))
			{
				JSONArray genresJsonArray = jsonObject.getJSONArray("genres");
	    		List<String> genres = new ArrayList<String>();
	    		
	    		for (int i = 0; i < genresJsonArray.length(); i++) 
	    		{
	    			genres.add(genresJsonArray.getString(i));
	    		}
	    		
	    		release.setGenres(genres);
			}
			
			// Tracklist
			if (jsonObject.has("tracklist"))
			{
				JSONArray tracklistJsonArray = jsonObject.getJSONArray("tracklist");
	    		List<Track> tracks = new ArrayList<Track>();
	    		
	    		for (int i = 0; i < tracklistJsonArray.length(); i++) 
	    		{
	    			Track track = new Track();
	    			JSONObject trackJsonObject = tracklistJsonArray.getJSONObject(i);
	    			track.setDuration(trackJsonObject.getString("duration"));
	    			track.setPosition(trackJsonObject.getString("position"));
	    			track.setTitle(trackJsonObject.getString("title"));

	    			if (trackJsonObject.has("extraartists"))
	    			{
	    				JSONArray extraartistsJSONArray = trackJsonObject.getJSONArray("extraartists");
	    				
	    				for (int y = 0; y < extraartistsJSONArray.length(); y++) 
	    	    		{
	    	    			Artist artist = new Artist();
	    	    			JSONObject extraartistJsonObject = extraartistsJSONArray.getJSONObject(y);
	    	    			artist.setName(extraartistJsonObject.getString("name"));
	    	    			
	    	    			track.getArtists().add(artist);
	    	    		}
	    			}
	    			
	    			tracks.add(track);
	    		}
	    		
	    		release.setTracks(tracks);
			}
			
			// Formats
			if (jsonObject.has("formats"))
			{
				JSONArray formatsJsonArray = jsonObject.getJSONArray("formats");
	    		List<Format> formats = new ArrayList<Format>();
	    		
	    		for (int i = 0; i < formatsJsonArray.length(); i++) 
	    		{
	    			Format format = new Format();
	    			JSONObject formatJsonObject = formatsJsonArray.getJSONObject(i);
	    			format.setName(formatJsonObject.getString("name"));
	    			format.setQty(formatJsonObject.getInt("qty"));
	    			
	    			if (formatJsonObject.has("descriptions")) 
	    			{
	    				JSONArray formatDescriptionsJsonArray = formatJsonObject.getJSONArray("descriptions");
		    			List<String> descriptions = new ArrayList<String>();
		    			descriptions.add(formatDescriptionsJsonArray.toString());
		    			
		    			format.setDescriptions(descriptions);
	    	        }
	    			
	    			formats.add(format);
	    		}
	    		
	    		release.setFormats(formats);
			}
			
			// Companies
			if (jsonObject.has("companies"))
			{
				JSONArray companiesJsonArray = jsonObject.getJSONArray("companies");
	    		List<Label> companies = new ArrayList<Label>();
	    		
	    		for (int i = 0; i < companiesJsonArray.length(); i++) 
	    		{
	    			Label label = new Label();
	    			JSONObject formatJsonObject = companiesJsonArray.getJSONObject(i);
	    			label.setEntityTypeName(formatJsonObject.getString("entity_type_name"));
	    			label.setName(formatJsonObject.getString("name"));
	    			companies.add(label);
	    		}
	    		
	    		release.setCompanies(companies);
			}
			
			// Artists
			if (jsonObject.has("videos"))
			{
				JSONArray videosJsonArray = jsonObject.getJSONArray("videos");
	    		List<Video> videos = new ArrayList<Video>();
	    		
	    		for (int i = 0; i < videosJsonArray.length(); i++) 
	    		{
	    			Video video = new Video();
	    			JSONObject videoJsonObject = videosJsonArray.getJSONObject(i);
	    			video.setUri((videoJsonObject.getString("uri")));
	    			video.setTitle((videoJsonObject.getString("title")));
	    			video.setDescription((videoJsonObject.getString("description")));
	    			video.setDuration(videoJsonObject.getInt("duration"));
	    			videos.add(video);
	    		}
	    		
	    		release.setVideos(videos);
			}
			
			if (jsonObject.has("extraartists"))
			{
				JSONArray extraartistsJsonArray = jsonObject.getJSONArray("extraartists");
	    		
	    		for (int i = 0; i < extraartistsJsonArray.length(); i++) 
	    		{
	    		}
			}
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return release;
	}

	public Label getLabel(String json) 
	{
		Label label = new Label();
		
		try 
		{
			JSONObject jsonObject = new JSONObject(json);
			label.setId(jsonObject.getLong("id"));
			label.setName(jsonObject.getString("name"));
			
			if (jsonObject.has("profile"))
			{
				label.setProfile(jsonObject.getString("profile"));
			}
		
			label.setResourceUrl(jsonObject.getString("resource_url"));
			label.setReleasesUrl(jsonObject.getString("releases_url"));
			label.setUri(jsonObject.getString("uri"));
			label.setDataQuality(jsonObject.getString("data_quality"));
			
			if (jsonObject.has("contact_info"))
			{
				label.setContactInfo(jsonObject.getString("contact_info"));
			}
			
			// Parent label
			if (jsonObject.has("parent_label"))
			{
				String parentLabelJsonArray = jsonObject.getString("parent_label");
			}
			
			// Sublabels
			if (jsonObject.has("sublabels"))
			{
				JSONArray subLabelsJsonArray = jsonObject.getJSONArray("sublabels");
	    		List<Label> subLabels = new ArrayList<Label>();
	    		
	    		for (int i = 0; i < subLabelsJsonArray.length(); i++) 
	    		{
	    			Label subLabel = new Label();
	    			JSONObject subLabelJsonObject = subLabelsJsonArray.getJSONObject(i);
	    			subLabel.setId((subLabelJsonObject.getLong("id")));
	    			subLabel.setName(subLabelJsonObject.getString("name"));
	    			subLabel.setResourceUrl(subLabelJsonObject.getString("resource_url"));
	    			subLabels.add(subLabel);
	    		}
	    		
	    		label.setSubLabels(subLabels);
			}
			
			// Urls
			if (jsonObject.has("urls"))
			{
				JSONArray urlsJsonArray = jsonObject.getJSONArray("urls");
	    		List<String> urls = new ArrayList<String>();
	    		
	    		for (int i = 0; i < urlsJsonArray.length(); i++) 
	    		{
	    			urls.add(urlsJsonArray.getString(i));
	    		}
	    		
	    		label.setUrls(urls);
			}
			
			// Images
			if (jsonObject.has("images"))
			{
				JSONArray imagesJsonArray = jsonObject.getJSONArray("images");
				List<Image> images = new ArrayList<Image>();
				    		
				for (int i = 0; i < imagesJsonArray.length(); i++) 
				{
					Image image = new Image();
					JSONObject imageJsonObject = imagesJsonArray.getJSONObject(i);
					image.setResourceUrl(imageJsonObject.getString("resource_url"));
					image.setUri(imageJsonObject.getString("uri"));
					image.setUri150(imageJsonObject.getString("uri150"));
					image.setType(imageJsonObject.getString("type"));
					images.add(image);
				}
				    		
				label.setImages(images);
			}
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return label;
	}

	public Master getMaster(String json) 
	{
		Master release = new Master();
		
		try 
		{
			JSONObject jsonObject = new JSONObject(json);
			release.setId(jsonObject.getLong("id"));
			release.setTitle(jsonObject.getString("title"));
			release.setResourceUrl(jsonObject.getString("resource_url"));
			
			if (jsonObject.has("versions_url"))
			{
				release.setVersionsUrl(jsonObject.getString("versions_url"));
			}
			
			if (jsonObject.has("country"))
			{
				release.setCountry(jsonObject.getString("country"));
			}
			
			if (jsonObject.has("notes"))
			{
				release.setNotes(jsonObject.getString("notes"));
			}
			
			if (jsonObject.has("released"))
			{
				release.setReleased(jsonObject.getString("released"));
			}
			
			if (jsonObject.has("year"))
			{
				release.setYear(jsonObject.getString("year"));
			}
			
			// Artists
			if (jsonObject.has("artists"))
			{
				JSONArray artistsJsonArray = jsonObject.getJSONArray("artists");
	    		List<Artist> artists = new ArrayList<Artist>();
	    		
	    		for (int i = 0; i < artistsJsonArray.length(); i++) 
	    		{
	    			Artist artist = new Artist();
	    			JSONObject artistJsonObject = artistsJsonArray.getJSONObject(i);
	    			artist.setName(artistJsonObject.getString("name"));
	    			artists.add(artist);
	    		}
	    		
	    		release.setArtists(artists);
			}
			
			// Labels
			if (jsonObject.has("labels"))
			{
				JSONArray labelsJsonArray = jsonObject.getJSONArray("labels");
	    		List<Label> labels = new ArrayList<Label>();
	    		
	    		for (int i = 0; i < labelsJsonArray.length(); i++) 
	    		{
	    			Label label = new Label();
	    			JSONObject imageJsonObject = labelsJsonArray.getJSONObject(i);
	    			label.setCatNo(imageJsonObject.getString("catno"));
	    			label.setName(imageJsonObject.getString("name"));
	    			label.setResourceUrl(imageJsonObject.getString("resource_url"));
	    			labels.add(label);
	    		}
	    		
	    		release.setLabels(labels);
			}
			
			// Images
			if (jsonObject.has("images"))
			{
				JSONArray imagesJsonArray = jsonObject.getJSONArray("images");
				List<Image> images = new ArrayList<Image>();
				    		
				for (int i = 0; i < imagesJsonArray.length(); i++) 
				{
					Image image = new Image();
					JSONObject imageJsonObject = imagesJsonArray.getJSONObject(i);
					image.setResourceUrl(imageJsonObject.getString("resource_url"));
					image.setUri(imageJsonObject.getString("uri"));
					image.setUri150(imageJsonObject.getString("uri150"));
					image.setType(imageJsonObject.getString("type"));
					images.add(image);
				}
				    		
				release.setImages(images);
			}
			
			// Styles
			if (jsonObject.has("styles"))
			{
				JSONArray stylesJsonArray = jsonObject.getJSONArray("styles");
	    		List<String> styles = new ArrayList<String>();
	    		
	    		for (int i = 0; i < stylesJsonArray.length(); i++) 
	    		{
	    			styles.add(stylesJsonArray.getString(i));
	    		}
	    		
	    		release.setStyles(styles);
			}
			
			// Styles
			if (jsonObject.has("genres"))
			{
				JSONArray genresJsonArray = jsonObject.getJSONArray("genres");
	    		List<String> genres = new ArrayList<String>();
	    		
	    		for (int i = 0; i < genresJsonArray.length(); i++) 
	    		{
	    			genres.add(genresJsonArray.getString(i));
	    		}
	    		
	    		release.setGenres(genres);
			}
			
			// Tracklist
			if (jsonObject.has("tracklist"))
			{
				JSONArray tracklistJsonArray = jsonObject.getJSONArray("tracklist");
	    		List<Track> tracks = new ArrayList<Track>();
	    		
	    		for (int i = 0; i < tracklistJsonArray.length(); i++) 
	    		{
	    			Track track = new Track();
	    			JSONObject trackJsonObject = tracklistJsonArray.getJSONObject(i);
	    			track.setDuration(trackJsonObject.getString("duration"));
	    			track.setPosition(trackJsonObject.getString("position"));
	    			track.setTitle(trackJsonObject.getString("title"));
	    			
	    			if (trackJsonObject.has("extraartists"))
	    			{
	    				JSONArray extraartistsJSONArray = trackJsonObject.getJSONArray("extraartists");
	    				
	    				for (int y = 0; y < extraartistsJSONArray.length(); y++) 
	    	    		{
	    	    			Artist artist = new Artist();
	    	    			JSONObject extraartistJsonObject = extraartistsJSONArray.getJSONObject(y);
	    	    			artist.setName(extraartistJsonObject.getString("name"));
	    	    			
	    	    			track.getArtists().add(artist);
	    	    		}
	    			}
	    			
	    			tracks.add(track);
	    		}
	    		
	    		release.setTracks(tracks);
			}
			
			// Formats
			if (jsonObject.has("formats"))
			{
				JSONArray formatsJsonArray = jsonObject.getJSONArray("formats");
	    		List<Format> formats = new ArrayList<Format>();
	    		
	    		for (int i = 0; i < formatsJsonArray.length(); i++) 
	    		{
	    			Format format = new Format();
	    			JSONObject formatJsonObject = formatsJsonArray.getJSONObject(i);
	    			format.setName(formatJsonObject.getString("name"));
	    			format.setQty(formatJsonObject.getInt("qty"));
	    			
	    			if (formatJsonObject.has("descriptions")) 
	    			{
	    				JSONArray formatDescriptionsJsonArray = formatJsonObject.getJSONArray("descriptions");
		    			List<String> descriptions = new ArrayList<String>();
		    			descriptions.add(formatDescriptionsJsonArray.toString());
		    			
		    			format.setDescriptions(descriptions);
	    	        }
	    			
	    			formats.add(format);
	    		}
	    		
	    		release.setFormats(formats);
			}
			
			// Companies
			if (jsonObject.has("companies"))
			{
				JSONArray companiesJsonArray = jsonObject.getJSONArray("companies");
	    		List<Label> companies = new ArrayList<Label>();
	    		
	    		for (int i = 0; i < companiesJsonArray.length(); i++) 
	    		{
	    			Label label = new Label();
	    			JSONObject formatJsonObject = companiesJsonArray.getJSONObject(i);
	    			label.setEntityTypeName(formatJsonObject.getString("entity_type_name"));
	    			label.setName(formatJsonObject.getString("name"));
	    			companies.add(label);
	    		}
	    		
	    		release.setCompanies(companies);
			}
			
			// Artists
			if (jsonObject.has("videos"))
			{
				JSONArray videosJsonArray = jsonObject.getJSONArray("videos");
	    		List<Video> videos = new ArrayList<Video>();
	    		
	    		for (int i = 0; i < videosJsonArray.length(); i++) 
	    		{
	    			Video video = new Video();
	    			JSONObject videoJsonObject = videosJsonArray.getJSONObject(i);
	    			video.setUri((videoJsonObject.getString("uri")));
	    			video.setTitle((videoJsonObject.getString("title")));
	    			video.setDescription((videoJsonObject.getString("description")));
	    			video.setDuration(videoJsonObject.getInt("duration"));
	    			videos.add(video);
	    		}
	    		
	    		release.setVideos(videos);
			}
			
			if (jsonObject.has("extraartists"))
			{
				JSONArray extraartistsJsonArray = jsonObject.getJSONArray("extraartists");
	    		
	    		for (int i = 0; i < extraartistsJsonArray.length(); i++) 
	    		{
	    		}
			}
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return release;
	
	}

	public List<MasterRelease> getMasterReleases(String json) 
	{
		List<MasterRelease> releases = null;
		
		try 
		{
			JSONObject jsonObject = new JSONObject(json);
			
			if (jsonObject.has("versions"))
			{
				releases = new ArrayList<MasterRelease>();
				JSONArray releasesJsonArray = jsonObject.getJSONArray("versions");
				
				for (int i = 0; i < releasesJsonArray.length(); i++) 
				{
					MasterRelease release = new MasterRelease();
					JSONObject releaseJsonObject = releasesJsonArray.getJSONObject(i);
					release.setTitle(releaseJsonObject.getString("title"));
					release.setThumb(releaseJsonObject.getString("thumb"));
					release.setFormat(releaseJsonObject.getString("format"));
					
					if (releaseJsonObject.has("label"))
					{
						release.setLabel(releaseJsonObject.getString("label"));
					}
				
					if (releaseJsonObject.has("released"))
					{
						release.setReleased(releaseJsonObject.getString("released"));
					}
					
					release.setCatno(releaseJsonObject.getString("catno"));
					release.setResourceUrl(releaseJsonObject.getString("resource_url"));
					releases.add(release);
				}
			}
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return releases;
	}
	
	public List<MasterRelease> getLabelReleases(String json) 
	{
		List<MasterRelease> releases = null;
		
		try 
		{
			JSONObject jsonObject = new JSONObject(json);
			
			if (jsonObject.has("releases"))
			{
				releases = new ArrayList<MasterRelease>();
				JSONArray releasesJsonArray = jsonObject.getJSONArray("releases");
				
				for (int i = 0; i < releasesJsonArray.length(); i++) 
				{
					MasterRelease release = new MasterRelease();
					JSONObject releaseJsonObject = releasesJsonArray.getJSONObject(i);
					release.setTitle(releaseJsonObject.getString("title"));
					release.setFormat(releaseJsonObject.getString("format"));
					
					if (releaseJsonObject.has("artist"))
					{
						release.setArtist(releaseJsonObject.getString("artist"));
					}
					
					if (releaseJsonObject.has("thumb"))
					{
						release.setThumb(releaseJsonObject.getString("thumb"));
					}
					
					if (releaseJsonObject.has("label"))
					{
						release.setLabel(releaseJsonObject.getString("label"));
					}
				
					if (releaseJsonObject.has("released"))
					{
						release.setReleased(releaseJsonObject.getString("released"));
					}
					
					release.setCatno(releaseJsonObject.getString("catno"));
					release.setResourceUrl(releaseJsonObject.getString("resource_url"));
					releases.add(release);
				}
			}
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return releases;
	}

	/**
	 * We use http://searchupc.com to get product name
	 */
	public String getProduct(String json) 
	{
		String productName = null;
		
		try 
		{
			JSONObject jsonObject = new JSONObject(json);
			
			if (jsonObject.has("0"))
			{
				JSONObject productJsonObject = jsonObject.getJSONObject("0");
				
				if (productJsonObject.has("productname"))
				{
					productName = productJsonObject.getString("productname");
				}
			}
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return productName;
	}
	
	/**************
	 * Marketplace
	 **************/
	
	public void listListings(String json) 
	{
		try 
		{
			JSONObject jsonObject = new JSONObject(json);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}
}
