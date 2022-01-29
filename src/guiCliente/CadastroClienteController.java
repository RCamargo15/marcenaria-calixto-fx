package guiCliente;

import java.net.URL;
import java.util.ResourceBundle;

import Db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import marcenaria.entities.Cliente;
import model.services.ClienteService;

public class CadastroClienteController implements Initializable {

	private Cliente cliente;

	private ClienteService clienteService;

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
	private DatePicker dpDataCadastro;

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

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		if (cliente == null) {
			throw new IllegalStateException("Cliente vazio");
		}
		if (clienteService == null) {
			throw new IllegalStateException("ClienteService vazio");
		}
		try {
			cliente = getClienteData();
			clienteService.saveOrUpdate(cliente);
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("Erro ao cadastrar cliente", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private Cliente getClienteData() {
		Cliente obj = new Cliente();

		obj.setCodCliente(0);
		obj.setNome(txtNome.getText());
		obj.setRg(txtRg.getText());
		obj.setCpf(txtCpf.getText());
		obj.setRua(txtRua.getText());
		obj.setNumero(Utils.tryParseToInt(txtNumero.getText()));
		obj.setComplemento(txtComplemento.getText());
		obj.setBairro(txtBairro.getText());
		obj.setCep(txtCep.getText());
		obj.setDdd(Utils.tryParseToInt(txtDdd.getText()));
		obj.setTelefone(txtTelefone.getText());
		obj.setCelular(txtCelular.getText());
		obj.setEmail(txtEmail.getText());
		obj.setObs(txtObs.getText());

		return obj;
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void updateClienteData() {
		if (cliente == null) {
			throw new IllegalStateException("Cliente vazio");
		}
		txtNome.setText(cliente.getNome());
		txtRg.setText(cliente.getRg());
		txtCpf.setText(cliente.getCpf());
		txtRua.setText(cliente.getRua());
		txtNumero.setText(String.valueOf(cliente.getNumero()));
		txtComplemento.setText(cliente.getComplemento());
		txtBairro.setText(cliente.getBairro());
		txtCep.setText(cliente.getCep());
		txtDdd.setText(String.valueOf(cliente.getDdd()));
		txtTelefone.setText(cliente.getTelefone());
		txtCelular.setText(cliente.getCelular());
		txtEmail.setText(cliente.getEmail());
		txtObs.setText(cliente.getObs());
		
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
		
		
	}

}
