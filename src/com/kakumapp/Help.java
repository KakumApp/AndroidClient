package com.kakumapp;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.kakumapp.views.ExpandableTextView;

public class Help extends AppCompatActivity {

	private Toolbar toolbar;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		((TextView) findViewById(R.id.header1).findViewById(R.id.title))
				.setText("About:");
		((TextView) findViewById(R.id.header2).findViewById(R.id.title))
				.setText("How it works:");

		ExpandableTextView expTv2 = (ExpandableTextView) findViewById(
				R.id.header1).findViewById(R.id.expand_text_view);
		ExpandableTextView expTv3 = (ExpandableTextView) findViewById(
				R.id.header2).findViewById(R.id.expand_text_view);

		expTv2.setText(getString(R.string.app_desc));
		expTv3.setText(getString(R.string.how_help));
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
}
