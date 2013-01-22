package com.tulip.maphometest.API;

public class StepSerializer extends ResponseObject {

	public StepInformationSerializer distance;
	public StepInformationSerializer duration;
	public LocationSerializer end_location;
	public LocationSerializer start_location;
	public PolylineSerializer polyline;
	
	@Override
	public boolean isValid() 
	{
		return true;
	}

}
