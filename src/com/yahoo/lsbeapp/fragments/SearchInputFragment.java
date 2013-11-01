package com.yahoo.lsbeapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

	SearchClickListener listener;

	public interface SearchClickListener {
		public void onSearchButtonClick(String query, String location);
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
				listener.onSearchButtonClick(query, location);
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