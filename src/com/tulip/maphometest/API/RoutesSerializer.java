package com.tulip.maphometest.API;

import java.util.List;

public class RoutesSerializer extends ResponseObject {
	public String summary;
	public List<LegSerializer> legs;
	public BoundsSerializer bounds;
	public String copyrights;
	
	
	@Override
	public boolean isValid() 
	{
		return true;
	}	
}
