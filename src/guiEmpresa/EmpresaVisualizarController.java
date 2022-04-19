package guiEmpresa;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Db.DbException;
import application.Main;
import entities.services.EmpresaService;
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
import marcenaria.entities.Empresa;

public class EmpresaVisualizarController implements Initializable, DataChangeListener {

	private EmpresaService empresaService;

	@FXML
	private TableView<Empresa> tableViewEmpresa;

	@FXML
	private TableColumn<Empresa, Integer> tableColumnCodEmpresa;

	@FXML
	private TableColumn<Empresa, String> tableColumnRazaoSocial;

	@FXML
	private TableColumn<Empresa, String> tableColumnNomeFantasia;

	@FXML
	private TableColumn<Empresa, String> tableColumnCnpj;

	@FXML
	private TableColumn<Empresa, String> tableColumnAtividadeFim;

	@FXML
	private TableColumn<Empresa, String> tableColumnRua;

	@FXML
	private TableColumn<Empresa, Integer> tableColumnNumero;

	@FXML
	private TableColumn<Empresa, String> tableColumnComplemento;

	@FXML
	private TableColumn<Empresa, String> tableColumnBairro;

	@FXML
	private TableColumn<Empresa, String> tableColumnCep;

	@FXML
	private TableColumn<Empresa, String> tableColumnCidade;

	@FXML
	private TableColumn<Empresa, String> tableColumnEstado;

	@FXML
	private TableColumn<Empresa, String> tableColumnUF;

	@FXML
	private TableColumn<Empresa, Integer> tableColumnDDD;

	@FXML
	private TableColumn<Empresa, String> tableColumnTelefone;

	@FXML
	private TableColumn<Empresa, String> tableColumnSite;

	@FXML
	private TableColumn<Empresa, String> tableColumnEmail;

	@FXML
	private TableColumn<Empresa, String> tableColumnObs;

	@FXML
	private TableColumn<Empresa, Empresa> tableColumnEditar;

	@FXML
	private TableColumn<Empresa, Empresa> tableColumnRemover;

	@FXML
	private TextField searchByCod;

	@FXML
	private Button btBuscar;

	@FXML
	private Button mostrarTodos;

	@FXML
	private Button btNovoEmpresa;

	private ObservableList<Empresa> obsList;

	@FXML
	public void onBtNovoEmpresa(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Empresa obj = new Empresa();
		createCadastroEmpresaForm(obj, parentStage, "/guiEmpresa/CadastroEmpresa.fxml");
	}

	@FXML
	public void onBtMostrarTodos() {
		updateTableViewEmpresa();
	}

	@FXML
	public void onBtBuscar() {

		if (empresaService == null) {
			throw new IllegalStateException("EmpresaService null");
		}

		Empresa buscaEmpresa = empresaService.findByCodEmpresa(Utils.tryParseToInt(searchByCod.getText()));

		if (buscaEmpresa == null) {
			Alerts.showAlert("Busca de empresa", null, "Nenhuma empresa com esse código foi encontrado no sistema!",
					AlertType.INFORMATION);
		} else {
			obsList = FXCollections.observableArrayList(buscaEmpresa);
			tableViewEmpresa.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
		}

	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnCodEmpresa.setCellValueFactory(new PropertyValueFactory<>("codEmpresa"));
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

		searchByCod.setPromptText("Insira o código da empresa");

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewEmpresa.prefHeightProperty().bind(stage.heightProperty());
		tableViewEmpresa.prefWidthProperty().bind(stage.widthProperty());
		
	}

	public void updateTableViewEmpresa() {
		if (empresaService == null) {
			throw new IllegalStateException("Service null");
		}

		List<Empresa> list = empresaService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewEmpresa.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createCadastroEmpresaForm(Empresa obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox vBox = loader.load();

			CadastroEmpresaController cadastroController = loader.getController();
			cadastroController.setEmpresa(obj);
			cadastroController.setEmpresaService(new EmpresaService());
			cadastroController.subscribeDataChangeListener(this);
			cadastroController.updateEmpresaData();

			Stage cadastroEmpresaStage = new Stage();
			cadastroEmpresaStage.setTitle("Cadastro de empresa");
			cadastroEmpresaStage.setScene(new Scene(vBox));
			cadastroEmpresaStage.setResizable(false);
			cadastroEmpresaStage.initOwner(parentStage);
			cadastroEmpresaStage.initModality(Modality.WINDOW_MODAL);
			cadastroEmpresaStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableViewEmpresa();
	}

	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<Empresa, Empresa>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Empresa obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createCadastroEmpresaForm(obj, Utils.currentStage(event),
						"/guiEmpresa/CadastroEmpresa.fxml"));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<Empresa, Empresa>() {
			private final Button button = new Button("Deletar");

			@Override
			protected void updateItem(Empresa obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> excluirEmpresa(obj));
			}
		});
	}

	private void excluirEmpresa(Empresa obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("EXCLUIR EMPRESA",
				"Tem certeza que deseja remover essa empresa?");
		if (result.get() == ButtonType.OK) {
			if (empresaService == null) {
				throw new IllegalStateException("Empresa está vazio");
			}
			try {
				empresaService.removerEmpresa(obj);
				updateTableViewEmpresa();
			} catch (DbException e) {
				Alerts.showAlert("Erro ao excluir empresa", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
