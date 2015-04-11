package com.kakumapp.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakumapp.R;

public class RegisteredPersonsAdapter extends
		RecyclerView.Adapter<RegisteredPersonsAdapter.ViewHolder> {
	private ArrayList<RegisteredPerson> registeredPersons;
	private Context context;

	// constructor
	public RegisteredPersonsAdapter(
			ArrayList<RegisteredPerson> registeredPersons, Context context) {
		this.registeredPersons = registeredPersons;
		this.context = context;
	}

	// Create new views (invoked by the layout manager)
	@Override
	public RegisteredPersonsAdapter.ViewHolder onCreateViewHolder(
			ViewGroup parent, int viewType) {
		// create a new view
		View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.recently_registered_item, null);
		// create ViewHolder
		ViewHolder viewHolder = new ViewHolder(itemLayoutView);
		return viewHolder;
	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		// - get data from your itemsData at this position
		RegisteredPerson person = registeredPersons.get(position);

		String name = person.getName();
		String location = person.getLocation();
		String phone = person.getPhone();
		Bitmap photo = person.getPhoto();

		if (name != null) {
			viewHolder.nameTextView.setText(name);
		}

		if (location != null) {
			viewHolder.locationTextView.setText(location);
		}

		if (phone != null) {
			viewHolder.phoneTextView.setText(phone);
		}

		if (photo != null) {
			viewHolder.photoImageView.setImageBitmap(photo);
		} else {
			viewHolder.photoImageView
					.setImageResource(R.drawable.default_profile);
		}
		viewHolder.meetingButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent("com.kakumapp.MeetingPoints");
				context.startActivity(intent);
			}
		});
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return registeredPersons.size();
	}

	// inner class to hold a reference to each item of RecyclerView
	public static class ViewHolder extends RecyclerView.ViewHolder {
		public TextView nameTextView, locationTextView, phoneTextView;
		private ImageView photoImageView;
		private Button meetingButton;

		public ViewHolder(View itemLayoutView) {
			super(itemLayoutView);

			nameTextView = (TextView) itemLayoutView
					.findViewById(R.id.textView_name);
			locationTextView = (TextView) itemLayoutView
					.findViewById(R.id.textView_origin);
			phoneTextView = (TextView) itemLayoutView
					.findViewById(R.id.textView_phone);
			photoImageView = (ImageView) itemLayoutView
					.findViewById(R.id.image_view_photo);
			meetingButton = (Button) itemLayoutView
					.findViewById(R.id.button_meeting_point);
		}
	}

}