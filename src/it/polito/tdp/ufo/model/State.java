package it.polito.tdp.ufo.model;

public class State {

	private String id;
	private String name;
	private String capital;
	private double area;
	private double lat;
	private double lng;
	private int population;
	private String neighbors;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public String getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(String neighbors) {
		this.neighbors = neighbors;
	}

	public State(String id, String name, String capital, double lat, double lng, double area, int population,
			String neighbors) {
		super();
		this.id = id;
		this.name = name;
		this.capital = capital;
		this.lat = lat;
		this.lng = lng;
		this.area = area;
		this.population = population;
		this.neighbors = neighbors;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append(" - #");
		builder.append(id);
		return builder.toString();
	}

}
