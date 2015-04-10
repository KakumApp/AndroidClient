package com.kakumapp;

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

public class SearchOrigin extends ActionBarActivity {

	private Toolbar toolbar;
	private ActionBar actionBar;
	private Button continueButton;
	private TextView searchDescrTextView;
	private Typeface typeface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_origin);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		continueButton = (Button) findViewById(R.id.button_continue);
		searchDescrTextView = (TextView) findViewById(R.id.textView_origin_desc);

		// fonts
		typeface = new Utils(this).getFont("Ubuntu-L");
		searchDescrTextView.setTypeface(typeface);

		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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
