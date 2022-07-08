package guiCliente;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import Db.DbException;
import entities.services.ClienteService;
import gui.listeners.DataChangeListener;
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
import model.exceptions.ValidationException;

public class CadastroClienteController implements Initializable {

	private Cliente cliente;

	private ClienteService clienteService;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtCodCliente;

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
	private TextField txtCidade;
	@FXML
	private Label errorCidade;

	@FXML
	private TextField txtEstado;
	@FXML
	private Label errorEstado;

	@FXML
	private TextField txtUF;
	@FXML
	private Label errorUF;

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
	private Label errorDataCadastro;

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

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
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
			notificarDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Erro ao cadastrar cliente", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notificarDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	private Cliente getClienteData() {
		Cliente obj = new Cliente();

		obj.setCodCliente(Utils.tryParseToInt(txtCodCliente.getText()));
		obj.setNome(txtNome.getText());
		obj.setRg(txtRg.getText());
		obj.setCpf(txtCpf.getText());
		obj.setRua(txtRua.getText());
		obj.setNumero(Utils.tryParseToInt(txtNumero.getText()));
		obj.setComplemento(txtComplemento.getText());
		obj.setBairro(txtBairro.getText());
		obj.setCep(txtCep.getText());
		obj.setCidade(txtCidade.getText());
		obj.setEstado(txtEstado.getText());
		obj.setUf(txtUF.getText());
		obj.setDdd(Utils.tryParseToInt(txtDdd.getText()));
		obj.setTelefone(txtTelefone.getText());
		obj.setCelular(txtCelular.getText());
		obj.setEmail(txtEmail.getText());
		obj.setObs(txtObs.getText());

		ValidationException exception = new ValidationException("Erro de validação");
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("Nome", "O nome não pode estar vazio.");
		}

		if (txtRg.getText() == null || txtRg.getText().trim().equals("")) {
			exception.addError("RG", "Insira um número de RG");
		}

		if (txtCpf.getText() == null || txtCpf.getText().trim().equals("")) {
			exception.addError("CPF", "Insira um número de CPF");
		}

		if (txtRua.getText() == null || txtRua.getText().trim().equals("")) {
			exception.addError("Rua", "A rua não pode estar vazia.");
		}

		if (txtNumero.getText() == null || txtNumero.getText().trim().equals("")) {
			exception.addError("Numero", "Insira um número da rua");
		}

		if (txtBairro.getText() == null || txtBairro.getText().trim().equals("")) {
			exception.addError("Bairro", "O bairro não pode estar vazio");
		}

		if (txtCep.getText() == null || txtCep.getText().trim().equals("")) {
			exception.addError("CEP", "O CEP não pode estar vazio");
		}

		if (txtCidade.getText() == null || txtCidade.getText().trim().equals("")) {
			exception.addError("Cidade", "O campo cidade não pode estar vazio");
		}

		if (txtEstado.getText() == null || txtEstado.getText().trim().equals("")) {
			exception.addError("Estado", "O campo estado não pode estar vazio");
		}

		if (txtUF.getText() == null || txtUF.getText().trim().equals("")) {
			exception.addError("UF", "Necessário adicionar a sigla do estado");
		}

		if (txtDdd.getText() == null || txtDdd.getText().trim().equals("")) {
			exception.addError("DDD", "Insira um DDD válido");
		}

		if (txtTelefone.getText() == null || txtTelefone.getText().trim().equals("")) {
			exception.addError("Contatos", "Pelo menos um número para contato deve ser inserido.");
		}

		if (dpDataCadastro.getValue() == null) {
			exception.addError("dataCadastro", "A data não pode estar vazia.");
		} else {
			Instant instant = Instant.from(dpDataCadastro.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataCadastro(Date.from(instant));
		}

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

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
		txtCodCliente.setText(String.valueOf(cliente.getCodCliente()));
		txtNome.setText(cliente.getNome());
		txtRg.setText(cliente.getRg());
		txtCpf.setText(cliente.getCpf());
		txtRua.setText(cliente.getRua());
		txtNumero.setText(String.valueOf(cliente.getNumero()));
		txtComplemento.setText(cliente.getComplemento());
		txtBairro.setText(cliente.getBairro());
		txtCep.setText(cliente.getCep());
		txtCidade.setText(cliente.getCidade());
		txtEstado.setText(cliente.getEstado());
		txtUF.setText(cliente.getUf());
		txtDdd.setText(String.valueOf(cliente.getDdd()));
		txtTelefone.setText(cliente.getTelefone());
		txtCelular.setText(cliente.getCelular());
		txtEmail.setText(cliente.getEmail());
		if (cliente.getDataCadastro() != null) {
			dpDataCadastro.setValue(LocalDate.ofInstant(cliente.getDataCadastro().toInstant(), ZoneId.systemDefault()));
		}
		txtObs.setText(cliente.getObs());

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		errorNome.setText(fields.contains("Nome") ? errors.get("Nome") : "");
		errorRg.setText(fields.contains("RG") ? errors.get("RG") : "");
		errorCpf.setText(fields.contains("CPF") ? errors.get("CPF") : "");
		errorRua.setText(fields.contains("Rua") ? errors.get("Rua") : "");
		errorNumero.setText(fields.contains("Numero") ? errors.get("Numero") : "");
		errorBairro.setText(fields.contains("Bairro") ? errors.get("Bairro") : "");
		errorCep.setText(fields.contains("CEP") ? errors.get("CEP") : "");
		errorDdd.setText(fields.contains("DDD") ? errors.get("DDD") : "");
		errorCelular.setText(fields.contains("Contatos") ? errors.get("Contatos") : "");
		errorDataCadastro.setText(fields.contains("dataCadastro") ? errors.get("dataCadastro") : "");
		errorCidade.setText(fields.contains("Cidade") ? errors.get("Cidade") : "");
		errorEstado.setText(fields.contains("Estado") ? errors.get("Estado") : "");
		errorUF.setText(fields.contains("UF") ? errors.get("UF") : ""); 
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtNumero);
		Constraints.setTextFieldInteger(txtDdd);
		Constraints.setTextFieldInteger(txtCelular);
		Constraints.setTextFieldInteger(txtTelefone);
		Constraints.setTextFieldMaxLength(txtDdd, 2);
		Constraints.setTextFieldMaxLength(txtCelular, 9);
		Constraints.setTextFieldMaxLength(txtCep, 9);
		Constraints.setTextFieldMaxLength(txtTelefone, 9);
		Constraints.setTextFieldMaxLength(txtCpf, 15);
		Constraints.setTextFieldMaxLength(txtRg, 15);
		Constraints.setTextFieldMaxLength(txtUF, 2);
		Utils.formatDatePicker(dpDataCadastro, "dd/MM/yyyy");
	}
}
