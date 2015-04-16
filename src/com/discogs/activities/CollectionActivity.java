package com.discogs.activities;

import java.util.List;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.commons.lang.StringUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.actionbar.ActionBarListActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.discogs.Constants;
import com.discogs.R;
import com.discogs.adapters.FolderAdapter;
import com.discogs.model.Folder;
import com.discogs.services.Engine;

public class CollectionActivity extends ActionBarListActivity 
{
	private static final int DIALOG_ADD_FOLDER = 0;
	
	private Handler handler = new Handler();

	private List<Folder> folders;
	private Engine engine;
	private String userName;
	private String collectionFoldersUrl;
	private boolean loading = true;
      
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_collection);
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
		this.userName = sharedPreferences.getString("user_name", null);
		this.collectionFoldersUrl = sharedPreferences.getString("collection_folders_url", null);
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		Thread thread = new Thread(new Runnable() 
		{
			public void run() 
			{
				folders = engine.listFolders(userName, collectionFoldersUrl);
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
		Folder folder = ((FolderAdapter) getListAdapter()).getItem(position);
		Intent intent = new Intent(this, FolderReleasesActivity.class);
		intent.putExtra("resourceUrl", folder.getResourceUrl());
		intent.putExtra("name", folder.getName());
		intent.putExtra("folderId", folder.getId());
		startActivity(intent);
	}
	
	/*******
	 * Menu
	 *******/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// MenuInflater menuInflater = getMenuInflater();
		// menuInflater.inflate(R.menu.menu_collection, menu);

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

	    switch(id) 
	    {
		    case DIALOG_ADD_FOLDER:
		    {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Folder name");
				final View view = LayoutInflater.from(this).inflate(R.layout.layout_add_folder, null);
				builder.setView(view);
				builder.setPositiveButton("OK", new OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						EditText folderNameEditText = (EditText) view.findViewById(R.id.folderNameEditText);
						String folderName = folderNameEditText.getText().toString();
						
						if (StringUtils.isNotBlank(folderName))
						{
							String resourceUrl = "http://api.discogs.com/users/" + userName + "/collection/folders";
							engine.createFolder(resourceUrl, folderName);
						}
					}
				});
				builder.setNegativeButton("Cancel", new OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
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
		// NOTE: does the progress bar start automatically?
		// Answer: Yes
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.GONE);
		
		View content = findViewById(R.id.content);
		content.setVisibility(View.VISIBLE);
		
		setListAdapter(new FolderAdapter(this, folders));
		loading = false;
	}
}