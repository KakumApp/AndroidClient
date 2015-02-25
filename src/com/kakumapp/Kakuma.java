package com.kakumapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.kakumapp.utils.SwipeGestureFilter.SwipeGestureListener;
import com.kakumapp.utils.Utils;

public class Kakuma extends ActionBarActivity implements SwipeGestureListener {

	private Button registerButton, findAPersonButton;
	private TextView titleTextView, descTextView, howToUseTextView;
	private Typeface typeface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kakuma);

		registerButton = (Button) findViewById(R.id.button_register);
		findAPersonButton = (Button) findViewById(R.id.button_find_a_person);
		titleTextView = (TextView) findViewById(R.id.textView_title);
		descTextView = (TextView) findViewById(R.id.textView_desc);
		howToUseTextView = (TextView) findViewById(R.id.textView_how_to_use);

		typeface = new Utils(this).getFont("Ubuntu-L");
		titleTextView.setTypeface(typeface);
		descTextView.setTypeface(typeface);
		howToUseTextView.setTypeface(typeface);

		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent registerIntent = new Intent(Kakuma.this, Register.class);
				startActivity(registerIntent);
			}
		});

		findAPersonButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent findPersonIntent = new Intent(Kakuma.this, Home.class);
				startActivity(findPersonIntent);
			}
		});

		howToUseTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent howIntent = new Intent(Kakuma.this, Help.class);
				startActivity(howIntent);
			}
		});
	}

	@Override
	public void onBackPressed() {
		// new Utils(this).quit();
		finish();
		moveTaskToBack(true);
	}

	@Override
	public void onSwipe(int direction) {
		
	}

	@Override
	public void onDoubleTap() {
		
	}
}
