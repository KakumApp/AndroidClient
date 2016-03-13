package com.findme.adapters;

public class Place {

	private long id;
	private long countryId;
	private String name;
	private String url;

	public Place() {
	}

	/**
	 * 
	 * @param id
	 * @param countryId
	 * @param name
	 * @param url
	 */
	public Place(long id, long countryId, String name, String url) {
		setId(id);
		setCountryId(countryId);
		setName(name);
		setUrl(url);
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the countryId
	 */
	public long getCountryId() {
		return countryId;
	}

	/**
	 * @param countryId
	 *            the countryId to set
	 */
	public void setCountryId(long countryId) {
		this.countryId = countryId;
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
}
