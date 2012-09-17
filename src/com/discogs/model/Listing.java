package com.discogs.model;

import java.util.List;

public class Listing 
{
	private String id;
	private String status;
	private String resourceUrl;
	private String posted;
	private String shipsFrom;
	private String comments;
	private String condition;
	private String sleeveCondition;
	private boolean audio;
	private boolean allowOffers;
	private List<Seller> sellers;
	private List<Price> prices;
	private List<Release> releases;
}
