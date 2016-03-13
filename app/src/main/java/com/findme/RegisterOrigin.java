package com.findme;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cocosw.bottomsheet.BottomSheet;
import com.findme.adapters.Country;
import com.findme.adapters.Place;
import com.findme.utils.Utils;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Register a place of origin
 *
 * @author paul
 */
public class RegisterOrigin extends AppCompatActivity {

    public static final String TAG = "RegisterOrigin";
    private static final String SELECT_COUNTRY = "Select country";
    private static String URL;
    public static int FETCH_TYPE, indexOfPlace;
    private Button continueButton;
    private TextView findPersonTextView, descTextView;
    private Typeface typeface;
    private Spinner countriesSpinner;
    private MultiAutoCompleteTextView placesAutoCompleteTextView;
    private String selectedCountryName;
    public static Country country;
    private Country defaultCountry;
    private ArrayList<String> selectedPlaces;
    private ArrayAdapter<Country> dataAdapterCountries;
    private ArrayAdapter<Place> dataAdapterPlaces;
    private ArrayList<Country> countries = new ArrayList<>();
    private ArrayList<Place> places = new ArrayList<>();
    private ArrayList<String> selectedPlacesIds = new ArrayList<>();
    private ArrayList<String> placeNames = new ArrayList<>();
    private ArrayList<String> placesToRegister = new ArrayList<>();
    private BottomSheet bottomSheet;
    private ProgressBarCircularIndeterminate progressBar;
    private LinearLayout optionsLayout;
    private MaterialDialog dialog;
    private FindMeApplication application;
    private FetchTask fetchTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_origin);
        // global app
        application = (FindMeApplication) getApplication();
        URL = FindMeApplication.APIURL;
        // get the views
        continueButton = (Button) findViewById(R.id.button_register_continue);
        findPersonTextView = (TextView) findViewById(R.id.textView_register_find_person);
        descTextView = (TextView) findViewById(R.id.textView_origin_desc);
        countriesSpinner = (Spinner) findViewById(R.id.spinner_origin_countries);
        placesAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.autocomplete_origin_places);
        progressBar = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBar);
        optionsLayout = (LinearLayout) findViewById(R.id.options_layout);

        // fonts
        typeface = new Utils(this).getFont("Ubuntu-L");
        findPersonTextView.setTypeface(typeface);
        descTextView.setTypeface(typeface);

        // hints color
        int hintTextColor = getResources().getColor(R.color.half_white);
        placesAutoCompleteTextView.setHintTextColor(hintTextColor);

        continueButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                goNext();
            }
        });

        findPersonTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // meeting activity
                Intent findPersonIntent = new Intent(RegisterOrigin.this,
                        SearchResults.class);
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

        // create the spinners adapters
        String[] countriesLocal = {"South Sudan", "Sudan", "Somalia",
                "Ethiopia", "D.R. Congo", "Burundi", "Rwanda", "Eritrea",
                "Uganda"};
        defaultCountry = new Country(0, SELECT_COUNTRY, "");
        countries.add(defaultCountry);
        for (int i = 0; i < countriesLocal.length; i++) {
            countries.add(new Country(i + 1, countriesLocal[i],
                    "http://kakumapp-api.herokuapp.com/countries/" + (i + 1)));
        }
        dataAdapterCountries = new ArrayAdapter<Country>(this,
                android.R.layout.simple_spinner_item, countries);
        dataAdapterCountries
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countriesSpinner.setAdapter(dataAdapterCountries);
        // when a country is selected,get the places under that country
        countriesSpinner
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View arg1, int position, long arg3) {
                        TextView textView = ((TextView) parent.getChildAt(0));
                        if (textView != null) {
                            textView.setTextColor(Color.WHITE);
                        }
                        country = (Country) parent.getItemAtPosition(position);
                        if (!country.getName().equals(SELECT_COUNTRY)) {
                            // get the places
                            FETCH_TYPE = 2;
                            fetchTask = new FetchTask(FetchTask.GET_TASK);
                            fetchTask.execute(new String[]{URL + "countries/"
                                    + country.getId()});
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
        // places spinner adatapter
        dataAdapterPlaces = new ArrayAdapter<Place>(this,
                android.R.layout.simple_list_item_1, places);

        dataAdapterPlaces.setDropDownViewResource(R.layout.spinner_dropdown);
        placesAutoCompleteTextView.setAdapter(dataAdapterPlaces);
        // specify the minimum type of characters before drop-down list is shown
        placesAutoCompleteTextView.setThreshold(1);
        // comma to separate the different places
        placesAutoCompleteTextView
                .setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        // set global data if available
        selectedCountryName = application.getSelectedCountryName();
        selectedPlaces = application.getSelectedPlaces();
        selectedPlacesIds = application.getSelectedPlacesIds();

        if (selectedPlaces != null && !selectedPlaces.isEmpty()) {
            String plcs = "";
            for (int i = 0; i < selectedPlaces.size(); i++) {
                plcs += selectedPlaces.get(i) + ",";
            }
            placesAutoCompleteTextView.setText(plcs);
        } else {
            selectedPlaces = new ArrayList<>();
        }

        if (selectedPlacesIds == null) {
            selectedPlacesIds = new ArrayList<>();
        }
        /**
         * In an effort to speed up the process of fetching countries,the
         * countries will be localized (hardcoded :) )
         */
        // // get the countries
        // FETCH_TYPE = 1;
        // FetchTask fetchTask = new FetchTask(FetchTask.GET_TASK);
        // fetchTask.execute(new String[] { URL + "countries/" });
    }

    // show progress bar and hide button
    private void showProgress() {
        if (progressBar != null && optionsLayout != null) {
            progressBar.setVisibility(View.VISIBLE);
            optionsLayout.setVisibility(View.GONE);
        }
    }

    // hide progress bar and show button
    private void hideprogress() {
        if (progressBar != null && optionsLayout != null) {
            progressBar.setVisibility(View.GONE);
            optionsLayout.setVisibility(View.VISIBLE);
        }
    }

    protected void goNext() {
        // if there are any places that the user has entered,register them first
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
        // check the places not registered(user generated)
        for (String selectedPlace : selectedPlaces) {
            if (!placeNames.contains(selectedPlace)) {
                placesToRegister.add(selectedPlace);
            }
        }
        // register places(user generated)
        if (placesToRegister.size() > 0) {
            for (indexOfPlace = 0; indexOfPlace < placesToRegister.size(); indexOfPlace++) {
                // register a place
                FETCH_TYPE = 3;
                fetchTask = new FetchTask(FetchTask.POST_TASK);
                fetchTask.addNameValuePair("country", country.getId() + "");
                fetchTask.addNameValuePair("name", placesToRegister.get(indexOfPlace).trim());
                fetchTask.execute(new String[]{URL + "places/"});
            }
        } else {
            moveToNextScreen();
        }
    }

    public void moveToNextScreen() {
        // if data is valid
        if (isValidData()) {
            // get the ids of the places selected
            for (String selectedPlace : selectedPlaces) {
                for (Place place : places) {
                    if (place.getName().equals(selectedPlace)) {
                        selectedPlacesIds.add(place.getId() + "");
                    }
                }
            }
            application.setCountry(country);
            application.setSelectedCountryName(selectedCountryName);
            application.setSelectedPlaces(selectedPlaces);
            application.setSelectedPlacesIds(selectedPlacesIds);

            Intent nameIntent = new Intent(RegisterOrigin.this,
                    RegisterChoosePhoto.class);
            startActivity(nameIntent);
            finish();
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
                if (fetchTask != null
                        && fetchTask.getStatus() == AsyncTask.Status.RUNNING) {
                    fetchTask.cancel(true);
                }
            }
        } else {
            showErrorMessage("Country required",
                    "You need to select one of the countries");
            if (fetchTask != null
                    && fetchTask.getStatus() == AsyncTask.Status.RUNNING) {
                fetchTask.cancel(true);
            }
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
                            switch (FETCH_TYPE) {
                                // case 1:
                                // fetchTask = new FetchTask(FetchTask.GET_TASK);
                                // fetchTask.execute(new String[] { URL
                                // + "countries/" });
                                // break;
                                case 2:
                                    fetchTask = new FetchTask(FetchTask.GET_TASK);
                                    fetchTask.execute(new String[]{URL + "countries/" + country.getId()});
                                    break;
                                case 3:
                                    if (placesToRegister.size() > 0) {
                                        for (indexOfPlace = 0; indexOfPlace < placesToRegister
                                                .size(); indexOfPlace++) {
                                            fetchTask = new FetchTask(FetchTask.POST_TASK);
                                            fetchTask.addNameValuePair("country", country.getId() + "");
                                            fetchTask.addNameValuePair("name",
                                                    placesToRegister.get(indexOfPlace).trim());
                                            fetchTask.execute(new String[]{URL + "places/"});
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            if (FETCH_TYPE == 1) {
                                // go back one screen since there is nothing one
                                // can do without net
                                Intent originIntent = new Intent(RegisterOrigin.this, RegisterPhone.class);
                                startActivity(originIntent);
                            }
                        }
                    }).build();
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    /**
     * This class does pulling/pushing some data
     *
     * @author paul
     */
    private class FetchTask extends AsyncTask<String, Integer, String> {
        public static final int POST_TASK = 1;
        public static final int GET_TASK = 2;
        private Map<String, String> params = new HashMap<>();
        private int taskType;

        public FetchTask(int taskType) {
            this.taskType = taskType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        public void addNameValuePair(String name, String value) {
            params.put(name, value);
        }

        /**
         * fetch data
         */
        protected String doInBackground(String... urls) {
            String url = urls[0];
            if (taskType == POST_TASK) {
                return Utils.postData(url, params);
            } else {
                return Utils.getData(url, params);
            }
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
                        // dataAdapterCountries.notifyDataSetChanged();
                        // countriesSpinner.setSelection(0);
                        dataAdapterCountries = new ArrayAdapter<Country>(
                                RegisterOrigin.this,
                                android.R.layout.simple_spinner_item, countries);
                        dataAdapterCountries
                                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                        // notifyDataSetChanged not working sometimes
                        // dataAdapterPlaces.notifyDataSetChanged();
                        dataAdapterPlaces = new ArrayAdapter<Place>(
                                RegisterOrigin.this,
                                android.R.layout.simple_list_item_1, places);
                        dataAdapterPlaces
                                .setDropDownViewResource(R.layout.spinner_dropdown);
                        placesAutoCompleteTextView
                                .setAdapter(dataAdapterPlaces);
                    }
                } catch (JSONException e) {
                    showRetry();
                }
            }
            // registering places
            else if (FETCH_TYPE == 3) {
                try {
                    JSONObject placeJsonObject = new JSONObject(response);
                    places.add(new Place(placeJsonObject.getLong("id"),
                            placeJsonObject.getLong("country"), placeJsonObject
                            .getString("name"), placeJsonObject
                            .getString("url")));
                    placeNames.add(placeJsonObject.getString("name"));
                    // dataAdapterPlaces.notifyDataSetChanged();
                    dataAdapterPlaces = new ArrayAdapter<Place>(
                            RegisterOrigin.this,
                            android.R.layout.simple_list_item_1, places);
                    dataAdapterPlaces
                            .setDropDownViewResource(R.layout.spinner_dropdown);
                    placesAutoCompleteTextView.setAdapter(dataAdapterPlaces);

                    if (indexOfPlace >= placesToRegister.size()) {
                        moveToNextScreen();
                    }
                } catch (JSONException e) {
                    try {
                        JSONObject placeJsonObject = new JSONObject(response);
                        if (placeJsonObject.getString("name").contains(
                                "This field must be unique")) {
                            if (indexOfPlace >= placesToRegister.size()) {
                                moveToNextScreen();
                            }
                        }
                    } catch (JSONException e1) {
                        showRetry();
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent nameIntent = new Intent(RegisterOrigin.this, RegisterPhone.class);
        startActivity(nameIntent);
        finish();
    }
}
