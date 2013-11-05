package com.yahoo.lsbeapp.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.yahoo.lsbeapp.ListingAdapter;
import com.yahoo.lsbeapp.R;
import com.yahoo.lsbeapp.db.ListingsDB;
import com.yahoo.lsbeapp.model.Listing;

public class BookmarksFragment extends Fragment {

	private ListingsDB listingsDB = null;
	ListingAdapter adapter;

    public static BookmarksFragment newInstance(String arg) {
    	
    	BookmarksFragment bookmarksFragment = new BookmarksFragment();
        Bundle args = new Bundle();
        args.putString("db", arg);
        bookmarksFragment.setArguments(args);
        return bookmarksFragment;
        
    }
     
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		String db = getArguments().getString("db");

		Log.d("DEBUG", "Bookmarks Fragment : db=" + db);
		
		listingsDB = new ListingsDB(getActivity());
		listingsDB.open();
		Log.d("DEBUG", "create db");
		
	}

	
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState) {
		return inf.inflate(R.layout.fragment_bookmarks, parent, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		List<Listing> listings = listingsDB.getAllListings();

		adapter = new ListingAdapter(getActivity(), listings);

		ListView lvBookmarks = (ListView) getActivity().findViewById(R.id.lvBookmarks);
		lvBookmarks.setAdapter(adapter);
				
		lvBookmarks.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View parent,
					int position, long rowId) {
				Listing listing = (Listing) adapterView.getItemAtPosition(position);
				listingsDB.deleteListingById(listing.getId());
				adapter.remove(listing);
				Toast.makeText(getActivity(), "Bookmark deleted", Toast.LENGTH_SHORT).show();

				return false;
			}
		});
		
		lvBookmarks.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View parent, int position,
					long rowId) {

				// go to detail page.
			}
		});

	}

}