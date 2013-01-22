package com.tulip.maphometest.API;

public class BoundsSerializer extends ResponseObject {

	public LocationSerializer northeast;
	public LocationSerializer southwest;
	
	@Override
	public boolean isValid() {
		
		if (northeast.getLatLng() == null) return false;
		if (southwest.getLatLng() == null) return false;
		
		return true;
	}

}
