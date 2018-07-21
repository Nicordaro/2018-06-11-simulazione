package it.polito.tdp.ufo.model;

public class City {
	private String city;
	private String state;
	private String country;
	private int duration;

	public City(String city, String state, String country, int duration) {
		super();
		this.city = city;
		this.state = state;
		this.country = country;
		this.duration = duration;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
