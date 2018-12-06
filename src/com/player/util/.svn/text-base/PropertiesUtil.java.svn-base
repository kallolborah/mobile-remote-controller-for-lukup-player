/*
 * Copyright (c) Lukup Media Pvt Limited, India.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Lukup Media Pvt Limited ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms
 * of the licence agreement you entered into with Lukup Media Pvt Limited.
 *
 */
package com.player.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Properties;

import com.player.R;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Environment;

/**
 * static class to store the data into the properties file
 * 
 * @author Yuvaraja
 * 
 */
public class PropertiesUtil {

	static String extStorageDirectory = Environment.getExternalStorageDirectory().toString()+"/lukup";

	/**
	 * Get data to the application's Internal storage file
	 * @param activity - current activity
	 * @param filename - filename to load data
	 * @param key - key value 
	 * @return
	 */
	public static String getLocalFilePropertiesValue(Activity activity,String filename,String key){
		File proFlie=new File(activity.getFilesDir(),filename);
		Properties mProperties=new Properties();
		if(proFlie.exists()){
			try{
				Runtime.getRuntime().exec("chmod 777 "+proFlie);
				//				FileInputStream stream=new FileInputStream(proFlie);

				FileInputStream stream = (FileInputStream) activity.getResources().openRawResource(R.raw.lukupproperties);
				mProperties.load(stream);
				Enumeration em=mProperties.keys();
				while(em.hasMoreElements()){
					String str = (String)em.nextElement();
					if(str.equalsIgnoreCase(key)){
						if(mProperties.get(str) != null){
							stream.close();
							return (String) mProperties.get(str);
						}
					}
				}
				stream.close();
			}catch (FileNotFoundException e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
			}
		}
		return null;
	}
	/**
	 * Store data to the application's Internal storage file
	 * @param properties
	 * @param activity
	 * @param filename
	 */
	public static void storeOnLocalFile(Properties properties,Activity activity,String filename){
		Enumeration enm=null;
		enm=properties.keys();
		File proFlie=new File(activity.getFilesDir(),filename);
		try {

			if(!proFlie.exists()){
				proFlie.createNewFile();
			}
			Runtime.getRuntime().exec("chmod 777 "+proFlie);
			Properties propertyStore=new Properties();
			FileInputStream stream=new FileInputStream(proFlie);
			propertyStore.load(stream);
			enm = properties.keys();
			while(enm.hasMoreElements()){
				String str = (String)enm.nextElement();
				if(properties.get(str) != null){
					propertyStore.remove(str);
				}
			}
			stream.close();

			FileOutputStream outstream = new FileOutputStream(proFlie);
			propertyStore.store(outstream, null);
			outstream.close();

			if(properties != null && propertyStore != null){
				FileWriter fw = new FileWriter(proFlie,true); 
				enm = properties.keys();
				while(enm.hasMoreElements()){
					String str = (String)enm.nextElement();
					String value=str + "=" + properties.get(str);
					fw.write("\n"+value);//appends the string to the file 
				}
				fw.close(); 
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
	}

	/**
	 * get all Properties from Localstorage 
	 * @param activity - current activity
	 * @param filename - file name to load
	 * @return
	 */
	public static Properties getLocalFileAllData(Activity activity,String filename){
		Properties pro=new Properties();
		InputStream is;
		File proFlie=new File(activity.getFilesDir(),filename);
		if(proFlie.exists()){
			try {
				FileInputStream stream = activity.openFileInput(filename);
				pro.load(stream);
				return pro;
			} catch (IOException e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
			}
		}
		return null;
	}

	public static String getPropertiesByValue(Activity activity,String value){
		Properties mProperties = new Properties();
		try{
		    
		    String readLine = null;
		    AssetManager mgr = activity.getBaseContext().getAssets();

//		    InputStream stream = (InputStream)mgr.open("lukupproperties.properties");
		    InputStream stream = activity.getAssets().open("lukupproperties.properties");
			mProperties .load(stream);
			Enumeration em=mProperties.keys();
			while(em.hasMoreElements()){
				
				String str = (String)em.nextElement();
				if(mProperties.get(str) != null){
					if(mProperties.get(str).toString().equalsIgnoreCase(value)){
						stream.close();
						return str;
					}
				}
			}
			stream.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_APPLICATION, errors.toString(), e.getMessage());
		}
		return null;

	}
}
