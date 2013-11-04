package com.yahoo.lsbeapp.model;


import org.json.JSONException;
import org.json.JSONObject;

public class BaseModel {
	public JSONObject jsonObject;
	
	public BaseModel() {
		super();
	}
	
	public BaseModel(JSONObject json) {
		this.jsonObject = json;
	}
	
	public String getString(String key) {
		try {
			return jsonObject.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int getInt(String key) {
		try {
			return jsonObject.getInt(key);
		} catch (JSONException e) {
            e.printStackTrace();
			return 0;
		}
	}
	
    protected long getLong(String name) {
        try {
            return jsonObject.getLong(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

	public double getDouble(String key) {
		try {
			return jsonObject.getDouble(key);
		} catch (JSONException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public JSONObject getJSONObject(String key) {
		JSONObject jsonObj;
		try {
			jsonObj = jsonObject.getJSONObject(key);
		} catch(JSONException e) {
			jsonObj = null;
		}
		return jsonObj;
	}
	
	public static BaseModel fromJSON(JSONObject jsonObj) {
		BaseModel baseModel = new BaseModel();
		return baseModel;
	}
}

