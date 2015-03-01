package com.kakumapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
	private Spinner countriesSpinner;
	private EditText phoneEditText;
	private String phoneNumber, countryCode;
	private ArrayAdapter<String> countriesCodesAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone);

		continueButton = (Button) findViewById(R.id.button_register_continue);
		descTextView = (TextView) findViewById(R.id.textView_phone_desc);
		findPersonTextView = (TextView) findViewById(R.id.textView_register_find_person);
		phoneEditText = (EditText) findViewById(R.id.edittext_phone);

		typeface = new Utils(this).getFont("Ubuntu-L");
		findPersonTextView.setTypeface(typeface);
		descTextView.setTypeface(typeface);
		phoneEditText.setHintTextColor(Color.WHITE);

		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isValidData()) {
					Bundle bundle = new Bundle();
					bundle.putString("phone", phoneNumber);
					bundle.putString("code", countryCode);
					Intent nameIntent = new Intent(RegisterPhone.this,
							RegisterName.class);
					nameIntent.putExtras(bundle);
					startActivity(nameIntent);
				}
			}
		});

		findPersonTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent findPersonIntent = new Intent(RegisterPhone.this,
						Home.class);
				startActivity(findPersonIntent);
			}
		});

		countriesSpinner = (Spinner) findViewById(R.id.spinner_phone_countries);
		String[] list = { "SS +211", "SD +249", "SO +252", "ET +251",
				"CD +243", "BI +257", "RW +250", "ER +291", "UG +256" };
		countriesCodesAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		countriesCodesAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		countriesSpinner.setAdapter(countriesCodesAdapter);

		countriesSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View arg1, int position, long arg3) {
						TextView textView = ((TextView) parent.getChildAt(0));
						if (textView != null) {
							textView.setTextColor(Color.WHITE);
						}
						countryCode = (String) parent
								.getItemAtPosition(position);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		// set data if passed
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			phoneNumber = bundle.getString("phone");
			countryCode = bundle.getString("code");

			if (phoneNumber != null) {
				phoneEditText.setText(phoneNumber);
			}

			if (countryCode != null) {
				countriesSpinner.setSelection(countriesCodesAdapter
						.getPosition(countryCode));
			}
		}
	}

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
}
