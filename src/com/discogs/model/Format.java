package com.discogs.model;

import java.util.ArrayList;
import java.util.List;

public class Format 
{
	private String text;
	private int qty;
	private List<String> descriptions = new ArrayList<String>();
	private String name;
	
	public String getText() 
	{
		return text;
	}
	
	public void setText(String text) 
	{
		this.text = text;
	}
	
	public int getQty() 
	{
		return qty;
	}
	
	public void setQty(int qty) 
	{
		this.qty = qty;
	}
	
	public List<String> getDescriptions() 
	{
		return descriptions;
	}
	
	public void setDescriptions(List<String> descriptions) 
	{
		this.descriptions = descriptions;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
}
