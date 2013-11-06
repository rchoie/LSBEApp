package com.yahoo.lsbeapp.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.yahoo.lsbeapp.R;

public class Review extends BaseModel {
	private String alias;
	private String date;
	private String review;
	private String rating;
	
	public Review(JSONObject jsonObj) {
		super(jsonObj);
	}
	
	public String getAlias() {
		return ("".equals(alias)) ? "Kelly Hu" : alias;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getText() {
		return review;
	}
	
	public String getRating() {
		return rating;
	}

	public int getRatingIcon() {
		int intRating = 0;
		int ratingIcon;
		try {
			intRating = Integer.parseInt(rating);
			intRating = Math.round(intRating);
			if (intRating < 0 || intRating > 5) {
				intRating = 0;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		switch (intRating) {
		case 2:
			ratingIcon = R.drawable.star2;
			break;
		case 3:
			ratingIcon = R.drawable.star3;
			break;
		case 4:
			ratingIcon = R.drawable.star4;
			break;
		case 5:
			ratingIcon = R.drawable.star5;
			break;
		case 0:
		case 1:
		default:
			ratingIcon = R.drawable.star1;
			break;

		}
		return ratingIcon;
	}
	
	public static Review fromJSON(JSONObject jsonObj) {
		Review review = new Review(jsonObj);
		review.alias = review.getString("alias");
		review.date = review.getString("date");
		if (review.date != null) {
			review.date = review.date.substring(0, review.date.indexOf(" "));
		}
		String text = review.getString("text");
		if (text != null) {
			text = text.substring(1);
			review.review = text;
		}
		
		review.rating = review.getString("rating");
		return review;
	}
	
	public static ArrayList<Review> fromJSON(JSONArray array) {
		ArrayList<Review> reviews = new ArrayList<Review>(array.length());
		for (int i = 0; i < array.length(); i++) {
			Review review;
			try {
				JSONObject jsonObj  = array.getJSONObject(i);
				review = Review.fromJSON(jsonObj);
			} catch (JSONException e) {
				review = null;
			}
			if (review != null) {
				reviews.add(review);
			}
		}
		return reviews;
	}
	
	public String truncatedString(String str, int len) {
		if (str != null && str.length() > len) {
			return str.substring(0, len) + " ...";
		}
		return str;
	}

}
