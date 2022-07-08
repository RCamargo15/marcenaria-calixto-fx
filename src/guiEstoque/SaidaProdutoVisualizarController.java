package guiEstoque;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import marcenaria.entities.Funcionario;
import marcenaria.entities.SaidaProduto;

public class SaidaProdutoVisualizarController implements Initializable, DataChangeListener {

	private SaidaProdutoService saidaProdutoService;
	
	private ProdutoService produtoService;
	
	private FuncionarioService funcionarioService;
	
	private EstoqueService estoqueService;

	@FXML
	private TableView<SaidaProduto> tableViewSaidaProduto;

	@FXML
	private TableColumn<SaidaProduto, Integer> tableColumnCodSaida;
	
	@FXML
	private TableColumn<Estoque, Integer> tableColumnIdEstoque;
	
	@FXML
	private TableColumn<Estoque, Integer> tableColumnProdutoSaidaProduto;

	@FXML
	private TableColumn<SaidaProduto, Date> tableColumnDataSaida;
	
	@FXML
	private TableColumn<SaidaProduto, Integer> tableColumnQuantidade;
	
	@FXML
	private TableColumn<Funcionario, Integer> tableColumnFuncResponsavel;
	
	@FXML
	private TableColumn<SaidaProduto, SaidaProduto> tableColumnEditar;

	@FXML
	private TableColumn<SaidaProduto, SaidaProduto> tableColumnRemover;

	@FXML
	private TextField searchByCod;

	@FXML
	private Button btBuscar;

	@FXML
	private Button mostrarTodos;

	@FXML
	private Button btRetornarAoEstoque;
	

	private ObservableList<SaidaProduto> obsList;
	
	public void setServices(SaidaProdutoService saidaProdutoService, ProdutoService produtoService, FuncionarioService funcionarioService, EstoqueService estoqueService) {
		this.saidaProdutoService = saidaProdutoService;
		this.produtoService = produtoService;
		this.funcionarioService = funcionarioService;
		this.estoqueService = estoqueService;
	}

	@FXML
	public void onBtRetornarAoEstoqueAction() {
		loadEstoqueVisualizar("/guiEstoque/EstoqueVisualizar.fxml", (EstoqueVisualizarController controller) ->{
			controller.setServices(estoqueService, produtoService);
			controller.updateTableViewEstoque();
		});
	}

	@FXML
	public void onBtMostrarTodos() {
		updateTableViewSaidaProduto();
	}

	@FXML
	public void onBtBuscar() {

		if (saidaProdutoService == null) {
			throw new IllegalStateException("Service null");
		}

		SaidaProduto buscaSaidaProduto = saidaProdutoService.findByCodSaida(Utils.tryParseToInt(searchByCod.getText()));

		if (buscaSaidaProduto == null) {
			Alerts.showAlert("Busca de saída de produto", null, "Nenhum registro de saída com esse código foi encontrado no sistema!",
					AlertType.INFORMATION);
		} else {
			obsList = FXCollections.observableArrayList(buscaSaidaProduto);
			tableViewSaidaProduto.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
			searchByCod.setText("");
		}

	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnCodSaida.setCellValueFactory(new PropertyValueFactory<>("codSaida"));
		tableColumnIdEstoque.setCellValueFactory(new PropertyValueFactory<>("idEstoque"));
		tableColumnProdutoSaidaProduto.setCellValueFactory(new PropertyValueFactory<>("codProduto"));
		tableColumnDataSaida.setCellValueFactory(new PropertyValueFactory<>("dataSaida"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tableColumnFuncResponsavel.setCellValueFactory(new PropertyValueFactory<>("respSaida"));

		searchByCod.setPromptText("Insira o c�digo de sa�da");

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSaidaProduto.prefHeightProperty().bind(stage.heightProperty());
		tableViewSaidaProduto.prefWidthProperty().bind(stage.widthProperty());
		
		Utils.formatTableColumnDate(tableColumnDataSaida, "dd/MM/yyyy");

	}

	public void updateTableViewSaidaProduto() {
		if (saidaProdutoService == null) {
			throw new IllegalStateException("Service null");
		}

		List<SaidaProduto> list = saidaProdutoService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSaidaProduto.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	public void createSaidaDeProdutosEstoqueForm(SaidaProduto obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox vBox = loader.load();
			
			EditarSaidaDeProdutoEstoqueController saidaProdutoController = loader.getController();
			saidaProdutoController.setSaidaProduto(obj);
			saidaProdutoController.setServices(new SaidaProdutoService(), estoqueService, funcionarioService);
			saidaProdutoController.subscribeDataListenerChange(this);
			saidaProdutoController.loadEstoque();
			saidaProdutoController.loadFuncionario();
			saidaProdutoController.updateSaidaProdutoData();
			
			Stage saidaProdutoEstoqueStage = new Stage();
			saidaProdutoEstoqueStage.setTitle("Editar reigstro de sa�da de produtos do estoque");
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
		updateTableViewSaidaProduto();
	}

	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<SaidaProduto, SaidaProduto>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(SaidaProduto obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createSaidaDeProdutosEstoqueForm(obj, Utils.currentStage(event),
						"/guiEstoque/EditarSaidaDeProdutoEstoque.fxml"));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<SaidaProduto, SaidaProduto>() {
			private final Button button = new Button("Deletar");

			@Override
			protected void updateItem(SaidaProduto obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> excluirSaidaProduto(obj));
			}
		});
	}

	private void excluirSaidaProduto(SaidaProduto obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("EXCLUIR REGISTRO",
				"Tem certeza que deseja remover esse registro de retirada de produto?");
		if (result.get() == ButtonType.OK) {
			if (saidaProdutoService == null) {
				throw new IllegalStateException("SaidaProduto est� vazio");
			}
			try {
				saidaProdutoService.removerSaida(obj);
				updateTableViewSaidaProduto();
			} catch (DbException e) {
				Alerts.showAlert("Erro ao excluir registro", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
	
	public synchronized <T> void loadEstoqueVisualizar(String absoluteName, Consumer<T> initializeTable){
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
}
