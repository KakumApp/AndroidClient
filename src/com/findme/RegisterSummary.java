package com.findme;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.mobileconnectors.s3.transfermanager.Transfer.TransferState;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.s3.transfermanager.Upload;
import com.cocosw.bottomsheet.BottomSheet;
import com.findme.utils.Utils;
import com.findme.views.CircularImageView;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

/**
 * 
 * @author paul
 * 
 */
public class RegisterSummary extends AppCompatActivity {

	public static final String TAG = "RegisterSummary";
	private static String URL;
	private static String PHOTO_URL;
	private static String PHOTO = "URL";
	public static int FETCH_TYPE = 1;
	private String firstName, lastName, otherName, phoneNumber;
	private ArrayList<String> places, placesIds;
	private Typeface typeface;
	private TextView nameTextView, phoneTextView, placeTextView;
	private TextView nameEditText, phoneEditText, placesEditText;
	private CircularImageView circularImageView;
	private BottomSheet bottomSheet;
	private Button registerButton;
	public String jsonData;
	private FindMeApplication application;
	private File photoFile;
	private Bitmap bitmap;
	private ProgressBarCircularIndeterminate progressBar;
	private LinearLayout optionsLayout;
	private MaterialDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_summary);
		// app
		application = (FindMeApplication) getApplication();
		URL = FindMeApplication.APIURL;
		// get the views
		nameTextView = (TextView) findViewById(R.id.textView_name);
		phoneTextView = (TextView) findViewById(R.id.textView_phone);
		placeTextView = (TextView) findViewById(R.id.textView_origin);
		circularImageView = (CircularImageView) findViewById(R.id.image_view_photo);
		nameEditText = (TextView) findViewById(R.id.edittext_name);
		phoneEditText = (TextView) findViewById(R.id.edittext_phone);
		placesEditText = (TextView) findViewById(R.id.edittext_origin);
		registerButton = (Button) findViewById(R.id.button_register);
		progressBar = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBar);
		optionsLayout = (LinearLayout) findViewById(R.id.options_layout);

		// fonts
		typeface = new Utils(this).getFont("Ubuntu-L");
		nameTextView.setTypeface(typeface);
		phoneTextView.setTypeface(typeface);
		placeTextView.setTypeface(typeface);

		places = new ArrayList<>();
		placesIds = new ArrayList<>();
		// get the data in the application
		firstName = application.getFirstName();
		lastName = application.getLastName();
		otherName = application.getOtherName();
		phoneNumber = application.getPhoneNumber();
		places = application.getSelectedPlaces();
		placesIds = application.getSelectedPlacesIds();
		photoFile = application.getPhotoFile();

		if (photoFile != null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(),
					options);
		} else {
			PHOTO_URL = "URL";
		}
		// set the data on the views
		if (firstName != null && lastName != null) {
			nameEditText.setText(firstName + " " + lastName);
			if (otherName != null) {
				nameEditText.append(" " + otherName);
			} else {
				otherName = null;
			}
		}

		if (phoneNumber != null) {
			/**
			 * used when the country code was being appended at the front
			 */
			// if (phoneNumber.startsWith("0") && phoneNumber.length() > 1) {
			// phoneNumber = phoneNumber.substring(1);
			// }
			phoneEditText.setText(phoneNumber);
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
		} else {
			circularImageView.setImageResource(R.drawable.bck_landing);
		}

		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				registerUserPhoto();
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

	/**
	 * send the data to the server
	 */

	private void registerUserPhoto() {
		if (photoFile != null) {
			new UploadFile().execute();
		} else {
			registerUser();
		}
	}

	/**
	 * send the data to the server
	 */

	private void registerUser() {
		Log.e(TAG, PHOTO_URL);

		RegisterTask registerTask = new RegisterTask(RegisterTask.POST_TASK);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("first_name", firstName);
			jsonObject.put("last_name", lastName);
			jsonObject.put("other_name", otherName);
			jsonObject.put("phone_no", phoneNumber);
			jsonObject.put("photo", PHOTO_URL);
			JSONArray placesArray = new JSONArray();
			for (String id : placesIds) {
				placesArray.put(id);
			}
			jsonObject.put("places", placesArray);
			jsonData = jsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		registerTask.execute(new String[] { URL + "targets/" });
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

	private void showProgress() {
		if (progressBar != null && optionsLayout != null) {
			progressBar.setVisibility(View.VISIBLE);
			optionsLayout.setVisibility(View.GONE);
		}
	}

	private void hideprogress() {
		if (progressBar != null && optionsLayout != null) {
			progressBar.setVisibility(View.GONE);
			optionsLayout.setVisibility(View.VISIBLE);
		}
	}

	public void showPhotoRetry() {
		hideprogress();
		if (dialog == null || !dialog.isShowing()) {
			dialog = new MaterialDialog.Builder(this)
					.title(R.string.connect_error)
					.content(R.string.connection_error_message)
					.positiveText(R.string.yes).negativeText(R.string.cancel)
					.callback(new MaterialDialog.ButtonCallback() {
						@Override
						public void onPositive(MaterialDialog dialog) {
							registerUserPhoto();
						}

						@Override
						public void onNegative(MaterialDialog dialog) {
						}
					}).build();
			dialog.show();
		}
	}

	public void showRetry() {
		hideprogress();
		if (dialog == null || !dialog.isShowing()) {
			dialog = new MaterialDialog.Builder(this)
					.title(R.string.connect_error)
					.content(R.string.connection_error_message)
					.positiveText(R.string.yes).negativeText(R.string.cancel)
					.callback(new MaterialDialog.ButtonCallback() {
						@Override
						public void onPositive(MaterialDialog dialog) {
							registerUser();
						}

						@Override
						public void onNegative(MaterialDialog dialog) {
						}
					}).build();
			dialog.show();
		}
	}

	private class UploadFile extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgress();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean uploaded = false;
			try {
				// Initialize the Amazon Cognito credentials provider
				CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
						RegisterSummary.this, application.getIdentityPoolId(),
						application.getRegion());
				// Get a TransferManager for upload
				TransferManager transferManager = new TransferManager(
						credentialsProvider);
				// Build the name of the file to be unique
				// pic.png will be turned to pic_1427991238526_.png
				String fileName = photoFile.getName();
				PHOTO = FilenameUtils.removeExtension(fileName) + "_"
						+ System.currentTimeMillis() + "."
						+ FilenameUtils.getExtension(fileName);
				PHOTO_URL = application.getAWSURL() + PHOTO;

				Upload upload = transferManager.upload(application.getBucket(),
						PHOTO, photoFile);
				upload.addProgressListener(new ProgressListener() {

					@Override
					public void progressChanged(ProgressEvent arg0) {

					}
				});
				while (!upload.isDone()) {
				}
				if (upload.getState() == TransferState.Failed) {
					Log.e(TAG, "Failed");
					uploaded = false;
				} else if (upload.getState() == TransferState.Completed) {
					Log.e(TAG, "Complete");
					uploaded = true;
				}

			} catch (Exception e) {
				Log.e(TAG, "Exception " + e.getLocalizedMessage());
			}
			return uploaded;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				Log.e(TAG, "File uploaded");
				registerUser();
			} else {
				showPhotoRetry();
			}
		}
	}

	private class RegisterTask extends AsyncTask<String, Integer, String> {
		public static final int POST_TASK = 1;
		public static final int GET_TASK = 2;
		// connection timeout, in milliseconds-waiting to connect
		private static final int CONN_TIMEOUT = 60000;
		// socket timeout, in milliseconds-waiting for data
		private static final int SOCKET_TIMEOUT = 60000;
		private int TASK_TYPE;

		public RegisterTask(int TASK_TYPE) {
			this.TASK_TYPE = TASK_TYPE;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgress();
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
				hideprogress();
				try {
					JSONObject jsonObject = new JSONObject(response);
					String url = jsonObject.getString("url");
					if (url != null && !url.equals("")) {
						moveToSuccessScreen();
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
						application.getApiUsername(),
						application.getApiPassword());
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
		// if the user had opted to take a photo take him back to the
		// camera,else to to choose photo actrivity
		Intent intent = new Intent(RegisterSummary.this, RegisterPhoto.class);
		if (photoFile == null) {
			intent = new Intent(RegisterSummary.this, RegisterChoosePhoto.class);
		}
		startActivity(intent);
		finish();
	}

	public void moveToSuccessScreen() {
		Intent successIntent = new Intent(RegisterSummary.this,
				Registered.class);
		startActivity(successIntent);
		finish();
	}
}
