package com.discogs.utils;

import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.StrictMode;

import com.discogs.activities.ArtistReleasesActivity;
import com.discogs.activities.SearchActivity;

public class Utils 
{
	private Utils() 
	{
	};

	@TargetApi(11)
	public static void enableStrictMode() 
	{
		if (Utils.hasGingerbread()) 
		{
			StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder()
	        	.detectAll()
	        	.penaltyLog();

			StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder()
				.detectAll()
				.penaltyLog();


			if (Utils.hasHoneycomb()) 
			{
				threadPolicyBuilder.penaltyFlashScreen();
				vmPolicyBuilder
					.setClassInstanceLimit(SearchActivity.class, 1)
					.setClassInstanceLimit(ArtistReleasesActivity.class, 1);
			}
	            
			
			StrictMode.setThreadPolicy(threadPolicyBuilder.build());
			StrictMode.setVmPolicy(vmPolicyBuilder.build());
		}
	 }
	  
	public static boolean hasFroyo() 
	{
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() 
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() 
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() 
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}
	  
	public static boolean hasJellyBean() 
	{
	        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	public static void copyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
    public static String splitToComponentTimes(long seconds)
    {
       int hours = (int) seconds / 3600;
       int remainder = (int) seconds - hours * 3600;
       int mins = remainder / 60;
       remainder = remainder - mins * 60;
       int secs = remainder;
       
       StringBuffer stringBuffer = new StringBuffer();
       
       if (hours > 0)
       {
    	   if (hours < 10)
    	   {
    		   stringBuffer.append("0");
    	   }
    	   
    	   stringBuffer.append(String.valueOf(hours));
    	   stringBuffer.append(":");
       }
       
       if (mins > 0)
       {
    	   if (mins < 10)
    	   {
    		   stringBuffer.append("0");
    	   }
    	   
    	   stringBuffer.append(String.valueOf(mins));
    	   stringBuffer.append(":");
       }
       else
       {
    	   stringBuffer.append("00");
    	   stringBuffer.append(":");
       }
       
       if (secs > 0)
       {
    	   if (secs < 10)
    	   {
    		   stringBuffer.append("0");
    	   }
    	   
    	   stringBuffer.append(String.valueOf(secs));
       }
       else
       {
    	   stringBuffer.append("00"); 
       }
       
       return stringBuffer.toString();
    }
    
    public static boolean isNetworkAvailable(Context context) 
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected()) 
        {
            return true;
        } 
        else 
        {
            return false;
        }
    }
}