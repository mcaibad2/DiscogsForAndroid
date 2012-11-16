package com.discogs.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.comparators.ReverseComparator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.actionbar.ActionBarListActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.discogs.Constants;
import com.discogs.R;
import com.discogs.adapters.ArtistReleaseAdapter;
import com.discogs.adapters.ArtistReleaseEndlessAdapter;
import com.discogs.model.Release;
import com.discogs.services.Engine;
import com.discogs.utils.ReleaseComparator;
import com.discogs.utils.ReleasePredicate;

public class ArtistReleasesActivity extends ActionBarListActivity
{
	private static final int DIALOG_FILTER = 0;
	private static final int DIALOG_SORT = 1;

	private Handler handler = new Handler();
	
	private List<Release> releases = new ArrayList<Release>();
	private String releasesUrl;
	private String artist;
	private Engine engine;
	private ProgressBar progressBar;
	private View content;
	private boolean loading = true;
	
	private ReleasePredicate mainPredicate = new ReleasePredicate("Main");
	private ReleasePredicate remixPredicate = new ReleasePredicate("Remix");
	private ReleasePredicate appearancePredicate = new ReleasePredicate("Appearance");
	private ReleasePredicate trackAppearancePredicate = new ReleasePredicate("TrackAppearance");
	
	private Comparator<Release> ascendingReleaseComparator = new ReleaseComparator();
	private Comparator<Release> descendingReleaseComparator = new ReverseComparator(ascendingReleaseComparator);
	private CharSequence selection;
	
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
		this.releasesUrl = extras.getString("releasesUrl");
		this.artist = extras.getString("title");
		setTitle(artist + " Discography");
		
		Thread thread = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				releases = engine.getArtistReleases(releasesUrl, 1);
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
		menuInflater.inflate(R.menu.menu_artist_releases, menu);

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
			case R.id.menu_filter:
			{
				if (!loading)
				{
					showDialog(DIALOG_FILTER);
				}
				
				return true;
			}
			case R.id.menu_sort:
			{
				if (!loading)
				{
					showDialog(DIALOG_SORT);
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
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

	    switch(id) 
	    {
		    case DIALOG_SORT:
		    {
		    	final CharSequence[] items = {"Date Ascending", "Date Descending"};
		    	builder.setTitle("Sort by");
		    	builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() 
		    	{
					public void onClick(DialogInterface dialog, int item) 
		    	    {
		    			progressBar.setVisibility(View.VISIBLE);
		    			content.setVisibility(View.GONE);
		    			
		    	    	selection = items[item];
		    	    	ArtistReleaseEndlessAdapter artistReleaseEndlessAdapter = (ArtistReleaseEndlessAdapter) getListAdapter();
		    	    	int page = artistReleaseEndlessAdapter.getPage();
		    	    	
		    	    	if (artistReleaseEndlessAdapter != null)
		    	    	{
		    	    		ArtistReleaseAdapter artistReleaseAdapter = (ArtistReleaseAdapter) artistReleaseEndlessAdapter.getAdapter();
		    	    		List<Release> releases = artistReleaseAdapter.getReleases();
			    			
							if (selection.equals("Date Ascending"))
			    	    	{
								Collections.sort(releases, ascendingReleaseComparator);
//								artistReleaseAdapter.setReleases(releases);
//								artistReleaseAdapter.notifyDataSetChanged();
//			    				getListView().setSelection(0);
								artistReleaseEndlessAdapter = new ArtistReleaseEndlessAdapter(ArtistReleasesActivity.this, engine, releases, releasesUrl);
								artistReleaseEndlessAdapter.setPage(page);
			    				setListAdapter(artistReleaseAdapter);
			    	    	}
			    			else if (selection.equals("Date Descending"))
			    	    	{
			    				Collections.sort(releases, descendingReleaseComparator);
//			    				artistReleaseAdapter.setReleases(releases);
//			    				artistReleaseAdapter.notifyDataSetChanged();
//			    				getListView().setSelection(0);
			    				artistReleaseEndlessAdapter = new ArtistReleaseEndlessAdapter(ArtistReleasesActivity.this, engine, releases, releasesUrl);
			    				artistReleaseEndlessAdapter.setPage(page);
			    				setListAdapter(artistReleaseAdapter);
			    	    	}
		    	    	}
		    			
		    			progressBar.setVisibility(View.GONE);
		    			content.setVisibility(View.VISIBLE);
		    			
		    			dialog.dismiss();
		    	    }
		    	});
				dialog = builder.create();
				
				break;
			}
		    case DIALOG_FILTER:
		    {
		    	final CharSequence[] items = {"All", "Main release", "Remix", "Appearance", "Track appearance"};
		    	builder.setTitle(R.string.label_filter_discography_by);
		    	builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() 
		    	{
		    	    public void onClick(DialogInterface dialog, int item) 
		    	    {
		    			progressBar.setVisibility(View.VISIBLE);
		    			content.setVisibility(View.GONE);
		    			
		    	    	CharSequence selection = items[item];
		    	    	ArtistReleaseEndlessAdapter artistReleaseEndlessAdapter = (ArtistReleaseEndlessAdapter) getListAdapter();
		    	    	ArtistReleaseAdapter artistReleaseAdapter = (ArtistReleaseAdapter) artistReleaseEndlessAdapter.getAdapter();
		    			
		    			if (selection.equals("All"))
		    	    	{
		    				artistReleaseAdapter.setReleases(releases);
		    				artistReleaseAdapter.notifyDataSetChanged();
		    				getListView().setSelection(0);
		    	    	}
		    			else if (selection.equals("Main release"))
		    	    	{
		    	    		List<Release> releasesCopy = new ArrayList<Release>(releases);
							CollectionUtils.filter(releasesCopy, mainPredicate);
							artistReleaseAdapter.setReleases(releasesCopy);
							artistReleaseAdapter.notifyDataSetChanged();
							getListView().setSelection(0);
		    	    	}
		    			else if (selection.equals("Remix"))
		    	    	{
		    	    		List<Release> releasesCopy = new ArrayList<Release>(releases);
							CollectionUtils.filter(releasesCopy, remixPredicate);
							artistReleaseAdapter.setReleases(releasesCopy);
							artistReleaseAdapter.notifyDataSetChanged();
							getListView().setSelection(0);
		    	    	}
		    			else if (selection.equals("Appearance"))
		    	    	{
		    	    		List<Release> releasesCopy = new ArrayList<Release>(releases);
							CollectionUtils.filter(releasesCopy, appearancePredicate);
							artistReleaseAdapter.setReleases(releasesCopy);
							artistReleaseAdapter.notifyDataSetChanged();
							getListView().setSelection(0);
		    	    	}
		    			else if (selection.equals("Track appearance"))
		    	    	{
		    	    		List<Release> releasesCopy = new ArrayList<Release>(releases);
							CollectionUtils.filter(releasesCopy, trackAppearancePredicate);
							artistReleaseAdapter.setReleases(releasesCopy);
							artistReleaseAdapter.notifyDataSetChanged();
							getListView().setSelection(0);
		    	    	}
		    			
		    			progressBar.setVisibility(View.GONE);
		    			content.setVisibility(View.VISIBLE);
		    			
		    			dialog.dismiss();
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
	
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) 
	{
		super.onListItemClick(listView, view, position, id);
		ArtistReleaseEndlessAdapter artistReleaseEndlessAdapter = (ArtistReleaseEndlessAdapter) getListAdapter();
    	ArtistReleaseAdapter artistReleaseAdapter = (ArtistReleaseAdapter) artistReleaseEndlessAdapter.getAdapter();
    	Release release = (Release) artistReleaseAdapter.getItem(position);
    	String mainRelease = release.getMainRelease();
		
		if (mainRelease != null)
		{
			Intent intent = new Intent(this, MasterActivity.class);
			intent.putExtra("resourceUrl", release.getResourceUrl());
			intent.putExtra("title", artist + " - " + release.getTitle());
			startActivity(intent);
		}
		else
		{
			Intent intent = new Intent(this, ReleaseActivity.class);
			intent.putExtra("resourceUrl", release.getResourceUrl());
			intent.putExtra("title", artist + " - " + release.getTitle());
			startActivity(intent);
		}
	}
	
	private void showUI() 
	{
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.GONE);
		
		View content = findViewById(R.id.content);
		content.setVisibility(View.VISIBLE);
		
		setListAdapter(new ArtistReleaseEndlessAdapter(this, engine, releases, releasesUrl));
		
		loading = false;
	}
}
