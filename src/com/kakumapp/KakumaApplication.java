package com.kakumapp;

import java.io.File;
import java.util.ArrayList;

import com.kakumapp.adapters.Country;

import android.app.Application;

public class KakumaApplication extends Application {

	private String firstName, lastName, otherName, phoneNumber,
			selectedCountryName;
	private ArrayList<String> selectedPlaces, selectedPlacesIds;
	private Country country;
	private File photoFile;

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

}
