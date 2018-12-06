package com.player.action;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.player.Layout;
import com.player.Player;
import com.player.R;
import com.player.util.CommonUtil;
import com.player.util.Constant;
import com.player.util.SystemLog;
import com.player.widget.HelpText;

public class Download {
	private static final String TAG = "Download";
	
	public static boolean unpackZip(String path, String zipname){      
	    InputStream is;
	    ZipInputStream zis;
	    try {
	        String filename;
	        is = new FileInputStream(new File(Player.getContext().getFilesDir(),zipname).getAbsoluteFile());
	        zis = new ZipInputStream(new BufferedInputStream(is));          
	        
	        ZipEntry ze;
	        byte[] buffer = new byte[1024];
	        int count;
	
	        while ((ze = zis.getNextEntry()) != null){
	            filename = ze.getName();
	            if(Constant.DEBUG)  Log.d(TAG,"File unzipped " + filename);
	            if (ze.isDirectory()) {
	               File fmd = new File(path + filename);
	               fmd.mkdirs();
	               continue;
	            }
	
	            FileOutputStream fout = new FileOutputStream(path + filename);
	            while ((count = zis.read(buffer)) != -1){
	                fout.write(buffer, 0, count);             
	            }
	            fout.close();               
	            zis.closeEntry();
	        }
	        zis.close();
	    } 
	    catch(IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	   return true;
	}
	
	
	public static boolean DownloadFile(String Url,String name){
		int count;
		if(CommonUtil.isNetworkConnected(Player.getContext())){
			try {
				URL url = new URL(Url);
				URLConnection conexion = url.openConnection();
				conexion.connect();
	
				InputStream input = new BufferedInputStream(url.openStream());
				FileOutputStream filetowrite = Player.getContext().openFileOutput(name, Player.getContext().MODE_WORLD_READABLE);
							
				byte data[] = new byte[1024];
				long total = 0;
				while ((count = input.read(data)) != -1) {
					total += count;
					filetowrite.write(data, 0, count);
				}
	
				filetowrite.flush();
				filetowrite.close();
				input.close();
				
			} catch (Exception e) {
	    		e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_WEBSERVICE, errors.toString(), e.getMessage());
				return false;
			}
			
			return true;
		}else{
			return false;
		}
	}
	
	public class DataFetch extends AsyncTask<String, String, Boolean>{

    	String dataUrl;
    	String c;
    	
    	public DataFetch(String url, String name) {
    		dataUrl = url;
    		c = name;
    	}
    	
		protected void onPreExecute() {

		}

		@Override
		protected Boolean doInBackground(String... params) {
			
			if(DownloadFile(dataUrl,c)){
				return true;
			}else{
				return false;
			}
		}
	}

	
}
