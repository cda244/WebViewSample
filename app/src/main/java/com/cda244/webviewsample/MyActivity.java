package com.cda244.webviewsample;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cda244.webviewsample.view.MyDrawerLayout;
import com.cda244.webviewsample.view.MyWebView;


public class MyActivity extends Activity {

	private static final String FIRST_URL = "http://google.com";

	public MyWebView webView;
	public MyDrawerLayout drawerLayout;

	private ProgressBar mProgBar;
	private float lastTranslate = 0.0f;
	private boolean mDrawerOpen = false;
	private boolean mKeyboardShown = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_my);

		Display disp = getWindowManager().getDefaultDisplay();
		int w, h;

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
			w = disp.getWidth();
			h = disp.getHeight();
			Log.d("cda244", "13未満");
		} else {
			Point size = new Point();
			disp.getSize(size);
			w = size.x;
			h = size.y;
			Log.d("cda244", "13以上");
		}

		/*
		Rect rect = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		int statusBarH = rect.top;
		*/
		Log.d("cda244", "w x h: " + w + " x " + h);

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String url = sp.getString("url", FIRST_URL);

		drawerLayout = (MyDrawerLayout) findViewById(R.id.drawer_layout);

		drawerLayout.setDrawerListener(new MyDrawerLayout.DrawerListener() {
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {

				if (drawerView == findViewById(R.id.drawer_right) ) {
					float moveFactor = -(drawerView.getWidth() * slideOffset);
                /*
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    //描画が遅れる
                    findViewById(R.id.main).setTranslationX(moveFactor);
                } else {
                }
                */
					TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
					anim.setDuration(0);
					anim.setFillAfter(true);
					findViewById(R.id.main).startAnimation(anim);

					lastTranslate = moveFactor;
				}

			}

			@Override
			public void onDrawerOpened(View drawerView) {
				mDrawerOpen = true;
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				mDrawerOpen = false;
			}

			@Override
			public void onDrawerStateChanged(int newState) {
			}
		});


		//if (savedInstanceState == null) {
			mProgBar = (ProgressBar) findViewById(R.id.prog_bar);

			webView = (MyWebView) findViewById(R.id.web_view);
			webView.init( mProgBar );

		//webView.loadUrl(url);
		webView.loadUrl("http://cda244.com/dm.html");
		//}
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//webView.saveState(outState);
	}


	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		//webView.restoreState(savedInstanceState);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

        /*
        if(mKeyboardShown) {
            return true;
        } else if(mDrawerOpen) {
            mDrawerLayout.closeDrawers();
            return true;
        } else {
            if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else {
                finish();
            }
        }
        */

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mKeyboardShown) {
				return true;
			} else if (mDrawerOpen) {
				drawerLayout.closeDrawers();
				return true;
			} else {
				if (webView.canGoBack()) {
					webView.goBack();
					return true;
				} else {
					finish();
				}
			}

		}
		return super.onKeyUp(keyCode, event);
	}


	public void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}


	public String getFirstURL() {
		return ((EditText) findViewById(R.id.edit)).getText().toString();
	}
}

    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mKeyboardShown) {
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
    */

