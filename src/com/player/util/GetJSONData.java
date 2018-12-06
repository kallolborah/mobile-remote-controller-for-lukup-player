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

import java.io.PrintWriter;
import java.io.StringWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * @author abhijeet
 *
 */
public class GetJSONData {

	private static String TAG = "GetJSONData";

	/**
	 * @param line
	 * @throws JSONException
	 */
	public static JSONObject getJsonData(String line,String value) throws JSONException {
		try {
//			if(Constant.DEBUG)  Log.d(TAG  , " line : "+line);	
			if(line != null && !(line.toString().trim().equalsIgnoreCase(""))) {
				JSONObject jsonObject = new JSONObject(line.toString());
				if(jsonObject != null){
					JSONObject jsonData = jsonObject.getJSONObject("data");
					String data = jsonData.getString(value);
//					if(Constant.DEBUG)  Log.d(TAG , " Data : "+data);	
					JSONObject paramsObject = jsonData.getJSONObject(value);
//					if(Constant.DEBUG)  Log.d(TAG , " a jsonData : "+paramsObject.toString());	
					if(paramsObject != null) {
//						if(Constant.DEBUG)  Log.d(TAG , " jsonData : "+paramsObject.toString());	
						return paramsObject;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
		return null;
	}
	
	public static JSONArray getJsonArray(String line,String value) throws JSONException {
		try {
			if(line != null && !(line.toString().trim().equalsIgnoreCase(""))) {
				JSONObject jsonObject = new JSONObject(line.toString());
				if(jsonObject != null){
					JSONArray jsonData = jsonObject.getJSONArray(value);
					if(jsonData != null) {
						return jsonData;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
		return null;
	}
}
