package com.tulip.maphometest.API;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.tulip.maphometest.Utils.Logger;

public class GsonSerializer 
{
	private static final String TAG = "GsonSerializer";

	public static <T> Object seralizeData (String response, T t)
	{
		Gson serializer = new Gson();
		
		try 
		{
			return serializer.fromJson(response, t.getClass());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Logger.e(TAG, "deserialization failure: ".concat(response));
		}
		
		return null;
	}
	
	public static <T> Object seralizeData (InputStream response, T t)
	{
		Gson serializer = new Gson();
		InputStreamReader reader = new InputStreamReader(response);
		try 
		{
			return serializer.fromJson(reader, t.getClass());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Logger.e(TAG, "deserialization failure ");
		}
		
		return null;
	}
	
}
