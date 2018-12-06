/*
* Copyright (c) Lukup Media Pvt Limited, India.
* All rights reserved.
*
* This software is the confidential and proprietary information of Lukup Media Pvt Limited ("Confidential Information").
* You shall not disclose such Confidential Information and shall use it only in accordance with the terms
* of the licence agreement you entered into with Lukup Media Pvt Limited.
*
*/
package com.player.util;

import android.app.Activity;
import android.widget.ImageView;

/**
 *
 * set Url Background Drawable to list
 * @author Yuvaraja 
 *
 */
public class SetBackgroundFromUrl implements Runnable{
	private String imageUrl;
	private ImageView imageView ;
	private Activity activity;
	public ImageLoader imageLoader;
	
	public SetBackgroundFromUrl(Activity activity,String url, ImageView imageView){
		this.imageUrl = url;
		this.imageView = imageView;
		this.activity = activity;
		 imageLoader = new ImageLoader(activity); 
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
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
