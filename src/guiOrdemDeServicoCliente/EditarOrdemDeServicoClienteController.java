package guiOrdemDeServicoCliente;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import Db.DbException;
import application.Main;
import entities.services.ClienteService;
import entities.services.FuncionarioService;
import entities.services.OrdemServicoClienteService;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import marcenaria.entities.Cliente;
import marcenaria.entities.Funcionario;
import marcenaria.entities.OrcamentoCliente;
import marcenaria.entities.OrdemServicoCliente;

public class EditarOrdemDeServicoClienteController implements Initializable {

	private OrcamentoCliente orcamentoCliente;
	private OrdemServicoCliente ordemServicoCliente;
	private OrdemServicoClienteService ordemServicoClienteService;
	private ClienteService clienteService;
	private FuncionarioService funcionarioService;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtNumOrcamento;

	@FXML
	private ComboBox<Cliente> cbCliente;

	@FXML
	private TextField txtDescServico;

	@FXML
	private DatePicker dpDataOrcamento;

	@FXML
	private DatePicker dpDataInicio;

	@FXML
	private DatePicker dpPrazoEntrega;

	@FXML
	private DatePicker dpDataEntrega;

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

	private ObservableList<Cliente> cbListCliente;

	private ObservableList<Funcionario> cbListFuncionario;

	private ObservableList<String> statusList;

	public void setOrcamentoCliente(OrcamentoCliente orcamentoCliente) {
		this.orcamentoCliente = orcamentoCliente;
	}

	public void setOrdemServicoCliente(OrdemServicoCliente ordemServicoCliente) {
		this.ordemServicoCliente = ordemServicoCliente;
	}

	public void setServicos(OrdemServicoClienteService ordemServicoClienteService, ClienteService clienteService,
			FuncionarioService funcionarioService) {
		this.ordemServicoClienteService = ordemServicoClienteService;
		this.clienteService = clienteService;
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

	public void receberDadosParaEditarOS() {
		if (ordemServicoCliente == null) {
			throw new IllegalStateException("Orcamento inexistente");
		}

		txtNumOrcamento.setText(String.valueOf(ordemServicoCliente.getNumeroPedido()));
		cbCliente.setValue(ordemServicoCliente.getCodCliente());
		txtDescServico.setText(ordemServicoCliente.getDescServico());
		if (ordemServicoCliente.getDataOrdem() != null) {
			dpDataOrcamento.setValue(
					LocalDate.ofInstant(ordemServicoCliente.getDataOrdem().toInstant(), ZoneId.systemDefault()));
		}
		if (ordemServicoCliente.getDataInicio() != null) {
			dpDataInicio.setValue(
					LocalDate.ofInstant(ordemServicoCliente.getDataInicio().toInstant(), ZoneId.systemDefault()));
		}
		if (ordemServicoCliente.getPrazoEntrega() != null) {
			dpPrazoEntrega.setValue(
					LocalDate.ofInstant(ordemServicoCliente.getPrazoEntrega().toInstant(), ZoneId.systemDefault()));
		}
		if (ordemServicoCliente.getDataEntrega() != null) {
			dpDataEntrega.setValue(
					LocalDate.ofInstant(ordemServicoCliente.getDataEntrega().toInstant(), ZoneId.systemDefault()));
		}

		statusServico.setValue(ordemServicoCliente.getStatusServico());
		txtValorTotalOrcamento.setText(String.valueOf(ordemServicoCliente.getValorTotal()));
		cbFuncionarioResp.setValue(ordemServicoCliente.getRegistroFunc());
		txtObs.setText(ordemServicoCliente.getObs());
	}

	public OrdemServicoCliente getOrdemServicoClienteData() {
		OrdemServicoCliente obj = new OrdemServicoCliente();

		obj.setId(ordemServicoCliente.getId());
		obj.setNumeroPedido(Integer.parseInt(txtNumOrcamento.getText()));
		obj.setCodCliente(cbCliente.getValue());
		obj.setDescServico(txtDescServico.getText());

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

		if (dpDataEntrega.getValue() != null) {
			Instant instant4 = Instant.from(dpDataEntrega.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataEntrega(Date.from(instant4));
		} else {
			obj.setDataEntrega(null);
		}

		obj.setStatusServico(statusServico.getValue());
		obj.setValorTotal(Double.parseDouble(txtValorTotalOrcamento.getText()));
		obj.setRegistroFunc(cbFuncionarioResp.getValue());
		obj.setObs(txtObs.getText());

		return obj;
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		try {
			ordemServicoCliente = getOrdemServicoClienteData();
			ordemServicoClienteService.saveOrUpdate(ordemServicoCliente);
			Utils.currentStage(event).close();
			loadOrdemServicoClienteVisualizar("/guiOrdemDeServicoCliente/OrdemServicoClienteVisualizar.fxml",
					(OrdemServicoClienteVisualizarController controller) -> {
						controller.setServices(new OrdemServicoClienteService(), new FuncionarioService(),
								new ClienteService());
						controller.updateTableViewOrdemClienteVisualizar();
					});
			Utils.currentStage(event).close();

		} catch (DbException e) {
			e.getMessage();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeComboBoxCliente();
		initializeComboBoxFuncionario();
		initializeComboBoxStatus();
	}

	// ORCAMENTO
	public void loadClienteOrcamento() {
		if (clienteService == null) {
			throw new IllegalStateException("Cliente service null");
		}

		List<Cliente> list = new ArrayList<>();
		list.add(orcamentoCliente.getCodCliente());
		cbListCliente = FXCollections.observableArrayList(list);
		cbCliente.setItems(cbListCliente);
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

	public void loadClienteOS() {
		if (clienteService == null) {
			throw new IllegalStateException("Cliente service null");
		}

		List<Cliente> list = clienteService.findAll();
		cbListCliente = FXCollections.observableArrayList(list);
		cbCliente.setItems(cbListCliente);
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
		List<String> list = Arrays.asList("ENTREGUE", "MONTAGEM", "RECEBIDO", "PRODUÇÃO");
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

	private void initializeComboBoxCliente() {
		Callback<ListView<Cliente>, ListCell<Cliente>> factory = lv -> new ListCell<Cliente>() {
			@Override
			protected void updateItem(Cliente item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		cbCliente.setCellFactory(factory);
		cbCliente.setButtonCell(factory.call(null));
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

	public synchronized <T> void loadOrdemServicoClienteVisualizar(String absoluteName, Consumer<T> initializeTable) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());

			T controller = loader.getController();
			initializeTable.accept(controller);
		} catch (IOException e) {
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}

}
