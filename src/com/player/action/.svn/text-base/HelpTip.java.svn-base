package com.player.action;

import com.player.Layout;
import com.player.Player;
import com.player.R;
import com.player.util.Constant;
import com.player.util.DataAccess;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HelpTip {
	private static Activity mActivity;
	private static String TAG = "HelpTip";
	private static Dialog dialog;
	private static DataAccess dataAccess;
	public static void requestForHelp(final String title, final String msg, final Activity act) {
		if(Constant.DEBUG)  Log.d(TAG  ,"requestForHelp() msg "+msg);
		mActivity = act;
		if(mActivity != null){
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(Constant.DEBUG)  Log.d(TAG,"before Help value: "+Player.help);
					dataAccess = new DataAccess();
					if(dataAccess.getHelptip().equalsIgnoreCase("false")){
						Player.help = false;
					}else {
						Player.help = true;
					}	
					
					boolean value = Player.help;
					if(value){
						if(dialog != null && dialog.isShowing()){
							dialog.dismiss();
						}
						
						LayoutInflater inflater = mActivity.getLayoutInflater();
						dialog = new Dialog(mActivity,R.style.ThemeDialogCustom);
						View messageDialog = inflater.inflate(R.layout.overlay_help, null);
						
						if(dialog != null){
							TextView titleView = (TextView) messageDialog.findViewById(R.id.overlayTitle);
							TextView msgView = (TextView) messageDialog.findViewById(R.id.helpmsg);
							Button closeBtn = (Button) messageDialog.findViewById(R.id.button1);
							Button Btn = (Button) messageDialog.findViewById(R.id.button2);
							titleView.setText(title);
							msgView.setText(msg);
							
							if(closeBtn != null){
								closeBtn.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View arg0) {
										if(dialog != null && dialog.isShowing()){
											dialog.dismiss();
										}
									}
								});
							}
							if(Btn != null){
								Btn.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View arg0) {
										if(Constant.DEBUG)  Log.d(TAG,"before Help value: "+ Player.help);
										dataAccess.updateSetupDB("Helptip", "false");
										if(Constant.DEBUG)  Log.d(TAG,"after Help value: "+Player.help);
										if(dialog != null && dialog.isShowing()){
											dialog.dismiss();
										}
									}
								});
							}
							dialog.setContentView(messageDialog);
							dialog.show();
						}
					}
				}
			});
		}
	}
	
	public static void close (final Activity act){
		mActivity = act;
		if(mActivity != null){
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(dialog != null && dialog.isShowing()){
						dialog.dismiss();
					}
				}
			});
		}
	}
}
