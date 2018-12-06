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
 * @author Yuvaraja
 *
 */
public class LoadLocalImage implements Runnable{
	private String imageUrl;
	private ImageView imageView ;
	private Activity activity;
	private int id ;
	public LoadLocalImage(Activity activ,String url, ImageView imageView){
		this.activity = activ;
		this.imageUrl = url;
		this.imageView = imageView;
		try {
			this.id = Integer.parseInt(url);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			this.id = -1;
		}
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(id != -1)
					imageView.setBackgroundDrawable(activity.getResources().getDrawable(id));
			}
		});

	}

}
