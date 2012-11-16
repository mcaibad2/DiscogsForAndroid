package com.discogs.services;

import java.util.ArrayList;
import java.util.List;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.discogs.model.Artist;
import com.discogs.model.Field;
import com.discogs.model.Folder;
import com.discogs.model.Label;
import com.discogs.model.Master;
import com.discogs.model.MasterRelease;
import com.discogs.model.Profile;
import com.discogs.model.Release;
import com.discogs.model.Result;
import com.discogs.model.Want;

public class Engine 
{
	private NetworkHelper networkHelper;
	private JsonHelper jsonHelper;
	private StringBuffer stringBuffer = new StringBuffer();
	
	public Engine(CommonsHttpOAuthConsumer consumer) 
	{
		this.networkHelper = new NetworkHelper(consumer);
		this.jsonHelper = new JsonHelper();
	}
	
	/**********
	 * Profile
	 **********/
	
	public String getUserName() 
	{
		String userName = null;
		
		try
		{
			String response = networkHelper.doHTTPGet("http://api.discogs.com/oauth/identity");
			userName = jsonHelper.getUserName(response);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return userName;
	}
	
	public Profile getProfile(String userName)
	{
		Profile profile = null;
		
		try 
		{
			String response = networkHelper.doHTTPGet("http://api.discogs.com/users/" + userName);
			profile = jsonHelper.getProfile(response);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return profile;
	}
	
	/***********
	 * Database
	 ***********/
	
	public Bitmap getImage(String resourceUrl) 
	{
		Bitmap bitmap = null;
		stringBuffer.setLength(0);
		stringBuffer.append(resourceUrl);
		
		try 
		{
			String response = networkHelper.doHTTPGet(stringBuffer.toString());
		    bitmap = BitmapFactory.decodeByteArray(response.getBytes(), 0, response.getBytes().length);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		return bitmap;
	}
	
	public List<Result> search(String query, int page) 
	{
		List<Result> results = null;
		stringBuffer.setLength(0);
		stringBuffer.append("http://api.discogs.com/database/search?page=");
		stringBuffer.append(String.valueOf(page));
		stringBuffer.append("&q=");
		stringBuffer.append(query);
		
		try 
		{
			String response = networkHelper.doHTTPGet(stringBuffer.toString());
			results = jsonHelper.getResults(response);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		return results;
	}
	
	public List<Result> searchByBarCode(String barcode, int page) 
	{
		List<Result> results = null;
		stringBuffer.setLength(0);
		stringBuffer.append("http://api.discogs.com/database/search?page=");
		stringBuffer.append(String.valueOf(page));
		stringBuffer.append("&q=");
		stringBuffer.append(barcode);
		stringBuffer.append("&type=release");
		
		try 
		{
			String response = networkHelper.doHTTPGet(stringBuffer.toString());
			results = jsonHelper.getResults(response);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		return results;
	}
	
	public Release getRelease(String resourceUrl)
	{
		Release release = null;
		
		stringBuffer.setLength(0);
		stringBuffer.append(resourceUrl);

		try 
		{
			String response = networkHelper.doHTTPGet(stringBuffer.toString());
			release = jsonHelper.getRelease(response);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return release;
	}
	
	public Artist getArtist(String resourceUrl)
	{
		Artist artist = null;
		
		stringBuffer.setLength(0);
		stringBuffer.append(resourceUrl);

		try 
		{
			String response = networkHelper.doHTTPGet(stringBuffer.toString());
			artist = jsonHelper.getArtist(response);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return artist;
	}
	
	public List<Release> getArtistReleases(String releasesUrl, int page)
	{
		List<Release> releases = null;
		
		stringBuffer.setLength(0);
		stringBuffer.append(releasesUrl);
		stringBuffer.append("?page=");
		stringBuffer.append(String.valueOf(page));

		try 
		{
			String response = networkHelper.doHTTPGet(stringBuffer.toString());
			releases = jsonHelper.getArtistReleases(response);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return releases;
	}
	
	public Label getLabel(String resourceUrl)
	{
		Label label = null;
		
		stringBuffer.setLength(0);
		stringBuffer.append(resourceUrl);

		try 
		{
			String response = networkHelper.doHTTPGet(stringBuffer.toString());
			label = jsonHelper.getLabel(response);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return label;
	}
	
	public Master getMaster(String resourceUrl)
	{
		Master master = null;
		
		stringBuffer.setLength(0);
		stringBuffer.append(resourceUrl);

		try 
		{
			String response = networkHelper.doHTTPGet(stringBuffer.toString());
			master = jsonHelper.getMaster(response);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return master;
	}
	
	public List<MasterRelease> getMasterReleases(String resourceUrl, int page) 
	{
		List<MasterRelease> releases = null;
		
		stringBuffer.setLength(0);
		stringBuffer.append(resourceUrl);

		try 
		{
			String response = networkHelper.doHTTPGet(stringBuffer.toString());
			releases = jsonHelper.getMasterReleases(response);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return releases;
	}
	
	public List<MasterRelease> getLabelReleases(String resourceUrl, int page) 
	{
		List<MasterRelease> releases = null;
		
		stringBuffer.setLength(0);
		stringBuffer.append(resourceUrl);

		try 
		{
			String response = networkHelper.doHTTPGet(stringBuffer.toString());
			releases = jsonHelper.getLabelReleases(response);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return releases;
	}
	
	/***********
	 * Wantlist
	 ***********/
	
	/**
	 * Add release to wantlist
	 */
	public String addReleaseToWantlist(String userName, long releaseId) 
	{
		String response = null;
		
		stringBuffer.setLength(0);
		stringBuffer.append("http://api.discogs.com/users/");
		stringBuffer.append(userName);
		stringBuffer.append("/wants/");
		stringBuffer.append(String.valueOf(releaseId));
		
		try 
		{
			response = networkHelper.doHTTPPut(stringBuffer.toString());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return response;
	}
	
	public String addReleaseToWantlist(String userName, long releaseId, int stars, String notes, boolean notesPublic) 
	{
		String response = null;
		
		stringBuffer.setLength(0);
		stringBuffer.append("http://api.discogs.com/users/");
		stringBuffer.append(userName);
		stringBuffer.append("/wants/");
		stringBuffer.append(String.valueOf(releaseId));
		stringBuffer.append("?rating=");
		stringBuffer.append(String.valueOf(stars));
		stringBuffer.append("&notes=");
		stringBuffer.append(notes);
		stringBuffer.append("&notes_public=");
		stringBuffer.append(String.valueOf(notesPublic));
		
		try 
		{
			response = networkHelper.doHTTPPut(stringBuffer.toString());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return response;
	}
	
	/**
	 * Removes release from wantlist
	 */
	public String deleteWant(String userName, long releaseId)
	{
		stringBuffer.setLength(0);
		stringBuffer.append("http://api.discogs.com/users/");
		stringBuffer.append(userName);
		stringBuffer.append("/wants/");
		stringBuffer.append(String.valueOf(releaseId));
		String response = networkHelper.doHTTPDelete(stringBuffer.toString());
		
		return response;
	}
	
	/**
	 * List releases in wantlist
	 */
	public List<Want> listWants(String resourceUrl, int page) 
	{
		List<Want> wants = new ArrayList<Want>();
		
		stringBuffer.setLength(0);
		stringBuffer.append(resourceUrl);
		stringBuffer.append("?page=");
		stringBuffer.append(String.valueOf(page));
		stringBuffer.append("&per_page=100");
		
		try 
		{
			String response = networkHelper.doHTTPGet(stringBuffer.toString());
			wants = jsonHelper.listReleasesInWantlist(response);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return wants;
	}
	
	/*************
	 * Collection
	 *************/
	
	/**
	 * List folders
	 */
	public List<Folder> listFolders(String userName, String collectionFoldersUrl) 
	{
		List<Folder> folders = new ArrayList<Folder>();
		stringBuffer.setLength(0);
		stringBuffer.append("http://api.discogs.com/users/");
		stringBuffer.append(userName);
		stringBuffer.append("/collection/folders");
		
		try 
		{
			String response = networkHelper.doHTTPGet(stringBuffer.toString());
			folders = jsonHelper.listFolders(response);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return folders;
	}
	
	/**
	 * List folders in folder
	 */
	public List<Release> listReleasesInFolder(String resourceUrl, int page) 
	{
		List<Release> releases = new ArrayList<Release>();
		stringBuffer.setLength(0);
		stringBuffer.append(resourceUrl);
		stringBuffer.append("/releases?page=");
		stringBuffer.append(String.valueOf(page));
		stringBuffer.append("&per_page=100");
		
		try 
		{
			String response = networkHelper.doHTTPGet(stringBuffer.toString());
			releases = jsonHelper.listReleasesInFolder(response);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return releases;
	}
	
	/**
	 * Delete folder
	 */
	public void deleteFolder()
	{
	}
	
	/**
	 * Edit instance in folder
	 */
	public void editInstanceInFolder()
	{
	}
	
	/**
	 * Remove an instance of a release from a user’s collection folder. Example request:
	 * DELETE /users/<username>/collection/folders/<folder_id>/releases/<release_id>/instances/<instance_id>
	 */
	public void deleteInstanceFromFolder(String userName, long folderId, long releaseId, long instanceId)
	{
		stringBuffer.setLength(0);
		stringBuffer.append("http://api.discogs.com/users/");
		stringBuffer.append(userName);
		stringBuffer.append("/collection/folders/");
		stringBuffer.append(folderId);
		stringBuffer.append("/releases/");
		stringBuffer.append(releaseId);
		stringBuffer.append("/instances/");
		stringBuffer.append(instanceId);
		
		try 
		{
			networkHelper.doHTTPDelete(stringBuffer.toString());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void createFolder(String resourceUrl, String folderName) 
	{
		stringBuffer.setLength(0);
		stringBuffer.append(resourceUrl);
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("name", folderName));
			
		try 
		{
			String response = networkHelper.doHTTPPost(stringBuffer.toString(), nameValuePairs);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void addReleaseToCollection(String userName, long id) 
	{
		stringBuffer.setLength(0);
		stringBuffer.append("http://api.discogs.com/users/");
		stringBuffer.append(userName);
		stringBuffer.append("/collection/folders/1/releases/");
		stringBuffer.append(id);
			
		try 
		{
			String response = networkHelper.doHTTPPost(stringBuffer.toString());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieve a list of user-defined collection notes fields. These fields are available on every release in the collection. 
	 * If the collection has been made private by its owner, authentication as the collection owner is required.
	 * If you are not authenticated as the collection owner, only fields with public set to true will be visible.
	 * 
	 * GET /users/<username>/collection/fields
	 */
	public List<Field> listFields(String userName) 
	{
		List<Field> fields = new ArrayList<Field>();
		
		stringBuffer.setLength(0);
		stringBuffer.append("http://api.discogs.com/users/");
		stringBuffer.append(userName);
		stringBuffer.append("/collection/fields");
		String response = null;
		
		try 
		{
			response = networkHelper.doHTTPGet(stringBuffer.toString());
			fields = jsonHelper.listFields(response);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return fields;
	}
	
	/**
	 * Change the value of a notes field on a particular instance.
	 * 
	 * POST /users/<username>/collection/folders/<folder_id>/releases/<release_id>/instances/<instance_id>/fields/<field_id>
	 */
	public void editFields(String userName, long folderId, long releaseId, long instanceId, String fieldId, String value) 
	{
		stringBuffer.setLength(0);
		stringBuffer.append("http://api.discogs.com/users/");
		stringBuffer.append(userName);
		stringBuffer.append("/collection/folders/");
		stringBuffer.append(folderId);
		stringBuffer.append("/releases/");
		stringBuffer.append(releaseId);
		stringBuffer.append("/instances/");
		stringBuffer.append(instanceId);
		stringBuffer.append("/fields/");
		stringBuffer.append(fieldId);
		
		try 
		{
			networkHelper.doJsonHTTPPost(stringBuffer.toString(), value);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/************************
	 * Marketplace inventory
	 ************************/
	
	/**
	 * The Inventory resource allows you to view a user’s Marketplace inventory.
	 * GET /users/<username>/inventory
	 */
	public void listListing(String userName) 
	{
		stringBuffer.setLength(0);
		stringBuffer.append("http://api.discogs.com/users/");
		stringBuffer.append(userName);
		stringBuffer.append("/inventory");
			
		try 
		{
			String response = networkHelper.doHTTPGet(stringBuffer.toString());
			jsonHelper.listListings(response);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
//	public String getProductJSON(String code) 
//	{
//		String productName = null;
//		stringBuffer.setLength(0);
//		stringBuffer.append("http://www.searchupc.com/handlers/upcsearch.ashx?request_type=3&access_token=148F9FB0-2BB7-4ED8-A121-611765CB7557&upc=");
//		stringBuffer.append(code);
//		
//		try 
//		{
//			String response = networkHelper.doHTTPGet(stringBuffer.toString());
//			productName = jsonHelper.getProduct(response);
//		}
//		catch (Exception e) 
//		{
//			e.printStackTrace();
//		}
//		
//		return productName;
//	}
}
