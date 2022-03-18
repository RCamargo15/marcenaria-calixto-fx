package guiOrcamentoEmpresa;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Db.DbException;
import application.Main;
import entities.services.EmpresaService;
import entities.services.OrcamentoEmpresaService;
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
import marcenaria.entities.Empresa;
import marcenaria.entities.OrcamentoEmpresa;
import marcenaria.entities.Produto;

public class OrcamentoEmpresaVisualizarController implements Initializable, DataChangeListener {

	private OrcamentoEmpresaService orcamentoEmpresaService;
	private EmpresaService empresaService;
	private ProdutoService produtoService;
	
	@FXML
	private TableView<OrcamentoEmpresa> tableViewOrcamentoEmpresa;
	
	@FXML
	private TableColumn<OrcamentoEmpresa, Integer> tableColumnId;

	@FXML
	private TableColumn<OrcamentoEmpresa, Integer> tableColumnNumOrcamento;

	@FXML
	private TableColumn<Empresa, Integer> tableColumnCodEmpresa;

	@FXML
	private TableColumn<Empresa, String> tableColumnTelefone;

	@FXML
	private TableColumn<Empresa, String> tableColumnCelular;

	@FXML
	private TableColumn<Empresa, String> tableColumnEmail;

	@FXML
	private TableColumn<OrcamentoEmpresa, String> tableColumnDescServico;

	@FXML
	private TableColumn<OrcamentoEmpresa, Date> tableColumnDataOrcamento;

	@FXML
	private TableColumn<Produto, Integer> tableColumnCodProduto;
	
	@FXML
	private TableColumn<OrcamentoEmpresa, Integer> tableColumnQuantidade;

	@FXML
	private TableColumn<OrcamentoEmpresa, Double> tableColumnValor;

	@FXML
	private TableColumn<OrcamentoEmpresa, Double> tableColumnValorTotal;

	@FXML
	private TableColumn<OrcamentoEmpresa, String> tableColumnObs;

	@FXML
	private TableColumn<OrcamentoEmpresa, OrcamentoEmpresa> tableColumnEditar;

	@FXML
	private TableColumn<OrcamentoEmpresa, OrcamentoEmpresa> tableColumnRemover;

	@FXML
	private TextField searchByCod;

	@FXML
	private Button btBuscar;

	@FXML
	private Button mostrarTodos;

	@FXML
	private Button btNovoOrcamentoEmpresa;

	private ObservableList<OrcamentoEmpresa> obsList;

	public void SetServices(OrcamentoEmpresaService orcamentoEmpresaService, EmpresaService empresaService,
			ProdutoService produtoService) {
		this.orcamentoEmpresaService = orcamentoEmpresaService;
		this.empresaService = empresaService;
		this.produtoService = produtoService;
	}

	@FXML
	public void onBtNovoOrcamentoEmpresa(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		OrcamentoEmpresa obj = new OrcamentoEmpresa();
		createCadastroOrcamentoEmpresaForm(obj, parentStage, "/guiOrcamentoEmpresa/GerarNovoOrcamentoEmpresa.fxml");
		
	}

	@FXML
	public void onBtMostrarTodos() {
		updateTableViewOrcamentoEmpresa();
	}

	@FXML
	public void onBtBuscar() {
		if (orcamentoEmpresaService == null) {
			throw new IllegalStateException("Orcamento Empresa null");
		}

		OrcamentoEmpresa buscaOrcamentoEmpresa = orcamentoEmpresaService
				.findByNumOrcamento(Integer.parseInt(searchByCod.getText()));

		if (buscaOrcamentoEmpresa == null) {
			Alerts.showAlert("Busca de or�amentos", null, "Nenhum or�amento encontrado no sistema", AlertType.ERROR);
		} else {
			obsList = FXCollections.observableArrayList(buscaOrcamentoEmpresa);
			tableViewOrcamentoEmpresa.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
		}
	}

	public void updateTableViewOrcamentoEmpresa() {
		if (orcamentoEmpresaService == null) {
			throw new IllegalStateException("Orcamento null");
		}

		List<OrcamentoEmpresa> list = orcamentoEmpresaService.findAllParaTabela();
		obsList = FXCollections.observableArrayList(list);
		tableViewOrcamentoEmpresa.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	@Override
	public void onDataChanged() {
		updateTableViewOrcamentoEmpresa();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnNumOrcamento.setCellValueFactory(new PropertyValueFactory<>("numOrcamento"));
		tableColumnCodEmpresa.setCellValueFactory(new PropertyValueFactory<>("codEmpresa"));
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
		tableViewOrcamentoEmpresa.prefHeightProperty().bind(stage.heightProperty());
		tableViewOrcamentoEmpresa.prefWidthProperty().bind(stage.widthProperty());
	}

	private void createCadastroOrcamentoEmpresaForm(OrcamentoEmpresa obj, Stage parentStage, String string) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(string));
			VBox vBox = loader.load();
			
			GerarNovoOrcamentoEmpresaController orcamentoController = loader.getController();
			orcamentoController.setOrcamentoEmpresa(obj);
			orcamentoController.setServices(orcamentoEmpresaService, empresaService, produtoService);
			orcamentoController.loadEmpresas();
			orcamentoController.loadProdutos();
			orcamentoController.subscribeDataChangeListener(this);
			
			Stage orcamentoEmpresaStage = new Stage();
			orcamentoEmpresaStage.setTitle("Novo or�amento");
			orcamentoEmpresaStage.setScene(new Scene(vBox));
			orcamentoEmpresaStage.setResizable(false);
			orcamentoEmpresaStage.initOwner(parentStage);
			orcamentoEmpresaStage.initModality(Modality.WINDOW_MODAL);
			orcamentoEmpresaStage.showAndWait();
		}
		catch(IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}

	}
	
	private void createEditarOrcamentoEmpresaForm(OrcamentoEmpresa obj, Stage parentStage, String string) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(string));
			VBox vBox = loader.load();
			
			EditarOrcamentoEmpresaController editarController = loader.getController();
			editarController.setOrcamentoEmpresa(obj);
			editarController.setServices(orcamentoEmpresaService, empresaService, produtoService);
			editarController.loadEmpresas();
			editarController.updateOrcamentoEmpresaData();
			editarController.subscribeDataChangeListener(this);
			
			Stage editarOrcamentoStage = new Stage();
			editarOrcamentoStage.setTitle("Editar or�amento");
			editarOrcamentoStage.setScene(new Scene(vBox));
			editarOrcamentoStage.setResizable(false);
			editarOrcamentoStage.initOwner(parentStage);
			editarOrcamentoStage.initModality(Modality.WINDOW_MODAL);
			editarOrcamentoStage.showAndWait();
		}
		catch(IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<OrcamentoEmpresa, OrcamentoEmpresa>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(OrcamentoEmpresa obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createEditarOrcamentoEmpresaForm(obj, Utils.currentStage(event),
						"/guiOrcamentoEmpresa/EditarOrcamentoEmpresa.fxml"));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<OrcamentoEmpresa, OrcamentoEmpresa>() {
			private final Button button = new Button("Deletar");

			@Override
			protected void updateItem(OrcamentoEmpresa obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> excluirOrcamentoEmpresa(obj));
			}
		});
	}
	
	private void excluirOrcamentoEmpresa(OrcamentoEmpresa obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("EXCLUIR OR�AMENTO", "Tem certeza que deseja remover esse or�amento?");
			if(result.get() == ButtonType.OK) {
				if(orcamentoEmpresaService == null) {
					throw new IllegalStateException("Orcamento vazio");
				}
				try {
					orcamentoEmpresaService.removerOrcamento(obj);
					updateTableViewOrcamentoEmpresa();
				}
				catch(DbException e) {
					Alerts.showAlert("Erro ao excluir or�amento", null, e.getMessage(), AlertType.ERROR);
				}
			}
	}
}