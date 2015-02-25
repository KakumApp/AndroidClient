package com.kakumapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kakumapp.utils.Utils;

public class RegisterName extends ActionBarActivity {

	private Button continueButton;
	private TextView descTextView, findPersonTextView;
	private Typeface typeface;
	private EditText firstNameEditText, lastNameEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_name);

		continueButton = (Button) findViewById(R.id.button_register_continue);
		descTextView = (TextView) findViewById(R.id.textView_name_desc);
		findPersonTextView = (TextView) findViewById(R.id.textView_register_find_person);
		firstNameEditText = (EditText) findViewById(R.id.edittext_first_name);
		lastNameEditText = (EditText) findViewById(R.id.edittext_last_name);

		typeface = new Utils(this).getFont("Ubuntu-L");
		findPersonTextView.setTypeface(typeface);
		descTextView.setTypeface(typeface);
		firstNameEditText.setHintTextColor(Color.WHITE);
		lastNameEditText.setHintTextColor(Color.WHITE);

		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent originIntent = new Intent(RegisterName.this,
						RegisterOrigin.class);
				startActivity(originIntent);
			}
		});

		findPersonTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent findPersonIntent = new Intent(RegisterName.this, Home.class);
				startActivity(findPersonIntent);
			}
		});

	}
}
