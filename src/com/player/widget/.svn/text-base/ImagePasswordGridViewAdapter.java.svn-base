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
import android.widget.ImageView;

import com.player.R;
import com.player.util.CustomLayout;
import com.player.util.ImageLoader;
import com.player.util.LoadLocalImage;
import com.player.util.ScreenStyles;


/**
 * @author Yuvaraja
 *
 */
public class ImagePasswordGridViewAdapter extends BaseAdapter{

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

	/**
	 * Constructor
	 * @param activ -Activity of UI
	 * @param list - list to display
	 * @param viewEnableId -Type of list view
	 */
	public ImagePasswordGridViewAdapter(Activity activ, ArrayList<HashMap<String, String>> list,int viewEnableId){
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
			vi = inflater.inflate(R.layout.imagepassword_griditem, null);
		CustomLayout layout = new CustomLayout(activity);
		ImageView thumb_image=(ImageView)vi.findViewById(R.id.grid_item_image); // thumb image
		HashMap<String, String> fields = new HashMap<String, String>();
		fields = data.get(position);

		if(fields.get(ScreenStyles.LIST_KEY_THUMB_URL) != null){
			String imgUrl = fields.get(ScreenStyles.LIST_KEY_THUMB_URL);
				thumb_image.setTag(imgUrl);
				Runnable r = new LoadLocalImage(activity,imgUrl,thumb_image);
				new Thread(r).start();
		}
		return vi;

	}

}