package com.discogs.utils;

import java.util.Comparator;
import java.util.List;

import com.discogs.model.Artist;
import com.discogs.model.Label;
import com.discogs.model.Want;

public class WantLabelComparator implements Comparator<Want>
{
	@Override
	public int compare(Want want1, Want want2) 
	{
		List<Label> labels1 = want1.getBasicInformation().getLabels();
		List<Label> labels2 = want2.getBasicInformation().getLabels();
		
		if (labels1 != null && labels2 != null)
		{
			String label1 = labels1.get(0).getName();
			String label2 = labels2.get(0).getName();
		       
		    return label1.compareTo(label2);
		}
		else
		{
			return 0;
		}
	}
}
