package it.polito.tdp.ufo.model;

public class EdgeState {

	private State state1;
	private State state2;

	public State getState1() {
		return state1;
	}

	public void setState1(State state1) {
		this.state1 = state1;
	}

	public State getState2() {
		return state2;
	}

	public void setState2(State state2) {
		this.state2 = state2;
	}

	public EdgeState(State state1, State state2) {
		super();
		this.state1 = state1;
		this.state2 = state2;
	}

}
