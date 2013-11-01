package com.yahoo.lsbeapp;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.yahoo.lsbeapp.model.Listing;

public class ListingAdapter extends ArrayAdapter<Listing> {

	public ListingAdapter(Context context, List<Listing> listings) {
		super(context, 0, listings);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Listing biz = this.getItem(position);
		View view = convertView;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.biz_item, null);
		}
		if (biz.getImage() != null) {
			((SmartImageView) view.findViewById(R.id.imgBiz)).setImageUrl(biz.getImage());
		}
		((TextView) view.findViewById(R.id.tvTitle)).setText(biz.getTitle());
		((TextView) view.findViewById(R.id.tvPhone)).setText(biz.getPhone());
		((TextView) view.findViewById(R.id.tvAddress)).setText(biz.getAddress());
		return view;
	}
}