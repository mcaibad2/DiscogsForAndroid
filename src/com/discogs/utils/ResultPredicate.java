package com.discogs.utils;

import org.apache.commons.collections.Predicate;

import com.discogs.model.Result;

public class ResultPredicate implements Predicate
{
	private String type;

	public ResultPredicate(String type)
	{
		this.type = type;
	}
	
	@Override
	public boolean evaluate(Object object) 
	{
		boolean accept = false;
		
		if (object instanceof Result)
		{
			Result result = (Result) object;
			
			if (result != null && result.getType().equals(type))
			{
				accept = true;
			}
		}
		
		return accept;
	}
}
