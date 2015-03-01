package com.kakumapp;

import java.io.File;
import java.util.ArrayList;

import com.kakumapp.utils.Utils;
import com.kakumapp.views.CircularImageView;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class RegisterSummary extends ActionBarActivity {

	private String firstName, lastName, otherName, phoneNumber, countryCode,
			country;
	private ArrayList<String> places;
	private Typeface typeface;
	private TextView nameTextView, phoneTextView, countryTextView,
			placeTextView;
	protected static File photoFile;
	protected static Bitmap bitmap;
	private CircularImageView circularImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_summary);

		nameTextView = (TextView) findViewById(R.id.textView_name);
		phoneTextView = (TextView) findViewById(R.id.textView_phone);
		countryTextView = (TextView) findViewById(R.id.textView_country);
		placeTextView = (TextView) findViewById(R.id.textView_place);
		circularImageView = (CircularImageView) findViewById(R.id.image_view_photo);

		typeface = new Utils(this).getFont("Ubuntu-L");
		nameTextView.setTypeface(typeface);
		phoneTextView.setTypeface(typeface);
		countryTextView.setTypeface(typeface);
		placeTextView.setTypeface(typeface);

		// get data if passed
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			// for the phone number
			phoneNumber = bundle.getString("phone");
			countryCode = bundle.getString("code");
			firstName = bundle.getString("firstName");
			lastName = bundle.getString("lastName");
			otherName = bundle.getString("otherName");
			country = bundle.getString("country");
			places = bundle.getStringArrayList("places");

			if (firstName != null && lastName != null) {
				nameTextView.setText(firstName + " " + lastName);
				if (otherName != null) {
					nameTextView.append(" " + otherName);
				}
			}

			if (phoneNumber != null) {
				phoneTextView.setText(phoneNumber);
			}

			if (country != null) {
				countryTextView.setText(country);
			}

			if (places != null) {
				String plcs = "";
				for (String plc : places) {
					plcs += plc;
				}
				placeTextView.setText(plcs);
			}

			if (bitmap != null) {
				circularImageView.setImageBitmap(bitmap);
			}
		}
	}
}
