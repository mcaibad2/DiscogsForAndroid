package com.discogs.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.discogs.activities.R;
import com.discogs.cache.ImageLoader;
import com.discogs.model.MasterRelease;

public class MasterReleaseAdapter extends BaseAdapter
{
	private LayoutInflater layoutInflater;
	private List<MasterRelease> releases;
	private ImageLoader imageLoader;
	private StringBuffer stringBuffer = new StringBuffer();
	private Context context;
	
	public MasterReleaseAdapter(Context context, List<MasterRelease> releases) 
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
		MasterRelease release = releases.get(position);
		
		if (convertView == null)
		{
			convertView = layoutInflater.inflate(R.layout.list_item_master_release, null);
			viewHolder = new ViewHolder();
			viewHolder.thumbImageView = (ImageView) convertView.findViewById(R.id.thumbImageView);
			viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
			viewHolder.countryTextView = (TextView) convertView.findViewById(R.id.countryTextView);
			viewHolder.formatTextView = (TextView) convertView.findViewById(R.id.formatTextView);
			viewHolder.releasedTextView = (TextView) convertView.findViewById(R.id.releasedTextView);
			viewHolder.catnoTextView = (TextView) convertView.findViewById(R.id.catnoTextView);
			viewHolder.labelTextView = (TextView) convertView.findViewById(R.id.labelTextView);
			viewHolder.artistTextView = (TextView) convertView.findViewById(R.id.artistTextView);
			
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.titleTextView.setText(release.getTitle());
		
		if (release.getArtist() == null)
		{
			viewHolder.artistTextView.setVisibility(View.GONE);
		}
		else
		{
			viewHolder.artistTextView.setText(release.getArtist());
			viewHolder.artistTextView.setVisibility(View.VISIBLE);
		}
		
		
		if (release.getCountry() == null)
		{
			viewHolder.countryTextView.setVisibility(View.GONE);
		}
		else
		{
			viewHolder.countryTextView.setText(release.getCountry());
			viewHolder.countryTextView.setVisibility(View.VISIBLE);
		}
		
		if (release.getCountry() == null)
		{
			viewHolder.labelTextView.setVisibility(View.GONE);
		}
		else
		{
			viewHolder.labelTextView.setText(release.getLabel());
			viewHolder.labelTextView.setVisibility(View.VISIBLE);
		}
		
		if (release.getFormat() == null)
		{
			viewHolder.formatTextView.setVisibility(View.GONE);
		}
		else
		{
			viewHolder.formatTextView.setText(release.getFormat());
			viewHolder.formatTextView.setVisibility(View.VISIBLE);
		}
		
		if (release.getReleased() == null)
		{
			viewHolder.releasedTextView.setVisibility(View.GONE);
		}
		else
		{
			viewHolder.releasedTextView.setText(release.getReleased());
			viewHolder.releasedTextView.setVisibility(View.VISIBLE);
		}
		
		if (release.getCatno() == null)
		{
			viewHolder.catnoTextView.setVisibility(View.GONE);
		}
		else
		{
			viewHolder.catnoTextView.setText(release.getCatno());
			viewHolder.catnoTextView.setVisibility(View.VISIBLE);
		}
		
		viewHolder.thumbImageView.setImageResource(R.drawable.ic_release);
		
		if (release.getThumb() != null)
		{
			imageLoader.load(release.getThumb(), viewHolder.thumbImageView);
		}
		
		return convertView;
	}
	
	public List<MasterRelease> getReleases() 
	{
		return releases;
	}

	public void setReleases(List<MasterRelease> releases) 
	{
		this.releases = releases;
	}

	static class ViewHolder 
	{
		ImageView thumbImageView;
		TextView titleTextView;
		TextView countryTextView;
		TextView formatTextView;
		TextView releasedTextView;
		TextView labelTextView;
		TextView catnoTextView;
		TextView artistTextView;
	}
}
