package com.discogs.adapters;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.commonsware.cwac.endless.EndlessAdapter;
import com.discogs.activities.R;
import com.discogs.model.Release;
import com.discogs.services.Engine;

public class ArtistReleaseEndlessAdapter extends EndlessAdapter 
{
	private LayoutInflater layoutInflater;
	
	private Engine engine;
	private List<Release> mReleases;
	private int page = 1;
	private String releasesUrl;
	
	public ArtistReleaseEndlessAdapter(Activity activity, Engine engine, List<Release> releases, String releasesUrl) 
	{
		super(new ArtistReleaseAdapter(activity, releases));
		this.engine = engine;
		this.layoutInflater = LayoutInflater.from(activity.getApplicationContext());
		this.releasesUrl = releasesUrl;
	}

	@Override
	protected View getPendingView(ViewGroup parent) 
	{
		View layout = layoutInflater.inflate(R.layout.list_item_result, null);
		
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
			mReleases = engine.getArtistReleases(releasesUrl, page);
		
			if (mReleases == null || mReleases.size() == 0)
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

	public int getPage() 
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	@Override
	protected void appendCachedData() 
	{
		if (mReleases != null)
		{
			ArtistReleaseAdapter adapter = (ArtistReleaseAdapter) getWrappedAdapter();
			adapter.getReleases().addAll(mReleases);
			adapter.notifyDataSetChanged();
		}
	}
	
	public ListAdapter getAdapter()
	{
		return getWrappedAdapter();
	}
}