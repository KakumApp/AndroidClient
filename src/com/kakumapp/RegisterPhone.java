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
 * Register phone number
 * 
 * @author paul
 * 
 */
public class RegisterPhone extends ActionBarActivity {

	private Button continueButton;
	private TextView descTextView, findPersonTextView;
	private Typeface typeface;
	private EditText phoneEditText;
	private String phoneNumber;
	private KakumaApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone);
		// app
		application = (KakumaApplication) getApplication();
		// get views
		continueButton = (Button) findViewById(R.id.button_register_continue);
		descTextView = (TextView) findViewById(R.id.textView_phone_desc);
		findPersonTextView = (TextView) findViewById(R.id.textView_register_find_person);
		phoneEditText = (EditText) findViewById(R.id.edittext_phone);
		// font
		typeface = new Utils(this).getFont("Ubuntu-L");
		findPersonTextView.setTypeface(typeface);
		descTextView.setTypeface(typeface);
		// hint text color
		int hintTextColor = getResources().getColor(R.color.half_white);
		phoneEditText.setHintTextColor(hintTextColor);
		// move to next screen if data is valid
		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isValidData()) {
					application.setPhoneNumber(phoneNumber);
					Intent nameIntent = new Intent(RegisterPhone.this,
							RegisterOrigin.class);
					startActivity(nameIntent);
					finish();
				}
			}
		});

		findPersonTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent findPersonIntent = new Intent(RegisterPhone.this,
						MeetingPoints.class);
				startActivity(findPersonIntent);
			}
		});
		
		// get the default data
		phoneNumber = application.getPhoneNumber();

		if (phoneNumber != null) {
			phoneEditText.setText(phoneNumber);
		}
	}

	/**
	 * validate the data
	 * 
	 * @return
	 */
	protected boolean isValidData() {
		boolean valid = false;
		phoneNumber = phoneEditText.getText().toString().trim();
		// remove the starting zero if present
		if (phoneNumber.startsWith("0")) {
			phoneNumber = phoneNumber.substring(1);
		}
		if (phoneNumber.matches("\\d+") && phoneNumber.length() <= 20) {
			valid = true;
		} else {
			phoneEditText.setError("Invalid phone number");
		}
		return valid;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent nameIntent = new Intent(RegisterPhone.this, RegisterName.class);
		startActivity(nameIntent);
		finish();
	}
}
