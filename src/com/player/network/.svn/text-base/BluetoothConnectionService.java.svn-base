/**
* Classname : BluetoothConnectionService
* 
* Version information : 1.0
* 
* Date : 19th Aug 2015
* 
* Copyright (c) Lukup Media Pvt Limited, India.
* All rights reserved.
*
* This software is the confidential and proprietary information of Lukup Media Pvt Limited ("Confidential Information").
* You shall not disclose such Confidential Information and shall use it only in accordance with the terms
* of the licence agreement you entered into with Lukup Media Pvt Limited.
*
*/

package com.player.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.player.Player;
import com.player.network.WifiP2PConnectionService.WifiBinder;
import com.player.util.Constant;
import com.player.util.DataStorage;
import com.player.util.ScreenStyles;
import com.player.util.SystemLog;

/**
 * BluetoothConnectionService : This service class is wrapper for Bluetooth communication and one point of contact
 * 								for the interaction between Player applications and Port.
 *
 * @Version : 1.0
 * @Author  : Lukup
 */

public class BluetoothConnectionService extends Service {
	// Debugging
	private static final String TAG = "BTService";

	// Name for the SDP record when creating server socket
	private static final String NAME = "LukupPlayer";

	// Unique UUID for this application
//	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	private static final UUID MY_UUID_SECURE =
            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
                             
	// Member fields
	private BluetoothAdapter mAdapter;
	private AcceptThread mAcceptThread;
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;
	public static int mState;
//	private int tracker;
	
	//Bonded BT Address
	public static String address="";
	private boolean currentlybound = false;

	// Constants that indicate the current connection state
	public static final int STATE_NONE = 0;       // we're doing nothing
	public static final int STATE_LISTEN = 1;     // now listening for incoming connections
	public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
	public static final int STATE_CONNECTED = 3;  // now connected to a remote device
	
	//Constants for Message processing between Player and this Service
//	public static final int MSG_START = 4;
//	public static final int MSG_CONNECT = 5;
//	public static final int MSG_WRITE = 6;
//	public static final int MSG_STOP = 7;
//	public static final int MSG_BIND = 8;			//Bind the remote device
//	public static final int MSG_UNBIND = 9;
	
	 /** Keeps track of all current registered clients. */
    ArrayList<Messenger> mClients = new ArrayList<Messenger>();
	
    private Handler mHandler;
//	// Messenger object used by clients to send messages to IncomingHandler
//    final Messenger mMessenger = new Messenger(new IncomingHandler());
//    
//    /**
//     * IncomingHandler : Incoming messages Handler
//     *
//     * @Version : 1.0
//     * @Author  : Lukup
//     */
//    class IncomingHandler extends Handler {
//    	
//	/**
//	 * Handler to process messages from Player
//	 * @param msg  Message to be handled
//	 */
//	@Override
//	public void handleMessage(Message msg) {
//		Bundle bundle;
//	    switch (msg.what) {
//	    	case MSG_START :
//				Log.d(TAG, "Handler MSG_START");
//				mState = STATE_NONE;
//	
//				//To Register the client with the Service
//				mClients.add(msg.replyTo);
//				break;
//			
//		case MSG_CONNECT :
//				Log.d(TAG, "handleMessage : Handler MSG_CONNECT and mState" + address + " " + mState);
//		        if(mState!=3 && mState != 2){ //if BT is not connected already, then connect
//		        	connect();
//		        }
//		        break;
//	    case MSG_WRITE:
//		        bundle = msg.getData();
//		        String message = (String) bundle.get("message");
//		        Log.d(TAG, "handleMessage : Handler MSG_WRITE" + message);
//		        write(message.getBytes());
//		        break;
//	    case MSG_STOP:
//	    		Log.d(TAG, "Before Handler MSG_STOP" + mClients.size());
//	    		mClients.remove(msg.replyTo);
//	    		Log.d(TAG, "After Handler MSG_STOP" + mClients.size());
//		    	stop();
////		    	if(mClients.size()==0){
//		    		if (Constant.DEBUG) Log.d(TAG, "handleMessage : MSG_STOP : Self Stopping Service ");
//		    		stopSelf(tracker);
////		    	}
//		        break;
//	    case MSG_BIND:
//		    	Log.d(TAG, "handleMessage : Handler MSG_BIND");
//		        bundle = msg.getData();
//		        String device = (String) bundle.get("device");
//		        bindDevice(device);
//		        break;
//	    case MSG_UNBIND:
//		    	Log.d(TAG, "handleMessage : Handler MSG_UNBIND");
//		        bundle = msg.getData();
//		        device = (String) bundle.get("device");
//		        currentlybound = bundle.getBoolean("currentlyBound", false);
//		        unbindDevice(device, currentlybound);
//		        break;
//	    default:
//	        super.handleMessage(msg);
//	        
//	    	}
//		}
//    }
// 
	/**
	 * Constructor for BluetoothConnectionService
	 */
    public BluetoothConnectionService() {
    	mAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mAdapter != null) {
			Log.d(TAG, "handleMessage : mAdapter is not null");
			if (!mAdapter.isEnabled()) {
				mAdapter.enable();
			}
		}
		mState = STATE_NONE;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
    	
    	Log.d(TAG, "onStartCommand called");
//    	tracker = startId;
    	super.onStartCommand(intent, flags, startId);
    	return START_STICKY;
    }
	
 
//	/**
//	 * Service OnCreate CallBack.
//	 */
//    @Override
//    public void onCreate() {
//        Log.d(TAG, "onCreate called");
//        super.onCreate();
//    }
    
    public final IBinder mBinder = (IBinder) new BTBinder();

	public class BTBinder extends Binder {
		public BluetoothConnectionService getService(Handler handler) {
            // Return this instance of LocalService so clients can call public methods
			mHandler = handler;
			return BluetoothConnectionService.this ;
        }
    }
 
    /**
    * Return our Messenger interface for sending messages to
    * the service by the clients.
    * @param intent incoming intent
    */
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind done");
        return mBinder;
//        return mMessenger.getBinder();
    }
 
    /**
    * Callback when binding with client breaks 
    * @param intent incoming intent
    */
    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }
    
    /**
    * bind to the remote device
    */
    public void bindDevice(String address)
    {
    	if(Constant.DEBUG)  Log.d(TAG, "Binding to device with address " + address);
    	this.address = address;
		BluetoothDevice device = mAdapter.getRemoteDevice(address);
		boolean returnValue=false;
		if(device != null){
			if (device.getBondState() != BluetoothDevice.BOND_BONDED){
				try{
					if(Constant.DEBUG)  Log.d(TAG, "Device is not bonded, going to bond");
					Method createBondMethod = device.getClass().getMethod("createBond");
					returnValue = (Boolean) createBondMethod.invoke(device);
					if(Constant.DEBUG) Log.d(TAG, "BONDING value "+returnValue);
				}catch (Exception e){
					if(Constant.DEBUG)  Log.d(TAG, "setPiN failed!");
					e.printStackTrace();
				} 
			} 
			if(Constant.DEBUG)  Log.d(TAG, "HAS BOND_BONDED");	
			
			if(mState!=STATE_CONNECTED){ //if BT is not connected already, then connect
				connect();
	        }
		}
    }
    
    public void unbindDevice(String address, boolean currentlyBound){
    	if(Constant.DEBUG)  Log.d(TAG, "UnBinding to device with address " + address);
		BluetoothDevice device = mAdapter.getRemoteDevice(address);
		boolean returnValue=false;
		if(device != null){
			if (device.getBondState() == BluetoothDevice.BOND_BONDED){
				try{
					if(Constant.DEBUG)  Log.d(TAG, "Going to unbind now");
					Method removeBondMethod = device.getClass().getMethod("removeBond");
					returnValue = (Boolean) removeBondMethod.invoke(device);
					if(Constant.DEBUG) Log.d(TAG, "Unbinding response "+returnValue);
					if(returnValue){
						if(mState!=STATE_NONE && currentlyBound){
							stop();
						}
					}
				}catch (Exception e){
					if(Constant.DEBUG)  Log.d(TAG, "Unbinding failed!");
					e.printStackTrace();
				} 
			}else{
				if(Constant.DEBUG)  Log.d(TAG, "Device to unbind is already unbound !");
			}
		}else{
			if(Constant.DEBUG)  Log.d(TAG, "Device to unbind is null !");
		}
    }
    
	/**
	 * Set the current state of the chat connection
	 * @param state  An integer defining the current connection state
	 */
	private synchronized void setState(int state) {
		if (Constant.DEBUG) Log.d(TAG, "setState() " + mState + " -> " + state);
		mState = state;
		try{
			// Give the new state to the Handler so the UI Activity can update
//			int position = mClients.size()-1;
//			if(mClients.get(position) != null)
//				mClients.get(position).send(Message.obtain(null,ScreenStyles.MESSAGE_STATE_CHANGE, state, 0));
			mHandler.obtainMessage(ScreenStyles.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
		}catch(Exception e){
			
		}
	}

	/**
	 * Return the current connection state. 
	 */
	public synchronized int getState() {
		return mState;
	}

	/**
	 * Start the chat service. Specifically start AcceptThread to begin a
	 * session in listening (server) mode. Called by the Activity onResume()  
	 */
	public synchronized void start() {
		if (Constant.DEBUG) Log.d(TAG, "start");
		try{
			// Cancel any thread attempting to make a connection
			if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
	
			// Cancel any thread currently running a connection
			if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
	
			// Start the thread to listen on a BluetoothServerSocket
			if (mAcceptThread == null) {
				mAcceptThread = new AcceptThread();
				mAcceptThread.start();
			}
			setState(STATE_LISTEN);
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
		}
	}

	/**
	 * Start the ConnectThread to initiate a connection to a remote device.
	 * @param device  The BluetoothDevice to connect
	 */
	public synchronized void connect() {
		if (Constant.DEBUG) Log.d(TAG, "connect to: " + address);
		try{
			BluetoothDevice device = mAdapter.getRemoteDevice(address);
			
			// Cancel any thread attempting to make a connection
			if (mState == STATE_CONNECTING) {
				if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
			}

			// Cancel any thread currently running a connection
			if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
			
//			if(mAcceptThread!=null){mAcceptThread.cancel(); mAcceptThread = null;}
			
			if (Constant.DEBUG) Log.d(TAG, "connect: getState()" + getState());
	
			// Start the thread to connect with the given device
			if (Constant.DEBUG) Log.d(TAG, "connect: getState() is satified starting connect thread");
			mConnectThread = new ConnectThread(device);
			mConnectThread.start();
			setState(STATE_CONNECTING);
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
		}
	}

	/**
	 * Start the ConnectedThread to begin managing a Bluetooth connection
	 * @param socket  The BluetoothSocket on which the connection was made
	 * @param device  The BluetoothDevice that has been connected
	 */
	public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
		if (Constant.DEBUG) Log.d(TAG, "connected");
		try{
			// Cancel the thread that completed the connection
			if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
	
			// Cancel any thread currently running a connection
			if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
	
			// Cancel the accept thread because we only want to connect to one device
			if (mAcceptThread != null) {mAcceptThread.cancel(); mAcceptThread = null;}
	
			// Start the thread to manage the connection and perform transmissions
			mConnectedThread = new ConnectedThread(socket);
			mConnectedThread.start();
			setState(STATE_CONNECTED);
			
			Player.mBTBound = true;
//			try{
//				// Give the new state to the Handler so the UI Activity can update
//				Message msg = Message.obtain(null, ScreenStyles.MESSAGE_DEVICE_NAME, 0, 0);
//				Bundle bundle = new Bundle();
//		        bundle.putString(ScreenStyles.DEVICE_NAME, device.getName());
//		        if(device != null)
//		        	Log.d(TAG, "device is valid instance");
//		        else
//		        	Log.d(TAG, "device is null");
//
//		        if (Constant.DEBUG) Log.d(TAG, "device getName" + device.getName());
//		        
//		        if(msg != null)
//		        {
//		        	msg.setData(bundle);
//		        	Log.d(TAG, "msg is valid instance");
//		        }
//		        else
//		        	Log.d(TAG, "msg is null");	
//		        
//		        int position = mClients.size()-1;
//		        if(mClients.get(position) != null)
//		        {
//		        	mClients.get(position).send(msg);
//		        	Log.d(TAG, "mHandler is valid instance");
//		        }
//		        else
//		        	Log.d(TAG, "mHandler is null");			        
//		       
//			}catch(RemoteException e){
//				
//			}
			
		}catch (Exception e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
		}
	}

	/**
	 * Stop all threads
	 */
	public synchronized void stop() {
		if (Constant.DEBUG) Log.d(TAG, "stop");
		if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
		if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
		if (mAcceptThread != null) {mAcceptThread.cancel(); mAcceptThread = null;}

		setState(STATE_NONE);
	}

	/**
	 * Write to the connected bluetooth socket
	 * @param out write to bluetooth socket
	 */
	synchronized public void write(byte[] out) {
		// Create temporary object
		ConnectedThread r;
		// Synchronize a copy of the ConnectedThread
		
		synchronized (this) {
			if (mState != STATE_CONNECTED) return;
			r = mConnectedThread;
		}
		if (Constant.DEBUG) Log.d(TAG, "write" + out.toString());
		// Perform the write unsynchronized
		r.write(out);
	}

	/**
	 * Indicate that the connection attempt failed and notify the UI Activity.
	 */
	private void connectionFailed() {
		// Send a failure message back to the Activity
		if (Constant.DEBUG) Log.d(TAG, "connectionFailed and sending MESSAGE_TOAST");
//		Message msg = Message.obtain(null,ScreenStyles.MESSAGE_TOAST,0,0);
//		Bundle bundle = new Bundle();
//		bundle.putString(ScreenStyles.TOAST, "Connection to Lukup Player failed.");
//		msg.setData(bundle);
//		try {
//			int position = mClients.size()-1;
//			if(mClients.get(position) != null)
//				mClients.get(position).send(msg);
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		mHandler.obtainMessage(ScreenStyles.MESSAGE_TOAST, STATE_NONE, -1).sendToTarget();
		stop();
		connect();
	}

	/**
	 * Indicate that the connection was lost and notify the UI Activity.
	 */
	private void connectionLost() {
		// Send a failure message back to the Activity
		if (Constant.DEBUG) Log.d(TAG, "connectionLost and sending MESSAGE_TOAST");
//		Message msg = Message.obtain(null,ScreenStyles.MESSAGE_TOAST,0,0);
//		Bundle bundle = new Bundle();
//		bundle.putString(ScreenStyles.TOAST, "Device connection was lost");
//		msg.setData(bundle);
//		try {
//			int position = mClients.size()-1;
//			if(mClients.get(position) != null)
//				mClients.get(position).send(msg);
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		mHandler.obtainMessage(ScreenStyles.MESSAGE_TOAST, STATE_NONE, -1).sendToTarget();
		stop();
		connect();
	}
	 
    /**
     * AcceptThread : This thread runs while listening for incoming connections. 
     * It behaves like a server-side client. 
     * It runs until a connection is accepted (or until cancelled).
     *
     * @Version : 1.0
     * @Author  : Lukup
     */
	private class AcceptThread extends Thread {
		// The local server socket
		private final BluetoothServerSocket mmServerSocket;

		/**
		 * Constructor
		 */
		public AcceptThread() {
			BluetoothServerSocket tmp = null;
			// Create a new listening server socket
			try {
//				if(DataStorage.getDeviceType().equalsIgnoreCase("Lukup Player X1")){
//					tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME, MY_UUID);
//				}else{
					tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME,  MY_UUID_SECURE);
//				}
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
			}
			 mmServerSocket = tmp;
		}

		/**
		 * Start Accept Thread
		 */
		@Override
		public void run() {
			if (Constant.DEBUG) Log.d(TAG, "BEGIN mAcceptThread" + this);
			setName("AcceptThread");
			BluetoothSocket socket = null;
			
			// Listen to the server socket if we're not connected
			while (mState != STATE_CONNECTED) {
				try {
					// This is a blocking call and will only return on a
					// successful connection or an exception
					socket = mmServerSocket.accept();
				} catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
					break;
				}

				// If a connection was accepted
				if (socket != null) {
					synchronized (BluetoothConnectionService.this) {
						switch (mState) {
						case STATE_LISTEN:
						case STATE_CONNECTING:
							// Situation normal. Start the connected thread.
							connected(socket, socket.getRemoteDevice());
							break;
						case STATE_NONE:
						case STATE_CONNECTED:
							// Either not ready or already connected. Terminate new socket.
							try {
								socket.close();
							} catch (Exception e) {
								e.printStackTrace();
								StringWriter errors = new StringWriter();
								e.printStackTrace(new PrintWriter(errors));
								SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
								Log.e(TAG, "Could not close unwanted socket", e);
							}
							break;
						}
					}
				}
			}
			if (Constant.DEBUG) Log.i(TAG, "END mAcceptThread");
		}

		/**
		 * Close the socket
		 */
		public void cancel() {
			if (Constant.DEBUG) Log.d(TAG, "cancel " + this);
			try {
				mmServerSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
				Log.e(TAG, "close() of server failed", e);
			}
		}
	}

    /**
     * ConnectThread : This thread runs while attempting to make an outgoing connection 
	 * with a device. It runs straight through; the connection either
	 * succeeds or fails.
     *
     * @Version : 1.0
     * @Author  : Lukup
     */

	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket; 
		private final BluetoothDevice mmDevice;

		/**
		 * Constructor
		 */
		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
			BluetoothSocket tmp = null;
			if (Constant.DEBUG) Log.i(TAG, "Connecting using secure rf comm with device : "+ device.getAddress());
			try {
//				if(DataStorage.getDeviceType().equalsIgnoreCase("Lukup Player X1")){
//					tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
//					if (Constant.DEBUG) Log.i(TAG, "Connecting using insecure rf comm since device type is : "+DataStorage.getDeviceType());
//				}else{
//					tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID_SECURE);
					tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
//				}
				//Method m = device.getClass().getMethod("createInsecureRfcommSocket", new Class[]{int.class});
				//tmp = (BluetoothSocket) m.invoke(device, 1);
			}catch (Exception e) {
				e.printStackTrace();
				connectionFailed();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
			}
			mmSocket = tmp;
		}

		/**
		 * Start Connect Thread
		 */
		@Override
		public void run() {
			Log.i(TAG, "BEGIN mConnectThread");
			setName("ConnectThread");

			if(Constant.DEBUG)  Log.d(TAG , "Cancelling discovery");
			mAdapter.cancelDiscovery();
			
			if(mmSocket!=null){
				// Make a connection to the BluetoothSocket
				try {
					// This is a blocking call and will only return on a
					// successful connection or an exception

					mmSocket.connect();
					
				} catch (IOException e1) {
					// Close the socket
					try {
						mmSocket.close();
					} catch (Exception e) {
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
						Log.e(TAG, "unable to close() socket during connection failure", e);
					}
					e1.printStackTrace();
					StringWriter errors = new StringWriter();
					e1.printStackTrace(new PrintWriter(errors));
					Log.e(TAG, "unable to connect", e1);
					return;
				}
			}

			// Reset the ConnectThread because we're done
			synchronized (BluetoothConnectionService.this) {
				mConnectThread = null;
			}

			// Start the connected thread
			connected(mmSocket, mmDevice);
		}

		/**
		 * Closing the socket
		 * 
		 */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}

    /**
     * ConnectedThread : This thread runs during a connection with a remote device. 
	 * It handles all incoming and outgoing transmissions.
     *
     * @Version : 1.0
     * @Author  : Lukup
     */
	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket; 
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
		private boolean isConnected = false;
		
		/**
		 * Constructor
		 */
		public ConnectedThread(BluetoothSocket socket) {
			Log.d(TAG, "create ConnectedThread");
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;
			
			// Get the BluetoothSocket input and output streams
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
				Log.e(TAG, "temp sockets not created", e);
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
			
//			if(mmInStream!=null){
//				isConnected = true;
//			}
		}

		/**
		 * Start Connect Thread
		 */
		@Override
		public void run() {
			Log.i(TAG, "BEGIN mConnectedThread");
			
			// Keep listening to the InputStream while connected
//			while (isConnected) {
				Log.i(TAG, "BEGIN mConnectedThread");				
				setState(STATE_CONNECTED);
				
				// Keep listening to the InputStream while connected
				while (true) {
					try {
						byte[] buffer = new byte[1024*8];
						int bytes;
						Log.i(TAG, "About to read from Input Streaming Waiting");
						// Read from the InputStream
						bytes = mmInStream.read(buffer);
						Log.i(TAG, "Reading bytes " + bytes);
						if(bytes !=0){
							// Send the obtained bytes to the UI Activity	
							Bundle b = new Bundle();
							b.putByteArray("portmsg", buffer);
							mHandler.obtainMessage(ScreenStyles.MESSAGE_READ, bytes, -1, b).sendToTarget();

//							int position = mClients.size()-1;
//							if(mClients.get(position) != null)
//								mClients.get(position).send(Message.obtain(null,ScreenStyles.MESSAGE_READ, bytes, -1, b));
						} else {
							break;
						}
						
					} catch (Exception e) {
						Log.e(TAG, "disconnected", e);
						connectionLost();
						isConnected = false;
						e.printStackTrace();
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
						
						break;
					}
				}
				Log.i(TAG, "Sending to reinit BTMessage");
				Player.readBTMessage = "";
//			}
		}

		/**
		 * Write to the connected OutStream.
		 * @param buffer  The bytes to write
		 */
		synchronized public void write(byte[] buffer) {
			Log.i(TAG, "Sending message from Connected thread " + buffer.toString());
			try {
				Player.readBTMessage = "";
				mmOutStream.write(buffer);
				mmOutStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
				Log.e(TAG, "Exception during write", e);
			}
		}

		/**
		 * Closing the socket
		 * 
		 */
		public void cancel() {
			try {
				//Closing input and output socket streams Sudharsan R
				mmSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}	
	
}