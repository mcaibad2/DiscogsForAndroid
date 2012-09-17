package com.discogs.utils;

import java.util.Comparator;
import java.util.List;

import com.discogs.model.Artist;
import com.discogs.model.Want;

public class WantArtistComparator implements Comparator<Want>
{
	@Override
	public int compare(Want want1, Want want2) 
	{
		List<Artist> artists1 = want1.getBasicInformation().getArtists();
		List<Artist> artists2 = want2.getBasicInformation().getArtists();
		
		if (artists1 != null && artists2 != null)
		{
			String artistName1 = artists1.get(0).getName();
			String artistName2 = artists2.get(0).getName();
		       
		    return artistName1.compareTo(artistName2);
		}
		else
		{
			return 0;
		}
	}
}
