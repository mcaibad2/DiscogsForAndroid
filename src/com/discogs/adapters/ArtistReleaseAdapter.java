package com.discogs.adapters;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.discogs.activities.R;
import com.discogs.cache.ImageLoader;
import com.discogs.model.Release;

public class ArtistReleaseAdapter extends BaseAdapter
{
	private LayoutInflater layoutInflater;
	private List<Release> releases;
	private ImageLoader imageLoader;
	private StringBuffer stringBuffer = new StringBuffer();
	private Context context;
	
	public ArtistReleaseAdapter(Context context, List<Release> releases) 
	{
		super();
		this.context = context;
		this.releases = releases;
		this.layoutInflater = LayoutInflater.from(context);
		this.imageLoader = new ImageLoader(context);
	}
	
	@Override
	public int getCount() 
	{
		return releases.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return releases.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder viewHolder;
		Release release = releases.get(position);
		
		if (convertView == null)
		{
			convertView = layoutInflater.inflate(R.layout.list_item_artist_release, null);
			viewHolder = new ViewHolder();
			viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
			viewHolder.thumbImageView = (ImageView) convertView.findViewById(R.id.thumbImageView);
			viewHolder.yearTextView = (TextView) convertView.findViewById(R.id.yearTextView);
			viewHolder.roleTextView = (TextView) convertView.findViewById(R.id.roleTextView);
			
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.titleTextView.setText(release.getTitle());
		viewHolder.yearTextView.setText(release.getYear());
		viewHolder.thumbImageView.setImageResource(R.drawable.ic_release);
		
		if (release.getThumb() != null)
		{
			imageLoader.load(release.getThumb(), viewHolder.thumbImageView);
		}
		
		if (release.getRole() != null)
		{
			stringBuffer.setLength(0);
			stringBuffer.append("[");
			stringBuffer.append(StringUtils.capitalize(release.getRole()));
			stringBuffer.append("]");
			viewHolder.roleTextView.setText(stringBuffer.toString());
		}
		
		return convertView;
	}
	
	public List<Release> getReleases() 
	{
		return releases;
	}

	public void setReleases(List<Release> releases) 
	{
		this.releases = releases;
	}

	static class ViewHolder 
	{
		TextView titleTextView;
		TextView yearTextView;
		TextView roleTextView;
		ImageView thumbImageView;
	}
}
