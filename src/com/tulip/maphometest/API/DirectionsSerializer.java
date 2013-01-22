package com.tulip.maphometest.API;

import java.util.ArrayList;

public class DirectionsSerializer extends ResponseObject {

	public static final String OK = "OK";
	public static final String DENIED = "REQUEST_DENIED";
	
	public String status;
	public ArrayList<RoutesSerializer> routes;
	
	@Override
	public boolean isValid() 
	{
		if (status == null) return false;
		if (!status.equals(OK)) return false;
		
		return true;
	}

	
}
