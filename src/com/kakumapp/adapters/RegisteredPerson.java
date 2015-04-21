package com.kakumapp.adapters;

import java.util.ArrayList;

/**
 * RegisteredPerson
 * 
 * @author paul
 * 
 */
public class RegisteredPerson {

	private String url;
	private String name;
	private ArrayList<Place> places;
	private String phone;
	private String photoURL;

	/**
	 * 
	 * @param url
	 * @param name
	 * @param location
	 * @param phone
	 * @param photo
	 */
	public RegisteredPerson(String url, String name, ArrayList<Place> places,
			String phone, String photoURL) {
		setUrl(url);
		setName(name);
		setPlaces(places);
		setPhone(phone);
		setPhotoURL(photoURL);
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the places
	 */
	public ArrayList<Place> getPlaces() {
		return places;
	}

	/**
	 * @param places
	 *            the places to set
	 */
	public void setPlaces(ArrayList<Place> places) {
		this.places = places;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the photoURL
	 */
	public String getPhotoURL() {
		return photoURL;
	}

	/**
	 * @param photoURL the photoURL to set
	 */
	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}

}
