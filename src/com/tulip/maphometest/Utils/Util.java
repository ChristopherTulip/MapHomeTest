package com.tulip.maphometest.Utils;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;

import com.google.android.gms.maps.model.LatLng;
import com.tulip.maphometest.API.BasicLoader;
import com.tulip.maphometest.API.DirectionsSerializer;
import com.tulip.maphometest.API.ResponseObject;

public class Util {
	public static BasicLoader<? extends ResponseObject> createLoaderById(int id, Context applicationContext, Bundle args) 
	{
		switch (id) 
		{
		case Constant.GOOGLE_MAPS_LOADER_ID:
			return new BasicLoader<DirectionsSerializer>(applicationContext, new DirectionsSerializer(), args);
		default:
			return null;
		}
	}
	
	public static void connectToLoader(LoaderManager lm, int id, Bundle args, LoaderCallbacks<? extends ResponseObject> mLoaderCallbacks)
	{
		if (lm.getLoader(id) != null) lm.initLoader(id, args, mLoaderCallbacks);
	}
	
	public static void restartLoader(LoaderManager lm, int id, Bundle args, LoaderCallbacks<? extends ResponseObject> mLoaderCallbacks)
	{
		if (lm.getLoader(id) != null) lm.restartLoader(id, args, mLoaderCallbacks);
        
        else lm.restartLoader(id, args, mLoaderCallbacks);
	}
	
	public static String buildMapsAPIUrl (String startLocation, String endLocation)
	{
		String url = Constant.MAP_API_URL_BASE;
		
		url = url.concat("origin=");
		url = url.concat(Uri.encode(startLocation));
		url = url.concat("&destination=");
		url = url.concat(Uri.encode(endLocation));
		url = url.concat("&region=AU");
		url = url.concat("&sensor=true");
		
		return url;
	}
	
	public static List <LatLng> decodePoints(String encoded_points){
		int index = 0;
		int lat = 0;
		int lng = 0;
		List <LatLng> out = new ArrayList<LatLng>();

		try {
		    int shift;
		    int result;
		    while (index < encoded_points.length()) {
		        shift = 0;
		        result = 0;
		        while (true) {
		            int b = encoded_points.charAt(index++) - '?';
		            result |= ((b & 31) << shift);
		            shift += 5;
		            if (b < 32)
		                break;
		        }
		        lat += ((result & 1) != 0 ? ~(result >> 1) : result >> 1);

		        shift = 0;
		        result = 0;
		        while (true) {
		            int b = encoded_points.charAt(index++) - '?';
		            result |= ((b & 31) << shift);
		            shift += 5;
		            if (b < 32)
		                break;
		        }
		        lng += ((result & 1) != 0 ? ~(result >> 1) : result >> 1);
		        /* Add the new Lat/Lng to the Array. */
		        
		        out.add(new LatLng((double) lat / 1e5, (double) lng / 1e5));
		    }
		    return out;
		}catch(Exception e) {
		    e.printStackTrace();
		}
		return out;
	}
	
	
	
	public static double round(double unrounded, int precision)
	{
	    BigDecimal bd = new BigDecimal(unrounded);
	    BigDecimal rounded = bd.setScale(precision, BigDecimal.ROUND_HALF_UP);
	    return rounded.doubleValue();
	}
}
