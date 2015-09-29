package com.kakumapp.adapters;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakumapp.R;

public class MeetingPointsListAdapter extends ArrayAdapter<MeetingPoint> {

	private AppCompatActivity context;

	public MeetingPointsListAdapter(AppCompatActivity context,
			int textViewResourceId) {
		super(context, textViewResourceId);
		this.context = context;
	}

	public MeetingPointsListAdapter(AppCompatActivity context, int resource,
			List<MeetingPoint> items) {
		super(context, resource, items);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View activityView = convertView;
		if (activityView == null) {
			LayoutInflater vi;
			vi = LayoutInflater.from(getContext());
			activityView = vi.inflate(R.layout.meeting_point_item, null);
		}
		final MeetingPoint meetingPoint = getItem(position);
		if (meetingPoint != null) {
			TextView titleTextView = (TextView) activityView
					.findViewById(R.id.title_textView);
			TextView locationTextView = (TextView) activityView
					.findViewById(R.id.location_textView);
			TextView timesTextView = (TextView) activityView
					.findViewById(R.id.meeting_points);

			String title = meetingPoint.getTitle();
			String location = meetingPoint.getTitle();
			ArrayList<String> times = meetingPoint.getMeetingTimes();

			if (title != null) {
				titleTextView.setText(title);
			}

			if (location != null) {
				locationTextView.setText(location);
			}

			if (times != null) {
				String timesString = "";
				for (String time : times) {
					timesString += time + "\n";
				}
				timesTextView.setText(timesString);
			}

			final ImageView overflowImageView = (ImageView) activityView
					.findViewById(R.id.overflow);
			overflowImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// Creating the instance of PopupMenu
					PopupMenu popup = new PopupMenu(context, overflowImageView);
					// Inflating the Popup using xml file
					popup.getMenuInflater().inflate(R.menu.meeting_points_menu,
							popup.getMenu());
					// registering popup with OnMenuItemClickListener
					popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

						public boolean onMenuItemClick(MenuItem item) {
							return true;
						}
					});
					popup.show();
				}
			});
		}
		return activityView;
	}
}