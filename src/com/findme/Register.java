package com.findme;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.findme.utils.Utils;

public class Register extends AppCompatActivity {

	private Button continueButton;
	private TextView descTextView, policyTextView, findPersonTextView;
	private Typeface typeface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		continueButton = (Button) findViewById(R.id.button_register_continue);
		descTextView = (TextView) findViewById(R.id.textView_register_desc);
		policyTextView = (TextView) findViewById(R.id.textView_register_policy);
		findPersonTextView = (TextView) findViewById(R.id.textView_register_find_person);

		typeface = new Utils(this).getFont("Ubuntu-L");
		findPersonTextView.setTypeface(typeface);
		descTextView.setTypeface(typeface);
		policyTextView.setTypeface(typeface);

		String policy = "<font color='#C1C1C1'>By registering, you agree to use our </font>"
				+ "<font color='#ffffff'><b><u>Terms</u></b> </font>"
				+ "<font color='#C1C1C1'>and that you have read our </font>"
				+ "<font color='#ffffff'><b><u>Privacy Policy</u></b></font>"
				+ "<font color='#C1C1C1'>,including our </font>"
				+ "<font color='#ffffff'><b><u>Cookie Use</u></b></font>"
				+ "<font color='#C1C1C1'>. You may recieve Email notifications and can opt-out at any time</font>";
		policyTextView.setText(Html.fromHtml(policy));

		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent nameIntent = new Intent(Register.this,
						RegisterName.class);
				startActivity(nameIntent);
				finish();
			}
		});

		findPersonTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent findPersonIntent = new Intent(Register.this,
						FindPerson.class);
				startActivity(findPersonIntent);
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent nameIntent = new Intent(Register.this, FindMe.class);
		startActivity(nameIntent);
		finish();
	}
}
