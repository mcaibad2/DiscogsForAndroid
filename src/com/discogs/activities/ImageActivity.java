package com.discogs.activities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.actionbar.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.discogs.services.NetworkHelper;
import com.discogs.utils.HTTPRequestHelper;
import com.discogs.widgets.TouchImageView;

public class ImageActivity extends ActionBarActivity 
{
	private static final int DIALOG_ACTIONS = 0;
	
	private Handler handler = new Handler();
	private Bitmap bitmap;
	private String mTitle;
	private boolean loading = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(LayoutInflater.from(this).inflate(R.layout.layout_progressbar, null));
		Bundle extras = getIntent().getExtras();
		final String title = (String) extras.get("title");
		final String url = (String) extras.get("url");
		this.mTitle = URLDecoder.decode(title);
		setTitle(mTitle);

		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				HTTPRequestHelper hTTPRequestHelper = new HTTPRequestHelper(new ResponseHandler<String>()
				{
					@Override
					public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException 
					{
						HttpEntity entity = response.getEntity();
						InputStream inputStream = entity.getContent();
						bitmap = BitmapFactory.decodeStream(inputStream);
						
						handler.post(new Runnable()
						{
							@Override
							public void run() 
							{
								TouchImageView imageView = new TouchImageView(ImageActivity.this);
							    imageView.setImageBitmap(bitmap);
							    setContentView(imageView);
							    
							    getActionBarHelper().setRefreshActionItemState(false);
							    loading = false;
							}
						});
						
						return null;
					}
				});
				hTTPRequestHelper.performGet(url);
				
//				try 
//				{
//					URL mUrl = new URL(url);
//					URLConnection connection = mUrl.openConnection();
//					connection.setUseCaches(true);
//					InputStream inputStream = connection.getInputStream();
//			        bitmap = BitmapFactory.decodeStream(inputStream);
//				}
//				catch (MalformedURLException e) 
//				{
//					e.printStackTrace();
//				}
//				catch (IOException e) 
//				{
//					e.printStackTrace();
//				}
			}
		});
		thread.start();
	}
	
	/*******
	 * Menu
	 *******/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_image, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();
		
		switch (id) 
		{
			case android.R.id.home:
			{
				Intent intent = new Intent(this, DashboardActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	startActivity(intent);
            	return true;
			}
			case R.id.menu_actions:
			{
				if (!loading)
				{
					showDialog(DIALOG_ACTIONS);
				}
				return true;
			}
			default:
			{
				return true;
			}
		}
	}
	
	/**********
	 * Dialogs
	 **********/
	
	protected Dialog onCreateDialog(int id) 
	{
	    Dialog dialog = null;

	    switch(id) 
	    {
		    case DIALOG_ACTIONS:
		    {
		    	final CharSequence[] items = {"Save"};

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Select action");
				builder.setItems(items, new DialogInterface.OnClickListener() 
				{
				    public void onClick(DialogInterface dialog, int item) 
				    {
				    	CharSequence selection = items[item];
				    	
				    	if (selection.equals("Save"))
				    	{
							ContentValues values = new ContentValues(7); 
							values.put(android.provider.MediaStore.Images.Media.TITLE, mTitle); 
							values.put(android.provider.MediaStore.Images.Media.BUCKET_ID, mTitle); 
							values.put(android.provider.MediaStore.Images.Media.BUCKET_DISPLAY_NAME, "Discogs"); 
							values.put(android.provider.MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000); 
							values.put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, mTitle);
							values.put(android.provider.MediaStore.Images.Media.DESCRIPTION, "Downloaded with Discogs app");
							values.put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
							Uri uri = getContentResolver().insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
								
							try 
							{
								OutputStream outStream = getContentResolver().openOutputStream(uri);
								bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outStream);
								outStream.close();
							}
							catch (FileNotFoundException e) 
							{
								e.printStackTrace();
							} 
							catch (IOException e) 
							{
								e.printStackTrace();
							}
							
							sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()))); 
							Toast.makeText(ImageActivity.this, "Image saved successfully", Toast.LENGTH_SHORT).show();
				    	}
				    }
				});
				dialog = builder.create();
		        break;
		    }
		    default:
		    {
		    	dialog = null;
		    }
	    }
	    
	    return dialog;
	}
}

