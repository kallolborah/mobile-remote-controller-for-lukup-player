package com.player.service;

import org.teleal.cling.android.AndroidUpnpServiceConfiguration;
import org.teleal.cling.android.AndroidUpnpServiceImpl;

import android.net.wifi.WifiManager;

public class UpnpService extends AndroidUpnpServiceImpl{
	
	

    @Override
    protected AndroidUpnpServiceConfiguration createConfiguration(WifiManager wifiManager) {
        return new AndroidUpnpServiceConfiguration(wifiManager) {

    

        };
    
	
    }
}
