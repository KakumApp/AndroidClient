package com.findme;

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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.findme.adapters.GridAutofitLayoutManager;
import com.findme.adapters.Place;
import com.findme.adapters.RegisteredPerson;
import com.findme.adapters.RegisteredPersonsAdapter;
import com.findme.utils.Utils;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

@SuppressWarnings("deprecation")
public class SearchResults extends AppCompatActivity {

	public static final String TAG = "SearchResults";
	private static String URL, SEARCH_URL = "";
	private Toolbar toolbar;
	private ActionBar actionBar;
	private RecyclerView recyclerView;
	@SuppressWarnings("rawtypes")
	private RecyclerView.Adapter mAdapter;
	private ArrayList<RegisteredPerson> registeredPersons = new ArrayList<>();
	private Button searchButton;
	private String searchType;
	private ProgressBarCircularIndeterminate progressBar;
	private MaterialDialog dialog;
	private boolean search;
	private FindMeApplication application;
	private TextView noResultsTextView;
	private Typeface typeface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		// instance of the global application
		application = (FindMeApplication) getApplication();
		URL = FindMeApplication.APIURL + "targets?";

		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		searchButton = (Button) findViewById(R.id.button_search);
		progressBar = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBar);
		noResultsTextView = (TextView) findViewById(R.id.textView_no_results);

		// fonts
		typeface = new Utils(this).getFont("Ubuntu-L");
		noResultsTextView.setTypeface(typeface);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		recyclerView.setHasFixedSize(true);
		// use a custom layout manager
		recyclerView.setLayoutManager(new GridAutofitLayoutManager(this, 720));
		// specify an adapter
		mAdapter = new RegisteredPersonsAdapter(registeredPersons, this);
		recyclerView.setAdapter(mAdapter);

		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent searchOptions = new Intent(SearchResults.this,
						SearchOptions.class);
				startActivity(searchOptions);
			}
		});
		// get the parameters sent
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey("searchType")) {
			// search type
			searchType = extras.getString("searchType");
			if (searchType != null) {
				search = true;
				actionBar.setTitle("Results - " + searchType);
				SEARCH_URL = "";
				/**
				 * form URL of the form
				 * search=param1&search=param2&...search=paramn
				 */
				// append name param
				if (searchType.equalsIgnoreCase("Name")) {
					// append all params
					if (extras.containsKey("firstName")) {
						SEARCH_URL += "search=" + extras.getString("firstName")
								+ "&";
					}
					if (extras.containsKey("fatherName")) {
						SEARCH_URL += "search="
								+ extras.getString("fatherName") + "&";
					}
					if (extras.containsKey("grandFatherName")) {
						SEARCH_URL += "search="
								+ extras.getString("grandFatherName") + "&";
					}
				}
				// append phone param
				else if (searchType.equalsIgnoreCase("Phone")) {
					SEARCH_URL += "search=" + extras.getString("phone");
				}
				// append country and places param
				else if (searchType.equalsIgnoreCase("Origin")) {
					if (extras.containsKey("country")) {
						String country = extras.getString("country");
						country = country.replaceAll("\\s", "+");
						SEARCH_URL += "search=" + country + "&";
					}
					ArrayList<String> places = extras
							.getStringArrayList("places");
					for (String place : places) {
						SEARCH_URL += "search=" + place + "&";
					}
					if (SEARCH_URL.endsWith("&")) {
						SEARCH_URL = SEARCH_URL.substring(0,
								SEARCH_URL.length() - 1);
					}
				}
				SEARCH_URL = URL + SEARCH_URL;
				SearchTask searchTask = new SearchTask();
				searchTask.execute(new String[] { SEARCH_URL });
			}
		} else {
			SearchTask searchTask = new SearchTask();
			searchTask.execute(new String[] { URL });
		}

		findViewById(R.id.button_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});

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

	// show progress bar
	private void showProgress() {
		if (progressBar != null) {
			progressBar.setVisibility(View.VISIBLE);
		}
	}

	// hide progress bar
	private void hideprogress() {
		if (progressBar != null) {
			progressBar.setVisibility(View.GONE);
		}
	}

	/**
	 * This class does search based on the filters provided
	 * 
	 * @author paul
	 * 
	 */
	private class SearchTask extends AsyncTask<String, Integer, String> {
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
		 * fetch data
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
			Log.e(TAG, " response is " + response);
			hideprogress();
			try {
				JSONArray resultsArray = new JSONArray(response);
				if (resultsArray.length() > 0) {
					registeredPersons.clear();
					int length = resultsArray.length();
					if (resultsArray.length() > 4) {
						length = search ? resultsArray.length() : 4;
					}
					for (int j = 0; j < length; j++) {
						JSONObject personJsonObject = resultsArray
								.getJSONObject(j);
						String url = personJsonObject.getString("url");
						String name = "";
						String firstName = personJsonObject
								.getString("first_name");
						String fatherName = personJsonObject
								.getString("last_name");
						String grandFatherName = personJsonObject
								.getString("other_name");
						String photoURL = personJsonObject.getString("photo");
						name = firstName + " " + fatherName;
						if (grandFatherName != null
								&& !grandFatherName.equals("null")) {
							name += " " + grandFatherName;
						}
						ArrayList<Place> places = new ArrayList<>();
						JSONArray placesArray = personJsonObject
								.getJSONArray("places");
						if (placesArray.length() > 0) {
							for (int i = 0; i < placesArray.length(); i++) {
								JSONObject placeJsonObject = placesArray
										.getJSONObject(i);
								places.add(new Place(placeJsonObject
										.getLong("id"), placeJsonObject
										.getLong("country"), placeJsonObject
										.getString("name"), placeJsonObject
										.getString("url")));
							}
						}
						String phone = personJsonObject.getString("phone_no");
						registeredPersons.add(new RegisteredPerson(url, name,
								places, phone, photoURL));
					}
					// specify an adapter
					mAdapter = new RegisteredPersonsAdapter(registeredPersons,
							SearchResults.this);
					recyclerView.setAdapter(mAdapter);
				} else {
					noResultsTextView.setVisibility(View.VISIBLE);
				}
			} catch (JSONException e) {
				Log.e(TAG, "Exception " + e.getLocalizedMessage());
				showRetry();
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
						application.getApiUsername(),
						application.getApiPassword());
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
							SearchTask searchTask = new SearchTask();
							searchTask.execute(new String[] { SEARCH_URL });
						}

						@Override
						public void onNegative(MaterialDialog dialog) {
						}
					}).build();
			dialog.setCancelable(false);
			dialog.show();
		}
	}
}
