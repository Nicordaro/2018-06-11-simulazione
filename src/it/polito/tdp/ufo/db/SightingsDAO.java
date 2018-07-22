package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.polito.tdp.ufo.model.City;
import it.polito.tdp.ufo.model.Edge;
import it.polito.tdp.ufo.model.EdgeState;
import it.polito.tdp.ufo.model.Sighting;
import it.polito.tdp.ufo.model.State;

public class SightingsDAO {
	// PRENDO TUTTI GLI AVVISTAMENTI
	public List<Sighting> getSightings() {
		String sql = "SELECT * FROM sighting";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Sighting> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// RESTITUISCO IDMAP STATI
	public Map<String, State> getStateMap() {
		String sql = "SELECT * FROM state as s";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet res = st.executeQuery();
			Map<String, State> stateMap = new HashMap<>();

			while (res.next()) {
				stateMap.put(res.getString("id"),
						new State(res.getString("id"), res.getString("Name"), res.getString("Capital"),
								res.getDouble("Lat"), res.getDouble("Lng"), res.getDouble("Area"),
								res.getInt("Population"), res.getString("Neighbors")));
			}

			conn.close();
			return stateMap;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

//	 RESTITUISCE UN OGGETTO DI TIPO EDGE (idProv, idDest)
	public List<Edge> getEdges(int year) {
		String sql = "SELECT Distinct s1.state as source, s2.state as dest FROM sighting as s1, sighting as s2 WHERE s1.country='us' AND s2.country='us' AND s2.state!=s1.state AND s1.state!='' AND s2.state!=''  AND YEAR(s1.datetime)=? and s1.datetime<s2.datetime AND YEAR(s2.datetime)=YEAR(s1.datetime) ORDER BY source";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			List<Edge> list = new ArrayList<Edge>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Edge(res.getString("source"), res.getString("dest")));
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

//	RESTITUISCE MAPPA CON CHIAVE <anno - #avvistamentiInQuellAnno> a cui è associato come valore un intero che rappresenta l'anno
	public Map<String, Integer> getNumberOfSightsByYear() {
		String sql = "SELECT YEAR(s.datetime) as year, COUNT(s.datetime) as sights FROM sighting as s WHERE s.country='us' GROUP BY YEAR(s.datetime) ORDER BY year";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			Map<String, Integer> map = new TreeMap<String, Integer>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				map.put(res.getInt("year") + " - " + res.getInt("sights"), res.getInt("year"));
			}

			conn.close();
			return map;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

//	RESTITUISCE UNA LISTA DI CODICI DI STATI PRESENTI NEL DATABASE IN CUI COMPAIANO AVVISTAMENTI IN QUELL'ANNO 
	public List<String> getStates(int year) {
		String sql = "SELECT DISTINCT s.state as state FROM sighting as s WHERE s.country='us' AND YEAR(s.datetime)=?";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);

			List<String> list = new ArrayList<String>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(res.getString("state"));
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// RESTITUISCE GLI ARCHI STATO1-STATO2 TALI CHE LO STATO 1 SIA QUELLO
	// SELEZIONATO
	public List<EdgeState> getEdgeByStateId(String stateId) {

		String sql = "SELECT s1.*, s2.* FROM state as s1, state as s2, neighbor as n WHERE s1.id=n.state1 AND s2.id=n.state2 AND s1.id=?";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, stateId);

			List<EdgeState> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

//			NOTA: LA LISTA E' DI OGGETTI DI TIPO STATO!!!
			while (res.next()) {
				State state1 = new State(res.getString("id"), res.getString("Name"), res.getString("Capital"),
						res.getDouble("Lat"), res.getDouble("Lng"), res.getDouble("Area"), res.getInt("Population"),
						res.getString("Neighbors"));
				State state2 = new State(res.getString("id"), res.getString("Name"), res.getString("Capital"),
						res.getDouble("Lat"), res.getDouble("Lng"), res.getDouble("Area"), res.getInt("Population"),
						res.getString("Neighbors"));
				list.add(new EdgeState(state1, state2));

			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// RESTITUISCE TUTTI GLI ARCHI STATO1-STATO2
	public List<EdgeState> getAllStateEdge() {

		String sql = "SELECT s1.*, s2.* FROM state as s1, state as s2, neighbor as n WHERE s1.id=n.state1 AND s2.id=n.state2 ORDER BY s1.id";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<EdgeState> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

//				NOTA: LA LISTA E' DI OGGETTI DI TIPO STATO!!!
			while (res.next()) {
				State state1 = new State(res.getString("id"), res.getString("Name"), res.getString("Capital"),
						res.getDouble("Lat"), res.getDouble("Lng"), res.getDouble("Area"), res.getInt("Population"),
						res.getString("Neighbors"));
				State state2 = new State(res.getString("id"), res.getString("Name"), res.getString("Capital"),
						res.getDouble("Lat"), res.getDouble("Lng"), res.getDouble("Area"), res.getInt("Population"),
						res.getString("Neighbors"));
				list.add(new EdgeState(state1, state2));

			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

//	RESTITUISCE GLI STATI IN CUI LA SOMMA DELLE DURATE DEGLI AVVISTAMENTI SIA SUPERIORE AI SECONDI IN INPUT

	public List<State> getStatesWithSeconds(int seconds) {
		String sql = "SELECT s2.*, SUM(s.duration) as sum FROM sighting as s, state as s2 WHERE s.state = s2.id GROUP BY state";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<State> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

//			NOTA: LA LISTA E' DI OGGETTI DI TIPO STATO!!!
			while (res.next()) {
				if (res.getInt("sum") > seconds) {
					list.add(new State(res.getString("id"), res.getString("Name"), res.getString("Capital"),
							res.getDouble("Lat"), res.getDouble("Lng"), res.getDouble("Area"), res.getInt("Population"),
							res.getString("Neighbors")));
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

//	RESTITUISCE LE CITTA' IN CUI LA SOMMA DELLE DURATE DEGLI AVVISTAMENTI SIA SUPERIORE AI SECONDI IN INPUT
	public List<City> getCityWithSeconds(int seconds) {
		String sql = "SELECT s.city as city, s.state as state, s.country as country, SUM(s.duration) as sum FROM sighting as s WHERE country = us GROUP BY city";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<City> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

//			NOTA: LA LISTA E' DI OGGETTI DI TIPO CITTA'!!!
			while (res.next()) {
				if (res.getInt("sum") > seconds) {
					list.add(new City(res.getString("city"), res.getString("state"), res.getString("country"),
							res.getInt("sum")));
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
