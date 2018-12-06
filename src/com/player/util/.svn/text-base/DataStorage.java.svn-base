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

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.player.Player;

/**
 * @author Abhijeet
 *
 */
public class DataStorage {
	private static String activity;
	private static String currentScreen = "";
	private static String DeviceType ="";
	private static String bouquetName = "";
	private static String connectedVendor;
	private static String subscriberId=null;
	private static String mShowSeletedId = null;
	private static String showSelectedType = null;
	private static boolean status = false;
	private static String currentUser;
	private static boolean isShowSelected;
	private static boolean runningStatus=false; 	
	private static int irhandler = -1;
	private static int volPosition=0;
	private static JSONObject jo;
	private static String playing="00";
	private static String audioStatus="0";
	private static String videoStatus="0";
	private static String connectedBTDevice = "";
	private static boolean powerStatus=true;
	private static String playingUrl = "";
	private static String currentUserId ="";
	private static String ipAddress = "";
	private static String btAddress = "";
	private static String macAddress = "";
	private static String Dlandevice = "";
	private static String playingType = "";
	private static String playingEventId;
	private static String playingServiceId;
	private static String a2dpDevice;
	private static String wifiDisplayDevice;
	private static boolean isDlnaPlaying = false;
	private static JSONArray channelidList;
	private static JSONArray eventidList;
	private static boolean mobile=false;
	
	public static boolean isMobile() {
		return mobile;
	}

	public static void setMobile(boolean m) {
		DataStorage.mobile = m;
	}
	
	public static JSONArray getchannelidlist() {
		return channelidList;
	}

	public static void setchannelidlist(JSONArray channellist) {
		DataStorage.channelidList = channellist;
	}
	
	public static JSONArray geteventidlist() {
		return eventidList;
	}

	public static void seteventidlist(JSONArray eventlist) {
		DataStorage.eventidList = eventlist;
	}
	
	public static String getMacAddress() {
		return macAddress;
	}

	public static void setMacAddress(String macAddress) {
		DataStorage.macAddress = macAddress;
	}

	public static void setBTAddress(String mac){
		btAddress = mac;
	}
	
	public static String getBTAddress(){
		return btAddress;
	}
	
	public static void setIPAddress(String ip){
		ipAddress = ip;
	}
	
	public static String getIPAddress(){
		return ipAddress;
	}

	public static String getPlayingUrl() { //used in Playback 
		return playingUrl;
	}

	public static void setPlayingUrl(String playingUrl) { //used in Playback
		DataStorage.playingUrl = playingUrl;
	}
	
	public static void setPlayingType(String type){
		DataStorage.playingType = type;
	}
	
	public static String getPlayingType(){
		return playingType;
	}
	
	public static void setPlayingEvent(String eventid){
		DataStorage.playingEventId = eventid;
	}
	
	public static String getPlayingEvent(){
		return playingEventId;
	}
	
	public static void setPlayingService(String serviceid){
		DataStorage.playingServiceId = serviceid;
	}
	
	public static String getPlayingService(){
		return playingServiceId;
	}
	
	public static String getA2dpDevice() {
		return a2dpDevice;
	}

	public static void setA2dpDevice(String a2dpDevice) {
		DataStorage.a2dpDevice = a2dpDevice;
	}

	public static String getWifiDisplayDevice() {
		return wifiDisplayDevice;
	}

	public static void setWifiDisplayDevice(String wifiDisplayDevice) {
		DataStorage.wifiDisplayDevice = wifiDisplayDevice;
	}

	public static void setPowerStatus(boolean s){
		powerStatus = s;
	}
	
	public static boolean getPowerStatus(){
		return powerStatus;
	}
	
	
	public static String getConnectedBTDevice() {
		return connectedBTDevice;
	}

	public static void setConnectedBTDevice(String connectedBTDevice) {
		DataStorage.connectedBTDevice = connectedBTDevice;
	}

	public static String isPlaying() {
		return playing;
	}

	public static void setPlaying(String deviceType, String status) {
		if(deviceType.equalsIgnoreCase("audio")){ 
			audioStatus = status;
		}else if(deviceType.equalsIgnoreCase("video")){
			videoStatus = status;
		}
		DataStorage.playing = audioStatus.concat(videoStatus);
	}

	public static String getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(String currentUser) {
		DataStorage.currentUser = currentUser;
	}
	
	public static void setAboutInfo(JSONObject j){
		jo = j;
	}
	
	public static JSONObject getAboutInfo(){
		return jo;
	}
	
	public static void setVolume(int volume){
		volPosition = volume;
	}
	
	public static int getVolume(){
		return volPosition;
	}

	public static boolean isRunningStatus() {
		return runningStatus;
	}

	public static void setRunningStatus(boolean runningStatus) {
		DataStorage.runningStatus = runningStatus;
	}

	public static String getDeviceType() {
		return DeviceType;
	}
	
	public static void setDeviceType(String mDeviceType) {
		DataStorage.DeviceType = mDeviceType;
	}
	
	public static boolean isSetupDone() {
		return status;
	}

	public static void SetupDone(boolean init) {
		DataStorage.status = init;
	}
	
	public static String getConnectedVendor() {
		return connectedVendor;
	}

	public static void setConnectedVendor(String connectedVendor) {
		DataStorage.connectedVendor = connectedVendor;
	}
	
	public static int getIrhandler() {
		return irhandler;
	}

	public static void setIrhandler(int irhandler) {
		DataStorage.irhandler = irhandler;
	}

	/**
	 * @return the isShowSelected
	 */
	public static boolean isShowSelected() {
		return isShowSelected;
	}

	/**
	 * @param isShowSelected the isShowSelected to set
	 */
	public static void setShowSelected(boolean isShowSelected) {
		DataStorage.isShowSelected = isShowSelected;
	}
	
	/**
	 * @return the showSeletedId
	 */
	public static String getShowSeletedId() {
		return mShowSeletedId;
	}

	/**
	 * @param showSeletedId the showSeletedId to set
	 */
	public static void setShowSeletedId(String showSeletedId) {
		mShowSeletedId = showSeletedId;
	}

	/**
	 * @return the showSelectedType
	 */
	public static String getShowSelectedType() {
		return showSelectedType;
	}

	/**
	 * @param showSelectedType the showSelectedType to set
	 */
	public static void setShowSelectedType(String showSelectedType) {
		DataStorage.showSelectedType = showSelectedType;
	}
	/**
	 * @return the currentUserId
	 */
	public static String getCurrentUserId() {
		return currentUserId;
	}
	/**
	 * @param currentUserId the currentUserId to set
	 */
	public static void setCurrentUserId(String currentUserId) {
		DataStorage.currentUserId = currentUserId;
	}

	/**
	 * @return the currentScreen
	 */
	public static String getCurrentScreen() {
		return currentScreen;
	}
	/**
	 * @param currentScreen the currentScreen to set
	 */
	public static void setCurrentScreen(String currentScreen) {
		DataStorage.currentScreen = currentScreen;
	}
	/**
	 * @return the activity
	 */
	public static String getCurrentActivity() {
		return activity;
	}
	/**
	 * @param activity the activity to set
	 */
	public static void setCurrentActivity(String currentactivity) {
		activity = currentactivity;
	}
	/**
	 * @return
	 */
	public static String getBouquetName() {
		// TODO Auto-generated method stub
		return bouquetName;
	}
	public static void setBouquetName(String bouquetname){
		bouquetName = bouquetname;
	}
	/**
	 * @return the subscriberId
	 */
	public static String getSubscriberId() {
		return subscriberId;
	}

	/**
	 * @param subscriberId the subscriberId to set
	 */
	public static void setSubscriberId(String subscriberId) {
		DataStorage.subscriberId = subscriberId;
	}

	public static boolean isDlnaPlaying() {
		return isDlnaPlaying;
	}

	public static void setDlnaPlaying(boolean isDlnaPlaying) {
		DataStorage.isDlnaPlaying = isDlnaPlaying;
	}

	public static String getDLNAdevice() {
		return Dlandevice;
	}

	public static void setDLNAdevice(String dlandevice) {
		Dlandevice = dlandevice;
	}
	

}
