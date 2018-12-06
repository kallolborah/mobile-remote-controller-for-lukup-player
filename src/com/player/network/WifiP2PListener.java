package com.player.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.IBinder;
import android.util.Log;

import com.player.Player;
import com.player.network.WifiP2PConnectionService.WifiBinder;
import com.player.util.Constant;
import com.player.util.SystemLog;

public class WifiP2PListener implements ConnectionInfoListener{
	
	//Debugging Related
	private static String TAG = "WifiP2PListener";
	private String host;
	@SuppressWarnings("rawtypes")
	private List peers = new ArrayList();

	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo info) {

		if(info.groupFormed && info.isGroupOwner){
            if(Constant.DEBUG)  Log.d(TAG , "Mobile app is the wifi direct group owner !");
        }else if(info.groupFormed && !info.isGroupOwner){
        	
            host = info.groupOwnerAddress.getHostAddress();
            if(Constant.DEBUG)  Log.d(TAG , "Mobile app is the client, so start binding to Wifi direct service and connecting to group owner IP = " + host);
            
            Intent intent = new Intent(Player.getContext(), WifiP2PConnectionService.class);
           
            Player.getContext().bindService(intent, wConnection, Context.BIND_AUTO_CREATE);
        }
		
	}
	
	public ServiceConnection wConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className,IBinder service) {
			// We've bound to LocalService, cast the IBinder and get LocalService instance	
			WifiBinder binder = (WifiBinder) service;
			Player.mWifiP2PService = binder.getService();
			try {
				Player.mWifiP2PService.start(Player.getContext().new mBluetoothHandler(), host);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(Constant.DEBUG)  Log.d(TAG , "Wifi P2P initialized");
			
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Player.mWifiBound = false;
			if(Constant.DEBUG)  Log.d(TAG , "Wifi P2P Disconnected");
		}
	};	
	
	public PeerListListener peerListListener = new PeerListListener() {
		@SuppressWarnings("unchecked")
		@Override
		public void onPeersAvailable(WifiP2pDeviceList peerList) {
			try{
				// Out with the old, in with the new.
				ArrayList<HashMap<String,String>> wifiList = new ArrayList<HashMap<String,String>>();

				peers.clear();
				peers.addAll(peerList.getDeviceList());

				for(int i=0; i<peers.size(); i++){
					WifiP2pDevice device = (WifiP2pDevice) peers.get(i);
					HashMap<String,String> map = new HashMap<String, String>();
					String name = device.deviceName;
					String address = device.deviceAddress;
					map.put("name", name);
					map.put("address", address);
					wifiList.add(map);
					if(Constant.DEBUG)  Log.d(TAG,"Name: "+name+", address: "+address);
				}

				if (wifiList.size() == 0) {
					Log.d(TAG, "No wifi devices found");
					return;
				}else{
					if(Constant.DEBUG) Log.d(TAG, "wifiList.size(): "+wifiList.size());
					Intent peerBroadcast = new Intent();
					peerBroadcast.setAction("LUKUP_WIFI_PEERS");
					peerBroadcast.putExtra("State", "discovered");
					peerBroadcast.putExtra("Devices", wifiList);
					Player.getContext().sendBroadcast(peerBroadcast);
				}
			} catch(Exception e){
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_WIFI, errors.toString(), e.getMessage());
			}
		}
	};

}
