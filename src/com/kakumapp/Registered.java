package com.kakumapp;

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
	private Button continueButton;
	private Typeface typeface;
	private Bitmap bitmap;
	private String firstName, lastName, otherName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_success);

		application = (KakumaApplication) getApplication();

		nameTextView = (TextView) findViewById(R.id.textView_name);
		textView_desc = (TextView) findViewById(R.id.textView_desc);
		circularImageView = (CircularImageView) findViewById(R.id.image_view_photo);
		continueButton = (Button) findViewById(R.id.button_continue);

		typeface = new Utils(this).getFont("Ubuntu-L");
		nameTextView.setTypeface(typeface);
		textView_desc.setTypeface(typeface);

		BitmapFactory.Options options = new BitmapFactory.Options();
		bitmap = BitmapFactory.decodeFile(application.getPhotoFile()
				.getAbsolutePath(), options);

		firstName = application.getFirstName();
		lastName = application.getLastName();
		otherName = application.getOtherName();

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
		}

		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent homeIntent = new Intent(Registered.this, Home.class);
				startActivity(homeIntent);
				finish();
			}
		});
	}
}
