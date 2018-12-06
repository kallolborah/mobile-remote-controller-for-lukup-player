package com.player.network;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;

import com.player.Player;
import com.player.util.Constant;
import com.player.util.SystemLog;

/**
* A BroadcastReceiver that notifies of important wifi p2p events.
*/
public class WifiDirectBroadcastReceiver extends BroadcastReceiver {
	
	private String TAG = "WifiDirectBroadcastReceiver";
	private Player player;
	private WifiP2pManager mManager;
	private Channel mChannel;
	private PeerListListener peerListListener;
	
	public WifiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel, PeerListListener peerListListener, Player player){
		super();
		this.mManager = manager;
		this.mChannel = channel;
		this.peerListListener = peerListListener;
		this.player = player;
		if(Constant.DEBUG)  Log.d(TAG, "Wifi broadcastreceiver initialized");
	}

	@Override
    public void onReceive(Context context, Intent intent) {
		try{
	        String action = intent.getAction();
	        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
	            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
	            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
	            	if(Constant.DEBUG)  Log.d(TAG, "Wifi P2P bound");
	                player.mWifiBound = true;
	            } else {
	                player.mWifiBound = false;
	            }
	        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
	
	        	if(Constant.DEBUG)  Log.d(TAG, "Wifi P2P peers found");
	        	if (mManager != null) {
	                mManager.requestPeers(mChannel, peerListListener);
	            }
	
	        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
	        	if(Constant.DEBUG)  Log.d(TAG,"Wifi P2P Connection changed action");
	        	if(mManager == null){
	        		return;
	        	}
	        	NetworkInfo networkinfo = (NetworkInfo)intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
	        	if(networkinfo.isConnected()){
	        		if(Constant.DEBUG)  Log.d(TAG,"Notifying that connection is requested");
	        		mManager.requestConnectionInfo(mChannel, new WifiP2PListener());
	        		mManager.requestGroupInfo(mChannel, new WifiGroupInfo());
	        	}
	        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
	        	 player.p2pdevice = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
	        }
		}catch(Exception e){
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_WIFI, errors.toString(), e.getMessage());
			
		}
    }
}
