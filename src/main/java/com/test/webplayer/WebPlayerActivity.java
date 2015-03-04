package com.test.webplayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.VideoView;

public class WebPlayerActivity extends Activity {
	final String TAG = "TAG";
	final String KEY_SCRIPT_INTERFACE_NAME = "android";
	WebView webView = null;
	FrameLayout frameLayout = null;
	LinearLayout layoutPlayer = null;
	VideoView videoView = null;
	FrameLayout webFrame = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflator = getLayoutInflater();
		View inflatedView = inflator.inflate(R.layout.webview, null);

		if (!(inflatedView instanceof FrameLayout)) {
			throw new RuntimeException("inflated view not FrameLayout");
		} else {
			frameLayout = (FrameLayout) inflatedView;
		}

		setContentView(frameLayout);

		Intent i = this.getIntent();
		String StartUrl = i.getStringExtra("StartUrl");
		
		layoutPlayer = (LinearLayout) findViewById(R.id.layout_player);
		webView = (WebView) findViewById(R.id.webView);
		WebSettings ws = webView.getSettings();
//		ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
		ws.setJavaScriptEnabled(true);
		ws.setDomStorageEnabled(true);
		webView.clearCache(true);
		ws.setPluginState(WebSettings.PluginState.ON);
		webView.addJavascriptInterface(new ScriptInterface(), KEY_SCRIPT_INTERFACE_NAME);
		webView.setWebChromeClient(new MyWebChromeClient());
		webView.setWebViewClient(new CustomWebViewClient());
//		webView.loadUrl("http://192.168.10.200:8430/demo/demo-4.html");
//		webView.loadUrl("http://192.168.10.200:8430/demo/jwdemo.html");
//		webView.loadUrl("http://192.168.10.200:8888/e_pr/index.html");
		Log.d(":::::>>>>" , StartUrl);
		webView.loadUrl(StartUrl);

//		InputStream inputStream = getResources().openRawResource(R.raw.index);
//		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//		int readByte;
//
//		try {
//			while ((readByte = inputStream.read()) != -1) {
//				outStream.write(readByte);
//			}
//
//			String html = outStream.toString("UTF8");
//
//			webView.loadDataWithBaseURL("http://localhost/index.html", html,
//					"text/html", "utf-8", "http://localhost/index.html");
//		} catch (Exception e) {
//			throw new RuntimeException();
//		}
	}

	@Override
	public void onBackPressed() {
		if( webView.canGoBack() )
			webView.goBack();
		else
			super.onBackPressed();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.i("ScriptInterface", "onStop 이다!!");
		videoDestroy();
		super.onStop();
	}

	boolean isRepeat = false;
	public final class ScriptInterface {
		public void videoStop() {
			Log.i("ScriptInterface", " videoStop : " );//+ visibility);
			if( videoView == null )
				return;
			videoView.stopPlayback();
		}

		public void videoDestroy() {
			Log.i("ScriptInterface", " videoDestroy : " );//+ visibility);
			if( handle == null )
				return;

			handle.sendEmptyMessage(VIDEO_DESTROY);
		}

		public void videoSeekTo(int msec) {
			Log.i("ScriptInterface", " videoSeekTo : " + msec);
			if( videoView == null )
				return;

			Log.i("ScriptInterface", " videoSeekTo : " + msec);
			videoView.seekTo(msec);
		}
		
		public void videoPlay(boolean repeat) {
			Log.i("ScriptInterface", " videoPlay : " + repeat);
			isRepeat = repeat;
		}
		
		public void playerVisibility() {
			Log.i("ScriptInterface", " playerVisibility : " );//+ visibility);
			if( handle == null )
				return;

			handle.sendEmptyMessage(PLAYER_VISIVILITY);
		}

		public void playerCurrentPosition() {
			int time = videoView == null || !videoView.isPlaying() ? 0 : videoView.getCurrentPosition()/1000;
			Log.i("ScriptInterface", " playerCurrentTime : " + time);//+ visibility);
			webView.loadUrl("javascript:setAnPlayerCurrentTime(" + time + ");");
		}
		
		public void setTime(int msec) {
			if( videoView == null )
				return;

			Log.i("ScriptInterface", " setTime : " + msec);
			videoView.seekTo(msec);
		}
	}

	final int VIDEO_DESTROY = 0x2000;
	final int PLAYER_VISIVILITY = 0x1000;
	Handler handle = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case PLAYER_VISIVILITY:
				setPlayerVisibility();
				break;
			case VIDEO_DESTROY:
				videoDestroy();
				break;
			}
		}
	};
	
	private void videoDestroy() {
		if( videoView != null ) {
			if( videoView.isPlaying() )
				videoView.stopPlayback();
			layoutPlayer.removeView(videoView);
			if( webFrame != null )
				webFrame.addView(videoView);
			videoView = null;
			customViewCallback.onCustomViewHidden();
		}
		webView.loadUrl("javascript:endPlay();");
	}
 
	private void setPlayerVisibility() {
		if( videoView == null )
			return;

//		if (visibility.equals("GONE")) {
			if (videoView.isPlaying()) {
				videoView.pause();
				layoutPlayer.setVisibility(View.INVISIBLE);
//				webView.requestFocus();
				Log.i("ScriptInterface", " pause");
//		} else if (visibility.equals("VISIBLE")) {
			} else {
				layoutPlayer.setVisibility(View.VISIBLE);
//				videoView.requestFocus();
				videoView.start();
				Log.i("ScriptInterface", " start");
		}
	}

	WebChromeClient.CustomViewCallback customViewCallback;
	
	private class MyWebChromeClient extends WebChromeClient implements
			MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
			MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener {


//		public void onProgressChanged(WebView view, int newProgress) {
//			if (newProgress == 50) {
////				view.loadUrl("javascript:playVideo()");
//			}
//		}

		public void onShowCustomView(View view,
				WebChromeClient.CustomViewCallback callback) {
//			customViewCallback = callback;

			Log.i("ScriptInterface","onShowCustomView");
			if (view instanceof FrameLayout) {
				customViewCallback = callback;
				webFrame = (FrameLayout) view;

				if (webFrame.getFocusedChild() instanceof VideoView) {
					videoView = (VideoView) webFrame.getFocusedChild();
//					videoView.layout(50, 50, 100, 100);
					
// hide the video controls
					videoView.setMediaController(null);
// remove videoView from MediaPlayer and ad it to the content view
					webFrame.removeView(videoView);
					layoutPlayer.addView(videoView,
							ViewGroup.LayoutParams.WRAP_CONTENT);
					videoView.setOnCompletionListener(this);
					videoView.setOnErrorListener(this);
					videoView.setOnPreparedListener(this);
					videoView.start();
				}
			}
		}

		public void onCompletion(MediaPlayer mp) {
			if( isRepeat ) {
				mp.setLooping(true);
				videoView.start();
			}
// this is needed to release the MediaPlayer and its resources so it can be used again later
//			videoView.stopPlayback();

// now remove the video and tell the callback to hide the custom view
//			layoutPlayer.removeView(videoView);
//			if( webFrame != null )
//				webFrame.addView(videoView);
//			videoView = null;
//			webView.loadUrl("javascript:endPlay();"); 
//			customViewCallback.onCustomViewHidden();
			// finish();
		}

		public boolean onError(MediaPlayer mp, int what, int extra) {
//			layoutPlayer.removeView(videoView);
//			if( webFrame != null )
//				webFrame.addView(videoView);
//			videoView = null;
//			customViewCallback.onCustomViewHidden();
//			webView.loadUrl("javascript:endPlay();");
			videoDestroy();
			// we did not handle the error - onCompletion will be called
			return false;
		}
		

		public void onPrepared(MediaPlayer mp) {
			mp.setOnVideoSizeChangedListener(this);
			
			videoView.setLayoutParams(new LayoutParams(mp.getVideoWidth()/2, mp.getVideoHeight()/2));
		}

		@Override
		public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
			Log.i("ScriptInterface", "onVideoSizeChanged : " + width + " : " + height);
			LayoutParams lp = new LayoutParams(
					width,
					height);
//			videoView.layout(100, 100, 0, 0);
			videoView.setLayoutParams(lp);

		}
	}

	private class CustomWebViewClient extends WebViewClient {
		public void onPageFinished(WebView view, String url) {
			Log.i("ScriptInterface", "onPageFinished:" + url);
			super.onPageFinished(view, url);

			// js load 함수
//			String js = "'quiz_js/quiz1.js'";
//			String function = "loadJs(" + js + ");";
//			view.loadUrl("javascript:" + function);
		}

		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i("ScriptInterface","shouldOverrideUrlLoading:" + url);
			return super.shouldOverrideUrlLoading(view, url);
		}

		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.i("ScriptInterface","onPageStarted:" + url);
			super.onPageStarted(view, url, favicon);
			isRepeat = false;
			
			// js load 함수
//			String js = "'quiz_js/quiz1.js'";
//			String function = "loadJs(" + js + ");";
//			view.loadUrl("javascript:" + function);
		}
	}
}
