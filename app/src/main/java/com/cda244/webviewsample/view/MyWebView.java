package com.cda244.webviewsample.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.cda244.webviewsample.MyActivity;


public class MyWebView extends WebView {

	public MyWebView(Context context) {
		super(context);
	}

	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


	public void init( final ProgressBar progBar ) {
		setVerticalScrollbarOverlay(true);
		setHorizontalScrollbarOverlay(true);
		getSettings().setJavaScriptEnabled(true);
		getSettings().setDomStorageEnabled(true);
		getSettings().setAppCacheEnabled(true);
		getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		getSettings().setLoadWithOverviewMode(true);
		getSettings().setUseWideViewPort(true);

		initClient(progBar);
	}


	private void initClient( final ProgressBar progBar ) {
		WebViewClient viewClient = new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				progBar.setProgress(0);
				progBar.setVisibility(View.VISIBLE);
				requestFocus(View.FOCUS_DOWN);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				progBar.setVisibility(View.INVISIBLE);
				requestFocus(View.FOCUS_DOWN);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				progBar.setVisibility(View.INVISIBLE);
				((MyActivity) getContext()).showToast("ロードエラー\n" + failingUrl);
			}
		};
		setWebViewClient( viewClient );

		WebChromeClient chromeClient = new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {
				progBar.setProgress(progress);
			}
		};
		setWebChromeClient(chromeClient);
	}


}

/*
破棄
webView.stopLoading();
webView.setWebViewClient(null);
webView.setWebChromeClient(null);
webView.destroy();
webView = null;
 */