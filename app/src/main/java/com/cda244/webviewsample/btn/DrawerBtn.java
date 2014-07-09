package com.cda244.webviewsample.btn;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.cda244.webviewsample.MyActivity;

public class DrawerBtn extends Button implements View.OnClickListener {

	public DrawerBtn(Context context) {
		super(context);
		setOnClickListener(this);
	}

	public DrawerBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
	}

	public DrawerBtn(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		((MyActivity) getContext()).drawerLayout.openDrawer(Gravity.RIGHT);
	}

}
