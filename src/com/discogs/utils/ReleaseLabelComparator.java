package com.discogs.utils;

import java.util.Comparator;
import java.util.List;

import com.discogs.model.Label;
import com.discogs.model.Release;

public class ReleaseLabelComparator implements Comparator<Release>
{
	@Override
	public int compare(Release release1, Release release2) 
	{
		List<Label> labels1 = release1.getBasicInformation().getLabels();
		List<Label> labels2 = release2.getBasicInformation().getLabels();
		
		if (labels1 != null && labels1 != null)
		{
			String artistLabel1 = labels1.get(0).getName();
			String artistLabel2 = labels2.get(0).getName();
			
		    return artistLabel1.compareTo(artistLabel2);
		}
		else
		{
			return 0;
		}
	}
}
