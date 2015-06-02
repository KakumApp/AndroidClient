package com.kakumapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.kakumapp.utils.Utils;

public class Registered extends ActionBarActivity {

	private KakumaApplication application;
	private TextView textViewGreetings, textViewThanks, textViewInfo1,
			textViewInfo2, textViewInfo3, backTextView;
	private Button continueButton, registerButton;
	private Typeface typeface;
	private String firstName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_success);
		// app
		application = (KakumaApplication) getApplication();
		// views
		textViewThanks = (TextView) findViewById(R.id.textView_thanks);
		textViewGreetings = (TextView) findViewById(R.id.textView_greetings);
		textViewInfo1 = (TextView) findViewById(R.id.textView_info1);
		textViewInfo2 = (TextView) findViewById(R.id.textView_info2);
		textViewInfo3 = (TextView) findViewById(R.id.textView_info3);
		backTextView = (TextView) findViewById(R.id.textView_back);
		continueButton = (Button) findViewById(R.id.button_find_a_person);
		registerButton = (Button) findViewById(R.id.button_register);

		// fonts
		typeface = new Utils(this).getFont("Ubuntu-L");
		textViewThanks.setTypeface(typeface);
		textViewGreetings.setTypeface(typeface);
		textViewInfo1.setTypeface(typeface);
		textViewInfo2.setTypeface(typeface);
		textViewInfo3.setTypeface(typeface);
		backTextView.setTypeface(typeface);

		// get the application data
		firstName = application.getFirstName();

		// display photo and name
		if (firstName != null) {
			textViewGreetings.setText("Hello " + firstName);
		} else {
			textViewGreetings.setText("Hello");
		}

		textViewInfo1
				.setText(Html
						.fromHtml("Other people will now be able to find you using Kakumapp."));
		textViewInfo2
				.setText(Html
						.fromHtml("If someone is looking for you, you will be <u><b>notified on the phone</b></u> number you entered."));
		textViewInfo3
				.setText(Html
						.fromHtml("You can also visit the <u><b>Meeting Points regularly to find out who is looking for you.</b></u>"));

		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				application.clearData();
				Intent homeIntent = new Intent(Registered.this,
						FindPerson.class);
				startActivity(homeIntent);
				finish();
			}
		});

		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				application.clearData();
				Intent registerIntent = new Intent(Registered.this,
						Register.class);
				startActivity(registerIntent);
				finish();
			}
		});

		backTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	public void onBackPressed() {
		application.clearData();
		Intent homeIntent = new Intent(Registered.this, FindPerson.class);
		startActivity(homeIntent);
		finish();
	}
}
