package com.player.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.player.R;
import com.player.Layout;
import com.player.Player;

/**
 * AsyncDispatch: Async task to dispatch the requests from applications to Port. 
 *
 * @Version 	: 1.0
 * @Author  	: Lukup
 */
public class AsyncDispatch extends AsyncTask<String, String, Boolean> {
	String method;
	ArrayList<HashMap<String,String>> bundle;
	Boolean show;
	private static String TAG = "Dispatch";
	
	/**
	 * Constructor 
	 * @param  Method method to be executed on the port
	 * @param  params data to be passed port
	 * @param  Show   whether to display progress dialog or not
	 */
	public AsyncDispatch(String Method, ArrayList<HashMap<String,String>> params, Boolean Show) {
		method = Method;
		bundle = params;
		show = Show;
	}
		
	/**
	 * Initialization on UI thread - Displaying of Progress Dialog
	 */
	@Override
	protected void onPreExecute() {
	super.onPreExecute();
		if(show){
			Layout.progressDialog = new ProgressDialog(Player.player,R.style.MyTheme);
			Layout.progressDialog.setCancelable(true);
			Layout.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			Layout.progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
			Layout.progressDialog.show();
		}
	}
		
	/**
	 * Dispatch method to send data to port
	 * @param  params data to be sent to port
	 */
	@Override
	protected Boolean doInBackground(String... params) {
		if(Constant.DEBUG)  Log.d(TAG, "doInBackground ");
		Player.dispatchMethod(method, bundle);
		return true;
	}
		
	/**
	 * Result of dispatch task & update to UI thread
	 * @param  result result of the dispatch task
	 */		
	@Override
	protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result){
				if(Constant.DEBUG)  Log.d(TAG, "Data Dispatched "+result);
			}
		}
}

