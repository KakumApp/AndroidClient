package com.findme.adapters;

public class Country {

	private long id;
	private String name;
	private String url;

	public Country() {
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param url
	 */
	public Country(long id, String name, String url) {
		setId(id);
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
