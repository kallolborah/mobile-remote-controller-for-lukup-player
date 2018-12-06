package com.player.action;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.player.R;
import com.player.Layout;
import com.player.Player;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.webservices.AuthConnection;
import com.player.widget.HelpText;
import com.player.widget.OverLay;

public class AuthDialog {

	private static Dialog dialog; 
	public static String TAG = "AuthDialog";
	
	public static void authDialog(final Activity context){
		
		OverLay overlay = new OverLay(context);
		final String title = context.getResources().getString(R.string.SIGNIN);
		dialog = overlay .getOverLay(ScreenStyles.OVERLAY_TYPE_GETCREDENTIAL, title, null,null,null,true);
		if(dialog != null){
			dialog.show();
			Button connectBtn =(Button)dialog.findViewById(R.id.connect);
			Button cancelBtn = (Button)dialog.findViewById(R.id.overlayCancelButton);
			TextView titleView = (TextView) dialog.findViewById(R.id.overlayTitle);
			if(titleView != null){
				titleView.setText(title);
			}
			final EditText textfield = (EditText) dialog.findViewById(R.id.username);
			final EditText passwordfield = (EditText) dialog.findViewById(R.id.password);
			passwordfield.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			if(textfield != null){
				textfield.setHint(context.getResources().getString(R.string.USERNAME));
				
			}
			if(passwordfield != null){
				passwordfield.setHint(context.getResources().getString(R.string.PASSWORD));
				
			}
			if(connectBtn != null){
				connectBtn.setText(context.getResources().getString(R.string.SUBMIT));
				connectBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if(textfield != null && passwordfield != null){
	//							String screenid = screenId;
							String username = textfield.getText().toString().trim();
							String pwd  = passwordfield.getText().toString().trim();
							if(username == null || username.equalsIgnoreCase("")){
								HelpText.showHelpTextDialog(context, context.getResources().getString(R.string.EMPTY_INPUT), 2000);
								return;
							}
							if(pwd == null || pwd.equalsIgnoreCase("")){
								HelpText.showHelpTextDialog(context,context.getResources().getString(R.string.EMPTY_INPUT), 2000);
								return;
							}
							
							if(dialog != null && dialog.isShowing()){
								String loginurl = Constant.LoginURL+username+"&pwd="+pwd;
								new DoingInBackgroundClass(loginurl, context).execute();
								//dialog.cancel();
							}
						}
					}
				});
			}
			if(cancelBtn != null){
				cancelBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(dialog != null && dialog.isShowing()){
							dialog.cancel();
						}
					}
				});
			}
		}
	}

	
	public static class DoingInBackgroundClass extends AsyncTask<String, String, JSONObject>{
		
		private String Url;
		private Activity mActivity;
		private ProgressDialog progressDialog ;
		
		public DoingInBackgroundClass(String urlData,Activity context) {
			if(Constant.DEBUG)  Log.d(TAG, "DoingInBackgroundClass url "+urlData);
			Url = urlData;
			mActivity =context;
			progressDialog =  new ProgressDialog(mActivity);
		}
		
		protected void onPreExecute() {
			progressDialog.setCancelable(false);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage("Please wait...");
			if(Constant.DEBUG)  Log.d(TAG, "DoingInBackgroundClass onPreExecute");

		}
		
		
		@Override
		protected JSONObject doInBackground(String... params) {
			
			AuthConnection connection = new AuthConnection();
			JSONObject json = connection.getJSONData(Url);
			if(Constant.DEBUG)  Log.d(TAG, "DoingInBackgroundClass doInBackground");

			return json;
			
		}
		
		protected void onPostExecute(JSONObject obj) {
			progressDialog.dismiss();
			super.onPostExecute(obj);
			try {
				if(Constant.DEBUG)  Log.d(TAG, "DoingInBackgroundClass onPostExecute obj" + obj.toString());
				String checkstatus = obj.getJSONObject("data").getString("result");
				if(Constant.DEBUG)  Log.d(TAG, "DoingInBackgroundClass onPostExecute sucess"+obj.has("userAssociatedSubscriberIds" + "Userid" +obj.getJSONObject("data").has("userid")) );
				if(checkstatus.equalsIgnoreCase("success")){
					if(Constant.DEBUG)  Log.d(TAG, "DoingInBackgroundClass onPostExecute sucess"+"SubscriberID "+obj.getJSONObject("data").getString("userAssociatedSubscriberIds") + "Userid" + obj.getJSONObject("data").getString("userid"));
					JSONArray js = obj.getJSONObject("data").getJSONArray("userAssociatedSubscriberIds");
					if(js!=null && js.length()!=0){
						DataStorage.setMobile(true);
						String subscriberid= (String) js.get(0);
						Layout.mDataAccess.updateSetupDB("SubscriberID", subscriberid);
						Layout.mDataAccess.updateSetupDB("CurrentUserId", obj.getJSONObject("data").getString("userid"));
						DataStorage.setSubscriberId(subscriberid);
						DataStorage.setCurrentUserId(obj.getJSONObject("data").getString("userid"));
						dialog.cancel();
						Toast.makeText(mActivity, "Login Successful", Toast.LENGTH_LONG).show();
//						mActivity.sendBroadcast(new Intent().putExtra("authenticated", true));
					}else{
						Toast.makeText(mActivity, "Login failed. Please try again", Toast.LENGTH_LONG).show();
					}
				}else if(checkstatus.equalsIgnoreCase("failure")){
					if(Constant.DEBUG)  Log.d(TAG, "DoingInBackgroundClass onPostExecute failure" );
					Toast.makeText(mActivity, "Login failed. Please try again", Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
