package com.discogs.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.discogs.utils.Utils;

import android.content.Context;

public class FileCache 
{
	private File cacheDir;
	private Context context;
	   
	public FileCache(Context context)
	{
		this.cacheDir = context.getCacheDir();
		this.context = context;
	}
	
	public boolean fileExists(String id)
	{
		File file = new File(cacheDir, String.valueOf(id.hashCode()));
		return file.exists();
	}
	
	public File getFile(String id)
	{
		File file = new File(cacheDir, String.valueOf(id.hashCode()));
		return file;
	}
	
	public void saveFile(String url)
	{
		try 
		{
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream inputStream = conn.getInputStream();
            File file = new File(cacheDir, String.valueOf(url.hashCode()));
    		OutputStream outputStream = new FileOutputStream(file);
            Utils.copyStream(inputStream, outputStream);
            outputStream.close();
		}
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	    
	public void clear()
	{
		File[] files = cacheDir.listFiles();
	        
		for (File file : files)
		{
			file.delete();
		}
	}
}
