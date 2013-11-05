package com.yahoo.lsbeapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.lsbeapp.CategoryAdapter;
import com.yahoo.lsbeapp.R;
import com.yahoo.lsbeapp.model.Category;
import com.yahoo.lsbeapp.utils.LSBEAssets;

public class BrowseFragment extends Fragment {

	private OnClickListener listener;
	CategoryAdapter adapter;
	
    public static BrowseFragment newInstance(String lat, String lon) {
    	
    	BrowseFragment browseFragment = new BrowseFragment();
        Bundle args = new Bundle();
        args.putString("lat", lat);
        args.putString("lon", lon);
        browseFragment.setArguments(args);
        Log.d("DEBUG", "browse lat : " + lat + ", lon : " + lon);
        return browseFragment;
        
    }
    
	public interface OnClickListener {
		public void onClickEvent(String categoryName);
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnClickListener) {
			listener = (OnClickListener) activity;
		}
		else {
			throw new ClassCastException("Must implement OnClickListener interface");
		}
	}
	
	public void onDetach() {
		super.onDetach();
		listener = null;
	}
    
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		String categoryQuery = "http://api1.stage.ls.sk1.yahoo.com/xmlcategories";
    	AsyncHttpClient client = new AsyncHttpClient();
		client.get(categoryQuery,
				   new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray categoriesJson) {
				try {
					ArrayList<Category> categories = Category.fromJson(categoriesJson);
					getAdapter().addAll(categories);				
				}
				catch (Exception e) {
	                e.printStackTrace();
	            }
			}
		});
		
	}

	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState) {
		return inf.inflate(R.layout.fragment_browse, parent, false);
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ArrayList<Category> categories = new ArrayList<Category>(9);
		adapter = new CategoryAdapter(getActivity(), categories);

		ListView lvCategories = (ListView) getActivity().findViewById(R.id.lvCategories);
		lvCategories.setAdapter(adapter);
		lvCategories.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long rowId) {
				Category category = (Category) adapter.getItemAtPosition(position);
				listener.onClickEvent(category.getCateoryName());
			}
		});

	}
	
	public CategoryAdapter getAdapter() {
		return adapter;
	}
}