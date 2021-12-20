package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
		loadView2("/view/DepartmentList.fxml");
	}

	@FXML
	public void onItemAboutAction() {
		loadView("/view/About.fxml");
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	private void loadView(String absoluteName) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
		try {
			VBox vbox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(vbox.getChildren());
		} catch (IOException e) {
			Alerts.showAlert("Erro!", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void loadView2(String absoluteName) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
		try {
			VBox vbox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(vbox.getChildren());
			
			DepartmentListController controller = loader.getController();
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		} catch (IOException e) {
			Alerts.showAlert("Erro!", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
}
