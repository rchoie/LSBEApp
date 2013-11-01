package com.yahoo.lsbeapp;


import java.util.List;

import com.yahoo.lsbeapp.R;

import com.yahoo.lsbeapp.model.Category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategoryAdapter extends ArrayAdapter<Category> {

	public CategoryAdapter(Context context, List<Category> categories) {
		super(context, 0, categories);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.category_item, null);
		}
		
		Category category = getItem(position);
		
		TextView tvCategory = (TextView) view.findViewById(R.id.tvCategory);
		tvCategory.setText(category.getCateoryName());
		
		return view;
	}
}