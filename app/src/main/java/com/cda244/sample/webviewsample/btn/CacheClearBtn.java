package com.cda244.sample.webviewsample.btn;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.cda244.sample.webviewsample.MyActivity;

public class CacheClearBtn extends Button implements View.OnClickListener {

	public CacheClearBtn(Context context) {
		super(context);
		init();
	}

	public CacheClearBtn(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public CacheClearBtn(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}


	public void init() {
		setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		((MyActivity) getContext()).webView.clearCache(true);  //ローカル含めクリア
		((MyActivity) getContext()).showToast("キャッシュクリア");
	}
}
