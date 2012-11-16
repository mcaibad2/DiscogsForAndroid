package com.discogs.activities;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.actionbar.ActionBarActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.discogs.Constants;
import com.discogs.R;
import com.discogs.services.Engine;

public class MarketPlaceActivity extends ActionBarActivity
{
	private Handler handler = new Handler();
	
	private Engine engine;
	
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
		final String userName = sharedPreferences.getString("user_name", null);
		
		Thread thread = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				engine.listListing(userName);
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
	
	
	/*****************
	 * Helper methods
	 *****************/
	
	private void showUI() 
	{
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.GONE);
		
		View content = findViewById(R.id.content);
		content.setVisibility(View.VISIBLE);
	}
}
