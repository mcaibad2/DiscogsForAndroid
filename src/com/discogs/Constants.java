package com.discogs;

import android.os.Build;

public class Constants 
{
	public static final String REQUEST_TOKEN_ENDPOINT_URL = "http://api.discogs.com/oauth/request_token";
	public static final String AUTHORIZATION_WEBSITE_URL = "http://www.discogs.com/oauth/authorize";
	public static final String ACCESS_TOKEN_ENDPOINT_URL = "http://api.discogs.com/oauth/access_token";
	
	public static final String CONSUMER_KEY = "FSCnhWmlXBWevBtbVXfY";
	public static final String CONSUMER_SECRET = "QZHOMAZpUJCiZtUjkDEuXEjMbIvBvnxA"; 	
	public static final String CALLBACK_URL = "callback://discogs";
	public static final String USER_AGENT = "DiscogsForAndroid/v2.0 +http://daskaswelt.appspot.com";
	
	public static final int REQUEST_CODE_USER_LOGIN = 0;
	
	public static int SDK_VERSION = Integer.parseInt(Build.VERSION.SDK);
	public static final int BASE = 1;
	public static final int BASE_1_1 = 2;
	public static final int CUPCAKE = 3;
	public static final int DONUT = 4;
	public static final int ECLAIR = 5;
	public static final int ECLAIR_0_1 = 6;
	public static final int ECLAIR_MR1 = 7;
	public static final int FROYO = 8;
	public static final int GINGERBREAD = 9;
	public static final int GINGERBREAD_MR1 = 10;
	public static final int HONEYCOMB = 11;
	public static final int HONEYCOMB_1 = 12;
	public static final int HONEYCOMB_2 = 13;
	public static final int ICE_CREAM_SANDWICH = 14;
	public static final int ICE_CREAM_SANDWICH_1 = 15;
	
	public static boolean SUPPORTS_ICECREAMSANDWICH = SDK_VERSION >= ICE_CREAM_SANDWICH;
	public static boolean SUPPORTS_HONEYCOMB = SDK_VERSION >= HONEYCOMB;
	public static boolean SUPPORTS_GINGERBREAD = SDK_VERSION >= GINGERBREAD;
	public static boolean SUPPORTS_FROYO = SDK_VERSION >= FROYO;
	public static boolean SUPPORTS_ECLAIR = SDK_VERSION >= ECLAIR;
	public static boolean TEST = SDK_VERSION >= ECLAIR;
}
