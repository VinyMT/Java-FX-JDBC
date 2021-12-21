package view;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;
import view.util.Alerts;
import view.util.Utils;

public class DepartmentListController implements Initializable {
	
	private DepartmentService service;
	
	@FXML
	private Button btnNew;

	@FXML
	private TableView<Department> tblView;
	
	@FXML
	private TableColumn<Department, Integer> columnID;
	
	@FXML
	private TableColumn<Department, String> columnName;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtnNewAction(ActionEvent ev) {
		this.createDialogForm(Utils.currentStage(ev), "/view/DepartmentForm.fxml");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}

	private void initializeNodes() {
		columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
		columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tblView.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service estava nulo!");
		}
		List<Department> list = service.findAll();
		
		obsList = FXCollections.observableArrayList(list);
		
		tblView.setItems(obsList);
	}
	
	private void createDialogForm(Stage currentStage, String absoluteName) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
		try {
			Pane pane = loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Type department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(currentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch(IOException e) {
			Alerts.showAlert("Error!", null, e.getMessage(), AlertType.ERROR);
		}
	}
}
