/*
* Copyright (c) Lukup Media Pvt Limited, India.
* All rights reserved.
*
* This software is the confidential and proprietary information of Lukup Media Pvt Limited ("Confidential Information").
* You shall not disclose such Confidential Information and shall use it only in accordance with the terms
* of the licence agreement you entered into with Lukup Media Pvt Limited.
*
*/
package com.player.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;


/**
 * @author abhijeet
 *
 */
public class CacheDockData {
	private static boolean expiry;

	public static ArrayList<ProgramInfo> EventList = new ArrayList<ProgramInfo>();
	public static ArrayList<ChannelInfo> ServiceList = new ArrayList<ChannelInfo>();
	public static ArrayList<BouquetInfo> BouquetList = new ArrayList<BouquetInfo>();
	
	public static ArrayList<ProgramInfo> favouriteEventList = new ArrayList<ProgramInfo>();
	public static ArrayList<ChannelInfo> favouriteServiceList = new ArrayList<ChannelInfo>();
	
	public static ArrayList<ProgramInfo> historyEventList = new ArrayList<ProgramInfo>();
	public static ArrayList<ChannelInfo> historyServiceList = new ArrayList<ChannelInfo>();
	
	public static ArrayList<ProgramInfo> RecordEventList = new ArrayList<ProgramInfo>();
	public static ArrayList<ProgramInfo> ReminderEventList = new ArrayList<ProgramInfo>();
	
	public static ArrayList<HashMap<String,String>> fragmentFeaturedList = new ArrayList<HashMap<String,String>>();
	
	public static ArrayList<HashMap<String,String>> connectedDeviceList = new ArrayList<HashMap<String,String>>();
	
	public static ArrayList<HashMap<String,String>> notificationList = new ArrayList<HashMap<String,String>>();
	
	
	public static boolean isExpiryFeatured() {
		return expiry;
	}
	public static void setExpiryFeatured(boolean expiry) {
		CacheDockData.expiry = expiry;
	}
	
}
