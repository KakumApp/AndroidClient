package com.kakumapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Register phone number
 * 
 * @author paul
 * 
 */
public class RegisterChoosePhoto extends ActionBarActivity {

	private Button takePhotoButton, skipButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_photo);

		takePhotoButton = (Button) findViewById(R.id.button_take_photo);
		skipButton = (Button) findViewById(R.id.button_skip);

		takePhotoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent photoIntent = new Intent(RegisterChoosePhoto.this,
						RegisterPhoto.class);
				startActivity(photoIntent);
				finish();
			}
		});

		skipButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent skipIntent = new Intent(RegisterChoosePhoto.this,
						RegisterSummary.class);
				startActivity(skipIntent);
				finish();
			}
		});
	}
}
