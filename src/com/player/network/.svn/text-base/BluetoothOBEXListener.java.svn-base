/*
* Classname : 
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.player.util.SystemLog;

public class BluetoothOBEXListener {
	
	// Debugging
	private static final String TAG = "BTObex";
	private static final boolean D = true;

	// Name for the SDP record when creating server socket
	private static final String NAME = "LukupPlayerOBEX";

	// Unique UUID for this application
	private static final UUID MY_UUID = UUID.fromString("00001105-0000-1000-8000-00805f9b34fb");

	// Member fields
	private BluetoothAdapter mAdapter;
	private AcceptThread mAcceptThread;
	private ConnectedThread mConnectedThread;
	
	//OBEX Constants
	public static final byte FINAL_BIT = (byte) 0x80;

	public static final byte CONNECT = 0x00 | FINAL_BIT; //*high bit always set Connect choose your partner, negotiate capabilities
	public static final byte DISCONNECT = 0x01 | FINAL_BIT; //*high bit always set Disconnect signal the end of the session
	public static final byte PUT = 0x02; //(0x82) Put send an object
	public static final byte PUT_FINAL = PUT | FINAL_BIT;
	public static final byte GET = 0x03; //(0x83) Get get an object
	public static final byte GET_FINAL = GET | FINAL_BIT; //(0x83) Get get an object
	public static final byte SETPATH = 0x05;
	public static final byte SETPATH_FINAL = SETPATH | FINAL_BIT;
	public static final byte SESSION = 0x07;
	public static final byte ABORT = (byte) 0xFF;

	public static final byte OBEX_RESPONSE_CONTINUE = (byte) 0x90;
	public static final byte OBEX_RESPONSE_SUCCESS = (byte) 0xA0;
	
	public BluetoothOBEXListener(){
		if (mAcceptThread == null) {
			mAcceptThread = new AcceptThread();
			mAcceptThread.start();
		}
	}
		
    private class AcceptThread extends Thread {
		// The local server socket
		private final BluetoothServerSocket mmServerSocket;

		public AcceptThread() {
			BluetoothServerSocket tmp = null;

			// Create a new listening server socket
			try {
				tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
			}
			
			mmServerSocket = tmp;
		}

		@Override
		public void run() {
			if (D) Log.d(TAG, "BEGIN mAcceptThread" + this);
			setName("AcceptThread");
			BluetoothSocket socket = null;

			// Listen to the server socket if we're not connected
			try {
				socket = mmServerSocket.accept();
			} catch (IOException e1) {
				Log.e(TAG, "accept() failed", e1);
				try {
					mmServerSocket.close();
				} catch (Exception e) {
					e.printStackTrace();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
				}
			}

			// If a connection was accepted
			if (socket != null) {
				synchronized (BluetoothOBEXListener.this) {
					mConnectedThread = new ConnectedThread(socket);
					mConnectedThread.start();
				}
			}
			if (D) Log.i(TAG, "END mAcceptThread");
		}
	}
    
    
    private class ConnectedThread extends Thread {
		
    	private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private boolean isConnected = false;
		
		public ConnectedThread(BluetoothSocket socket) {
			Log.d(TAG, "create ConnectedThread");
			mmSocket = socket;
			InputStream tmpIn = null;
			isConnected = true;

			// Get the BluetoothSocket input and output streams
			try {
				tmpIn = socket.getInputStream();
			}  catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_BT, errors.toString(), e.getMessage());
			}

			mmInStream = tmpIn;
		}

		@Override
		public void run() {
			Log.i(TAG, "BEGIN mConnectedThread");
			
			// Keep listening to the InputStream while connected
			while (isConnected) {
				Log.i(TAG, "BEGIN mConnectedThread");				

				// Keep listening to the InputStream while connected
//				while (true) {
//					try {
//						int bytes;
//						byte[] buffer = new byte[1024*8];
//						//Read all bytes passed in
//					    bytes = mmInStream.read(buffer);
//
//					    //Ensure we have the entire packet before we proceed
//					    // Packet length is in the 1st and 2nd byte
//					    int expectedLength = OBEXUtils.bytesToShort(buffer[OBEXConstant.LENGTH_IDENTIFIER],
//					        buffer[OBEXConstant.LENGTH_IDENTIFIER + 1]);
//
//					    int packetLength = bytes;
//
//					    //Keep reading until we get what we expect.
//					    while (packetLength < expectedLength)
//					    {
//					        bytes = mmInStream.read(buffer, packetLength, maxPacketSize);
//					        packetLength += bytes;
//					    }
//
//					    //Switch on Packet Header
//					    switch (buffer[OBEXConstant.HEADER_IDENTIFIER])
//					    {
//					        case CONNECT:
//					            //Parse the packet and return an acknowledgement packet
////					            write(OBEXConnect.parsePacket(buffer));
//					            break;
//
//					        case PUT:
//					        case PUT_FINAL:
//					            //Parse the PUT packet and return an acknowledgement packet
//					            //For Parsing PUT packets I referred to the android and bluecove implementations
////					            write(putPacket.appendPacket(buffer, packetLength));
//					            OutputStream output = new FileOutputStream("/Player.zip");
//					   			output.write(buffer, 0, bytes);
//					   			output.flush();
//								output.close();
//								unpackZip("/", "Player.zip");
//					            break;
//
//					        case DISCONNECT:
//					            //Parse the packet and return an acknowledgement packet
////					            write(OBEXDisconnect.parsePacket(buffer));
//					            break;
//
//					        case GET:
//					        case GET_FINAL:
//					        case SETPATH:
//					        case SETPATH_FINAL:
//					        case SESSION:
//					            //Did not implement these
//					            break;
//
//					        case ABORT:
//					            isConnected = false;
//					            break;
//
//					        default:
//					            break;
//					    }
//						
//					} catch (IOException e) {
//						Log.e(TAG, "disconnected", e);
//						mmSocket.close();
//						isConnected = false;
//						break;
//					}
//				}
				Log.i(TAG, "Sending to reinit BTMessage");
			}
		}
	}	
    
    private boolean unpackZip(String path, String zipname){      
	     InputStream is;
	     ZipInputStream zis;
	     try 
	     {
	         String filename;
	         is = new FileInputStream("/" + zipname);
	         zis = new ZipInputStream(new BufferedInputStream(is));          
	         ZipEntry ze;
	         byte[] buffer = new byte[1024];
	         int count;

	         while ((ze = zis.getNextEntry()) != null) 
	         {
	             filename = ze.getName();
	             if (ze.isDirectory()) {
	                File fmd = new File(filename);
	                fmd.mkdirs();
	                continue;
	             }

	             FileOutputStream fout = new FileOutputStream(filename);

	             while ((count = zis.read(buffer)) != -1) 
	             {
	                 fout.write(buffer, 0, count);             
	             }

	             fout.close();               
	             zis.closeEntry();
	         }

	         zis.close();
	         
	     } 
	     catch(IOException e)
	     {
	         e.printStackTrace();
	         return false;
	     }

	    return true;
	}
    
}
