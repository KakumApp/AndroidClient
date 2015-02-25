package com.kakumapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.kakumapp.utils.Utils;

public class RegisterOrigin extends ActionBarActivity {

	private Button continueButton;
	private TextView findPersonTextView;
	private Typeface typeface;
	private Spinner countriesSpinner, placesSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_origin);

		continueButton = (Button) findViewById(R.id.button_register_continue);
		findPersonTextView = (TextView) findViewById(R.id.textView_register_find_person);
		countriesSpinner = (Spinner) findViewById(R.id.spinner_origin_countries);
		placesSpinner = (Spinner) findViewById(R.id.spinner_origin_places);

		typeface = new Utils(this).getFont("Ubuntu-L");
		findPersonTextView.setTypeface(typeface);

		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent nameIntent = new Intent(RegisterOrigin.this,
						RegisterPhoto.class);
				startActivity(nameIntent);
			}
		});

		findPersonTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent findPersonIntent = new Intent(RegisterOrigin.this, Home.class);
				startActivity(findPersonIntent);
			}
		});

		String[] list = { "Ethiopia", "Kenya", "Uganda" };
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

		String[] listPlaces = { "Lodwar", "Wajir" };
		ArrayAdapter<String> dataAdapterPlaces = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listPlaces);
		dataAdapterPlaces
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		placesSpinner.setAdapter(dataAdapterPlaces);

		placesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}
}
