package guiCliente;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Db.DbException;
import application.Main;
import entities.services.ClienteService;
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

public class ClienteVisualizarController implements Initializable, DataChangeListener {

	private ClienteService clienteService;

	@FXML
	private TableView<Cliente> tableViewCliente;

	@FXML
	private TableColumn<Cliente, Integer> tableColumnCodCliente;

	@FXML
	private TableColumn<Cliente, String> tableColumnNome;

	@FXML
	private TableColumn<Cliente, String> tableColumnRg;

	@FXML
	private TableColumn<Cliente, String> tableColumnCpf;

	@FXML
	private TableColumn<Cliente, String> tableColumnRua;

	@FXML
	private TableColumn<Cliente, Integer> tableColumnNumero;

	@FXML
	private TableColumn<Cliente, String> tableColumnComplemento;

	@FXML
	private TableColumn<Cliente, String> tableColumnBairro;

	@FXML
	private TableColumn<Cliente, String> tableColumnCep;

	@FXML
	private TableColumn<Cliente, String> tableColumnCidade;

	@FXML
	private TableColumn<Cliente, String> tableColumnEstado;

	@FXML
	private TableColumn<Cliente, String> tableColumnUF;

	@FXML
	private TableColumn<Cliente, Integer> tableColumnDDD;

	@FXML
	private TableColumn<Cliente, String> tableColumnTelefone;

	@FXML
	private TableColumn<Cliente, String> tableColumnCelular;

	@FXML
	private TableColumn<Cliente, String> tableColumnEmail;

	@FXML
	private TableColumn<Cliente, Date> tableColumnDataCadastro;

	@FXML
	private TableColumn<Cliente, String> tableColumnObs;

	@FXML
	private TableColumn<Cliente, Cliente> tableColumnEditar;

	@FXML
	private TableColumn<Cliente, Cliente> tableColumnRemover;

	@FXML
	private TextField searchByCod;

	@FXML
	private Button btBuscar;

	@FXML
	private Button mostrarTodos;

	@FXML
	private Button btNovoCliente;

	private ObservableList<Cliente> obsList;

	@FXML
	public void onBtNovoCliente(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Cliente obj = new Cliente();
		createCadastroClienteForm(obj, parentStage, "/guiCliente/CadastroCliente.fxml");
	}

	@FXML
	public void onBtMostrarTodos() {
		updateTableViewCliente();
	}

	@FXML
	public void onBtBuscar() {

		if (clienteService == null) {
			throw new IllegalStateException("Service null");
		}

		Cliente buscaCliente = clienteService.findByCodCliente(Utils.tryParseToInt(searchByCod.getText()));

		if (buscaCliente == null) {
			Alerts.showAlert("Busca de cliente", null, "Nenhum cliente com esse código foi encontrado no sistema!",
					AlertType.INFORMATION);
		} else {
			obsList = FXCollections.observableArrayList(buscaCliente);
			tableViewCliente.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
		}

	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnCodCliente.setCellValueFactory(new PropertyValueFactory<>("codCliente"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnRg.setCellValueFactory(new PropertyValueFactory<>("rg"));
		tableColumnCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		tableColumnRua.setCellValueFactory(new PropertyValueFactory<>("rua"));
		tableColumnNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
		tableColumnComplemento.setCellValueFactory(new PropertyValueFactory<>("complemento"));
		tableColumnBairro.setCellValueFactory(new PropertyValueFactory<>("bairro"));
		tableColumnCep.setCellValueFactory(new PropertyValueFactory<>("cep"));
		tableColumnCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
		tableColumnEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
		tableColumnUF.setCellValueFactory(new PropertyValueFactory<>("uf"));
		tableColumnDDD.setCellValueFactory(new PropertyValueFactory<>("ddd"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		tableColumnCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnDataCadastro.setCellValueFactory(new PropertyValueFactory<>("dataCadastro"));
		Utils.formatTableColumnDate(tableColumnDataCadastro, "dd/MM/yyyy");
		tableColumnObs.setCellValueFactory(new PropertyValueFactory<>("obs"));

		searchByCod.setPromptText("Insira o código do cliente");

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCliente.prefHeightProperty().bind(stage.heightProperty());
		tableViewCliente.prefWidthProperty().bind(stage.widthProperty());

	}

	public void updateTableViewCliente() {
		if (clienteService == null) {
			throw new IllegalStateException("Service null");
		}

		List<Cliente> list = clienteService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewCliente.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createCadastroClienteForm(Cliente obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox vBox = loader.load();

			CadastroClienteController cadastroController = loader.getController();
			cadastroController.setCliente(obj);
			cadastroController.setClienteService(new ClienteService());
			cadastroController.subscribeDataChangeListener(this);
			cadastroController.updateClienteData();

			Stage cadastroClienteStage = new Stage();
			cadastroClienteStage.setTitle("Cadastro de cliente");
			cadastroClienteStage.setScene(new Scene(vBox));
			cadastroClienteStage.setResizable(false);
			cadastroClienteStage.initOwner(parentStage);
			cadastroClienteStage.initModality(Modality.WINDOW_MODAL);
			cadastroClienteStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableViewCliente();
	}

	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<Cliente, Cliente>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Cliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createCadastroClienteForm(obj, Utils.currentStage(event),
						"/guiCliente/CadastroCliente.fxml"));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<Cliente, Cliente>() {
			private final Button button = new Button("Deletar");

			@Override
			protected void updateItem(Cliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> excluirCliente(obj));
			}
		});
	}

	private void excluirCliente(Cliente obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("EXCLUIR CLIENTE",
				"Tem certeza que deseja remover esse cliente?");
		if (result.get() == ButtonType.OK) {
			if (clienteService == null) {
				throw new IllegalStateException("Cliente está vazio");
			}
			try {
				clienteService.removerCliente(obj);
				updateTableViewCliente();
			} catch (DbException e) {
				Alerts.showAlert("Erro ao excluir cliente", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
