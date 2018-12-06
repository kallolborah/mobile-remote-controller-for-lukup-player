package com.player.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;

public class CustomListView extends ListView {

	public CustomListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		removeSelector();
	}

	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCacheColorHint(Color.TRANSPARENT);
		removeSelector();
	}

	public CustomListView(Context context) {
		super(context);
		removeSelector();// optional
	}

	public void removeSelector() {
		setSelector(android.R.color.transparent); // optional
	}

	

}