package com.player.action;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.player.network.ir.IRTransmitter;
import com.player.util.Constant;
import com.player.util.SystemLog;
import com.player.util.Utils;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class OperatorButtonClickListener implements OnClickListener{
	private static final String TAG = "OperatorButtonClickListener";

	int irEventCode = -1;
	int handle;
	String vendorName = "";
	

	public OperatorButtonClickListener(int ircode,int handler,String vendor){
		if(Constant.DEBUG)  Log.d(TAG , " In Operator button click listener :  "+ irEventCode +  " " + vendorName);
		irEventCode = ircode;
		handle = handler;
		vendorName = vendor;
	}
	
	@Override
	public void onClick(View v) {
		try{
			if(irEventCode != -1){
				if(Utils.checkNullAndEmpty(vendorName)){
					if(Constant.DEBUG)  Log.d(TAG , " In Operator button number:  "+ irEventCode);
					IRTransmitter.IRInput(irEventCode, handle, vendorName);
				}
			}	
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
		}
	}
}