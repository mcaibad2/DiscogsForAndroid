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
import android.view.View.OnClickListener;
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
import com.discogs.model.Label;
import com.discogs.services.Engine;
import com.discogs.services.YouTubeIntentProvider;

public class LabelActivity extends ActionBarActivity
{
	private static final int DIALOG_ACTIONS = 0;

	private Handler handler = new Handler();
	
	private Label label;
	private Engine engine;
	private String resourceUrl;
	private StringBuffer stringBuffer = new StringBuffer();
	private boolean loading = true;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_label);
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
				label = engine.getLabel(resourceUrl);
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
		    	final CharSequence[] items = {"Show Label Discography", "Search YouTube"};

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Select action");
				builder.setItems(items, new DialogInterface.OnClickListener() 
				{
				    public void onClick(DialogInterface dialog, int item) 
				    {
				    	CharSequence selection = items[item];
				    	
				    	if (selection.equals("Show Label Discography"))
				    	{
				    		Intent intent = new Intent(LabelActivity.this, LabelReleasesActivity.class);
			    			intent.putExtra("resourceUrl", label.getReleasesUrl());
			    			intent.putExtra("title", label.getName());
			    			startActivity(intent);
				    	}
				    	else if (selection.equals("Search YouTube"))
				    	{
				    		try
				    		{
				    			Intent intent = YouTubeIntentProvider.getInstance().getYouTubeIntent();
				    			intent.putExtra("query", label.getName());
				    			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				    			startActivity(intent);
				    		}
				    		catch (android.content.ActivityNotFoundException exception)
				    		{
				    			exception.printStackTrace();
				    			Toast.makeText(LabelActivity.this, R.string.label_no_youTube, Toast.LENGTH_SHORT).show();
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
		
		if (label.getImages() != null && label.getImages().size() > 0)
		{
			ImageAdapter galleryImageAdapter = new ImageAdapter(LabelActivity.this, label.getImages());
			gallery.setAdapter(galleryImageAdapter);
			
			gallery.setOnItemClickListener(new OnItemClickListener() 
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
				{
					Intent intent = new Intent(LabelActivity.this, ImageActivity.class);
					intent.putExtra("title", label.getName());
					intent.putExtra("url", label.getImages().get(position).getResourceUrl());
					startActivity(intent);
				}
			});
		}
		else
		{
			gallery.setVisibility(View.GONE);
		}
		
		// Profile
		if (label.getProfile() == null)
		{
			TableRow profileTableRow = (TableRow) findViewById(R.id.profileTableRow);
			profileTableRow.setVisibility(View.GONE);
		}
		else
		{
			TextView profileTextView = (TextView) findViewById(R.id.profileTextView);
			profileTextView.setText(StringUtils.strip(label.getProfile(), "\n"));
		}
		
		// Sublabels
		if (label.getSubLabels() != null && label.getSubLabels().size() > 0)
		{
			LinearLayout aliasesLayout = (LinearLayout) findViewById(R.id.sublabelsLayout);

			for (final Label sublabel : label.getSubLabels())
			{
				TextView sublabelTextView = new TextView(this);
				sublabelTextView.setText(sublabel.getName());
				sublabelTextView.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View view) 
					{
						Intent intent = new Intent(LabelActivity.this, LabelActivity.class);
						intent.putExtra("labelId", sublabel.getId());
						intent.putExtra("title", sublabel.getName());
						startActivity(intent);
					}
				});
				Linkify.addLinks(sublabelTextView, Linkify.ALL);
				aliasesLayout.addView(sublabelTextView);
			}
		}
		else
		{
			TableRow subLabelsTableRow = (TableRow) findViewById(R.id.sublabelsTableRow);
			subLabelsTableRow.setVisibility(View.GONE);
		}
		
		// Contact info
		if (label.getContactInfo() == null)
		{
			TableRow contactInfoTableRow = (TableRow) findViewById(R.id.contactInfoTableRow);
			contactInfoTableRow.setVisibility(View.GONE);
		}
		else
		{
			TextView contactInfoTextView = (TextView) findViewById(R.id.contactInfoTextView);
			contactInfoTextView.setText(StringUtils.strip(label.getContactInfo(), "\n"));
		}
		
		// Sites
		if (label.getUrls() != null && label.getUrls().size() > 0)
		{
			TextView urlsTextView = (TextView) findViewById(R.id.sitesTextView);
			
			if (label.getUrls().size() > 1)
			{
				stringBuffer.setLength(0);
				
				for (String url : label.getUrls())
				{
					stringBuffer.append(url);
					stringBuffer.append("\n");
				}
				
				urlsTextView.setText(StringUtils.strip(stringBuffer.toString(), "\n"));
				Linkify.addLinks(urlsTextView, Linkify.ALL);
			}
			else
			{
				String url = label.getUrls().get(0);
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
		
		loading = false;
	}
}
