package com.discogs.activities;

import java.util.List;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.commons.lang.StringUtils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.actionbar.ActionBarActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.discogs.Constants;
import com.discogs.R;
import com.discogs.adapters.ImageAdapter;
import com.discogs.cache.ImageLoader;
import com.discogs.model.Artist;
import com.discogs.model.Format;
import com.discogs.model.Image;
import com.discogs.model.Label;
import com.discogs.model.Release;
import com.discogs.model.Track;
import com.discogs.model.Video;
import com.discogs.services.Engine;
import com.discogs.utils.Utils;

public class WantActivity extends ActionBarActivity
{
	private Handler handler = new Handler();
	private LayoutInflater layoutInflater;
	
	private Release release;
	private Engine engine;
	private String resourceUrl;
	private String title;
	private String userName;
	private StringBuffer stringBuffer = new StringBuffer();
	private ImageLoader imageLoader;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_want);
		init();
    }

	private void init() 
	{
		this.layoutInflater = LayoutInflater.from(this);
		this.imageLoader = new ImageLoader(this);
		
		CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		String token = sharedPreferences.getString("token", null);
		String tokenSecret = sharedPreferences.getString("token_secret", null);
		consumer.setTokenWithSecret(token, tokenSecret);
		this.engine = new Engine(consumer);
		this.userName = sharedPreferences.getString("user_name", null);
	
		Bundle extras = getIntent().getExtras();
		this.resourceUrl = extras.getString("resourceUrl");
		this.title = extras.getString("title");
		setTitle(title);
		
		Thread thread = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				WantActivity.this.release = engine.getRelease(resourceUrl);
				handler.post(new Runnable() 
				{
					@Override
					public void run() 
					{
						showUI();
					}
				});
			}
		});
		thread.start();
	}
	
	/*******
	 * Menu
	 *******/
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();
		
		switch (id) 
		{
			case android.R.id.home:
			{
				Intent intent = new Intent(this, DashboardActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	startActivity(intent);
            	return true;
			}
			default:
			{
				return true;
			}
		}
	}
	
	/*****************
	 * Helper methods
	 *****************/
	
	private void showUI() 
	{
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.GONE);
		
		View content = findViewById(R.id.content);
		content.setVisibility(View.VISIBLE);
		
		if (release != null)
		{
//			final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
//			ratingBar.setNumStars(release.getRating());
			
//			final EditText notesEditText = (EditText) findViewById(R.id.notesEditText);
//			notesEditText.setText(release.getNotes());
//			
//			final CheckBox notesPublicCheckbox = (CheckBox) findViewById(R.id.notesPublicCheckbox);
//			notesPublicCheckbox.setChecked(release.isNotesPublic());
			
			// Artists
			TextView artistsTextView = (TextView) findViewById(R.id.artistsTextView);
			List<Artist> artists = release.getArtists();
			
			if (artists != null && artists.size() > 0)
			{
				stringBuffer.setLength(0);
				
				if (artists.size() == 1)
				{
					Artist artist = artists.get(0);
					stringBuffer.append(artist.getName());
					artistsTextView.setText(stringBuffer.toString());
				}
				else
				{
					for (Artist artist: artists)
					{
						stringBuffer.append(artist.getName());
						stringBuffer.append(", ");
					}
					
					artistsTextView.setText(StringUtils.strip(stringBuffer.toString(), ", "));
				}
			}
			
			// Title
			TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
			titleTextView.setText(release.getTitle());
			
			// Labels
			TextView labelTextView = (TextView) findViewById(R.id.labelTextView);
			
			if (release.getLabels() != null)
			{
				if (release.getLabels().size() > 1)
				{
					stringBuffer.setLength(0);
					
					for (Label label : release.getLabels())
					{
						stringBuffer.append(label.getName());
						stringBuffer.append(" - ");
						stringBuffer.append(label.getCatNo());
						stringBuffer.append(", ");
					}
				}
				else
				{
					stringBuffer.setLength(0);
					
					Label label = release.getLabels().get(0);
					stringBuffer.append(label.getName());
					stringBuffer.append(" - ");
					stringBuffer.append(label.getCatNo());
				}

				labelTextView.setText(stringBuffer.toString());
			}
			else
			{
				findViewById(R.id.labelTableRow).setVisibility(View.GONE);
			}
			
			TextView countryTextView = (TextView) findViewById(R.id.countryTextView);
			
			if (release.getCountry() == null)
			{
				findViewById(R.id.countryTableRow).setVisibility(View.GONE);
			}
			else
			{
				countryTextView.setText(release.getCountry());
			}
			
			TextView releasedTextView = (TextView) findViewById(R.id.releasedTextView);
			
			if (release.getReleased() == null)
			{
				findViewById(R.id.releasedTableRow).setVisibility(View.GONE);
			}
			else
			{
				releasedTextView.setText(release.getReleased());
			}
			
			// Genres
			TextView genreTextView = (TextView) findViewById(R.id.genreTextView);
			
			if (release.getGenres() != null)
			{
				if (release.getGenres().size() > 1)
				{
					stringBuffer.setLength(0);
					
					for (String genre : release.getGenres())
					{
						stringBuffer.append(genre);
						stringBuffer.append(", ");
					}
					
					genreTextView.setText(StringUtils.strip(stringBuffer.toString(), ", "));
				}
				else
				{
					genreTextView.setText(release.getGenres().get(0));
				}
			}
			else
			{
				findViewById(R.id.genreTableRow).setVisibility(View.GONE);
			}
			
			// Styles
			TextView styleTextView = (TextView) findViewById(R.id.styleTextView);
			List<String> styles = release.getStyles();
			
			if (styles != null && styles.size() > 0)
			{
				if (styles.size() > 1)
				{
					stringBuffer.setLength(0);
					
					for (String style : styles)
					{
						stringBuffer.append(style);
						stringBuffer.append(", ");
					}
					
					String labels = stringBuffer.toString();
					styleTextView.setText(TextUtils.substring(labels, 0, labels.length() - 2));
				}
				else
				{
					styleTextView.setText(styles.get(0));
				}
			}
			else
			{
				findViewById(R.id.styleTableRow).setVisibility(View.GONE);
			}
			
			// Gallery
			Gallery gallery = (Gallery) findViewById(R.id.gallery);
			ImageView imageView = (ImageView) findViewById(R.id.imageView);
			
			if (release.getImages() != null && release.getImages().size() > 0)
			{
				ImageAdapter galleryImageAdapter = new ImageAdapter(WantActivity.this, release.getImages());
				gallery.setAdapter(galleryImageAdapter);
				gallery.setOnItemClickListener(new OnItemClickListener() 
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
					{
						Intent intent = new Intent(WantActivity.this, ImageActivity.class);
						intent.putExtra("title", release.getTitle());
						intent.putExtra("url", release.getImages().get(position).getResourceUrl());
						startActivity(intent);
					}
				});
				
				String imageUri = null;
				
				for (Image image : release.getImages())
				{
					if (image.getType().equals("secondary"))
					{
						continue;
					}
					else
					{
						imageUri = image.getUri150();
					}
				}
				
				if (imageUri == null)
				{
					imageUri = release.getImages().get(0).getUri150();
				}
				
				if (imageUri != null)
				{
					imageLoader.load(imageUri, imageView);
				}
				
				imageView.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View view) 
					{
						Intent intent = new Intent(WantActivity.this, ImageActivity.class);
						intent.putExtra("title", release.getTitle());
						intent.putExtra("url", release.getImages().get(0).getResourceUrl());
						startActivity(intent);
					}
				});
			}
			else
			{
				TextView imagesHeader = (TextView) findViewById(R.id.imagesHeader);
				imagesHeader.setVisibility(View.GONE);
				gallery.setVisibility(View.GONE);
			}
			
			TextView notesTextView = (TextView) findViewById(R.id.notesTextView);
			
			if (release.getNotes() == null)
			{
				notesTextView.setVisibility(View.GONE);
				
				TextView notesHeader = (TextView) findViewById(R.id.notesHeader);
				notesHeader.setVisibility(View.GONE);
			}
			else
			{
				notesTextView.setText(Html.fromHtml(release.getNotes()));
			}
			
			// Tracks
			TableLayout tracklistLayout = (TableLayout) findViewById(R.id.tracklistLayout);
			
			for (Track track : release.getTracks())
			{
				TableRow tableRow = new TableRow(this);
				
				TextView textView = new TextView(this);
				textView.setText(track.getPosition());
				tableRow.addView(textView);
				
				textView = new TextView(this);
				TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
				layoutParams.setMargins(10, 0, 0, 0);
				textView.setLayoutParams(layoutParams);
				textView.setText(Html.fromHtml(track.getTitle()));
				tableRow.addView(textView);
				
				textView = new TextView(this);
				textView.setText(track.getDuration());
				tableRow.addView(textView);
				
				tracklistLayout.addView(tableRow);
			}
			
			// Formats
			TextView formatTextView = (TextView) findViewById(R.id.formatTextView);
			
			if (release.getFormats() != null)
			{
				if (release.getFormats().size() > 1)
				{
					stringBuffer.setLength(0);
					
					for (Format format : release.getFormats())
					{
						stringBuffer.append(format.getName());
						stringBuffer.append(", ");
					}
					
					String labels = stringBuffer.toString();
					formatTextView.setText(TextUtils.substring(labels, 0, labels.length() - 2));
				}
				else
				{
					formatTextView.setText(release.getFormats().get(0).getName());
				}
			}
			else
			{
				findViewById(R.id.formatTableRow).setVisibility(View.GONE);
			}
			
			// Companies
			TextView companiesTextView = (TextView) findViewById(R.id.companiesTextView);
			
			if (release.getCompanies() != null && release.getCompanies().size() > 0)
			{
				if (release.getCompanies().size() > 1)
				{
					stringBuffer.setLength(0);
					
					for (Label label : release.getCompanies())
					{
						stringBuffer.append(label.getEntityTypeName());
						stringBuffer.append(" - ");
						stringBuffer.append(label.getName());
						stringBuffer.append("\n");
					}
					
					String labels = stringBuffer.toString();
					companiesTextView.setText(TextUtils.substring(labels, 0, labels.length() - 2));
				}
				else
				{
					Label company = release.getCompanies().get(0);
					stringBuffer.setLength(0);
					stringBuffer.append(company.getEntityTypeName());
					stringBuffer.append(", ");
					stringBuffer.append(company.getName());
					companiesTextView.setText(stringBuffer.toString());
				}
			}
			else
			{
				companiesTextView.setVisibility(View.GONE);
				findViewById(R.id.companiesHeader).setVisibility(View.GONE);
			}
			
			// Videos
			TextView videosHeader = (TextView) findViewById(R.id.videosHeader);
			LinearLayout videosLayout = (LinearLayout) findViewById(R.id.videosLayout);
			List<Video> videos = release.getVideos();
			
			if (videos != null && videos.size() > 0)
			{
				for (final Video video : videos)
				{
					LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.layout_video, null);
					linearLayout.setOnClickListener(new OnClickListener() 
					{
						@Override
						public void onClick(View view) 
						{
							startActivity(new Intent(Intent.ACTION_VIEW,  Uri.parse(video.getUri())));
						}
					});
					
					TextView mTitleTextView = (TextView) linearLayout.findViewById(R.id.titleTextView);
					mTitleTextView.setText(video.getTitle());
					
					// TextView mDescriptionTextView = (TextView) linearLayout.findViewById(R.id.descriptionTextView);
					// mDescriptionTextView.setText(video.getDescription());
					
					TextView mDurationTextView = (TextView) linearLayout.findViewById(R.id.durationTextView);
					
					if (video.getDuration() > 0)
					{
						mDurationTextView.setText(Utils.splitToComponentTimes(Long.valueOf(video.getDuration())));
					}
					else
					{
						mDurationTextView.setVisibility(View.GONE);
					}
		
					videosLayout.addView(linearLayout);
				}
			}
			else
			{
				videosHeader.setVisibility(View.GONE);
				videosLayout.setVisibility(View.GONE);
			}
			
//			Button saveButton = (Button) findViewById(R.id.saveButton);
//			saveButton.setOnClickListener(new OnClickListener() 
//			{
//				@Override
//				public void onClick(View view) 
//				{
//					int stars = ratingBar.getNumStars();
//					String notes = notesEditText.getText().toString();
//					boolean notesPublic = notesPublicCheckbox.isChecked();
//					
//					engine.addReleaseToWantlist(userName, release.getId(), stars, notes, notesPublic);
//				}
//			});
		}
	}
}
