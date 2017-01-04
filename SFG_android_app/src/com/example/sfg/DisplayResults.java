package com.example.sfg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DisplayResults extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);

		TextView loops = (TextView) findViewById(R.id.loops);
		TextView fb = (TextView) findViewById(R.id.forwardpaths);
		TextView nontouch = (TextView) findViewById(R.id.nonTouchingLoops);
		TextView tf = (TextView) findViewById(R.id.overalltf);
		Button button = (Button) findViewById(R.id.button);

		String temp = "";
		for (int i = 0; i < Data.loops.length; i++) {
			temp += Data.loops[i] + "\n";
		}
		loops.setText(temp);

		temp = "";
		for (int i = 0; i < Data.nonTouchingloops.length; i++) {
			temp += Data.nonTouchingloops[i] + "\n";
		}
		nontouch.setText(temp);

		temp = "";
		for (int i = 0; i < Data.forwardPaths.length; i++) {
			temp += Data.forwardPaths[i] + "\n";
		}
		fb.setText(temp);

		tf.setText(Data.overAllTF + "");

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(),
						DisplayGraph.class));
			}
		});
	}
}
