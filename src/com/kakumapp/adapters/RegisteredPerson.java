package com.kakumapp.adapters;

import java.util.ArrayList;

import android.graphics.Bitmap;

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
	private Bitmap photo;

	/**
	 * 
	 * @param url
	 * @param name
	 * @param location
	 * @param phone
	 * @param photo
	 */
	public RegisteredPerson(String url, String name, ArrayList<Place> places,
			String phone, Bitmap photo) {
		setUrl(url);
		setName(name);
		setPlaces(places);
		setPhone(phone);
		setPhoto(photo);
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
	 * @return the photo
	 */
	public Bitmap getPhoto() {
		return photo;
	}

	/**
	 * @param photo
	 *            the photo to set
	 */
	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

}
