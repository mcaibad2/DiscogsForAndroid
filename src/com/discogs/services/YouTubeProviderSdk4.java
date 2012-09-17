package com.discogs.services;

import android.content.Intent;

public class YouTubeProviderSdk4 extends YouTubeIntentProvider
{
	@Override
	public Intent getYouTubeIntent() 
	{
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEARCH);
		intent.setPackage("com.google.android.youtube");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		return intent;
	}
}
