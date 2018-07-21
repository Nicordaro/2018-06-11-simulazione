package it.polito.tdp.ufo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {
	private SightingsDAO dao;
	private Map<String, Integer> mapSights;
	private Graph<String, DefaultEdge> graph;
	private Map<String, State> stateIdMap;
	private List<String> stateOfGraphid;

	public Model() {
		this.dao = new SightingsDAO();
		this.mapSights = new TreeMap<String, Integer>(dao.getNumberOfSightsByYear());
		this.graph = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		this.stateIdMap = new HashMap<>(dao.getStateMap());
		this.stateOfGraphid = new ArrayList<>();

	}

	public Map<String, Integer> getMapSights() {
		return mapSights;
	}

	public void createGraph(int year) {
		this.clearGraph(graph);

		Graphs.addAllVertices(graph, dao.getStates(year));

		List<Edge> list = new ArrayList<>(dao.getEdges(year));
		for (Edge e : list) {
			Graphs.addEdgeWithVertices(graph, e.getSource(), e.getDest());
			System.out.format("%s to %s\n", e.getSource(), e.getDest());
		}

	}

	public Graph<String, DefaultEdge> getGraph() {
		return graph;
	}

	public List<State> getBefore(String choice) {
		List<State> before = new ArrayList<State>();
		for (String s : Graphs.predecessorListOf(graph, choice)) {
			before.add(stateIdMap.get(s.toUpperCase()));
			System.out.println(stateIdMap.get(s.toUpperCase()));
		}
		return before;
	}

	public List<State> getAfter(String choice) {
		List<State> after = new ArrayList<State>();
		for (String s : Graphs.successorListOf(graph, choice)) {
			after.add(stateIdMap.get(s.toUpperCase()));
			System.out.println(stateIdMap.get(s.toUpperCase()));
		}
		return after;
	}

	public List<State> statiRaggiungibili(String choice) {
		List<State> arrivabili = new ArrayList<State>();
		GraphIterator<String, DefaultEdge> iterator = new BreadthFirstIterator<String, DefaultEdge>(graph, choice);
		while (iterator.hasNext()) {
			arrivabili.add(stateIdMap.get(iterator.next().toUpperCase()));
		}
		arrivabili.remove(stateIdMap.get(choice.toUpperCase()));
		return arrivabili;
	}

	public State getState(String choice) {
		if (stateIdMap.containsKey(choice.toUpperCase())) {
			return stateIdMap.get(choice.toUpperCase());
		}
		return null;
	}

	public List<String> getListaStatiWithRequisitiDiCitta(int secondi) {
		List<City> cities = new ArrayList<>(dao.getCityWithSeconds(secondi));

		for (City c : cities) {
			if (!stateOfGraphid.contains(c.getState().toUpperCase())) {
				stateOfGraphid.add(c.getState());
			}
		}
		return this.stateOfGraphid;
	}

	// SERVONO PER PULIRE IL GRAFO ALTRIMENTI IL CONNECTIVITY INSPECTOR TIENE
	// MEMORIZZATA L'ULTIMA SIZE DEL GRAFO

	public static <V, E> void removeAllEdges(Graph<V, E> graph) {
		LinkedList<E> copy = new LinkedList<E>();
		for (E e : graph.edgeSet()) {
			copy.add(e);
		}
		graph.removeAllEdges(copy);
	}

	public <V, E> void clearGraph(Graph<V, E> graph) {
		removeAllEdges(graph);
		removeAllVertices(graph);
	}

	public static <V, E> void removeAllVertices(Graph<V, E> graph) {
		LinkedList<V> copy = new LinkedList<V>();
		for (V v : graph.vertexSet()) {
			copy.add(v);
		}
		graph.removeAllVertices(copy);
	}

}
