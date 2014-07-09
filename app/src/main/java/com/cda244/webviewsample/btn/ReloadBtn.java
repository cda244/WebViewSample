package com.cda244.webviewsample.btn;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.cda244.webviewsample.MyActivity;

public class ReloadBtn extends Button implements View.OnClickListener {

	public ReloadBtn(Context context) {
		super(context);
		init();
	}

	public ReloadBtn(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public ReloadBtn(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}


	public void init() {
		setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		((MyActivity) getContext()).webView.reload();
		((MyActivity) getContext()).showToast("リロード");
		((MyActivity) getContext()).drawerLayout.closeDrawers();
	}
}
