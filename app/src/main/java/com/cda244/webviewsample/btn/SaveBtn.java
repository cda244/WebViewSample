package com.cda244.webviewsample.btn;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.cda244.webviewsample.MyActivity;

public class SaveBtn extends Button implements View.OnClickListener {

	public SaveBtn(Context context) {
		super(context);
		init();
	}

	public SaveBtn(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public SaveBtn(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}


	public void init() {
		setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		String url = ((MyActivity) getContext()).getFirstURL();
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences( getContext() );
		pref.edit().putString("url", url).commit();
		((MyActivity) getContext()).showToast("初期URLを\n" + url + "に変更");

		((MyActivity) getContext()).webView.loadUrl( url );
		((MyActivity) getContext()).drawerLayout.closeDrawers();

		//キーボード非表示
		InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
