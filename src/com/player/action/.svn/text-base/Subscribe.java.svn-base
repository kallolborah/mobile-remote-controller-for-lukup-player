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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.player.R;
import com.player.Layout;
import com.player.apps.Search;
import com.player.util.CommonUtil;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.util.Utils;
import com.player.widget.HelpText;
import com.player.widget.OverLay;


public class Subscribe {
	
	private static Activity mActivity;
	private static LinearLayout mContainer;
	private static boolean isLukupSelected;
	private static String selectedId;
	private static String selectedType;
	private static String pricingModel;
	public static String Caller;
	private static String TAG ="Subscribe";
	
	public static void requestForSubscriber(String id,String title,String price, String pm, String tag, String type,Activity act,LinearLayout llayout,String caller){
		if (Constant.DEBUG)	Log.d("Subscribe", "id :" + id+", title :" + title+", price :" + price+", pricingModel :" + pm +", tag :" + tag+", type :" + type);
		selectedId = id;
		selectedType = type;
		pricingModel = pm;
		Caller = caller;
		if(pricingModel.equalsIgnoreCase("PPV")){
			showSubscribeUnSubscribeDialog(tag, title,"", price,type, caller, act, llayout); //price of service is not there
		}else{
			showSubscribeUnSubscribeDialog(tag, title, price,"",type, caller, act, llayout); //price of event is not there
		}
	}
	
	/**
	 * @param subscribeOption(tag)
	 * @param mPreviousSelectedSevicePrice 
	 */
	// tag,channelName,channelPrice,eventPrice,activity,LinearLayout
	public static void showSubscribeUnSubscribeDialog(final String subscribeOption,final String titleName, final String ServicePrice,final String EventPrice, final String type,final String caller,final Activity act,LinearLayout llayout) {
		if (Constant.DEBUG)	Log.d("Subscribe", " subscribeOption :" + subscribeOption+", titleName :" + titleName+", ServicePrice :" + ServicePrice+", type :" + type);
		if (Constant.DEBUG)	Log.d("Subscribe", " Caller :" + Caller+", EventPrice :" + EventPrice);
		mActivity = act;
		mContainer = llayout;
		try {
			if(mActivity != null){
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(mContainer != null){
							OverLay overlay = new OverLay(mActivity);
							String message = mActivity.getResources().getString(R.string.SUBSCRIBE_MESSAGE);
							final String title = subscribeOption;
							String value = title.toLowerCase().trim();
							if (Utils.checkNullAndEmpty(EventPrice) && type.equalsIgnoreCase("event") && subscribeOption != null && subscribeOption.equalsIgnoreCase("subscribe")) {
								message = mActivity.getResources().getString(R.string.SUBSCRIBE_MESSAGE)+value+" "+ titleName + " for Rs."+EventPrice+"?";
							}else if(type.equalsIgnoreCase("event") && !subscribeOption.equalsIgnoreCase("subscribe")){
								message = mActivity.getResources().getString(R.string.SUBSCRIBE_MESSAGE)+value+" "+titleName+"?";
							}
							
							if (Utils.checkNullAndEmpty(ServicePrice) && type.equalsIgnoreCase("service") && subscribeOption != null &&  subscribeOption.equalsIgnoreCase("subscribe")) {
								message = mActivity.getResources().getString(R.string.SUBSCRIBE_MESSAGE)+value+" "+titleName+" for Rs."+ServicePrice+"?";
							}else if(type.equalsIgnoreCase("service") && !subscribeOption.equalsIgnoreCase("subscribe")){
								message = mActivity.getResources().getString(R.string.SUBSCRIBE_MESSAGE)+value+" "+titleName+"?";
							}
							
							if (Utils.checkNullAndEmpty(ServicePrice) && type.equalsIgnoreCase("package") && subscribeOption != null &&  subscribeOption.equalsIgnoreCase("subscribe")) {
								message = mActivity.getResources().getString(R.string.SUBSCRIBE_MESSAGE)+value+" "+titleName+" for Rs."+ServicePrice+"?";
							}
							final Dialog dialog = overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_CONFIRMATION, title, message, null,null,DataStorage.isShowSelected());
							if(dialog != null){
								Button okBtn = (Button) dialog.findViewById(R.id.okButton);
								ImageView alertLogo = (ImageView) dialog.findViewById(R.id.alertlogo);
								if(alertLogo != null){
									alertLogo.setVisibility(View.GONE);
								}
								if(okBtn != null){
									okBtn.setText("YES");
									okBtn.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View arg0) {
											if(dialog != null && dialog.isShowing()){
												dialog.dismiss();
											}
											if(title.equalsIgnoreCase("Subscribe")){
												showPasswordDialog(title,caller,act);
												
											}else if(title.trim().equalsIgnoreCase("UnSubscribe")){
												showPasswordDialog(title,caller,act);
											}
										}
									});
								}
								Button cancelBtn = (Button) dialog.findViewById(R.id.overlayCancelButton);
								if(cancelBtn != null){
									cancelBtn.setVisibility(View.VISIBLE);
									cancelBtn.setText("CANCEL");
									cancelBtn.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View arg0) {
											if(dialog != null && dialog.isShowing()){
												dialog.dismiss();
											}
										}
									});
								}
								dialog.show();
							}
						}
					}
				});
			}
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	
	private static void showPasswordDialog(final String header,final String caller,final Activity act){
		if(mActivity != null){
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					OverLay overlay = new OverLay(mActivity);
					final Dialog dialog = overlay.getOverLay(ScreenStyles.OVERLAY_TYPE_GETVALUE,header, "", null, null,isLukupSelected);
					if(dialog != null){
						dialog.show();
						Button cancelBtn = (Button)dialog.findViewById(R.id.overlayCancelButton);
						if(cancelBtn != null){
							cancelBtn.setText("CANCEL");
							cancelBtn.setOnClickListener(new  OverlayCancelListener(dialog));
						}
						if (Constant.DEBUG)	Log.d("Subscribe", " Caller :" + Caller);
						Button sendBtn = (Button) dialog.findViewById(R.id.overlayOkButton);
						final EditText textfield = (EditText) dialog.findViewById(R.id.keyValue);
						textfield.setHint(mActivity.getResources().getString(R.string.ENTER_PASSWORD));
						textfield.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
						if(sendBtn != null){
							sendBtn.setText("OK");
							sendBtn.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									String textdata = null;
									if(textfield != null)
									{
										textdata = textfield.getText().toString().trim();
										if(textdata == null || textdata.equalsIgnoreCase("")){
											HelpText.showHelpTextDialog(mActivity,mActivity.getResources().getString(R.string.EMPTY_INPUT), 2000);
											return;
										}
										if(header.trim().equalsIgnoreCase("Subscribe") || header.trim().equalsIgnoreCase("UnSubscribe")){
											if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
												ArrayList<HashMap<String,String>> dispatchHashMap  = new ArrayList<HashMap<String,String>>();
												HashMap<String, String> list = new HashMap<String, String>();
												list.put("type",selectedType);
												list.put("id",selectedId);
												list.put("state", header);
												list.put("password",textdata);
												list.put("consumer", "TV");
												list.put("network",Layout.mDataAccess.getConnectionType());
												list.put("caller", caller);	// according to Class
												list.put("called", "startService");
												dispatchHashMap.add(list);
												String method = "com.port.apps.epg.Attributes.Subscriptions";
												new AsyncDispatchMethod(method, dispatchHashMap,true,act).execute();
											}else{ 
												String Url = Constant.SUBSCRIBE+selectedId+"&type="+header+"&subscriberid="+Layout.mDataAccess.getSubscriberID()+"&pricingmodel="+pricingModel+"&adminPassword="+textdata+"&profile="+Layout.mDataAccess.getCurrentUserId();
												new Subscribe().new DataFetch(Url, caller).execute();
											}
											if(dialog != null && dialog.isShowing()){
												dialog.dismiss();
											}
												
										}
									}
								}
							});
						}
					}
				}
			});
		}
	}
	
	
	public class DataFetch extends AsyncTask<String, String, JSONObject>{

    	String dataUrl;
    	String c;
    	
    	public DataFetch(String url, String caller) {
    		dataUrl = url;
    		c = caller;
    	}
    	
		protected void onPreExecute() {
			Layout.progressDialog = new ProgressDialog(mActivity,R.style.MyTheme);
			Layout.progressDialog.setCancelable(true);
			Layout.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			Layout.progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
			Layout.progressDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			
			Subscribe.getJSONData(dataUrl,c);
			return null;
		}
	}
	
	
	public static void getJSONData(String urls, String caller) {
		InputStream is = null;
		String response = "";
		DataInputStream dataIn = null;
		BufferedReader br = null;
		if(CommonUtil.isNetworkConnected(mActivity)){
			try {
				if (Constant.DEBUG)
					Log.d(TAG, "Subscription url  : " + urls);
				URL url = new URL(urls.trim());
				URLConnection connection = url.openConnection();
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");

				// get the response from the server and store it in result
				dataIn = new DataInputStream(connection.getInputStream());

				if (dataIn != null) {
					br = new BufferedReader(new InputStreamReader(dataIn));
					String inputLine;
					while ((inputLine = br.readLine()) != null) {
						response += inputLine;
					}

					if (Constant.DEBUG)
						Log.d(TAG, "final response from subscription request is  : " + response);

					br.close();
					dataIn.close();
				}
//				String userAgent = System.getProperty("http.agent");
//				DefaultHttpClient httpClient = new DefaultHttpClient();
//				httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
//						userAgent);
//				HttpPost httpPost = new HttpPost(url);
//	
//				HttpResponse httpResponse = httpClient.execute(httpPost);
//				HttpEntity httpEntity = httpResponse.getEntity();
//				is = httpEntity.getContent();
//				BufferedReader reader = new BufferedReader(new InputStreamReader(
//						is, "iso-8859-1"), 8);
//				StringBuilder sb = new StringBuilder();
//				String line = null;
//				while ((line = reader.readLine()) != null) {
//					sb.append(line + "\n");
//				}
//				is.close();
//				response = sb.toString();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			Intent intent = new Intent();
			intent.putExtra("Params", response);
			intent.putExtra("Handler", "com.port.apps.epg.Attributes.Subscriptions");
			intent.setAction(caller);
			mActivity.getApplicationContext().sendBroadcast(intent);
		}else{
			HelpText.showHelpTextDialog(mActivity, "Network not connected. Try again.", 5000);
		}
		if(Layout.progressDialog.isShowing()){
			Layout.progressDialog.cancel();
		}
	}
	
}
