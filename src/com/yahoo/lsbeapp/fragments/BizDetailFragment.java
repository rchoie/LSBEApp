package com.yahoo.lsbeapp.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yahoo.lsbeapp.R;
import com.yahoo.lsbeapp.ReviewAdapter;
import com.yahoo.lsbeapp.model.Listing;
import com.yahoo.lsbeapp.utils.LSBEAssets;

public class BizDetailFragment extends Fragment {

	//TODO: enable correct url..
	//private static final String LOCAL_DETAIL_API_URL = "http://dd.local.yahoo.com:4080/xmllocal?cust_nm=pinank&output=json";
	private static final String LOCAL_DETAIL_API_URL = "http://yahoo.com";
	private String gid;
	private TextView tvTitle;
	private TextView tvAddress;
	private TextView tvPhone;
	private Listing biz;
	private ListView lvReview;

	

	public static BizDetailFragment newInstance(String gid) {
		BizDetailFragment detailFragment = new BizDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putString("gid", gid);
		detailFragment.setArguments(bundle);
		return detailFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Get gid
		Bundle bundle = getArguments();
		gid = (String) bundle.get("gid");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_listing_details, parent, false);
		return view;
	}
	
	private void setUpViews() {
		tvTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
		tvAddress = (TextView) getActivity().findViewById(R.id.tvAddress);
		tvPhone = (TextView) getActivity().findViewById(R.id.tvPhone);
		lvReview = (ListView) getActivity().findViewById(R.id.lvReviews);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setUpViews();

		//Make call to get Biz-Details..
		RequestParams params = new RequestParams();
		params.put("id", gid);
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(LOCAL_DETAIL_API_URL, params, new JsonHttpResponseHandler() {

			public void onSuccess(JSONObject jsonObj) {
				try {
					biz = Listing.fromJSON(jsonObj.getJSONObject("ResultSet").getJSONArray("Result").getJSONObject(0));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				populateView();
			}
			

			//TODO:============================
			//TODO: REMOVE THE LINES BELOW THIS
			//TODO:============================
			public void onFailure(Throwable t, JSONObject jsonObj) {
				jsonObj = LSBEAssets.getFakeResults(getActivity(), "detail.txt");
				try {
					biz = Listing.fromJSON(jsonObj.getJSONObject("ResultSet").getJSONArray("Result").getJSONObject(0));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				populateView();
			}
			//TODO:============================
			//TODO: REMOVE THE LINES ABOVE THIS
			//TODO:============================

		});
	}

	
	public void populateView() {
		//if bizDetails are not available..
		if (biz == null) {
			Toast.makeText(getActivity(), "Error: Couldn't fetch Biz Details", Toast.LENGTH_SHORT).show();
			return;
		}
		tvTitle.setText(biz.getTitle());
		tvAddress.setText(biz.getAddress());
		tvPhone.setText(biz.getPhone());
		ReviewAdapter reviewAdapter = new ReviewAdapter(getActivity(), R.layout.review_item, biz.getReviews());
		lvReview.setAdapter(reviewAdapter);
		
	}

	
	

	
}
