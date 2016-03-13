package com.findme;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.Application;
import android.util.Log;

import com.amazonaws.regions.Regions;
import com.findme.adapters.Country;

public class FindMeApplication extends Application {

    private static final String TAG = "KakumaApplication";
    public static final String APIURL = "http://kaku.iminds.be/";
    // public static final String APIURL = "http://kakumapp-api.herokuapp.com/";
    private String firstName, lastName, otherName, phoneNumber, selectedCountryName;
    private ArrayList<String> selectedPlaces, selectedPlacesIds;
    private Country country;
    private File photoFile;
    private String apiUsername;
    private String apiPassword;
    private String bucket;
    private String identityPoolId;
    private String AWSURL;
    private Regions region;

    public void clearData() {
        firstName = null;
        lastName = null;
        otherName = null;
        phoneNumber = null;
        selectedCountryName = null;
        selectedPlaces = null;
        selectedPlacesIds = null;
        country = null;
        photoFile = null;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        readCredentials();
    }

    private void readCredentials() {
        // instance of the global application
        Scanner reader = null;
        try {
            reader = new Scanner(getAssets().open("credentials.txt"));
            String identityPoolId = reader.nextLine();
            String bucket = reader.nextLine();
            String region = reader.nextLine();
            String apiUsername = reader.nextLine();
            String apiPassword = reader.nextLine();

            setRegion(Regions.fromName(region));
            setIdentityPoolId(identityPoolId);
            setBucket(bucket);
            setApiUsername(apiUsername);
            setApiPassword(apiPassword);
            setAWSURL("https://s3-" + region + ".amazonaws.com/" + bucket + "/");
        } catch (IOException e) {
            Log.e(TAG, "Exception " + e.getLocalizedMessage());
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the otherName
     */
    public String getOtherName() {
        return otherName;
    }

    /**
     * @param otherName the otherName to set
     */
    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the country
     */
    public Country getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * @return the selectedCountryName
     */
    public String getSelectedCountryName() {
        return selectedCountryName;
    }

    /**
     * @param selectedCountryName the selectedCountryName to set
     */
    public void setSelectedCountryName(String selectedCountryName) {
        this.selectedCountryName = selectedCountryName;
    }

    /**
     * @return the selectedPlaces
     */
    public ArrayList<String> getSelectedPlaces() {
        return selectedPlaces;
    }

    /**
     * @param selectedPlaces the selectedPlaces to set
     */
    public void setSelectedPlaces(ArrayList<String> selectedPlaces) {
        this.selectedPlaces = selectedPlaces;
    }

    /**
     * @return the selectedPlacesIds
     */
    public ArrayList<String> getSelectedPlacesIds() {
        return selectedPlacesIds;
    }

    /**
     * @param selectedPlacesIds the selectedPlacesIds to set
     */
    public void setSelectedPlacesIds(ArrayList<String> selectedPlacesIds) {
        this.selectedPlacesIds = selectedPlacesIds;
    }

    /**
     * @return the photoFile
     */
    public File getPhotoFile() {
        return photoFile;
    }

    /**
     * @param photoFile the photoFile to set
     */
    public void setPhotoFile(File photoFile) {
        this.photoFile = photoFile;
    }

    /**
     * @return the apiUsername
     */
    public String getApiUsername() {
        return apiUsername;
    }

    /**
     * @param apiUsername the apiUsername to set
     */
    public void setApiUsername(String apiUsername) {
        this.apiUsername = apiUsername;
    }

    /**
     * @return the apiPassword
     */
    public String getApiPassword() {
        return apiPassword;
    }

    /**
     * @param apiPassword the apiPassword to set
     */
    public void setApiPassword(String apiPassword) {
        this.apiPassword = apiPassword;
    }

    /**
     * @return the bucket
     */
    public String getBucket() {
        return bucket;
    }

    /**
     * @param bucket the bucket to set
     */
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    /**
     * @return the identityPoolId
     */
    public String getIdentityPoolId() {
        return identityPoolId;
    }

    /**
     * @param identityPoolId the identityPoolId to set
     */
    public void setIdentityPoolId(String identityPoolId) {
        this.identityPoolId = identityPoolId;
    }

    /**
     * @return the aWSURL
     */
    public String getAWSURL() {
        return AWSURL;
    }

    /**
     * @return the region
     */
    public Regions getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(Regions region) {
        this.region = region;
    }

    /**
     * @param aWSURL the aWSURL to set
     */
    public void setAWSURL(String aWSURL) {
        AWSURL = aWSURL;
    }

}
