package com.kakumapp;

import java.io.File;
import java.util.ArrayList;

import android.app.Application;

import com.kakumapp.adapters.Country;

public class KakumaApplication extends Application {

	// private static final String TAG = "KakumaApplication";
	public static final String APIURL = "http://kakumapp-api.herokuapp.com/";
	private String firstName, lastName, otherName, phoneNumber,
			selectedCountryName;
	private ArrayList<String> selectedPlaces, selectedPlacesIds;
	private Country country;
	private File photoFile;
	private String apiUsername;
	private String apiPassword;
	private String bucket;
	private String identityPoolId;
	private String AWSURL;

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

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
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
	 * @param lastName
	 *            the lastName to set
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
	 * @param otherName
	 *            the otherName to set
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
	 * @param phoneNumber
	 *            the phoneNumber to set
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
	 * @param country
	 *            the country to set
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
	 * @param selectedCountryName
	 *            the selectedCountryName to set
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
	 * @param selectedPlaces
	 *            the selectedPlaces to set
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
	 * @param selectedPlacesIds
	 *            the selectedPlacesIds to set
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
	 * @param photoFile
	 *            the photoFile to set
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
	 * @param apiUsername
	 *            the apiUsername to set
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
	 * @param apiPassword
	 *            the apiPassword to set
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
	 * @param bucket
	 *            the bucket to set
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
	 * @param identityPoolId
	 *            the identityPoolId to set
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
	 * @param aWSURL
	 *            the aWSURL to set
	 */
	public void setAWSURL(String aWSURL) {
		AWSURL = aWSURL;
	}

}
