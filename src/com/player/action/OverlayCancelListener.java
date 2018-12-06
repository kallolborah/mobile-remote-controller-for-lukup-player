/*
 * Copyright (c) Lukup Media Pvt Limited, India.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Lukup Media Pvt Limited ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms
 * of the licence agreement you entered into with Lukup Media Pvt Limited.
 *
 */
package com.player.action;

import com.player.R;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;


/**
 * @author lukup
 *
 */
public class OverlayCancelListener implements OnClickListener {

	private Dialog dialog;
	/**
	 * @param dialog
	 */
	public OverlayCancelListener(Dialog dialog) {
		this.dialog = dialog;
	}

	
	@Override
	public void onClick(View v) {
		if(dialog != null && dialog.isShowing()){
			this.dialog.cancel();
		}
		if(v.getId() == R.id.closeBtn){
			if(dialog != null && dialog.isShowing()){
				this.dialog.cancel();
			}
		}
	}

}
