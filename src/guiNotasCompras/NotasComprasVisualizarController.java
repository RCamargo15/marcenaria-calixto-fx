package guiNotasCompras;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Db.DbException;
import application.Main;
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

public class NotasComprasVisualizarController implements Initializable, DataChangeListener {

	private NotasComprasService notasComprasService;
	
	private FornecedorService fornecedorService;
	
	private ProdutoService produtoService;

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
	private TextField searchByCod;

	@FXML
	private Button btBuscar;

	@FXML
	private Button mostrarTodos;

	@FXML
	private Button btNovoNotasCompras;
	

	private ObservableList<NotasCompras> obsList;
	
	public void setServices(NotasComprasService notasComprasService, FornecedorService fornecedorService, ProdutoService produtoService) {
		this.notasComprasService = notasComprasService;
		this.fornecedorService = fornecedorService;
		this.produtoService = produtoService;
	}

	@FXML
	public void onBtNovaNotaCompra(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		NotasCompras obj = new NotasCompras();
		createCadastroNotasComprasForm(obj, parentStage, "/guiNotasCompras/CadastroNotasCompras.fxml");
	}
	
//	@FXML
//	public void onBtSaidaDeProduto(ActionEvent event) {
//		Stage parentStage = Utils.currentStage(event);
//		SaidaProduto obj = new SaidaProduto();
//		createSaidaDeProdutosEstoqueForm(obj, parentStage, "/guiEstoque/SaidaDeProdutoEstoque.fxml");
//	}
//	
//	@FXML
//	public void onBtVisualizarSaidasAction() {
//		loadSaidaProdutoVisualizar("/guiEstoque/SaidaProdutoVisualizar.fxml", (SaidaProdutoVisualizarController controller) ->{
//			controller.setServices(new SaidaProdutoService(), produtoService, new FuncionarioService(), estoqueService);
//			controller.updateTableViewSaidaProduto();
//		});
//	}

	@FXML
	public void onBtMostrarTodos() {
		updateTableViewNotasCompras();
	}

	@FXML
	public void onBtBuscar() {

		if (notasComprasService == null) {
			throw new IllegalStateException("Service null");
		}

		NotasCompras buscaNotasCompras = notasComprasService.findByCodNotasCompras(Utils.tryParseToInt(searchByCod.getText()));

		if (buscaNotasCompras == null) {
			Alerts.showAlert("Busca de estoque", null, "Nenhum estoque com esse código foi encontrado no sistema!",
					AlertType.INFORMATION);
		} else {
			obsList = FXCollections.observableArrayList(buscaNotasCompras);
			tableViewNotasCompras.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
		}

	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
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
		
		searchByCod.setPromptText("Insira o código da nota fiscal");

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewNotasCompras.prefHeightProperty().bind(stage.heightProperty());
		tableViewNotasCompras.prefWidthProperty().bind(stage.widthProperty());

	}

	public void updateTableViewNotasCompras() {
		if (notasComprasService == null) {
			throw new IllegalStateException("Service null");
		}

		List<NotasCompras> list = notasComprasService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewNotasCompras.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createCadastroNotasComprasForm(NotasCompras obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox vBox = loader.load();

			CadastroNotasComprasController cadastroController = loader.getController();
			cadastroController.setNotasCompras(obj);
			cadastroController.setServices(new FornecedorService(), new ProdutoService());
			cadastroController.loadProdutos();
			cadastroController.loadFornecedor();
			cadastroController.subscribeDataChangeListener(this);
			cadastroController.updateNotasComprasData();

			Stage cadastroNotasComprasStage = new Stage();
			cadastroNotasComprasStage.setTitle("Cadastro de notas fiscais");
			cadastroNotasComprasStage.setScene(new Scene(vBox));
			cadastroNotasComprasStage.setResizable(false);
			cadastroNotasComprasStage.initOwner(parentStage);
			cadastroNotasComprasStage.initModality(Modality.WINDOW_MODAL);
			cadastroNotasComprasStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
//	public synchronized <T> void loadSaidaProdutoVisualizar(String absoluteName, Consumer<T> initializeTable) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//			VBox newVBox = loader.load();
//			
//			Scene mainScene = Main.getMainScene();
//			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
//			
//			Node mainMenu = mainVBox.getChildren().get(0);
//			mainVBox.getChildren().clear();
//			mainVBox.getChildren().add(mainMenu);
//			mainVBox.getChildren().addAll(newVBox.getChildren());
//			
//			T controller = loader.getController();
//			initializeTable.accept(controller);
//		}
//		catch(IOException e) {
//			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
//		}
//	}
//	
//	public void createSaidaDeProdutosEstoqueForm(SaidaProduto obj, Stage parentStage, String absoluteName) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//			VBox vBox = loader.load();
//			
//			SaidaDeProdutoEstoqueController saidaProdutoController = loader.getController();
//			saidaProdutoController.setSaidaProduto(obj);
//			saidaProdutoController.setServices(new SaidaProdutoService(), estoqueService, new FuncionarioService());
//			saidaProdutoController.subscribeDataListenerChange(this);
//			saidaProdutoController.loadEstoque();
//			saidaProdutoController.loadFuncionario();
//			saidaProdutoController.updateSaidaProdutoData();
//			
//			Stage saidaProdutoEstoqueStage = new Stage();
//			saidaProdutoEstoqueStage.setTitle("Registro de saída de produtos do estoque");
//			saidaProdutoEstoqueStage.setScene(new Scene(vBox));
//			saidaProdutoEstoqueStage.setResizable(false);
//			saidaProdutoEstoqueStage.initOwner(parentStage);
//			saidaProdutoEstoqueStage.initModality(Modality.WINDOW_MODAL);
//			saidaProdutoEstoqueStage.showAndWait();
//		}
//		catch(IOException e) {
//			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
//		}
//	}

	@Override
	public void onDataChanged() {
		updateTableViewNotasCompras();
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
				button.setOnAction(event -> createCadastroNotasComprasForm(obj, Utils.currentStage(event),
						"/guiNotasCompras/CadastroNotasCompras.fxml"));
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
		Optional<ButtonType> result = Alerts.showConfirmation("EXCLUIR PRODUTO",
				"Tem certeza que deseja remover esse estoque?");
		if (result.get() == ButtonType.OK) {
			if (notasComprasService == null) {
				throw new IllegalStateException("Estoque está vazio");
			}
			try {
				notasComprasService.removerNotasCompras(obj);
				updateTableViewNotasCompras();
			} catch (DbException e) {
				Alerts.showAlert("Erro ao excluir estoque", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
