package com.kakumapp;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.kakumapp.adapters.RegisteredPerson;
import com.kakumapp.adapters.RegisteredPersonsAdapter;

public class FindPerson extends ActionBarActivity {

	private Toolbar toolbar;
	private ActionBar actionBar;
	private RecyclerView recyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	private ArrayList<RegisteredPerson> registeredPersons = new ArrayList<>();
	private Button searchButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_person);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		for (int i = 0; i < 10; i++) {
			registeredPersons.add(new RegisteredPerson("URL", "John Doe Aden"
					+ (i + 1), "Wajir, Kenya", "071X XXX XXX", null));
		}
		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		searchButton = (Button) findViewById(R.id.button_search);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		recyclerView.setHasFixedSize(true);
		// use a linear layout manager
		mLayoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(mLayoutManager);
		// specify an adapter
		mAdapter = new RegisteredPersonsAdapter(registeredPersons, this);
		recyclerView.setAdapter(mAdapter);

		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent searchOptions = new Intent(FindPerson.this,
						SearchOptions.class);
				startActivity(searchOptions);
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
		Intent homeIntent = new Intent(FindPerson.this, Kakuma.class);
		startActivity(homeIntent);
		finish();
	}
}
