package com.discogs.adapters;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.commonsware.cwac.endless.EndlessAdapter;
import com.discogs.R;
import com.discogs.model.Release;
import com.discogs.services.Engine;
import com.discogs.utils.ReleaseArtistComparator;
import com.discogs.utils.ReleaseLabelComparator;

public class FolderReleaseEndlessAdapter extends EndlessAdapter 
{
	private LayoutInflater layoutInflater;
	
	private Engine engine;
	private int page = 1;
	private String resourceUrl;
	private List<Release> mReleases;
	private CharSequence selection;
	private ReleaseArtistComparator releaseArtistComparator = new ReleaseArtistComparator();
	private ReleaseLabelComparator releaseLabelComparator = new ReleaseLabelComparator();
	
	public FolderReleaseEndlessAdapter(Activity activity, Engine engine, List<Release> releases, String resourceUrl, RemoveButtonClickListener listener, CharSequence selection) 
	{
		super(new ReleaseAdapter(activity, releases, listener));
		this.engine = engine;
		this.layoutInflater = LayoutInflater.from(activity.getApplicationContext());
		this.resourceUrl = resourceUrl;
		this.selection = selection;
	}

	@Override
	protected View getPendingView(ViewGroup parent) 
	{
		View layout = layoutInflater.inflate(R.layout.list_item_release, null);
		
		View child = layout.findViewById(R.id.itemContent);
		child.setVisibility(View.GONE);
		
		child = layout.findViewById(R.id.itemLoading);
		child.setVisibility(View.VISIBLE);
		
		return layout;
	}

	@Override
	protected boolean cacheInBackground() 
	{
		boolean result = false;
		
		if (getWrappedAdapter().getCount() == page*50)
		{
			page++;
			mReleases = engine.listReleasesInFolder(resourceUrl, page);
		
			if (mReleases == null || mReleases.size() < 50)
			{
				result = false; // Stop loading more items
			}
			else
			{
				result = true;
			}
		}
	
		return result;
	}

	@Override
	protected void appendCachedData() 
	{
		if (mReleases != null)
		{
			ReleaseAdapter adapter = (ReleaseAdapter) getWrappedAdapter();
			adapter.getReleases().addAll(mReleases);
			
			if (selection.equals("Label"))
	    	{
				Collections.sort(adapter.getReleases(), releaseLabelComparator);
	    	}
			else if (selection.equals("Artist"))
	    	{
				Collections.sort(adapter.getReleases(), releaseArtistComparator);
	    	}
			
			adapter.notifyDataSetChanged();
		}
	}
	
	public ListAdapter getAdapter()
	{
		return getWrappedAdapter();
	}
}