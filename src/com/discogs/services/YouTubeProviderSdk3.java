package com.discogs.services;

import android.content.Intent;

public class YouTubeProviderSdk3 extends YouTubeIntentProvider
{
	@Override
	public Intent getYouTubeIntent() 
	{
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEARCH);
		intent.setClassName("com.google.android.youtube", "com.google.android.youtube.QueryActivity");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 
		return intent;
	}
}
