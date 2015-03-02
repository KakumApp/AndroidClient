package com.kakumapp;

import java.io.BufferedReader;
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
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.kakumapp.adapters.Country;
import com.kakumapp.adapters.Place;
import com.kakumapp.utils.Utils;

public class RegisterOrigin extends ActionBarActivity {

	public static final String TAG = "RegisterOrigin";
	private static final String SELECT_COUNTRY = "Select country";
	private static final String URL = "http://kakumapp-api.herokuapp.com/";
	public static final String USERNAME = "admin";
	public static final String PASSWORD = "admin";
	public static int FETCH_TYPE;
	private Button continueButton;
	private TextView findPersonTextView;
	private Typeface typeface;
	private Spinner countriesSpinner;
	private MultiAutoCompleteTextView placesAutoCompleteTextView;
	private String firstName, lastName, otherName, phoneNumber, countryCode,
			selectedCountry;
	private Country country, defaultCountry;
	private ArrayList<String> selectedPlaces;
	private ArrayAdapter<Country> dataAdapterCountries;
	private ArrayAdapter<Place> dataAdapterPlaces;
	private ArrayList<Country> countries = new ArrayList<>();
	private ArrayList<Place> places = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_origin);

		continueButton = (Button) findViewById(R.id.button_register_continue);
		findPersonTextView = (TextView) findViewById(R.id.textView_register_find_person);
		countriesSpinner = (Spinner) findViewById(R.id.spinner_origin_countries);
		placesAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.autocomplete_origin_places);

		typeface = new Utils(this).getFont("Ubuntu-L");
		findPersonTextView.setTypeface(typeface);
		placesAutoCompleteTextView.setHintTextColor(Color.WHITE);

		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goNext();
			}
		});

		findPersonTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent findPersonIntent = new Intent(RegisterOrigin.this,
						Home.class);
				startActivity(findPersonIntent);
			}
		});

		defaultCountry = new Country(0, SELECT_COUNTRY, "");
		countries.add(defaultCountry);

		dataAdapterCountries = new ArrayAdapter<Country>(this,
				android.R.layout.simple_spinner_item, countries);
		dataAdapterCountries
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		countriesSpinner.setAdapter(dataAdapterCountries);

		countriesSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View arg1, int position, long arg3) {
						TextView textView = ((TextView) parent.getChildAt(0));
						if (textView != null) {
							textView.setTextColor(Color.WHITE);
						}
						Country selectedCountry = (Country) parent
								.getItemAtPosition(position);
						country = selectedCountry;
						if (!selectedCountry.getName().equals(SELECT_COUNTRY)) {
							// get the places
							FETCH_TYPE = 2;
							FetchTask fetchTask = new FetchTask();
							fetchTask.addNameValuePair("username", "admin");
							fetchTask.addNameValuePair("password", "admin");
							fetchTask.execute(new String[] { URL + "countries/"
									+ selectedCountry.getId() });
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		dataAdapterPlaces = new ArrayAdapter<Place>(this,
				android.R.layout.simple_list_item_1, places);

		dataAdapterPlaces.setDropDownViewResource(R.layout.spinner_dropdown);
		placesAutoCompleteTextView.setAdapter(dataAdapterPlaces);
		// specify the minimum type of characters before drop-down list is shown
		placesAutoCompleteTextView.setThreshold(1);
		// comma to separate the different places
		placesAutoCompleteTextView
				.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		// when the user clicks an item of the drop-down list
		placesAutoCompleteTextView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View arg1,
							int position, long arg3) {
						Place place = (Place) parent
								.getItemAtPosition(position);

						if (!selectedPlaces.contains(place.getName())) {
							selectedPlaces.add(place.getName());
						}
					}
				});

		// get data if passed
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			// for the phone number
			phoneNumber = bundle.getString("phone");
			countryCode = bundle.getString("code");
			firstName = bundle.getString("firstName");
			lastName = bundle.getString("lastName");
			otherName = bundle.getString("otherName");
			// // for this activity
			selectedCountry = bundle.getString("country");
			selectedPlaces = bundle.getStringArrayList("places");

			if (selectedPlaces != null && !selectedPlaces.isEmpty()) {
				String plcs = "";
				for (int i = 0; i < selectedPlaces.size(); i++) {
					plcs += selectedPlaces.get(i) + ",";
				}
				placesAutoCompleteTextView.setText(plcs);
			} else {
				selectedPlaces = new ArrayList<>();
			}
		}
		// get the countries
		FETCH_TYPE = 1;
		FetchTask fetchTask = new FetchTask();
		fetchTask.addNameValuePair("username", "admin");
		fetchTask.addNameValuePair("password", "admin");
		fetchTask.execute(new String[] { URL + "countries/" });
	}

	protected void goNext() {
		selectedPlaces.clear();
		String places = placesAutoCompleteTextView.getText().toString().trim();
		String[] plcs = places.split(",");
		if (plcs.length > 0) {
			for (String plc : plcs) {
				if (plc != null && !plc.equals("")) {
					selectedPlaces.add(plc);
					
				}
			}
		}
		if (isValidData()) {
			Bundle bundle = new Bundle();
			bundle.putString("phone", phoneNumber);
			bundle.putString("code", countryCode);
			bundle.putString("firstName", firstName);
			bundle.putString("lastName", lastName);
			bundle.putString("otherName", otherName);
			bundle.putString("country", selectedCountry);
			bundle.putStringArrayList("places", selectedPlaces);
			Intent nameIntent = new Intent(RegisterOrigin.this,
					RegisterPhoto.class);
			nameIntent.putExtras(bundle);
			startActivity(nameIntent);
		}
	}

	/**
	 * validate the data entered
	 * 
	 * @return true if valid,false otherwise
	 */
	protected boolean isValidData() {
		boolean valid = false;
		// validate
		if (country != null && !country.getName().equals(SELECT_COUNTRY)) {
			if (!selectedPlaces.isEmpty()) {
				valid = true;
			} else {
				showErrorMessage("Place required",
						"You need to enter at least one of the places");
			}
		} else {
			showErrorMessage("Country required",
					"You need to select one of the countries");
		}
		return valid;
	}

	/**
	 * Show a an message
	 * 
	 * @param title
	 * @param message
	 */
	public void showErrorMessage(String title, String message) {
		BottomSheet bottomSheet = new BottomSheet.Builder(this,
				R.style.BottomSheet_StyleDialog)
				.icon(R.drawable.ic_action_warning).title(title)
				.sheet(1, message).build();
		bottomSheet.show();
	}

	private class FetchTask extends AsyncTask<String, Integer, String> {
		// connection timeout, in milliseconds-waiting to connect
		private static final int CONN_TIMEOUT = 60000;
		// socket timeout, in milliseconds-waiting for data
		private static final int SOCKET_TIMEOUT = 60000;
		private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		public void addNameValuePair(String name, String value) {
			params.add(new BasicNameValuePair(name, value));
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
					JSONArray countriesArray = new JSONArray(response);
					if (countriesArray.length() > 0) {
						countries.clear();
						countries.add(defaultCountry);
						for (int j = 0; j < countriesArray.length(); j++) {
							JSONObject countryJsonObject = countriesArray
									.getJSONObject(j);
							countries.add(new Country(countryJsonObject
									.getLong("id"), countryJsonObject
									.getString("name"), countryJsonObject
									.getString("url")));
						}
						dataAdapterCountries.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					showErrorMessage("Internet connection",
							"Connection could not be established.Check your internet settings.");
				}
			}

			if (FETCH_TYPE == 2) {
				try {
					JSONArray placesArray = new JSONObject(response)
							.getJSONArray("places");
					if (placesArray.length() > 0) {
						for (int i = 0; i < placesArray.length(); i++) {
							JSONObject placeJsonObject = placesArray
									.getJSONObject(i);
							places.add(new Place(placeJsonObject.getLong("id"),
									placeJsonObject.getLong("country"),
									placeJsonObject.getString("name"),
									placeJsonObject.getString("url")));
						}
						dataAdapterPlaces.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					// showErrorMessage("Internet connection",
					// "Connection could not be established.Check your internet settings.");
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
				// HttpGet httpget = new HttpGet(url);
				// response = httpclient.execute(httpget);
				// HttpPost httppost = new HttpPost(url);
				// httppost.setEntity(new UrlEncodedFormEntity(params));
				// response = httpclient.execute(httppost);
				// HttpGet httpget = new HttpGet(url + "?"
				// + URLEncodedUtils.format(params, "utf-8"));
				// response = httpclient.execute(httpget);

				HttpGet request = new HttpGet(url);
				UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
						USERNAME, PASSWORD);
				BasicScheme scheme = new BasicScheme();
				Header authorizationHeader = scheme.authenticate(credentials,
						request);
				request.addHeader(authorizationHeader);
				response = httpclient.execute(request);
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
}
