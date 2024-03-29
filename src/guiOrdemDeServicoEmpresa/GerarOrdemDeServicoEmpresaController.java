package guiOrdemDeServicoEmpresa;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import Db.DbException;
import entities.services.EmpresaService;
import entities.services.FuncionarioService;
import entities.services.OrdemServicoEmpresaService;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
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
import marcenaria.entities.Empresa;
import marcenaria.entities.Funcionario;
import marcenaria.entities.OrcamentoEmpresa;
import marcenaria.entities.OrdemServicoEmpresa;
import model.exceptions.ValidationException;

public class GerarOrdemDeServicoEmpresaController implements Initializable {

	private OrdemServicoEmpresaService ordemServicoEmpresaService;
	private EmpresaService empresaService;
	private FuncionarioService funcionarioService;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtNumOrcamento;

	@FXML
	private ComboBox<Empresa> cbEmpresa;

	@FXML
	private TextField txtNomeResponsavel;

	@FXML
	private TextField txtDescServico;

	@FXML
	private DatePicker dpDataOrcamento;

	@FXML
	private DatePicker dpDataInicio;
	@FXML
	private Label errorDataInicio;

	@FXML
	private DatePicker dpPrazoEntrega;
	@FXML
	private Label errorPrazoEntrega;

	@FXML
	private ComboBox<String> statusServico;

	@FXML
	private TextField txtValorTotalOrcamento;

	@FXML
	private ComboBox<Funcionario> cbFuncionarioResp;

	@FXML
	private TextField txtObs;

	@FXML
	private Button btCadastrar;

	@FXML
	private Button btCancelar;

	private ObservableList<Empresa> cbListEmpresa;

	private ObservableList<Funcionario> cbListFuncionario;

	private ObservableList<String> statusList;

	public void setServicos(OrdemServicoEmpresaService ordemServicoEmpresaService, EmpresaService empresaService,
			FuncionarioService funcionarioService) {
		this.ordemServicoEmpresaService = ordemServicoEmpresaService;
		this.empresaService = empresaService;
		this.funcionarioService = funcionarioService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	public void notificarDataChangeListener() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	public void receberDadosParaCriarOS(OrcamentoEmpresa orcamentoEmpresa) {
		if (orcamentoEmpresa == null) {
			throw new IllegalStateException("Orcamento inexistente");
		}

		txtNumOrcamento.setText(String.valueOf(orcamentoEmpresa.getNumOrcamento()));
		cbEmpresa.setValue(orcamentoEmpresa.getCodEmpresa());
		txtNomeResponsavel.setText(orcamentoEmpresa.getNomeResponsavel());
		if (orcamentoEmpresa.getDataOrcamento() != null) {
			dpDataOrcamento.setValue(
					LocalDate.ofInstant(orcamentoEmpresa.getDataOrcamento().toInstant(), ZoneId.systemDefault()));
		}
		txtDescServico.setText(orcamentoEmpresa.getDescServico());
		txtObs.setText(orcamentoEmpresa.getObs());
		txtValorTotalOrcamento.setText("R$ " + String.format("%.2f", orcamentoEmpresa.getValorTotal()));

		
		List<Empresa> list = new ArrayList<>();
		list.add(orcamentoEmpresa.getCodEmpresa());
		cbListEmpresa = FXCollections.observableArrayList(list);
		cbEmpresa.setItems(cbListEmpresa);
	}

	public OrdemServicoEmpresa getOrdemServicoEmpresaData() {
		OrdemServicoEmpresa obj = new OrdemServicoEmpresa();

		obj.setNumeroPedido(Integer.parseInt(txtNumOrcamento.getText()));
		obj.setCodEmpresa(cbEmpresa.getValue());
		obj.setDescServico(txtDescServico.getText());
		obj.setNomeResponsavel(txtNomeResponsavel.getText());

		Instant instant1 = Instant.from(dpDataOrcamento.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setDataOrdem(Date.from(instant1));

		if (dpDataInicio.getValue() != null) {
			Instant instant2 = Instant.from(dpDataInicio.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataInicio(Date.from(instant2));
		} else {
			obj.setDataInicio(null);
		}

		if (dpPrazoEntrega.getValue() != null) {
			Instant instant3 = Instant.from(dpPrazoEntrega.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setPrazoEntrega(Date.from(instant3));
		} else {
			obj.setPrazoEntrega(null);
		}

		obj.setDataEntrega(null);
		obj.setStatusServico(statusServico.getValue());
		obj.setValorTotal(Double.parseDouble(Utils.getValorTotalNota(txtValorTotalOrcamento.getText())));
		obj.setRegistroFunc(cbFuncionarioResp.getValue());
		obj.setObs(txtObs.getText());

		return obj;
	}

	public Map<String, String> validateExceptions() {
		ValidationException exception = new ValidationException("Erro de validação");

		if (dpDataInicio.getValue() == null) {
			exception.addError("dataInicio", "A data de início de produção deve ser inserida");
		}

		if (dpPrazoEntrega.getValue() == null) {
			exception.addError("prazoEntrega", "Um prazo de entrega deve ser estabelecido");
		}

		setErrorMessages(exception.getErrors());

		return exception.getErrors();
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		errorDataInicio.setText(fields.contains("dataInicio") ? errors.get("dataInicio") : "");
		errorPrazoEntrega.setText(fields.contains("prazoEntrega") ? errors.get("prazoEntrega") : "");

	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@FXML
	public void onBtCadastrarAction(ActionEvent event) {

		Map<String, String> errors = validateExceptions();
		try {
			if (errors.size() > 0) {
				setErrorMessages(errors);
			} else {
				OrdemServicoEmpresa ordemServicoEmpresa = getOrdemServicoEmpresaData();
				OrdemServicoEmpresa safetyVerification = ordemServicoEmpresaService.findByNumPedido(ordemServicoEmpresa.getNumeroPedido());
				if (safetyVerification != null) {
					Alerts.showAlert("Erro ao gerar ordem de serviço", null, "Já existe uma ordem de serviço para esse cliente.", AlertType.ERROR);
				} else {
					ordemServicoEmpresaService.saveOrUpdate(ordemServicoEmpresa);
					notificarDataChangeListener();
					Utils.currentStage(event).close();
				}
			}
		} catch (DbException e) {
			e.getMessage();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeComboBoxEmpresa();
		initializeComboBoxFuncionario();
		initializeComboBoxStatus();

		Utils.formatDatePicker(dpDataInicio, "dd/MM/yyyy");
		Utils.formatDatePicker(dpDataOrcamento, "dd/MM/yyyy");
		Utils.formatDatePicker(dpPrazoEntrega, "dd/MM/yyyy");
	}

	public void loadFuncionariosOrcamento() {
		if (funcionarioService == null) {
			throw new IllegalStateException("Funcionario Service null");
		}

		List<Funcionario> list = funcionarioService.findAll();
		cbListFuncionario = FXCollections.observableArrayList(list);
		cbFuncionarioResp.setItems(cbListFuncionario);
	}

	// ORDEM DE SERVICO

	public void loadEmpresaOS() {
		if (empresaService == null) {
			throw new IllegalStateException("Empresa service null");
		}

		List<Empresa> list = empresaService.findAll();
		cbListEmpresa = FXCollections.observableArrayList(list);
		cbEmpresa.setItems(cbListEmpresa);
	}

	public void loadFuncionariosOS() {
		if (funcionarioService == null) {
			throw new IllegalStateException("Funcionario Service null");
		}

		List<Funcionario> list = funcionarioService.findAll();
		cbListFuncionario = FXCollections.observableArrayList(list);
		cbFuncionarioResp.setItems(cbListFuncionario);
	}

	public void loadStatusServico() {
		List<String> list = Arrays.asList("ENTREGUE", "MONTAGEM", "RECEBIDO", "EM PRODUÇÃO");
		statusList = FXCollections.observableArrayList(list);
		statusServico.setItems(statusList);
	}

	private void initializeComboBoxFuncionario() {
		Callback<ListView<Funcionario>, ListCell<Funcionario>> factory = lv -> new ListCell<Funcionario>() {
			@Override
			protected void updateItem(Funcionario item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		cbFuncionarioResp.setCellFactory(factory);
		cbFuncionarioResp.setButtonCell(factory.call(null));
	}

	private void initializeComboBoxEmpresa() {
		Callback<ListView<Empresa>, ListCell<Empresa>> factory = lv -> new ListCell<Empresa>() {
			@Override
			protected void updateItem(Empresa item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeFantasia());
			}
		};
		cbEmpresa.setCellFactory(factory);
		cbEmpresa.setButtonCell(factory.call(null));
	}

	private void initializeComboBoxStatus() {
		Callback<ListView<String>, ListCell<String>> factory = lv -> new ListCell<String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : String.valueOf(item));
			}
		};
		statusServico.setCellFactory(factory);
		statusServico.setButtonCell(factory.call(null));
	}

}
