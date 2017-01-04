package com.example.sfg;

import android.app.Activity;
import android.os.Bundle;

public class DisplayGraph extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new Drawer(getApplicationContext()));

	}
}
