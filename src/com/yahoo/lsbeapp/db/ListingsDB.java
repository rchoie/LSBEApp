package com.yahoo.lsbeapp.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yahoo.lsbeapp.model.Listing;

public class ListingsDB {

	  // Database fields
	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;
 
	public ListingsDB(Context context) {
	    dbHelper = new SQLiteHelper(context);
	}

	public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	}

	public void close() {
	    dbHelper.close();
	}
	
	public void addLisitng(Listing listing) {

		Log.d("DEBUG", "Add listing " + listing.getTitle());

		ArrayList<Listing> listings = (ArrayList<Listing>) getListingByGid(listing.getId());
		if (listings.size() == 1) {
			return;
		}
		
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.GID, listing.getId());
		values.put(SQLiteHelper.TITLE, listing.getTitle());
		values.put(SQLiteHelper.STREET, listing.getStreet());
		values.put(SQLiteHelper.CITY, listing.getCity());
		values.put(SQLiteHelper.STATE, listing.getState());
		values.put(SQLiteHelper.LAT, listing.getLat());
		values.put(SQLiteHelper.LON, listing.getLon());
		values.put(SQLiteHelper.PHONE, listing.getPhone());
		values.put(SQLiteHelper.IMAGE, listing.getImage());
		values.put(SQLiteHelper.RATING, listing.getRating());
		values.put(SQLiteHelper.TIMESTAMP, Float.toString(System.currentTimeMillis()/1000));

		long insertId = database.insert(SQLiteHelper.TABLE_NAME, null, values);
		
	}
	
	public List<Listing> getAllListings() {
		
		List<Listing> listings = new ArrayList<Listing>();
		String[] allColumns = { SQLiteHelper.ID,
								SQLiteHelper.GID,
			     				SQLiteHelper.TITLE,
			     				SQLiteHelper.STREET,
			     				SQLiteHelper.CITY,
			     				SQLiteHelper.STATE,
			     				SQLiteHelper.LAT,
			     				SQLiteHelper.LON,
			     				SQLiteHelper.PHONE,
			     				SQLiteHelper.IMAGE,
			     				SQLiteHelper.RATING,
			     				SQLiteHelper.TIMESTAMP
							  };
		
		Cursor cursor = database.query(SQLiteHelper.TABLE_NAME, allColumns, null, null, null, null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
		      Listing listing = cursorToListing(cursor);
		      listings.add(listing);
		      cursor.moveToNext();
		}
	    cursor.close();
	    return listings;

	}
	
	public List<Listing> getListingByGid(String gid) {
		
		List<Listing> listings = new ArrayList<Listing>();
		String[] allColumns = { SQLiteHelper.ID,
								SQLiteHelper.GID,
			     				SQLiteHelper.TITLE,
			     				SQLiteHelper.STREET,
			     				SQLiteHelper.CITY,
			     				SQLiteHelper.STATE,
			     				SQLiteHelper.LAT,
			     				SQLiteHelper.LON,
			     				SQLiteHelper.PHONE,
			     				SQLiteHelper.IMAGE,
			     				SQLiteHelper.RATING,
			     				SQLiteHelper.TIMESTAMP
							  };
		
		String selection = SQLiteHelper.GID + " = ?";
		String[] selectionArgs = { gid };

		Cursor cursor = database.query(SQLiteHelper.TABLE_NAME, allColumns, selection, selectionArgs, null, null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
		      Listing listing = cursorToListing(cursor);
		      listings.add(listing);
		      cursor.moveToNext();
		}
	    cursor.close();
	    return listings;

	}
	
	  private Listing cursorToListing(Cursor cursor) {
		  
		  Listing listing = new Listing();
		  listing.setId(cursor.getString(1));
		  listing.setTitle(cursor.getString(2));
		  listing.setStreet(cursor.getString(3));
		  listing.setCity(cursor.getString(4));
		  listing.setState(cursor.getString(5));
		  listing.setLat(cursor.getString(6));
		  listing.setLon(cursor.getString(7));
		  listing.setPhone(cursor.getString(8));
		  listing.setImageUrl(cursor.getString(9));
		  listing.setRating(cursor.getString(10));

		  return listing;
	  }

	public void deleteListingById(String id) {
		// TODO Auto-generated method stub

		Log.d("DEBUG", "DB delete : " + id);
		String selection = SQLiteHelper.GID + " = ?";
		String[] selectionArgs = { id };
		database.delete(SQLiteHelper.TABLE_NAME, selection, selectionArgs);
	}
	  
}