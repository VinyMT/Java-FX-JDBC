package view;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.DepartmentService;
import model.services.SellerService;
import view.listeners.DataChangeListener;
import view.util.Alerts;
import view.util.Utils;

public class SellerListController implements Initializable, DataChangeListener {

	private SellerService service;

	@FXML
	private Button btnNew;

	@FXML
	private TableView<Seller> tblView;

	@FXML
	private TableColumn<Seller, Integer> columnID;

	@FXML
	private TableColumn<Seller, String> columnName;
	
	@FXML
	private TableColumn<Seller, String> columnEmail;

	@FXML
	private TableColumn<Seller, Date> columnBirthDate;
	
	@FXML
	private TableColumn<Seller, Double> columnBaseSalary;

	@FXML
	private TableColumn<Seller, Seller> columnEdit;

	@FXML
	private TableColumn<Seller, Seller> columnRemove;

	private ObservableList<Seller> obsList;

	@FXML
	public void onBtnNewAction(ActionEvent ev) {
		Seller sel = new Seller();
		this.createDialogForm(Utils.currentStage(ev), "/view/SellerForm.fxml", sel);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
		columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		columnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(columnBirthDate, "dd/MM/yyyy");
		columnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(columnBaseSalary, 2);

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tblView.prefHeightProperty().bind(stage.heightProperty());
	}

	public void setSellerService(SellerService service) {
		this.service = service;
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service estava nulo!");
		}
		List<Seller> list = service.findAll();

		obsList = FXCollections.observableArrayList(list);

		tblView.setItems(obsList);

		initEditButtons();
		initRemoveButtons();
	}
	
	private void createDialogForm(Stage currentStage, String absoluteName, Seller sel) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
		try {
			Pane pane = loader.load();
			Stage dialogStage = new Stage();

			SellerFormController controller = loader.getController();

			controller.setServices(new SellerService(), new DepartmentService());
			controller.loadAssociatedObjects();
			controller.setSeller(sel);
			controller.updateFormData();
			controller.addListeners(this);

			dialogStage.setTitle("Type seller data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(currentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("Error!", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void initEditButtons() {
		columnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		columnEdit.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(Utils.currentStage(event), "/view/SellerForm.fxml", obj));
			}
		});
	}

	private void initRemoveButtons() {
		columnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		columnRemove.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Seller obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure you want to delete this department?");
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			
			try {
				service.remove(obj);
				updateTableView();
			} catch(DbIntegrityException e) {
				Alerts.showAlert("Error!", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
	
	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
