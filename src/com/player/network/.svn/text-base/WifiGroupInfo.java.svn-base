package com.player.network;

import com.player.util.Constant;

import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager.GroupInfoListener;
import android.util.Log;

public class WifiGroupInfo implements GroupInfoListener {
	
	private static String TAG = "WifiGroupInfo";

	@Override
	public void onGroupInfoAvailable(WifiP2pGroup arg0) {
		// TODO Auto-generated method stub
		if(Constant.DEBUG)  Log.d(TAG , "Group owner is " + arg0.getOwner().deviceAddress);
	}

}
