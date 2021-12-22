package view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import view.util.Constraints;

public class DepartmentFormController implements Initializable {
	
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
    public void onBtnSaveAction() {
    	System.out.println("onBtnSaveAction");
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
	
	public void setDepartment(Department dep) {
		this.dep = dep;
	}
	
	public void updateFormData() {
		if(dep == null) {
			throw new IllegalStateException("Dep was null!");
		}
		tfId.setText(String.valueOf(dep.getId()));
		tfName.setText(dep.getName());
	}
}
