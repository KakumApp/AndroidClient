package com.kakumapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cocosw.bottomsheet.BottomSheet;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.kakumapp.adapters.Country;
import com.kakumapp.adapters.Place;
import com.kakumapp.utils.Utils;

public class SearchOrigin extends AppCompatActivity {

	public static final String TAG = "SearchOrigin";
	private static final String SELECT_COUNTRY = "Select country";
	private static final String URL = "http://kakumapp-api.herokuapp.com/";
	public static final String USERNAME = "admin";
	public static final String PASSWORD = "admin";
	public static int FETCH_TYPE, indexOfPlace;
	private Spinner countriesSpinner;
	private MultiAutoCompleteTextView placesAutoCompleteTextView;
	public static Country country;
	private Country defaultCountry;
	private ArrayList<String> selectedPlaces = new ArrayList<>();
	private ArrayAdapter<Country> dataAdapterCountries;
	private ArrayAdapter<Place> dataAdapterPlaces;
	private ArrayList<Country> countries = new ArrayList<>();
	private ArrayList<Place> places = new ArrayList<>();
	private ArrayList<String> placeNames = new ArrayList<>();
	private BottomSheet bottomSheet;
	private ProgressBarCircularIndeterminate progressBar;
	private MaterialDialog dialog;
	private Toolbar toolbar;
	private ActionBar actionBar;
	private Button continueButton;
	private TextView searchDescrTextView;
	private Typeface typeface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_origin);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		continueButton = (Button) findViewById(R.id.button_continue);
		searchDescrTextView = (TextView) findViewById(R.id.textView_origin_desc);
		progressBar = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBar);
		placesAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.autocomplete_origin_places);
		countriesSpinner = (Spinner) findViewById(R.id.spinner_origin_countries);

		// fonts
		typeface = new Utils(this).getFont("Ubuntu-L");
		searchDescrTextView.setTypeface(typeface);

		// hints color
		int hintTextColor = getResources().getColor(R.color.primary);
		placesAutoCompleteTextView.setHintTextColor(hintTextColor);

		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isValidInput()) {
					Bundle bundle = new Bundle();
					bundle.putString("searchType", "Origin");
					if (!country.getName().equals(SELECT_COUNTRY)) {
						bundle.putString("country", country.getName());
					}
					bundle.putStringArrayList("places", selectedPlaces);
					Intent intent = new Intent(SearchOrigin.this,
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
		
		// create the spinners adapters
		String[] countriesLocal = { "South Sudan", "Sudan", "Somalia",
				"Ethiopia", "D.R. Congo", "Burundi", "Rwanda", "Eritrea",
				"Uganda" };
		defaultCountry = new Country(0, SELECT_COUNTRY, "");
		countries.add(defaultCountry);
		for (int i = 0; i < countriesLocal.length; i++) {
			countries.add(new Country(i + 1, countriesLocal[i],
					"http://kakumapp-api.herokuapp.com/countries/" + (i + 1)));
		}
		// dataAdapterCountries = new ArrayAdapter<Country>(this,
		// android.R.layout.simple_spinner_item, countries);
		dataAdapterCountries = createDataAdapterCountries(countries);
		countriesSpinner.setAdapter(dataAdapterCountries);
		// when a country is selected,get the places under that country
		countriesSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View arg1, int position, long arg3) {
						TextView textView = ((TextView) parent.getChildAt(0));
						if (textView != null) {
							textView.setTextColor(getResources().getColor(
									R.color.primary));
						}
						country = (Country) parent.getItemAtPosition(position);
						if (!country.getName().equals(SELECT_COUNTRY)) {
							// get the places
							FETCH_TYPE = 2;
							FetchTask fetchTask = new FetchTask();
							fetchTask.execute(new String[] { URL + "countries/"
									+ country.getId() });
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		// places spinner adatapter
		// dataAdapterPlaces = new ArrayAdapter<Place>(this,
		// android.R.layout.simple_list_item_1, places);
		dataAdapterPlaces = createDataAdapterPlaces(places);
		placesAutoCompleteTextView.setAdapter(dataAdapterPlaces);
		// specify the minimum type of characters before drop-down list is shown
		placesAutoCompleteTextView.setThreshold(1);
		// comma to separate the different places
		placesAutoCompleteTextView
				.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

		/**
		 * In an effort to speed up the process of fetching countries,the
		 * countries will be localized (hardcoded :) )
		 */
		// // get the countries
		// FETCH_TYPE = 1;
		// FetchTask fetchTask = new FetchTask();
		// fetchTask.execute(new String[] { URL + "countries/" });
	}

	/**
	 * 
	 * @param places
	 * @return
	 */
	private ArrayAdapter<Place> createDataAdapterPlaces(ArrayList<Place> places) {
		ArrayAdapter<Place> dataAdapterPlaces = new ArrayAdapter<Place>(this,
				android.R.layout.simple_list_item_1, places) {

			@Override
			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text = (TextView) view
						.findViewById(android.R.id.text1);
				text.setTextColor(getResources().getColor(R.color.dark_grey));
				return view;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text = (TextView) view
						.findViewById(android.R.id.text1);
				text.setTextColor(getResources().getColor(R.color.dark_grey));
				return view;
			}
		};
		dataAdapterPlaces.setDropDownViewResource(R.layout.spinner_dropdown);
		return dataAdapterPlaces;
	}

	/**
	 * 
	 * @param countries
	 * @return
	 */
	private ArrayAdapter<Country> createDataAdapterCountries(
			ArrayList<Country> countries) {
		ArrayAdapter<Country> dataAdapterCountries = new ArrayAdapter<Country>(
				this, android.R.layout.simple_spinner_dropdown_item, countries) {

			@Override
			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text = (TextView) view
						.findViewById(android.R.id.text1);
				text.setTextColor(getResources().getColor(R.color.dark_grey));
				return view;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text = (TextView) view
						.findViewById(android.R.id.text1);
				text.setTextColor(getResources().getColor(R.color.dark_grey));
				return view;
			}
		};
		dataAdapterCountries
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return dataAdapterCountries;
	}

	// show progress bar and hide button
	private void showProgress() {
		if (progressBar != null && continueButton != null) {
			progressBar.setVisibility(View.VISIBLE);
			continueButton.setVisibility(View.GONE);
		}
	}

	// hide progress bar and show button
	private void hideprogress() {
		if (progressBar != null && continueButton != null) {
			progressBar.setVisibility(View.GONE);
			continueButton.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * validate input
	 * 
	 * check if there are any places that the user has entered
	 * 
	 * @return
	 */
	protected boolean isValidInput() {
		boolean valid = false;
		// check if there are any places that the user has entered
		selectedPlaces.clear();
		String placesEntered = placesAutoCompleteTextView.getText().toString()
				.trim();
		String[] plcs = placesEntered.split(",");
		if (plcs.length > 0) {
			for (String plc : plcs) {
				if (plc != null && !plc.equals("")) {
					selectedPlaces.add(plc.trim());
				}
			}
		}
		if (selectedPlaces.size() > 0) {
			valid = true;
		} else {
			showErrorMessage("Place required",
					"You need to enter at least one of the places");
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
		if (bottomSheet == null || !bottomSheet.isShowing()) {
			bottomSheet = new BottomSheet.Builder(this,
					R.style.BottomSheet_StyleDialog)
					.icon(R.drawable.ic_action_warning).title(title)
					.sheet(1, message).build();
			bottomSheet.show();
		}
	}

	// show a retry dialog for poor/no internet connection
	public void showRetry() {
		hideprogress();
		// show only if there are no other dialogs already showing
		if (dialog == null || !dialog.isShowing()) {
			dialog = new MaterialDialog.Builder(this)
					.title(R.string.connect_error)
					.content(R.string.connection_error_message)
					.positiveText(R.string.yes).negativeText(R.string.cancel)
					.callback(new MaterialDialog.ButtonCallback() {
						@Override
						public void onPositive(MaterialDialog dialog) {
							// refetch the data/retry to send some data
							FetchTask fetchTask = null;
							switch (FETCH_TYPE) {
							// case 1:
							// fetchTask = new FetchTask();
							// fetchTask.execute(new String[] { URL
							// + "countries/" });
							// break;
							case 2:
								fetchTask = new FetchTask();
								fetchTask.execute(new String[] { URL
										+ "countries/" + country.getId() });
								break;
							default:
								break;
							}
						}

						@Override
						public void onNegative(MaterialDialog dialog) {
						}
					}).build();
			dialog.setCancelable(false);
			dialog.show();
		}
	}

	/**
	 * This class does pulling some data
	 * 
	 * @author paul
	 * 
	 */
	private class FetchTask extends AsyncTask<String, Integer, String> {
		// connection timeout, in milliseconds-waiting to connect
		private static final int CONN_TIMEOUT = 60000;
		// socket timeout, in milliseconds-waiting for data
		private static final int SOCKET_TIMEOUT = 60000;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgress();
		}

		/**
		 * execute either GET/POST
		 */
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
				} catch (Exception e) {
					Log.e(TAG, e.getLocalizedMessage(), e);
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(String response) {
			Log.e(TAG, "type is " + FETCH_TYPE + " response is " + response);
			// fetch all countries
			if (FETCH_TYPE == 1) {
				hideprogress();
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
						// dataAdapterCountries = new ArrayAdapter<Country>(
						// SearchOrigin.this,
						// android.R.layout.simple_spinner_item, countries);
						dataAdapterCountries = createDataAdapterCountries(countries);
						countriesSpinner.setAdapter(dataAdapterCountries);
					}
				} catch (JSONException e) {
					showRetry();
				}
			}
			// fetching places under a chosen country
			else if (FETCH_TYPE == 2) {
				hideprogress();
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
							placeNames.add(placeJsonObject.getString("name"));
						}
						// dataAdapterPlaces = new ArrayAdapter<Place>(
						// SearchOrigin.this,
						// android.R.layout.simple_list_item_1, places);
						// dataAdapterPlaces
						// .setDropDownViewResource(R.layout.spinner_dropdown);
						dataAdapterPlaces = createDataAdapterPlaces(places);
						placesAutoCompleteTextView
								.setAdapter(dataAdapterPlaces);
					}
				} catch (JSONException e) {
					showRetry();
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
				HttpGet request = new HttpGet(url);
				authorizationHeader = scheme.authenticate(credentials, request);
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
}
