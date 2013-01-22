package com.tulip.maphometest.API;

import android.content.Context;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.tulip.maphometest.Utils.Logger;
import com.tulip.maphometest.Utils.Util;

public abstract class BaseLoaderCallbacks<T extends ResponseObject> implements LoaderCallbacks<T>
{

	protected static final String TAG = "LoaderCallbacks";
	public static final String LOADER_ID_TAG = "loader_id";
	private Context mContext;
	
	public BaseLoaderCallbacks(Context context) 
	{
		this.mContext = context;
	}
	
	public void onDestroy() 
	{
		this.mContext = null;
	}
	
	@SuppressWarnings("unchecked")
	public Loader<T> onCreateLoader(int id, Bundle args) 
	{
		Logger.e(TAG, "onCreateLoader()");
		
		return (Loader<T>) Util.createLoaderById(id, mContext, args);	
	}
	
	public void onLoadFinished(Loader<T> loader, T response) 
	{
		if (response.isValid())
		{
			LoaderFinishedSuccessfully(response);
		}
		else
		{
			
			LoaderFailed(response);
		}
	}
	
	protected abstract void LoaderFinishedSuccessfully(T response);
	protected abstract void LoaderFailed(T response);

	public void onLoaderReset(Loader<T> arg0) 
	{
		Logger.e(TAG, "Loader Reset!");
	}

}
