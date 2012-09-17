package com.discogs;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.app.Application;

public class DiscogsForAndroidApplication extends Application
{
	private DefaultHttpClient httpClient;
	
	@Override
	public void onTerminate() 
	{
		super.onTerminate();
		shutDownClientConnectionManager();
	}
	
	private void shutDownClientConnectionManager() 
	{
		if (httpClient != null && httpClient.getConnectionManager() != null)
		{
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	public DefaultHttpClient getHttpClient() 
	{
		if (httpClient == null)
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
	            
	        });
		}
		
		return httpClient;
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
