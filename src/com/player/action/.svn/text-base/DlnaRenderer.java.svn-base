package com.player.action;
//
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Future;

import org.json.JSONObject;
import org.teleal.cling.android.AndroidUpnpService;
import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.UDAServiceType;
import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
import org.teleal.cling.registry.DefaultRegistryListener;
import org.teleal.cling.registry.Registry;
import org.teleal.cling.support.avtransport.callback.Play;
import org.teleal.cling.support.avtransport.callback.SetAVTransportURI;
import org.teleal.cling.support.avtransport.callback.Stop;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.player.R;
import com.player.Layout;
import com.player.apps.Guide;
import com.player.service.UpnpService;
import com.player.util.Constant;
import com.player.util.DataProvider;
import com.player.util.DataStorage;
import com.player.util.SystemLog;
import com.player.util.Utils;
import com.player.widget.HelpText;

public class DlnaRenderer extends Layout implements OnItemClickListener {

	private static ArrayList<HashMap<String, String>> dispatchHashMap;
	private static final String TAG = "Renderer";

	private ArrayAdapter<DeviceDisplay> listAdapter;

	private RendererRegistryListener registryListener = new RendererRegistryListener();

	private AndroidUpnpService upnpService;
	private Service<?, ?> avTransportService ;
	private  Device<?, ?, ?> renderer;
	private DlnaRenderer mDlnaRenderer;
	private  String serverurl;
	RelativeLayout currentlayoutMain;
	private ListView rendererList;
	private android.widget.LinearLayout.LayoutParams standParams;
	
	private ServiceConnection serviceConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			upnpService = (AndroidUpnpService) service;
			// Refresh the list with all known devices
			listAdapter.clear();
			upnpService.getControlPoint().search();
			for (@SuppressWarnings("rawtypes")
			Device device : upnpService.getRegistry().getDevices()) {
				registryListener.deviceAdded(device);
				if(Constant.DEBUG) Log.i(TAG, device.getType().getType());
			}
			// Getting ready for future device advertisements
			upnpService.getRegistry().addListener(registryListener);
			// Search asynchronously for all devices
		}
		public void onServiceDisconnected(ComponentName className) {
			upnpService = null;
		}
	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDlnaRenderer = this;
		getApplicationContext().bindService(
				new Intent(this, UpnpService.class), serviceConnection,
				Context.BIND_AUTO_CREATE);
		
		Intent intent = getIntent();
		serverurl = intent.getStringExtra("sourceUrl");

		IntentFilter renderer = new IntentFilter("com.player.action.DlnaRenderer");
		registerReceiver(DLNAReceiver,renderer); 
		
		LayoutInflater  inflater = mDlnaRenderer.getLayoutInflater();
		View listView = inflater.inflate(R.layout.guidelist,null);
		mContainer.addView(listView,getLinearLayoutParams());
		rendererList = (ListView) listView.findViewById(R.id.lukuplist);
		rendererList = (ListView) findViewById(R.id.lukuplist);
		listAdapter = new ArrayAdapter(this,R.layout.activity_home_secure_schedule,R.id.schedule_text);
		rendererList.setAdapter(listAdapter);
		rendererList.setOnItemClickListener(this);
		progressDialog = new ProgressDialog(DlnaRenderer.this,R.style.MyTheme);
		progressDialog.setCancelable(true);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);    //Add
		progressDialog.show();
	
	}
	
	public LinearLayout.LayoutParams getLinearLayoutParams(){
		if(standParams== null){
			standParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		return standParams;
	}	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (upnpService != null) {
			upnpService.getRegistry().removeListener(registryListener);
		}
		getApplicationContext().unbindService(serviceConnection);
		
		if(DLNAReceiver!=null){
			mDlnaRenderer.unregisterReceiver(DLNAReceiver);
		}
	
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position,long id) {
//		Thread thread1 = new Thread() {
//		public void run() {
		renderer = listAdapter.getItem(position).getDevice();
			if(serverurl.equalsIgnoreCase("cancel")){
				avTransportService = renderer
						.findService(new UDAServiceType("AVTransport"));
				if (null != avTransportService) {
					upnpService.getControlPoint().execute(new Stop(new UnsignedIntegerFourBytes(0),avTransportService) {
						@Override
						public void failure(@SuppressWarnings("rawtypes") ActionInvocation invocation,UpnpResponse arg1,String arg2) {
							if(Constant.DEBUG) Log.i(TAG,"stop failure");
							HelpText.showHelpTextDialog(mDlnaRenderer, invocation.getFailure().getMessage(), 5000);
							//finish();
						}
						@Override
						public void success(@SuppressWarnings("rawtypes") ActionInvocation invocation) {
							super.success(invocation);
							if(Constant.DEBUG) Log.i(TAG,"stop success");
							mDataAccess.updateSetupDB(DataProvider.IsDlnaPlaying, "false");
							mDataAccess.updateSetupDB(DataProvider.DlnaDeviceName, "");
							DataStorage.setDlnaPlaying(false);
							DataStorage.setDLNAdevice("");
							Layout.mCurrentDevice = null;
							//finish();
						}
					});
				}
				
				dispatchHashMap  = new ArrayList<HashMap<String,String>>();
				HashMap<String, String> list = new HashMap<String, String>();
				list.put("consumer", "TV");
				list.put("network",mDataAccess.getConnectionType());
				list.put("caller", "com.player.action.DlnaRenderer");
				list.put("called", "startService");
				dispatchHashMap.add(list);
				new AsyncDispatch("com.port.apps.epg.Devices.StopDLNA", dispatchHashMap,false).execute();
			}else{
				if(Constant.DEBUG)Log.i(TAG, "IP===  "+DataStorage.getIPAddress());
				if(Constant.DEBUG) Log.i(TAG, "serverurl===  "+serverurl);
				dispatchHashMap  = new ArrayList<HashMap<String,String>>();
				HashMap<String, String> list = new HashMap<String, String>();
				list.put("consumer", "TV");
				list.put("network",mDataAccess.getConnectionType());
				list.put("sourceUrl", serverurl);
				list.put("caller", "com.player.action.DlnaRenderer");	// according to Class
				list.put("called", "startService");
				dispatchHashMap.add(list);
				String method = "com.port.apps.epg.Devices.playOnDLNA";
				new AsyncDispatchMethod(method, dispatchHashMap,false).execute();
			}
//			}
//		};
//		thread1.start();
	}

	protected class RendererRegistryListener extends DefaultRegistryListener {

		@Override
		public void remoteDeviceDiscoveryStarted(Registry registry,RemoteDevice device) {
			deviceAdded(device);
		}

		@Override
		public void remoteDeviceDiscoveryFailed(Registry registry,final RemoteDevice device, final Exception ex) {
			showToast("Discovery failed of "+ device.getDisplayString() +" Couldn't retrieve device/service descriptors",true);
			deviceRemoved(device);
		}

		@Override
		public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
			deviceAdded(device);
		}

		@Override
		public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
			deviceRemoved(device);
		}

		@Override
		public void localDeviceAdded(Registry registry, LocalDevice device) {
			deviceAdded(device);
		}

		@Override
		public void localDeviceRemoved(Registry registry, LocalDevice device) {
			deviceRemoved(device);
		}

		public void deviceAdded(final Device device) {
			runOnUiThread(new Runnable() {
				public void run() {
					
					if(Layout.progressDialog.isShowing()){
						Layout.progressDialog.dismiss();
						if(Constant.DEBUG)  Log.d(TAG , "progressDialog dismiss()");
					}
					if (device.getType().getType().equals("MediaRenderer")&& device instanceof RemoteDevice) {
						if(Constant.DEBUG) Log.i("mUpnpService Device", device.getType().getType());
						DeviceDisplay d = new DeviceDisplay(device);
						int position = listAdapter.getPosition(d);
						if (position >= 0) {
							if(Constant.DEBUG) Log.e(TAG," Removed =========");
							listAdapter.remove(d);
							listAdapter.insert(d, position);
						} else {
							listAdapter.add(d);
						}
					}
				}
			});
		}

		public void deviceRemoved(final Device device) {
			runOnUiThread(new Runnable() {
				public void run() {
					listAdapter.remove(new DeviceDisplay(device));
				}
			});
		}
	}

	protected void showToast(final String msg, final boolean longLength) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(DlnaRenderer.this, msg,
						longLength ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	public class DeviceDisplay {

		Device device;

		public DeviceDisplay(Device device) {
			this.device = device;
		}

		public Device getDevice() {
			return device;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			DeviceDisplay that = (DeviceDisplay) o;
			return device.equals(that.device);
		}

		@Override
		public int hashCode() {
			return device.hashCode();
		}

		@Override
		public String toString() {
			String name = device.getDetails() != null
					&& device.getDetails().getFriendlyName() != null ? device
					.getDetails().getFriendlyName() : device.getDisplayString();
			return device.isFullyHydrated() ? name : name + " *";
		}
	}

	//============================
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.DEBUG)	Log.d(TAG," Current Screen: " + DataStorage.getCurrentScreen());
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			if (Constant.DEBUG)	Log.d(TAG, "onKeyUp()  isRemoteConnected :" + Layout.mDataAccess.getIsRemoteConnected().equalsIgnoreCase("true")+", method :");
			finish();
			
		}
		return super.onKeyUp(keyCode, event);
	}
	
	//==============================
	
	public final BroadcastReceiver DLNAReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(Constant.DEBUG)  Log.d(TAG , "BroadcastReceiver ");
			Bundle extras = intent.getExtras();
			String jsondata = "";
			String handler = "";
			JSONObject objData = null;
			if(extras != null){
				if(extras.containsKey("Params")){
					jsondata = extras.getString("Params");
					if(extras.containsKey("Handler")){
						handler = extras.getString("Handler");
					}
					try {
						objData = new JSONObject(jsondata);
					}catch(Exception e){
				    	e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
				    }
					if(Constant.DEBUG)  Log.d(TAG , "jsondata  "+jsondata+", handler  "+handler);
					processUIData(handler, objData);
				}
			}
		}
	};
	
	private void processUIData(String handler,final JSONObject jsonData){
		try{
			if(Layout.progressDialog.isShowing()){
				Layout.progressDialog.dismiss();
				if(Constant.DEBUG)  Log.d(TAG , "progressDialog dismiss()");
			}
			if(Constant.DEBUG)  Log.d(TAG , "Process Action >>>>>.  "+handler);
			if(handler.equalsIgnoreCase("com.port.apps.epg.Devices.playOnDLNA")){
				if(jsonData.has("result")){
					String result = Utils.getDataFromJSON(jsonData, "result");
					if(result.equalsIgnoreCase("success")){
						avTransportService = renderer
								.findService(new UDAServiceType("AVTransport"));
						if (null != avTransportService) {
							// execute setAvTransportURI
							if(Constant.DEBUG) Log.i(TAG, "launch setAVTransportURI");
							
							final String controlurl = "http://" + DataStorage.getIPAddress()+ ":4044/proxy/0_1_1_1.mp4";
							
							if(Constant.DEBUG) Log.i(TAG, "controlurl to device ====> :  "+controlurl);
							if(Constant.DEBUG) Log.i(TAG, "url: to the Port :===>  " + serverurl);
				
							upnpService.getControlPoint().execute(new SetAVTransportURI(new UnsignedIntegerFourBytes(0), avTransportService, controlurl,"No") {
								@Override
								public void failure(@SuppressWarnings("rawtypes") ActionInvocation invocation,UpnpResponse arg1, String arg2) {
									if(Constant.DEBUG) Log.i(TAG, "setAVTransportURI failure");
									if(Constant.DEBUG) Log.i(TAG, "invocation: "+ invocation.getFailure().getMessage());
									HelpText.showHelpTextDialog(mDlnaRenderer, mDlnaRenderer.getResources().getString(R.string.DLNA_ERROR), 5000);
									//finish();
		
								}
								@Override
								public void success(@SuppressWarnings("rawtypes") ActionInvocation invocation) {
									super.success(invocation);
									if(Constant.DEBUG) Log.i(TAG, "setAVTransportURI success");
									if(Constant.DEBUG) Log.i(TAG, "launch play");
									upnpService.getControlPoint().execute(new Play(new UnsignedIntegerFourBytes(0),avTransportService,"1") {
										@Override
										public void failure(@SuppressWarnings("rawtypes") ActionInvocation invocation,UpnpResponse arg1,String arg2) {
											if(Constant.DEBUG) Log.i(TAG,"play failure");
											if(Constant.DEBUG) Log.i(TAG,"invocation: "+ invocation.getFailure().getMessage());
											//Failed playing media
											HelpText.showHelpTextDialog(mDlnaRenderer, mDlnaRenderer.getResources().getString(R.string.DLNA_ERROR), 5000);
											//finish();
										}
										@Override
										public void success(@SuppressWarnings("rawtypes") ActionInvocation invocation) {
											super.success(invocation);
											if(Constant.DEBUG) Log.i(TAG,"play success");
											mDataAccess.updateSetupDB(DataProvider.IsDlnaPlaying, "true");
											mDataAccess.updateSetupDB(DataProvider.DlnaDeviceName, renderer.getDisplayString());
											DataStorage.setDlnaPlaying(true);
											DataStorage.setDLNAdevice(renderer.getDisplayString());
											Layout.mCurrentDevice = renderer;
											//finish();
										}
									});
								  }
								});
							}
					}else{
						HelpText.showHelpTextDialog(mDlnaRenderer, mDlnaRenderer.getResources().getString(R.string.FAILED_LOAD), 5000);
					}
				}
			}
			if(handler.equalsIgnoreCase("com.port.apps.epg.Devices.StopDLNA")){
				if(jsonData.has("result")){
					String result = Utils.getDataFromJSON(jsonData, "result");
					if(!result.equalsIgnoreCase("success")){
						HelpText.showHelpTextDialog(mDlnaRenderer, mDlnaRenderer.getResources().getString(R.string.FAILED_UNLOAD), 5000);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}
	
}
