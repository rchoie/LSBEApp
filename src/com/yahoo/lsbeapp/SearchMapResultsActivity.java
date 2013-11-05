package com.yahoo.lsbeapp;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yahoo.lsbeapp.model.Listing;

public class SearchMapResultsActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_results);
		
		Intent i = getIntent();
		String lat = i.getStringExtra("lat");
		String lon = i.getStringExtra("lon");
    	ArrayList<Listing> listings = (ArrayList<Listing>) i.getSerializableExtra("listings");
    	
		LatLng searchLocation = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
		
		GoogleMap map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		
		map.setMyLocationEnabled(true);
		
		if (map!=null) {
			
			for (int j=0; j<listings.size(); j++) {

				LatLng ll = new LatLng(Double.parseDouble(listings.get(j).getLat()),  Double.parseDouble(listings.get(j).getLon()));			  
				String title = listings.get(j).getTitle();
				String address = listings.get(j).getStreet() + ", " + listings.get(j).getCity() + ", " + listings.get(j).getState();
				Marker marker = map.addMarker(new MarkerOptions().position(ll).title(title).snippet(address));

			}
			
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(searchLocation, 15));
			map.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
      
		}		

	}
}
