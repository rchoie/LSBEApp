package com.yahoo.lsbeapp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;

import com.yahoo.lsbeapp.fragments.BookmarksFragment;
import com.yahoo.lsbeapp.fragments.BrowseFragment;
import com.yahoo.lsbeapp.fragments.BrowseFragment.OnClickListener;
import com.yahoo.lsbeapp.fragments.SearchInputFragment;
import com.yahoo.lsbeapp.fragments.SearchInputFragment.SearchClickListener;
import com.yahoo.lsbeapp.fragments.SearchResultsFragment;
import com.yahoo.lsbeapp.fragments.SearchResultsFragment.ItemSelectedListener;

public class LSBEActivity extends FragmentActivity implements TabListener,OnClickListener,SearchClickListener,ItemSelectedListener,LocationListener {

	Tab tabBrowse;
	Tab tabSearch;
	Tab tabBookmarks;
	
	String lat = null;
	String lon = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lsbe);
		setupNavigationTabs();
		setupLocationManager();

	}
	
	private void setupNavigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		tabBrowse = actionBar.newTab().setTag("BrowseFragment").setIcon(R.drawable.ic_location_found).setTabListener(this);
		tabSearch = actionBar.newTab().setTag("SearchFragment").setIcon(R.drawable.ic_search).setTabListener(this);
		tabBookmarks = actionBar.newTab().setTag("BookmarksFragment").setIcon(R.drawable.ic_bookmark).setTabListener(this);
		actionBar.addTab(tabBrowse);
		actionBar.addTab(tabSearch);
		actionBar.addTab(tabBookmarks);
		actionBar.selectTab(tabBrowse);
	}
	
	private void setupLocationManager() {
		
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	    Log.d("DEBUG", "enabled " + enabled);
	    
	    if (!enabled) { 
	    	  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    	  startActivity(intent);
	    } 
	    
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
	    
	    Criteria criteria = new Criteria();
	    String provider = locationManager.getBestProvider(criteria, false);
	    Location location = locationManager.getLastKnownLocation(provider);
	    onLocationChanged(location);

	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lsbe, menu);
		return true;
	}


	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		
		onTabSelected(arg0, arg1);
	}


	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {

		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		
		if (tab.getTag() == "BrowseFragment") {
	 		BrowseFragment browseFragment = BrowseFragment.newInstance(lat, lon);
			fts.replace(R.id.frame_container, browseFragment);
		}
		else if (tab.getTag() == "SearchFragment") {
	 		SearchInputFragment searchInputFragment = new SearchInputFragment();
			fts.replace(R.id.frame_container, searchInputFragment);
		}
		else {
	 		BookmarksFragment bookmarksFragment = BookmarksFragment.newInstance("bookmarks argument");
			fts.replace(R.id.frame_container, bookmarksFragment);
		}
		fts.commit();		
	}


	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
	}

	public void onClickEvent(String categoryName) {
		
		ActionBar actionBar = getActionBar();
		actionBar.selectTab(tabSearch);

        //Log.d("DEBUG", "browse on click lat : " + lat + ", lon : " + lon);

		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
 		SearchResultsFragment searchFragment = SearchResultsFragment.newInstance(categoryName, null, lat, lon);
		fts.replace(R.id.frame_container, searchFragment);
		fts.commit();		

	}

	/**
	 * Implement listener for Button (search) Event
	 */
	@Override
	public void onSearchButtonClick(String query, String location) {
		android.support.v4.app.FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
		fts.replace(R.id.frame_container, SearchResultsFragment.newInstance(query, location, null, null));
		fts.commit();
	}
	
	/**
	 * Implement listener for ListView (biz) Events
	 */
	
	
	@Override
	public void onItemSelected(String gid) {
		android.support.v4.app.FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
		//fts.replace(R.id.frame_container, BizDetailFragment.newInstance(gid));
		fts.commit();
	}


	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if (location != null) {
			Log.d("DEBUG", "lat : " + location.getLatitude() + ", lon : " + location.getLongitude());
			lat = String.valueOf(location.getLatitude());
			lon = String.valueOf(location.getLongitude());
		}
	}


	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		Log.d("DEBUG", "onProviderDisabled : " + arg0);
	}


	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		Log.d("DEBUG", "onProviderEnabled : " + arg0);

	}


	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		Log.d("DEBUG", "onStatusChanged : " + arg0);

	}

}
