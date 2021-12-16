package view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
		System.out.println("onItemDepartmentAction");
	}
	
	@FXML
	public void onItemAboutAction() {
		System.out.println("onItemAboutAction");
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
