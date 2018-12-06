package com.player.action;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmErrorEvent;
import android.drm.DrmEvent;
import android.drm.DrmInfoEvent;
import android.drm.DrmInfoRequest;
import android.drm.DrmManagerClient;
import android.drm.DrmStore;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.player.R;
import com.player.Layout;
import com.player.exoplayer.LukupPlayer;
import com.player.util.Constant;
import com.player.util.SystemLog;

public class Play extends Layout implements OnCompletionListener{

	public DrmManagerClient drmManager = null;
	private VideoView videoView;
	private Activity activity;
	private MediaController media_Controller;
	
	private String TAG = "Play";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		
		if(Constant.DEBUG)  Log.d(TAG,"onCreate()");
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
    	activity = this;
		
    	setContentView(R.layout.videoviewer);
		
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		if(Constant.DEBUG)  Log.d(TAG,"onNewIntent()");
		if(progressDialog != null && progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		
		progressDialog = new ProgressDialog(this,R.style.MyTheme);
		progressDialog.setCancelable(true);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
		progressDialog.show();
		
		videoView = (VideoView)findViewById(R.id.videoPlayer);
		videoView.setOnCompletionListener(this);
		
		Bundle extras = this.getIntent().getExtras();
		String url="";
		String type="";
		if (extras != null) {
			if(extras.containsKey("type")){
	    		type = extras.getString("type"); 
	    		if(Constant.DEBUG) Log.w(TAG, "type " + type);
	    	}
			if(extras.containsKey("url")){
	    		url = extras.getString("url"); 
	    		if(Constant.DEBUG) Log.w(TAG, "url " + url);
	    	}
		}
		
		View view = activity.getCurrentFocus();
		if (view != null) {  
		    InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
		if(url.contains(".wvm")){
			if(Constant.DEBUG) Log.w(TAG, "playing in widevine player " + url);
			new DrmManagerImpl(url).execute();
		}else if(url.contains(".mp4") || url.contains(".m3u8")) {
			if(Constant.DEBUG) Log.w(TAG, "playing in android player " + url);
			playVideoData(url);
		}
	}
	
	/* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
	protected void onPause() {
		super.onPause();
		if(Constant.DEBUG) Log.w(TAG, "onPause()");
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
    	super.onResume();
    	if(Constant.DEBUG) Log.w(TAG, "onResume()");
    };
    
    /* (non-Javadoc)
     * @see com.port.api.interactive.iView#onDestroy()
     */
    @Override
	public void onDestroy() {
    	super.onDestroy();
    }
    
    @Override
	public void finish() {
    	super.finish();
    }
	
	@Override
	public void onCompletion(MediaPlayer mp) {
			
		videoView.stopPlayback();
		videoView.setVisibility(View.INVISIBLE);
		finish();
	}
	
	
	public class DrmManagerImpl extends AsyncTask<String, String, String> implements DrmManagerClient.OnEventListener,
	DrmManagerClient.OnInfoListener, DrmManagerClient.OnErrorListener {
	
		/** The Constant TAG. */
		private static final String TAG = "DRMVideo";
		
		/** The url_link. */
		private String url_link;
	
		/**
		 * Instantiates a new drm manager impl.
		 *
		 * @param url the url
		 */
		public DrmManagerImpl(String url) {
			url_link  = url.replace("http", "widevine");
		//	url_link = url;
			drmManager = new DrmManagerClient(activity);
			if(Constant.DEBUG) Log.e(TAG, "Inside DrmManagerImpl url"+url_link);
		}
			
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected String doInBackground(String... params) {
			if (drmManager == null) {
				drmManager = new DrmManagerClient(activity);
			}
			startDrmRegister();
			return url_link;
		}
	
		/**
		 * Start drm register.
		 */
		@SuppressLint("NewApi")
		private void startDrmRegister() {
			if (drmManager != null) {
				if(Constant.DEBUG) Log.d(TAG, "Drm Manager Set Listener called");
				drmManager.setOnErrorListener(this);
				drmManager.setOnEventListener(this);
				drmManager.setOnInfoListener(this);
				DrmInfoRequest drmRequest = new DrmInfoRequest(DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_INFO,"video/wvm");
				// Setup drm info object
				String cgiProto = "http://license.lukup.com/widevine/cypherpc/cgi-bin/GetEMMs.cgi";
								
				if(Constant.DEBUG) Log.d(TAG, "cgiProto : "+cgiProto);
				drmRequest.put("WVDRMServerKey", cgiProto);
				drmRequest.put("WVAssetURIKey", url_link);
				drmRequest.put("WVDeviceIDKey", "device1");
				drmRequest.put("WVPortalKey", "lukup");
				// Request license
				drmManager.acquireRights(drmRequest);
			} else {
				if(Constant.DEBUG)  Log.d(TAG, "Drm Manager Null");
			}
		}
	
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		protected void onPostExecute(String obj) {
			super.onPostExecute(obj);
			
			playEvent();
		}
		
		/**
		 * Play event.
		 */
		private void playEvent() {
			try {
				if(Constant.DEBUG) Log.e(TAG, "Inside playEvent url"+url_link);
				videoView.setVisibility(View.VISIBLE);
				Uri mMediaUri = Uri.parse(url_link);
				videoView.setVideoURI(mMediaUri);
				videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer pMp) {
						try{
							
						}catch(Exception e){
							e.printStackTrace();
							StringWriter errors = new StringWriter();
							e.printStackTrace(new PrintWriter(errors));
						}
						if(progressDialog != null && progressDialog.isShowing()){
							progressDialog.dismiss();
						}

						videoView.start();
					}
				});
				
				// View DRM License Information
				if (drmManager != null) {
					ContentValues values = drmManager.getConstraints(url_link,DrmStore.Action.PLAY);
					if (values != null) {
						Set<String> keys = values.keySet();
						StringBuilder builder = new StringBuilder();
						for (String key : keys) {
							builder.append(key);
							builder.append(" = ");
							builder.append(values.get(key));
							builder.append("\n");
						}
						if(Constant.DEBUG)  Log.d(TAG,"DRM License Information : "+ builder.toString());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
			}
		}
	
		/* (non-Javadoc)
		 * @see android.drm.DrmManagerClient.OnErrorListener#onError(android.drm.DrmManagerClient, android.drm.DrmErrorEvent)
		 */
		@Override
		public void onError(DrmManagerClient client, DrmErrorEvent event) {
			if(Constant.DEBUG)  Log.d(TAG, "DRM Error Code " + event.getUniqueId() + " Message  " + event.getMessage() + " , Type  " + event.getType());
			switch (event.getType()) {
			case DrmErrorEvent.TYPE_ACQUIRE_DRM_INFO_FAILED:
				if(Constant.DEBUG)  Log.d(TAG,"DRM TYPE_ACQUIRE_DRM_INFO_FAILED");
				break;
				
			case DrmErrorEvent.TYPE_DRM_INFO_PROCESSED:
				if(Constant.DEBUG)  Log.d(TAG, "DRM TYPE_DRM_INFO_PROCESSED");
				break;
				
			case DrmErrorEvent.TYPE_NOT_SUPPORTED:
				if(Constant.DEBUG)  Log.d(TAG, "DRM TYPE_NOT_SUPPORTED");
				break;
				
			case DrmErrorEvent.TYPE_NO_INTERNET_CONNECTION:
				if(Constant.DEBUG)  Log.d(TAG,"DRM TYPE_NO_INTERNET_CONNECTION");
				break;
			}
		}
	
		/* (non-Javadoc)
		 * @see android.drm.DrmManagerClient.OnInfoListener#onInfo(android.drm.DrmManagerClient, android.drm.DrmInfoEvent)
		 */
		@Override
		public void onInfo(DrmManagerClient client, DrmInfoEvent event) {
			if(Constant.DEBUG)  Log.d(TAG, "DRM Info Code  " + event.getUniqueId() + " Message  "
					+ event.getMessage() + " , Type  " + event.getType());
			switch (event.getType()) {
			case DrmInfoEvent.TYPE_RIGHTS_INSTALLED:
				if(Constant.DEBUG)  Log.d(TAG,"Rights Installed");
				break;
			case DrmInfoEvent.TYPE_RIGHTS_REMOVED:
				if(Constant.DEBUG)  Log.d(TAG,"Rights Removed");
				break;
			}
		}
	
		/* (non-Javadoc)
		 * @see android.drm.DrmManagerClient.OnEventListener#onEvent(android.drm.DrmManagerClient, android.drm.DrmEvent)
		 */
		@Override
		public void onEvent(DrmManagerClient client, DrmEvent event) {
			if(Constant.DEBUG)  Log.d(TAG,"DRM On Event Code  " + event.getUniqueId() + " Message  " + event.getMessage() + " , Type  " + event.getType());
			switch (event.getType()) {
			case DrmEvent.TYPE_DRM_INFO_PROCESSED:
				if(Constant.DEBUG)  Log.d(TAG,"DRM Info Processed");
				break;
			case DrmEvent.TYPE_ALL_RIGHTS_REMOVED:
				if(Constant.DEBUG)  Log.d(TAG,"All Rights Removed");
				break;
			}
		}
	}
	
	
	public void playVideoData(final String mMediaUri){
		if(Constant.DEBUG)  Log.d("Play", "playVideoData mMediaUri: "+mMediaUri);
		try{
				if(Constant.DEBUG)  Log.i("Play", "Play =============");
				if(activity != null ){
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
//							media_Controller = new MediaController(activity);
							
							if(videoView.isPlaying()){
								videoView.stopPlayback();
							}
							
							videoView.setVisibility(View.VISIBLE);
							videoView.setVideoURI(Uri.parse(mMediaUri));
//							videoView.setMediaController(media_Controller);
							videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
								@SuppressWarnings("deprecation")
								@Override
								public void onPrepared(MediaPlayer pMp) {
									try{
										videoView.requestFocus();
										videoView.start();
										if(Constant.DEBUG)  Log.d("Play", "playVideoData: playing =========");
										if(videoView.isPlaying()){
											if(progressDialog != null && progressDialog.isShowing()){
												progressDialog.dismiss();
											}
										}
									}catch(Exception e){
										e.printStackTrace();
										StringWriter errors = new StringWriter();
										e.printStackTrace(new PrintWriter(errors));
										SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_PLAYBACK, errors.toString(), e.getMessage());
									}		
									if(Constant.DEBUG)  Log.d("Play", "playVideoData: "+videoView.isPlaying());
								}
							});
						}
					});
				}
			
		}catch(Exception e){
			e.printStackTrace();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			SystemLog.createErrorLogXml(SystemLog.TYPE_PLAYER,SystemLog.LOG_PLAYBACK, errors.toString(), e.getMessage());
		}
	}
}
