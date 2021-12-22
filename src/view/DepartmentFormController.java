package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import view.listeners.DataChangeListener;
import view.util.Alerts;
import view.util.Constraints;
import view.util.Utils;

public class DepartmentFormController implements Initializable {
	
	private DepartmentService service;
	private Department dep;
	private List<DataChangeListener> listeners = new ArrayList<>();
	
    @FXML
	private TextField tfName;
    
    @FXML
	private TextField tfId;
    
    @FXML
	private Button btnSave;
    
    @FXML
	private Button btnCancel;
    
    @FXML
	private Label lblError;
	
    @FXML
    public void onBtnSaveAction(ActionEvent ev) {
    	if(dep == null) {
    		throw new IllegalStateException("Department was null");
    	}
       	if(service == null) {
    		throw new IllegalStateException("Service was null");
    	}
       	try {
       		dep = getFormData();
       		service.saveOrUpdate(dep);
       		Utils.currentStage(ev).close();
       		notifyDataChangeListeners();
       	} catch(DbException e) {
       		Alerts.showAlert("Error!", null, e.getMessage(), AlertType.ERROR);
       	} catch(ValidationException e) {
       		setErrorMessages(e.getErrors());
       	}
    }
    
    private void notifyDataChangeListeners() {
    	for(DataChangeListener l: listeners) {
    		l.onDataChanged();
    	}
	}

	private Department getFormData() {
    	Department department = new Department();
    	ValidationException exception = new ValidationException("Validation Error");
    	
    	department.setId(Utils.tryParseToInt(tfId.getText()));
    	
    	if(tfName.getText() == null || tfName.getText().trim().equals("")) {
    		exception.addErrors("Name", "Field can't be empty");
    	}
    	
    	department.setName(tfName.getText());
    	
    	if(exception.getErrors().size() > 0) {
    		throw exception;
    	}
    	
    	return department;
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("Name")) {
			lblError.setText(errors.get("Name"));
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
		Constraints.setTextFieldMaxLength(tfName, 30);
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void setDepartment(Department dep) {
		this.dep = dep;
	}
	
	public void updateFormData() {
		if(dep == null) {
			throw new IllegalStateException("Department was null!");
		}
		tfId.setText(String.valueOf(dep.getId()));
		tfName.setText(dep.getName());
	}
	
	public void addListeners(DataChangeListener listener) {
		listeners.add(listener);
	}
}
