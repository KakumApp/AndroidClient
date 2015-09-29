package com.kakumapp;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.kakumapp.adapters.MeetingPoint;
import com.kakumapp.adapters.MeetingPointsListAdapter;

public class MeetingPoints extends AppCompatActivity {

	private Toolbar toolbar;
	private ActionBar actionBar;
	private MeetingPointsListAdapter dataAdapter;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_points);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		listView = (ListView) findViewById(R.id.listView1);

		ArrayList<MeetingPoint> meetingPoints = new ArrayList<>();
		// ArrayList<String> meetingtimes = new ArrayList<>();
		// meetingtimes.add("Mondays 14:00 to 17:00");
		// meetingtimes.add("Thursdays 10:00 to 12:00");
		// meetingtimes.add("Sundays 14:00 to 1700");
		//
		// meetingPoints.add(new MeetingPoint("Meeting point A",
		// "Location: North Horr, Marsabit", meetingtimes));
		dataAdapter = new MeetingPointsListAdapter(this,
				R.layout.meeting_point_item, meetingPoints);
		listView.setAdapter(dataAdapter);
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
		// Intent homeIntent = new Intent(MeetingPoints.this, Kakuma.class);
		// startActivity(homeIntent);
		// finish();
	}
}
