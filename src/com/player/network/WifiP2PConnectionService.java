package com.player.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.player.Player;
import com.player.util.Constant;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;

public class WifiP2PConnectionService extends Service {
	
	// Debugging
	private static final String TAG = "WifiP2PConnectionService";
	private String host;
	private Handler mHandler; 
	private WifiConnectionThread ConnectionThread;
	private boolean isConnected = false;
	public static int mState = 0;
	public static final int STATE_NONE = 0;       // we're doing nothing
	public static final int STATE_LISTEN = 1;     // now listening for incoming connections
	public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
	public static final int STATE_CONNECTED = 3;  // now connected to a remote device
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId){
    	
    	if(Constant.DEBUG) Log.d(TAG, "onStartCommand in Wifi Connection Service called");
    	super.onStartCommand(intent, flags, startId);
    	return START_STICKY;
    }
	
	public final IBinder mBinder = (IBinder) new WifiBinder();

	public class WifiBinder extends Binder {
		public WifiP2PConnectionService getService() {
            // Return this instance of LocalService so clients can call public methods
			return WifiP2PConnectionService.this ;
        }
    }
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	public synchronized void start(Handler handler, String host) throws IOException {
		if (Constant.DEBUG) Log.d(TAG, "mState in start Wifi connection " + mState);
		if(mState!=STATE_CONNECTED){
			if (Constant.DEBUG) Log.d(TAG, "starting WifiP2P Listener and will try to connect to host " + host);
			mHandler = handler;
			this.host = host;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// Start the thread to listen on a ServerSocket
					if (ConnectionThread == null) {
						try {
							ConnectionThread = new WifiConnectionThread();
							ConnectionThread.start();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
			}).start();
		}
		
	}
	
	public synchronized void stop() {
		if (Constant.DEBUG) Log.d(TAG, "stop");

		if (ConnectionThread != null) {ConnectionThread.cancel(); ConnectionThread = null;}
	}
	
	private class WifiConnectionThread extends Thread {
		// The local server socket
		private Socket mmSocket;
		private OutputStream os=null;
		private InputStream is=null;
		
		public WifiConnectionThread() throws IOException {
			if(mmSocket==null){
				mmSocket = new Socket(host, 8988);
			}else{
//			mmSocket.bind(null);
				mmSocket.connect((new InetSocketAddress(host, 8988)), 5*60*1000);
			}
		}

		@Override
		public void run() {
			try {
				if(mmSocket!=null){
					is = mmSocket.getInputStream();
					os = mmSocket.getOutputStream();
					if(is!=null && os!=null){
						if (Constant.DEBUG) Log.i(TAG, "WifiP2P input and outputstream is fine");
						mHandler.obtainMessage(ScreenStyles.CONNECTION_STATE_CHANGE, STATE_CONNECTED, 0).sendToTarget();
						mState = STATE_CONNECTED;
						isConnected = true;
					}
					while(isConnected){
						if (Constant.DEBUG) Log.i(TAG, "Begin reading WifiP2P inputstream");
//						byte[] buffer = new byte[1024];
//						int bytes;

						// Keep listening to the InputStream while connected
						while (isConnected) {
							try {
								byte[] buffer = new byte[1024*8];
								int bytes;
								// Read from the InputStream
								bytes = is.read(buffer);
								if (Constant.DEBUG) Log.d(TAG, "Buffer size " + buffer.length);
								if(bytes != -1){
									Bundle b = new Bundle();
									b.putByteArray("portmsg", buffer);
									// Send the obtained bytes to the UI Activity
									mHandler.obtainMessage(ScreenStyles.MESSAGE_READ, bytes, -1, b)
									.sendToTarget();
								}else{
									break;
								}
									
							} catch (IOException e) {
								if (Constant.DEBUG) Log.e(TAG, "disconnected", e);
								e.printStackTrace();
								isConnected = false;
								StringWriter errors = new StringWriter();
								e.printStackTrace(new PrintWriter(errors));
								SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
								connectionLost();
								break;
							}
						}				
						Player.readBTMessage = "";
						isConnected = false;
						connectionLost();
					}		
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void cancel() {
			if (Constant.DEBUG) Log.d(TAG, "cancel " + this);
			try {
				mmSocket.close();
				if(os!=null){
					os.close();
				}
				if(is!=null){
					is.close();
				}
				isConnected = false;
			} catch (IOException e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
			}
		}
		
		synchronized public void write(byte[] buffer) {
			try {
				Player.readBTMessage = "";
				os.write(buffer);
				if (Constant.DEBUG) Log.d(TAG, "Writing to outputstream buffer");
				os.flush();
			} catch (IOException e) {
				if (Constant.DEBUG) Log.e(TAG, "Exception during write", e);
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
			}
		}
	}
	
	public void connectionLost() {
		try{
			mState = STATE_NONE;
			// Send a failure message back to the Activity
			mHandler.obtainMessage(ScreenStyles.CONNECTION_STATE_CHANGE, STATE_NONE, 0).sendToTarget();
			if (Constant.DEBUG) Log.d(TAG, "Wifi P2P connection lost ");
			stop();
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
		}
	}

	synchronized public void write(byte[] out) {
		try{
			// Create temporary object
			WifiConnectionThread r;
			// Synchronize a copy of the ConnectedThread
			if (Constant.DEBUG) Log.d(TAG, "checking state before sending message " + out.toString());
			synchronized (this) {
				if (ConnectionThread == null) return;
				r = ConnectionThread;
			}
			// Perform the write unsynchronized
			r.write(out);
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
		}
	}
	
}
