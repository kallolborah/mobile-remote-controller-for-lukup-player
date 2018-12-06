package com.player.webservices;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.player.Layout;
import com.player.Player;
import com.player.util.CommonUtil;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.util.SystemLog;

public class Cloudstorage extends IntentService{

	public static String TAG = "Cloudstorage";
	static String handler="";
	
	public Cloudstorage() {
		super("Cloudstorage");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		
		JSONObject jsonobj;
		if(intent.hasExtra("handler")){
			handler = intent.getStringExtra("handler");
		}	
		if(handler.equalsIgnoreCase("com.port.apps.epg.Plan.showRecording")){
			RecordingData();
		}else if(handler.equalsIgnoreCase("com.port.apps.epg.Plan.deleteRecording")){
			String eventId = intent.getStringExtra("item");
			String objectId = intent.getStringExtra("ObjectId");
			deleteRecording(objectId, eventId);
		}else if(handler.equalsIgnoreCase("com.port.apps.epg.Plan.setRecord")){
			String eventId = intent.getStringExtra("eventid");
			String serviceId = intent.getStringExtra("serviceid");
			String eventName = intent.getStringExtra("eventname");
			String eventTime = intent.getStringExtra("starttime");
			String endTime = intent.getStringExtra("stoptime");
			String subscriberid = intent.getStringExtra("subscriberid");
			String userID = intent.getStringExtra("userid");
			ScheduleRecording(eventId, serviceId, eventName, eventTime, endTime, subscriberid, userID);
		}else if(handler.equalsIgnoreCase("com.port.apps.epg.Plan.stopRecord")){
			String serviceId = intent.getStringExtra("serviceid");
			String eventName = intent.getStringExtra("eventname");
			String eventTime = intent.getStringExtra("starttime");
			String endTime = intent.getStringExtra("stoptime");
			String subscriberid = intent.getStringExtra("subscriberid");
			String eventId = intent.getStringExtra("eventid");
			cancelRecording(serviceId, eventName, eventTime, endTime, subscriberid, eventId);
		}
	}
	
	
	public void RecordingData(){
		String response = "";
		DataInputStream dataIn = null;
		String cloudurl= Constant.CloudURL+Layout.mDataAccess.getSubscriberID()+"&userid="+Layout.mDataAccess.getCurrentUserId();
		BufferedReader br = null;
		if(Constant.DEBUG)  Log.d(TAG, "RecordingData  "+cloudurl);
		if(CommonUtil.isNetworkConnected(getApplicationContext())){
			try {
				URL url = new URL(cloudurl.trim());
				URLConnection connection = url.openConnection();
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
				try {
					dataIn = new DataInputStream(connection.getInputStream());
				} catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
				}
				if (dataIn != null) {
					br = new BufferedReader(new InputStreamReader(dataIn));
					String inputLine;
					while ((inputLine = br.readLine()) != null) {
						response += inputLine;
					}
	
					if (Constant.DEBUG) Log.d(TAG, "RECORDING RESPONSE(Data) : " + response);
					br.close();
					dataIn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				try {
					if (br != null) { br.close();}
					if (dataIn != null) { dataIn.close();}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			//jsonObject;
			Intent intent = new Intent();
			intent.setAction("com.player.apps.Storage");
			intent.putExtra("Params", response);
			intent.putExtra("Handler",handler);
			sendBroadcast(intent);
		}else{
			if(Layout.progressDialog.isShowing()){
				Layout.progressDialog.cancel();
			}	
			
			Toast.makeText(getApplicationContext(), "Network not connected. Try again.", 5000).show();
		}
	}


	public void deleteRecording(String objId, String eventId){
		String stringURL = Constant.DELETE_RECORDING;
		
		String subscriberid = DataStorage.getSubscriberId();
		if(subscriberid != null && !(subscriberid.trim().equals(""))) {
			stringURL += subscriberid;
		}
		
		if(!DataStorage.getCurrentUserId().equalsIgnoreCase("")) {
			stringURL += "&userid="+DataStorage.getCurrentUserId();
		}
		
		if (objId != null && !(objId.trim().equals(""))) {
			stringURL += "&objectid=" + objId + "&eventid=" + eventId;
		}
		
		if (Constant.DEBUG) Log.d(TAG , "Final url : "+ stringURL);
		JSONObject jsonObject = null;
		String response = "";

		DataInputStream dataIn = null;
		BufferedReader br = null;

		try {
			URL url = new URL(stringURL.trim());
			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			// get the response from the server and store it in result

			try {
				dataIn = new DataInputStream(connection.getInputStream());
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_TRANSCODER, errors.toString(), e.getMessage());
			}
			if (dataIn != null) {
				br = new BufferedReader(new InputStreamReader(dataIn));
				String inputLine;
				while ((inputLine = br.readLine()) != null) {
					response += inputLine;
				}

				if (Constant.DEBUG) Log.d(TAG, "RECORDING RESPONSE(Delete) : " + response);

				if (response != null && !(response.trim().equals(""))) {
					jsonObject = new JSONObject(response);
				}
				br.close();
				dataIn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_TRANSCODER, errors.toString(), e.getMessage());
			try {
				if (br != null) { br.close();}
				if (dataIn != null) { dataIn.close();}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		//jsonObject;
		Intent intent = new Intent();
		intent.setAction("com.player.apps.Storage");
		intent.putExtra("Params", response);
		intent.putExtra("Handler",handler);
		sendBroadcast(intent);

	}

	
	public void ScheduleRecording(String eventId, String serviceId, String eventName, String eventTime, String endTime, String subscriberid, String userID){
		
		String stringURL = Constant.SCHEDULE_RECORD + "eventid=" + eventId + "&serviceid=" + serviceId + "&eventname=" + eventName.replaceAll("\\s", "") + "&starttime=" + eventTime + "&endtime=" + endTime + "&subscriberid=" + subscriberid + "&userid=" + userID; 
		if (Constant.DEBUG) Log.d(TAG , "Final url : "+ stringURL);
		JSONObject jsonObject = null;
		String response = "";

		DataInputStream dataIn = null;
		BufferedReader br = null;
		if(CommonUtil.isNetworkConnected(Player.getContext())){
			try {
				URL url = new URL(stringURL.trim());
				URLConnection connection = url.openConnection();
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
				// get the response from the server and store it in result

				try {
					dataIn = new DataInputStream(connection.getInputStream());
				} catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_TRANSCODER, errors.toString(), e.getMessage());
				}
				if (dataIn != null) {
					br = new BufferedReader(new InputStreamReader(dataIn));
					String inputLine;
					while ((inputLine = br.readLine()) != null) {
						response += inputLine;
					}

					if (Constant.DEBUG) Log.d(TAG, "RECORDING RESPONSE(Info) : " + response);

					if (response != null && !(response.trim().equals(""))) {
						jsonObject = new JSONObject(response);
					}
					br.close();
					dataIn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_TRANSCODER, errors.toString(), e.getMessage());
				try {
					if (br != null) { br.close();}
					if (dataIn != null) { dataIn.close();}
				} catch (IOException e1) {
					e1.printStackTrace();
			   }
			}
		}
		//jsonObject;
		Intent intent = new Intent();
		intent.setAction("com.player.apps.Guide");
		intent.putExtra("Params", response);
		intent.putExtra("Handler",handler);
		sendBroadcast(intent);
	}
	
	public void cancelRecording(String serviceId, String eventName, String eventTime, String endTime, String subscriberid, String eventid){
		
		String stringURL = Constant.SCHEDULE_RECORD + "method=remove&serviceid=" + serviceId + "&eventname=" + eventName.replaceAll("\\s", "") + "&starttime=" + eventTime + "&endtime=" + endTime + "&subscriberid=" + subscriberid;// + "&eventid=" + eventid; 
		if (Constant.DEBUG) Log.d(TAG , "Final url : "+ stringURL);
		JSONObject jsonObject = null;
		String response = "";

		DataInputStream dataIn = null;
		BufferedReader br = null;
		if(CommonUtil.isNetworkConnected(Player.getContext())){
			try {
				URL url = new URL(stringURL.trim());
				URLConnection connection = url.openConnection();
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
				// get the response from the server and store it in result

				try {
					dataIn = new DataInputStream(connection.getInputStream());
				} catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_TRANSCODER, errors.toString(), e.getMessage());
				}
				if (dataIn != null) {
					br = new BufferedReader(new InputStreamReader(dataIn));
					String inputLine;
					while ((inputLine = br.readLine()) != null) {
						response += inputLine;
					}

					if (Constant.DEBUG) Log.d(TAG, "RECORDING RESPONSE(Info) : " + response);

					if (response != null && !(response.trim().equals(""))) {
						jsonObject = new JSONObject(response);
					}
					br.close();
					dataIn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_TRANSCODER, errors.toString(), e.getMessage());
				try {
					if (br != null) { br.close();}
					if (dataIn != null) { dataIn.close();}
				} catch (IOException e1) {
					e1.printStackTrace();
			   }
			}
		}
		//jsonObject;
		Intent intent = new Intent();
		intent.setAction("com.player.apps.Guide");
		intent.putExtra("Params", response);
		intent.putExtra("Handler",handler);
		sendBroadcast(intent);
	}
}
