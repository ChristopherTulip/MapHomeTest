package com.tulip.maphometest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.google.android.gms.maps.model.LatLng;
import com.tulip.maphometest.API.BaseLoaderCallbacks;
import com.tulip.maphometest.API.DirectionsSerializer;
import com.tulip.maphometest.API.LegSerializer;
import com.tulip.maphometest.API.RoutesSerializer;
import com.tulip.maphometest.API.StepSerializer;
import com.tulip.maphometest.Utils.AndroidUtil;
import com.tulip.maphometest.Utils.Constant;
import com.tulip.maphometest.Utils.Logger;
import com.tulip.maphometest.Utils.Util;

public class MainActivity extends SherlockFragmentActivity {
    
	protected static final String TAG = MainActivity.class.getSimpleName();

	private SearchView searchView;
	
	private BaseLoaderCallbacks<DirectionsSerializer> mLoaderCallbacks;
	private Button calculate;
	private TextView cost;
	private TextView distance;
	private TextView time;
	private ProgressDialog myDialog;
	
	private String mRegion= "AU";

	protected String mState;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        calculate = (Button) findViewById(R.id.calculate_button);
        cost = (TextView) findViewById(R.id.cost_textview);
        distance = (TextView) findViewById(R.id.distance_textview);
        time = (TextView) findViewById(R.id.time_textview);
        
        calculate.setOnClickListener(new CalculateButtonListener());
        
        mLoaderCallbacks = new GoogleMapsLoaderCallbacks(this); 
        
        // Initialize the location fields
	    if (getSupportLoaderManager().getLoader(Constant.GOOGLE_MAPS_LOADER_ID)!=null) 
        {
        	Bundle args = new Bundle();
        	args.putString(Constant.URL_KEY, "");
        	Util.connectToLoader(getSupportLoaderManager(), Constant.GOOGLE_MAPS_LOADER_ID, args, mLoaderCallbacks);
        }
        
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.map, new TaxiMapFragment(), Constant.TAXI_FRAGMENT_ID);
        ft.commit();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Create the search view
        searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setQueryHint("Where to?");

        searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) 
			{
				LatLng startLocation = getTaxiMapFragment().getStartLocation();
				
				if (mState == null) 
				{
					try {
						Geocoder geo = new Geocoder(MainActivity.this, Locale.getDefault());
						Address addy = geo.getFromLocation(startLocation.latitude,
								startLocation.longitude, 1).get(0);
						mState = addy.getAdminArea();
						mRegion = addy.getCountryCode();
						
						query = query.concat(", "  + mState);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				Logger.e(TAG, query);
				
				showProgressDialog();
				
				String url = Util.buildMapsAPIUrl(
						startLocation.latitude+","+startLocation.longitude,
						query, mRegion);				
				
				Bundle args = new Bundle();
		        args.putString(Constant.URL_KEY, url);
		        
		        Util.restartLoader(
		        		getSupportLoaderManager(),
		        		Constant.GOOGLE_MAPS_LOADER_ID, 
		        		args, 
		        		mLoaderCallbacks
		        		);
		        
				return true;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
        
        menu.add("Search")
            .setIcon(R.drawable.abs__ic_search)
            .setActionView(searchView)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        
        return true;
    }

	@Override
	public void onDestroy() {
		mLoaderCallbacks.onDestroy();
		super.onDestroy();
	}
	
	private class GoogleMapsLoaderCallbacks extends BaseLoaderCallbacks<DirectionsSerializer>
    {
    	public GoogleMapsLoaderCallbacks(Context context) {
			super(context);
		}

		@Override
		protected void LoaderFinishedSuccessfully(DirectionsSerializer response) 
		{
			if (myDialog != null) myDialog.dismiss();
			
			final RoutesSerializer route = response.routes.get(0);	
			final LegSerializer leg = route.legs.get(0);
			//process the data
			double tripCost = Constant.BASE_FARE +
					Constant.COST_PER_M * leg.distance.value + 
					Constant.COST_PER_SECOND * leg.duration.value;
			
			tripCost = Util.round(tripCost, 2);
			
			List<LatLng> routePolyline = new ArrayList<LatLng>();
			for (StepSerializer s : leg.steps)
			{
				routePolyline.addAll(Util.decodePoints(s.polyline.points));
			}
			
			LatLng northeast = new LatLng(route.bounds.northeast.lat, route.bounds.northeast.lng);
			LatLng southwest = new LatLng(route.bounds.southwest.lat, route.bounds.southwest.lng);
			
			LatLng endlocation = new LatLng(leg.end_location.lat, leg.end_location.lng);
			
			//update the UI
			cost.setText("$".concat(String.valueOf(tripCost)));
			distance.setText(leg.distance.text);
			time.setText(leg.duration.text);
			
			getTaxiMapFragment().drawPolyline(routePolyline);
			getTaxiMapFragment().setEndLocation(endlocation);
			getTaxiMapFragment().setMapBounds(northeast, southwest);
			
			
		}

		@Override
		protected void LoaderFailed(DirectionsSerializer response) {
			if (myDialog != null) myDialog.dismiss();
			
			Toast.makeText(MainActivity.this, "Error loading data: " + response.status, Toast.LENGTH_SHORT).show();
			
			AndroidUtil.hideSoftInput(MainActivity.this, searchView);
	        cost.requestFocus();
		}
    }
    
    private class CalculateButtonListener implements OnClickListener
    {
		@Override
		public void onClick(View v) 
		{
			LatLng endLocation = getTaxiMapFragment().getEndLocation();
			LatLng startLocation = getTaxiMapFragment().getStartLocation();
			
			if (endLocation != null && startLocation != null)
			{	
				String url = Util.buildMapsAPIUrl(
						startLocation.latitude+","+startLocation.longitude,
						endLocation.latitude+","+endLocation.longitude,
						mRegion);
				
				Bundle args = new Bundle();
		        args.putString(Constant.URL_KEY, url);
		        
		        Util.restartLoader(
		        		getSupportLoaderManager(),
		        		Constant.GOOGLE_MAPS_LOADER_ID, 
		        		args, 
		        		mLoaderCallbacks
		        		);

		        showProgressDialog();
			}
			else
			{
				Toast.makeText(MainActivity.this, "You must have selected a destination to calculate the distance", Toast.LENGTH_SHORT).show();
			}
		}
    }
    
    private void showProgressDialog ()
    {
    	if (myDialog == null) 
        {
        	myDialog = new ProgressDialog(MainActivity.this);
        	myDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        myDialog.setMessage("Please wait as we retieve the details of your route");
        }
        
        myDialog.show();
    }
    
    private TaxiMapFragment getTaxiMapFragment() {
		return (TaxiMapFragment) getSupportFragmentManager().findFragmentByTag(Constant.TAXI_FRAGMENT_ID);
	}
}
