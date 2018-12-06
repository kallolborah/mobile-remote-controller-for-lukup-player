
/*
 * Copyright (c) Lukup Media Pvt Limited, India.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Lukup Media Pvt Limited ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms
 * of the licence agreement you entered into with Lukup Media Pvt Limited.
 *
 */package com.player.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
 /**
  * Class to convert layout based on device pixels. 
  * @author Abhijeet
  *
  */
 public  class CustomLayout {
	 private int xpos;
	 private int ypos;
	 private int height;
	 private int width;
	 private Activity activity;
	 private  int scnWidth;
	 private  int scnHeight;
	 private double avg;
	 private int scndpi;
	 private AbsoluteLayout.LayoutParams params;
	 private LinearLayout.LayoutParams linlayout;
	 private RelativeLayout.LayoutParams relativeParams;
	 private TableLayout.LayoutParams tableeParams;
	 private int fontSize=12;

	 public CustomLayout(Activity activ){
		 this.activity=activ;
		 DisplayMetrics dm=activity.getResources().getDisplayMetrics();
		 this.scnWidth = dm.widthPixels;
		 this.scnHeight = dm.heightPixels;
		 this.scndpi = dm.densityDpi;
	 }
	 /**
	  * get Absolute converted Layout Based on resolution
	  * @param width - width of layout
	  * @param height - height of layout
	  * @param x - x position of layout
	  * @param y - y position of layout
	  * @return
	  */
	 public  AbsoluteLayout.LayoutParams getLayoutParams(int width,int height,int x,int y){
		 this.xpos=x;
		 this.ypos=y;
		 this.height=height;
		 this.width=width;
		 if(this.activity !=null){

			 this.width=convertResolutionWithWidth(width);
			 this.xpos=convertResolutionWithWidth(xpos);

			 this.ypos=convertResolutionWithHeight(ypos);
			 this.height=convertResolutionWithHeight(height);
			 params=new AbsoluteLayout.LayoutParams(this.width, this.height, xpos, ypos);
			 return params;
		 }
		 return null;
	 }

	 /**
	  * get Linear converted Layout Based on resolution
	  * @param width - width of layout
	  * @param height - height of layout
	  * @param x - x position of layout
	  * @param y - y position of layout
	  * @return converted Linear Layout
	  */
	 public  LinearLayout.LayoutParams getLinearLayoutParams(int width,int height,int x,int y){
		 this.xpos=x;
		 this.ypos=y;
		 this.height=height;
		 this.width=width;
		 if(this.activity !=null){

			 this.width=convertResolutionWithWidth(width);
			 this.xpos=convertResolutionWithWidth(xpos);
			 this.ypos=convertResolutionWithHeight(ypos);
			 this.height=convertResolutionWithHeight(height);
			 linlayout=new LinearLayout.LayoutParams(this.width, this.height);
			 linlayout.leftMargin =xpos;
			 linlayout.topMargin = ypos;
			 return linlayout;
		 }
		 return null;
	 }

	 /**
	  * get Linear converted Layout Based on resolution
	  * @param width - width of layout
	  * @param height - height of layout
	  * @return converted Linear Layout
	  */
	 public  LinearLayout.LayoutParams getLinearLayoutParams(int width,int height){
		 this.height=height;
		 this.width=width;
		 if(this.activity !=null){
			 DisplayMetrics dm=activity.getResources().getDisplayMetrics();
			 this.width=convertResolutionWithWidth(width);
			 this.xpos=convertResolutionWithWidth(xpos);

			 this.ypos=convertResolutionWithHeight(ypos);
			 this.height=convertResolutionWithHeight(height);
			 linlayout=new LinearLayout.LayoutParams(this.width, this.height);
			 return linlayout;
		 }
		 return null;
	 }
	 /**
	  * get Table converted Layout Based on resolution
	  * @param width
	  * @param height
	  * @param x
	  * @param y
	  * @return
	  */
	 public  TableLayout.LayoutParams getTableLayoutParams(int width,int height,int x,int y){
		 this.xpos=x;
		 this.ypos=y;
		 this.height=height;
		 this.width=width;
		 if(this.activity !=null){
			 this.width=convertResolutionWithWidth(width);
			 this.height=convertResolutionWithHeight(height);
			 tableeParams=new TableLayout.LayoutParams(this.width, this.height);
			 return tableeParams;
		 }
		 return null;
	 }
	 
	 /**
	  * get Relative converted Layout Based on resolution
	  * @param width
	  * @param height
	  * @param x
	  * @param y
	  * @return
	  */
	 public  RelativeLayout.LayoutParams getRelativeLayoutParams(int width,int height,int x,int y){
		 this.xpos=x;
		 this.ypos=y;
		 this.height=height;
		 this.width=width;
		 if(this.activity !=null){
			 DisplayMetrics dm=activity.getResources().getDisplayMetrics();
			 this.width=convertResolutionWithWidth(width);
			 this.xpos=convertResolutionWithWidth(xpos);
			 this.ypos=convertResolutionWithHeight(ypos);
			 this.height=convertResolutionWithHeight(height);
			 relativeParams=new RelativeLayout.LayoutParams(this.width, this.height);
			 relativeParams.leftMargin =xpos;
			 relativeParams.topMargin = ypos;
			 return relativeParams;
		 }
		 return null;
	 }
	 /**
	  * get converted font size
	  * @param x - font size
	  * @return converted font size
	  */
	 public int getFontSize(float x){
		 if(this.scnWidth>=320){
			 return (int)x;
		 }
		 this.fontSize=convertFontResolution(x);
		 return this.fontSize;
	 }
	 private int convertResolutionWithHeight(int x){
		 float val=480;
		 if(x != 0){	
			 double convertedResolution;
			 avg= (x*100)/val;
			 convertedResolution=(avg*this.scnHeight)/100;
			 return (int) Math.ceil(convertedResolution);
		 }
		 return 0;
	 }
	 private int convertResolutionWithWidth(int x){
		 float val=320;
		 if(x != 0){	
			 double convertedResolution;
			 avg=(x*100)/val;
			 convertedResolution=(avg*this.scnWidth)/100;
			 return (int) Math.ceil(convertedResolution);
		 }
		 return 0;
	 }
	 private int convertFontResolution(float x){
		 if(x != 0.0f){	
			 float val=320;
			 float convertedResolution;
			 convertedResolution=(this.scnWidth*x)/val;
			 return (int) convertedResolution;
		 }
		 return 0;
	 }
	 
	 public int getConvertedWidth(int x){
		 float val=320;
		 if(x != 0){	
			 double convertedResolution;
			 avg=(x*100)/val;
			 convertedResolution=(avg*this.scnWidth)/100;
			 return (int) Math.ceil(convertedResolution);
		 }
		 return 0;
	 }
	 public int getConvertedHeight(int x){
		 float val=480;
		 if(x != 0){	
			 double convertedResolution;
			 avg= (x*100)/val;
			 convertedResolution=(avg*this.scnHeight)/100;
			 return (int) Math.ceil(convertedResolution);
		 }
		 return 0;
	 }
	 
	 public int getSystemWidth(){
		 return scnWidth;
	 }
	 
	 public int getSystemHeight(){
		 return scnHeight;
	 }

 }
