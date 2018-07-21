/**
 * Sample Skeleton for 'Ufo.fxml' Controller Class
 */

package it.polito.tdp.ufo;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.ufo.model.Model;
import it.polito.tdp.ufo.model.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class UfoController {

	private Model model;
	private Map<String, Integer> map;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="boxAnno"
	private ComboBox<String> boxAnno; // Value injected by FXMLLoader

	@FXML // fx:id="boxStato"
	private ComboBox<State> boxStato; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
	void handleAnalizza(ActionEvent event) {
		txtResult.clear();

		String choice = boxStato.getValue().getId().toLowerCase();
		if (choice == null) {
			txtResult.setText("Errore, devi effettuare una selezione.");
		} else {
			List<State> before = new ArrayList<>(model.getBefore(choice));
//			System.out.println(before);
			List<State> after = new ArrayList<>(model.getAfter(choice));
//			System.out.println(after);

			txtResult.setText(
					"Dallo stato " + model.getState(choice).getName() + " si può raggiungere con un un solo arco:\n");
			for (State s : after) {
				txtResult.appendText(s.toString() + "\n");

			}

			txtResult.appendText(
					"Allo stato " + model.getState(choice).getName() + " ci si può arrivare con un solo arco da:\n");
			for (State s : before) {
				txtResult.appendText(s.toString() + "\n");

			}
			txtResult.appendText("Da questo stato si può arrivare anche ad altri "
					+ model.statiRaggiungibili(choice).size() + " stati, con più passi:\n");
			for (State s : model.statiRaggiungibili(choice)) {
				txtResult.appendText(s.toString() + "\n");
			}

		}

	}

	@FXML
	void handleAvvistamenti(ActionEvent event) {
		txtResult.clear();
		String choice = boxAnno.getValue();
		if (choice == null) {
			txtResult.setText("Errore, devi effettuare una selezione.");
		} else {
			int year = map.get(choice);
			model.createGraph(year);
			for (String s : model.getGraph().vertexSet()) {
				boxStato.getItems().add(model.getState(s));
			}

		}

	}

	@FXML
	void handleSequenza(ActionEvent event) {

	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Ufo.fxml'.";
		assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Ufo.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Ufo.fxml'.";

	}

	public void setModel(Model model) {
		this.model = model;
		boxAnno.getItems().addAll(model.getMapSights().keySet());
		this.map = new HashMap<String, Integer>(model.getMapSights());

	}
}
