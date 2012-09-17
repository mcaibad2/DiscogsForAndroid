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
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.discogs.Constants;
import com.discogs.adapters.FolderReleaseEndlessAdapter;
import com.discogs.adapters.ReleaseAdapter;
import com.discogs.adapters.RemoveButtonClickListener;
import com.discogs.model.Artist;
import com.discogs.model.BasicInformation;
import com.discogs.model.Field;
import com.discogs.model.Release;
import com.discogs.services.Engine;
import com.discogs.utils.ReleaseArtistComparator;
import com.discogs.utils.ReleaseLabelComparator;

public class FolderReleasesActivity extends ActionBarListActivity implements RemoveButtonClickListener
{
	private static final int DIALOG_SORT = 0;
	private static final int DIALOG_SEARCH = 1;
	private static final int DIALOG_EDIT_FIELDS = 2;
	private static final int DIALOG_OPERATIONS = 3;
	
	private Handler handler = new Handler();
	
	private ProgressBar progressBar;
	private View content;
	
	private List<Release> releases;
	private Engine engine;
	private String resourceUrl;
	private boolean loading = true;
	private CharSequence selection = "Artist";
	
	private ReleaseArtistComparator releaseArtistComparator = new ReleaseArtistComparator();
	private ReleaseLabelComparator releaseLabelComparator = new ReleaseLabelComparator();
	private String folder;
	private String userName;
	private List<Field> fields;
	// private BeanComparator artistComparator = new BeanComparator("artists", new BeanComparator("name"));
	// private BeanComparator labelComparator = new BeanComparator("labels", new BeanComparator("name"));
	private int position;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_folder_releases);
		init();
//		registerForContextMenu(getListView());
    }

	private void init() 
	{
		getListView().setItemsCanFocus(true);
		
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
		this.folder = extras.getString("name");
		this.userName = sharedPreferences.getString("user_name", null);
		setTitle("Collection - " + folder);
		
		Thread thread = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				releases = engine.listReleasesInFolder(resourceUrl, 1);
				fields = engine.listFields(userName);
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
	protected void onListItemClick(ListView listView, View view, int position, long id) 
	{
		super.onListItemClick(listView, view, position, id);
		
		try
		{
			FolderReleaseEndlessAdapter listAdapter = (FolderReleaseEndlessAdapter) getListAdapter();
			ReleaseAdapter releaseAdapter = (ReleaseAdapter) listAdapter.getAdapter();
			Release release = (Release) releaseAdapter.getItem(position);
			
			Intent intent = new Intent(this, ReleaseActivity.class);
			intent.putExtra("resourceUrl", release.getBasicInformation().getResourceUrl());
			intent.putExtra("title", release.getBasicInformation().getTitle());
			startActivity(intent);
		}
		catch (Exception e) 
		{
		}
	}
	
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) 
//	{
//		super.onCreateContextMenu(menu, view, menuInfo);
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.context_menu_folder_release, menu);
//	}
//	
//	@Override
//	public boolean onContextItemSelected(MenuItem item) 
//	{
//		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//		Want want = (Want) getListAdapter().getItem(info.position);
//		
//		switch (item.getItemId()) 
//		{
//			case R.id.edit:
//			{
//		  		return true;
//			}
//		  	default:
//		  	{
//		  		return super.onContextItemSelected(item);
//		  	}
//		}
//	}
	
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
		LayoutInflater factory = LayoutInflater.from(this);
		
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
		    	    	FolderReleaseEndlessAdapter folderReleaseEndlessAdapter = (FolderReleaseEndlessAdapter) getListAdapter();
		    	    	
		    	    	if (folderReleaseEndlessAdapter != null)
		    	    	{
			    	    	ReleaseAdapter releaseAdapter = (ReleaseAdapter) folderReleaseEndlessAdapter.getAdapter();
			    	    	List<Release> mReleases = releaseAdapter.getReleases();
			    			
			    			if (selection.equals("Label"))
			    	    	{
								Collections.sort(mReleases, releaseLabelComparator);
			    				releaseAdapter.setReleases(mReleases);
			    				releaseAdapter.notifyDataSetChanged();
			    				getListView().setSelection(0);
			    	    	}
			    			else if (selection.equals("Artist"))
			    	    	{
			    				Collections.sort(mReleases, releaseArtistComparator);
			    				releaseAdapter.setReleases(mReleases);
			    				releaseAdapter.notifyDataSetChanged();
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
		    	View view = factory.inflate(R.layout.alert_dialog_text_entry, null);
		    	final EditText editText = (EditText) view.findViewById(R.id.editText);
		    	builder.setTitle("Search " + folder);
		    	builder.setView(view);
		    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		    	{
		    		public void onClick(DialogInterface dialog, int whichButton) 
		    		{
		    			String searchTerm = editText.getText().toString();
		    			
		    			if (!TextUtils.isEmpty(searchTerm) && releases != null)
		    			{
		    				List<Release> mReleases = new ArrayList<Release>();
		    				
		    				for (Release release : releases)
		    				{
		    					boolean addToReleases = false;
		    					BasicInformation basicInformation = release.getBasicInformation();
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
		    						mReleases.add(release);
		    					}
		    				}

	    					FolderReleaseEndlessAdapter folderReleaseEndlessAdapter = (FolderReleaseEndlessAdapter) getListAdapter();
			    	    	ReleaseAdapter releaseAdapter = (ReleaseAdapter) folderReleaseEndlessAdapter.getAdapter();
			    	    	
			    			if (selection.equals("Label"))
			    	    	{
								Collections.sort(mReleases, releaseLabelComparator);
			    				releaseAdapter.setReleases(mReleases);
			    				releaseAdapter.notifyDataSetChanged();
			    				getListView().setSelection(0);
			    	    	}
			    			else if (selection.equals("Artist"))
			    	    	{
			    				Collections.sort(mReleases, releaseArtistComparator);
			    				releaseAdapter.setReleases(mReleases);
			    				releaseAdapter.notifyDataSetChanged();
			    				getListView().setSelection(0);
			    	    	}
		    			}
		    		}
		    	});
		    	builder.setNeutralButton("Clear", new DialogInterface.OnClickListener() 
		    	{
		    		public void onClick(DialogInterface dialog, int whichButton) 
		    		{
		    			FolderReleaseEndlessAdapter folderReleaseEndlessAdapter = (FolderReleaseEndlessAdapter) getListAdapter();
		    	    	ReleaseAdapter releaseAdapter = (ReleaseAdapter) folderReleaseEndlessAdapter.getAdapter();
		    	    	
		    			if (selection.equals("Label"))
		    	    	{
							Collections.sort(releases, releaseLabelComparator);
		    				releaseAdapter.setReleases(releases);
		    				releaseAdapter.notifyDataSetChanged();
		    				getListView().setSelection(0);
		    	    	}
		    			else if (selection.equals("Artist"))
		    	    	{
		    				Collections.sort(releases, releaseArtistComparator);
		    				releaseAdapter.setReleases(releases);
		    				releaseAdapter.notifyDataSetChanged();
		    				getListView().setSelection(0);
		    	    	}
		    			
		    			removeDialog(DIALOG_SEARCH);
		    		}
		    	});
		    	dialog = builder.create();
				
				break;
			}
		 	case DIALOG_EDIT_FIELDS:
		    {
		    	FolderReleaseEndlessAdapter folderReleaseEndlessAdapter = (FolderReleaseEndlessAdapter) getListAdapter();
    	    	final ReleaseAdapter releaseAdapter = (ReleaseAdapter) folderReleaseEndlessAdapter.getAdapter();
    	    	Release release = (Release) releaseAdapter.getItem(position);
    	    	final long releaseId = release.getId();
    	    	final long instanceId = release.getInstanceId();
    	    	final long folderId = release.getFolderId();
    	    	
		    	View view = factory.inflate(R.layout.dialog_edit_fields, null);
		    	
		    	final Spinner mediaConditionSpinner = (Spinner) view.findViewById(R.id.mediaConditionSpinner);
		    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.media_condition_array, android.R.layout.simple_spinner_item);
		    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    	mediaConditionSpinner.setAdapter(adapter);
		    	
		    	final Spinner sleeveConditionSpinner = (Spinner) view.findViewById(R.id.sleeveConditionSpinner);
		    	adapter = ArrayAdapter.createFromResource(this, R.array.media_condition_array, android.R.layout.simple_spinner_item);
		    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    	sleeveConditionSpinner.setAdapter(adapter);
		    	
		    	final EditText notesEditText = (EditText) view.findViewById(R.id.notesEditText);
		    	
		    	List<Field> fields = release.getFields();
		    	
				if (fields != null && fields.size() > 0)
		    	{
		    		for (Field field : fields)
		    		{
		    			long _id = field.getId();
		    			String value = field.getValue();
		    			
		    			// Media condition
		    			if (_id == 1)
		    			{
		    				if (value.equals("Mint (M)"))
		    				{
		    					mediaConditionSpinner.setSelection(1);
		    				}
		    				else if (value.equals("Near Mint (NM or M-)"))
		    				{
		    					mediaConditionSpinner.setSelection(2);
		    				}
		    				else if (value.equals("Very Good Plus (VG+)"))
		    				{
		    					mediaConditionSpinner.setSelection(3);
		    				}
		    				else if (value.equals("Very Good (VG)"))
		    				{
		    					mediaConditionSpinner.setSelection(4);
		    				}
		    				else if (value.equals("Good Plus (G+)"))
		    				{
		    					mediaConditionSpinner.setSelection(5);
		    				}
		    				else if (value.equals("Good (G)"))
		    				{
		    					mediaConditionSpinner.setSelection(6);	
		    				}
		    				else if (value.equals("Fair (F)"))
		    				{
		    					mediaConditionSpinner.setSelection(7);
		    				}
		    				else if (value.equals("Poor (P)"))
		    				{
		    					mediaConditionSpinner.setSelection(8);
		    				}
		    				else
		    				{
		    					mediaConditionSpinner.setSelection(0);
		    				}
		    			}
		    			
		    			// Sleeve condition
		    			if (_id == 2)
		    			{
		    				if (value.equals("Generic"))
		    				{
		    					mediaConditionSpinner.setSelection(1);
		    				}
		    				else if (value.equals("No Cover"))
		    				{
		    					mediaConditionSpinner.setSelection(2);
		    				}
		    				else if (value.equals("Mint (M)"))
		    				{
		    					mediaConditionSpinner.setSelection(3);
		    				}
		    				else if (value.equals("Near Mint (NM or M-)"))
		    				{
		    					mediaConditionSpinner.setSelection(4);
		    				}
		    				else if (value.equals("Very Good Plus (VG+)"))
		    				{
		    					mediaConditionSpinner.setSelection(5);
		    				}
		    				else if (value.equals("Very Good (VG)"))
		    				{
		    					mediaConditionSpinner.setSelection(6);
		    				}
		    				else if (value.equals("Good Plus (G+)"))
		    				{
		    					mediaConditionSpinner.setSelection(7);
		    				}
		    				else if (value.equals("Good (G)"))
		    				{
		    					mediaConditionSpinner.setSelection(8);	
		    				}
		    				else if (value.equals("Fair (F)"))
		    				{
		    					mediaConditionSpinner.setSelection(9);
		    				}
		    				else if (value.equals("Poor (P)"))
		    				{
		    					mediaConditionSpinner.setSelection(10);
		    				}
		    				else
		    				{
		    					mediaConditionSpinner.setSelection(0);
		    				}
		    			}
		    			
		    			// Notes
		    			if (_id == 3)
		    			{
		    				notesEditText.setText(value);
		    			}
		    		}
		    	}
		    	
		    	builder.setView(view);
		    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		    	{
		    		public void onClick(DialogInterface dialog, int whichButton) 
		    		{
		    	    	final String mediaCondition = (String) mediaConditionSpinner.getSelectedItem();
		    	    	
//		    	    	if (!mediaCondition.equals("Select media condition"))
//		    	    	{
//		    	    		Thread thread = new Thread(new Runnable() 
//		    	    		{
//								@Override
//								public void run() 
//								{
//									engine.editFields(userName, folderId, releaseId, instanceId, "1", mediaCondition);
//								}
//							});
//		    	    		thread.start();
//		    	    	}
		    	    	
		    	    	final String sleeveCondition = (String) sleeveConditionSpinner.getSelectedItem();
		    	    	
//		    	    	if (!sleeveCondition.equals("Select sleeve condition"))
//		    	    	{
//		    	    		Thread thread = new Thread(new Runnable() 
//		    	    		{
//								@Override
//								public void run() 
//								{
//									engine.editFields(userName, folderId, releaseId, instanceId, "2", sleeveCondition);
//								}
//							});
//		    	    		thread.start();
//		    	    	}
		    			
		    			Editable text = notesEditText.getText();
		    	    	
		    	    	if (text != null)
		    	    	{
		    	    		final String notes = String.valueOf(text);
		    	    		
		    	    		Thread thread = new Thread(new Runnable() 
		    	    		{
								@Override
								public void run() 
								{
									engine.editFields(userName, folderId, releaseId, instanceId, "3", notes);
								}
							});
		    	    		thread.start();
		    	    	}
		    		}
		    	});
		    	builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() 
		    	{
		    		public void onClick(DialogInterface dialog, int whichButton) 
		    		{
		    		}
		    	});
		    	dialog = builder.create();
				
				break;
			}
		 	case DIALOG_OPERATIONS:
		    {
		    	final CharSequence[] items = {"Remove", "Fields"};
		    	builder.setTitle("Select action");
		    	builder.setItems(items, new DialogInterface.OnClickListener() 
		    	{
		    	    public void onClick(DialogInterface dialog, int item) 
		    	    {
		    	    	String selection = (String) items[item];
		    	    	
		    	    	if (selection.equals("Remove"))
		    	    	{
		    	    		removeFromCollection(position);
		    	    	}
		    	    	else if (selection.equals("Fields"))
		    	    	{
		    	    		showDialog(DIALOG_EDIT_FIELDS);
		    	    	}
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
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.GONE);
		
		View content = findViewById(R.id.content);
		content.setVisibility(View.VISIBLE);
		
		if (!CollectionUtils.isEmpty(releases))
		{
			Collections.sort(releases, releaseArtistComparator);
			setListAdapter(new FolderReleaseEndlessAdapter(this, engine, releases, resourceUrl, this, selection));
		}
		
		loading = false;
	}

	@Override
	public void removeFromCollection(final int position) 
	{
		FolderReleaseEndlessAdapter folderReleaseEndlessAdapter = (FolderReleaseEndlessAdapter) getListAdapter();
    	final ReleaseAdapter releaseAdapter = (ReleaseAdapter) folderReleaseEndlessAdapter.getAdapter();
    	Release release = (Release) releaseAdapter.getItem(position);
    	final long releaseId = release.getId();
    	final long instanceId = release.getInstanceId();
    	final long folderId = release.getFolderId();
    	
		Thread thread = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				engine.deleteInstanceFromFolder(userName, folderId, releaseId, instanceId);
				handler.post(new Runnable() 
				{
					@Override
					public void run() 
					{
						releases.remove(position);
						releaseAdapter.setReleases(releases);
						releaseAdapter.notifyDataSetChanged();
//						getListView().setSelection(0);
					}
				});
			}
		});
		thread.start();
	}

	@Override
	public void editFields(int position) 
	{
		FolderReleaseEndlessAdapter folderReleaseEndlessAdapter = (FolderReleaseEndlessAdapter) getListAdapter();
    	final ReleaseAdapter releaseAdapter = (ReleaseAdapter) folderReleaseEndlessAdapter.getAdapter();
    	Release release = (Release) releaseAdapter.getItem(position);
    	
    	showDialog(DIALOG_EDIT_FIELDS);
	}

	@Override
	public void showOperations(int position) 
	{
		this.position = position;
		showDialog(DIALOG_OPERATIONS);
	}
}
