package guiOrdemDeServicoCliente;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Db.DbException;
import application.Main;
import entities.services.ClienteService;
import entities.services.FuncionarioService;
import entities.services.OrdemServicoClienteService;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import marcenaria.entities.Cliente;
import marcenaria.entities.OrdemServicoCliente;

public class OrdemServicoClienteVisualizarController implements DataChangeListener, Initializable {

	private OrdemServicoClienteService ordemServicoClienteService;
	private FuncionarioService funcionarioService;
	private ClienteService clienteService;

	@FXML
	private TableView<OrdemServicoCliente> tableViewOrdemServicoCliente;

	@FXML
	private TableColumn<OrdemServicoCliente, Integer> tableColumnNumOrcamento;

	@FXML
	private TableColumn<Cliente, Integer> tableColumnCodCliente;

	@FXML
	private TableColumn<OrdemServicoCliente, String> tableColumnDescServico;

	@FXML
	private TableColumn<OrdemServicoCliente, Date> tableColumnDataOrdem;

	@FXML
	private TableColumn<OrdemServicoCliente, Date> tableColumnDataInicio;

	@FXML
	private TableColumn<OrdemServicoCliente, Date> tableColumnPrazoEntrega;

	@FXML
	private TableColumn<OrdemServicoCliente, Date> tableColumnDataEntrega;

	@FXML
	private TableColumn<OrdemServicoCliente, String> tableColumnStatusServico;

	@FXML
	private TableColumn<OrdemServicoCliente, Double> tableColumnValorTotal;

	@FXML
	private TableColumn<OrdemServicoCliente, String> tableColumnFuncResponsavel;

	@FXML
	private TableColumn<OrdemServicoCliente, String> tableColumnObs;

	@FXML
	private TableColumn<OrdemServicoCliente, OrdemServicoCliente> tableColumnEditar;

	@FXML
	private TableColumn<OrdemServicoCliente, OrdemServicoCliente> tableColumnRemover;

	ObservableList<OrdemServicoCliente> obsListOrdemServico;

	public void setServices(OrdemServicoClienteService ordemClienteServicoService,
			FuncionarioService funcionarioService, ClienteService clienteService) {
		this.ordemServicoClienteService = ordemClienteServicoService;
		this.funcionarioService = funcionarioService;
		this.clienteService = clienteService;
	}

	public void updateTableViewOrdemClienteVisualizar() {
		List<OrdemServicoCliente> list = ordemServicoClienteService.findAll();
		obsListOrdemServico = FXCollections.observableArrayList(list);
		tableViewOrdemServicoCliente.setItems(obsListOrdemServico);
	}

	@Override
	public void onDataChanged() {
		updateTableViewOrdemClienteVisualizar();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
		initEditButtons();
		initRemoveButtons();

	}

	public void initializeNodes() {
		tableColumnNumOrcamento.setCellValueFactory(new PropertyValueFactory<>("numeroPedido"));
		tableColumnCodCliente.setCellValueFactory(new PropertyValueFactory<>("codCliente"));
		tableColumnDescServico.setCellValueFactory(new PropertyValueFactory<>("descServico"));
		tableColumnDataOrdem.setCellValueFactory(new PropertyValueFactory<>("dataOrdem"));
		tableColumnDataInicio.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
		tableColumnPrazoEntrega.setCellValueFactory(new PropertyValueFactory<>("prazoEntrega"));
		tableColumnDataEntrega.setCellValueFactory(new PropertyValueFactory<>("dataEntrega"));
		tableColumnStatusServico.setCellValueFactory(new PropertyValueFactory<>("statusServico"));
		tableColumnValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
		tableColumnFuncResponsavel.setCellValueFactory(new PropertyValueFactory<>("funcResponsavel"));
		tableColumnObs.setCellValueFactory(new PropertyValueFactory<>("obs"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewOrdemServicoCliente.prefWidthProperty().bind(stage.widthProperty());
		tableViewOrdemServicoCliente.prefHeightProperty().bind(stage.heightProperty());
	}

	private void createGerarOrdemServicoClienteForm(OrdemServicoCliente obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox vBox = loader.load();

			GerarOrdemDeServicoClienteController controller = loader.getController();
			controller.setOrdemServicoCliente(obj);
			controller.setServicos(ordemServicoClienteService, clienteService, funcionarioService);
			controller.loadClienteOS();
			controller.loadFuncionariosOS();
			controller.loadStatusServico();
			controller.receberDadosParaEditarOS();

			Stage stage = new Stage();
			stage.setTitle("Gerar ordem de serviço");
			stage.setScene(new Scene(vBox));
			stage.setResizable(false);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(parentStage);
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<OrdemServicoCliente, OrdemServicoCliente>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(OrdemServicoCliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createGerarOrdemServicoClienteForm(obj, Utils.currentStage(event),
						"/guiOrdemDeServicoCliente/GerarOrdemDeServicoCliente.fxml"));

			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<OrdemServicoCliente, OrdemServicoCliente>() {
			private final Button button = new Button("Deletar");

			@Override
			protected void updateItem(OrdemServicoCliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction((event -> excluirOrdemServicoCliente(obj)));

			}
		});
	}

	private void excluirOrdemServicoCliente(OrdemServicoCliente obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("EXCLUIR ORÇAMENTO",
				"Tem certeza que deseja remover esse orçamento?");
		if (result.get() == ButtonType.OK) {
			if (ordemServicoClienteService == null) {
				throw new IllegalStateException("Orcamento vazio");
			}
			try {
				ordemServicoClienteService.removerOrdemServicoCliente(obj);
				updateTableViewOrdemClienteVisualizar();

			} catch (DbException e) {
				Alerts.showAlert("Erro ao excluir orçamento", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
