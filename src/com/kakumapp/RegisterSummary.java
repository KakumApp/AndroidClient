package com.kakumapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.kakumapp.utils.Utils;
import com.kakumapp.views.CircularImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

public class RegisterSummary extends ActionBarActivity {

	public static final String TAG = "RegisterSummary";
	private static final String URL = "http://kakumapp-api.herokuapp.com/";
	public static final String USERNAME = "admin";
	public static final String PASSWORD = "admin";
	public static int FETCH_TYPE = 1;
	private String firstName, lastName, otherName, phoneNumber, countryCode;
	private ArrayList<String> places, placesIds;
	// private String placesIds = "";
	private Typeface typeface;
	private TextView nameTextView, phoneTextView, placeTextView, textView_desc;
	private MaterialEditText nameEditText, phoneEditText, placesEditText;
	private CircularImageView circularImageView;
	private BottomSheet bottomSheet;
	private Button registerButton;
	public String jsonData;
	private KakumaApplication application;
	private File photoFile;
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_summary);

		application = (KakumaApplication) getApplication();

		nameTextView = (TextView) findViewById(R.id.textView_name);
		phoneTextView = (TextView) findViewById(R.id.textView_phone);
		textView_desc = (TextView) findViewById(R.id.textView_desc);
		placeTextView = (TextView) findViewById(R.id.textView_origin);
		circularImageView = (CircularImageView) findViewById(R.id.image_view_photo);
		nameEditText = (MaterialEditText) findViewById(R.id.edittext_name);
		phoneEditText = (MaterialEditText) findViewById(R.id.edittext_phone);
		placesEditText = (MaterialEditText) findViewById(R.id.edittext_origin);
		registerButton = (Button) findViewById(R.id.button_register);

		typeface = new Utils(this).getFont("Ubuntu-L");
		nameTextView.setTypeface(typeface);
		phoneTextView.setTypeface(typeface);
		textView_desc.setTypeface(typeface);
		placeTextView.setTypeface(typeface);

		places = new ArrayList<>();
		placesIds = new ArrayList<>();

		firstName = application.getFirstName();
		lastName = application.getLastName();
		otherName = application.getOtherName();
		phoneNumber = application.getPhoneNumber();
		countryCode = application.getCountryCode();
		places = application.getSelectedPlaces();
		placesIds = application.getSelectedPlacesIds();
		photoFile = application.getPhotoFile();

		BitmapFactory.Options options = new BitmapFactory.Options();
		bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
		// bitmap = Bitmap.createScaledBitmap(bitmap, parent.getWidth(),
		// parent.getHeight(), true);

		if (firstName != null && lastName != null) {
			nameEditText.setText(firstName + " " + lastName);
			if (otherName != null) {
				nameEditText.append(" " + otherName);
			} else {
				otherName = null;
			}
		}

		if (phoneNumber != null) {
			if (phoneNumber.startsWith("0") && phoneNumber.length() > 1) {
				phoneNumber = phoneNumber.substring(1);
			}
			phoneEditText.setText(countryCode + phoneNumber);
		}

		if (places != null) {
			String plcs = "";
			for (String plc : places) {
				plcs += plc + ",";
			}
			placesEditText.setText(plcs);
		}

		if (bitmap != null) {
			circularImageView.setImageBitmap(bitmap);
		}

		//
		// // get data if passed
		// Bundle bundle = getIntent().getExtras();
		// if (bundle != null) {
		// // for the phone number
		// phoneNumber = bundle.getString("phone");
		// countryCode = bundle.getString("code");
		//
		// firstName = bundle.getString("firstName");
		// lastName = bundle.getString("lastName");
		// otherName = bundle.getString("otherName");
		// country = bundle.getString("country");
		// places = bundle.getStringArrayList("places");
		// placesIds = bundle.getStringArrayList("placesIds");
		//
		// if (firstName != null && lastName != null) {
		// nameEditText.setText(firstName + " " + lastName);
		// if (otherName != null) {
		// nameEditText.append(" " + otherName);
		// } else {
		// otherName = null;
		// }
		// }
		//
		// if (phoneNumber != null) {
		// phoneEditText.setText(phoneNumber);
		// }
		//
		// if (places != null) {
		// String plcs = "";
		// for (String plc : places) {
		// plcs += plc + ",";
		// }
		// placesEditText.setText(plcs);
		// }
		//
		// if (bitmap != null) {
		// circularImageView.setImageBitmap(bitmap);
		// }
		// }

		// for (int i = 0; i < RegisterOrigin.places.size(); i++) {
		// String id = RegisterOrigin.places.get(i).getId() + "";
		// Log.e(TAG, "Id " + id);
		// placesIds.add(id);
		// // placesIds += RegisterOrigin.places.get(i).getId() + ",";
		// }
		// if (placesIds.endsWith(",")) {
		// placesIds = placesIds.substring(0, placesIds.length() - 1);
		// }

		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e(TAG, "Othername " + otherName);
				for (String id : placesIds) {
					Log.e(TAG, "Id " + id);
				}
				RegisterTask registerTask = new RegisterTask(
						RegisterTask.POST_TASK);
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("first_name", firstName);
					jsonObject.put("last_name", lastName);
					jsonObject.put("other_name", otherName);
					jsonObject.put("phone_no", phoneNumber);
					jsonObject.put("photo", "URL");
					JSONArray placesArray = new JSONArray();
					for (String id : placesIds) {
						placesArray.put(id);
					}
					jsonObject.put("places", placesArray);
					jsonData = jsonObject.toString();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// registerTask.addNameValuePair("first_name", firstName);
				// registerTask.addNameValuePair("last_name", lastName);
				// registerTask.addNameValuePair("other_name", otherName);
				// registerTask.addNameValuePair("phone_no", phoneNumber);
				// registerTask.addNameValuePair("places[]",
				// placesIds.toArray(new String[placesIds.size()]));
				// registerTask.addNameValuePair("photo", "URL");
				registerTask.execute(new String[] { URL + "targets/" });
			}
		});
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

	private class RegisterTask extends AsyncTask<String, Integer, String> {
		public static final int POST_TASK = 1;
		public static final int GET_TASK = 2;
		// connection timeout, in milliseconds-waiting to connect
		private static final int CONN_TIMEOUT = 60000;
		// socket timeout, in milliseconds-waiting for data
		private static final int SOCKET_TIMEOUT = 60000;
		private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		private int TASK_TYPE;

		public RegisterTask(int TASK_TYPE) {
			this.TASK_TYPE = TASK_TYPE;
		}

		/**
		 * Add name value pairs for simple strings
		 * 
		 * @param name
		 * @param value
		 */
		public void addNameValuePair(String name, String value) {
			params.add(new BasicNameValuePair(name, value));
		}

		/**
		 * Add name value pairs for string arrays
		 * 
		 * @param name
		 * @param value
		 */
		public void addNameValuePair(String name, String[] values) {
			for (String value : values) {
				params.add(new BasicNameValuePair(name, value));
			}
		}

		protected String doInBackground(String... urls) {
			String url = urls[0];
			String result = "";
			HttpResponse response = doResponse(url);
			if (response == null) {
				return result;
			} else {
				try {
					result = inputStreamToString(response.getEntity()
							.getContent());
				} catch (IllegalStateException e) {
					Log.e(TAG, e.getLocalizedMessage(), e);
				} catch (IOException e) {
					Log.e(TAG, e.getLocalizedMessage(), e);
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(String response) {
			Log.e(TAG, "type is " + FETCH_TYPE + " response is " + response);
			if (FETCH_TYPE == 1) {
				try {
					JSONObject jsonObject = new JSONObject(response);

				} catch (JSONException e) {
					showErrorMessage("Internet connection",
							"Connection could not be established.Check your internet settings.");
				}
			}
		}

		/**
		 * Establish connection and socket (data retrieval) timeouts
		 * 
		 * @return
		 */
		private HttpParams getHttpParams() {
			HttpParams htpp = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
			HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);
			return htpp;
		}

		/**
		 * Use our connection and data timeouts as parameters for our
		 * DefaultHttpClient
		 * 
		 * @param url
		 * @return
		 */
		private HttpResponse doResponse(String url) {
			HttpClient httpclient = new DefaultHttpClient(getHttpParams());
			HttpResponse response = null;
			try {
				UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
						USERNAME, PASSWORD);
				BasicScheme scheme = new BasicScheme();
				Header authorizationHeader;
				switch (TASK_TYPE) {
				case POST_TASK:
					HttpPost httppost = new HttpPost(url);
					StringEntity entity = new StringEntity(jsonData, HTTP.UTF_8);
					entity.setContentType("application/json");
					httppost.setEntity(entity);
					// httppost.setEntity(new UrlEncodedFormEntity(params));
					authorizationHeader = scheme.authenticate(credentials,
							httppost);
					httppost.addHeader(authorizationHeader);
					response = httpclient.execute(httppost);
					break;
				case GET_TASK:
					HttpGet request = new HttpGet(url);
					authorizationHeader = scheme.authenticate(credentials,
							request);
					request.addHeader(authorizationHeader);
					response = httpclient.execute(request);
					break;
				}

			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
			}
			return response;
		}

		/**
		 * 
		 * @param is
		 * @return
		 */
		private String inputStreamToString(InputStream is) {
			String line = "";
			StringBuilder total = new StringBuilder();
			// Wrap a BufferedReader around the InputStream
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(is));
			try {
				// Read response until the end
				while ((line = bufferedReader.readLine()) != null) {
					total.append(line);
				}
			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
			}
			// Return full string
			return total.toString();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent nameIntent = new Intent(RegisterSummary.this,
				RegisterPhoto.class);
		startActivity(nameIntent);
		finish();
	}
}
