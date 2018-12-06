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

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.player.R;
import com.player.util.CustomLayout;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;


/**
 * CustomMenu class
 *
 * This is the class that manages our menu items and the popup window.
 * 
 * @author abhijeet
 *
 */
public class CustomMenu {
	/**
	 * Global variables.
	 */
	private ArrayList<CustomMenuItem> mMenuItems;
	private OnMenuItemSelectedListener mListener = null;
	private Activity mContext = null;
	private LayoutInflater mLayoutInflater = null;
	private PopupWindow mPopupWindow = null;
	private boolean mIsShowing = false;
	private boolean mHideOnSelect = true;
	private boolean mReverseMenu = false;
	private int mRows = 0;
	private int mItemsPerLineInPortraitOrientation = 3;
	private int mItemsPerLineInLandscapeOrientation = 6;

	private CustomLayout customLayout;

	/**
	 * The interface for returning the item clicked.
	 */
	public interface OnMenuItemSelectedListener {
		public void onClickMenuItem(CustomMenuItem selection);
	}

	/**
	 * Use this method to determine if the menu is currently displayed to the user.
	 * @return boolean isShowing
	 */	
	public boolean isShowing() { 
		return mIsShowing;
	}

	/**
	 * This setting controls whether or not the menu closes after an item is selected.
	 * @param boolean doHideOnSelect
	 * @return void
	 */	
	public void setHideOnSelect(boolean doHideOnSelect) { 
		mHideOnSelect = doHideOnSelect; 
	} 

	/**
	 * 
	 * This setting in particular applied to portrait orientation.
	 * @param int count
	 * @return void
	 */	
	public void setItemsPerLineInPortraitOrientation(int count) {
		mItemsPerLineInPortraitOrientation = count; 
	}

	/**
	 * set No of item per row
	 * This setting in particular applied to landscape orientation.
	 * @param int count
	 * @return void
	 */	
	public void setItemsPerLineInLandscapeOrientation(int count) { 
		mItemsPerLineInLandscapeOrientation = count; 
	}

	/**
	 * Use this method to assign your menu items. You can only call this when the menu is hidden.
	 * @param ArrayList<CustomMenuItem> items
	 * @return void
	 * @throws Exception "Menu list may not be modified while menu is displayed."
	 */	
	public synchronized void setMenuItems(ArrayList<CustomMenuItem> items) throws Exception {
		if (mIsShowing) {
			throw new Exception("Menu list may not be modified while menu is displayed.");
		}
		mMenuItems = items;
	}

	public synchronized void setMenuReverseOrder(boolean showReverse){
		mReverseMenu = showReverse;
	}

	/**
	 * default constructor
	 * @param Context context
	 * @param OnMenuItemSelectedListener listener
	 * @param LayoutInflater lo
	 * @return void
	 */	
	public CustomMenu(Activity context, OnMenuItemSelectedListener listener, LayoutInflater lo) {
		mListener = listener;
		mMenuItems = new ArrayList<CustomMenuItem>();
		mContext = context;
		mLayoutInflater = lo;
		customLayout = new CustomLayout(mContext);
	}

	/**
	 * Display menu.
	 * @param View v
	 * @return void
	 */	
	public synchronized void show(View v) {
		try {
			mIsShowing = true;
			boolean isLandscape = false;
			if(mMenuItems == null || mMenuItems.size() == 0){
				return;
			}
			int itemCount = mMenuItems.size();
			if (itemCount<1) 
				return; //no menu items to show
			if (mPopupWindow != null) 
				return; //already showing

			DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
			Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			if (display.getWidth() > display.getHeight()) isLandscape = true;
			View mView= mLayoutInflater.inflate(R.layout.custom_menu, null);
			mPopupWindow = new PopupWindow(mView,android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT, false);
			mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
			mPopupWindow.setWidth(display.getWidth());


			int divisor = mItemsPerLineInPortraitOrientation;
			if (isLandscape) divisor = mItemsPerLineInLandscapeOrientation;
			int remainder = 0;
			if (itemCount < divisor) {
				mRows = 1;
				remainder = itemCount;
			} else {
				mRows = (itemCount / divisor);
				remainder = itemCount % divisor;
				if (remainder != 0) mRows++;
			}

			TableLayout table = (TableLayout)mView.findViewById(R.id.custom_menu_table);
			HashMap<Integer, TableRow> tablelist = new HashMap<Integer, TableRow>();
			table.removeAllViews();
//			Drawable drawable = mContext.getResources().getDrawable(R.drawable.list_selector);
			for (int i=0; i < mRows; i++) {
				TableRow row = null;
				TextView tv = null;
				ImageView iv = null;
				row = new TableRow(mContext);
				TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1f);
				row.setLayoutParams(layoutParams);
				row.setPadding(0, 0, 0, 0);
				for (int j=0; j< divisor; j++) {
					if (i*divisor+j >= itemCount) 
						break;
					final CustomMenuItem cmi = mMenuItems.get(i*divisor+j);
					View itemLayout = mLayoutInflater.inflate(R.layout.custom_menu_item, null);
					itemLayout.setPadding(1, 1, 1, 1);
					TableRow.LayoutParams mRowParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1f);
					itemLayout.setLayoutParams(mRowParams);
					tv = (TextView)itemLayout.findViewById(R.id.custom_menu_item_caption);
					Typeface font = Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Regular.ttf");
					tv.setTypeface(font);
					if(cmi.getCaption() != null){
						tv.setText(cmi.getCaption());
					}
					iv = (ImageView)itemLayout.findViewById(R.id.custom_menu_item_icon);
					if(cmi.getImageResourceId() != -1 ){
						iv.setImageResource(cmi.getImageResourceId());
					}else{
						itemLayout.setBackgroundDrawable(null);
					}
					itemLayout.setOnClickListener( new OnClickListener() {
						@Override
						public void onClick(View v) {
							mListener.onClickMenuItem(cmi);
							if (mHideOnSelect) hide();
						}
					});
					row.addView(itemLayout);

				}
				
				tablelist.put(i,row);
				if(!mReverseMenu)
					table.addView(row);
			}
			if (mReverseMenu) {
				if (tablelist != null && tablelist.size() > 0) {
					for (int i = tablelist.size() - 1; i >= 0; i--) {
						table.addView(tablelist.get(i));
					}
				}
			}
			/*if(tablelist != null && tablelist.size() >0){
				List<Integer> list = new ArrayList<Integer>();
				for (Integer str : tablelist.keySet()) {
					list.add(str);
				}
				Collections.reverse(list);
				for(Integer value:list){
					table.addView(tablelist.get(value));
				}
				}*/
			int convertedHieght = 80;
			if(customLayout != null){
				convertedHieght = customLayout.getConvertedHeight(80);
			}
			int tempHeight = (mRows*convertedHieght)+customLayout.getConvertedHeight(ScreenStyles.BOTTOM_LAYOUT_HEIGHT);
			int menuY = dm.heightPixels-tempHeight;
			mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}

	/**
	 * Hide your menu.
	 * @return void
	 */	
	public synchronized void hide() {
		mIsShowing = false;
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
			mPopupWindow = null;
		}
		return;
	}
}
