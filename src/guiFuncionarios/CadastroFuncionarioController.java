package guiFuncionarios;

import java.io.IOException;
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
import java.util.function.Consumer;

import Db.DbException;
import application.Main;
import entities.services.FuncionarioService;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import marcenaria.entities.Funcionario;
import model.exceptions.ValidationException;

public class CadastroFuncionarioController implements Initializable {

	private Funcionario funcionario;

	private FuncionarioService funcionarioService;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtRegistroFunc;

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
	private TextField txtCtps;
	@FXML
	private Label errorCtps;

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
	private DatePicker dpDataNasc;
	@FXML
	private Label errorDataNasc;

	@FXML
	private DatePicker dpDataAdmissao;
	@FXML
	private Label errorDataAdmissao;

	@FXML
	private TextField txtTipoSang;
	@FXML
	private Label errorTipoSang;

	@FXML
	private TextField txtFuncao;
	@FXML
	private Label errorFuncao;

	@FXML
	private TextField txtSetor;

	@FXML
	private TextField txtSalario;
	@FXML
	private Label errorSalario;

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
	private GridPane gpFuncional;

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public void setFuncionarioService(FuncionarioService funcionarioService) {
		this.funcionarioService = funcionarioService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		if (funcionario == null) {
			throw new IllegalStateException("Funcionario não foi injetado no sistema");
		}
		if (funcionarioService == null) {
			throw new IllegalStateException("FuncionarioService não foi injetado no sistema");
		}

		try {
			funcionario = getFuncionarioData();
			funcionarioService.saveOrUpdate(funcionario);
			notificarDataChangeListeners();
			loadFuncionarioVisualizar("/guiFuncionarios/FuncionarioVisualizar.fxml",
					(FuncionarioVisualizarController controller) -> {
						controller.setFuncionarioService(new FuncionarioService());
						controller.updateTableViewFuncionario();
					});
		} catch (ValidationException e) {
			setErrorMessage(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Erro ao cadastrar funcionario", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notificarDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	private Funcionario getFuncionarioData() {
		Funcionario obj = new Funcionario();

		obj.setRegistroFunc(Utils.tryParseToInt(txtRegistroFunc.getText()));
		obj.setNome(txtNome.getText());
		obj.setRg(txtRg.getText());
		obj.setCpf(txtCpf.getText());
		obj.setCtps(txtCtps.getText());
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
		obj.setTipoSang(txtTipoSang.getText());
		obj.setFuncao(txtFuncao.getText());
		obj.setSetor(txtSetor.getText());
		obj.setSalario(Double.parseDouble(txtSalario.getText()));
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

		if (txtCtps.getText() == null || txtCtps.getText().trim().equals("")) {
			exception.addError("CTPS", "Insira o número da carteira de trabalho do profissional");
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

		if ((txtTelefone.getText() == null || txtTelefone.getText().trim().equals(""))
				&& (txtCelular.getText() == null || txtCelular.getText().trim().equals(""))) {
			exception.addError("Contatos", "Pelo menos um número para contato deve ser inserido.");
		}

		if (dpDataNasc.getValue() == null) {
			exception.addError("dataNasc", "A data de nascimento não pode estar vazia.");
		} else {
			Instant instant = Instant.from(dpDataNasc.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataNasc(Date.from(instant));
		}

		if (dpDataAdmissao.getValue() == null) {
			exception.addError("dataAdmissao", "A data de nascimento não pode estar vazia.");
		} else {
			Instant instant = Instant.from(dpDataAdmissao.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataAdmissao(Date.from(instant));
		}
		
		if(txtTipoSang.getText() == null || txtTipoSang.getText().trim().equals("")) {
			exception.addError("tipo", "Informar a tipagem sanguínea do funcionário");
		}
		if(txtSalario.getText() == null || txtSalario.getText().trim().equals("")){
			exception.addError("salario", "Digite uma remuneração para esse funcionário");
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

	public void updateFuncionarioData() {
		if (funcionario == null) {
			throw new IllegalStateException("Funcionario vazio");
		}
		txtRegistroFunc.setText(String.valueOf(funcionario.getRegistroFunc()));
		txtNome.setText(funcionario.getNome());
		txtRg.setText(funcionario.getRg());
		txtCpf.setText(funcionario.getCpf());
		txtCtps.setText(funcionario.getCtps());
		txtRua.setText(funcionario.getRua());
		txtNumero.setText(String.valueOf(funcionario.getNumero()));
		txtComplemento.setText(funcionario.getComplemento());
		txtBairro.setText(funcionario.getBairro());
		txtCep.setText(funcionario.getCep());
		txtCidade.setText(funcionario.getCidade());
		txtEstado.setText(funcionario.getEstado());
		txtUF.setText(funcionario.getUf());
		txtDdd.setText(String.valueOf(funcionario.getDdd()));
		txtTelefone.setText(funcionario.getTelefone());
		txtCelular.setText(funcionario.getCelular());
		txtFuncao.setText(funcionario.getFuncao());
		txtSetor.setText(funcionario.getSetor());
		txtSalario.setText(String.valueOf(funcionario.getSalario()));
		if (funcionario.getDataNasc() != null) {
			dpDataNasc.setValue(LocalDate.ofInstant(funcionario.getDataNasc().toInstant(), ZoneId.systemDefault()));
		}
		if(funcionario.getDataAdmissao() != null) {
			dpDataAdmissao.setValue(LocalDate.ofInstant(funcionario.getDataAdmissao().toInstant(), ZoneId.systemDefault()));
		}
		txtObs.setText(funcionario.getObs());

	}

	private void setErrorMessage(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		errorNome.setText(fields.contains("Nome") ? errors.get("Nome") : "");
		errorRg.setText(fields.contains("RG") ? errors.get("RG") : "");
		errorCpf.setText(fields.contains("CPF") ? errors.get("CPF") : "");
		errorRua.setText(fields.contains("Rua") ? errors.get("Rua") : "");
		errorNumero.setText(fields.contains("Numero") ? errors.get("Numero") : "");
		errorBairro.setText(fields.contains("Bairro") ? errors.get("Bairro") : "");
		errorCep.setText(fields.contains("CEP") ? errors.get("CEP") : "");
		errorDdd.setText(fields.contains("DDD") ? errors.get("DDD") : "");
		errorTelefone.setText(fields.contains("Contatos") ? errors.get("Contatos") : "");
		errorDataNasc.setText(fields.contains("dataNasc") ? errors.get("dataNasc") : "");
		errorDataAdmissao.setText(fields.contains("dataAdmissao") ? errors.get("dataAdmissao") : "");
		errorCidade.setText(fields.contains("Cidade") ? errors.get("Cidade") : "");
		errorEstado.setText(fields.contains("Estado") ? errors.get("Estado") : "");
		errorUF.setText(fields.contains("UF") ? errors.get("UF") : "");
		errorCtps.setText(fields.contains("CTPS") ? errors.get("CTPS") : "");
		errorTipoSang.setText(fields.contains("tipo") ? errors.get("tipo") : "");
		errorSalario.setText(fields.contains("salario") ? errors.get("salario") : "");

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

		Utils.formatDatePicker(dpDataNasc, "dd/MM/yyyy");
		Utils.formatDatePicker(dpDataAdmissao, "dd/MM/yyyy");

		Stage stage = (Stage) Main.getMainScene().getWindow();
		gpPessoal.prefHeightProperty().bind(stage.heightProperty());
		gpPessoal.prefWidthProperty().bind(stage.widthProperty());
		gpEndereco.prefHeightProperty().bind(stage.heightProperty());
		gpEndereco.prefWidthProperty().bind(stage.widthProperty());
		gpFuncional.prefWidthProperty().bind(stage.widthProperty());
		gpFuncional.prefHeightProperty().bind(stage.heightProperty());
	}

	private synchronized <T> void loadFuncionarioVisualizar(String absoluteName, Consumer<T> initializaTable) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainManu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainManu);
			mainVBox.getChildren().addAll(newVBox.getChildren());

			T controller = loader.getController();
			initializaTable.accept(controller);

		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
