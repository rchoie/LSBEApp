package com.yahoo.lsbeapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.yahoo.lsbeapp.R;

public class SearchInputFragment extends Fragment {
	
	EditText etQuery;
	EditText etLocation;
	Button btnSearch;
	String lat;
	String lon;

	SearchClickListener listener;

    public static SearchInputFragment newInstance(String lat, String lon) {
    	
    	SearchInputFragment searchInputFragment = new SearchInputFragment();
        Bundle args = new Bundle();
        args.putString("lat", lat);
        args.putString("lon", lon);
        searchInputFragment.setArguments(args);
        Log.d("DEBUG", "search input lat : " + lat + ", lon : " + lon);
        return searchInputFragment;
        
    }
    
	public interface SearchClickListener {
		public void onSearchButtonClick(String query, String location, String lat, String lon);
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		lat = getArguments().getString("lat");
		lon = getArguments().getString("lon");

	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search_input, parent, false);
		
		//get handles to various elements..
		setupViews(view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//onClick
		btnSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String query = etQuery.getText().toString();
				String location = etLocation.getText().toString();
				
				listener.onSearchButtonClick(query, location, lat, lon);
				
				/*
				ListingsDB listingsDB = new ListingsDB(getActivity());
				listingsDB.open();
				ArrayList<Listing> listings = (ArrayList<Listing>) listingsDB.getAllListings();

		    	Intent i = new Intent(getActivity(), SearchMapResultsActivity.class);
		    	i.putExtra("listings", listings);
		    	i.putExtra("lat", "37.77");
		    	i.putExtra("lon", "-122.42");
		    	startActivity(i);
				*/
				
				
			}
		});		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof SearchClickListener) {
			listener = (SearchClickListener) activity;
		} else {
			throw new ClassCastException(activity.toString() + " must implement SearchClickListener");
		}
	}

	public void setupViews(View view) {
		etQuery = (EditText) view.findViewById(R.id.etQuery);
		etLocation = (EditText) view.findViewById(R.id.etLocation);
		btnSearch = (Button) view.findViewById(R.id.btnSearch);
	}
	
}