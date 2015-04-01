package com.kakumapp;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.kakumapp.utils.Utils;
import com.kakumapp.views.CircularImageView;

public class Registered extends ActionBarActivity {

	private KakumaApplication application;
	private TextView nameTextView;
	private TextView textView_desc;
	private CircularImageView circularImageView;
	private Button continueButton, registerButton;
	private Typeface typeface;
	private Bitmap bitmap;
	private String firstName, lastName, otherName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_success);
		// app
		application = (KakumaApplication) getApplication();
		// views
		nameTextView = (TextView) findViewById(R.id.textView_name);
		textView_desc = (TextView) findViewById(R.id.textView_desc);
		circularImageView = (CircularImageView) findViewById(R.id.image_view_photo);
		continueButton = (Button) findViewById(R.id.button_find_a_person);
		registerButton = (Button) findViewById(R.id.button_register);

		// fonts
		typeface = new Utils(this).getFont("Ubuntu-L");
		nameTextView.setTypeface(typeface);
		textView_desc.setTypeface(typeface);
		File photoFile = application.getPhotoFile();

		if (photoFile != null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(),
					options);
		}
		// get the application data
		firstName = application.getFirstName();
		lastName = application.getLastName();
		otherName = application.getOtherName();
		// display photo and name
		if (firstName != null && lastName != null) {
			nameTextView.setText(firstName + " " + lastName);
			if (otherName != null) {
				nameTextView.append(" " + otherName);
			} else {
				otherName = null;
			}
		}

		if (bitmap != null) {
			circularImageView.setImageBitmap(bitmap);
		} else {
			circularImageView.setImageResource(R.drawable.bck_landing);
		}
		
		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				application.clearData();
				Intent homeIntent = new Intent(Registered.this,
						FindPerson.class);
				startActivity(homeIntent);
				finish();
			}
		});

		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				application.clearData();
				Intent registerIntent = new Intent(Registered.this,
						Register.class);
				startActivity(registerIntent);
				finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		application.clearData();
		Intent homeIntent = new Intent(Registered.this, FindPerson.class);
		startActivity(homeIntent);
		finish();
	}
}
