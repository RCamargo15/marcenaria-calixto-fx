package guiOrcamentos;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Db.DbException;
import application.Main;
import entities.services.ClienteService;
import entities.services.OrcamentoClienteService;
import entities.services.ProdutoService;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import marcenaria.entities.Cliente;
import marcenaria.entities.OrcamentoCliente;
import marcenaria.entities.Produto;

public class OrcamentoClienteVisualizarController implements Initializable, DataChangeListener {

	private OrcamentoClienteService orcamentoClienteService;
	private ClienteService clienteService;
	private ProdutoService produtoService;
	
	@FXML
	private TableView<OrcamentoCliente> tableViewOrcamentoCliente;
	
	@FXML
	private TableColumn<OrcamentoCliente, Integer> tableColumnId;

	@FXML
	private TableColumn<OrcamentoCliente, Integer> tableColumnNumOrcamento;

	@FXML
	private TableColumn<Cliente, Integer> tableColumnCodCliente;

	@FXML
	private TableColumn<Cliente, String> tableColumnTelefone;

	@FXML
	private TableColumn<Cliente, String> tableColumnCelular;

	@FXML
	private TableColumn<Cliente, String> tableColumnEmail;

	@FXML
	private TableColumn<OrcamentoCliente, String> tableColumnDescServico;

	@FXML
	private TableColumn<OrcamentoCliente, Date> tableColumnDataOrcamento;

	@FXML
	private TableColumn<Produto, Integer> tableColumnCodProduto;
	
	@FXML
	private TableColumn<OrcamentoCliente, Integer> tableColumnQuantidade;

	@FXML
	private TableColumn<OrcamentoCliente, Double> tableColumnValor;

	@FXML
	private TableColumn<OrcamentoCliente, Double> tableColumnValorTotal;

	@FXML
	private TableColumn<OrcamentoCliente, String> tableColumnObs;

	@FXML
	private TableColumn<OrcamentoCliente, OrcamentoCliente> tableColumnEditar;

	@FXML
	private TableColumn<OrcamentoCliente, OrcamentoCliente> tableColumnRemover;

	@FXML
	private TextField searchByCod;

	@FXML
	private Button btBuscar;

	@FXML
	private Button mostrarTodos;

	@FXML
	private Button btNovoOrcamentoCliente;

	private ObservableList<OrcamentoCliente> obsList;

	public void SetServices(OrcamentoClienteService orcamentoClienteService, ClienteService clienteService,
			ProdutoService produtoService) {
		this.orcamentoClienteService = orcamentoClienteService;
		this.clienteService = clienteService;
		this.produtoService = produtoService;
	}

	@FXML
	public void onBtNovoOrcamentoCliente(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		OrcamentoCliente obj = new OrcamentoCliente();
		createCadastroOrcamentoClienteForm(obj, parentStage, "/guiOrcamentos/GerarNovoOrcamentoCliente.fxml");
		
	}

	@FXML
	public void onBtMostrarTodos() {
		updateTableViewOrcamentoCliente();
	}

	@FXML
	public void onBtBuscar() {
		if (orcamentoClienteService == null) {
			throw new IllegalStateException("Orcamento Cliente null");
		}

		OrcamentoCliente buscaOrcamentoCliente = orcamentoClienteService
				.findByNumOrcamento(Integer.parseInt(searchByCod.getText()));

		if (buscaOrcamentoCliente == null) {
			Alerts.showAlert("Busca de or�amentos", null, "Nenhum or�amento encontrado no sistema", AlertType.ERROR);
		} else {
			obsList = FXCollections.observableArrayList(buscaOrcamentoCliente);
			tableViewOrcamentoCliente.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
		}
	}

	public void updateTableViewOrcamentoCliente() {
		if (orcamentoClienteService == null) {
			throw new IllegalStateException("Orcamento null");
		}

		List<OrcamentoCliente> list = orcamentoClienteService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewOrcamentoCliente.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	@Override
	public void onDataChanged() {
		updateTableViewOrcamentoCliente();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnNumOrcamento.setCellValueFactory(new PropertyValueFactory<>("numOrcamento"));
		tableColumnCodCliente.setCellValueFactory(new PropertyValueFactory<>("codCliente"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		tableColumnCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnDescServico.setCellValueFactory(new PropertyValueFactory<>("descServico"));
		tableColumnDataOrcamento.setCellValueFactory(new PropertyValueFactory<>("dataOrcamento"));
		tableColumnCodProduto.setCellValueFactory(new PropertyValueFactory<>("codProduto"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
		tableColumnValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
		tableColumnObs.setCellValueFactory(new PropertyValueFactory<>("obs"));
		
		searchByCod.setPromptText("Insira c�digo busca");
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewOrcamentoCliente.prefHeightProperty().bind(stage.heightProperty());
		tableViewOrcamentoCliente.prefWidthProperty().bind(stage.widthProperty());
	}

	private void createCadastroOrcamentoClienteForm(OrcamentoCliente obj, Stage parentStage, String string) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(string));
			VBox vBox = loader.load();
			
			GerarNovoOrcamentoClienteController orcamentoController = loader.getController();
			orcamentoController.setOrcamentoCliente(obj);
			orcamentoController.setServices(orcamentoClienteService, clienteService, produtoService);
			orcamentoController.loadClientes();
			orcamentoController.loadProdutos();
			orcamentoController.subscribeDataChangeListener(this);
			
			Stage orcamentoClienteStage = new Stage();
			orcamentoClienteStage.setTitle("Novo or�amento");
			orcamentoClienteStage.setScene(new Scene(vBox));
			orcamentoClienteStage.setResizable(false);
			orcamentoClienteStage.initOwner(parentStage);
			orcamentoClienteStage.initModality(Modality.WINDOW_MODAL);
			orcamentoClienteStage.showAndWait();
		}
		catch(IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}

	}
	
	private void createEditarOrcamentoClienteForm(OrcamentoCliente obj, Stage parentStage, String string) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(string));
			VBox vBox = loader.load();
			
			EditarOrcamentoClienteController editarController = loader.getController();
			editarController.setOrcamentoCliente(obj);
			editarController.setServices(orcamentoClienteService, clienteService, produtoService);
			editarController.loadClientes();
			editarController.loadProdutos();
			editarController.updateOrcamentoClienteData();
			
			Stage editarOrcamentoStage = new Stage();
			editarOrcamentoStage.setTitle("Editar or�amento");
			editarOrcamentoStage.setScene(new Scene(vBox));
			editarOrcamentoStage.setResizable(false);
			editarOrcamentoStage.initOwner(parentStage);
			editarOrcamentoStage.initModality(Modality.WINDOW_MODAL);
			editarOrcamentoStage.showAndWait();
		}
		catch(IOException e) {
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<OrcamentoCliente, OrcamentoCliente>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(OrcamentoCliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createEditarOrcamentoClienteForm(obj, Utils.currentStage(event),
						"/guiOrcamentos/EditarOrcamentoCliente.fxml"));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<OrcamentoCliente, OrcamentoCliente>() {
			private final Button button = new Button("Deletar");

			@Override
			protected void updateItem(OrcamentoCliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> excluirOrcamentoCliente(obj));
			}
		});
	}
	
	private void excluirOrcamentoCliente(OrcamentoCliente obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("EXCLUIR OR�AMENTO", "Tem certeza que deseja remover esse or�amento?");
			if(result.get() == ButtonType.OK) {
				if(orcamentoClienteService == null) {
					throw new IllegalStateException("Orcamento vazio");
				}
				try {
					orcamentoClienteService.removerOrcamento(obj);
					updateTableViewOrcamentoCliente();
				}
				catch(DbException e) {
					Alerts.showAlert("Erro ao excluir or�amento", null, e.getMessage(), AlertType.ERROR);
				}
			}
	}
}