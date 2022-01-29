package guiCliente;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CadastroClienteTelaPrincipalController implements Initializable {
	@FXML
	private TextField txtNome;
	@FXML
	private Label errorNome;
	
	@FXML
	private TextField txtRg;
	@FXML
	private Label errorRg;
	
	@FXML
	private TextField txtCpf;
	@FXML
	private Label errorCpf;
	
	@FXML
	private TextField txtRua;
	@FXML
	private Label errorRua;
	
	@FXML
	private TextField txtNumero;
	@FXML
	private Label errorNumero;
	
	@FXML
	private TextField txtComplemento;
	@FXML
	private Label errorComplemento;
	
	@FXML
	private TextField txtBairro;
	@FXML
	private Label errorBairro;
	
	@FXML
	private TextField txtCep;
	@FXML
	private Label errorCep;
	
	@FXML
	private TextField txtDdd;
	@FXML
	private Label errorDdd;
	
	@FXML
	private TextField txtTelefone;
	@FXML
	private Label errorTelefone;
	
	@FXML
	private TextField txtCelular;
	@FXML
	private Label errorCelular;
	
	@FXML
	private TextField txtEmail;
	@FXML
	private Label errorEmail;
	
	@FXML
	private DatePicker	dataCadastro;
	
	@FXML
	private TextField txtObs;
	
	@FXML
	private Button btCadastrar;
	
	@FXML
	private Button btCancelar;
	
	@FXML
	private GridPane gpPessoal;
	
	@FXML
	private GridPane gpEndereco;
	
	
	@FXML
	public void onBtCadastrarAction() {
		
	}
	
	@FXML
	public void onBtCancelarAction() {
		
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtNumero);
		Constraints.setTextFieldInteger(txtCep);
		Constraints.setTextFieldInteger(txtDdd);
		Constraints.setTextFieldInteger(txtCelular);
		Constraints.setTextFieldInteger(txtTelefone);
		
		Constraints.setTextFieldMaxLength(txtDdd, 2);
		Constraints.setTextFieldMaxLength(txtCelular, 9);
		Constraints.setTextFieldMaxLength(txtCep, 9);
		Constraints.setTextFieldMaxLength(txtCpf, 15);
		Constraints.setTextFieldMaxLength(txtRg, 15);
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		gpPessoal.prefHeightProperty().bind(stage.heightProperty());
		gpEndereco.prefHeightProperty().bind(stage.heightProperty());
	}
}
