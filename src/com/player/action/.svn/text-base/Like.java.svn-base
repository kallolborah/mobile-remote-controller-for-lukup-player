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

import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Facebook.DialogListener;
import com.player.R;
import com.player.Layout;
import com.player.service.CacheDockData;
import com.player.util.Constant;
import com.player.util.SystemLog;
import com.player.util.Utils;


/**
 * @author jeetendra
 *
 */
public class Like {
	
	private static String TAG = "Like";
	
	private static ArrayList<HashMap<String, String>> dispatchHashMap;
	
	private static final String[] PERMISSIONS = new String[] {"publish_stream", "read_stream", "offline_access"};
	private static final String APP_ID = "210799812304634";
	private Facebook mFacebook;
	private Activity activity;
	private String name;
	private String url;
	private String desc;
	private String imagePath;
	private String pricingModel;
		
	public static void requestForLike(String id,String tag,String type,String caller){
		dispatchHashMap  = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> list = new HashMap<String, String>();
		list.put("id", id);
		list.put("type", type);
		list.put("state", tag);
		list.put("consumer", "TV");
		list.put("network",Layout.mDataAccess.getConnectionType());
		list.put("caller", caller);	// according to Class
		list.put("called", "startService");
		dispatchHashMap.add(list);
		String method = "com.port.apps.epg.Attributes.Like";
		new AsyncDispatchMethod(method, dispatchHashMap,false).execute();
		CacheDockData.favouriteEventList.clear();
	}
	
//	public void FaceBookLogin(Activity activity, String title, String desc, String imagePath, String pricingModel, String url){
////		ShareLinkContent content = new ShareLinkContent.Builder().setContentUrl(Uri.parse("https://developers.facebook.com")).build();
////		SharePhoto photo = new SharePhoto.Builder().setBitmap(imagePath).build();
////		content.addPhoto(photo).build();
//		
//		if(ShareDialog.canShow(ShareLinkContent.class)){
//			ShareLinkContent linkContent = new ShareLinkContent.Builder()
//				.setContentTitle(title)
//				.setBitmap(imagePath)
//				.setContentDescription(desc)
//				.setContentUrl(Uri.parse(url))
//				.build();
//			shareDialog.show(linkContent);
//		}
//	}
		
//	public static void responseOfLike(String screenId,final JSONObject jsonData){
//		if(jsonData != null){
//			try {
//				String result = Utils.getDataFromJSON(jsonData, "result");
//				if(Utils.checkNullAndEmpty(result)){
//					if(result.equalsIgnoreCase("success")){
//						String isLikeorUnlike = Utils.getDataFromJSON(jsonData, "type");
//					}
//				}
//			}catch (Exception e) {
//				e.printStackTrace();
//				StringWriter errors = new StringWriter();
//				e.printStackTrace(new PrintWriter(errors));
//				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
//			}
//		}
//	}
//	
//	public void FaceBookLogin(Activity activity, String title, String desc, String imagePath, String pricingModel, String url){
//		this.activity = activity;
//		this.name = title;
//		this.desc = desc;
//		this.imagePath = imagePath;
//		this.url = url;
//		this.pricingModel = pricingModel;
//		if(Constant.DEBUG) Log.d("FbLoginDialogListener()", "Logging into facebook " + "");
//		mFacebook = new Facebook(APP_ID);
//        SessionStore.restore(mFacebook, activity);
//
//        if (mFacebook.isSessionValid()) {
//			String name = SessionStore.getName(activity);
//			name = (name.equals("")) ? "Unknown" : name;
//			postToFacebook(title, desc, imagePath, url, pricingModel);
//		}else{
//			onFacebookClick();
//		}
//	}
//	
//	
//	private void postToFacebook(String title,String desc,String imagePath, String url, String pricingModel) {	
//		
//		Layout.progressDialog = new ProgressDialog(activity,R.style.MyTheme);
//		Layout.progressDialog.setCancelable(true);
//		Layout.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		Layout.progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
//		Layout.progressDialog.show();
//		
//		AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(mFacebook);
//		Bundle params = new Bundle();
//		if(pricingModel.equalsIgnoreCase("PPC")){
//			params.putString("name", title);
//			params.putString("caption", "www.lukup.com");
//			params.putString("description", desc);
//			params.putString("picture", imagePath);
//		}else{
//			String infoLink = url;
//			params.putString("name", title);
//			params.putString("caption", "www.lukup.com");
//			params.putString("link", infoLink);
//			params.putString("description", desc);
//			params.putString("picture", imagePath);
//		}
//		mAsyncFbRunner.request("me/feed", params, "POST", new WallPostListener());
//	}
//
//	private final class WallPostListener extends BaseRequestListener {
//        public void onComplete(final String response) {
//        	activity.runOnUiThread(new Runnable() {
//				@Override
//        		public void run() {
//					if(Layout.progressDialog.isShowing()){
//						Layout.progressDialog.dismiss();
//					}
//        			settingToast("You liked "+ name);
//        		}
//        	});
//        }
//    }
//    
//    private void onFacebookClick() {
//		if (!mFacebook.isSessionValid()) {
//			mFacebook.authorize(activity, PERMISSIONS, -1, new FbLoginDialogListener());
//		}
//	}
//    
//    private final class FbLoginDialogListener implements DialogListener {
//        public void onComplete(Bundle values) {
//            SessionStore.save(mFacebook, activity);
//            if(Constant.DEBUG) Log.d("FbLoginDialogListener()", "onComplete()");
//            getFbName();
//        }
//
//        public void onFacebookError(FacebookError error) {
//        	if(Constant.DEBUG) Log.d("FbLoginDialogListener()", "onFacebookError().Facebook connection failed");
//        }
//        
//        public void onError(DialogError error) {
//        	if(Constant.DEBUG) Log.d("FbLoginDialogListener()", "onError().Facebook connection failed");
//        }
//
//        public void onCancel() {
//        }
//    }
//    
//	private void getFbName() {
//		Layout.progressDialog = new ProgressDialog(activity,R.style.MyTheme);
//		Layout.progressDialog.setCancelable(true);
//		Layout.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		Layout.progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
//		Layout.progressDialog.show();
//		
//		new Thread() {
//			@Override
//			public void run() {
//		        String name = "";
//		        int what = 1;
//		        
//		        try {
//		        	String me = mFacebook.request("me");
//		        	
//		        	JSONObject jsonObj = (JSONObject) new JSONTokener(me).nextValue();
//		        	name = jsonObj.getString("name");
//		        	what = 0;
//		        } catch (Exception ex) {
//		        	ex.printStackTrace();
//		        }
//		        
//		        mFbHandler.sendMessage(mFbHandler.obtainMessage(what, name));
//			}
//		}.start();
//		postToFacebook(name,desc,imagePath,url,pricingModel);
//	}
//	
//	private void fbLogout() {
//		Layout.progressDialog = new ProgressDialog(activity,R.style.MyTheme);
//		Layout.progressDialog.setCancelable(true);
//		Layout.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		Layout.progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
//		Layout.progressDialog.show();
//		
//		new Thread() {
//			@Override
//			public void run() {
//				SessionStore.clear(activity);
//		        	   
//				int what = 1;
//					
//		        try {
//		        	mFacebook.logout(activity);
//		        		 
//		        	what = 0;
//		        } catch (Exception ex) {
//		        	ex.printStackTrace();
//		        }
//		        	
//		        mHandler.sendMessage(mHandler.obtainMessage(what));
//			}
//		}.start();
//	}
//	
//	private Handler mFbHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			if(Layout.progressDialog.isShowing()){
//				Layout.progressDialog.dismiss();
//			}	
//			if (msg.what == 0) {
//				String username = (String) msg.obj;
//		        username = (username.equals("")) ? "No Name" : username;
//		        SessionStore.saveName(username, activity);
//		        if(Constant.DEBUG) Log.d(TAG,"  Connected to (" + username + ")");
//		        if(Constant.DEBUG) Log.d("mFbHandler()", "Connected to Facebook as " + username);
//			} else {
//				if(Constant.DEBUG) Log.d("mFbHandler()", "Connected to Facebook");
//			}
//		}
//	};
//	
//	private Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			if(Layout.progressDialog.isShowing()){
//				Layout.progressDialog.dismiss();
//			}	
//			if (msg.what == 1) {
//				if(Constant.DEBUG) Log.d(TAG,"  Disconnected");
//				Log.i("mHandler()", "Disconnected logout failed");
//			} else {
//				Log.i("mHandler()", "Disconnected from Facebook");
//			}
//		}
//	};
//	
//	private void settingToast(String Massegestirng){
////	 Intent toastintent =new Intent(activity, ToastDisplayer.class);
////	 toastintent.putExtra("Message", Massegestirng);
////	 startActivityForResult(toastintent, Toast_REQUESTCODE);
//	}
	
}
