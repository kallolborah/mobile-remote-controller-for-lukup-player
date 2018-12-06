package com.player.webservices;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.player.Layout;
import com.player.util.CommonUtil;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.util.SystemLog;
import com.player.widget.HelpText;

public class EPG extends IntentService{

	private static final String TAG = "EPG";
	private String handler;
	private String bouquetname;
	private String serviceid;
	private String subscriberid;
	private SharedPreferences setupDetails;
	
	public EPG() {
		super("EPG");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		setupDetails = getApplication().getSharedPreferences("SetupDetail", MODE_WORLD_WRITEABLE);
		
		if(Constant.DEBUG) Log.d(TAG, "========handler Found==========" + intent.getDataString());
	
		if (intent.hasExtra("handler")) {
			handler = intent.getStringExtra("handler");
			if(Constant.DEBUG) Log.d(TAG, "========handler Found==========" + handler);
		}
		if (intent.hasExtra("bouquetname")) {
			bouquetname = intent.getStringExtra("bouquetname");
			if(Constant.DEBUG) Log.d(TAG, "========bouquetname Found==========" + bouquetname);
		}
		
		if (intent.hasExtra("serviceid")) {
			serviceid = intent.getStringExtra("serviceid");
			if(Constant.DEBUG) Log.d(TAG, "========serviceid Found==========" + serviceid);
		}
		if(intent.hasExtra("subscriberid")){
			subscriberid = intent.getStringExtra("subscriberid");
		}
		
		if (handler.equalsIgnoreCase("com.player.webservices.fetchBouquet")) {
			fetchBouquet();
		}
		if (handler.equalsIgnoreCase("com.player.webservices.fetchServices")) {
			fetchServices(bouquetname, subscriberid);
		}
		if (handler.equalsIgnoreCase("com.player.webservices.fetchPrograms")) {
			fetchPrograms(serviceid, subscriberid);
		}
		if (handler.equalsIgnoreCase("com.player.webservices.getAccountInfo")) {
			getAccountInfo(subscriberid);
		}
		
	}
	
	private void getAccountInfo(String subscriberid){
		String stringURL = Constant.ISCHANNELSUBSCRIBE_URL+subscriberid;
		
		String balance = "";
		String response = "";
		DataInputStream dataIn = null;
		BufferedReader br = null;
		if(CommonUtil.isNetworkConnected(getApplicationContext())){
			try {
				if (Constant.DEBUG)
					Log.d(TAG, "Bouquet url  : " + stringURL);
				URL url = new URL(stringURL.trim());
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
						Log.d(TAG, "final response fetchBouquet is  : " + response);

					br.close();
					dataIn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_WEBSERVICE, errors.toString(), e.getMessage());

				try {
					if (br != null) {
						br.close();
					}
					if (dataIn != null) {
						dataIn.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			try {
				JSONObject resp = new JSONObject(response);
				if(resp.has("data")){
					JSONObject data = resp.getJSONObject("data");
					if(data.has("channelIdList")){
						if(data.getJSONArray("channelIdList").length()>0){
							DataStorage.setchannelidlist(data.getJSONArray("channelIdList"));
						}
					}
					if(data.has("eventIdList")){
						if(data.getJSONArray("eventIdList").length()>0){
							DataStorage.seteventidlist(data.getJSONArray("eventIdList"));
						}
					}
					if(data.has("balance")){
						balance = data.getString("balance");
					}
				}
				if(Layout.progressDialog.isShowing()){
					Layout.progressDialog.cancel();
				}	
				
				JSONObject params = new JSONObject();
				params.put("balance", balance);
				params.put("Subscriber ID", subscriberid);
				
				Intent intent =  new Intent();
				intent.putExtra("Params", String.valueOf(params));
				intent.putExtra("Handler", "com.port.apps.settings.Settings.getAccountInfo");
				intent.setAction("com.player.apps.Setup");
				sendBroadcast(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			if(Layout.progressDialog.isShowing()){
				Layout.progressDialog.cancel();
			}	
			
			Toast.makeText(getApplicationContext(), "Network not connected. Try again.", 5000).show();
		}
	}

	public  void fetchBouquet() {
		String stringURL = Constant.FETCHBOUQUET_URL+setupDetails.getString("distributorid", "");
		
		String response = "";
		DataInputStream dataIn = null;
		BufferedReader br = null;
		if(CommonUtil.isNetworkConnected(getApplicationContext())){
			try {
				if (Constant.DEBUG)
					Log.d(TAG, "Bouquet url  : " + stringURL);
				URL url = new URL(stringURL.trim());
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
						Log.d(TAG, "final response fetchBouquet is  : " + response);

					br.close();
					dataIn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_WEBSERVICE, errors.toString(), e.getMessage());

				try {
					if (br != null) {
						br.close();
					}
					if (dataIn != null) {
						dataIn.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if(Layout.progressDialog.isShowing()){
				Layout.progressDialog.cancel();
			}	
			
			Intent intent =  new Intent();
			intent.putExtra("Params", response);
			intent.putExtra("Handler", "com.port.apps.epg.Guide.sendBouquetList");
			intent.setAction("com.player.apps.Guide");
			sendBroadcast(intent);
		}else{
			if(Layout.progressDialog.isShowing()){
				Layout.progressDialog.cancel();
			}	
			
			Toast.makeText(getApplicationContext(), "Network not connected. Try again.", 5000).show();
		}
	}
	
	
	public  void fetchServices(String BouquetName, String subscriberid){
		
		String stringURL = Constant.FETCHBOUQUET_URL+setupDetails.getString("distributorid", "")+"&subscriberid="+subscriberid;
		
		String response = "";
		DataInputStream dataIn = null;
		BufferedReader br = null;
		if(CommonUtil.isNetworkConnected(getApplicationContext())){
			try {
				if (Constant.DEBUG)
					Log.d(TAG, "Services url  : " + stringURL);
				URL url = new URL(stringURL.trim());
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
						if(Constant.DEBUG) Log.d(TAG, "final response  fetchServices is  : " + response);

					br.close();
					dataIn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_WEBSERVICE, errors.toString(), e.getMessage());

				try {
					if (br != null) {
						br.close();
					}
					if (dataIn != null) {
						dataIn.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if(Layout.progressDialog.isShowing()){
				Layout.progressDialog.cancel();
			}	
			
			Intent intent =  new Intent();
			intent.putExtra("Params", response);
			intent.putExtra("Handler", "com.port.apps.epg.Guide.sendServiceList");
			intent.setAction("com.player.apps.Guide");
			sendBroadcast(intent);
		}else{
			if(Layout.progressDialog.isShowing()){
				Layout.progressDialog.cancel();
			}	
			
			Toast.makeText(getApplicationContext(), "Network not connected. Try again.", 5000).show();
		}
	}
	

	public void  fetchPrograms(String ServiceID, String subscriberid){
		
		String stringURL = Constant.FETCHEVENT_URL+ServiceID+"&distributorid="+setupDetails.getString("distributorid", "")+"&subscriberid="+subscriberid;
		
		String response = "";
		DataInputStream dataIn = null;
		BufferedReader br = null;
		if(CommonUtil.isNetworkConnected(getApplicationContext())){
			try {
				if (Constant.DEBUG)
					Log.d(TAG, "Event url  : " + stringURL);
				URL url = new URL(stringURL.trim());
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
						Log.d(TAG, "final response from Event url is  : " + response);

					br.close();
					dataIn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_WEBSERVICE, errors.toString(), e.getMessage());

				try {
					if (br != null) {
						br.close();
					}
					if (dataIn != null) {
						dataIn.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if(Layout.progressDialog.isShowing()){
				Layout.progressDialog.cancel();
			}	
			
			Intent intent =  new Intent();
			intent.putExtra("Params", response);
			intent.putExtra("Handler", "com.port.apps.epg.Guide.sendEventList");
			intent.setAction("com.player.apps.Guide");
			sendBroadcast(intent);
		}else{
			if(Layout.progressDialog.isShowing()){
				Layout.progressDialog.cancel();
			}	
			
			Toast.makeText(getApplicationContext(), "Network not connected. Try again.", 5000).show();
		}
	}
	
	
}
