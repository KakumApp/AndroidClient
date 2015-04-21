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
import android.widget.Button;
import android.widget.TextView;

import com.kakumapp.utils.Utils;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SearchPhone extends ActionBarActivity {

	private Toolbar toolbar;
	private ActionBar actionBar;
	private Button continueButton;
	private TextView searchDescrTextView;
	private Typeface typeface;
	private String phoneNumber;
	private MaterialEditText phoneEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_phone);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		phoneEditText = (MaterialEditText) findViewById(R.id.edittext_phone);
		continueButton = (Button) findViewById(R.id.button_continue);
		searchDescrTextView = (TextView) findViewById(R.id.textView_phone_desc);

		// fonts
		typeface = new Utils(this).getFont("Ubuntu-L");
		searchDescrTextView.setTypeface(typeface);

		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isValidInput()) {
					Bundle bundle = new Bundle();
					bundle.putString("searchType", "Phone");
					bundle.putString("phone", phoneNumber);
					Intent intent = new Intent(SearchPhone.this,
							FindPerson.class);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});

	}

	/**
	 * validate the data
	 * 
	 * @return
	 */
	protected boolean isValidInput() {
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
