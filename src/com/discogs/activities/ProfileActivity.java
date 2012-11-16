package com.discogs.activities;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.actionbar.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.discogs.Constants;
import com.discogs.R;
import com.discogs.model.Profile;
import com.discogs.services.Engine;

public class ProfileActivity extends ActionBarActivity 
{
	private Handler handler = new Handler();
	
	private Profile profile;
	private Engine engine;
      
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile);
        init();
    }
    
	private void init() 
	{
		CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		String token = sharedPreferences.getString("token", null);
		String tokenSecret = sharedPreferences.getString("token_secret", null);
		consumer.setTokenWithSecret(token, tokenSecret);
		final String userName = sharedPreferences.getString("user_name", null);
		this.engine = new Engine(consumer);
		
		Thread thread = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				profile = engine.getProfile(userName);
				
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
	
	@Override
	protected void onResume() 
	{
		super.onResume();
	}
	
	private void showUI() 
	{
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.GONE);
		
		View content = findViewById(R.id.content);
		content.setVisibility(View.VISIBLE);
		
		if (profile != null)
		{
			TextView usernameTextView = (TextView) findViewById(R.id.usernameTextView);
			usernameTextView.setText(profile.getUsername());
			
			TextView profileTextView = (TextView) findViewById(R.id.profileTextView);
			profileTextView.setText(profile.getProfile());
			
			TextView homepageTextView = (TextView) findViewById(R.id.homepageTextView);
			homepageTextView.setText(profile.getHomePage());
			
			TextView locationTextView = (TextView) findViewById(R.id.locationTextView);
			locationTextView.setText(profile.getLocation());
			
			TextView registeredTextView = (TextView) findViewById(R.id.registeredTextView);
			registeredTextView.setText(profile.getRegistered());
			
			TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
			nameTextView.setText(profile.getName());
		}
		else
		{
			findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
			findViewById(R.id.profileLayout).setVisibility(View.GONE);
		}
	}

	/*******
	 * Menu
	 *******/
	
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
			default:
			{
				return true;
			}
		}
	}
}