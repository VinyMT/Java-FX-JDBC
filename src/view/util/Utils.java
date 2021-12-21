package view.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	public static Stage currentStage(ActionEvent ev) {
		return (Stage) ((Node) ev.getSource()).getScene().getWindow();
	}
}
