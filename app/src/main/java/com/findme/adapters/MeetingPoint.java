package com.findme.adapters;

import java.util.ArrayList;

public class MeetingPoint {

	private String title;
	private String location;
	private ArrayList<String> meetingTimes;

	/**
	 * 
	 * @param title
	 * @param location
	 * @param meetingTimes
	 */
	public MeetingPoint(String title, String location,
			ArrayList<String> meetingTimes) {
		setTitle(title);
		setLocation(location);
		setMeetingTimes(meetingTimes);
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the meetingTimes
	 */
	public ArrayList<String> getMeetingTimes() {
		return meetingTimes;
	}

	/**
	 * @param meetingTimes
	 *            the meetingTimes to set
	 */
	public void setMeetingTimes(ArrayList<String> meetingTimes) {
		this.meetingTimes = meetingTimes;
	}
}
