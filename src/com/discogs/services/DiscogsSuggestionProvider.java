package com.discogs.services;

import android.content.SearchRecentSuggestionsProvider;

public class DiscogsSuggestionProvider extends SearchRecentSuggestionsProvider 
{
	public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;
	public final static String AUTHORITY = "com.discogs.services.DiscogsSuggestionProvider";

    public DiscogsSuggestionProvider() 
    {
        setupSuggestions(AUTHORITY, MODE);
    }
}