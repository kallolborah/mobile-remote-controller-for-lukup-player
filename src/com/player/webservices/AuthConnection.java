package com.player.webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONException;
import org.json.JSONObject;

import com.player.util.Constant;

import android.os.Build;
import android.util.Log;

public class AuthConnection {
	JSONObject jObj = null;
	String json = null;

	public AuthConnection() {

	}

	public JSONObject getJSONData(String url) {
		InputStream is = null;
		try {if(Constant.DEBUG)  Log.d("AuthConnection", "DoingInBackgroundClass getJSONData");
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
			if(Constant.DEBUG)  Log.d("AuthConnection", "DoingInBackgroundClass final response" +sb.toString());
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
		return jObj;
	}

	

//	public JSONObject getJSONGettData(String url) {
//		InputStream is = null;
//		try {
//			String userAgent = System.getProperty("http.agent");
//			DefaultHttpClient httpClient = new DefaultHttpClient();
//			httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT,userAgent);
//			HttpGet httpGet = new HttpGet(url);
//
//			HttpResponse httpResponse = httpClient.execute(httpGet);
//			HttpEntity httpEntity = httpResponse.getEntity();
//			is = httpEntity.getContent();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(
//					is, "iso-8859-1"), 8);
//			StringBuilder sb = new StringBuilder();
//			String line = null;
//			while ((line = reader.readLine()) != null) {
//				sb.append(line + "\n");
//			}
//			is.close();
//			json = sb.toString();
//			jObj = new JSONObject(json);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//
//		} catch (JSONException e) {
//			// Log.e("JSON Parser", "Error parsing data " + e.toString());
//		}
//		return jObj;
//	}
	
	public static JSONObject getSearchData(String url,String keyword) {
		InputStream inputStream = null;
		JSONObject jsonData = null;
        String response = "";
        String json = "";
		
		try{
			 HttpClient httpclient = new DefaultHttpClient();
	         HttpPost httpPost = new HttpPost(url);
	         JSONObject jsonObject = new JSONObject();
	
//	         {"keyword":"Grilling Chicken","category":"all","devicetype":"all","network":"all","limit":0}
	         
	         jsonObject.accumulate("keyword", keyword);
	         jsonObject.accumulate("category", "all");
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
				 Log.d("ConnectClass", "final response of getSearchData is  : "+response);
				 
	             bufferedReader.close();
	         }
	         inputStream.close();
	         if (response != null && !(response.trim().equals(""))) {
	        	 jsonData = new JSONObject(response);
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
         
		return jsonData;
	}

}
