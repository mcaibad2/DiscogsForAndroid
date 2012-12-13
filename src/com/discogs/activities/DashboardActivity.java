package com.discogs.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import oauth.signpost.OAuth;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.actionbar.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.discogs.Constants;
import com.discogs.R;
import com.discogs.model.Profile;
import com.discogs.services.Engine;
import com.discogs.utils.IntentIntegrator;
import com.discogs.utils.IntentResult;
import com.discogs.utils.Utils;

public class DashboardActivity extends ActionBarActivity 
{
	private static final int DIALOG_ABOUT = 0;
	private static final int DIALOG_LOGIN = 1;
	
	private Handler handler = new Handler();

	private ProgressBar progressBar;
	private View content;
	private Button searchButton;
	private Button loginButton;
	private Button profileButton;
	private Button collectionButton;
	private Button wantlistButton;
	private Button aboutButton;
	private Button barcodeButton;
	
	private CommonsHttpOAuthConsumer consumer;
    private CommonsHttpOAuthProvider provider;
	private SharedPreferences sharedPreferences;
	private Engine engine;
	private String authUrl;
      
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dashboard);
        init();
        // enableHttpResponseCache();
    }
    
	private void init() 
	{
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DashboardActivity.this);
		consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		provider = new CommonsHttpOAuthProvider(Constants.REQUEST_TOKEN_ENDPOINT_URL, Constants.ACCESS_TOKEN_ENDPOINT_URL, Constants.AUTHORIZATION_WEBSITE_URL);
		provider.setOAuth10a(true);
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		content = findViewById(R.id.content);
		
		collectionButton = (Button) findViewById(R.id.collectionButton);
		collectionButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view) 
			{
				startActivity(new Intent(DashboardActivity.this, CollectionActivity.class));
			}
		});
		
		wantlistButton = (Button) findViewById(R.id.wantlistButton);
		wantlistButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view) 
			{
				startActivity(new Intent(DashboardActivity.this, WantlistActivity.class));
			}
		});
		
		profileButton = (Button) findViewById(R.id.profileButton);
		profileButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view) 
			{
				startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
			}
		});
		
		searchButton = (Button) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view) 
			{
				onSearchRequested();
			}
		});
		
		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view) 
			{
				if (Utils.isNetworkAvailable(DashboardActivity.this))
				{
					Map<String, String> requestHeaders = provider.getRequestHeaders();
			    	requestHeaders.put("User-Agent", Constants.USER_AGENT);
			    	requestHeaders.put("Accept-Encoding", "gzip");
			         
			    	Thread thread = new Thread(new Runnable() 
			    	{
						@Override
						public void run() 
						{
							try 
					    	{
					    		authUrl = provider.retrieveRequestToken(consumer, Constants.CALLBACK_URL);
					    		
//					 			Uri uri = Uri.parse(authUrl);
//					 			startActivity(new Intent(Intent.ACTION_VIEW, uri));
					 			
					 			Intent intent = new Intent(DashboardActivity.this, WebActivity.class);
					 			intent.putExtra("authUrl", authUrl);
					 			startActivityForResult(intent, Constants.REQUEST_CODE_USER_LOGIN);
					    		
//					    		handler.post(new Runnable() 
//					    		{
//									@Override
//									public void run() 
//									{
//										showDialog(DIALOG_LOGIN);
//									}
//								});
					 		}
					    	catch (OAuthMessageSignerException e) 
					    	{
					 			e.printStackTrace();
					 		}
					    	catch (OAuthNotAuthorizedException e) 
					    	{
					 			e.printStackTrace();
					 		}
					    	catch (OAuthExpectationFailedException e) 
					    	{
					 			e.printStackTrace();
					 		}
					    	catch (OAuthCommunicationException e) 
					    	{
					    		e.printStackTrace();
					 		}
						}
					});
			    	thread.start();
				}
				else
				{
					Toast.makeText(DashboardActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		barcodeButton = (Button) findViewById(R.id.barcodeButton);
		barcodeButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view) 
			{
				if (engine == null)
				{
					CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
					SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DashboardActivity.this);
					String token = sharedPreferences.getString("token", null);
					String tokenSecret = sharedPreferences.getString("token_secret", null);
					consumer.setTokenWithSecret(token, tokenSecret);
					engine = new Engine(consumer);
				}
				
				IntentIntegrator intentIntegrator = new IntentIntegrator(DashboardActivity.this);
				List<String> desiredCodeFormats = new ArrayList<String>();
				desiredCodeFormats.add("UPC_A");
				intentIntegrator.initiateScan(desiredCodeFormats);
			}
		});
		
		aboutButton = (Button) findViewById(R.id.aboutButton);
		aboutButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view) 
			{
				showDialog(DIALOG_ABOUT);
			}
		});
		
		Button marketPlaceButton = (Button) findViewById(R.id.marketPlaceButton);
		marketPlaceButton.setVisibility(View.GONE);
		marketPlaceButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view) 
			{
				startActivity(new Intent(DashboardActivity.this, MarketPlaceActivity.class));
			}
		});
		
		// Check if token and tokensecret are already stored at app preferences
		String token = sharedPreferences.getString("token", null);
		String tokenSecret = sharedPreferences.getString("token_secret", null);
			    
		if (token == null || tokenSecret == null) 
		{
			loginButton.setVisibility(View.VISIBLE);
			collectionButton.setVisibility(View.GONE);
			wantlistButton.setVisibility(View.GONE);
			profileButton.setVisibility(View.GONE);
		}
		else
		{
			loginButton.setVisibility(View.GONE);
			collectionButton.setVisibility(View.VISIBLE);
			wantlistButton.setVisibility(View.VISIBLE);
			profileButton.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
    protected void onNewIntent(Intent intent) 
    {
    	super.onNewIntent(intent);
    	final Uri uri = intent.getData();
    	    
    	if (uri != null && uri.toString().contains(Constants.CALLBACK_URL)) 
    	{
    		progressBar.setVisibility(View.VISIBLE);
    		content.setVisibility(View.GONE);
    		
    		Thread thread = new Thread(new Runnable() 
    		{
				@Override
				public void run() 
				{
					try 
					{
						String uriString = URLDecoder.decode(uri.toString(), "UTF-8");
						Uri mUri = Uri.parse(uriString);
			    		final String verifier = mUri.getQueryParameter(OAuth.OAUTH_VERIFIER);
			    		
						provider.retrieveAccessToken(consumer, verifier);
						handler.post(new Runnable() 
						{
							@Override
							public void run() 
							{
								final String token = consumer.getToken();
				    	        final String tokenSecret = consumer.getTokenSecret();
				    	        consumer.setTokenWithSecret(token, tokenSecret);
				     	        
				    	        DashboardActivity.this.engine = new Engine(consumer);
				    	        
				    	        Thread aThread = new Thread(new Runnable() 
				    	        {
									@Override
									public void run() 
									{
										final String userName = engine.getUserName();
						    			final Profile profile = engine.getProfile(userName);
						    			handler.post(new Runnable() 
						    			{
											@Override
											public void run() 
											{
												// Store critical data at app preferences
								    	        Editor editor = sharedPreferences.edit();
								    	        editor.putString("token", token);
								    	        editor.putString("token_secret", tokenSecret);
								    	        editor.putString("user_name", userName);
								    	        
								    	        // Store some profile information at app preferences
								    	        if (profile != null)
								    	        {
								    	        	editor.putLong("id", profile.getId());
								    	        	editor.putString("resource_url", profile.getResourceUrl());
								    	        	editor.putString("inventory_url", profile.getInventoryUrl());
								    	        	editor.putString("collection_folders_url", profile.getCollectionFoldersUrl());
								    	        	editor.putString("collection_fields_url", profile.getCollectionFieldsUrl());
								    	        	editor.putString("wantlist_url", profile.getWantlistUrl());
								    	        }
								    	        
								    	        editor.commit();
								    	        
								    	        collectionButton.setVisibility(View.VISIBLE);
								    			wantlistButton.setVisibility(View.VISIBLE);
								    			profileButton.setVisibility(View.VISIBLE);
								    			loginButton.setVisibility(View.GONE);
								    			
								    			progressBar.setVisibility(View.GONE);
								        		content.setVisibility(View.VISIBLE);
											}
										});
									}
								});
				    	        aThread.start();
							}
						});
					}
					catch (OAuthMessageSignerException e) 
					{
						e.printStackTrace();
					}
					catch (OAuthNotAuthorizedException e) 
					{
						e.printStackTrace();
					}
					catch (OAuthExpectationFailedException e) 
					{
						e.printStackTrace();
					}
					catch (OAuthCommunicationException e) 
					{
						e.printStackTrace();
					} 
					catch (UnsupportedEncodingException e) 
					{
						e.printStackTrace();
					}
				}
			});
    		thread.start();
    	}
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (data != null)
		{
			if (requestCode == Constants.REQUEST_CODE_USER_LOGIN)
			{
				final Uri uri = data.getData();
	    	    
		    	if (uri != null && uri.toString().contains(Constants.CALLBACK_URL)) 
		    	{
		    		progressBar.setVisibility(View.VISIBLE);
		    		content.setVisibility(View.GONE);
		    		
		    		Thread thread = new Thread(new Runnable() 
		    		{
						@Override
						public void run() 
						{
							try 
							{
								String uriString = URLDecoder.decode(uri.toString(), "UTF-8");
								Uri mUri = Uri.parse(uriString);
					    		final String verifier = mUri.getQueryParameter(OAuth.OAUTH_VERIFIER);
					    		
								provider.retrieveAccessToken(consumer, verifier);
								handler.post(new Runnable() 
								{
									@Override
									public void run() 
									{
										final String token = consumer.getToken();
						    	        final String tokenSecret = consumer.getTokenSecret();
						    	        consumer.setTokenWithSecret(token, tokenSecret);
						     	        
						    	        DashboardActivity.this.engine = new Engine(consumer);
						    	        
						    	        Thread aThread = new Thread(new Runnable() 
						    	        {
											@Override
											public void run() 
											{
												final String userName = engine.getUserName();
								    			final Profile profile = engine.getProfile(userName);
								    			handler.post(new Runnable() 
								    			{
													@Override
													public void run() 
													{
														// Store critical data at app preferences
										    	        Editor editor = sharedPreferences.edit();
										    	        editor.putString("token", token);
										    	        editor.putString("token_secret", tokenSecret);
										    	        editor.putString("user_name", userName);
										    	        
										    	        // Store some profile information at app preferences
										    	        if (profile != null)
										    	        {
										    	        	editor.putLong("id", profile.getId());
										    	        	editor.putString("resource_url", profile.getResourceUrl());
										    	        	editor.putString("inventory_url", profile.getInventoryUrl());
										    	        	editor.putString("collection_folders_url", profile.getCollectionFoldersUrl());
										    	        	editor.putString("collection_fields_url", profile.getCollectionFieldsUrl());
										    	        	editor.putString("wantlist_url", profile.getWantlistUrl());
										    	        }
										    	        
										    	        editor.commit();
										    	        
										    	        collectionButton.setVisibility(View.VISIBLE);
										    			wantlistButton.setVisibility(View.VISIBLE);
										    			profileButton.setVisibility(View.VISIBLE);
										    			loginButton.setVisibility(View.GONE);
										    			
										    			progressBar.setVisibility(View.GONE);
										        		content.setVisibility(View.VISIBLE);
													}
												});
											}
										});
						    	        aThread.start();
									}
								});
							}
							catch (OAuthMessageSignerException e) 
							{
								e.printStackTrace();
							}
							catch (OAuthNotAuthorizedException e) 
							{
								e.printStackTrace();
							}
							catch (OAuthExpectationFailedException e) 
							{
								e.printStackTrace();
							}
							catch (OAuthCommunicationException e) 
							{
								e.printStackTrace();
							} 
							catch (UnsupportedEncodingException e) 
							{
								e.printStackTrace();
							}
						}
					});
		    		thread.start();
		    	}
			}
			else
			{
				IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
				String barcode = intentResult.getContents();
				
				if (barcode != null)
				{
					Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);
					intent.putExtra("barcode", barcode);
					startActivity(intent);
				}
			}
		}
		
//		Thread thread = new Thread(new Runnable() 
//		{
//			@Override
//			public void run() 
//			{
//				final String productName = engine.getProductJSON(code);
//				
//				handler.post(new Runnable() 
//				{
//					@Override
//					public void run() 
//					{
//						if (TextUtils.isEmpty(productName))
//						{
//							Toast.makeText(DashboardActivity.this, "No product found", Toast.LENGTH_LONG).show();
//						}
//						else
//						{
//							Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);
//							intent.putExtra("productName", productName);
//							startActivity(intent);
//						}
//					}
//				});
//			}
//		});
//		thread.start();
	}
	
	/*******
	 * Menu
	 *******/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
//		MenuInflater menuInflater = getMenuInflater();
//		menuInflater.inflate(R.menu.menu_dashboard, menu);

		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();
		
		switch (id) 
		{
			case R.id.menu_info:
			{
				showDialog(DIALOG_ABOUT);
	            return true;
			}
			default:
			{
				return true;
			}
		}
	}
	
	/**********
	 * Dialogs
	 **********/
	
	protected Dialog onCreateDialog(int id) 
	{
	    Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
	    switch(id) 
	    {
		    case DIALOG_ABOUT:
		    {
		    	builder.setTitle("About");
		    	View layout = LayoutInflater.from(this).inflate(R.layout.layout_about, null);
		    	
		    	try 
		    	{
		    		PackageInfo packageInfo = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0 );
					String versionName = packageInfo.versionName;
					int versionCode = packageInfo.versionCode;
		            TextView versionTextView = (TextView) layout.findViewById(R.id.versionTextView);
			    	versionTextView.setText(String.format(getResources().getString(R.string.about_message), versionName, versionCode));
		        } 
		    	catch (Exception e) 
		    	{
		    	}
				
		    	builder.setView(layout);
		    	builder.setCancelable(false);
		    	builder.setInverseBackgroundForced(true);
		    	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		    	{
		    		public void onClick(DialogInterface dialog, int id) 
		    		{
		    		}
		    	});
		    	dialog = builder.create();
		        break;
		    }
		    case DIALOG_LOGIN:
		    {
		    	View view = LayoutInflater.from(this).inflate(R.layout.layout_web, null);
				builder.setView(view);
		    	
		    	WebView webView = (WebView) view.findViewById(R.id.webView);
		    	webView.setWebViewClient(new HelloWebViewClient());
				
				WebSettings settings = webView.getSettings();
				settings.setUserAgent(1);
				settings.setJavaScriptEnabled(true);
				settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
				settings.setBuiltInZoomControls(true);
		        settings.setSupportZoom(true);
				
				// webView.loadUrl("http://www.google.com");
				
				webView.loadUrl(Uri.parse(authUrl).toString());
		    	
		    	dialog = builder.create();
		        break;
		    }
		    default:
		    {
		    	dialog = null;
		    }
	    }
	    
	    return dialog;
	}
	
	/*****************
	 * Helper methods
	 *****************/
	
	public String convertStreamToString(InputStream is) throws IOException 
	{
		if (is != null) 
		{
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
	        
			try 
			{
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
	            
				while ((n = reader.read(buffer)) != -1) 
				{
					writer.write(buffer, 0, n);
				}
			}
			finally 
			{
				is.close();
			}
	        
			return writer.toString();
		}
		else 
		{        
			return "";
		}
	}
	
	private void enableHttpResponseCache() 
	{
	    try 
	    {
	        long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
	        File httpCacheDir = new File(getCacheDir(), "http");
	        Class.forName("android.net.http.HttpResponseCache").getMethod("install", File.class, long.class).invoke(null, httpCacheDir, httpCacheSize);
	    } 
	    catch (Exception httpResponseCacheNotAvailable) 
	    {
	    }
	}
	
	private class HelloWebViewClient extends WebViewClient 
	{
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) 
	    {
	        view.loadUrl(url);
	        return true;
	    }
	}
}