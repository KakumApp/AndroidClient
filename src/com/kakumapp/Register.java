package com.kakumapp;

import com.kakumapp.utils.Utils;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Register extends ActionBarActivity {

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

		String policy = "By registering, you agree to use our <b>Terms</b> and that you have read our <b>Privacy Policy</b>, "
				+ "including our <b>Cookie Use</b>. You may recieve Email notifications and can opt-out at any time";
		policyTextView.setText(Html.fromHtml(policy));

		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent nameIntent=new Intent(Register.this,RegisterName.class);
				startActivity(nameIntent);
				finish();
			}
		});

		findPersonTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent findPersonIntent = new Intent(Register.this, MeetingPoints.class);
				startActivity(findPersonIntent);
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent nameIntent = new Intent(Register.this, Kakuma.class);
		startActivity(nameIntent);
		finish();
	}
}
