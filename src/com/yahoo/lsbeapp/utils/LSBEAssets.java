package com.yahoo.lsbeapp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.res.AssetManager;

public class LSBEAssets {
	public static JSONObject getFakeResults(Activity activity, String fileName) {
		AssetManager am = activity.getAssets();
		JSONObject jsonObject;
		
		try { 
			InputStream is = am.open(fileName);
			InputStreamReader isReader = new InputStreamReader(is);
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(isReader);

			String read = br.readLine();
			while(read != null) {
			    sb.append(read);
			    read = br.readLine();
			}
			jsonObject = new JSONObject(sb.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return jsonObject;		
	}
	
}
