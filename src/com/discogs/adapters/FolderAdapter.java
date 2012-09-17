package com.discogs.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.discogs.activities.R;
import com.discogs.model.Folder;

public class FolderAdapter extends BaseAdapter 
{
	private LayoutInflater layoutInflater;
	private List<Folder> folders;
	
	public FolderAdapter(Context context, List<Folder> folders) 
	{
		super();
		this.folders = folders;
		this.layoutInflater = LayoutInflater.from(context);
	}
	
	public int getCount() 
	{
		return folders.size();
	}

	public Folder getItem(int item) 
	{
		return folders.get(item);
	}

	public long getItemId(int position) 
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder viewHolder;
		Folder folder = folders.get(position);
        
		if (convertView == null)
		{
			convertView = layoutInflater.inflate(R.layout.layout_folderslist, null);
			viewHolder = new ViewHolder();
			viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
			viewHolder.itemsTextView = (TextView) convertView.findViewById(R.id.itemsTextView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.titleTextView.setText(folder.getName());
		viewHolder.itemsTextView.setText(folder.getCount() + " item(s)");
		
        return convertView;
    }

	static private class ViewHolder 
	{
		TextView titleTextView;
		TextView itemsTextView;
	}
}
