package com.kakumapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kakumapp.utils.Utils;

/**
 * Register name
 * 
 * @author paul
 * 
 */
public class RegisterName extends ActionBarActivity {

	private Button continueButton;
	private TextView descTextView, findPersonTextView;
	private Typeface typeface;
	private EditText firstNameEditText, lastNameEditText, otherNameEditText;
	private String firstName, lastName, otherName;
	private KakumaApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_name);

		application = (KakumaApplication) getApplication();

		continueButton = (Button) findViewById(R.id.button_register_continue);
		descTextView = (TextView) findViewById(R.id.textView_name_desc);
		findPersonTextView = (TextView) findViewById(R.id.textView_register_find_person);
		firstNameEditText = (EditText) findViewById(R.id.edittext_first_name);
		lastNameEditText = (EditText) findViewById(R.id.edittext_last_name);
		otherNameEditText = (EditText) findViewById(R.id.edittext_other_name);

		typeface = new Utils(this).getFont("Ubuntu-L");
		findPersonTextView.setTypeface(typeface);
		descTextView.setTypeface(typeface);

		int hintTextColor = getResources().getColor(R.color.half_white);
		firstNameEditText.setHintTextColor(hintTextColor);
		lastNameEditText.setHintTextColor(hintTextColor);
		otherNameEditText.setHintTextColor(hintTextColor);

		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isValidData()) {
					application.setFirstName(firstName);
					application.setLastName(lastName);
					application.setOtherName(otherName);

					// Bundle bundle = new Bundle();
					// bundle.putString("phone", phoneNumber);
					// bundle.putString("code", countryCode);
					// bundle.putString("firstName", firstName);
					// bundle.putString("lastName", lastName);
					// bundle.putString("otherName", otherName);
					//
					Intent originIntent = new Intent(RegisterName.this,
							RegisterPhone.class);
					// originIntent.putExtras(bundle);
					startActivity(originIntent);
					finish();
				}
			}
		});

		findPersonTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent findPersonIntent = new Intent(RegisterName.this,
						MeetingPoints.class);
				startActivity(findPersonIntent);
			}
		});

		firstName = application.getFirstName();
		lastName = application.getLastName();
		otherName = application.getOtherName();

		// set the data on the different fields
		if (firstName != null) {
			firstNameEditText.setText(firstName);
		}

		if (lastName != null) {
			lastNameEditText.setText(lastName);
		}

		if (otherName != null) {
			otherNameEditText.setText(otherName);
		}
		//
		// // get data if passed
		// Bundle bundle = getIntent().getExtras();
		// if (bundle != null) {
		// // for the phone number
		// phoneNumber = bundle.getString("phone");
		// countryCode = bundle.getString("code");
		// // if any data passed for this activity
		// firstName = bundle.getString("firstName");
		// lastName = bundle.getString("lastName");
		// otherName = bundle.getString("otherName");
		//
		// // set the data on the different fields
		// if (firstName != null) {
		// firstNameEditText.setText(firstName);
		// }
		//
		// if (lastName != null) {
		// lastNameEditText.setText(lastName);
		// }
		//
		// if (otherName != null) {
		// otherNameEditText.setText(otherName);
		// }
		// }
	}

	/**
	 * validate the data entered
	 * 
	 * @return true if valid,false otherwise
	 */
	protected boolean isValidData() {
		boolean valid = false;
		// get the data
		firstName = firstNameEditText.getText().toString().trim();
		lastName = lastNameEditText.getText().toString().trim();
		otherName = otherNameEditText.getText().toString().trim();
		// validate
		if (firstName != null && !firstName.equals("")) {
			if (lastName != null && !lastName.equals("")) {
				valid = true;
			} else {
				lastNameEditText.setError("Required");
			}
		} else {
			firstNameEditText.setError("Required");
		}
		return valid;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent nameIntent = new Intent(RegisterName.this, Register.class);
		startActivity(nameIntent);
		finish();
	}
}
