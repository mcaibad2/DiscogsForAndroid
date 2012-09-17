package com.discogs.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.zip.GZIPInputStream;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class NetworkHelper 
{
	private DefaultHttpClient httpClient;
	private CommonsHttpOAuthConsumer consumer;
	
	public NetworkHelper(CommonsHttpOAuthConsumer consumer) 
	{
		this.consumer = consumer;
		this.initHTTPClient();
	}
	
	private void initHTTPClient() 
	{
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setContentCharset(params, "utf-8");
		params.setBooleanParameter("http.protocol.expect-continue", false);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);
		
		ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager(params, schemeRegistry);
		
		httpClient = new DefaultHttpClient(connectionManager, params);
		httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(3, false));
		httpClient.addRequestInterceptor(new HttpRequestInterceptor() 
		{
            public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException 
            {
                if (!request.containsHeader("Accept-Encoding")) 
                {
                    request.addHeader("Accept-Encoding", "gzip");
                }
            }
        });
		httpClient.addResponseInterceptor(new HttpResponseInterceptor() 
		{
            public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException 
            {
                HttpEntity entity = response.getEntity();
                
                if (entity != null)
                {
                	 Header ceheader = entity.getContentEncoding();
                     
                     if (ceheader != null) 
                     {
                         HeaderElement[] codecs = ceheader.getElements();
                         
                         for (int i = 0; i < codecs.length; i++) 
                         {
                             if (codecs[i].getName().equalsIgnoreCase("gzip")) 
                             {
                                 response.setEntity(new GzipDecompressingEntity(response.getEntity())); 
                                 return;
                             }
                         }
                     }
                }
            }
        });
	}

	public String doHTTPGet(String uri)
	{
		Log.d("Discogs", "Requesting:" + uri);
		String response = null;
		HttpGet hTTPGet = new HttpGet(uri);

		try 
		{
			consumer.sign(hTTPGet);
			HttpResponse hTTPResponse = httpClient.execute(hTTPGet);
			response = EntityUtils.toString(hTTPResponse.getEntity());
			Log.d("Discogs", "Response: " + response);
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (OAuthMessageSignerException e) 
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
		
		return response;
	}
	
	public String doHTTPDelete(String uri)
	{
		Log.d("Discogs", "Requesting:" + uri);
		String response = null;
		HttpDelete hTTPdelete = new HttpDelete(uri);
		
		try 
		{
			consumer.sign(hTTPdelete);
			httpClient.execute(hTTPdelete);
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (OAuthMessageSignerException e) 
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
		
		return response;
	}
	
	public String doHTTPPost(String uri) 
	{
		String response = null;
		HttpPost hTTPPost = new HttpPost(uri);
		
		try 
		{
			consumer.sign(hTTPPost);
			HttpResponse hTTPResponse = httpClient.execute(hTTPPost);
			response = EntityUtils.toString(hTTPResponse.getEntity());
			Log.d("Discogs", "Response: " + response);
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (OAuthMessageSignerException e) 
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
		
		return response;	
	}
	
	public String doJsonHTTPPost(String uri, String value) 
	{
		String response = null;
		HttpPost post = new HttpPost(uri);
		post.setHeader("Content-Type", "application/json");
		
		try 
		{
			JSONObject jSONObject = new JSONObject();
			jSONObject.put("value", value);
			
//			StringEntity entity = new StringEntity(jSONObject.toString(), "UTF-8");
//			entity.setContentType("application/json");
//			post.setEntity(entity);
			
			consumer.sign(post);
			HttpResponse hTTPResponse = httpClient.execute(post);
			response = EntityUtils.toString(hTTPResponse.getEntity());
			Log.d("Discogs", "Response: " + response);
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (OAuthMessageSignerException e) 
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
		
		return response;
	} 
	
	public String doHTTPPost(String uri, List<NameValuePair> nameValuePairs) 
	{
		String response = null;
		HttpPost hTTPPost = new HttpPost(uri);
		
		try 
		{
			hTTPPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			consumer.sign(hTTPPost);
			HttpResponse hTTPResponse = httpClient.execute(hTTPPost);
			response = EntityUtils.toString(hTTPResponse.getEntity());
			Log.d("Discogs", "Response: " + response);
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (OAuthMessageSignerException e) 
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
		
		return response;
	} 
	
	public String doHTTPPut(String uri) throws Exception
	{
		Log.d("Discogs", "Requesting:" + uri);
		String response = null;
		HttpPut hTTPPut = new HttpPut(uri);

		consumer.sign(hTTPPut);
		HttpResponse hTTPResponse = httpClient.execute(hTTPPut);
		//int statusCode = hTTPResponse.getStatusLine().getStatusCode();
		//response = hTTPResponse.getStatusLine().getReasonPhrase();
		response = EntityUtils.toString(hTTPResponse.getEntity());
		Log.d("Discogs", "Response: " + response);
		
		return response;
	}
	
	static class GzipDecompressingEntity extends HttpEntityWrapper 
	{
        public GzipDecompressingEntity(final HttpEntity entity) 
        {
            super(entity);
        }
    
        @Override
        public InputStream getContent() throws IOException, IllegalStateException 
        {
            // the wrapped entity's getContent() decides about repeatability
            InputStream wrappedin = wrappedEntity.getContent();

            return new GZIPInputStream(wrappedin);
        }

        @Override
        public long getContentLength() 
        {
            // length of ungzipped content is not known
            return -1;
        }
    }
}
