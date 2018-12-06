package com.player.util;

import java.io.StringWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import com.player.Layout;
import com.player.Player;
import com.player.action.AsyncDispatchMethod;

import android.os.Build;
import android.util.Log;

public class SystemLog {
	
	private static final String TAG = "SystemLog";
	
	public static final String TYPE_PLAYER	= "Player";
	
	/*
	1. Bluetooth       - All the errors related to bluetooth
	2. MediaPlayer     - All the errors related to playing content
	3. WiFi              - All the errors related to WiFi and  HotSpot
	4. A2dp             - All the errors related to a2dp
	5. Ethernet        - All the errors related to Ethernet
	6. HDMI            - All the errors related to HDMI
	7. CVBS            - All the errors related to CVBS
	8. Transcoder     - All the errors related to recording,uploading
	9. Updates         - Errors related to parse initialization and push notifications, App/Firmware updates
	10. Application   - All the common exceptions like Null pointer exception,Filenotfound exception etc.
	11. Webservices  - Errors related to web services calls
	12. IR                 - Errors related to IR
	13. USB             - Errors related to USB, External storage
	14. WiFiDisplay   - Errors related to WiFiDisplay
	15. DRM
	*/
	
	public static final String LOG_BT	= "Bluetooth";
	public static final String LOG_WIFI	= "Wifi";
	public static final String LOG_ETHERNET	= "Ethernet";
	public static final String LOG_IR	= "IR";
	public static final String LOG_PLAYBACK = "MediaPlayer";
	public static final String LOG_STORAGE = "USB";
	public static final String LOG_WEBSERVICE = "Webservices";
	public static final String LOG_A2DP = "A2dp";
	public static final String LOG_HDMI = "HDMI";
	public static final String LOG_CVBS = "CVBS";
	public static final String LOG_TRANSCODER = "Transcoder";
	public static final String LOG_UPDATES = "Updates";
	public static final String LOG_APPLICATION = "Application";
	public static final String LOG_WIFIDISPLAY = "WiFiDisplay";
	public static final String LOG_DRM = "DRM";
	
	private static InetAddress	 address;
	private static final int PORT = 514;
	private static DatagramPacket	packet;
	private static DatagramSocket	socket;

	
	public static void createErrorLogXml(String type, String category,String errorname,String msg){
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("<ErrorLog>");
			sb.append("<MacID>"+" "+"</MacID>");
			sb.append("<Model.No>"+Build.MODEL+" - "+type+"</Model.No>");
			sb.append("<Version>"+Build.VERSION.RELEASE +"</Version>");
			sb.append("<PlayerVersion>"+Constant.APP_VERSION +"</PlayerVersion>");
			String subscriberId = DataStorage.getSubscriberId();
			if (subscriberId != null && !subscriberId.equalsIgnoreCase("")) {
				sb.append("<SubscriberID>"+ subscriberId +"</SubscriberID>");
			}
			sb.append("<report>");
			
			sb.append("<category>"+category.toString()+"</category>");
			sb.append("<name>"+errorname.toString() +"</name>");
			sb.append("<message>"+msg.toString() +"</message>");
			sb.append("<date-time>"+getCurrentDateAndTime() +"</date-time>");
			
			sb.append("</report>");
			sb.append("</ErrorLog>");
			
			if(Constant.DEBUG) Log.d(TAG,"Error xml : "+sb.toString());
			
			String data = sb.toString();
			int length = data.length();
			if(Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")){
				sendLogsData(data, length);
			}
		}catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	private static void sendLogsData(final String data,final int length){
		try {
			ArrayList<HashMap<String,String>> dispatchHashMap  = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("consumer", "TV");
			list.put("network",Layout.mDataAccess.getConnectionType());
			list.put("data", data);
			list.put("length", length+"");
			list.put("caller", "com.player.util.SystemLog");
			list.put("called", "startService");
			dispatchHashMap.add(list);
			String method = "com.port.api.webservices.Logs.sendSystemLog";
			new AsyncDispatchMethod(method, dispatchHashMap,false).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Date getCurrentDateAndTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"));
		return calendar.getTime();
	}
	
}
