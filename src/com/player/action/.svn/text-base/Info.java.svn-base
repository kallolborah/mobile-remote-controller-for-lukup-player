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
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.exoplayer.VideoSurfaceView;
import com.player.R;
import com.player.Layout;
import com.player.Layout.AsyncDispatch;
import com.player.action.Play.DrmManagerImpl;
import com.player.apps.Guide;
import com.player.apps.PlayBack;
import com.player.apps.Search;
import com.player.apps.TVRemote;
import com.player.util.Constant;
import com.player.util.CustomLayout;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;
import com.player.util.Utils;
import com.player.widget.HelpText;


/**
 * @author abhijeet
 *
 */
public class Info {
	
	public static String Caller;
	private static String TAG = "Info";

	public static void requestForInfoById(String id,String caller){
		Caller = caller;
		ArrayList<HashMap<String,String>> dispatchHashMap  = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> list = new HashMap<String, String>();
		list.put("id", id);
		list.put("consumer", "TV");
		list.put("network",Layout.mDataAccess.getConnectionType());
		list.put("caller", caller);	// according to Class
		list.put("called", "startService");
		dispatchHashMap.add(list);
		String method = "com.port.apps.epg.Guide.sendEventInfo"; 
		new AsyncDispatchMethod(method, dispatchHashMap,false).execute();
	}
	
	public static void infoActionUpdate(String value){
		
	}
	
	public static void rInfoAction(final JSONObject jsonData,final LinearLayout lLayout,final Activity activity,final String caller){
		try {
			if(Constant.DEBUG)  Log.d("InfoAction", "rInfoAction() jsonData: "+jsonData);
			final HashMap<String,String> showMap = new HashMap<String, String>();
			if(jsonData != null){
				final String id = Utils.getDataFromJSON(jsonData, "id");
				final String servicetype = Utils.getDataFromJSON(jsonData, "servicetype");
				final String serviceid = Utils.getDataFromJSON(jsonData, "serviceid");
				final String url = Utils.getDataFromJSON(jsonData, "url");
				final String subscribe = Utils.getDataFromJSON(jsonData, "subscribe");
				final String lock = Utils.getDataFromJSON(jsonData, "lock");
				final String like = Utils.getDataFromJSON(jsonData, "like");
				final String price = Utils.getDataFromJSON(jsonData, "price");
				final String pricingModel = Utils.getDataFromJSON(jsonData, "pricingmodel");
				final String channelPrice = Utils.getDataFromJSON(jsonData, "channelPrice");
				final String movieName = Utils.getDataFromJSON(jsonData, "name");
				String artists = Utils.getDataFromJSON(jsonData, "actors");
				String views = Utils.getDataFromJSON(jsonData, "views");
				String releaseDate  = Utils.getDataFromJSON(jsonData, "releasedate");
				String rating = Utils.getDataFromJSON(jsonData, "rating");
				String image = Utils.getDataFromJSON(jsonData, "image");
				String singers = Utils.getDataFromJSON(jsonData, "singers");
				String director = Utils.getDataFromJSON(jsonData, "director");
				String production = Utils.getDataFromJSON(jsonData, "production");
				String description = Utils.getDataFromJSON(jsonData, "description");
				String producer = Utils.getDataFromJSON(jsonData, "producer");
				String musicdirector = Utils.getDataFromJSON(jsonData, "musicdirector");
				final String channelname = Utils.getDataFromJSON(jsonData, "channelname");
				showMap.put(ScreenStyles.LIST_KEY_ID, id);
				showMap.put(ScreenStyles.LIST_KEY_THUMB_URL, R.drawable.defaultimage+"");
				showMap.put(ScreenStyles.LIST_KEY_TITLE, movieName);
				showMap.put("servicetype", servicetype);
				showMap.put("serviceid", serviceid);
				showMap.put("subscribe", subscribe);
				showMap.put("urllink", url);
				showMap.put("lock", lock);
				showMap.put("pricingmodel", pricingModel);
				showMap.put("like", like);
				showMap.put("event", "true");

				showMap.put(ScreenStyles.LIST_KEY_PRICE, price);
				try {
					if(Constant.DEBUG)  Log.d("InfoAction", "showInfoPage() name: "+movieName+",  artists: "+artists);
//					DataStorage.setCurrentScreen(ScreenStyles.REMOTE_INFO_SCREEN);
					LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View v = inflater.inflate(R.layout.media_detail_page, null);
					if(v != null){
						DataStorage.setCurrentScreen("remoteinfo");
		//				DataStorage.setCurrentScreen(ScreenStyles.HOME_SELECTED_TYPE);
						lLayout.removeAllViews();
						
						Typeface fontBold = Typeface.createFromAsset(activity.getAssets(), "OpenSans-Regular.ttf");  
						TextView moviewNameView = (TextView) v.findViewById(R.id.detailmoviename);
						TextView movieViews = (TextView) v.findViewById(R.id.detailmovie_des);
						
						final Button button = (Button) v.findViewById(R.id.btn);
						if(servicetype.equalsIgnoreCase("vod")){
							if(subscribe.equalsIgnoreCase("true") || MoneyConverter(price) == 0){
								button.setText("Play");
							}else{
								button.setText("Subscribe");
							}
						}else{
							if(subscribe.equalsIgnoreCase("true") || MoneyConverter(price) == 0){
								button.setText("Play");
							}else{
								button.setText("Subscribe");
							}
						}

						button.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								String tag = button.getText().toString();
								if(Constant.DEBUG)  Log.d("button", "tag: "+tag);
								if(tag.equalsIgnoreCase("Play")){
									if(!lock.equalsIgnoreCase("true")){
										if(subscribe.equalsIgnoreCase("true") || MoneyConverter(price) == 0){
											if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
												Media.playVideo(serviceid, id, url,servicetype,showMap,activity,caller);
											}else{ //if not connected to the Lukup Player, play video here itself
												Media.playOnClient(url, lLayout, activity,servicetype,"","","");
											}
										}else{
											if (pricingModel.equalsIgnoreCase("PPV")) {
												HelpText.showHelpTextDialog(activity, activity.getResources().getString(R.string.SUBSCRIBER_MESSAGE1)+" "+ movieName +" "+
														activity.getResources().getString(R.string.SUBSCRIBER_MESSAGE2), 2000);
											}else if (pricingModel.equalsIgnoreCase("PPC")) {
												HelpText.showHelpTextDialog(activity, activity.getResources().getString(R.string.SUBSCRIBER_MESSAGE1)+" "+serviceid +" "+
														activity.getResources().getString(R.string.SUBSCRIBER_MESSAGE2), 2000);
											}
										}
									}else{
										HelpText.showHelpTextDialog(activity, activity.getResources().getString(R.string.LOCK_MESSAGE), 2000);
									}
								}else{
									if(pricingModel.equalsIgnoreCase("PPV")){
										if(Constant.DEBUG)  Log.d(TAG, "PPV subscription request : " + movieName + " price : " + price);
										Subscribe.requestForSubscriber(id,movieName,price,pricingModel,"subscribe","event",activity,lLayout, caller);
									}else{
										if(Constant.DEBUG)  Log.d(TAG, "PPC subscription request : " + channelname + " price : " + channelPrice);
										Subscribe.requestForSubscriber(serviceid,channelname,channelPrice,pricingModel,"subscribe","service",activity,lLayout, caller);
									}
								}
							}
						});

						if(moviewNameView != null && movieName != null){
							moviewNameView.setTypeface(fontBold); 
							if(movieName.length() >20){
								moviewNameView .setFocusable(true);
								moviewNameView.setSelected(true);
								moviewNameView.setHorizontallyScrolling(true);
								moviewNameView.setEllipsize(TruncateAt.END);
								moviewNameView.setEllipsize(TruncateAt.MARQUEE);
								moviewNameView.setText(movieName);
							}else{
								moviewNameView.setText(movieName);
							}
						}
						Typeface fontregular = Typeface.createFromAsset(activity.getAssets(), "OpenSans-Regular.ttf");  
						LinearLayout infodetail = (LinearLayout) v.findViewById(R.id.infodetail);
						LinearLayout ratingView = (LinearLayout) v.findViewById(R.id.detailmovie_star);
						ratingView.setOrientation(LinearLayout.HORIZONTAL);

						if(movieViews != null && views != null){
							movieViews.setVisibility(View.VISIBLE);
							movieViews.setTypeface(fontregular);
							movieViews.setText(views +" Views");
						}
						
						LayoutInflater infoinflater = activity.getLayoutInflater();

						if(infodetail != null ){
							infodetail.removeAllViews();
							if(releaseDate != null && !releaseDate.equalsIgnoreCase("")){
								View infoView = getInfoDetailView(releaseDate, infoinflater,ScreenStyles.INFO_RELEASEDATE);
								infodetail.addView(infoView);
							}
							if(artists != null && !artists.equalsIgnoreCase("")){
								View infoView = getInfoDetailView(artists, infoinflater,ScreenStyles.INFO_ACTORS);
								infodetail.addView(infoView);
							}
							if(director != null && !director.equalsIgnoreCase("")){
								View infoView = getInfoDetailView(director, infoinflater,ScreenStyles.INFO_DIRECTOR);
								infodetail.addView(infoView);
							}
							if(musicdirector != null && !musicdirector.equalsIgnoreCase("")){
								View infoView = getInfoDetailView(musicdirector, infoinflater,ScreenStyles.INFO_MUSIC_DIRECTOR);
								infodetail.addView(infoView);
							}
							if(singers != null && !singers.equalsIgnoreCase("")){
								View infoView = getInfoDetailView(singers, infoinflater,ScreenStyles.INFO_SINGERS);
								infodetail.addView(infoView);
							}
							if(producer != null && !producer.equalsIgnoreCase("")){
								View infoView = getInfoDetailView(producer, infoinflater,ScreenStyles.INFO_PRODUCER);
								infodetail.addView(infoView);
							}
							if(production != null && !production.equalsIgnoreCase("")){
								View infoView = getInfoDetailView(production, infoinflater,ScreenStyles.INFO_PRODUCTION);
								infodetail.addView(infoView);
							}
							if(description != null && !description.equalsIgnoreCase("")){
								View infoView = getInfoDetailView(description, infoinflater,ScreenStyles.INFO_DESCRIPTION);
								infodetail.addView(infoView);
							}
						}
						if(Constant.DEBUG)  Log.d("InfoAction", "lLayout.addView(v)");
						lLayout.addView(v);
					}
				}catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
	
	/**
	 * @param director
	 * @param infoinflater
	 * @param infoView
	 * @return
	 */
	private static View getInfoDetailView(String director,LayoutInflater infoinflater,String header) {
		if(Constant.DEBUG)  Log.d("InfoAction", "getInfoDetailView()");
		View infoView1 = infoinflater.inflate(R.layout.mediainfo_element, null);
		TextView header1 = (TextView) infoView1.findViewById(R.id.detailmovie_header);
		TextView detail1 = (TextView) infoView1.findViewById(R.id.detailmovie_detail);
		header1.setText(header);
		detail1.setText(director);
		return infoView1;
	}
	
	
	private static int MoneyConverter(String val){
		int cost = 0;
		if(!val.equalsIgnoreCase("")){
			cost = (int) Double.parseDouble(val);
		}
		return cost;
	}


}
