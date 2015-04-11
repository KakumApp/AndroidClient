package com.kakumapp;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.kakumapp.adapters.RegisteredPerson;
import com.kakumapp.adapters.RegisteredPersonsAdapter;
import com.pnikosis.materialishprogress.ProgressWheel;

public class SearchResults extends ActionBarActivity {

	public static final String TAG = "SearchResults";
	private static String URL = "http://kakumapp-api.herokuapp.com/targets?",
			SEARCH_URL;
	public static final String USERNAME = "admin";
	public static final String PASSWORD = "admin";
	private Toolbar toolbar;
	private ActionBar actionBar;
	private RecyclerView recyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	private ArrayList<RegisteredPerson> registeredPersons = new ArrayList<>();
	private Button searchButton;
	private String searchType;
	private ProgressWheel progressWheel;

	// private String phoneNumber;
	// private ArrayList<String> selectedPlaces = new ArrayList<>();
	// private String countryName;
	// private String firstName, fatherName, grandFatherName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		searchButton = (Button) findViewById(R.id.button_search);
		progressWheel = (ProgressWheel) findViewById(R.id.progressWheel);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		recyclerView.setHasFixedSize(true);
		// use a linear layout manager
		mLayoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(mLayoutManager);
		// specify an adapter
		mAdapter = new RegisteredPersonsAdapter(registeredPersons);
		recyclerView.setAdapter(mAdapter);

		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent searchOptions = new Intent(SearchResults.this,
						SearchOptions.class);
				startActivity(searchOptions);
			}
		});
		// get the parameters sent
		Bundle extras = getIntent().getExtras();
		// search type
		searchType = extras.getString("searchType");
		if (searchType != null) {
			actionBar.setTitle("Results - " + searchType);
			SEARCH_URL = URL;
			/**
			 * form URL of the form search=param1&search=param2&...search=paramn
			 */
			// append name param
			if (searchType.equalsIgnoreCase("Name")) {
				// append all params
				if (extras.containsKey("firstName")) {
					SEARCH_URL += "search=" + extras.getString("firstName")
							+ "&";
				}
				if (extras.containsKey("fatherName")) {
					SEARCH_URL += "search=" + extras.getString("fatherName")
							+ "&";
				}
				if (extras.containsKey("grandFatherName")) {
					SEARCH_URL += "search="
							+ extras.getString("grandFatherName") + "&";
				}
			}
			// append phone param
			else if (searchType.equalsIgnoreCase("Phone")) {
				SEARCH_URL += "search=" + extras.getString("phone");
			}
			// append country and places param
			else if (searchType.equalsIgnoreCase("Origin")) {
				if (extras.containsKey("country")) {
					SEARCH_URL += "search=" + extras.getString("country") + "&";
				}
				ArrayList<String> places = extras.getStringArrayList("places");
				for (String place : places) {
					SEARCH_URL += "search=" + place + "&";
				}
			}
			Log.e(TAG, SEARCH_URL);
		}
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

	// show progress bar
	private void showProgress() {
		if (progressWheel != null) {
			progressWheel.setVisibility(View.VISIBLE);
		}
	}

	// hide progress bar
	private void hideprogress() {
		if (progressWheel != null) {
			progressWheel.setVisibility(View.GONE);
		}
	}

}
