package com.discogs.activities;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.commons.lang.StringUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.actionbar.ActionBarActivity;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.discogs.Constants;
import com.discogs.adapters.ImageAdapter;
import com.discogs.model.Alias;
import com.discogs.model.Artist;
import com.discogs.services.Engine;
import com.discogs.services.YouTubeIntentProvider;

public class ArtistActivity extends ActionBarActivity
{
	private static final int DIALOG_ACTIONS = 0;

	private Handler handler = new Handler();
	
	private Artist artist;
	private Engine engine;
	private String resourceUrl;
	private StringBuffer stringBuffer = new StringBuffer();
	private boolean loading = true;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_artist);
		init();
    }

	private void init() 
	{
		CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		String token = sharedPreferences.getString("token", null);
		String tokenSecret = sharedPreferences.getString("token_secret", null);
		consumer.setTokenWithSecret(token, tokenSecret);
		this.engine = new Engine(consumer);
	
		Bundle extras = getIntent().getExtras();
		this.resourceUrl = extras.getString("resourceUrl");
		setTitle(extras.getString("title"));
		
		Thread thread = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				artist = engine.getArtist(resourceUrl);
				handler.post(new Runnable() 
				{
					@Override
					public void run() 
					{
						showUI();
					}
				});
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
		menuInflater.inflate(R.menu.menu_artist, menu);

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
		    	final CharSequence[] items = {"Show Artist Discography", "Search YouTube"};

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Select action");
				builder.setItems(items, new DialogInterface.OnClickListener() 
				{
				    public void onClick(DialogInterface dialog, int item) 
				    {
				    	CharSequence selection = items[item];
				    	
				    	if (selection.equals("Show Artist Discography"))
				    	{
				    		Intent intent = new Intent(ArtistActivity.this, ArtistReleasesActivity.class);
			    			intent.putExtra("releasesUrl", artist.getReleasesUrl());
			    			intent.putExtra("title", artist.getName());
			    			startActivity(intent);
				    	}
				    	else if (selection.equals("Search YouTube"))
				    	{
				    		try
				    		{
				    			Intent intent = YouTubeIntentProvider.getInstance().getYouTubeIntent();
				    			intent.putExtra("query", artist.getName());
				    			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				    			startActivity(intent);
				    		}
				    		catch (android.content.ActivityNotFoundException exception)
				    		{
				    			exception.printStackTrace();
				    			Toast.makeText(ArtistActivity.this, R.string.label_no_youTube, Toast.LENGTH_SHORT).show();
				    		}
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
	
	/*****************
	 * Helper methods
	 *****************/
	
	private void showUI() 
	{
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.GONE);
		
		View content = findViewById(R.id.content);
		content.setVisibility(View.VISIBLE);
		
		// Gallery
		Gallery gallery = (Gallery) findViewById(R.id.gallery);
		
		if (artist != null)
		{
			if (artist.getImages() != null && artist.getImages().size() > 0)
			{
				ImageAdapter galleryImageAdapter = new ImageAdapter(ArtistActivity.this, artist.getImages());
				gallery.setAdapter(galleryImageAdapter);
				
				gallery.setOnItemClickListener(new OnItemClickListener() 
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
					{
						Intent intent = new Intent(ArtistActivity.this, ImageActivity.class);
						intent.putExtra("title", artist.getName());
						intent.putExtra("url", artist.getImages().get(position).getResourceUrl());
						startActivity(intent);
					}
				});
			}
			else
			{
				gallery.setVisibility(View.GONE);
			}
			
			// Real name
			if (artist.getRealName() == null)
			{
				TableRow realNameTableRow = (TableRow) findViewById(R.id.realNameTableRow);
				realNameTableRow.setVisibility(View.GONE);
			}
			else
			{
				TextView realNameTextView = (TextView) findViewById(R.id.realNameTextView);
				realNameTextView.setText(artist.getRealName());
			}
			
			// Profile
			if (artist.getProfile() == null)
			{
				TableRow profileTableRow = (TableRow) findViewById(R.id.profileTableRow);
				profileTableRow.setVisibility(View.GONE);
			}
			else
			{
				TextView profileTextView = (TextView) findViewById(R.id.profileTextView);
				profileTextView.setText(StringUtils.strip(artist.getProfile(), "\n"));
			}
			
			// Sites
			if (artist.getUrls() != null && artist.getUrls().size() > 0)
			{
				TextView urlsTextView = (TextView) findViewById(R.id.sitesTextView);
				
				if (artist.getUrls().size() > 1)
				{
					stringBuffer.setLength(0);
					
					for (String url : artist.getUrls())
					{
						stringBuffer.append(url);
						stringBuffer.append("\n");
					}
					
					urlsTextView.setText(StringUtils.strip(stringBuffer.toString(), "\n"));
					Linkify.addLinks(urlsTextView, Linkify.ALL);
				}
				else
				{
					String url = artist.getUrls().get(0);
					stringBuffer.setLength(0);
					stringBuffer.append(url);
					urlsTextView.setText(stringBuffer.toString());
				}
			}
			else
			{
				TableRow sitesTableRow = (TableRow) findViewById(R.id.sitesTableRow);
				sitesTableRow.setVisibility(View.GONE);
			}
			
			// Aliases
			if (artist.getAliases() != null && artist.getAliases().size() > 0)
			{
				LinearLayout aliasesLayout = (LinearLayout) findViewById(R.id.aliasesLayout);

				for (final Alias alias : artist.getAliases())
				{
					TextView aliasTextView = new TextView(this);
					aliasTextView.setText(alias.getName());
//					Linkify.addLinks(aliasTextView, Linkify.ALL);
//					aliasTextView.setOnClickListener(new OnClickListener() 
//					{
//						@Override
//						public void onClick(View view) 
//						{
//							Intent intent = new Intent(ArtistActivity.this, ArtistActivity.class);
//							intent.putExtra("artistId", alias.getId());
//							intent.putExtra("title", alias.getName());
//							startActivity(intent);
//						}
//					});
					aliasesLayout.addView(aliasTextView);
				}
			}
			else
			{
				TableRow aliasesTableRow = (TableRow) findViewById(R.id.aliasesTableRow);
				aliasesTableRow.setVisibility(View.GONE);
			}
			
			// Variations
			if (artist.getNameVariations() != null && artist.getNameVariations().size() > 0)
			{
				TextView nameVariationsTextView = (TextView) findViewById(R.id.nameVariationsTextView);
				
				if (artist.getNameVariations().size() > 1)
				{
					stringBuffer.setLength(0);
					
					for (String nameVariation : artist.getNameVariations())
					{
						stringBuffer.append(nameVariation);
						stringBuffer.append("\n");
					}
					
					nameVariationsTextView.setText(StringUtils.strip(stringBuffer.toString(), "\n"));
				}
				else
				{
					String nameVariation = artist.getNameVariations().get(0);
					stringBuffer.setLength(0);
					stringBuffer.append(nameVariation);
					nameVariationsTextView.setText(stringBuffer.toString());
				}
			}
			else
			{
				TableRow nameVariationsTableRow = (TableRow) findViewById(R.id.nameVariationsTableRow);
				nameVariationsTableRow.setVisibility(View.GONE);
			}
		}
		
		loading = false;
	}
}
