package com.yahoo.lsbeapp.model;

import java.util.ArrayList;

import org.json.JSONArray;

public class Category {
	
	String cateoryName;
	int categoryId;
	
	public String getCateoryName() {
		return cateoryName;
	}
	
	public void setCateoryName(String cateoryName) {
		this.cateoryName = cateoryName;
	}
	
	public int getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

    public static ArrayList<Category> fromJson(JSONArray jsonArray) {
        ArrayList<Category> categories = new ArrayList<Category>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            String categoryName = null;
            try {
                categoryName = jsonArray.getString(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Category category = new Category();
            category.setCateoryName(categoryName);

            if (category != null) {
                categories.add(category);
            }
        }

        return categories;
    }
    
    public String toString()  {
    	return cateoryName;
    }
}
