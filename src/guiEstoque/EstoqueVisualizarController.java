package guiEstoque;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import Db.DbException;
import application.Main;
import entities.services.EstoqueService;
import entities.services.FuncionarioService;
import entities.services.ProdutoService;
import entities.services.SaidaProdutoService;
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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import marcenaria.entities.Estoque;
import marcenaria.entities.Produto;
import marcenaria.entities.SaidaProduto;

public class EstoqueVisualizarController implements Initializable, DataChangeListener {

	private EstoqueService estoqueService;
	
	private ProdutoService produtoService;

	@FXML
	private TableView<Estoque> tableViewEstoque;

	@FXML
	private TableColumn<Estoque, Integer> tableColumnCodEstoque;

	@FXML
	private TableColumn<Produto, Integer> tableColumnProduto;

	@FXML
	private TableColumn<Estoque, Double> tableColumnEstoqueAtual;
	
	@FXML
	private TableColumn<Estoque, Double> tableColumnEstoqueMinimo;
	
	@FXML
	private TableColumn<Estoque, Estoque> tableColumnEditar;

	@FXML
	private TableColumn<Estoque, Estoque> tableColumnRemover;

	@FXML
	private TextField searchByCod;

	@FXML
	private Button btBuscar;

	@FXML
	private Button mostrarTodos;
	
	@FXML 
	private Button btSaidaDeProdutos;
	
	@FXML 
	private Button btVisualizarSaidas;

	private ObservableList<Estoque> obsList;
	
	public void setServices(EstoqueService estoqueService, ProdutoService produtoService) {
		this.estoqueService = estoqueService;
		this.produtoService = produtoService;
	}
	
	@FXML
	public void onBtSaidaDeProduto(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		SaidaProduto obj = new SaidaProduto();
		createSaidaDeProdutosEstoqueForm(obj, parentStage, "/guiEstoque/SaidaDeProdutoEstoque.fxml");
	}
	
	@FXML
	public void onBtVisualizarSaidasAction() {
		loadSaidaProdutoVisualizar("/guiEstoque/SaidaProdutoVisualizar.fxml", (SaidaProdutoVisualizarController controller) ->{
			controller.setServices(new SaidaProdutoService(), produtoService, new FuncionarioService(), estoqueService);
			controller.updateTableViewSaidaProduto();
		});
	}

	@FXML
	public void onBtMostrarTodos() {
		updateTableViewEstoque();
	}

	@FXML
	public void onBtBuscar() {

		if (estoqueService == null) {
			throw new IllegalStateException("Service null");
		}

		Estoque buscaEstoque = estoqueService.findByCodEstoque(Utils.tryParseToInt(searchByCod.getText()));

		if (buscaEstoque == null) {
			Alerts.showAlert("Busca de estoque", null, "Nenhum estoque com esse código foi encontrado no sistema!",
					AlertType.INFORMATION);
		} else {
			obsList = FXCollections.observableArrayList(buscaEstoque);
			tableViewEstoque.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
		}

	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnCodEstoque.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnProduto.setCellValueFactory(new PropertyValueFactory<>("codProduto"));
		tableColumnEstoqueAtual.setCellValueFactory(new PropertyValueFactory<>("estoqueAtual"));
		tableColumnEstoqueMinimo.setCellValueFactory(new PropertyValueFactory<>("estoqueMinimo"));

		searchByCod.setPromptText("Insira o código do estoque");

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewEstoque.prefHeightProperty().bind(stage.heightProperty());
		tableViewEstoque.prefWidthProperty().bind(stage.widthProperty());

	}

	public void updateTableViewEstoque() {
		if (estoqueService == null) {
			throw new IllegalStateException("Service null");
		}

		List<Estoque> list = estoqueService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewEstoque.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createCadastroEstoqueForm(Estoque obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox vBox = loader.load();

			CadastroEstoqueController cadastroController = loader.getController();
			cadastroController.setEstoque(obj);
			cadastroController.setServices(new EstoqueService(), new ProdutoService());
			cadastroController.loadProdutos();
			cadastroController.subscribeDataChangeListener(this);
			cadastroController.updateEstoqueData();

			Stage cadastroEstoqueStage = new Stage();
			cadastroEstoqueStage.setTitle("Cadastro de estoque");
			cadastroEstoqueStage.setScene(new Scene(vBox));
			cadastroEstoqueStage.setResizable(false);
			cadastroEstoqueStage.initOwner(parentStage);
			cadastroEstoqueStage.initModality(Modality.WINDOW_MODAL);
			cadastroEstoqueStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	public synchronized <T> void loadSaidaProdutoVisualizar(String absoluteName, Consumer<T> initializeTable) {
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
		}
		catch(IOException e) {
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	public void createSaidaDeProdutosEstoqueForm(SaidaProduto obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox vBox = loader.load();
			
			SaidaDeProdutoEstoqueController saidaProdutoController = loader.getController();
			saidaProdutoController.setSaidaProduto(obj);
			saidaProdutoController.setServices(new SaidaProdutoService(), estoqueService, new FuncionarioService());
			saidaProdutoController.subscribeDataListenerChange(this);
			saidaProdutoController.loadEstoque();
			saidaProdutoController.loadFuncionario();
			saidaProdutoController.updateSaidaProdutoData();
			
			Stage saidaProdutoEstoqueStage = new Stage();
			saidaProdutoEstoqueStage.setTitle("Registro de saída de produtos do estoque");
			saidaProdutoEstoqueStage.setScene(new Scene(vBox));
			saidaProdutoEstoqueStage.setResizable(false);
			saidaProdutoEstoqueStage.initOwner(parentStage);
			saidaProdutoEstoqueStage.initModality(Modality.WINDOW_MODAL);
			saidaProdutoEstoqueStage.showAndWait();
		}
		catch(IOException e) {
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableViewEstoque();
	}

	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<Estoque, Estoque>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Estoque obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createCadastroEstoqueForm(obj, Utils.currentStage(event),
						"/guiEstoque/CadastroEstoque.fxml"));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<Estoque, Estoque>() {
			private final Button button = new Button("Deletar");

			@Override
			protected void updateItem(Estoque obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> excluirEstoque(obj));
			}
		});
	}

	private void excluirEstoque(Estoque obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("EXCLUIR PRODUTO",
				"Tem certeza que deseja remover esse estoque?");
		if (result.get() == ButtonType.OK) {
			if (estoqueService == null) {
				throw new IllegalStateException("Estoque está vazio");
			}
			try {
				estoqueService.removerEstoque(obj);
				updateTableViewEstoque();
			} catch (DbException e) {
				Alerts.showAlert("Erro ao excluir estoque", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
