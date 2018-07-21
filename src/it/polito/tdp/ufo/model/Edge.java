package it.polito.tdp.ufo.model;

public class Edge {

	private String source;
	private String dest;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public Edge(String source, String dest) {
		this.source = source;
		this.dest = dest;
	}

}
