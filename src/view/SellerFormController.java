package view;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;
import view.listeners.DataChangeListener;
import view.util.Alerts;
import view.util.Constraints;
import view.util.Utils;

public class SellerFormController implements Initializable {

	private SellerService service;
	private DepartmentService depService;
	private Seller sel;
	private List<DataChangeListener> listeners = new ArrayList<>();

	@FXML
	private TextField tfName;

	@FXML
	private TextField tfId;

	@FXML
	private TextField tfEmail;

	@FXML
	private DatePicker dpBirthDate;

	@FXML
	private TextField tfBaseSalary;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnCancel;

	@FXML
	private Label lblErrorName;

	@FXML
	private Label lblErrorBirthDate;

	@FXML
	private Label lblErrorEmail;

	@FXML
	private Label lblErrorBaseSalary;

	@FXML
	private ComboBox<Department> comboBoxDepartment;

	private ObservableList<Department> obsList;

	@FXML
	public void onBtnSaveAction(ActionEvent ev) {
		if (sel == null) {
			throw new IllegalStateException("Seller was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			sel = getFormData();
			service.saveOrUpdate(sel);
			Utils.currentStage(ev).close();
			notifyDataChangeListeners();
		} catch (DbException e) {
			Alerts.showAlert("Error!", null, e.getMessage(), AlertType.ERROR);
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener l : listeners) {
			l.onDataChanged();
		}
	}

	private Seller getFormData() {
		Seller department = new Seller();
		ValidationException exception = new ValidationException("Validation Error");

		department.setId(Utils.tryParseToInt(tfId.getText()));

		if (tfName.getText() == null || tfName.getText().trim().equals("")) {
			exception.addErrors("Name", "Field can't be empty");
		}

		department.setName(tfName.getText());

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return department;
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("Name")) {
			lblErrorName.setText(errors.get("Name"));
		}
	}

	@FXML
	public void onBtnCancelAction(ActionEvent ev) {
		Utils.currentStage(ev).close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();

	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(tfId);
		Constraints.setTextFieldMaxLength(tfName, 70);
		Constraints.setTextFieldDouble(tfBaseSalary);
		Constraints.setTextFieldMaxLength(tfEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		initializeComboBoxDepartment();
	}

	public void setServices(SellerService service, DepartmentService depService) {
		this.service = service;
		this.depService = depService;
	}

	public void setSeller(Seller sel) {
		this.sel = sel;
	}

	public void updateFormData() {
		if (sel == null) {
			throw new IllegalStateException("Seller was null!");
		}
		
		tfId.setText(String.valueOf(sel.getId()));
		tfName.setText(sel.getName());
		tfEmail.setText(sel.getEmail());
		Locale.setDefault(Locale.US);
		tfBaseSalary.setText(String.format("%.2f", sel.getBaseSalary()));
		
		if (sel.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(sel.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		
		if (sel.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		} else {
			comboBoxDepartment.setValue(sel.getDepartment());
		}
	}

	public void addListeners(DataChangeListener listener) {
		listeners.add(listener);
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}

	public void loadAssociatedObjects() {
		if (depService == null) {
			throw new IllegalStateException("DepartmentService was null");
		}
		List<Department> list = depService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}
}
