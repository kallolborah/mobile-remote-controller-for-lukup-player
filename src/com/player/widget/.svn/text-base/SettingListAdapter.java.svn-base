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
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.player.R;
import com.player.util.ImageLoader;
import com.player.util.LoadLocalImage;
import com.player.util.ScreenStyles;


/**
 * @author abhijeet
 *
 */
public class SettingListAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater=null;
	public ImageLoader imageLoader; 
	private View vi;
	Typeface font ;
//	private int[] colors = new int[] { 0xFFE6E6E6 ,0xFFF2F2F2 };
	
	public SettingListAdapter(Activity activ, ArrayList<HashMap<String, String>> list) {
		activity = activ;
		data=list;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(activity);
		font = Typeface.createFromAsset(activity.getAssets(), "OpenSans-Regular.ttf");  
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	static class ViewHolder {
		TextView title ;
		TextView desc ;
		TextView rightTxt;
		RelativeLayout priceTag;
		ImageView thumb_image;
		ImageView arrow ;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		vi=convertView;
		ViewHolder holder = null;
		if(convertView==null){
			holder = new ViewHolder();
			vi = inflater.inflate(R.layout.settingslist, null);
			holder.title = (TextView)vi.findViewById(R.id.title); // title
			holder.thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
			vi.setTag(holder);
			
//			int colorPos = position % colors.length;
//			vi.setBackgroundColor(colors[colorPos]);
			vi.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.list_selector));
//			vi.setBackgroundColor(R.color.pink);
			
		}else{
			holder = (ViewHolder) vi.getTag();
		}
		
		holder.title.setTypeface(font);  
		HashMap<String, String> fields = new HashMap<String, String>();
		fields = data.get(position);

		// Setting all values in listview
		if(fields.get(ScreenStyles.LIST_KEY_TITLE) != null)
			holder.title.setText(fields.get(ScreenStyles.LIST_KEY_TITLE));

		if(fields.get(ScreenStyles.LIST_KEY_THUMB_URL) != null && !fields.get(ScreenStyles.LIST_KEY_THUMB_URL).equalsIgnoreCase("")){
			String imgUrl = fields.get(ScreenStyles.LIST_KEY_THUMB_URL);
			Runnable r = new LoadLocalImage(activity,imgUrl,holder.thumb_image);
			new Thread(r).start();

		}else{
			if(holder.thumb_image != null){
				holder.thumb_image.setVisibility(View.INVISIBLE);
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = 0;
				holder.title.setLayoutParams(layoutParams);
				holder.title.setGravity(Gravity.CENTER|Gravity.CENTER_VERTICAL);
				layoutParams.alignWithParent = true;
			}
		}
		return vi;

	}
	public class SetBackgroundThread implements Runnable{
		private String imageUrl;
		private ImageView imageView ;
		public SetBackgroundThread(String url, ImageView imageView){
			this.imageUrl = url;
			this.imageView = imageView;
		}
		@Override
		public void run() {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					imageLoader.displayImage(imageUrl,imageView);
				}
			});
		}
	}
}
