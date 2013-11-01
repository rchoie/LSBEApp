package com.yahoo.lsbeapp.fragments;

import com.yahoo.lsbeapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BookmarksFragment extends Fragment {

    public static BookmarksFragment newInstance(String arg) {
    	
    	BookmarksFragment bookmarksFragment = new BookmarksFragment();
        Bundle args = new Bundle();
        args.putString("arg", arg);
        bookmarksFragment.setArguments(args);
        return bookmarksFragment;
        
    }
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		String arg = getArguments().getString("arg");

		Log.d("DEBUG", "Bookmarks Fragment : arg=" + arg);
	}
	
	//import com.codepath.apps.twitterapp.R;

	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState) {
		return inf.inflate(R.layout.fragment_bookmarks, parent, false);
	}
}