package com.yahoo.lsbeapp.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Listing extends BaseModel {

	
	private String title;
	private String phone;
	private String rating;
	private String address;
	private String street;
	private String city;
	private String state;
	private String imageUrl;
	private String id;
	private ArrayList<Review> reviews;

	public Listing(JSONObject jsonObj) {
		super(jsonObj);
	}

	public String getId() {
		return id;
	}

	public String getImage() {
		return imageUrl;
	}

	public String getTitle() {
		return title;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public String getRating() {
		return rating;
	}

	private String getState() {
		return state;
	}

	private String getCity() {
		return city;
	}

	private String getStreet() {
		return street;
	}
	

	public ArrayList<Review> getReviews() {
		return reviews;
	}
	
	public String getAddress() {
		if (address == null) {
			StringBuffer sb = new StringBuffer();
			sb.append(getStreet())
				.append(", ")
				.append(getCity())
				.append(", ")
				.append(getState());
			address = sb.toString();
		}
		return address;
	}

	public static Listing fromJSON(JSONObject jsonObj) {
		Listing biz = new Listing(jsonObj);
		try {
			biz.id = biz.getString("id");
			biz.title = biz.getString("dtitle");
			biz.street = biz.getString("addr");
			biz.city = biz.getString("city");
			biz.state = biz.getString("state");
			biz.rating	= biz.getString("rating");
			biz.phone = biz.getString("phone");
			
			//reviews..
			JSONObject reviewObj = biz.getJSONObject("reviews");
			if (reviewObj != null && reviewObj.getInt("count") > 0) {
				biz.reviews = Review.fromJSON(reviewObj.getJSONArray("review"));
			}
					
			//Image
			JSONObject imgObj = biz.getJSONObject("fullsize_photos");
			if (imgObj.getInt("count") > 0) {
				biz.imageUrl = imgObj.getJSONArray("content").getJSONObject(0).getString("url");
			} else {
				biz.imageUrl = null;
			}
		} catch (JSONException e) {
			Log.d("pinank", e.getMessage());
			return null;
		}

		return biz;
	}

	public static ArrayList<Listing> fromJSON(JSONArray jsonArray) {
		ArrayList<Listing> bizs = new ArrayList<Listing>(jsonArray.length());
		Listing biz;
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject bizJson = null;
			try {
				bizJson = jsonArray.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}

			biz = Listing.fromJSON(bizJson);
			if (biz != null) {
				bizs.add(biz);
			}
		}
			
		return bizs;
	}


	
}
