package com.kakumapp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.kakumapp.utils.SwipeGestureFilter.SwipeGestureListener;
import com.kakumapp.utils.Utils;

public class Kakuma extends AppCompatActivity implements SwipeGestureListener {

	private static final String TAG = "Kakuma";
	private Button registerButton, findAPersonButton;
	private TextView titleTextView, descTextView, howToUseTextView,
			versionTextView;
	private Typeface typeface, typeface2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kakuma);
		// views
		registerButton = (Button) findViewById(R.id.button_register);
		findAPersonButton = (Button) findViewById(R.id.button_find_a_person);
		titleTextView = (TextView) findViewById(R.id.textView_title);
		descTextView = (TextView) findViewById(R.id.textView_desc);
		howToUseTextView = (TextView) findViewById(R.id.textView_how_to_use);
		versionTextView = (TextView) findViewById(R.id.textView_version);

		// font
		typeface = new Utils(this).getFont("Lato-Light");
		typeface2 = new Utils(this).getFont("Ubuntu-L");

		titleTextView.setTypeface(typeface);
		descTextView.setTypeface(typeface2);
		howToUseTextView.setTypeface(typeface2);
		versionTextView.setTypeface(typeface2);

		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String version = pInfo.versionName;
			versionTextView.setText("V " + version);
		} catch (NameNotFoundException e) {
			Log.e(TAG, "Exception  " + e.getLocalizedMessage());
		}

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
				Intent findPersonIntent = new Intent(Kakuma.this,
						FindPerson.class);
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
