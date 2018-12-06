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

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.player.R;
import com.player.util.Constant;
import com.player.util.CustomLayout;
import com.player.util.ImageLoader;
import com.player.util.LoadLocalImage;
import com.player.util.ScreenStyles;
import com.player.util.SetBackgroundFromUrl;
import com.player.util.SystemLog;
import com.player.util.Utils;


/**
 * @author abhijeet
 *
 */
public class ListViewAdapter extends BaseAdapter {

	/*
	 * Fields declaration
	 */
	private Activity mActivity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater=null;
	public ImageLoader imageLoader; 
	private View vi;
	private int selectedOption = 0;
	private String type = null;
	private HashMap<Integer, RadioButton> radioGroup = null;
	private String planDetail = null;
	private ViewGroup mParent = null;
	private View mSelectedView = null;
	private static int mSelectedPosition = -1;
	CustomLayout mCustomlay = null;
	Typeface font = null;
	
//	private int[] colors = new int[] { 0xFFE6E6E6 ,0xFFF2F2F2 };
	
	/**
	 * Constructor
	 * @param activ -Activity of UI
	 * @param list - list to display
	 * @param viewEnableId -Type of list view
	 * @param planOption 
	 */
	public ListViewAdapter(Activity activ, ArrayList<HashMap<String, String>> list,int viewEnableId,String mtype, String planOption,int selectedPosition){
		mActivity = activ;
		mSelectedPosition = selectedPosition;
		data=list;
		selectedOption = viewEnableId;
		inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(mActivity);
		this.type = mtype;
		radioGroup = new HashMap<Integer, RadioButton>();
		planDetail = planOption;
		mCustomlay = new CustomLayout(mActivity);
		font = Typeface.createFromAsset(mActivity.getAssets(), "OpenSans-Regular.ttf");
	}

	/*
	 * get List count
	 */
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		if(mParent != null){
			if(mParent.getChildAt(position) != null){
				return mParent.getChildAt(position);
			}
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean isEnabled(int position) {
		if(type != null && !type.equalsIgnoreCase("setup")){
			return super.isEnabled(position);
		}else if(position <= mSelectedPosition){
			return true;
		}else{
			return false;

		}
	}

	public void setSelected(int position){
		mSelectedPosition = position;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		vi = convertView;
		mParent = parent;
		 ViewHolder holder = null;
		if(convertView==null){
			holder = new ViewHolder();
			vi = inflater.inflate(R.layout.simplelist, null);
			holder.title = (TextView)vi.findViewById(R.id.title); // title
			holder.desc = (TextView)vi.findViewById(R.id.descript); // description
			holder.rightTxt = (TextView)vi.findViewById(R.id.rightTxt); // description 
			holder.priceTag = (RelativeLayout)vi.findViewById(R.id.priceTag);
			holder.thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
			holder.arrow = (ImageView) vi.findViewById(R.id.arrow);
			vi.setTag(holder);
			
//			int colorPos = position % colors.length;
//			vi.setBackgroundColor(colors[colorPos]);
			
			vi.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.list_selector));
			
		}else{
			holder = (ViewHolder)vi.getTag();
		}
		parent.setSelected(true);
		
		if(holder.title != null){
			holder.title.setTag("title");
		}

		holder.title.setTypeface(font);
		holder.title.setSingleLine(true);

		HashMap<String, String> fields = new HashMap<String, String>();
		fields = data.get(position);

		if(mSelectedPosition == position){
			mSelectedView = vi;
		}

		if(fields.get(ScreenStyles.LIST_KEY_THUMB_URL) != null){
			String imgUrl = fields.get(ScreenStyles.LIST_KEY_THUMB_URL);
			if(CheckImageID(imgUrl)){
				Runnable r = new LoadLocalImage(mActivity,imgUrl,holder.thumb_image);
				new Thread(r).start();
			}
		}else if(fields.get(ScreenStyles.LIST_KEY_HTTP_URL) != null && !fields.get(ScreenStyles.LIST_KEY_HTTP_URL).equalsIgnoreCase("") ){
			String imgUrl = fields.get(ScreenStyles.LIST_KEY_HTTP_URL);
			Runnable r = new SetBackgroundFromUrl(mActivity,imgUrl,holder.thumb_image);
			new Thread(r).start();
		}else{
			if(holder.thumb_image != null){
//				holder.thumb_image.setVisibility(View.INVISIBLE);
				holder.thumb_image.setVisibility(View.GONE);
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = 10;
				layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
				holder.title.setGravity(Gravity.CENTER_VERTICAL);
				holder.title.setLayoutParams(layoutParams);
				layoutParams.alignWithParent = true;
			}
		}
		
		
		if(type != null && type.equalsIgnoreCase("networklist")){
			if(holder.title != null ){
				if(planDetail != null && planDetail.equalsIgnoreCase("enable")){
					if(mSelectedPosition >= position){
						vi.setEnabled(true);
						holder.title.setTextColor(Color.parseColor("#FFFFFF"));
						vi.getBackground().setAlpha(255);
						if(holder.thumb_image != null && holder.thumb_image.getBackground() != null){
							holder.thumb_image.getBackground().setAlpha(255);
						}
					}
				}else{
				//	if (position > 1) {
				//Added by @Tomesh for Authenticate option coming transparent . 	
					if (position > 2) {
						vi.getBackground().setAlpha(72);
						if(Constant.DEBUG) System.out.println("set BG transparent");
						holder.title.setTextColor(Color.parseColor("#BBBBBB"));
						if(holder.thumb_image != null && holder.thumb_image.getBackground() != null){
							holder.thumb_image.getBackground().setAlpha(0);
						}
					}else{
						vi.setEnabled(true);
						holder.title.setTextColor(Color.parseColor("#FFFFFF"));
						vi.getBackground().setAlpha(255);
						if(holder.thumb_image != null && holder.thumb_image.getBackground() != null){
							holder.thumb_image.getBackground().setAlpha(255);
						}
					}
				}
				
			}
		}

		if(type != null && type.equalsIgnoreCase("setup")){
			if(holder.title != null){
				if(mSelectedPosition >= position){
					vi.setEnabled(true);
					holder.title.setTextColor(Color.parseColor("#FFFFFF"));
					vi.getBackground().setAlpha(255);
					if(holder.thumb_image != null && holder.thumb_image.getBackground() != null){
						holder.thumb_image.getBackground().setAlpha(255);
					}
				}else{
//					vi.getBackground().setAlpha(72);
//					if(Constant.DEBUG) System.out.println("set BG transparent");
//					holder.title.setTextColor(Color.parseColor("#BBBBBB"));
//					if(holder.thumb_image != null && holder.thumb_image.getBackground() != null){
//						holder.thumb_image.getBackground().setAlpha(0);
//					}
				}
			}
		}
		// Setting all values in listview
		if(fields.containsKey(ScreenStyles.LIST_KEY_TITLE)){
			String listTitle = fields.get(ScreenStyles.LIST_KEY_TITLE);
			if(listTitle != null && !listTitle.equalsIgnoreCase("")){
				holder.title.setText(listTitle);
				if(type != null && type.equalsIgnoreCase("show")){
					if(listTitle != null){
						if(mSelectedPosition == position){
							if(listTitle.length() >15){
								holder.title.setHorizontallyScrolling(true);
								holder.title.setSingleLine(true);
								holder.title.setFocusable(true);
								holder.title.setClickable(true);
								holder.title.setSelected(true);
								holder.title.setEllipsize(TruncateAt.END);
								holder.title.setEllipsize(TruncateAt.MARQUEE);
								holder.title.setMarqueeRepeatLimit(5);
								holder.title.setText(listTitle);
							}else{
								holder.title.setSingleLine(true);
								holder.title.setText(listTitle);
							}
						}else{
							if(listTitle.length() >20){
								String subTitle = listTitle.substring(0, 20);
								subTitle = subTitle+"..." ;
								holder.title.setSingleLine(true);
								holder.title.setText(subTitle);
							}else{
								holder.title.setSingleLine(true);
								holder.title.setText(listTitle);
							}
						}
					}
				}else{
					if(listTitle.length() >25){
						String subTitle = listTitle.substring(0, 25);
						subTitle = subTitle+"..." ;
						holder.title.setSingleLine(true);
						holder.title.setText(subTitle);
					}else{
						holder.title.setSingleLine(true);
						holder.title.setText(listTitle);
					}
				}
			}
		}

		if(fields.containsKey(ScreenStyles.LIST_KEY_PRICE) && Utils.checkNullAndEmpty(fields.get(ScreenStyles.LIST_KEY_PRICE))){
			String price = fields.get(ScreenStyles.LIST_KEY_PRICE);

			String msubscribe = "";
			if(fields.containsKey("subscribe")){
				msubscribe = fields.get("subscribe");
			}

			if(price.lastIndexOf(".") != -1){
				if(price.length() > price.lastIndexOf(".")+2)
					price = price.substring(0, price.lastIndexOf(".")+2);
				try {
					if(Float.parseFloat(price) == 0.0f){
						price = "Free";
					}
				}catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
				}
			}else{
				if(Constant.DEBUG) System.out.println("Index not found");
				try {
					if(Float.parseFloat(price) == 0.0f){
						price = "Free";
					}
				}catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
				}
			}
			if(Utils.checkNullAndEmpty(msubscribe)){
				if(msubscribe.equalsIgnoreCase("true")){
					price ="Subscribed";
				}
			}
			if(Constant.DEBUG) System.out.println("Getting price "+price+"  subscribed "+msubscribe);
			if(!price.equalsIgnoreCase("Free") && !price.equalsIgnoreCase("Subscribed") && !msubscribe.equalsIgnoreCase("true"))
				price = "Rs " + price;
			if(holder.rightTxt != null){
				holder.rightTxt.setText(price);
				//				rightTxt.setTypeface(font);
				holder.rightTxt.setTypeface(font, Typeface.BOLD);
				holder.rightTxt.setVisibility(View.VISIBLE);
			}
			if(holder.priceTag != null){
				holder.priceTag.setVisibility(View.VISIBLE);
			}
			if(price.equalsIgnoreCase("Subscribed")){
				holder.priceTag.setVisibility(View.INVISIBLE);
			}
		}else{
			if(holder.rightTxt != null){
				holder.rightTxt.setVisibility(View.INVISIBLE);
			}
			if(holder.priceTag != null){
				holder.priceTag.setVisibility(View.INVISIBLE);
			}
		}

		if(fields.containsKey(ScreenStyles.LIST_KEY_DESCRIPTION)){
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
//			layoutParams.topMargin = mCustomlay.getConvertedHeight(5);
			layoutParams.bottomMargin = mCustomlay.getConvertedHeight(5);
			layoutParams.leftMargin = mCustomlay.getConvertedWidth(10);
			layoutParams.addRule(RelativeLayout.ALIGN_TOP);
			if(holder.title != null){
				holder.title.setLayoutParams(layoutParams);
			}
			RelativeLayout.LayoutParams declayoutParams = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			declayoutParams.addRule(RelativeLayout.BELOW, R.id.title);	
			declayoutParams.leftMargin = mCustomlay.getConvertedHeight(10);
			holder.desc.setText(fields.get(ScreenStyles.LIST_KEY_DESCRIPTION));
			holder.desc.setLayoutParams(declayoutParams);
			holder.desc.setVisibility(View.VISIBLE);
		}
			

		if(type != null){
			if(type.equalsIgnoreCase("setup")){
				if(holder.desc != null){
					holder.desc.setVisibility(View.GONE);
				}
			}
		}
			
		if(type != null){
			if(type.equalsIgnoreCase("plan")){
				if(holder.arrow != null){
					holder.arrow.setVisibility(View.GONE);
					//					arrow.setOnClickListener(new PlanClickListener(mActivity,fields,planDetail));
				}
			}
		}
		if(type!= null && type.equalsIgnoreCase("show")){
			if(position != mSelectedPosition){
				vi.setSelected(false);
			}
		}

		if(holder.title != null){
			holder.title.setClickable(false);
			holder.title.setFocusable(false);
			holder.title.setSelected(false);
		}

		/**
		 * select the list view Ui type (Ex: With radio button,With Check Box )
		 */
		switch (selectedOption) {
		case 0:
			if(fields.get(ScreenStyles.LIST_KEY_IMAGE_AT_LEFT) != null){
				String imgUrl = fields.get(ScreenStyles.LIST_KEY_IMAGE_AT_LEFT);
				if(holder.arrow != null && CheckImageID(imgUrl)){
					Runnable r = new LoadLocalImage(mActivity,imgUrl,holder.arrow);
					new Thread(r).start();
				}
			}
			return vi;

		case 1:

			return vi;
		case 2:
			CheckBox checkBox = (CheckBox) vi.findViewById(R.id.checkbox);
			if(checkBox != null && holder.arrow != null){
				holder.arrow.setVisibility(View.INVISIBLE);
				checkBox.setVisibility(View.VISIBLE);
			}
			return vi;
		case 3:
			if(holder.thumb_image != null)
			{
				holder.thumb_image.setVisibility(View.INVISIBLE);
			}
			return vi;
		case 4:
			if(fields.get(ScreenStyles.LIST_KEY_THUMB_URL) != null){
				String imgUrl = fields.get(ScreenStyles.LIST_KEY_THUMB_URL);
				if(CheckImageID(imgUrl)){
					Runnable r = new LoadLocalImage(mActivity,imgUrl,holder.thumb_image);
					new Thread(r).start();
				}else{
					holder.thumb_image.setVisibility(View.INVISIBLE);
				}
			}else if(fields.get(ScreenStyles.LIST_KEY_HTTP_URL) != null && !fields.get(ScreenStyles.LIST_KEY_HTTP_URL).equalsIgnoreCase("") ){
				String imgUrl = fields.get(ScreenStyles.LIST_KEY_HTTP_URL);
				Runnable r = new SetBackgroundFromUrl(mActivity,imgUrl,holder.thumb_image);
				new Thread(r).start();
			}else{
				if(holder.thumb_image != null)
				{
					holder.thumb_image.setVisibility(View.INVISIBLE);
					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = 10;
					holder.title.setGravity(Gravity.CENTER_VERTICAL);
					layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
					holder.title.setLayoutParams(layoutParams);
				}
			}
			return vi;
		default:
			break;
		}
		return vi;
	}
	/**
	 * @param imgUrl
	 * @return
	 */
	private boolean CheckImageID(String imgUrl) {
		int id = -1;
		try{
			id = Integer.parseInt(imgUrl);
		}catch(Exception e){
			id = -1;
		}
		if(id != -1){
			return true;
		}
		return false;
	}

	public View getSelectedView(){
		return mSelectedView;
	}

}
