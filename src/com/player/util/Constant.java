/*
 * Copyright (c) Lukup Media Pvt Limited, India.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Lukup Media Pvt Limited ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms
 * of the licence agreement you entered into with Lukup Media Pvt Limited.
 *
 */
package com.player.util;

/**
 * This class provides the functionality to declare the global constants.
 * @author abhijeet
 */
public class Constant {
	public static boolean DEBUG = true;
	
	public static String model = "X";
	
	public static boolean DVB = false;
	
	public static String DeviceName = "Lukup Player";
	
	public static final String APP_VERSION = "2.0.5";
	
	public static final boolean Mobile = false;	//mobile
	
	public static String base_url = "http://staging.lukup.com";
	
	public static String parse_url = "staging.lukup.com";
	
	public static String CloudURL = base_url + "/SMS/RecordStorageServlet?method=retrieve&subscriberid=";
	
	public static String SCHEDULE_RECORD = base_url + "/SMS/RecordingScheduleServlet?";
	
	public static String LoginURL = base_url+ "/mpsubscribe/AuthServlet?type=login&email=";//"/"+mUserName+"&pwd="+mPassword
	
	public static String DELETE_RECORDING = base_url + "/SMS/RecordStorageServlet?method=delete&subscriberid=";
	
	public static String SEARCH_URL = base_url + "/search/search"; 
	
	public static String ISSUBSCRIBE_URL = base_url + "/mpsubscribe/RequestJsp.jsp?method=isSubscribed&contentId=";

	public static String ISCHANNELSUBSCRIBE_URL = base_url + "/mpsubscribe/SubscribeServlet?type=subscribedServices&subscriberid=";
	
	public static String SUBSCRIBE = base_url + "/mpsubscribe/SubscribeServlet?chid=";
	
	public static String FETCHBOUQUET_URL = base_url + "/portal/BouquetListingServlet?distributorid=";
	
	public static String FETCHEVENT_URL = base_url + "/mpsubscribe/FetchEventServlet?serviceid="; //&distributorid need to be mentioned
}