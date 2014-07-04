package cda244.webviewsample;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MyActivity extends Activity {

    private static final String FIRST_URL="http://google.com";
    private WebView mWebView;
    private WebViewClient mWebViewClient;
    private WebChromeClient mWebChromeClient;
    private ProgressBar mProgBar;
    private Button mBtnL, mBtnR;

    private DrawerLayout mDrawerLayout;
    private View mDrawerL, mDrawerR;
    private float lastTranslate = 0.0f;
    private boolean mDrawerOpen=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String url = sp.getString("url", FIRST_URL);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener( new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                float moveFactor = (drawerView.getWidth() * slideOffset);

                if(drawerView == mDrawerR ){    moveFactor *= -1;   }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    findViewById(R.id.main).setTranslationX(moveFactor);
                } else {
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
            public void onDrawerStateChanged(int newState) {    }
        });

        mDrawerL = findViewById(R.id.drawer_left);
        mDrawerR = findViewById(R.id.drawer_right);

        mProgBar = (ProgressBar) findViewById(R.id.prog_bar);

        mBtnL = (Button) findViewById(R.id.btn_left);
        mBtnL.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {   mDrawerLayout.openDrawer(mDrawerL); }
        });

        mBtnR = (Button) findViewById(R.id.btn_right);
        mBtnR.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {   mDrawerLayout.openDrawer(mDrawerR); }
        });



        Button reloadBtn = (Button) findViewById(R.id.reload_btn);
        reloadBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("リロード");
                mWebView.reload();
                mDrawerLayout.closeDrawers();
            }
        });

        Button clearBtn = (Button) findViewById(R.id.cache_clear_btn);
        clearBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("キャッシュクリア");
                mWebView.clearCache(true);  //ローカル含めクリア
            }
        });

        Button saveBtn = (Button) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ((EditText) findViewById(R.id.edit)).getText().toString();
                saveURL( url );
                mWebView.loadUrl(url);
                mDrawerLayout.closeDrawers();

                //キーボード非表示
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });


        if (savedInstanceState == null) {
            initWebView();
            mWebView.loadUrl(url);
        }
    }


    private void initWebView()
    {
        initClient();

        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.setWebViewClient( mWebViewClient );
        mWebView.setWebChromeClient( mWebChromeClient );
        mWebView.setVerticalScrollbarOverlay(true);
        mWebView.setHorizontalScrollbarOverlay(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
    }


    private void initClient()
    {
        mWebViewClient = new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgBar.setProgress(0);
                mProgBar.setVisibility(View.VISIBLE);
                mWebView.requestFocus(View.FOCUS_DOWN);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgBar.setVisibility(View.INVISIBLE);
                mWebView.requestFocus(View.FOCUS_DOWN);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                showToast("ロードエラー\n" + failingUrl);
                mProgBar.setVisibility(View.INVISIBLE);
            }
        };

        mWebChromeClient = new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int progress) {
                mProgBar.setProgress(progress);
            }
        };

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        mWebView.restoreState(savedInstanceState);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(mDrawerOpen){
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
        return super.onKeyUp(keyCode, event);
    }


    private void showToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    public void saveURL(String url)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit().putString("url", url).commit();
        showToast( "初期URLを\n"+url+"に変更");
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