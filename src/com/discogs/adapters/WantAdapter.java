package com.discogs.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.discogs.activities.R;
import com.discogs.cache.ImageLoader;
import com.discogs.model.Artist;
import com.discogs.model.BasicInformation;
import com.discogs.model.Label;
import com.discogs.model.Want;

public class WantAdapter extends BaseAdapter
{
	private LayoutInflater layoutInflater;
	private List<Want> wants;
	private ImageLoader imageLoader;
	private StringBuffer stringBuffer = new StringBuffer();
	private Context context;
	
	public WantAdapter(Context context, List<Want> wants) 
	{
		super();
		this.context = context;
		this.wants = wants;
		this.layoutInflater = LayoutInflater.from(context);
		this.imageLoader = new ImageLoader(context);
	}
	
	@Override
	public int getCount() 
	{
		return wants.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return wants.get(position);
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
		Want want = wants.get(position);
		
		if (convertView == null)
		{
			convertView = layoutInflater.inflate(R.layout.list_item_want, null);
			viewHolder = new ViewHolder();
			viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
			viewHolder.thumbImageView = (ImageView) convertView.findViewById(R.id.thumbImageView);
			viewHolder.labelsLayout = (LinearLayout) convertView.findViewById(R.id.labelsLayout);
			viewHolder.artistsTextView = (TextView) convertView.findViewById(R.id.artistsTextView);
			
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		BasicInformation basicInformation = want.getBasicInformation();
		viewHolder.titleTextView.setText(basicInformation.getTitle());
		List<Label> labels = basicInformation.getLabels();
		viewHolder.labelsLayout.removeAllViews();
		
		if (labels != null)
		{
			for (Label label : labels)
			{
				stringBuffer.setLength(0);
				stringBuffer.append(label.getName());
				stringBuffer.append(" - ");
				stringBuffer.append(label.getCatNo());
				TextView textView = new TextView(context);
				textView.setText(stringBuffer.toString());
				viewHolder.labelsLayout.addView(textView);
			}
		}
		
		List<Artist> artists = basicInformation.getArtists();
		
		for (Artist artist : artists)
		{
			stringBuffer.setLength(0);
			stringBuffer.append(artist.getName());
		}
		
		viewHolder.artistsTextView.setText(stringBuffer.toString());
		
		viewHolder.thumbImageView.setImageResource(R.drawable.ic_release);
		
		if (basicInformation.getThumb() != null)
		{
			imageLoader.load(basicInformation.getThumb(), viewHolder.thumbImageView);
		}
		
		return convertView;
	}
	
	public List<Want> getWants() 
	{
		return wants;
	}

	public void setWants(List<Want> wants) 
	{
		this.wants = wants;
	}
	
	static class ViewHolder 
	{
		TextView titleTextView;
		LinearLayout labelsLayout;
		TextView artistsTextView;
		ImageView thumbImageView;
	}
}
