package com.discogs.model;

import java.util.ArrayList;
import java.util.List;

public class Field 
{
	private String name;
	private int lines;
	private long id;
	private int position;
	private String type;
	private boolean isPublic;
	private List<String> options = new ArrayList<String>();
	private String value;
	
	public String getValue() 
	{
		return value;
	}

	public void setValue(String value) 
	{
		this.value = value;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public int getLines() 
	{
		return lines;
	}
	
	public void setLines(int lines) 
	{
		this.lines = lines;
	}
	
	public long getId() 
	{
		return id;
	}

	public void setId(long id) 
	{
		this.id = id;
	}

	public int getPosition() 
	{
		return position;
	}
	
	public void setPosition(int position) 
	{
		this.position = position;
	}
	
	public String getType() 
	{
		return type;
	}
	
	public void setType(String type) 
	{
		this.type = type;
	}
	
	public boolean isPublic() 
	{
		return isPublic;
	}
	
	public void setPublic(boolean isPublic) 
	{
		this.isPublic = isPublic;
	}
	
	public List<String> getOptions() 
	{
		return options;
	}
	
	public void setOptions(List<String> options) 
	{
		this.options = options;
	}
}
