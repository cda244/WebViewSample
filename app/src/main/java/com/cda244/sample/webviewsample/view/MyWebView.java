package com.cda244.sample.webviewsample.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.cda244.sample.webviewsample.MyActivity;


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
		//addJavascriptInterface( new JSHandler(), "AndroidNative");
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
				Log.d("cda244", "ロード " + url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				progBar.setVisibility(View.INVISIBLE);
				requestFocus(View.FOCUS_DOWN);

				//loadUrl("javascript:hoge('ネイティブからの呼び出し');");
				//loadUrl("javascript:hoge('　ページ読み込み完了');");
				//loadUrl("javascript:hoge('　url: "+url+"');");
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

			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				//return super.onJsAlert(view, url, message, result);
				if (message.indexOf("HOGE_PIYO")==0) {
					fromWebView(message.substring(9));
					result.confirm();
					return true;
				}
				else {
					return false;
				}
			}
		};
		setWebChromeClient(chromeClient);
	}


	private void fromWebView(String mes)
	{
		((MyActivity) getContext()).showToast(mes);

		String[] str = mes.split("_");
		String okBtnTxt="OK";
		String ngBtnTxt="NG";

		if(str.length>=3){  okBtnTxt = str[2];  }
		if(str.length>=4){  ngBtnTxt = str[3];  }


		AlertDialog.Builder alertDialog=new AlertDialog.Builder(getContext());

		alertDialog.setTitle( str[0] );
		alertDialog.setMessage( str[1] );

		alertDialog.setPositiveButton(okBtnTxt, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.d("cda244", "Positive which :" + which);
			}
		});

		alertDialog.setNegativeButton(ngBtnTxt, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.d("cda244", "Negative which :" + which);
			}
		});
		/*
		alertDialog.setNeutralButton("SKIP", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// SKIPボタン押下時の処理
			}
		});
		*/

		alertDialog.show();
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

	/*
	class JSHandler {
		@JavascriptInterface
		public void fromWebView( String str ) {
			Log.d("cda244", "webViewから "+str);
			((MyActivity) getContext()).showToast("webViewから\n"+str);
		}
	}
	*/
