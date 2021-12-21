package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import view.util.Alerts;

public class MainViewController implements Initializable{
	@FXML
	private MenuItem itemSeller;
	
	@FXML
	private MenuItem itemDepartment;
	
	@FXML
	private MenuItem itemAbout;
	
	@FXML
	public void onItemSellerAction() {
		System.out.println("onItemSellerAction");
	}
	
	@FXML
	public void onItemDepartmentAction() {
		loadView("/view/DepartmentList.fxml", (DepartmentListController controller) -> { 
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onItemAboutAction() {
		loadView("/view/About.fxml", x -> {});
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	private <T> void loadView(String absoluteName, Consumer<T> con) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
		try {
			VBox vbox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(vbox.getChildren());
			
			T controller = loader.getController();
			con.accept(controller);
		} catch (IOException e) {
			Alerts.showAlert("Erro!", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
}
