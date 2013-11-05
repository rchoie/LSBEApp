package com.yahoo.lsbeapp;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yahoo.lsbeapp.model.Review;

public class ReviewAdapter extends ArrayAdapter<Review> {

	public ReviewAdapter(Context context, int resource, List<Review> reviews) {
		super(context, resource, reviews);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Review review = getItem(position);
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.review_item, null);
		}
		
		((TextView) view.findViewById(R.id.tvAlias)).setText(review.getAlias());
		((TextView) view.findViewById(R.id.tvDate)).setText(review.getDate());
		((TextView) view.findViewById(R.id.tvText)).setText(review.truncatedString(review.getText(), 100));
		
		return view;
	}
}
