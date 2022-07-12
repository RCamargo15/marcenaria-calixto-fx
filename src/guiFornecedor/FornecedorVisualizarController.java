package guiFornecedor;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Db.DbException;
import application.Main;
import entities.services.FornecedorService;
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
import marcenaria.entities.Fornecedor;

public class FornecedorVisualizarController implements Initializable, DataChangeListener {

	private FornecedorService fornecedorService;

	@FXML
	private TableView<Fornecedor> tableViewFornecedor;

	@FXML
	private TableColumn<Fornecedor, Integer> tableColumnCodFornecedor;

	@FXML
	private TableColumn<Fornecedor, String> tableColumnRazaoSocial;

	@FXML
	private TableColumn<Fornecedor, String> tableColumnNomeFantasia;

	@FXML
	private TableColumn<Fornecedor, String> tableColumnCnpj;

	@FXML
	private TableColumn<Fornecedor, String> tableColumnAtividadeFim;

	@FXML
	private TableColumn<Fornecedor, String> tableColumnRua;

	@FXML
	private TableColumn<Fornecedor, Integer> tableColumnNumero;

	@FXML
	private TableColumn<Fornecedor, String> tableColumnComplemento;

	@FXML
	private TableColumn<Fornecedor, String> tableColumnBairro;

	@FXML
	private TableColumn<Fornecedor, String> tableColumnCep;

	@FXML
	private TableColumn<Fornecedor, String> tableColumnCidade;

	@FXML
	private TableColumn<Fornecedor, String> tableColumnEstado;

	@FXML
	private TableColumn<Fornecedor, String> tableColumnUF;

	@FXML
	private TableColumn<Fornecedor, Integer> tableColumnDDD;

	@FXML
	private TableColumn<Fornecedor, String> tableColumnTelefone;

	@FXML
	private TableColumn<Fornecedor, String> tableColumnSite;

	@FXML
	private TableColumn<Fornecedor, String> tableColumnEmail;

	@FXML
	private TableColumn<Fornecedor, String> tableColumnObs;

	@FXML
	private TableColumn<Fornecedor, Fornecedor> tableColumnEditar;

	@FXML
	private TableColumn<Fornecedor, Fornecedor> tableColumnRemover;

	@FXML
	private TextField searchByCod;

	@FXML
	private Button btBuscar;

	@FXML
	private Button mostrarTodos;

	@FXML
	private Button btNovoFornecedor;

	private ObservableList<Fornecedor> obsList;

	@FXML
	public void onBtNovoFornecedor(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Fornecedor obj = new Fornecedor();
		createCadastroFornecedorForm(obj, parentStage, "/guiFornecedor/CadastroFornecedor.fxml");
	}

	@FXML
	public void onBtMostrarTodos() {
		searchByCod.setText("");
		updateTableViewFornecedor();
	}

	@FXML
	public void onBtBuscar() {
		if (fornecedorService == null) {
			throw new IllegalStateException("Service null");
		}
		
		List<Fornecedor> buscaFornecedor = new ArrayList<>();
		if(searchByCod.getText().equals("") || searchByCod.getText() == null) {
			buscaFornecedor = Collections.emptyList();
		}
		else {
			buscaFornecedor = fornecedorService.findByNomeFornecedor(searchByCod.getText());
		}

		if (buscaFornecedor.isEmpty()) {
			Alerts.showAlert("Busca de fornecedor", null,
					"Nenhuma fornecedor foi encontrado no sistema!", AlertType.INFORMATION);
		} else {
			obsList = FXCollections.observableArrayList(buscaFornecedor);
			tableViewFornecedor.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
		}

	}

	public void setFornecedorService(FornecedorService fornecedorService) {
		this.fornecedorService = fornecedorService;
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnCodFornecedor.setCellValueFactory(new PropertyValueFactory<>("codFornecedor"));
		tableColumnRazaoSocial.setCellValueFactory(new PropertyValueFactory<>("razaoSocial"));
		tableColumnNomeFantasia.setCellValueFactory(new PropertyValueFactory<>("nomeFantasia"));
		tableColumnCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
		tableColumnAtividadeFim.setCellValueFactory(new PropertyValueFactory<>("atividadeFim"));
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
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnSite.setCellValueFactory(new PropertyValueFactory<>("site"));
		tableColumnObs.setCellValueFactory(new PropertyValueFactory<>("obs"));

		searchByCod.setPromptText("Insira o nome do fornecedor");

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewFornecedor.prefHeightProperty().bind(stage.heightProperty());
		tableViewFornecedor.prefWidthProperty().bind(stage.widthProperty());

	}

	public void updateTableViewFornecedor() {
		if (fornecedorService == null) {
			throw new IllegalStateException("Service null");
		}

		List<Fornecedor> list = fornecedorService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewFornecedor.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createCadastroFornecedorForm(Fornecedor obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox vBox = loader.load();

			CadastroFornecedorController cadastroController = loader.getController();
			cadastroController.setFornecedor(obj);
			cadastroController.setFornecedorService(new FornecedorService());
			cadastroController.subscribeDataChangeListener(this);
			cadastroController.updateFornecedorData();

			Stage cadastroFornecedorStage = new Stage();
			cadastroFornecedorStage.setTitle("Cadastro de fornecedor");
			cadastroFornecedorStage.setScene(new Scene(vBox));
			cadastroFornecedorStage.setResizable(false);
			cadastroFornecedorStage.initOwner(parentStage);
			cadastroFornecedorStage.initModality(Modality.WINDOW_MODAL);
			cadastroFornecedorStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableViewFornecedor();
	}

	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<Fornecedor, Fornecedor>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Fornecedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createCadastroFornecedorForm(obj, Utils.currentStage(event),
						"/guiFornecedor/CadastroFornecedor.fxml"));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<Fornecedor, Fornecedor>() {
			private final Button button = new Button("Deletar");

			@Override
			protected void updateItem(Fornecedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> excluirFornecedor(obj));
			}
		});
	}

	private void excluirFornecedor(Fornecedor obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("EXCLUIR FORNECEDOR",
				"Tem certeza que deseja remover esse fornecedor do seu banco de dados?");
		if (result.get() == ButtonType.OK) {
			if (fornecedorService == null) {
				throw new IllegalStateException("Fornecedor está vazio");
			}
			try {
				fornecedorService.removerFornecedor(obj);
				updateTableViewFornecedor();
			} catch (DbException e) {
				Alerts.showAlert("Erro ao excluir fornecedor", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
