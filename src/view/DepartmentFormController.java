package view;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;
import view.util.Alerts;
import view.util.Constraints;
import view.util.Utils;

public class DepartmentFormController implements Initializable {
	
	private DepartmentService service;
	private Department dep;
	
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
       	} catch(DbException e) {
       		Alerts.showAlert("Error!", null, e.getMessage(), AlertType.ERROR);
       	}
    }
    
    private Department getFormData() {
    	Department department = new Department();
    	
    	department.setId(Utils.tryParseToInt(tfId.getText()));
    	department.setName(tfName.getText());
    	
    	return department;
	}

	@FXML
    public void onBtnCancelAction() {
    	System.out.println("onBtnCancelAction");
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
}
