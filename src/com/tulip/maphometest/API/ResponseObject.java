package com.tulip.maphometest.API;
/**
 * This class is to base to all other serializer classes and the place 
 * to put all the generic error fields from your API. The error fields 
 * belong here so that all your serializers can handle them properly 
 * and will have something to base the isValid() function on
 * 
 * @author Chris Tulip
 *
 */
public abstract class ResponseObject 
{
	
	public abstract boolean isValid();
}
