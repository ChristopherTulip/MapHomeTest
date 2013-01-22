package com.tulip.maphometest.API;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.tulip.maphometest.Utils.Constant;
import com.tulip.maphometest.Utils.Logger;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;


public class BasicLoader<T> extends AsyncTaskLoader<T> {
	private static final String TAG = BasicLoader.class.getSimpleName();
	private Bundle args;
	
	private T mData;
	private boolean firstLoad;
	
	public BasicLoader(Context context, T responseType, Bundle args)
	{
		super(context);
		this.args = args;
		this.mData = responseType;
		firstLoad = true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T loadInBackground() 
	{
		Logger.e(TAG, "loadInBackground()");
		if (args.containsKey(Constant.URL_KEY))
		{
			String url = args.getString(Constant.URL_KEY);
			
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			
			try
			{
				HttpResponse response = client.execute(get);
				HttpEntity entity = response.getEntity();
				String Json = EntityUtils.toString(entity);
				entity.consumeContent();
				mData = (T) GsonSerializer.seralizeData(Json, mData);
				
			}
			catch (ClientProtocolException e) 
			{
				e.printStackTrace();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			firstLoad = false;
			
			return mData;
		}
		else
		{
			Logger.e(TAG, "args do not contain a valid url!!!");
			cancelLoad();
		}
		
		return null;
	}
	
	@Override
	protected void onStartLoading() 
	{
		if (!firstLoad && !isReset()) deliverResult(mData);
		
		forceLoad();
	}
	
	@Override
	public void onCanceled(T data) {
		super.onCanceled(data);
	}
}
