package com.example.sfg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Home extends Activity {
	private EditText numNodes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		numNodes = (EditText) findViewById(R.id.numbNodes);
		Button enter = (Button) findViewById(R.id.buttonenter);

		enter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String num = numNodes.getText().toString().trim();
				if (numNodes.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"enter total number of nodes first!",
							Toast.LENGTH_LONG).show();
				} else if (!Valid.isValidInt(num)) {
					Toast.makeText(getApplicationContext(),
							"invalid value for number of nodes!",
							Toast.LENGTH_LONG).show();
				} else {
					int n = Integer.parseInt(num);
					Data.segmentsGains = new double[n][n];
					Data.numOfNodes = n;
					startActivity(new Intent(getApplicationContext(),
							Input.class));
				}
			}
		});

	}

}
