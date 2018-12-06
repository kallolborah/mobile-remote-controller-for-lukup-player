package com.player.webservices;

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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.player.R;
import com.player.Layout;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.util.SystemLog;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class SearchService extends IntentService {

	private static final String TAG = "SearchService";
	private String handler;
	private String keyword;
	private String category;
	private String pricingModel;
	private String id;
	private String channelType;

	public SearchService() {
		super("SearchService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		if (intent.hasExtra("handler")) {
			handler = intent.getStringExtra("handler");
		}
		if (intent.hasExtra("keyword")) {
			keyword = intent.getStringExtra("keyword");
		}
		if (intent.hasExtra("pricingmodel")) {
			pricingModel = intent.getStringExtra("pricingmodel");
		}
		if (intent.hasExtra("channeltype")) {
			channelType = intent.getStringExtra("channeltype");
		}
		if (intent.hasExtra("id")) {
			id = intent.getStringExtra("id");
		}

		if (handler.equalsIgnoreCase("com.port.apps.search.Search.getSearchData")) {
			try {
				getSearchData(category, keyword);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (handler.equalsIgnoreCase("com.port.apps.search.Search.getEventStatus")) {
			getEventStatus(id, pricingModel, channelType);
		}
	}

	private void getEventStatus(String contentID, String pricingmodel,
			String channeltype) {
		JSONObject jsonData = null;
		String urlstring = Constant.ISSUBSCRIBE_URL;
		urlstring += contentID + "&userid=" + Layout.mDataAccess.getCurrentUserId()
				+ "&pricingModel=" + pricingmodel;

		String response = "";
		DataInputStream dataIn = null;
		BufferedReader br = null;

		try {
			URL url = new URL(urlstring.trim());
			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
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

				if (Constant.DEBUG)
					Log.d(TAG, "Is Event Subscribed RESPONSE(Data) : " + response);
				br.close();
				dataIn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
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
		Intent intent = new Intent();
		intent.putExtra("Params", response);
		intent.putExtra("Handler", "com.port.apps.search.Search.getEventStatus");
		intent.setAction("com.player.apps.Search");
		sendBroadcast(intent);
		
//		 if (response != null && !(response.trim().equals(""))) {
//			 try {
//					jsonData = new JSONObject(response);
//					JSONObject resp = new JSONObject();//
//					Intent intent = new Intent();
//					intent.putExtra("Params", jsonData.toString());
//					intent.putExtra("Handler", "com.port.apps.search.Search.getEventStatus");
//					intent.setAction("com.player.apps.Search");
//					sendBroadcast(intent);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		 }else{
//			 try {
//					jsonData = new JSONObject(response);
//					JSONObject resp = new JSONObject();//
//					Intent intent = new Intent();
//					intent.putExtra("Params", "false");
//					intent.putExtra("Handler", "com.port.apps.search.Search.getEventStatus");
//					intent.setAction("com.player.apps.Search");
//					sendBroadcast(intent);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//
//		 }
		
	}

	private void getChannelStatus(String channelid) {

		String urlstring = Constant.ISCHANNELSUBSCRIBE_URL;
		urlstring += DataStorage.getSubscriberId();

		String response = "";
		DataInputStream dataIn = null;
		BufferedReader br = null;

		try {
			URL url = new URL(urlstring.trim());
			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
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

				if (Constant.DEBUG)
					Log.d(TAG, "RECORDING RESPONSE(Data) : " + response);
				br.close();
				dataIn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
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
			JSONObject json = new JSONObject(response);
			JSONObject param = new JSONObject();
			JSONArray jsonArray = json.getJSONObject("data").getJSONArray(
					"packageIdList");
			boolean Channelfound = false;
			for (int i = 0; i <= jsonArray.length(); i++) {
				if (jsonArray.getString(i).equalsIgnoreCase(channelid)) {
					param.put("result", "success");
					Channelfound = true;
					break;
				}
			}
			if (Channelfound) {
				Intent intent = new Intent();
				intent.putExtra("params", param.toString());
				intent.putExtra("handler",
						"com.port.apps.search.Search.getChannelStatus");
				intent.setAction("com.player.apps.Search");
				sendBroadcast(intent);
			} else {
				param.put("result", "failure");
				Intent intent = new Intent();
				intent.putExtra("params", param.toString());
				intent.putExtra("handler",
						"com.port.apps.search.Search.getChannelStatus");
				intent.setAction("com.player.apps.Search");
				sendBroadcast(intent);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void getJSONData(String url) {
		InputStream is = null;
		JSONObject jObj = null;
		String json = null;
		try {
			String userAgent = System.getProperty("http.agent");
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
					userAgent);
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			jObj = new JSONObject(json);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();

		} catch (JSONException e) {
			// Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		// return jObj;
		if (jObj != null) {
			Intent intent = new Intent();
			intent.setAction("com.player.apps.Search");
			intent.putExtra("params", jObj.toString());
			intent.putExtra("Handler", handler);
			sendBroadcast(intent);
		}
	}

//===========================================================================================================
	// for getting Search list
	private void getSearchData(String category, String keywords)
			throws JSONException, InterruptedException {
		JSONArray jsonArray = new JSONArray();
		JSONObject resp = new JSONObject();
		JSONObject data = new JSONObject();
		try {
			jsonArray = getSearchPopularAndLatest(category, keywords);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
		}
		data.put("result", "success");
		data.put("type", "search");
		
		if (jsonArray != null && jsonArray.length() > 0) {
			data.put("eventdata", jsonArray);
		} else {
			if (Constant.DEBUG)
				Log.d(TAG, "map null ");
		}
		
//		resp.put("params", data);
		if(Constant.DEBUG)  Log.d(TAG , "handler  "+handler+"  final response sending to setup   "+resp.toString());
		Intent intent = new Intent();
		intent.setAction("com.player.apps.Search");
		intent.putExtra("Params", data.toString());
		intent.putExtra("Handler", "com.port.apps.search.Search.getSearchData");
		sendBroadcast(intent);
	}

	private JSONArray getSearchPopularAndLatest(String category, String keyword)
			throws JSONException {
		if (Constant.DEBUG)
			Log.d(TAG, "category: " + category + ", keyword: " + keyword);
		JSONArray jsonArray = new JSONArray();
		try {
			JSONObject jsonObject = getJsonSearchData(category,
					keyword);

			if (jsonObject != null) {
				JSONObject jsonData = jsonObject.getJSONObject("data");
				if (jsonData != null) {
					String result = jsonData.getString("result");
					if (result != null
							&& !(result.trim().equalsIgnoreCase("failure"))) {
						jsonArray = jsonData.getJSONArray("eventdata");
					} else {
						String message = this.getResources().getString(
								R.string.SEARCH_RESULT_FAILURE);
						if (jsonData.has("msg")) {
							message = jsonData.getString("msg");
						}
						if (Constant.DEBUG)
							Log.d(TAG, "result came failure : " + message);
					}
				}
			}
			return jsonArray;
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
		}
		return jsonArray;
	}
	
	
	public static JSONObject getJsonSearchData(String category, String keyword) {
		InputStream inputStream = null;
		JSONObject jsonData = null;
        String response = "";
        String json = "";
		
        String stringURL = Constant.SEARCH_URL;
		
		if(Constant.DEBUG)  Log.d("SearchWS", "getJsonSearchData final url is : "+stringURL);
        
		try{
			 HttpClient httpclient = new DefaultHttpClient();
	         HttpPost httpPost = new HttpPost(stringURL);
	         JSONObject jsonObject = new JSONObject();
	         
	         jsonObject.accumulate("keyword", keyword);
	         jsonObject.accumulate("category", category);
	         jsonObject.accumulate("devicetype", "mobile");
	         jsonObject.accumulate("network", "all");
	         jsonObject.accumulate("limit", 20);
			
	         json = jsonObject.toString();
	         StringEntity se = new StringEntity(json);
	         httpPost.setEntity(se);
	
	         httpPost.setHeader("Accept", "application/json");
	         httpPost.setHeader("Content-type", "application/json");
	         
	         HttpResponse httpResponse = httpclient.execute(httpPost);
	         inputStream = httpResponse.getEntity().getContent();
	
	         if (inputStream != null){
	        	 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	             String inputLine;
				 while ((inputLine = bufferedReader.readLine()) != null) {
					response += inputLine;
				 }
				 Log.d(TAG, "final response of getJsonSearchData is  : "+response);
				 
	             bufferedReader.close();
	         }
	         inputStream.close();
	         if (response != null && !(response.trim().equals(""))) {
	        	 jsonData = new JSONObject(response);
			 }
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
		}
         
		return jsonData;
	}
	
}
