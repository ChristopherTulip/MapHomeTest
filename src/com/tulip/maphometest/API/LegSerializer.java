package com.tulip.maphometest.API;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class LegSerializer extends ResponseObject {

	public List<StepSerializer> steps;
	public List<LatLng> polylineArray;
	
	public StepInformationSerializer distance;
	public StepInformationSerializer duration;
	
	public LocationSerializer end_location;
	public LocationSerializer start_location;
	
	public String end_address;
	public String start_address;
	
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

}
