package com.yahoo.lsbeapp.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Listing extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7027443567011627382L;
	
	private String title;
	private String phone;
	private String rating;
	private String address;
	private String street;
	private String city;
	private String state;
	private String imageUrl;
	private String id;
	private String lat;
	private String lon;
	private ArrayList<Review> reviews;

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public Listing(JSONObject jsonObj) {
		super(jsonObj);
	}

	public Listing() {
		super();
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

	public String getState() {
		return state;
	}

	public String getCity() {
		return city;
	}

	public String getStreet() {
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

	public String getLat() {
		return lat;
	}

	public String getLon() {
		return lon;
	}
	
	public static Listing fromJSON(JSONObject jsonObj) {
		Listing biz = new Listing(jsonObj);
		try {
			biz.id = jsonObj.getString("id");
			biz.title = jsonObj.getString("dtitle");
			biz.street = jsonObj.getString("addr");
			biz.city = jsonObj.getString("city");
			biz.state = jsonObj.getString("state");
			biz.rating	= jsonObj.getString("rating");
			biz.phone = jsonObj.getString("phone");
			biz.lat = jsonObj.getString("lat");
			biz.lon = jsonObj.getString("lon");

			//reviews..
			JSONObject reviewObj = biz.getJSONObject("reviews");
			if (reviewObj != null && reviewObj.getInt("count") > 0) {
				biz.reviews = Review.fromJSON(reviewObj.getJSONArray("review"));
			} else {
				biz.reviews = new ArrayList<Review>();
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
