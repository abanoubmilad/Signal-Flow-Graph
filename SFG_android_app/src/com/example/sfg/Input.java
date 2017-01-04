package com.example.sfg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Input extends Activity {
	private EditText node1, node2, gain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);

		node1 = (EditText) findViewById(R.id.node1);
		node2 = (EditText) findViewById(R.id.node2);
		gain = (EditText) findViewById(R.id.gain);
		Button next = (Button) findViewById(R.id.buttonNext);
		Button done = (Button) findViewById(R.id.buttonDone);
		Button clear = (Button) findViewById(R.id.buttonClear);
		Button reset = (Button) findViewById(R.id.buttonReset);
		Button draw = (Button) findViewById(R.id.buttonDraw);
		reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Data.segmentsGains = new double[Data.numOfNodes][Data.numOfNodes];
				Toast.makeText(getApplicationContext(),
						"all paths were cleared!", Toast.LENGTH_SHORT).show();
			}
		});
		clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String t1 = node1.getText().toString(), t2 = node2.getText()
						.toString();
				if (t1.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"please, input value of from #!",
							Toast.LENGTH_SHORT).show();
				} else if (t2.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"please, input value of to #!", Toast.LENGTH_SHORT)
							.show();

				} else if (!Valid.isValidInt(t1)) {
					Toast.makeText(getApplicationContext(),
							"from #, invalid numeric value!",
							Toast.LENGTH_SHORT).show();
				} else if (!Valid.isValidInt(t2)) {
					Toast.makeText(getApplicationContext(),
							"to #, invalid numeric value!", Toast.LENGTH_SHORT)
							.show();

				} else {
					int n1 = Integer.parseInt(t1), n2 = Integer.parseInt(t2);
					if (n1 > Data.numOfNodes || n1 < 1) {
						Toast.makeText(getApplicationContext(),
								"from #, invalid node label!",
								Toast.LENGTH_SHORT).show();
					} else if (n2 > Data.numOfNodes || n2 < 1) {
						Toast.makeText(getApplicationContext(),
								"to #, invalid node label!", Toast.LENGTH_SHORT)
								.show();
					} else {
						if (Data.segmentsGains[n1 - 1][n2 - 1] == 0) {
							Toast.makeText(getApplicationContext(),
									"cant remove, path does not exist!",
									Toast.LENGTH_SHORT).show();
						} else {
							Data.segmentsGains[n1 - 1][n2 - 1] = 0;
							node1.setText("");
							node2.setText("");
							Toast.makeText(getApplicationContext(),
									"path cleared!", Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		});
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String t1 = node1.getText().toString(), t2 = node2.getText()
						.toString(), g = gain.getText().toString();
				if (t1.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"please, input value of from #!",
							Toast.LENGTH_SHORT).show();
				} else if (t2.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"please, input value of to #!", Toast.LENGTH_SHORT)
							.show();

				} else if (gain.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"please, input value of gain!", Toast.LENGTH_SHORT)
							.show();
				} else if (!Valid.isValidInt(t1)) {
					Toast.makeText(getApplicationContext(),
							"from #, invalid numeric value!",
							Toast.LENGTH_SHORT).show();
				} else if (!Valid.isValidInt(t2)) {
					Toast.makeText(getApplicationContext(),
							"to #, invalid numeric value!", Toast.LENGTH_SHORT)
							.show();

				} else if (!Valid.isValidDouble(g)) {
					Toast.makeText(getApplicationContext(),
							"gain, invalid numeric value!", Toast.LENGTH_SHORT)
							.show();
				} else {
					int n1 = Integer.parseInt(t1), n2 = Integer.parseInt(t2);
					if (n1 > Data.numOfNodes || n1 < 1) {
						Toast.makeText(getApplicationContext(),
								"from #, invalid node label!",
								Toast.LENGTH_SHORT).show();
					} else if (n2 > Data.numOfNodes || n2 < 1) {
						Toast.makeText(getApplicationContext(),
								"to #, invalid node label!", Toast.LENGTH_SHORT)
								.show();
					} else {
						Data.segmentsGains[n1 - 1][n2 - 1] = Double
								.parseDouble(g);
						node1.setText("");
						node2.setText("");
						gain.setText("");
					}
				}
			}
		});
		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Mason mason = new Mason();
				mason.setSFG(Data.segmentsGains);

				Data.forwardPaths = mason.getForwardPaths();
				Data.loops = mason.getLoops();
				Data.nonTouchingloops = mason.getNonTouchingLoops();
				Data.overAllTF = mason.getOvalAllTF();
				Data.loopsGain = mason.getLoopGains();
				Data.nonTouchingloopsGain = mason.getNonTouchingLoopGains();
				Data.forwardPathsGain = mason.getForwardPathGains();

				node1.setText("");
				node2.setText("");
				gain.setText("");

				startActivity(new Intent(getApplicationContext(),
						DisplayResults.class));

			}
		});

		draw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(),
						DisplayGraph.class));
			}
		});
	}

}
