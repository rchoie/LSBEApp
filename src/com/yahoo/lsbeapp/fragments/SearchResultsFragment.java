package com.yahoo.lsbeapp.fragments;

import java.util.ArrayList;

import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.lsbeapp.ListingAdapter;
import com.yahoo.lsbeapp.R;
import com.yahoo.lsbeapp.db.ListingsDB;
import com.yahoo.lsbeapp.model.Listing;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResultsFragment extends Fragment {

	ListingAdapter adapter;
	private TextView tvQuery;
	private ListView lvBiz;
	private ItemSelectedListener listener;

	public interface ItemSelectedListener {
		public void onItemSelected(String gid);
	}
	
    public static SearchResultsFragment newInstance(String query, String location, String lat, String lon) {
    	
    	SearchResultsFragment searchFragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        args.putString("location", location);
        args.putString("lat", lat);
        args.putString("lon", lon);
        searchFragment.setArguments(args);
        return searchFragment;
        
    }
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		String query = getArguments().getString("query");
		String location = getArguments().getString("location");
		String lat = getArguments().getString("lat");
		String lon = getArguments().getString("lon");

		if (query != null && location != null) {
			lsbeSearch(query, location, null, null);
		}
		else if (query != null && lat != null && lon != null) {
			lsbeSearch(query, null, lat, lon);
		}

	}
	
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState) {
		return inf.inflate(R.layout.fragment_search_results, parent, false);
	}
	 
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof ItemSelectedListener) {
			listener = (ItemSelectedListener) activity;
		} else {
			throw new ClassCastException(activity.toString() + " must implement ItemSelectedListener");
		}
	}
	
    public void lsbeSearch(String query, String csz, String lat, String lon) {

    	String xmllocalQuery = null;
    	
    	if (csz != null) {
    	    //xmllocalQuery = "http://dd.local.yahoo.com:4080/xmllocal?output=json&stx=" + Uri.encode(query) + "&csz=" + Uri.encode(csz);
    	    xmllocalQuery = "http://api1.stage.ls.sk1.yahoo.com/xmllocal?output=json&stx=" + Uri.encode(query) + "&csz=" + Uri.encode(csz);

    	}
    	else if (lat != null && lon != null) {
    		//xmllocalQuery = "http://dd.local.yahoo.com:4080/xmllocal?output=json&stx=" + Uri.encode(query) + "&loc=point:" + lat + "," + lon;
    		xmllocalQuery = "http://api1.stage.ls.sk1.yahoo.com/xmllocal?output=json&stx=" + Uri.encode(query) + "&loc=point:" + lat + "," + lon;
    	}
    	
		Log.d("DEBUG", xmllocalQuery);

    	AsyncHttpClient client = new AsyncHttpClient();
		client.get(xmllocalQuery,
				   new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject listingsJson) {
				try {
					ArrayList<Listing> listings = Listing.fromJSON(listingsJson.getJSONObject("ResultSet").getJSONArray("Result"));
					getAdapter().addAll(listings);				
				}
				catch (Exception e) {
	                e.printStackTrace();
	            }
			}
		});
	 
    }
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		setUpViews();
		setUpListeners();
		
		ArrayList<Listing> listings = new ArrayList<Listing>();
		adapter = new ListingAdapter(getActivity(), listings);

		TextView tvQuery = (TextView) getActivity().findViewById(R.id.tvQuery);
		tvQuery.setText("TESTING");
		ListView lvBiz = (ListView) getActivity().findViewById(R.id.lvBiz);
		lvBiz.setAdapter(adapter);

	}
	
	public ListingAdapter getAdapter() {
		return adapter;
	}
	
	private void setUpViews() {
		tvQuery = (TextView) getActivity().findViewById(R.id.tvQuery);
		lvBiz = (ListView) getActivity().findViewById(R.id.lvBiz);
	}
	
	private void setUpListeners() {
		lvBiz.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Listing biz = (Listing) parent.getItemAtPosition(position);
				listener.onItemSelected(biz.getId());
			}
			
		});
		
		lvBiz.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Listing biz = (Listing) parent.getItemAtPosition(position);

				Log.d("DEBUG", "Save listing to bookmark");
				ListingsDB listingsDB = new ListingsDB(getActivity());
				listingsDB.open();
				listingsDB.addLisitng(biz);
				listingsDB.close();
				Log.d("DEBUG", "after Save listing to bookmark");
				
				Toast.makeText(getActivity(), "Saved to bookmark", Toast.LENGTH_SHORT).show();
				
				return false;
			}
		});
		
		// TODO Auto-generated method stub
		
	}
	 
}