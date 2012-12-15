package com.discogs.activities;

import java.util.ArrayList;
import java.util.List;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.support.v4.actionbar.ActionBarListActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.discogs.Constants;
import com.discogs.R;
import com.discogs.adapters.ResultAdapter;
import com.discogs.adapters.ResultEndlessAdapter;
import com.discogs.model.Result;
import com.discogs.services.DiscogsSuggestionProvider;
import com.discogs.services.Engine;
import com.discogs.utils.ResultPredicate;

public class SearchActivity extends ActionBarListActivity 
{
	private static final int DIALOG_FILTER = 0;
	
	private Handler handler = new Handler();
	
	private ProgressBar progressBar;
	private View content;
	
	private Engine engine;
	private List<Result> results;
	private String query;
	private ResultPredicate releasePredicate = new ResultPredicate("release");
	private ResultPredicate artistPredicate = new ResultPredicate("artist");
	private ResultPredicate masterPredicate = new ResultPredicate("master");
	private ResultPredicate labelPredicate = new ResultPredicate("label");
	private boolean loading = true;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search);
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
		
		final Intent intent = getIntent();
		 
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) 
		{
			query = intent.getStringExtra(SearchManager.QUERY);
			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, DiscogsSuggestionProvider.AUTHORITY, DiscogsSuggestionProvider.MODE);
			suggestions.saveRecentQuery(query, null);
			setTitle("Search: " + query);
			query = StringUtils.replace(query, " ", "+");
			Thread thread = new Thread(new Runnable() 
			{
				public void run() 
				{
					results = engine.search(query, 1);
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
		else
		{
			final String barCode = getIntent().getExtras().getString("barcode");
			Thread thread = new Thread(new Runnable() 
			{
				public void run() 
				{
					results = engine.searchByBarCode(barCode);
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
	}
	
	@Override
    public void onNewIntent(final Intent newIntent) 
	{
        super.onNewIntent(newIntent);
        final Intent intent = getIntent();
       
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) 
		{
			query = intent.getStringExtra(SearchManager.QUERY);
			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, DiscogsSuggestionProvider.AUTHORITY, DiscogsSuggestionProvider.MODE);
			suggestions.saveRecentQuery(query, null);
			query = StringUtils.replace(query, " ", "_");
			Thread thread = new Thread(new Runnable() 
			{
				public void run() 
				{
					results = engine.search(query, 1);
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
    }
	
	/*******
	 * Menu
	 *******/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_search, menu);

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
			default:
			{
				return true;
			}
		}
	}
	
	/**********
	 * Dialogs
	 **********/
	
	@Override
	protected Dialog onCreateDialog(int id) 
	{
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		switch(id) 
		{
		 	case DIALOG_FILTER:
		    {
		    	final CharSequence[] items = {"All", "Artist", "Release", "Master release", "Label"};
		    	builder.setTitle("Filter by");
		    	builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() 
		    	{
		    	    public void onClick(DialogInterface dialog, int item) 
		    	    {
		    			progressBar.setVisibility(View.VISIBLE);
		    			content.setVisibility(View.GONE);
		    			
		    	    	CharSequence selection = items[item];
		    	    	ResultEndlessAdapter resultEndlessAdapter = (ResultEndlessAdapter) getListAdapter();
		    	    	
		    	    	if (resultEndlessAdapter != null)
		    	    	{
		    	    		ResultAdapter resultAdapter = (ResultAdapter) resultEndlessAdapter.getAdapter();
			    			
			    			if (selection.equals("All"))
			    	    	{
			    	    		resultAdapter.setResults(results);
			    	    		resultAdapter.notifyDataSetChanged();
			    	    		getListView().setSelection(0);
			    	    	}
			    			else if (selection.equals("Artist"))
			    	    	{
			    	    		List<Result> resultsCopy = new ArrayList<Result>(results);
								CollectionUtils.filter(resultsCopy, artistPredicate);
								resultAdapter.setResults(resultsCopy);
			    	    		resultAdapter.notifyDataSetChanged();
			    	    		getListView().setSelection(0);
			    	    	}
			    	    	else if (selection.equals("Release"))
			    	    	{
			    	    		List<Result> resultsCopy = new ArrayList<Result>(results);
								CollectionUtils.filter(resultsCopy, releasePredicate);
								resultAdapter.setResults(resultsCopy);
			    	    		resultAdapter.notifyDataSetChanged();
			    	    		getListView().setSelection(0);
			    	    	}
			    	    	else if (selection.equals("Master release"))
			    	    	{
			    	    		List<Result> resultsCopy = new ArrayList<Result>(results);
								CollectionUtils.filter(resultsCopy, masterPredicate);
								resultAdapter.setResults(resultsCopy);
			    	    		resultAdapter.notifyDataSetChanged();
			    	    		getListView().setSelection(0);
			    	    	}
			    	    	else if (selection.equals("Label"))
			    	    	{
			    	    		List<Result> resultsCopy = new ArrayList<Result>(results);
								CollectionUtils.filter(resultsCopy, labelPredicate);
								resultAdapter.setResults(resultsCopy);
			    	    		resultAdapter.notifyDataSetChanged();
			    	    		getListView().setSelection(0);
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
	    }
		
		return dialog;
	}
	
	/*****************
	 * Helper methods
	 *****************/

	private void showUI() 
	{
		progressBar.setVisibility(View.GONE);
		content.setVisibility(View.VISIBLE);
		
		if (results != null)
		{
			setListAdapter(new ResultEndlessAdapter(this, engine, results, query));
		}
	
		loading = false;
	}
	
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) 
	{
		super.onListItemClick(listView, view, position, id);
		
		try
		{
			ResultEndlessAdapter resultEndlessAdapter = (ResultEndlessAdapter) getListAdapter();
			ResultAdapter resultAdapter = (ResultAdapter) resultEndlessAdapter.getAdapter();
			Result result = (Result) resultAdapter.getItem(position);
			String type = result.getType();

			if (type.equals("release"))
			{
				Intent intent = new Intent(this, ReleaseActivity.class);
				intent.putExtra("resourceUrl", result.getResourceUrl());
				intent.putExtra("title", result.getTitle());
				startActivity(intent);
			}
			else if (type.equals("artist"))
			{
				Intent intent = new Intent(this, ArtistActivity.class);
				intent.putExtra("resourceUrl", result.getResourceUrl());
				intent.putExtra("title", result.getTitle());
				startActivity(intent);
			}
			else if (type.equals("label"))
			{
				Intent intent = new Intent(this, LabelActivity.class);
				intent.putExtra("resourceUrl", result.getResourceUrl());
				intent.putExtra("title", result.getTitle());
				startActivity(intent);
			}
			else if (type.equals("master"))
			{
				Intent intent = new Intent(this, MasterActivity.class);
				intent.putExtra("resourceUrl", result.getResourceUrl());
				intent.putExtra("title", result.getTitle());
				startActivity(intent);
			}
		}
		catch (Exception e) 
		{
		}
	}
}