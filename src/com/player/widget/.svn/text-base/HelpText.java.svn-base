/*
 * Copyright (c) Lukup Media Pvt Limited, India.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Lukup Media Pvt Limited ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms
 * of the licence agreement you entered into with Lukup Media Pvt Limited.
 *
 */
package com.player.widget;

import java.util.Timer;
import java.util.TimerTask;

import com.player.R;
import com.player.util.CustomLayout;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



/**
 * @author abhijeet
 *
 */
public class HelpText {
	private static CustomLayout layer = null;

	public static void showHelpTextMessage(final Activity activity,final String message,final int sec){
		if(activity != null && message != null){
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast helptext = new Toast(activity);
					LayoutInflater inflater = activity.getLayoutInflater();
					View layout = inflater.inflate(R.layout.help_text,null);
					TextView text = (TextView) layout.findViewById(R.id.helptxt_view);
					text.setText(message);
					if(sec != 0){
						helptext.setDuration(sec);
					}
					CustomLayout mlayout = new CustomLayout(activity);
					helptext.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, mlayout.getConvertedHeight(80));
					helptext.setView(layout);
					helptext.show();
				}
			});
		}
	}
	
	/**
	 * show helpText Message on remote
	 * @param helpText
	 */
	public static void showHelpTextDialog(final Activity mActivity,final String helpText,final int sec) {
		if(mActivity != null){
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					int mSec = 3000;
					if(sec != 0){
						mSec = sec;
					}//overlay_pg
					LayoutInflater inflater = mActivity.getLayoutInflater();
					View messageDialog = inflater.inflate(R.layout.overlay_message, null);
					layer = new CustomLayout(mActivity);
					LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					messageDialog.setLayoutParams(params);
					TextView message = (TextView) messageDialog.findViewById(R.id.messageView);
					Button okBtn = (Button) messageDialog.findViewById(R.id.messageOkbtn);
					if(okBtn != null){
						okBtn.setVisibility(View.GONE);
					}
					message.setText(helpText);
					final Dialog dialog = new Dialog(mActivity,R.style.ThemeDialogCustom);
					dialog.setContentView(messageDialog);
					dialog.show();
					final Timer t = new Timer();
					t.schedule(new TimerTask() {
						@Override
						public void run() {
							if(dialog.isShowing()){
								dialog.dismiss(); 
							}
							t.cancel(); 
						}
					},mSec);
				}
			});
		}
	}
}
