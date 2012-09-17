package com.discogs.services;

import android.content.Intent;
import android.os.Build;

public abstract class YouTubeIntentProvider 
{
    private static YouTubeIntentProvider instance;

    public static YouTubeIntentProvider getInstance() 
    {
        if (instance == null) 
        {
            String className;
            int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
            
            if (sdkVersion > Build.VERSION_CODES.CUPCAKE) 
            {
            	className = "com.discogs.services.YouTubeProviderSdk4";
            }
            else 
            {
            	className = "com.discogs.services.YouTubeProviderSdk3";
            }

            try 
            {
                Class<? extends YouTubeIntentProvider> clazz = Class.forName(className).asSubclass(YouTubeIntentProvider.class);
                instance = clazz.newInstance();
            } 
            catch (Exception e) 
            {
                throw new IllegalStateException(e);
            }
        }

        return instance;
    }

    public abstract Intent getYouTubeIntent();
}