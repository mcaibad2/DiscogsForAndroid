package com.discogs.activities;

import java.util.List;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.actionbar.ActionBarListActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.discogs.Constants;
import com.discogs.R;
import com.discogs.adapters.MasterReleaseAdapter;
import com.discogs.model.MasterRelease;
import com.discogs.services.Engine;
import com.discogs.utils.ReleasePredicate;

public class MasterReleasesActivity extends ActionBarListActivity
{
	private Handler handler = new Handler();
	
	private List<MasterRelease> releases;
	private String resourceUrl;
	private Engine engine;
	private ProgressBar progressBar;
	private View content;
	private boolean loading = true;
	
	private ReleasePredicate mainPredicate = new ReleasePredicate("Main");
	private ReleasePredicate remixPredicate = new ReleasePredicate("Remix");
	private ReleasePredicate appearancePredicate = new ReleasePredicate("Appearance");
	private ReleasePredicate trackAppearancePredicate = new ReleasePredicate("TrackAppearance");
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_artist_releases);
		init();
    }

	private void init() 
	{
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		content = findViewById(R.id.content);
		
		CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		String token = sharedPreferences.getString("token", null);
		String tokenSecret = sharedPreferences.getString("token_secret", null);
		consumer.setTokenWithSecret(token, tokenSecret);
		this.engine = new Engine(consumer);
	
		Bundle extras = getIntent().getExtras();
		this.resourceUrl = extras.getString("resourceUrl");
		setTitle(extras.getString("title") + " Versions");
		
		Thread thread = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				releases = engine.getMasterReleases(resourceUrl, 1);
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
	
	/*****************
	 * Helper methods
	 *****************/
	
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) 
	{
		super.onListItemClick(listView, view, position, id);
		MasterReleaseAdapter masterReleaseAdapter = (MasterReleaseAdapter) getListAdapter();
    	MasterRelease release = (MasterRelease) masterReleaseAdapter.getItem(position);
		
		Intent intent = new Intent(this, ReleaseActivity.class);
		intent.putExtra("resourceUrl", release.getResourceUrl());
		intent.putExtra("title", release.getTitle());
		startActivity(intent);
	}
	
	private void showUI() 
	{
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.GONE);
		
		View content = findViewById(R.id.content);
		content.setVisibility(View.VISIBLE);
		
		setListAdapter(new MasterReleaseAdapter(this, releases));
		
		loading = false;
	}
}
