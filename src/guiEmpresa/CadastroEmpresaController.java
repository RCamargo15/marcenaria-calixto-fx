package guiEmpresa;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import Db.DbException;
import entities.services.EmpresaService;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import marcenaria.entities.Empresa;
import model.exceptions.ValidationException;

public class CadastroEmpresaController implements Initializable {

	private Empresa empresa;

	private EmpresaService empresaService;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtCodEmpresa;

	@FXML
	private TextField txtRazaoSocial;
	@FXML
	private Label errorRazaoSocial;

	@FXML
	private TextField txtNomeFantasia;
	@FXML
	private Label errorNomeFantasia;

	@FXML
	private TextField txtCnpj;
	@FXML
	private Label errorCnpj;

	@FXML
	private TextField txtAtividadeFim;
	@FXML
	private Label errorAtividadeFim;

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
	private TextField txtSite;


	@FXML
	private TextField txtEmail;
	
	
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

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		if (empresa == null) {
			throw new IllegalStateException("Empresa vazio");
		}
		if (empresaService == null) {
			throw new IllegalStateException("EmpresaService vazio");
		}
		try {
			empresa = getEmpresaData();
			empresaService.saveOrUpdate(empresa);
			notificarDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Erro ao cadastrar empresa", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notificarDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	private Empresa getEmpresaData() {
		Empresa obj = new Empresa();

		obj.setCodEmpresa(Utils.tryParseToInt(txtCodEmpresa.getText()));
		obj.setRazaoSocial(txtRazaoSocial.getText());
		obj.setNomeFantasia(txtNomeFantasia.getText());
		obj.setCnpj(txtCnpj.getText());
		obj.setAtividadeFim(txtAtividadeFim.getText());
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
		obj.setSite(txtSite.getText());
		obj.setEmail(txtEmail.getText());
		obj.setObs(txtObs.getText());

		ValidationException exception = new ValidationException("Erro de valida��o");

		if (txtRazaoSocial.getText() == null || txtRazaoSocial.getText().trim().equals("")) {
			exception.addError("razaoSocial", "O campo RAZÃO SOCIAL não pode estar vazio.");
		}

		if (txtNomeFantasia.getText() == null || txtNomeFantasia.getText().trim().equals("")) {
			exception.addError("nomeFantasia", "O campo NOME FANTASIA não pode estar vazio");
		}
		
		if (txtAtividadeFim.getText() == null || txtAtividadeFim.getText().trim().equals("")) {
			exception.addError("atividadeFim", "Insira um ramo de atuação para esta empresa");
		}

		if (txtCnpj.getText() == null || txtCnpj.getText().trim().equals("")) {
			exception.addError("CNPJ", "Insira um número de CNPJ");
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

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void updateEmpresaData() {
		if (empresa == null) {
			throw new IllegalStateException("Empresa vazia");
		}
		txtCodEmpresa.setText(String.valueOf(empresa.getCodEmpresa()));
		txtRazaoSocial.setText(empresa.getRazaoSocial());
		txtNomeFantasia.setText(empresa.getNomeFantasia());
		txtCnpj.setText(empresa.getCnpj());
		txtAtividadeFim.setText(empresa.getAtividadeFim());
		txtRua.setText(empresa.getRua());
		txtNumero.setText(String.valueOf(empresa.getNumero()));
		txtComplemento.setText(empresa.getComplemento());
		txtBairro.setText(empresa.getBairro());
		txtCep.setText(empresa.getCep());
		txtCidade.setText(empresa.getCidade());
		txtEstado.setText(empresa.getEstado());
		txtUF.setText(empresa.getUf());
		txtDdd.setText(String.valueOf(empresa.getDdd()));
		txtTelefone.setText(empresa.getTelefone());
		txtSite.setText(empresa.getSite());
		txtEmail.setText(empresa.getEmail());
		

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		errorRazaoSocial.setText(fields.contains("razaoSocial") ? errors.get("razaoSocial") : "");
		errorNomeFantasia.setText(fields.contains("nomeFantasia") ? errors.get("nomeFantasia") : "");
		errorCnpj.setText(fields.contains("CNPJ") ? errors.get("CNPJ") : "");
		errorAtividadeFim.setText(fields.contains("atividadeFim") ? errors.get("atividadeFim") : "");
		errorRua.setText(fields.contains("Rua") ? errors.get("Rua") : "");
		errorNumero.setText(fields.contains("Numero") ? errors.get("Numero") : "");
		errorBairro.setText(fields.contains("Bairro") ? errors.get("Bairro") : "");
		errorCep.setText(fields.contains("CEP") ? errors.get("CEP") : "");
		errorDdd.setText(fields.contains("DDD") ? errors.get("DDD") : "");
		errorCidade.setText(fields.contains("Cidade") ? errors.get("Cidade") : "");
		errorEstado.setText(fields.contains("Estado") ? errors.get("Estado") : "");
		errorUF.setText(fields.contains("UF") ? errors.get("UF") : "");
		errorTelefone.setText(fields.contains("Contatos") ? errors.get("Contatos") : "");

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtNumero);
		Constraints.setTextFieldInteger(txtDdd);
		Constraints.setTextFieldInteger(txtTelefone);
		Constraints.setTextFieldMaxLength(txtDdd, 2);
		Constraints.setTextFieldMaxLength(txtCep, 9);
		Constraints.setTextFieldMaxLength(txtTelefone, 9);
		Constraints.setTextFieldMaxLength(txtCnpj, 50);
		Constraints.setTextFieldMaxLength(txtUF, 2);
	}
}
