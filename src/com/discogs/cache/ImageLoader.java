package com.discogs.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader 
{
	private FileCache fileCache;
    private List<String> urls = Collections.synchronizedList(new ArrayList<String>());
    private ExecutorService executorService; 
    
    public ImageLoader(Context context)
    {
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
    }
    
    public void load(final String url, final ImageView imageView)
    {
    	if (url != null)
    	{
    		if (fileCache.fileExists(url))
            {
    			Log.d("", "From file cache: " + url);
    			Thread thread = new Thread(new Runnable() 
            	{
    				@Override
    				public void run() 
    				{
    					showImage(url, imageView);
    				}
    			});
            	thread.start();
            }
            else
            {
            	// imageView.setImageResource(android.R.drawable.ic_menu_camera);
            	Log.d("", "Queue: " + url);
                queue(url, imageView);
            }
    	}
    }
        
    private void queue(final String url, final ImageView imageView)
    {
        executorService.submit(new Runnable() 
        {
			@Override
			public void run() 
			{
	            if (!urls.contains(url))
	            {
	            	urls.add(url);
	            	fileCache.saveFile(url);
	            	//Bitmap bitmap = BitmapFactory.decodeFile(fileCache.getFile(url).getAbsolutePath());
	            	showImage(url, imageView);
	            }
			}
		});
    }
    
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) 
    {
    	// Raw height and width of image
    	final int height = options.outHeight;
    	final int width = options.outWidth;
    	int inSampleSize = 2;

    	if (height > reqHeight || width > reqWidth) 
    	{
    		if (width > height) 
    		{
    			inSampleSize = Math.round((float)height / (float)reqHeight);
    		} 
    		else 
    		{
    			inSampleSize = Math.round((float)width / (float)reqWidth);
    		}
    	}
    	return inSampleSize;
    }
    
    private void showImage(final String url, final ImageView imageView) 
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = calculateInSampleSize(options, 75, 75);
        options.inJustDecodeBounds = false;
        
    	final Bitmap bitmap = BitmapFactory.decodeFile(fileCache.getFile(url).getAbsolutePath(), options);
    	Activity activity = (Activity) imageView.getContext();
        activity.runOnUiThread(new Runnable() 
        {
			@Override
			public void run() 
			{
				imageView.setImageBitmap(bitmap);
			}
		});
	}
    
    public void clearCache() 
    {
        fileCache.clear();
    }
}