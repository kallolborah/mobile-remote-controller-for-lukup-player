
package com.player.network.ir;

/**
 * @author lukup
 *
 */

import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;
import com.player.util.Constant;
import com.player.util.SystemLog;

public final class IRTransmitter {

	static {
		System.loadLibrary ("IR-jni-remote");
	}

	//initialization
	public static final native int start (String deviceName, String vendorName); 
	public static native void stop ();
	public static native int stop (int handle);
	
	public static native String stringFromJNI();
	
	public static native int inputfunc (int func, int handle, String vendorName); //tatasky
	public static native int inputfuncnec (int func, int handle, String vendorName); //hathway
	public static native int inputfuncsun(int func, int handle,String vendorName); //sun
	public static native int inputfuncdish(int func, int handle,String vendorName); //dish
	public static native int inputfuncvideocon(int func, int handle,String vendorName); //videocon
	public static native int inputfuncbroadcom(int func, int handle,String vendorName); //broadcom
	

	private static final String TAG = "LUKUP-IR";
	
	public IRTransmitter () { }

	public static void setPermissionsForInputDevice () {
		String i = null;
		//Log.DEBUG ("Starting IR injection");
		if(Constant.DEBUG)  Log.d(TAG,"Starting IR and index=" +i);

		try {
			Process process = Runtime.getRuntime ().exec ("su");
			DataOutputStream dout = new DataOutputStream (process.getOutputStream ());
			dout.writeBytes ("chmod 666 /dev/ttyO4");
			dout.flush ();
			dout.close ();
			process.waitFor ();
			//if(Constant.DEBUG)  Log.debug ("Access to /dev/tty04 granted");
			if(Constant.DEBUG)  Log.d(TAG,"Access to /dev/ttyO4 granted and index=" +i);
		}  catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_IR, errors.toString(), e.getMessage());
		}
	}
	
	//wrapper function to start
	public static int startIr (String deviceName, String vendorName) {
		return start (deviceName, vendorName);
	}
	
	//wrapper function to stop
	public static void stopIr () {
		stop ();
	}
	
	//wrapper function with handle
	public static int stopIr (int handle) {
		return stop (handle);
	}
	
	//wrapper to send input signal
	public static int IRInput(int func, int handle, String vendorName){
		
		if (vendorName.equalsIgnoreCase("TATASKY")){
			if(Constant.DEBUG)  Log.d(TAG , " In IR transmitter number:  "+func);
			return inputfunc (func, handle, "tatasky");
		} 
		if (vendorName.equalsIgnoreCase("HATHWAY")){ 
			return inputfuncnec (func, handle, "hathway");
		} 
		if (vendorName.equalsIgnoreCase("SUNDIRECT")){
			return inputfuncsun(func, handle, "sun");
		}
		if (vendorName.equalsIgnoreCase("DISHTV")){
			return inputfuncdish(func, handle, "dishtv");
		}
		if (vendorName.equalsIgnoreCase("VIDEOCON")){
			return inputfuncvideocon(func, handle, "videocon");
		}
		if (vendorName.equalsIgnoreCase("BROADCOM")){
			return inputfuncbroadcom(func, handle, "broadcom");
		}
		if (vendorName.equalsIgnoreCase("AIRTEL")){
			return -1;
		}
		if (vendorName.equalsIgnoreCase("BIGTV")){
			return -1;
		}
		if (vendorName.equalsIgnoreCase("ACT")){
			return inputfuncnec (func, handle, "actir");
		}
		if (vendorName.equalsIgnoreCase("INCABLE")){
			return inputfuncnec (func, handle, "indigital");
		}
		if (vendorName.equalsIgnoreCase("HOMECABLE")){
			return inputfuncnec (func, handle, "hc");
		}
		if (vendorName.equalsIgnoreCase("DEN")){
			return inputfuncnec (func, handle, "actir");
		}
		return -1;
		
	}

}
