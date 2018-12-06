/**
* Classname : DataProvider
* 
* Version information : 1.0
* 
* Date : 19th Aug 2015
* 
* Copyright (c) Lukup Media Pvt Limited, India.
* All rights reserved.
*
* This software is the confidential and proprietary information of Lukup Media Pvt Limited ("Confidential Information").
* You shall not disclose such Confidential Information and shall use it only in accordance with the terms
* of the licence agreement you entered into with Lukup Media Pvt Limited.
*
*/

package com.player.util;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;


/**
 * DataProvider 	: Responsible for SetupDB creation 
 * 			   
 *
 * @Version : 1.0
 * @Author  : Lukup
 */
public class DataProvider extends ContentProvider {
 static final String PROVIDER_NAME = "com.player.util.DataProvider";
 static final String URL = "content://" + PROVIDER_NAME + "/cte";
 public static final Uri CONTENT_URI = Uri.parse(URL);
 private String TAG = "DataProvider";
 public static final String DeviceType = "DeviceType";
 public static final String ConnectedVendor = "ConnectedVendor";
 public static final String SubscriberID = "SubscriberID";
 public static final String CurrentUserId = "CurrentUserId";
 public static final String RemoteConnected = "RemoteConnected";
 public static final String Helptip = "Helptip";
 public static final String Volume = "Volume";
 public static final String ConnectionType = "ConnectionType";
 public static final String BTAddress = "BTAddress";
 public static final String WIFIAddress= "WIFIAddress";
 public static final String NetworkConnected = "NetworkConnected";
 public static final String IsSetup = "IsSetup";
 public static final String IsDlnaPlaying = "IsDlnaPlaying";
 public static final String DlnaDeviceName = "DlnaDeviceName";
 
 static final int uriCode = 1;
 static final UriMatcher uriMatcher;
 private static HashMap<String, String> values;
 static {
  uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
  uriMatcher.addURI(PROVIDER_NAME, "cte", uriCode);
  uriMatcher.addURI(PROVIDER_NAME, "cte/*", uriCode);
 }

/**
 * Callback method - Deleting the Setup DB
 * @param URI URI location to the table
 * @param selection Select Query
 * @param selectionArgs Arguments to be selected
 */
	 @Override
	 public int delete(Uri uri, String selection, String[] selectionArgs) {
		  int count = 0;
		  switch (uriMatcher.match(uri)) {
		  case uriCode:
			  count = db.delete(TABLE_NAME, selection, selectionArgs);
			  break;
		  default:
			  throw new IllegalArgumentException("Unknown URI " + uri);
		  }
		  getContext().getContentResolver().notifyChange(uri, null);
		  return count;
	 }

 /**
  * Callback method - Gets URI type
  * @param URI URI location to the table
  */
	 @Override
	 public String getType(Uri uri) {
	  switch (uriMatcher.match(uri)) {
	  case uriCode:
		  return "vnd.android.cursor.dir/cte";
	  default:
		  throw new IllegalArgumentException("Unsupported URI: " + uri);
	  }
	}

 /**
  * Callback method - Insert the data into the DB
  * @param values data to inserted in to the DB
  */
	 @Override
	 public Uri insert(Uri uri, ContentValues values) {
		  long rowID = db.insert(TABLE_NAME, "", values);
		  if(Constant.DEBUG)  Log.d(TAG," insert: rowID" + rowID);
		  if (rowID > 0) {
			   Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			   getContext().getContentResolver().notifyChange(_uri, null);
			   if(Constant.DEBUG)  Log.d(TAG," insert: _uri" + _uri);
			   return _uri;
		  }
		  throw new SQLException("Failed to add a record into " + uri);
	 }

 /**
  * Callback method - onCreate - Creation of DB
  */
	 @Override
	 public boolean onCreate() {
	  Context context = getContext();
	  DatabaseHelper dbHelper = new DatabaseHelper(context);
	  db = dbHelper.getWritableDatabase();
	  if (db != null) {
		 return true;
	  }
	  return false;
    }

 /**
  * Callback method - query - Query specific data from DB
  * @param URI URI location to the table
  * @param projection
  * @param selection  Selection
  * @param selectionArgs Select Args
  * @param sortOrder Sorting order
  * @param selectionArgs Arguments to be selected
  */
	 @Override
	 public Cursor query(Uri uri, String[] projection, String selection,
	   String[] selectionArgs, String sortOrder) {
		  SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		  qb.setTables(TABLE_NAME);
		
		  switch (uriMatcher.match(uri)) {
		  case uriCode:
			  qb.setProjectionMap(values);
			  break;
		  default:
			  throw new IllegalArgumentException("Unknown URI " + uri);
		  }
		  if (sortOrder == null || sortOrder == "") {
			  sortOrder = SubscriberID;
		  }
		  Cursor c = qb.query(db, projection, selection, selectionArgs, null,
		    null, sortOrder);
		  c.setNotificationUri(getContext().getContentResolver(), uri);
		  return c;
	 }

 /**
  * Callback method - update - Updation of DB with specific value
  */
	 @Override
	 public int update(Uri uri, ContentValues values, String selection,
	   String[] selectionArgs) {
	  int count = 0;
	  switch (uriMatcher.match(uri)) {
	  case uriCode:
		  count = db.update(TABLE_NAME, values, selection, selectionArgs);
		  break;
	  default:
		  throw new IllegalArgumentException("Unknown URI " + uri);
	  }
	  getContext().getContentResolver().notifyChange(uri, null);
	  return count;
	 }
	 
 
	private SQLiteDatabase db;
	private static final String DATABASE_NAME = "HOMEDB";
	private static final String TABLE_NAME = "Home";
	private static final int DATABASE_VERSION = 5;
	static final String CREATE_DB_TABLE = " CREATE TABLE " + TABLE_NAME 
			   + " 	(id INTEGER PRIMARY KEY AUTOINCREMENT, "
			   + 	SubscriberID+" TEXT NOT NULL,"+  DeviceType +" TEXT NOT NULL,"+ ConnectedVendor +" TEXT NOT NULL,"+ CurrentUserId +" TEXT NOT NULL, "
			   +  	Helptip +" TEXT NOT NULL,"+ RemoteConnected +" TEXT NOT NULL, "+ NetworkConnected +" TEXT NOT NULL,"+IsSetup +" TEXT NOT NULL ,"
			   +    IsDlnaPlaying + " TEXT NOT NULL," + DlnaDeviceName + " TEXT NOT NULL,"
			   +  	Volume +" INTEGER,"+ConnectionType +" TEXT NOT NULL,"+ BTAddress +" TEXT NOT NULL,"+ WIFIAddress + " TEXT NOT NULL);";	

	 private static class DatabaseHelper extends SQLiteOpenHelper {
	  DatabaseHelper(Context context) {
		  super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

  /**
   * Callback method - onCreate - Creation of DB
   */
	  @Override
	  public void onCreate(SQLiteDatabase db) {
		  db.execSQL(CREATE_DB_TABLE);
	  }

  /**
   * Callback method - onUpgrade - Creation of DB
   */
	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		   db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		   onCreate(db);
	  }
	}
  }

