package com.discogs.model;

public class Fields {
	private long id;
    private long position;
    private String name;
    private boolean isPublic;
      
    @SuppressWarnings("unused")
    private enum Type {
    	Dropdown,
     	TextArea
    };
      
    @SuppressWarnings("unused")
    private enum Options {
    	Generic,
        NoCover,
        Mint,
        NearMint,
        VeryGoodPlus,
        VeryGood,
        GoodPlus,
        Good,
        Fair,
        Poor
	};
	private int lines;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getPosition() {
		return position;
	}
	
	public void setPosition(long position) {
		this.position = position;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isPublic() {
		return isPublic;
	}
	
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public int getLines() {
		return lines;
	}

	public void setLines(int lines) {
		this.lines = lines;
	}
}
