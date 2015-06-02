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

import com.cocosw.bottomsheet.BottomSheet;
import com.kakumapp.utils.Utils;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SearchName extends ActionBarActivity {

	private Toolbar toolbar;
	private ActionBar actionBar;
	private Button continueButton;
	private TextView searchDescrTextView;
	private Typeface typeface;
	private String firstName, fatherName, grandFatherName;
	private MaterialEditText firstNameEditText, fatherNameEditText,
			grandFatherNameEditText;
	private BottomSheet bottomSheet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_name);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		firstNameEditText = (MaterialEditText) findViewById(R.id.edittext_first_name);
		fatherNameEditText = (MaterialEditText) findViewById(R.id.edittext_last_name);
		grandFatherNameEditText = (MaterialEditText) findViewById(R.id.edittext_other_name);
		continueButton = (Button) findViewById(R.id.button_continue);
		searchDescrTextView = (TextView) findViewById(R.id.textView_name_desc);

		// fonts
		typeface = new Utils(this).getFont("Ubuntu-L");
		searchDescrTextView.setTypeface(typeface);

		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isValidInput()) {
					Bundle bundle = new Bundle();
					bundle.putString("searchType", "Name");
					if (firstName != null && !firstName.equals("")) {
						bundle.putString("firstName", firstName);
					}
					if (fatherName != null && !fatherName.equals("")) {
						bundle.putString("fatherName", fatherName);
					}
					if (grandFatherName != null && !grandFatherName.equals("")) {
						bundle.putString("grandFatherName", grandFatherName);
					}
					Intent intent = new Intent(SearchName.this,
							FindPerson.class);
					intent.putExtras(bundle);
					startActivity(intent);
				}
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

	protected boolean isValidInput() {
		boolean valid = true;
		firstName = firstNameEditText.getText().toString().trim();
		fatherName = fatherNameEditText.getText().toString().trim();
		grandFatherName = grandFatherNameEditText.getText().toString().trim();

		if ((firstName == null || firstName.equals(""))
				&& (fatherName == null || fatherName.equals(""))
				&& (grandFatherName == null || grandFatherName.equals(""))) {
			valid = false;
			showErrorMessage("Name", "Enter at least one name");
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

	/**
	 * Show a an message
	 * 
	 * @param title
	 * @param message
	 */
	public void showErrorMessage(String title, String message) {
		if (bottomSheet == null || !bottomSheet.isShowing()) {
			bottomSheet = new BottomSheet.Builder(this,
					R.style.BottomSheet_StyleDialog)
					.icon(R.drawable.ic_action_warning).title(title)
					.sheet(1, message).build();
			bottomSheet.show();
		}
	}

}
