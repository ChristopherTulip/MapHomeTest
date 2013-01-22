package com.tulip.maphometest;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class TaxiMapFragment extends SupportMapFragment implements LocationListener
{
	private static final String TAG = TaxiMapFragment.class.getSimpleName();
	
	private LocationManager locationManager;
	
	private Marker me_marker;
	private Marker end_marker;
	
	private LatLng startLocation;
	private LatLng endLocation;
	
	private Polyline mCurrentRoute;
	
	public LatLng getStartLocation() {
		return startLocation;
	}
	public LatLng getEndLocation() {
		return endLocation;
	}
	
	public void setEndLocation(LatLng endLocation) {
		this.endLocation = endLocation;
		
		if (end_marker == null) end_marker = getMap().addMarker(new MarkerOptions().position(endLocation));					
		else end_marker.setPosition(endLocation);
	}
	
	public TaxiMapFragment() {}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 60, this);
		
	    getMap().setOnMapLongClickListener(new OnMapLongClickListener() 
	    {
			@Override
			public void onMapLongClick(LatLng touch) 
			{
				endLocation = touch;
				if (end_marker == null) end_marker = getMap().addMarker(new MarkerOptions().position(touch));					
				else end_marker.setPosition(touch);
			}
		});
	    
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onPause()
	{
	    super.onPause();
	    locationManager.removeUpdates(this);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		double lat = location.getLatitude();
		double lng = location.getLongitude();
		
		startLocation = new LatLng(lat, lng);
		
		getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
		
		if (me_marker == null)	me_marker = getMap().addMarker(new MarkerOptions().position(startLocation));
		else me_marker.setPosition(startLocation);
		
		locationManager.removeUpdates(this);
	}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}
	
	public void drawPolyline(final List<LatLng> routePolyline)
	{	
		PolylineOptions options = new PolylineOptions();
		options.addAll(routePolyline);
		options.color(Color.BLUE);
		if (mCurrentRoute == null) mCurrentRoute = getMap().addPolyline(options);
		else mCurrentRoute.setPoints(routePolyline);
	}
	
	public void setMapBounds (final LatLng northEast, final LatLng southWest)
	{
		final ViewTreeObserver vto = getView().getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		    @Override
		    public void onGlobalLayout() {
		    	
				LatLngBounds bounds = new LatLngBounds(southWest, northEast);
				
				getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
				
				getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
		    }
		});
	}
	
	
}
