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

public class RegisterPhone extends ActionBarActivity {

	private Button continueButton;
	private TextView descTextView, findPersonTextView;
	private Typeface typeface;
	private Spinner countriesSpinner;
	private EditText phoneEditText;

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
				Intent nameIntent = new Intent(RegisterPhone.this,
						RegisterName.class);
				startActivity(nameIntent);
			}
		});

		findPersonTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent findPersonIntent = new Intent(RegisterPhone.this, Home.class);
				startActivity(findPersonIntent);
			}
		});

		countriesSpinner = (Spinner) findViewById(R.id.spinner_phone_countries);
		String[] list = { "AF +93", "KE +254", "ZW +263" };
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		countriesSpinner.setAdapter(dataAdapter);

		countriesSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View arg1, int position, long arg3) {
						((TextView) parent.getChildAt(0))
								.setTextColor(Color.WHITE);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

	}
}
