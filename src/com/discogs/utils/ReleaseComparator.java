package com.discogs.utils;

import java.util.Comparator;

import com.discogs.model.Release;

public class ReleaseComparator implements Comparator<Release>
{
	@Override
	public int compare(Release release1, Release release2) 
	{
		if (release1 != null && release2 != null)
		{
			String year1 = release1.getYear();
			String year2 = release2.getYear();
		       
		    return year1.compareTo(year2);
		}
		else
		{
			return 0;
		}
	}
}
