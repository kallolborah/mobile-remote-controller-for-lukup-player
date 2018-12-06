package com.player.action;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.google.android.exoplayer.VideoSurfaceView;
import com.player.R;
import com.player.Layout;
import com.player.Player;
import com.player.apps.PlayBack;
import com.player.apps.TVRemote;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.widget.HelpText;

public class Media {
	
	private static String TAG = "Media";
	
	public static void playOnClient(String url, LinearLayout lLayout, Activity activity, String type, String starttime, String startdate, String duration){
		
		if(Constant.DEBUG)  Log.d(TAG , "Url to play " + url + " start time " + starttime + " start date " + startdate + " duration " + duration + " type " + type);
		try{
			if(!url.equalsIgnoreCase("")){
				String extension = url.trim().substring(url.trim().lastIndexOf("."));
				String base="";
				String protocol="";
				String chname="";
				String title="";
				String urltoplay="";
				String timestamp = "";
				int count=0;
				if(!extension.equalsIgnoreCase(".mp4")){
					StringTokenizer st = new StringTokenizer(url, "/");
					if(!type.equalsIgnoreCase("recorded")){
						while(st.hasMoreTokens()){
							if(count==0) protocol = st.nextToken();
							if(count==2) base = protocol+"//"+st.nextToken();
							if(count==3) chname = st.nextToken();
							if(count==4) title = st.nextToken();
							count++;
						}
					}else{
						while(st.hasMoreTokens()){
							if(count==0) protocol = st.nextToken();
							if(count==2) base = protocol+"//"+st.nextToken();
							if(count==3) chname = st.nextToken();
							if(count==4) timestamp = st.nextToken();
							if(count==5) title = st.nextToken();
							count++;
						}
						
					}
				}
				long StartTime=0;
				long CurrentTime=0;
				long eventduration=0;
				CurrentTime = System.currentTimeMillis();
				if(type.equalsIgnoreCase("live") && !starttime.equalsIgnoreCase("") && !startdate.equalsIgnoreCase("") && !duration.equalsIgnoreCase("")){
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
					Date date = formatter.parse(startdate+" "+starttime);
					StartTime = date.getTime();
					if(Constant.DEBUG)Log.e(TAG, "Start time "+StartTime);
					StringTokenizer tokens = new StringTokenizer(duration, ":");
					int h = 0;
					int min = 0;
					int sec = 0;
					if (tokens.hasMoreTokens()) {
						h = Integer.parseInt(tokens.nextToken());
					}
					if(tokens.hasMoreTokens()){
						min = Integer.parseInt(tokens.nextToken());
					}
					if(tokens.hasMoreTokens()){
						sec = Integer.parseInt(tokens.nextToken());
					}
					if(Constant.DEBUG)  Log.d(TAG, "Duration Hours: "+h+", Minute: "+min);
					eventduration = ((((h*60)+min)*60)+sec)*1000;
				}
				int x = new BigInteger(String.valueOf(CurrentTime)).compareTo(new BigInteger(String.valueOf(StartTime+eventduration)));
				if(type.equalsIgnoreCase("live")){
					if(Constant.DEBUG)  Log.d(TAG, "x : " + x + "Start time : "+ StartTime +", Current time : "+ CurrentTime + ", Duration :" + eventduration);
					if(CurrentTime!=0 && StartTime!=0 && eventduration!=0){
//						if((CurrentTime/1000) > ((StartTime/1000) +(eventduration/1000))){
						if(x==1){
							urltoplay = base + "/hls/" + chname + "/index.m3u8?starttime="+StartTime+"&stoptime="+(StartTime+eventduration);
							if(Constant.DEBUG)  Log.d(TAG , "Url to play " + urltoplay);
						}else{
							urltoplay = base + "/hls/" + chname + "/index.m3u8";
							if(Constant.DEBUG)  Log.d(TAG , "Url to play " + urltoplay);
						}
					}else{
						urltoplay = base + "/hls/" + chname + "/index.m3u8";
					}
				}else if(type.equalsIgnoreCase("vod")){ //vod
					if(extension.equalsIgnoreCase(".mp4")){
						urltoplay = url;
					}else{
						urltoplay = base + "/widevine/" +chname + "/" + title + ".wvm";
					}
				}else if(type.equalsIgnoreCase("recorded")){
					if(Player.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
						if(DataStorage.getDeviceType() != null && ((DataStorage.getDeviceType().equalsIgnoreCase("Lukup Player S")) ||  (DataStorage.getDeviceType().equalsIgnoreCase("Lukup Player X")))){
							urltoplay = base + "/hlsvod" + "/" + chname + "/" + timestamp + "/" + title + "/index.m3u8";
						}else{
							urltoplay = base + "/dashvod" + "/" + chname + "/" + timestamp + "/" + title + "/manifest.mpd";
						}
					}else{
						urltoplay = base + "/hlsvod" + "/" + chname + "/" + timestamp + "/" + title + "/index.m3u8";
					}
					if(Constant.DEBUG)  Log.d(TAG , "Going to play recorded content " + urltoplay);
				}else{
					urltoplay = url;
				}
//				String DRMUrl = urltoplay.trim().substring(urltoplay.trim().lastIndexOf("."));
				if(urltoplay.contains(".wvm")){								
					if(Constant.DEBUG)  Log.d(TAG , "Playing in Classic DRM===="  + urltoplay);
					Intent play = new Intent(activity, Play.class);
					play.putExtra("type", type);
					play.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					if(urltoplay != ""){
						play.putExtra("url", urltoplay);
					}
					activity.startActivity(play);														
				}else if(urltoplay.contains(".mp4") || urltoplay.contains(".m3u8")){	
					if(Constant.DEBUG)  Log.d(TAG , "Playing in Android media player ===="  + urltoplay);
					Intent play = new Intent(activity, Play.class);
					play.putExtra("type", type);
					play.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					if(urltoplay != null){
						play.putExtra("url", urltoplay);
					}
					activity.startActivity(play);
				}
			}
		}catch(Exception e){
			
		}
	}
	
	
	public static void playVideo(String serviceid, String id,String url, String type,HashMap<String,String> map,Activity mactivity, String caller){
		if(Constant.DEBUG)  Log.d("Info" ,"Pre.CurrentScreen: "+DataStorage.getCurrentScreen());
		if(Constant.DEBUG)  Log.d("Info" ,"id: "+id+", url: "+url+", Service type : "+ type);

		if(type.equalsIgnoreCase("live")){
			if(url.equalsIgnoreCase("")){
				if(Constant.DVB){
					String serviceId = "0";
					if(map.containsKey("serviceid")){
						serviceId = map.get("serviceid");
						DataStorage.setPlayingService(serviceId);
					}
					ArrayList<HashMap<String,String>> dispatchHashMap  = new ArrayList<HashMap<String,String>>();
					HashMap<String, String> list = new HashMap<String, String>();
					list.put("type", type);
					list.put("id", id);
					list.put("serviceid", serviceId);
					list.put("consumer", "TV");
					list.put("network",Layout.mDataAccess.getConnectionType());
					list.put("caller", caller);
					list.put("activity", caller);
					if(Constant.DEBUG)  Log.d("Info" , "Starting Port Play, Status: "+DataStorage.isRunningStatus());
					list.put("called", "startActivity");
					dispatchHashMap.add(list);
					new AsyncDispatchMethod("com.port.apps.epg.Play.PlayOn", dispatchHashMap,false).execute();
				}else{ //OTT
					Intent play = new Intent(Player.player, TVRemote.class);
					play.putExtra("ActivityName", caller);
					Player.player.startActivity(play);
				}
		}else{
			if(url != null && !url.equalsIgnoreCase("")){
				Intent play = new Intent(Player.player, PlayBack.class);
				play.putExtra("ActivityName", caller);
				play.putExtra("EventData", map);
				play.putExtra("Type", type);
				play.putExtra("EventId", id);
				play.putExtra("serviceid", serviceid);
//				play.putExtra("pricingmodel", pricingModel);
				if(url != null){
					play.putExtra("EventUrl", url);
				}
				Player.player.startActivity(play);
			}else{
				if(Constant.DEBUG)  Log.d("Info" ,"Url is Null ");
				}
			}
		}else{ //vod
			if(url != null && !url.equalsIgnoreCase("")){
				Intent play = new Intent(Player.player, PlayBack.class);
				play.putExtra("ActivityName", caller);
				play.putExtra("EventData", map);
				play.putExtra("Type", type);
				play.putExtra("EventId", id);
				play.putExtra("serviceid", serviceid);
//				play.putExtra("pricingmodel", pricingModel);
				if(url != null){
					play.putExtra("EventUrl", url);
				}
				play.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				Player.player.startActivity(play);
			}else{
				if(Constant.DEBUG)  Log.d("Info" ,"Url is Null ");
			}
			
		}
	}

}
