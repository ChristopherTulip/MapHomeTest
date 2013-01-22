package com.tulip.maphometest.API;

import com.google.android.gms.maps.model.LatLng;

public class LocationSerializer extends ResponseObject {

	public double lat = -1;
	public double lng = -1;
	
	@Override
	public boolean isValid() {
		if (lat != -1 && lng != -1) return true;
		
		return false;
	}
	
	public LatLng getLatLng()
	{
		if (isValid()) return new LatLng(lat, lng);
		
		return null;
	}
	
}
