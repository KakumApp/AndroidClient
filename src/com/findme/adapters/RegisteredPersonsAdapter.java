package com.findme.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

import com.findme.R;
import com.findme.views.SmartImageView;

public class RegisteredPersonsAdapter extends
		RecyclerView.Adapter<RegisteredPersonsAdapter.ViewHolder> {
	protected static final String TAG = "RegisteredPersonsAdapter";
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
		measureSize(itemLayoutView);
		// create ViewHolder
		ViewHolder viewHolder = new ViewHolder(itemLayoutView);
		return viewHolder;
	}
	
	private void measureSize(final View view) {
		ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
		if (viewTreeObserver.isAlive()) {
			viewTreeObserver
					.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
						@Override
						public void onGlobalLayout() {
							if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
								view.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
							} else {
								view.getViewTreeObserver()
										.removeOnGlobalLayoutListener(this);
							}
							int viewWidth = view.getWidth();
							int viewHeight = view.getHeight();
							Log.e(TAG, "Width " + viewWidth + " Height "
									+ viewHeight);
						}
					});
		}
	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		// - get data from your itemsData at this position
		RegisteredPerson person = registeredPersons.get(position);

		String name = person.getName();
		String location = "";
		ArrayList<Place> places = person.getPlaces();
		for (Place place : places) {
			location += place.getName() + ",";
		}
		String phone = person.getPhone();
		String photoURL = person.getPhotoURL();

		if (name != null) {
			viewHolder.nameTextView.setText(name);
		}

		if (location != null) {
			viewHolder.locationTextView.setText(location);
		}

		if (phone != null) {
			viewHolder.phoneTextView.setText(phone);
		}

		if (photoURL != null && Patterns.WEB_URL.matcher(photoURL).matches()) {
			viewHolder.photoImageView.setImageUrl(photoURL);
		} else {
			viewHolder.photoImageView
					.setImageResource(R.drawable.ic_default_profile);
		}
		// viewHolder.meetingButton.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent(context, MeetingPoints.class);
		// context.startActivity(intent);
		// }
		// });
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return registeredPersons.size();
	}

	// inner class to hold a reference to each item of RecyclerView
	public static class ViewHolder extends RecyclerView.ViewHolder {
		public TextView nameTextView, locationTextView, phoneTextView;
		private SmartImageView photoImageView;

		// private Button meetingButton;

		public ViewHolder(View itemLayoutView) {
			super(itemLayoutView);

			nameTextView = (TextView) itemLayoutView
					.findViewById(R.id.textView_name);
			locationTextView = (TextView) itemLayoutView
					.findViewById(R.id.textView_origin);
			phoneTextView = (TextView) itemLayoutView
					.findViewById(R.id.textView_phone);
			photoImageView = (SmartImageView) itemLayoutView
					.findViewById(R.id.image_view_photo);
			// meetingButton = (Button) itemLayoutView
			// .findViewById(R.id.button_meeting_point);
		}
	}

}