/*
* Copyright (c) Lukup Media Pvt Limited, India.
* All rights reserved.
*
* This software is the confidential and proprietary information of Lukup Media Pvt Limited ("Confidential Information").
* You shall not disclose such Confidential Information and shall use it only in accordance with the terms
* of the licence agreement you entered into with Lukup Media Pvt Limited.
*
*/
package com.player.action;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.player.R;
import com.player.util.Constant;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.util.Utils;
import com.player.widget.OverLay;


/**
 * @author abhijeet
 *
 */
public class Lock {
	private static Dialog overlayView = null;
	
	public static void getImagePassword(final Activity activity,JSONArray imagelist,final String method, final String Id, final String name){
		if (Constant.DEBUG)	Log.d("Lock", "getImagePassword() ");
		final ArrayList<HashMap<String,String>> imageList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = null;
		for(int i=0;i<imagelist.length();i++){
			try {
				String object = imagelist.getString(i).trim();

				int drawableId = 0;
				int id = activity.getResources().getIdentifier(object, "drawable", activity.getPackageName());
				map = new HashMap<String, String>();
				if(id != 0){
					drawableId = id;
				}else{
					if(object.equalsIgnoreCase("image01")){
						drawableId = R.drawable.image01;
					}else if(object.equalsIgnoreCase("image02")){
						drawableId = R.drawable.image02;
					}else if(object.equalsIgnoreCase("image03")){
						drawableId = R.drawable.image03;
					}else if(object.equalsIgnoreCase("image04")){
						drawableId = R.drawable.image04;
					}else if(object.equalsIgnoreCase("image05")){
						drawableId = R.drawable.image05;
					}else if(object.equalsIgnoreCase("image06")){
						drawableId = R.drawable.image06;
					}else if(object.equalsIgnoreCase("image07")){
						drawableId = R.drawable.image07;
					}else if(object.equalsIgnoreCase("image08")){
						drawableId = R.drawable.image08;
					}else if(object.equalsIgnoreCase("image09")){
						drawableId = R.drawable.image09;
					}else if(object.equalsIgnoreCase("image10")){
						drawableId = R.drawable.image10;
					}else if(object.equalsIgnoreCase("image11")){
						drawableId = R.drawable.image11;
					}else if(object.equalsIgnoreCase("image12")){
						drawableId = R.drawable.image12;
					}
				}
				map.put(ScreenStyles.LIST_KEY_THUMB_URL, drawableId+"");
				imageList.add(map);

			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
			}
		}
		if(imageList != null && imageList.size() >0){
			if(activity != null){
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						OverLay overlay = new OverLay(activity);
						if(overlayView != null){
							if(overlayView.isShowing()){
								return;
							}
						}
						overlayView = overlay.getImagePwdOverlay(0, null, imageList,method,Id, name);
						if(overlayView != null){
							overlayView.show();
							ImageView closeBtn = (ImageView) overlayView.findViewById(R.id.closeBtn);
							if(closeBtn != null){
								closeBtn.bringToFront();
								closeBtn.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										if(overlayView.isShowing()){
											overlayView.dismiss();
										}
									}
								});
							}
						}					
					}
				});

			}
		}
	}
	
	
}
