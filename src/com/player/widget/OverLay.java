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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.player.R;
import com.player.action.ImagePasswordAction;
import com.player.action.OverlayCancelListener;
import com.player.util.CustomLayout;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;


/**
 * @author abhijeet
 *
 */
public class OverLay {

	/**
	 * field declaration
	 */
	private Activity activity;
	private Dialog dialog ;
	private EditText textfield;
	private EditText passwordfield;
	private String title;
	private HashMap<Integer, EditText> map = null;
	private String message = null;
	private String profileId = null;
	private boolean mShowSelected;
	private String mShowSelectedId = null;
	private String mShowSelectedType =null;
	private CustomLayout customlay = null;
	private HashMap<String, String> fetchmap =null;

	String radioButtonSelected = "Male";


	private ArrayList<HashMap<String, String>> list;
	public OverLay(Activity activity){
		this.activity = activity;
		customlay = new CustomLayout(activity);
	}
	/**
	 * create Overlay based on type
	 * @param type - Overlay type
	 * @param title - Overlay title
	 * @param txtMessage - text message to display
	 * @param id 
	 * @return
	 */
	public Dialog getOverLay(int type,String titlemessage,String txtMessage, ArrayList<HashMap<String, String>> bouquetList, String id,boolean isShowSelected){
		try {
			this.list = bouquetList;
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			dialog = new Dialog(activity,R.style.ThemeDialogCustom);
			mShowSelected =isShowSelected;
			message = txtMessage;
			profileId = id;
			this.title = titlemessage;
			if(mShowSelected){
				if(DataStorage.getShowSelectedType()!= null){
					mShowSelectedType = DataStorage.getShowSelectedType();
				}
				if(DataStorage.getShowSeletedId() != null){
					mShowSelectedId = DataStorage.getShowSeletedId();
				}
			}
			switch (type) {

			case ScreenStyles.OVERLAY_TYPE_GETVALUE:
				return getInputValueOverlay(txtMessage, inflater);

			case ScreenStyles.OVERLAY_TYPE_CONFIRMATION:
				getConfirmationOverlay(inflater);
				return dialog;
				
			case ScreenStyles.OVERLAY_TYPE_GETURL:
				break;

			case ScreenStyles.OVERLAY_TYPE_GETCREDENTIAL:
				return getCredentialOverlay(inflater);

			case ScreenStyles.OVERLAY_TYPE_LIST:
				return getOverlayList(inflater);
				
			case ScreenStyles.OVERLAY_WFILIST:
				return getWifiNetworkDetail(txtMessage, inflater);		
				
			default:
				break;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
			return null;
		}
	}
	
	private Dialog getWifiNetworkDetail(String txtMessage,LayoutInflater inflater){ 
		try{ 
			TextView titleView; 
			View view = inflater.inflate(R.layout.overlay_getvalue,null); 
			LinearLayout overlaymainscreen = (LinearLayout) view.findViewById(R.id.dialogLayout); 
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT); 

			dialog.setContentView(view); 
			if(title != null){ 
				titleView = (TextView) dialog.findViewById(R.id.overlayTitle); 
				if(titleView != null){ 
					Typeface font = Typeface.createFromAsset(activity.getAssets(), "OpenSans-Regular.ttf"); 
					titleView.setTypeface(font); 
					titleView.setText(title); 
				} 
			} 
			TextView plaintextview = (TextView) dialog.findViewById(R.id.messageView); 
			if(txtMessage != null){ 
				Typeface font = Typeface.createFromAsset(activity.getAssets(), "OpenSans-Regular.ttf"); 
				plaintextview.setTypeface(font); 
				plaintextview.setText(txtMessage); 
			} 
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
		return dialog; 
	}

	public Dialog getImagePwdOverlay(int type,String titlemessage,ArrayList<HashMap<String, String>> imageList,String method, String Id,String name) {
		try {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			dialog = new Dialog(activity,R.style.ThemeDialogCustom);
			View confirmationView;
			TextView titleView;
			confirmationView = inflater.inflate(R.layout.overlay_imagepassword,null);
			GridView grid_imagepassword = (GridView) confirmationView.findViewById(R.id.imagepassword_grid);
			TextView labelTxt = (TextView) confirmationView.findViewById(R.id.imagepassword_bottom_label);
			if(imageList == null ){
				imageList = new ArrayList<HashMap<String,String>>();
				for(int i=0;i<ScreenStyles.TEMPPROFILE_IMAGE_LIST_ICONS.length;i++){
					// adding each child node to HashMap key => value
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(ScreenStyles.LIST_KEY_THUMB_URL, Integer.toString(ScreenStyles.TEMPPROFILE_IMAGE_LIST_ICONS[i]));
					imageList.add(map);
				}
			}
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(confirmationView);
			grid_imagepassword.setAdapter(new ImagePasswordGridViewAdapter(activity, imageList, 0));
			grid_imagepassword.setOnItemClickListener(new ImagePasswordAction(activity,dialog,method,Id,name));
			return dialog;
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
		return dialog;
	}
	
	/**
	 * get confirmation overlay like delete
	 * @param inflater
	 */
	private void getConfirmationOverlay(LayoutInflater inflater) {
		Button cancelBtn;
		View confirmationView;
		TextView titleView;
		TextView messageView;
		confirmationView = inflater.inflate(R.layout.overlay_confirm,null);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(confirmationView);
		ImageView alertLogo = (ImageView) dialog.findViewById(R.id.alertlogo);
		if(title != null ){
			if(alertLogo != null)
				alertLogo.setBackgroundResource(R.drawable.v13_ico_alert_01);
			titleView = (TextView) dialog.findViewById(R.id.overlayTitle);
			if(titleView != null){
				titleView.setText(title);
			}
		}
		if(title != null && title.equalsIgnoreCase("Unlike")&& alertLogo != null){
			alertLogo.setBackgroundResource(R.drawable.v13_ico_alert_01);
		}
		messageView = (TextView) dialog.findViewById(R.id.messageView);
		if(title.equalsIgnoreCase("Delete")){
			messageView.setText(activity.getResources().getString(R.string.DELETE_TITLE));
		}
		if(message != null){
			messageView.setText(message);
		}
		messageView.setTypeface(null, Typeface.BOLD);
		cancelBtn = (Button)dialog.findViewById(R.id.overlayCancelButton);
		if(cancelBtn != null)
			cancelBtn.setOnClickListener(new  OverlayCancelListener(dialog));
	}
	/**
	 * show list overlay
	 * @param inflater
	 * @return
	 */
	private Dialog getOverlayList(LayoutInflater inflater) {
		TextView titleView;
		View listOverlay = inflater.inflate(R.layout.overlay_list,null);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(listOverlay);
		if(title != null){
			titleView = (TextView) dialog.findViewById(R.id.overlayTitle);
			if(titleView != null){
				titleView.setText(title);
			}
		}
		if(list != null && list.size() > 0 ){
			ListView ovelaylistItem = (ListView) dialog.findViewById(R.id.overlayList);
			
			if(ovelaylistItem != null){
				ovelaylistItem.setCacheColorHint(Color.TRANSPARENT);
				OverlayListAdapter adapter = new OverlayListAdapter(activity, list, 0);
				ovelaylistItem.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		}
		return dialog;
	}

	/**
	 * @param txtMessage
	 * @param inflater
	 * @return
	 */
	private Dialog getInputValueOverlay(String txtMessage,LayoutInflater inflater) {
		TextView titleView;
		View view = inflater.inflate(R.layout.overlay_getvalue,null);
		LinearLayout overlaymainscreen = (LinearLayout) view.findViewById(R.id.dialogLayout);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		dialog.setContentView(view);
		if(title != null){
			titleView = (TextView) dialog.findViewById(R.id.overlayTitle);
			if(titleView != null){
				Typeface font = Typeface.createFromAsset(activity.getAssets(), "OpenSans-Regular.ttf");
				titleView.setTypeface(font);
				titleView.setText(title);
			}
		}
		TextView plaintextview = (TextView) dialog.findViewById(R.id.messageView);
		if(txtMessage != null){
			Typeface font = Typeface.createFromAsset(activity.getAssets(), "OpenSans-Regular.ttf");
			plaintextview.setTypeface(font);
			plaintextview.setText(txtMessage);
		}

		return dialog;
	}

	/**
	 * @param inflater
	 * @return
	 */
	private Dialog getCredentialOverlay(final LayoutInflater inflater) {
		Button cancelBtn;
		View confirmationView;
		confirmationView = inflater.inflate(R.layout.overlay_add_account,null);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title from dialog
		dialog.setContentView(confirmationView);// set view for dialog

		/*
		 * overlay fields
		 */
		LinearLayout header = (LinearLayout)dialog.findViewById(R.id.overlayheader);
		textfield = (EditText) dialog.findViewById(R.id.username);
		passwordfield  =  (EditText) dialog.findViewById(R.id.password);
		
		TextView overlayTitle = (TextView) dialog.findViewById(R.id.overlayTitle);

		if(title != null && (title.equalsIgnoreCase("Login") || title.equalsIgnoreCase("lukuplogin")) && header != null){
			
			if(textfield != null){
				textfield.setHint("UserName");
				textfield.setTextColor(Color.BLACK);		
			}
			if(passwordfield != null){
				passwordfield.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				passwordfield.setHint("Password");
				passwordfield.setTextColor(Color.BLACK);			
			}
		}else{
			if(textfield != null){
				textfield.setHint("Provider");
				textfield.setTextColor(Color.BLACK);		
			}
			if(passwordfield != null){
				passwordfield.setInputType(InputType.TYPE_CLASS_TEXT);
				passwordfield.setHint("Url");
				passwordfield.setTextColor(Color.BLACK);			
			}
		}

		if(title != null){
			overlayTitle.setText(title);
		}
		cancelBtn = (Button)dialog.findViewById(R.id.overlayCancelButton);
		return dialog;
	}
	
}
