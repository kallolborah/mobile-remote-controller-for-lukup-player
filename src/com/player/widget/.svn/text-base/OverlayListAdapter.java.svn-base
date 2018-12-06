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

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.player.R;
import com.player.util.CustomLayout;
import com.player.util.ImageLoader;
import com.player.util.LoadLocalImage;
import com.player.util.ScreenStyles;


/**
 * @author abhijeet
 *
 */
public class OverlayListAdapter extends BaseAdapter{

	/*
	 * Fields declaration
	 */
	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater=null;
	public ImageLoader imageLoader; 
	private View vi;
	private int selectedOption = 0;
	private int enablePosition;
//	private int[] colors = new int[] { 0xFFD9D9D9 ,0xFFF2F2F2 };
	
	/**
	 * Constructor
	 * @param activ -Activity of UI
	 * @param list - list to display
	 * @param viewEnableId -Type of list view
	 */
	public OverlayListAdapter(Activity activ, ArrayList<HashMap<String, String>> list,int viewEnableId){
		activity = activ;
		data=list;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(activity);
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		vi=convertView;
		if(convertView==null)
			vi = inflater.inflate(R.layout.list, null);
		
		CustomLayout layout = new CustomLayout(activity);
		TextView title = (TextView)vi.findViewById(R.id.overlaylist_title); // title
		TextView desc = (TextView)vi.findViewById(R.id.desc); // title
		RelativeLayout thumb_image=(RelativeLayout)vi.findViewById(R.id.overlaylist_image); // thumb image
		ImageView arrow = (ImageView) vi.findViewById(R.id.arrow);
		HashMap<String, String> fields = new HashMap<String, String>();
		fields = data.get(position);

		// Setting all values in listview
		if(fields.get(ScreenStyles.LIST_KEY_TITLE) != null)
			title.setText(fields.get(ScreenStyles.LIST_KEY_TITLE));
		if(fields.get(ScreenStyles.LIST_KEY_THUMB_URL) != null){
			String imgUrl = fields.get(ScreenStyles.LIST_KEY_THUMB_URL);
//			Runnable r = new LoadLocalImage(activity,imgUrl,thumb_image);
//			new Thread(r).start();
		}else{
			thumb_image.setVisibility(View.GONE);
			RelativeLayout.LayoutParams params = (LayoutParams) title.getLayoutParams();
			params.leftMargin = new CustomLayout(activity).getConvertedWidth(8);
			
		}
		
		desc.setVisibility(View.GONE);
		
		vi.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.list_selector));
		
		/**
		 * select the lisview Ui type (Ex: With radio button,With Check Box )
		 */
		switch (selectedOption) {
		case 0:
			return vi;
		case 1:
//			RadioButton radioBtn = (RadioButton) vi.findViewById(R.id.radiobutton);
//			if(radioBtn != null && arrow != null){
//				arrow.setVisibility(View.INVISIBLE);
//				radioBtn.setVisibility(View.VISIBLE);
//			}
			return vi;
		case 2:
			CheckBox checkBox = (CheckBox) vi.findViewById(R.id.checkbox);
			if(checkBox != null && arrow != null){
				arrow.setVisibility(View.INVISIBLE);
				checkBox.setVisibility(View.VISIBLE);
			}
			return vi;
		case 3:
			if(thumb_image != null)
			{
				thumb_image.setVisibility(View.INVISIBLE);
			}
			return vi;
		default:
			break;
		}
		return vi;
	}

}
