package com.discogs.adapters;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.commonsware.cwac.endless.EndlessAdapter;
import com.discogs.activities.R;
import com.discogs.model.Want;
import com.discogs.services.Engine;
import com.discogs.utils.WantArtistComparator;
import com.discogs.utils.WantLabelComparator;

public class WantEndlessAdapter extends EndlessAdapter 
{
	private LayoutInflater layoutInflater;
	
	private Engine engine;
	private List<Want> mWants;
	private int page = 1;
	private String resourceUrl;
	private CharSequence selection;
	private WantArtistComparator wantArtistComparator = new WantArtistComparator();
	private WantLabelComparator wantLabelComparator = new WantLabelComparator();
	
	public WantEndlessAdapter(Activity activity, Engine engine, List<Want> wants, String resourceUrl, CharSequence selection) 
	{
		super(new WantAdapter(activity, wants));
		this.engine = engine;
		this.layoutInflater = LayoutInflater.from(activity.getApplicationContext());
		this.resourceUrl = resourceUrl;
		this.selection = selection;
	}

	@Override
	protected View getPendingView(ViewGroup parent) 
	{
		View layout = layoutInflater.inflate(R.layout.list_item_want, null);
		
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
			mWants = engine.listWants(resourceUrl, page);
		
			if (mWants == null || mWants.size() < 50)
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
		if (mWants != null)
		{
			WantAdapter adapter = (WantAdapter) getWrappedAdapter();
			adapter.getWants().addAll(mWants);
			
			if (selection.equals("Label"))
	    	{
				Collections.sort(adapter.getWants(), wantLabelComparator);
	    	}
			else if (selection.equals("Artist"))
	    	{
				Collections.sort(adapter.getWants(), wantArtistComparator);
	    	}
			
			adapter.notifyDataSetChanged();
		}
	}
	
	public ListAdapter getAdapter()
	{
		return getWrappedAdapter();
	}
}