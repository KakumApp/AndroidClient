package com.kakumapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.kakumapp.utils.Utils;

public class SearchOptions extends ActionBarActivity {

	private static final String ORIGIN_OPTION = "Place of origin",
			NAME_OPTION = "Name", PHONE_OPTION = "Phone number";
	private Toolbar toolbar;
	private ActionBar actionBar;
	private Spinner searchOptionsSpinner;
	private Button continueButton;
	private TextView searchDescrTextView;
	private Typeface typeface;
	private ArrayAdapter<String> dataAdapterSearchOptions;
	protected String option;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_options);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		continueButton = (Button) findViewById(R.id.button_continue);
		searchOptionsSpinner = (Spinner) findViewById(R.id.spinner_search_options);
		searchDescrTextView = (TextView) findViewById(R.id.textView_search_desc);

		// fonts
		typeface = new Utils(this).getFont("Ubuntu-L");
		searchDescrTextView.setTypeface(typeface);

		// create the spinners adapters
		String[] options = { ORIGIN_OPTION, NAME_OPTION, PHONE_OPTION };
		// dataAdapterSearchOptions = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_dropdown_item, options);
		dataAdapterSearchOptions = new ArrayAdapter<String>(
				getApplicationContext(),
				android.R.layout.simple_spinner_dropdown_item, options) {

			@Override
			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text = (TextView) view
						.findViewById(android.R.id.text1);
				text.setTextColor(getResources().getColor(R.color.dark_grey));
				return view;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text = (TextView) view
						.findViewById(android.R.id.text1);
				text.setTextColor(getResources().getColor(R.color.dark_grey));
				return view;
			}
		};

		dataAdapterSearchOptions
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		searchOptionsSpinner.setAdapter(dataAdapterSearchOptions);
		// when a country is selected,get the places under that country
		searchOptionsSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View arg1, int position, long arg3) {
						TextView textView = ((TextView) parent.getChildAt(0));
						if (textView != null) {
							textView.setTextColor(getResources().getColor(
									R.color.primary));
						}
						option = (String) parent.getItemAtPosition(position);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// take the user to the apt screen depending on the choice
				Intent intent = null;
				if (option.equals(ORIGIN_OPTION)) {
					intent = new Intent(SearchOptions.this, SearchOrigin.class);
				} else if (option.equals(NAME_OPTION)) {
					intent = new Intent(SearchOptions.this, SearchName.class);
				} else {
					intent = new Intent(SearchOptions.this, SearchPhone.class);
				}
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
