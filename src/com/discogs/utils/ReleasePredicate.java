package com.discogs.utils;

import org.apache.commons.collections.Predicate;

import com.discogs.model.Release;

public class ReleasePredicate implements Predicate
{
	private String role;

	public ReleasePredicate(String role)
	{
		this.role = role;
	}
	
	@Override
	public boolean evaluate(Object object) 
	{
		boolean accept = false;
		
		if (object instanceof Release)
		{
			Release release = (Release) object;
			
			if (release != null && release.getRole().equalsIgnoreCase(role))
			{
				accept = true;
			}
		}
		
		return accept;
	}
}
