package com.kakumapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class Home extends ActionBarActivity {

	private Toolbar toolbar;
	private long delay;
	protected TextView textView;
	private Handler handler;
	protected int index;
	private String text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		textView = (TextView) findViewById(R.id.textView1);
		handler = new Handler();
		setCharacterDelay(200);
		animateText("Finding...");
	}

	private Runnable characterAdder = new Runnable() {
		@Override
		public void run() {
			textView.setText(text.subSequence(0, index++));
			if (index <= text.length()) {
				handler.postDelayed(characterAdder, delay);
			}
		}
	};

	public void animateText(String text) {
		this.text = text;
		index = 0;
		textView.setText("");
		handler.removeCallbacks(characterAdder);
		handler.postDelayed(characterAdder, delay);
	}

	public void setCharacterDelay(long millis) {
		delay = millis;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		}
		return true;
	}
}
