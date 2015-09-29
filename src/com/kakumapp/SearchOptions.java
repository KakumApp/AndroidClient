package com.kakumapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kakumapp.utils.Utils;

public class SearchOptions extends AppCompatActivity {

	private Toolbar toolbar;
	private ActionBar actionBar;
	private Button continueButton;
	private TextView searchDescrTextView;
	private Typeface typeface;
	protected String option;
	private RadioGroup radioGroup;

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
		radioGroup = (RadioGroup) findViewById(R.id.search_options_layout);
		searchDescrTextView = (TextView) findViewById(R.id.textView_search_desc);

		// fonts
		typeface = new Utils(this).getFont("Ubuntu-L");
		searchDescrTextView.setTypeface(typeface);

		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// get selected radio button from radioGroup
				int selectedId = radioGroup.getCheckedRadioButtonId();
				// take the user to the apt screen depending on the choice
				Intent intent = null;
				if (selectedId == R.id.radio_origin) {
					intent = new Intent(SearchOptions.this, SearchOrigin.class);
				} else if (selectedId == R.id.radio_name) {
					intent = new Intent(SearchOptions.this, SearchName.class);
				} else {
					intent = new Intent(SearchOptions.this, SearchPhone.class);
				}
				startActivity(intent);
			}
		});
		
		findViewById(R.id.button_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
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
