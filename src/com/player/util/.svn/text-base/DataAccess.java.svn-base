/**
* Classname : DataAccess
* 
* Version information : 1.0
* 
* Date : 28th Aug 2015
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

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.player.Player;

/**
 * DataAccess : Interacts with the Setup Content Provider - com.setup.DataProvider. 
 *
 * @Version   : 1.0
 * @Author    : Lukup
 */
public class DataAccess {
	
	public static final Uri CONTENT_URI = Uri.parse("content://com.player.util.DataProvider/cte");
	public static final String TAG = "DataAccess";
	
	/**
	 * Returns number of records from the content provider
	 */
	public int NoOfRecords()
	 {
		Cursor countCursor = null;
		countCursor = Player.getContext().getContentResolver().query(CONTENT_URI,
	         new String[] {"count(*) AS count"},
	         null,
	         null,
	         null);

		 countCursor.moveToFirst();
		 int count = countCursor.getInt(0);
		 if(Constant.DEBUG) Log.w(TAG, "NoOfRecords :: count : " + count);
		 return count;
	 }
	
	public String isNetworkConnected(){
		
		String networkConnected = "";
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "isNetworkConnected() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "isNetworkConnected() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "isNetworkConnected() cursor(2) ::"+cursor.getString(cursor.getColumnIndex("NetworkConnected")));
				networkConnected = cursor.getString(cursor.getColumnIndex("NetworkConnected"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "NetworkConnected().NetworkConnected is ::"+ networkConnected);
		
		return networkConnected;
		
	}
	
	/**
	 * Get the DeviceType from DB
	 */
	public String getDeviceType()
	{
		String DeviceType = "";
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getDeviceType() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getDeviceType() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getDeviceType() cursor(2) ::"+cursor.getString(cursor.getColumnIndex("DeviceType")));
				DeviceType = cursor.getString(cursor.getColumnIndex("DeviceType"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getDeviceType().DeviceType is ::"+ DeviceType);
		return DeviceType;
	}

	/**
	 * Get the SubscriberID from DB
	 */
	public String getSubscriberID()
	{
		String SubscriberID = "";
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getSubscriberID() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getSubscriberID() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getSubscriberID() cursor(1) ::"+cursor.getString(cursor.getColumnIndex("SubscriberID")));
				SubscriberID = cursor.getString(cursor.getColumnIndex("SubscriberID"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getSubscriberID().SubscriberID is ::"+ SubscriberID);
		return SubscriberID;
	}
	
	/**
	 * Get the ConnectedVendor[TV Operator] from DB
	 */
	public String getConnectedVendor()
	{
		String ConnectedVendor = "";
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getConnectedVendor() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getConnectedVendor() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getConnectedVendor() cursor(3) ::"+cursor.getString(cursor.getColumnIndex("ConnectedVendor")));
				ConnectedVendor = cursor.getString(cursor.getColumnIndex("ConnectedVendor"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getConnectedVendor().ConnectedVendor is ::"+ ConnectedVendor);
		return ConnectedVendor;
	}	
	
	/**
	 * Get Current User ID from DB
	 */
	public String getCurrentUserId()
	{
		String CurrentUserId = "";
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getCurrentUserId() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getCurrentUserId() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getCurrentUserId() ::"+cursor.getString(cursor.getColumnIndex("CurrentUserId")));
				CurrentUserId = cursor.getString(cursor.getColumnIndex("CurrentUserId"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getCurrentUserId().CurrentUserId is ::"+ CurrentUserId);
		return CurrentUserId;
	}	
	
	/**
	 * Get ServiceId from DB
	 */
	/*public String getServiceId()
	{
		String ServiceId = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getServiceId() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getServiceId() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getServiceId() ::"+cursor.getString(cursor.getColumnIndex("ServiceId")));
				ServiceId = cursor.getString(cursor.getColumnIndex("ServiceId"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getServiceId().getServiceId is ::"+ ServiceId);
		return ServiceId;
	}	
	*/
	/**
	 * Get ShowSelectedId from DB
	 */
	/*public String getShowSelectedId()
	{
		String ShowSelectedId = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getShowSelectedId() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getShowSelectedId() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getShowSelectedId() ::"+cursor.getString(cursor.getColumnIndex("ShowSelectedId")));
				ShowSelectedId = cursor.getString(cursor.getColumnIndex("ShowSelectedId"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getShowSelectedId().ShowSelectedId is ::"+ ShowSelectedId);
		return ShowSelectedId;
	}	
	
	*//**
	 * Get SelectedType from DB
	 *//*	
	public String getSelectedType()
	{
		String SelectedType = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getSelectedType() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getSelectedType() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getSelectedType() ::"+cursor.getString(cursor.getColumnIndex("SelectedType")));
				SelectedType = cursor.getString(cursor.getColumnIndex("SelectedType"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getSelectedType().SelectedType is ::"+ SelectedType);
		return SelectedType;
	}	
	
	*//**
	 * Get playingUrl from DB
	 *//*
	public String getplayingUrl()
	{
		String playingUrl = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getplayingUrl() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getplayingUrl() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getplayingUrl() ::"+cursor.getString(cursor.getColumnIndex("playingUrl")));
				playingUrl = cursor.getString(cursor.getColumnIndex("playingUrl"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getplayingUrl().playingUrl is ::"+ playingUrl);
		return playingUrl;
	}	
	
	*//**
	 * Get A2DPDevice from DB
	 *//*	
	public String getA2DPDevice()
	{
		String A2DPDevice = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getA2DPDevice() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getA2DPDevice() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getA2DPDevice() ::"+cursor.getString(cursor.getColumnIndex("A2DPDevice")));
				A2DPDevice = cursor.getString(cursor.getColumnIndex("A2DPDevice"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getA2DPDevice().A2DPDevice is ::"+ A2DPDevice);
		return A2DPDevice;
	}	
	
	*//**
	 * Get WifiDisplayDevice from DB
	 *//*		
	public String getWifiDisplayDevice()
	{
		String WifiDisplayDevice = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getWifiDisplayDevice() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getWifiDisplayDevice() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getWifiDisplayDevice() ::"+cursor.getString(cursor.getColumnIndex("WifiDisplayDevice")));
				WifiDisplayDevice = cursor.getString(cursor.getColumnIndex("WifiDisplayDevice"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getWifiDisplayDevice().WifiDisplayDevice is ::"+ WifiDisplayDevice);
		return WifiDisplayDevice;
	}	
	
	*//**
	 * Get runningStatus from DB
	 *//*		
	public String getrunningStatus()
	{
		String runningStatus = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getrunningStatus() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getrunningStatus() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getrunningStatus() ::"+cursor.getString(cursor.getColumnIndex("runningStatus")));
				runningStatus = cursor.getString(cursor.getColumnIndex("runningStatus"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getrunningStatus().runningStatus is ::"+ runningStatus);
		return runningStatus;
	}		*/
	
	/**
	 * Get playing from DB
	 */	
	/*public String getplaying()
	{
		String playing = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getplaying() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getplaying() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getplaying() ::"+cursor.getString(cursor.getColumnIndex("playing")));
				playing = cursor.getString(cursor.getColumnIndex("playing"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getplaying().WifiDisplayDevice is ::"+ playing);
		return playing;
	}		
	
	*//**
	 * Get PlayingType from DB
	 *//*
	public String getPlayingType()
	{
		String PlayingType = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getPlayingType() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getPlayingType() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getPlayingType() ::"+cursor.getString(cursor.getColumnIndex("PlayingType")));
				PlayingType = cursor.getString(cursor.getColumnIndex("PlayingType"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getPlayingType().PlayingType is ::"+ PlayingType);
		return PlayingType;
	}		
	
	*//**
	 * Get isShowSelected from DB
	 *//*
	public String getisShowSelected()
	{
		String isShowSelected = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getisShowSelected() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getisShowSelected() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getisShowSelected() ::"+cursor.getString(cursor.getColumnIndex("isShowSelected")));
				isShowSelected = cursor.getString(cursor.getColumnIndex("isShowSelected"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getisShowSelected().isShowSelected is ::"+ isShowSelected);
		return isShowSelected;
	}		
	
	*//**
	 * Get MacAddress from DB
	 *//*
	public String getMacAddress()
	{
		String MacAddress = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getMacAddress() records ::"+records);
		Cursor cursor = null;
		if(records > 0)
		{
			cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getMacAddress() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getMacAddress() ::"+cursor.getString(cursor.getColumnIndex("MacAddress")));
				MacAddress = cursor.getString(cursor.getColumnIndex("MacAddress"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getMacAddress().MacAddress is ::"+ MacAddress);
		return MacAddress;
	}		
	
	*//**
	 * Get IPAddress from DB
	 *//*
	public String getIPAddress()
	{
		String IPAddress = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getIPAddress() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getIPAddress() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getIPAddress() ::"+cursor.getString(cursor.getColumnIndex("IPAddress")));
				IPAddress = cursor.getString(cursor.getColumnIndex("IPAddress"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getIPAddress().IPAddress is ::"+ IPAddress);
		return IPAddress;
	}		
	
	*//**
	 * Get PlayingEventId from DB
	 *//*
	public String getPlayingEventId()
	{
		String PlayingEventId = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getPlayingEventId() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getPlayingEventId() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getPlayingEventId() ::"+cursor.getString(cursor.getColumnIndex("PlayingEventId")));
				PlayingEventId = cursor.getString(cursor.getColumnIndex("PlayingEventId"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getPlayingEventId().PlayingEventId is ::"+ PlayingEventId);
		return PlayingEventId;
	}		
	
	*//**
	 * Get AboutFWVer from DB
	 *//*
	public String getAboutFWVer()
	{
		String AboutFWVer = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getAboutFWVer() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getAboutFWVer() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getAboutFWVer() ::"+cursor.getString(cursor.getColumnIndex("AboutFWVer")));
				AboutFWVer = cursor.getString(cursor.getColumnIndex("AboutFWVer"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getAboutFWVer().AboutFWVer is ::"+ AboutFWVer);
		return AboutFWVer;
	}	
	
	*//**
	 * Get AboutPortVer from DB
	 *//*
	public String getAboutPortVer()
	{
		String AboutPortVer = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getAboutPortVer() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getAboutPortVer() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getAboutPortVer() ::"+cursor.getString(cursor.getColumnIndex("AboutPortVer")));
				AboutPortVer = cursor.getString(cursor.getColumnIndex("AboutPortVer"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getAboutPortVer().AboutPortVer is ::"+ AboutPortVer);
		return AboutPortVer;
	}	
	
	*//**
	 * Get AboutPortVer from DB
	 *//*
	public String getAboutPlayerVer()
	{
		String AboutPlayerVer = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getAboutPlayerVer() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getAboutPlayerVer() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getAboutPlayerVer() ::"+cursor.getString(cursor.getColumnIndex("AboutPlayerVer")));
				AboutPlayerVer = cursor.getString(cursor.getColumnIndex("AboutPlayerVer"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getAboutPlayerVer().AboutPlayerVer is ::"+ AboutPlayerVer);
		return AboutPlayerVer;
	}	
	
	*//**
	 * Get AboutModel from DB
	 *//*
	public String getAboutModel()
	{
		String AboutModel = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getAboutModel() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getAboutModel() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getAboutModel() cursor(4) ::"+cursor.getString(cursor.getColumnIndex("AboutModel")));
				AboutModel = cursor.getString(cursor.getColumnIndex("AboutModel"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getAboutModel().AboutModel is ::"+ AboutModel);
		return AboutModel;
	}	
	
	*//**
	 * Get AboutIP from DB
	 *//*
	public String getAboutIP()
	{
		String AboutIP = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getAboutIP() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getAboutIP() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getAboutIP() cursor(4) ::"+cursor.getString(cursor.getColumnIndex("AboutIP")));
				AboutIP = cursor.getString(cursor.getColumnIndex("AboutIP"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getAboutIP().AboutIP is ::"+ AboutIP);
		return AboutIP;
	}	
	
	*//**
	 * Get AboutMac from DB
	 *//*
	public String getAboutMac()
	{
		String AboutMac = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getAboutMac() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getAboutMac() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getAboutMac() cursor(4) ::"+cursor.getString(cursor.getColumnIndex("AboutMac")));
				AboutMac = cursor.getString(cursor.getColumnIndex("AboutMac"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getAboutMac().AboutMac is ::"+ AboutMac);
		return AboutMac;
	}	
	
	*//**
	 * Get PlayingService from DB
	 *//*
	public String getPlayingService()
	{
		String PlayingService = null;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getPlayingService() records ::"+records);
		if(records > 0)
		{
			Cursor cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getPlayingService() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getPlayingService() cursor(4) ::"+cursor.getString(cursor.getColumnIndex("PlayingService")));
				PlayingService = cursor.getString(cursor.getColumnIndex("PlayingService"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getPlayingService().PlayingService is ::"+ PlayingService);
		return PlayingService;
	}		
	*/
	/**
	 * Get IsBTServiceConnected from DB
	 */
	public String getIsRemoteConnected()
	{
		String IsRemoteConnected = "";
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getIsRemoteConnected() records ::"+records);
		Cursor cursor = null;
		if(records > 0)
		{
			cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getIsRemoteConnected() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getIsRemoteConnected() RemoteConnected) ::"+cursor.getString(cursor.getColumnIndex("RemoteConnected")));
				IsRemoteConnected = cursor.getString(cursor.getColumnIndex("RemoteConnected"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getIsRemoteConnected().IsRemoteConnected is ::"+ IsRemoteConnected);
		return IsRemoteConnected;
	}
	
	
	
	public String getIsDlnaPlaying()
	{
		String IsDlnaPlaying = "";
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getIsRemoteConnected() records ::"+records);
		Cursor cursor = null;
		if(records > 0)
		{
			cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getIsDlnaPlaying() cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getIsDlnaPlaying() IsDlnaPlaying) ::"+cursor.getString(cursor.getColumnIndex("IsDlnaPlaying")));
				IsDlnaPlaying = cursor.getString(cursor.getColumnIndex("IsDlnaPlaying"));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getIsRemoteConnected().IsRemoteConnected is ::"+ IsDlnaPlaying);
		return IsDlnaPlaying;
	}
	
	
	public String getDlnaDeviceName()
	{
		String DlnaDeviceName = "";
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getDlnaDeviceName records ::"+records);
		Cursor cursor = null;
		if(records > 0)
		{
			cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getDlnaDeviceName cursor(0) ::"+cursor.getString(cursor.getColumnIndex("id")));
				if(Constant.DEBUG) Log.w(TAG, "getDlnaDeviceName DlnaDeviceName) ::"+cursor.getString(cursor.getColumnIndex(DataProvider.DlnaDeviceName)));
				DlnaDeviceName = cursor.getString(cursor.getColumnIndex(DataProvider.DlnaDeviceName));
			}
		}
		
		if(Constant.DEBUG) Log.w(TAG, "getDlnaDeviceName().DlnaDeviceName is ::"+ DlnaDeviceName);
		return DlnaDeviceName;
	}
	
	
	/**
	 * Get Helptip from DB
	 */
	public String getHelptip()
	{
		String Helptip = "";
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getHelptip() records ::"+records);
		Cursor cursor = null;
		if(records > 0)
		{
			cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getHelptip() Helptip) ::"+cursor.getString(cursor.getColumnIndex("Helptip")));
				Helptip = cursor.getString(cursor.getColumnIndex("Helptip"));
			}
		}

		return Helptip;
	}		
	
	/**
	 * Get Volume from DB
	 */
	public int getVolume()
	{
		int Volume = 0;
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getVolume() records ::"+records);
		Cursor cursor = null;
		if(records > 0)
		{
			cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getVolume() Volume) ::"+cursor.getInt(cursor.getColumnIndex("Volume")));
				Volume = cursor.getInt(cursor.getColumnIndex("Volume"));
			}
		}
		return Volume;
	}		
	
	
	/**
	 * Get Connection Type from DB
	 */
	public String getConnectionType()
	{
		String type = "";
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getConnectionType() records ::"+records);
		Cursor cursor = null;
		if(records > 0)
		{
			cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getConnectionType() tyep) ::"+cursor.getInt(cursor.getColumnIndex("ConnectionType")));
				type = cursor.getString(cursor.getColumnIndex("ConnectionType"));
			}
		}
		return type;
	}	
	
	
	/**
	 * Get Connection Type from DB
	 */
	public String getBTAddress()
	{
		String address = "";
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getBTAddress() records ::"+records);
		Cursor cursor = null;
		if(records > 0)
		{
			cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getBTAddress()  ::"+cursor.getInt(cursor.getColumnIndex("BTAddress")));
				address = cursor.getString(cursor.getColumnIndex("BTAddress"));
			}
		}
		return address;
	}	
	
	public String getWifiAddress()
	{
		String address = "";
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getBTAddress() records ::"+records);
		Cursor cursor = null;
		if(records > 0)
		{
			cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getBTAddress()  ::"+cursor.getInt(cursor.getColumnIndex("WIFIAddress")));
				address = cursor.getString(cursor.getColumnIndex("WIFIAddress"));
			}
		}
		return address;
	}
	
	
	
	public String getIsSetup()
	{
		String IsSetup = "";
		int records = NoOfRecords();
		if(Constant.DEBUG) Log.w(TAG, "getIsSetup() records ::"+records);
		Cursor cursor = null;
		if(records > 0)
		{
			cursor = Player.getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
			
			if(cursor != null)
			{
				cursor.moveToFirst();
				if(Constant.DEBUG) Log.w(TAG, "getIsSetup()  ::"+cursor.getInt(cursor.getColumnIndex("IsSetup")));
				IsSetup = cursor.getString(cursor.getColumnIndex("IsSetup"));
			}
		}
		return IsSetup;
	}
	
	/**
	 * Update the column in SetupDB with given value
	 */
	public void updateSetupDB(String Key, String Value)
	{
		DataAccess dataAccess = new DataAccess();
		int nRecords = dataAccess.NoOfRecords();
		if(Constant.DEBUG)  Log.d(TAG,"updateSetupDB : nRecords" + nRecords);
		if( nRecords <= 0)
		{
			ContentValues values = new ContentValues();
			values.put(Key, Value);
			Uri uri = Player.getContext().getContentResolver().insert(CONTENT_URI, values);
			if(Constant.DEBUG)  Log.d(TAG,"updateSetupDB : Insert uri" + uri);
		}
		else
		{
			ContentValues values = new ContentValues();
			values.put(Key, Value);
			int result = Player.getContext().getContentResolver().update(CONTENT_URI, values, null, null);
			if(Constant.DEBUG)  Log.d(TAG,"updateSetupDB : Updated result" + result);
		}
	}
	
	/**
	 * Update the column in SetupDB with given value
	 */
	public void updateSetupDBWithInt(String Key, int Value)
	{
		DataAccess dataAccess = new DataAccess();
		int nRecords = dataAccess.NoOfRecords();
		if(Constant.DEBUG)  Log.d(TAG,"updateSetupDB : nRecords" + nRecords);
		if( nRecords <= 0)
		{
			ContentValues values = new ContentValues();
			values.put(Key, Value);
			Uri uri = Player.getContext().getContentResolver().insert(CONTENT_URI, values);
			if(Constant.DEBUG)  Log.d(TAG,"updateSetupDB : Insert uri" + uri);
		}
		else
		{
			ContentValues values = new ContentValues();
			values.put(Key, Value);
			int result = Player.getContext().getContentResolver().update(CONTENT_URI, values, null, null);
			if(Constant.DEBUG)  Log.d(TAG,"updateSetupDB : Updated result" + result);
		}
	}	
}
