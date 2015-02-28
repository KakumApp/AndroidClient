package com.kakumapp;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.kakumapp.utils.Utils;

public class RegisterOrigin extends ActionBarActivity {

	private static final String SELECT_COUNTRY = "Select country";
	private Button continueButton;
	private TextView findPersonTextView;
	private Typeface typeface;
	private Spinner countriesSpinner;
	private MultiAutoCompleteTextView placesAutoCompleteTextView;
	private String firstName, lastName, otherName;
	private String phoneNumber, countryCode;
	private String country;
	private ArrayList<String> places;
	private ArrayAdapter<String> dataAdapterCountries, dataAdapterPlaces;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_origin);

		continueButton = (Button) findViewById(R.id.button_register_continue);
		findPersonTextView = (TextView) findViewById(R.id.textView_register_find_person);
		countriesSpinner = (Spinner) findViewById(R.id.spinner_origin_countries);
		placesAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.autocomplete_origin_places);

		typeface = new Utils(this).getFont("Ubuntu-L");
		findPersonTextView.setTypeface(typeface);
		placesAutoCompleteTextView.setHintTextColor(Color.WHITE);

		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goNext();
			}
		});

		findPersonTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent findPersonIntent = new Intent(RegisterOrigin.this,
						Home.class);
				startActivity(findPersonIntent);
			}
		});

		String[] list = { SELECT_COUNTRY, "Ethiopia", "Kenya", "Uganda" };
		dataAdapterCountries = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapterCountries
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		countriesSpinner.setAdapter(dataAdapterCountries);

		countriesSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View arg1, int position, long arg3) {
						TextView textView = ((TextView) parent.getChildAt(0));
						if (textView != null) {
							textView.setTextColor(Color.WHITE);
						}
						String selected = (String) parent
								.getItemAtPosition(position);
						if (!selected.equals(SELECT_COUNTRY)) {
							country = selected;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		String[] listPlaces = { "Lodwar", "Wajir" };

		dataAdapterPlaces = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listPlaces);

		dataAdapterPlaces.setDropDownViewResource(R.layout.spinner_dropdown);
		placesAutoCompleteTextView.setAdapter(dataAdapterPlaces);
		// specify the minimum type of characters before drop-down list is shown
		placesAutoCompleteTextView.setThreshold(1);
		// comma to separate the different places
		placesAutoCompleteTextView
				.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		// when the user clicks an item of the drop-down list
		placesAutoCompleteTextView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View arg1,
							int position, long arg3) {
						String place = (String) parent
								.getItemAtPosition(position);
						if (!places.contains(place)) {
							places.add(place);
						}
					}
				});

		// get data if passed
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			// for the phone number
			phoneNumber = bundle.getString("phone");
			countryCode = bundle.getString("code");
			firstName = bundle.getString("firstName");
			lastName = bundle.getString("lastName");
			otherName = bundle.getString("otherName");
			// for this activity
			country = bundle.getString("country");
			places = bundle.getStringArrayList("places");

			if (country != null) {
				countriesSpinner.setSelection(dataAdapterCountries
						.getPosition(country));
			}

			if (places != null && !places.isEmpty()) {
				String plcs = "";
				for (int i = 0; i < places.size(); i++) {
					plcs += places.get(i) + ",";
				}
				placesAutoCompleteTextView.setText(plcs);
			} else {
				places = new ArrayList<>();
			}
		}
	}

	protected void goNext() {
		if (isValidData()) {
			Bundle bundle = new Bundle();
			bundle.putString("phone", phoneNumber);
			bundle.putString("code", countryCode);
			bundle.putString("firstName", firstName);
			bundle.putString("lastName", lastName);
			bundle.putString("otherName", otherName);
			bundle.putString("country", country);
			bundle.putStringArrayList("places", places);
			Intent nameIntent = new Intent(RegisterOrigin.this,
					RegisterPhoto.class);
			nameIntent.putExtras(bundle);
			startActivity(nameIntent);
		}
	}

	/**
	 * validate the data entered
	 * 
	 * @return true if valid,false otherwise
	 */
	protected boolean isValidData() {
		boolean valid = false;
		// validate
		if (country != null && !country.equals("")) {
			if (!places.isEmpty()) {
				valid = true;
			} else {

			}
		} else {
		}
		return valid;
	}
}
