package com.player.action;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.VideoSurfaceView;
import com.player.exoplayer.DashRendererBuilder;
import com.player.exoplayer.DemoUtil;
import com.player.exoplayer.LukupPlayer;
import com.player.exoplayer.UnsupportedDrmException;
import com.player.exoplayer.WidevineTestMediaDrmCallback;
import com.player.util.Constant;

public class ExoPlay implements  com.player.exoplayer.LukupPlayer.Listener ,SurfaceHolder.Callback {

	private VideoSurfaceView surfaceView;
	private Context context;
	private LukupPlayer exoplayer;
	
	public ExoPlay(VideoSurfaceView surfaceview , Context context) {
		this.surfaceView = surfaceview;
		this.context = context;
		if(Constant.DEBUG)  Log.d("ModularDRM" , "Playing in EXOPLAYER===="  );
		surfaceView.setVisibility(View.VISIBLE); 
	}	
	
	public void lukupExoPlayer(String sourceUrl){
		
		if (exoplayer != null) {
			exoplayer.release();
		}
		if(Constant.DEBUG)  Log.i("ModularDRM", "Player is Null");
		surfaceView.setVisibility(View.VISIBLE); 
		surfaceView.getHolder().addCallback(this);
		//videoView.setVisibility(View.INVISIBLE);
		surfaceView.setVisibility(View.VISIBLE);	
	    String userAgent = DemoUtil.getUserAgent(context);////http://ec2-174-129-78-19.compute-1.amazonaws.com/dark_knight/DARK_KNIGHT.mpd
		//"http://ec2-54-237-119-34.compute-1.amazonaws.com:8080/movie/dark_knight/DARK_KNIGHT.mpd"
	    exoplayer = new LukupPlayer(new DashRendererBuilder(userAgent,sourceUrl,"",new WidevineTestMediaDrmCallback(""), null)); 
		if(Constant.DEBUG)  Log.i("ModularDRM", sourceUrl);
		exoplayer.addListener(this);
		exoplayer.prepare();
		if(Constant.DEBUG)  Log.i("ModularDRM", "PreparePlayer ");
        exoplayer.setSurface(surfaceView.getHolder().getSurface());
	    exoplayer.setPlayWhenReady(true);
		if(Constant.DEBUG)  Log.i("ModularDRM", "Loading================Loading================Loading ");
		
	}
	
	@Override
	public void onStateChanged(boolean playWhenReady, int playbackState) {
		if (playbackState == ExoPlayer.STATE_ENDED) {
			exoplayer.release();
			surfaceView.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onError(Exception e) {
		 if (e instanceof UnsupportedDrmException) {
		      // Special case DRM failures.
		      UnsupportedDrmException unsupportedDrmException = (UnsupportedDrmException) e;
		      Toast.makeText(context, "Unable to play video", Toast.LENGTH_LONG).show();
		    }
		
	}

	@Override
	public void onVideoSizeChanged(int width, int height,
			float pixelWidthHeightRatio) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if(exoplayer != null)
			exoplayer.blockingClearSurface();
	}

}
