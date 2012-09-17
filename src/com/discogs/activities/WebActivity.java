package com.discogs.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.discogs.Constants;

public class WebActivity extends Activity
{
	private ProgressBar progressBar;
	private WebView webView;

	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_web);
		init();
    }

	private void init() 
	{
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		webView = (WebView) findViewById(R.id.webView);
		webView.setWebViewClient(new HelloWebViewClient());
		
		WebSettings settings = webView.getSettings();
//		settings.setEnableSmoothTransition(true);
		settings.setUserAgentString("desktop");
		settings.setJavaScriptEnabled(true);
		settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        settings.setSaveFormData(true);
        settings.setSavePassword(true);
		
		String authUrl = getIntent().getExtras().getString("authUrl");
		webView.loadUrl(Uri.parse(authUrl).toString());
	}
	
	/*******
	 * Menu
	 *******/
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();
		
		switch (id) 
		{
			case android.R.id.home:
			{
				Intent intent = new Intent(this, DashboardActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	startActivity(intent);
            	return true;
			}
			default:
			{
				return true;
			}
		}
	}
	
	private class HelloWebViewClient extends WebViewClient 
	{
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) 
	    {
	    	if (url.contains("callback"))
	    	{
	    		Intent intent = new Intent();
	    		intent.setData(Uri.parse(url));
	    		setResult(Constants.REQUEST_CODE_USER_LOGIN, intent);
	    		finish();
	    	}
	    	else
	    	{
	    		view.loadUrl(url);
	    	}
	        
	        return true;
	    }
	    
	    @Override
	    public void onPageStarted(WebView view, String url, Bitmap favicon) 
	    {
	    	super.onPageStarted(view, url, favicon);
	    	progressBar.setVisibility(View.VISIBLE);
	    	webView.setVisibility(View.GONE);
	    }
	    
	    @Override
	    public void onPageFinished(WebView view, String url) 
	    {
	    	super.onPageFinished(view, url);
	    	progressBar.setVisibility(View.GONE);
	    	webView.setVisibility(View.VISIBLE);
	    }
	}
}
