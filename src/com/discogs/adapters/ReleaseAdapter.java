package com.discogs.adapters;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.discogs.R;
import com.discogs.cache.ImageLoader;
import com.discogs.model.Artist;
import com.discogs.model.BasicInformation;
import com.discogs.model.Label;
import com.discogs.model.Release;
import org.apache.commons.lang.StringUtils;

public class ReleaseAdapter extends BaseAdapter
{
	private LayoutInflater layoutInflater;
	private List<Release> releases;
	private ImageLoader imageLoader;
	private StringBuffer stringBuffer = new StringBuffer();
	private Context context;
	private RemoveButtonClickListener listener;
	
	public ReleaseAdapter(Context context, List<Release> releases, RemoveButtonClickListener listener) 
	{
		super();
		this.context = context;
		this.releases = releases;
		this.layoutInflater = LayoutInflater.from(context);
		this.imageLoader = new ImageLoader(context);
		this.listener = listener;
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
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		ViewHolder viewHolder;
		Release release = releases.get(position);
		
		if (convertView == null)
		{
			convertView = layoutInflater.inflate(R.layout.list_item_release, null);
			viewHolder = new ViewHolder();
			viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
			viewHolder.thumbImageView = (ImageView) convertView.findViewById(R.id.thumbImageView);
			viewHolder.labelTextView = (TextView) convertView.findViewById(R.id.labelTextView);
			viewHolder.artistsTextView = (TextView) convertView.findViewById(R.id.artistsTextView);
			viewHolder.removeButton = (Button) convertView.findViewById(R.id.removeButton);
			viewHolder.fieldsButton = (Button) convertView.findViewById(R.id.fieldsButton);
			viewHolder.operationsImageButton = (Button) convertView.findViewById(R.id.operationsImageButton);
			
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		BasicInformation basicInformation = release.getBasicInformation();
		viewHolder.titleTextView.setText(basicInformation.getTitle());
		List<Label> labels = basicInformation.getLabels();

        // Labels
        if (labels != null && labels.size() > 0) {
            String[] labelsStrings = new String[labels.size()];
            for (int i = 0; i < labels.size(); i++) {
                Label label = labels.get(i);
                stringBuffer.append(label.getName());
                stringBuffer.append(" - ");
                stringBuffer.append(label.getCatNo());
                labelsStrings[i] = stringBuffer.toString();
            }
            viewHolder.labelTextView.setText(StringUtils.join(labelsStrings, ", "));
        }

        // Artists
        List<Artist> artists = basicInformation.getArtists();
        stringBuffer.setLength(0);
        if (artists != null && artists.size() > 0) {
            String[] artistStrings = new String[artists.size()];
            for (int i = 0; i < artists.size(); i++) {
                Artist artist = artists.get(0);
                artistStrings[i] = artist.getName();
            }
            viewHolder.artistsTextView.setText(StringUtils.join(artistStrings, ", "));
        }

		viewHolder.thumbImageView.setImageResource(R.drawable.ic_release);
		
		if (basicInformation.getThumb() != null)
		{
			imageLoader.load(basicInformation.getThumb(), viewHolder.thumbImageView);
		}
		
		viewHolder.removeButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view) 
			{
				listener.removeFromCollection(position);
			}
		});
		
		viewHolder.fieldsButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view) 
			{
				listener.editFields(position);
			}
		});
		
		viewHolder.operationsImageButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view) 
			{
				listener.showOperations(position);
			}
		});
		
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
		TextView labelTextView;
		TextView artistsTextView;
		ImageView thumbImageView;
		Button removeButton;
		Button fieldsButton;
		Button operationsImageButton;
	}
}
