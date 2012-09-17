package com.discogs.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.actionbar.ActionBarListActivity;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.discogs.Constants;
import com.discogs.adapters.WantAdapter;
import com.discogs.adapters.WantEndlessAdapter;
import com.discogs.model.Artist;
import com.discogs.model.BasicInformation;
import com.discogs.model.Want;
import com.discogs.services.Engine;
import com.discogs.utils.WantArtistComparator;
import com.discogs.utils.WantLabelComparator;

public class WantlistActivity extends ActionBarListActivity 
{
	private static final int DIALOG_SORT = 0;
	private static final int DIALOG_SEARCH = 1;
	
	private Handler handler = new Handler();
	private ProgressBar progressBar;
	private View content;
	
	private Engine engine;
	private String wantlistUrl;
	private List<Want> wants;
	private String userName;
	private CharSequence selection = "Artist";
    
	private WantArtistComparator wantArtistComparator = new WantArtistComparator();
	private WantLabelComparator wantLabelComparator = new WantLabelComparator();
	private boolean loading = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wantlist);
        init();
        // registerForContextMenu(getListView());
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
		this.userName = sharedPreferences.getString("user_name", null);
		this.engine = new Engine(consumer);
		this.wantlistUrl = sharedPreferences.getString("wantlist_url", null);
		
		Thread thread = new Thread(new Runnable() 
		{
			public void run() 
			{
				wants = engine.listWants(wantlistUrl, 1);
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
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) 
	{
		super.onCreateContextMenu(menu, view, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu_wantlist, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) 
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Want want = (Want) getListAdapter().getItem(info.position);
		
		switch (item.getItemId()) 
		{
			case R.id.delete:
			{
				deleteWant(want.getId());
				deleteWantFromList(info.position);
		  		return true;
			}
		  	default:
		  	{
		  		return super.onContextItemSelected(item);
		  	}
		}
	}

	/*******
	 * Menu
	 *******/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_folder_releases, menu);

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
			case R.id.menu_sort:
			{
				if (!loading)
				{
					showDialog(DIALOG_SORT);
				}
				
	            return true;
			}
			case R.id.menu_search:
			{
				if (!loading)
				{
					showDialog(DIALOG_SEARCH);
				}
				
	            return true;
			}
//			case R.id.menu_add_folder:
//			{
//				if (!loading)
//				{
//					showDialog(DIALOG_ADD_FOLDER);
//				}
//				
//	            return true;
//			}
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
		    	final CharSequence[] items = {"Artist", "Label"};
		    	builder.setTitle("Sort by");
		    	builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() 
		    	{
					public void onClick(DialogInterface dialog, int item) 
		    	    {
		    			progressBar.setVisibility(View.VISIBLE);
		    			content.setVisibility(View.GONE);
		    			
		    	    	selection = items[item];
		    	    	WantEndlessAdapter wantEndlessAdapter = (WantEndlessAdapter) getListAdapter();
		    	    	
		    	    	if (wantEndlessAdapter != null)
		    	    	{
		    	    		WantAdapter wantAdapter = (WantAdapter) wantEndlessAdapter.getAdapter();
		    	    		List<Want> mWants = wantAdapter.getWants();
			    			
			    			if (selection.equals("Label"))
			    	    	{
								Collections.sort(mWants, wantLabelComparator);
								wantAdapter.setWants(mWants);
								wantAdapter.notifyDataSetChanged();
			    				getListView().setSelection(0);
			    	    	}
			    			else if (selection.equals("Artist"))
			    	    	{
			    				Collections.sort(mWants, wantArtistComparator);
								wantAdapter.setWants(mWants);
								wantAdapter.notifyDataSetChanged();
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
		 	case DIALOG_SEARCH:
		    {
		    	LayoutInflater factory = LayoutInflater.from(this);
		    	View view = factory.inflate(R.layout.alert_dialog_text_entry, null);
		    	final EditText editText = (EditText) view.findViewById(R.id.editText);
		    	builder.setTitle("Search");
		    	builder.setView(view);
		    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		    	{
		    		public void onClick(DialogInterface dialog, int whichButton) 
		    		{
		    			String searchTerm = editText.getText().toString();
		    			
		    			if (!TextUtils.isEmpty(searchTerm) && wants != null)
		    			{
		    				List<Want> mWants = new ArrayList<Want>();
		    				
		    				for (Want want : wants)
		    				{
		    					boolean addToReleases = false;
		    					BasicInformation basicInformation = want.getBasicInformation();
		    					String title = basicInformation.getTitle();
		    					
		    					if (StringUtils.containsIgnoreCase(title, searchTerm))
		    					{
		    						addToReleases = true;
		    					}
		    					
		    					if (!addToReleases)
		    					{
		    						List<Artist> artists = basicInformation.getArtists();
			    					
			    					for (Artist artist : artists)
			    					{
			    						String name = artist.getName();
			    						
										if (StringUtils.containsIgnoreCase(name, searchTerm))
			    						{
			    							addToReleases = true;
			    							break;
			    						}
			    					}
		    					}
		    					
		    					if (addToReleases)
		    					{
		    						mWants.add(want);
		    					}
		    				}

		    				WantEndlessAdapter wantEndlessAdapter = (WantEndlessAdapter) getListAdapter();
		    				WantAdapter wantAdapter = (WantAdapter) wantEndlessAdapter.getAdapter();
			    	    	
			    			if (selection.equals("Label"))
			    	    	{
								Collections.sort(mWants, wantLabelComparator);
								wantAdapter.setWants(mWants);
								wantAdapter.notifyDataSetChanged();
			    				getListView().setSelection(0);
			    	    	}
			    			else if (selection.equals("Artist"))
			    	    	{
			    				Collections.sort(mWants, wantArtistComparator);
			    				wantAdapter.setWants(mWants);
			    				wantAdapter.notifyDataSetChanged();
			    				getListView().setSelection(0);
			    	    	}
		    			}
		    		}
		    	});
		    	builder.setNeutralButton("Clear", new DialogInterface.OnClickListener() 
		    	{
		    		public void onClick(DialogInterface dialog, int whichButton) 
		    		{
		    			WantEndlessAdapter wantEndlessAdapter = (WantEndlessAdapter) getListAdapter();
		    			WantAdapter wantAdapter = (WantAdapter) wantEndlessAdapter.getAdapter();
		    	    	
		    			if (selection.equals("Label"))
		    	    	{
							Collections.sort(wants, wantLabelComparator);
							wantAdapter.setWants(wants);
							wantAdapter.notifyDataSetChanged();
		    				getListView().setSelection(0);
		    	    	}
		    			else if (selection.equals("Artist"))
		    	    	{
		    				Collections.sort(wants, wantArtistComparator);
		    				wantAdapter.setWants(wants);
							wantAdapter.notifyDataSetChanged();
		    				getListView().setSelection(0);
		    	    	}
		    			
		    			removeDialog(DIALOG_SEARCH);
		    		}
		    	});
		    	dialog = builder.create();
				
				break;
			}
	    }
		
		return dialog;
	}
	
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) 
	{
		super.onListItemClick(listView, view, position, id);
		
		try
		{
			WantEndlessAdapter wantEndlessAdapter = (WantEndlessAdapter) getListAdapter();
			WantAdapter wantAdapter = (WantAdapter) wantEndlessAdapter.getAdapter();
			Want want = (Want) wantAdapter.getItem(position);
		
			Intent intent = new Intent(this, WantActivity.class);
			intent.putExtra("resourceUrl", want.getBasicInformation().getResourceUrl());
			intent.putExtra("title", want.getBasicInformation().getArtists().get(0).getName() + " - " + want.getBasicInformation().getTitle());
			startActivity(intent);
		}
		catch (Exception e) 
		{
		}
	}
	
	/*****************
	 * Helper methods
	 *****************/
	
	private void showUI() 
	{
		progressBar.setVisibility(View.GONE);
		content.setVisibility(View.VISIBLE);
		
		if (!CollectionUtils.isEmpty(wants))
		{
			Collections.sort(wants, wantArtistComparator);
			setListAdapter(new WantEndlessAdapter(this, engine, wants, wantlistUrl, selection));
		}
		
		loading = false;
	}
	
	/**
	 * Remove release from wantlist
	 */
	private void deleteWant(long releaseId) 
	{
		engine.deleteWant(userName, releaseId);
	}
	
	/**
	 * Removes release from wantlist ListView widgets
	 */
	private void deleteWantFromList(int location) 
	{
		WantAdapter wantAdapter = (WantAdapter) ((WantEndlessAdapter) getListAdapter()).getAdapter();
		wantAdapter.getWants().remove(location);
		wantAdapter.notifyDataSetChanged();
		Toast.makeText(this, "Removed successfully from wantlist", Toast.LENGTH_SHORT).show();
	}
}