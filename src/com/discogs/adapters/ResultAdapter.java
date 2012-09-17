package com.discogs.adapters;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.discogs.activities.R;
import com.discogs.cache.ImageLoader;
import com.discogs.model.Result;

public class ResultAdapter extends BaseAdapter 
{
	private LayoutInflater layoutInflater;
	private List<Result> results;
	private ImageLoader imageLoader;
//	private ImageDownloader imageDownloader = new ImageDownloader();
	private StringBuffer stringBuffer = new StringBuffer();
	
	public ResultAdapter(Context context, List<Result> results) 
	{
		super();
		this.results = results;
		this.layoutInflater = LayoutInflater.from(context);
		this.imageLoader = new ImageLoader(context);
	}
	
	public int getCount() 
	{
		return results.size();
	}

	public Result getItem(int item) 
	{
		return results.get(item);
	}

	public long getItemId(int position) 
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder viewHolder;
		Result result = results.get(position);
        
		if (convertView == null)
		{
			convertView = layoutInflater.inflate(R.layout.list_item_result, null);
			viewHolder = new ViewHolder();
			viewHolder.thumbImageView = (ImageView) convertView.findViewById(R.id.thumbImageView);
			viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
			viewHolder.typeTextView = (TextView) convertView.findViewById(R.id.typeTextView);
			viewHolder.subTitleTextView = (TextView) convertView.findViewById(R.id.subTitleTextView);
			viewHolder.genreStyleTextView = (TextView) convertView.findViewById(R.id.genreStyleTextView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.thumbImageView.setImageResource(R.drawable.ic_release);
		
		if (result.getThumb() != null)
		{
			imageLoader.load(result.getThumb(), viewHolder.thumbImageView);
//			imageDownloader.download(result.getThumb(), viewHolder.thumbImageView);
		}
		
		viewHolder.titleTextView.setText(result.getTitle());
		
		stringBuffer.setLength(0);
		stringBuffer.append("[");
		stringBuffer.append(StringUtils.capitalize(result.getType()));
		stringBuffer.append("]");
		viewHolder.typeTextView.setText(stringBuffer.toString());
		
		if (result.getType().equals("release"))
		{
			stringBuffer.setLength(0);
			
			if (result.getYear() != null)
			{
				stringBuffer.append(result.getYear());
				stringBuffer.append(" | ");
			}
			
			for (String format : result.getFormats())
			{
				stringBuffer.append(format);
				stringBuffer.append(" | ");
			}
	
			stringBuffer.append(result.getLabel());
			stringBuffer.append(" | ");
			stringBuffer.append(result.getCatno());
			viewHolder.subTitleTextView.setText(stringBuffer.toString());
			viewHolder.subTitleTextView.setVisibility(View.VISIBLE);
			
			stringBuffer.setLength(0);
			
			if (result.getGenres() != null || result.getStyles() != null)
			{
				if (result.getGenres() != null)
				{
					for (String genre : result.getGenres())
					{
						stringBuffer.append(genre);
						stringBuffer.append(", ");
					}
				}
				
				if (result.getStyles() != null)
				{
					for (String style : result.getStyles())
					{
						stringBuffer.append(style);
						stringBuffer.append(", ");
					}
				}
				
				String temp = stringBuffer.toString();
				stringBuffer.setLength(0);
				stringBuffer.append(TextUtils.substring(temp, 0, temp.length() - 2));
				viewHolder.genreStyleTextView.setText(stringBuffer.toString());
				viewHolder.genreStyleTextView.setVisibility(View.VISIBLE);
			}
			else
			{
				viewHolder.genreStyleTextView.setVisibility(View.GONE);
			}
		}
		else
		{
			viewHolder.subTitleTextView.setVisibility(View.GONE);
			viewHolder.genreStyleTextView.setVisibility(View.GONE);
		}
		
        return convertView;
    }
	
	public List<Result> getResults() 
	{
		return results;
	}

	public void setResults(List<Result> results) 
	{
		this.results = results;
	}
	
	static private class ViewHolder 
	{
		ImageView thumbImageView;
		TextView titleTextView;
		TextView subTitleTextView;
		TextView typeTextView;
		TextView genreStyleTextView;
	}
}
