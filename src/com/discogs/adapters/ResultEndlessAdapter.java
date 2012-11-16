package com.discogs.adapters;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.commonsware.cwac.endless.EndlessAdapter;
import com.discogs.R;
import com.discogs.model.Result;
import com.discogs.services.Engine;

public class ResultEndlessAdapter extends EndlessAdapter 
{
	private LayoutInflater layoutInflater;
	
	private Engine engine;
	private List<Result> mResults;
	private int page = 1;
	private String query;
	private boolean stopLoading;
	
	public ResultEndlessAdapter(Activity activity, Engine engine, List<Result> results, String query) 
	{
		super(new ResultAdapter(activity, results));
		this.engine = engine;
		this.layoutInflater = LayoutInflater.from(activity.getApplicationContext());
		this.query = query;
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
			mResults = engine.search(query, page);
		
			if (mResults == null || mResults.size() == 0)
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
		if (mResults != null)
		{
			ResultAdapter adapter = (ResultAdapter) getWrappedAdapter();
			adapter.getResults().addAll(mResults);
			adapter.notifyDataSetChanged();
		}
	}
	
	public ListAdapter getAdapter()
	{
		return getWrappedAdapter();
	}
}