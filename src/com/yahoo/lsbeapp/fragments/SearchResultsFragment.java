package com.yahoo.lsbeapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.lsbeapp.EndlessScrollListener;
import com.yahoo.lsbeapp.ListingAdapter;
import com.yahoo.lsbeapp.R;
import com.yahoo.lsbeapp.SearchMapResultsActivity;
import com.yahoo.lsbeapp.db.ListingsDB;
import com.yahoo.lsbeapp.model.Listing;
import com.yahoo.lsbeapp.utils.LSBEAssets;

public class SearchResultsFragment extends Fragment {

	//TODO: Enable correct XMLLOCAL_URL
	private static final String XMLLOCAL_URL = "http://api1.stage.ls.sk1.yahoo.com/xmllocal?output=json&stx=";
	
	ListingAdapter adapter;
	private TextView tvQuery;
	private ListView lvBiz;
	private ItemSelectedListener listener;
	private Button btnMap;

	private String query;
	private String location;
	private String lat;
	private String lon;
	private String searchMethod = null;

	private ArrayList<Listing> listings;

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
		
		query = getArguments().getString("query");
		location = getArguments().getString("location");
		lat = getArguments().getString("lat");
		lon = getArguments().getString("lon");
		
		if (query != null && location != null) {
			lsbeSearch(query, location);
			findLocationFromCity();
			searchMethod = "csz";
		}
		else if (query != null && lat != null && lon != null) {
			lsbeSearch(query, null);
			findLocationFromLatLong();
			searchMethod = "ll";
		}

	}
	
	protected void customLoadMoreDataFromApi(int page) {
		// TODO Auto-generated method stub
	}


	private void findLocationFromLatLong() {
	
		String findLocationQuery = "http://gws2.maps.yahoo.com/findlocation?q=" + lat + "+" + lon + "&gflags=R&flags=J";

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(findLocationQuery, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject listingsJson) {
				try {
					String city = listingsJson.getJSONObject("ResultSet").getJSONObject("Result").getString("city");
					String state = listingsJson.getJSONObject("ResultSet").getJSONObject("Result").getString("statecode");
					location = city + ", " + state;
					tvQuery.setText(query + ", " + location);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void findLocationFromCity() {
		//TODO: get location from findLocation..
	
		String findLocationQuery = "http://gws2.maps.yahoo.com/findlocation?q=" + Uri.encode(location) + "&flags=J";

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(findLocationQuery, new JsonHttpResponseHandler() {
			//private ArrayList<Listing> listings;

			@Override
			public void onSuccess(JSONObject listingsJson) {
				try {
					lat = listingsJson.getJSONObject("ResultSet").getJSONObject("Result").getString("latitude");
					lon = listingsJson.getJSONObject("ResultSet").getJSONObject("Result").getString("longitude");
					tvQuery.setText(query + ", " + location);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
	
	public void lsbeSearch(String query, String csz) {

		String xmllocalQuery = null;

		if (csz != null) {
			xmllocalQuery = XMLLOCAL_URL + Uri.encode(query) + "&csz="
					+ Uri.encode(csz);
		} else if (lat != null && lon != null) {
			xmllocalQuery = XMLLOCAL_URL + Uri.encode(query) + "&loc=point:"
					+ lat + "," + lon;
		}

		Log.d("DEBUG", xmllocalQuery);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(xmllocalQuery, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject listingsJson) {
				try {
					listings = Listing.fromJSON(listingsJson.getJSONObject("ResultSet").getJSONArray("Result"));
					getAdapter().addAll(listings);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// TODO:============================
			// TODO: REMOVE THE LINES BELOW THIS:
			// TODO:============================
			public void onFailure(Throwable t, JSONObject jsonObj) {
				try {
					// Fake Results..
					jsonObj = LSBEAssets.getFakeResults(getActivity(), "search_results.txt");
					JSONArray jsonArray = jsonObj.getJSONObject("ResultSet").getJSONArray("Result");
					listings = Listing.fromJSON(jsonArray);
					getAdapter().addAll(listings);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			// TODO:============================
			// TODO: REMOVE THE LINES ABOVE THIS:
			// TODO:============================
		});

	}
    
	public void loadMoreSearchResults(int page, int totalItemsCount) {
		
		String xmllocalQuery = null;

		int begin = (page - 1) * 10 + 1;
		
		if (searchMethod == "csz") {
			xmllocalQuery = XMLLOCAL_URL + Uri.encode(query) + "&csz="
					+ Uri.encode(location);
		} else {
			xmllocalQuery = XMLLOCAL_URL + Uri.encode(query) + "&loc=point:"
					+ lat + "," + lon;
		}

		xmllocalQuery += "&begin=" + begin;
		
		Log.d("DEBUG", "load more query : " + xmllocalQuery);
		Log.d("DEBUG", "totalItemCount : " + totalItemsCount);
		if (totalItemsCount > 400) {
			return;
		}
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(xmllocalQuery, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject listingsJson) {
				try {
					listings = Listing.fromJSON(listingsJson.getJSONObject("ResultSet").getJSONArray("Result"));
					getAdapter().addAll(listings);
				} catch (Exception e) {
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
		lvBiz.setAdapter(adapter);
		//tvQuery.setText(query + ", " + location);

	}
	
	public ListingAdapter getAdapter() {
		return adapter;
	}
	
	private void setUpViews() {
		btnMap = (Button) getActivity().findViewById(R.id.btnMap1);
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
		
		
		/**
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
		**/
		
		//MapView..
		btnMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				int n = getAdapter().getCount();
				ArrayList<Listing> allListings = new ArrayList<Listing>(n);
				for (int j=0; j<n; j++) {
					allListings.add(getAdapter().getItem(j));
				}
				
		    	Intent i = new Intent(getActivity(), SearchMapResultsActivity.class);
		    	Log.d("DEBUG", "maps view listing size : " + listings.size());
		    	i.putExtra("listings", allListings);
		    	i.putExtra("lat", lat);
		    	i.putExtra("lon", lon);
		    	startActivity(i);
				
				Toast.makeText(getActivity(), "Showing Map View", Toast.LENGTH_SHORT).show();
			}
		});
		
		lvBiz.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// TODO Auto-generated method stub
				loadMoreSearchResults(page, totalItemsCount);
			}
		});
				
	}
	 
}
