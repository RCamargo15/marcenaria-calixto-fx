package guiNotasCompras;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Db.DbException;
import application.Main;
import entities.services.EntradaProdutoService;
import entities.services.FornecedorService;
import entities.services.NotasComprasService;
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
import marcenaria.entities.Fornecedor;
import marcenaria.entities.NotasCompras;
import marcenaria.entities.Produto;

public class NotaCompraVisualizarController implements Initializable, DataChangeListener {

	private NotasComprasService notasComprasService;
	private FornecedorService fornecedorService;
	private ProdutoService produtoService;
	private EntradaProdutoService entradaProdutoService;
	
	@FXML
	private TableView<NotasCompras> tableViewNotasCompras;
	
	@FXML
	private TableColumn<NotasCompras, Integer> tableColumnCodNota;

	@FXML
	private TableColumn<Fornecedor, Integer> tableColumnCodFornecedor;

	@FXML
	private TableColumn<NotasCompras, String> tableColumnNumeroNF;
	
	@FXML
	private TableColumn<Produto, Integer> tableColumnCodProduto;

	@FXML
	private TableColumn<NotasCompras, Integer> tableColumnQuantidade;
	
	@FXML
	private TableColumn<NotasCompras, Double> tableColumnValorUnit;
	
	@FXML
	private TableColumn<NotasCompras, Double> tableColumnValorTotal;

	@FXML
	private TableColumn<NotasCompras, Double> tableColumnValorTotalNota;

	@FXML
	private TableColumn<NotasCompras, String> tableColumnChaveNF;
	
	@FXML
	private TableColumn<NotasCompras, Date> tableColumnDataEmissao;
	
	@FXML
	private TableColumn<NotasCompras, Date> tableColumnDataEntrada;

	@FXML
	private TableColumn<NotasCompras, String> tableColumnObs;

	@FXML
	private TableColumn<NotasCompras, NotasCompras> tableColumnEditar;

	@FXML
	private TableColumn<NotasCompras, NotasCompras> tableColumnRemover;
	

	@FXML
	private TextField txtSearchByCod;

	@FXML
	private Button btBuscar;
	
	@FXML
	private Button btEntradaProdutos;

	@FXML
	private Button btMostrarTodos;

	@FXML
	private Button btNovoNotasCompras;

	private ObservableList<NotasCompras> obsList;

	public void setServices(NotasComprasService notasComprasService, FornecedorService fornecedorService, ProdutoService produtoService, EntradaProdutoService entradaProdutoService) {
		this.notasComprasService = notasComprasService;
		this.fornecedorService = fornecedorService;
		this.produtoService = produtoService;
		this.entradaProdutoService = entradaProdutoService;
	}

	@FXML
	public void onBtNovoNotasCompras(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		NotasCompras obj = new NotasCompras();
		createCadastroNotasComprasForm(obj, parentStage, "/guiNotasCompras/GerarNovaNotaCompra.fxml");
		
	}

	@FXML
	public void onBtMostrarTodos() {
		updateTableViewNotasCompras();
	}

	@FXML
	public void onBtBuscar() {
		if (notasComprasService == null) {
			throw new IllegalStateException("Orcamento Fornecedor null");
		}

		NotasCompras buscaNotasCompras = notasComprasService.findByNumeroNFSingle(txtSearchByCod.getText());

		if (buscaNotasCompras == null) {
			Alerts.showAlert("Busca de orçamentos", null, "Nenhum orçamento encontrado no sistema", AlertType.ERROR);
		} else {
			obsList = FXCollections.observableArrayList(buscaNotasCompras);
			tableViewNotasCompras.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
		}
	}

	public void updateTableViewNotasCompras() {
		if (notasComprasService == null) {
			throw new IllegalStateException("Orcamento null");
		}

		List<NotasCompras> list = notasComprasService.findAllParaTabela();
		obsList = FXCollections.observableArrayList(list);
		tableViewNotasCompras.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	@Override
	public void onDataChanged() {
		updateTableViewNotasCompras();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnCodNota.setCellValueFactory(new PropertyValueFactory<>("codNota"));
		tableColumnCodFornecedor.setCellValueFactory(new PropertyValueFactory<>("codFornecedor"));
		tableColumnNumeroNF.setCellValueFactory(new PropertyValueFactory<>("numeroNF"));
		tableColumnCodProduto.setCellValueFactory(new PropertyValueFactory<>("codProduto"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tableColumnValorUnit.setCellValueFactory(new PropertyValueFactory<>("valorUnit"));
		tableColumnValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
		tableColumnValorTotalNota.setCellValueFactory(new PropertyValueFactory<>("valorTotalNota"));
		tableColumnChaveNF.setCellValueFactory(new PropertyValueFactory<>("chaveNF"));
		tableColumnDataEmissao.setCellValueFactory(new PropertyValueFactory<>("dataEmissao"));
		tableColumnDataEntrada.setCellValueFactory(new PropertyValueFactory<>("dataEntrada"));
		tableColumnObs.setCellValueFactory(new PropertyValueFactory<>("obs"));
		
		txtSearchByCod.setPromptText("Insira código busca");
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewNotasCompras.prefHeightProperty().bind(stage.heightProperty());
		tableViewNotasCompras.prefWidthProperty().bind(stage.widthProperty());
	}

	private void createCadastroNotasComprasForm(NotasCompras obj, Stage parentStage, String string) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(string));
			VBox vBox = loader.load();
			
			GerarNovaNotaCompraController novaNotaController = loader.getController();
			novaNotaController.setNotasCompras(obj);
			novaNotaController.setServices(notasComprasService, fornecedorService, produtoService, entradaProdutoService);
			novaNotaController.loadFornecedores();
			novaNotaController.loadProdutos();
			novaNotaController.subscribeDataChangeListener(this);
			
			Stage orcamentoFornecedorStage = new Stage();
			orcamentoFornecedorStage.setTitle("Inserir nota fiscal");
			orcamentoFornecedorStage.setScene(new Scene(vBox));
			orcamentoFornecedorStage.setResizable(false);
			orcamentoFornecedorStage.initOwner(parentStage);
			orcamentoFornecedorStage.initModality(Modality.WINDOW_MODAL);
			orcamentoFornecedorStage.showAndWait();
		}
		catch(IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}

	}
	
	private void createEditarNotasComprasForm(NotasCompras obj, Stage parentStage, String string) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(string));
			VBox vBox = loader.load();
			
			EditarNotaCompraController editarController = loader.getController();
//			editarController.setNotasCompras(obj);
//			editarController.setServices(notasComprasService, fornecedorService, produtoService);
//			editarController.loadFornecedors();
//			editarController.updateNotasComprasData();
			editarController.subscribeDataChangeListener(this);
			
			Stage editarOrcamentoStage = new Stage();
			editarOrcamentoStage.setTitle("Editar orçamento");
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
		tableColumnEditar.setCellFactory(param -> new TableCell<NotasCompras, NotasCompras>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(NotasCompras obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createEditarNotasComprasForm(obj, Utils.currentStage(event),
						"/guiNotasCompras/EditarNotaCompra.fxml"));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<NotasCompras, NotasCompras>() {
			private final Button button = new Button("Deletar");

			@Override
			protected void updateItem(NotasCompras obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> excluirNotasCompras(obj));
			}
		});
	}
	
	private void excluirNotasCompras(NotasCompras obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("EXCLUIR ORÇAMENTO", "Tem certeza que deseja remover esse orçamento?");
			if(result.get() == ButtonType.OK) {
				if(notasComprasService == null) {
					throw new IllegalStateException("Orcamento vazio");
				}
				try {
					notasComprasService.removerNotaCompra(obj);
					updateTableViewNotasCompras();
				}
				catch(DbException e) {
					Alerts.showAlert("Erro ao excluir orçamento", null, e.getMessage(), AlertType.ERROR);
				}
			}
	}
}
