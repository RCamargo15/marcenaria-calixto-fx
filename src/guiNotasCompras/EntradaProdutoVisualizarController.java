package guiNotasCompras;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import entities.services.EntradaProdutoService;
import entities.services.EstoqueService;
import entities.services.FornecedorService;
import entities.services.NotasComprasService;
import entities.services.ProdutoService;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import marcenaria.entities.EntradaProduto;

public class EntradaProdutoVisualizarController implements Initializable, DataChangeListener {

	private EntradaProdutoService entradaProdutoService;

	@FXML
	private TableView<EntradaProduto> tableViewEntradaProduto;

	@FXML
	private TableColumn<EntradaProduto, Integer> tableColumnCodEntrada;
	
	@FXML
	private TableColumn<EntradaProduto, String> tableColumnNumeroNF;
	
	@FXML
	private TableColumn<EntradaProduto, Integer> tableColumnProduto;

	@FXML
	private TableColumn<EntradaProduto, Date> tableColumnDataEntrada;
	
	@FXML
	private TableColumn<EntradaProduto, Integer> tableColumnQuantidade;
	
	@FXML
	private TextField searchByCod;

	@FXML
	private Button btBuscar;

	@FXML
	private Button mostrarTodos;

	@FXML
	private Button btRetornarAsNotasFiscais;
	

	private ObservableList<EntradaProduto> obsList;
	
	public void setServices(EntradaProdutoService entradaProdutoService) {
		this.entradaProdutoService = entradaProdutoService;
	}

	@FXML
	public void onBtRetornarAsNotasFiscaisAction() {
		loadNotasFiscaisVisualizar("/guiNotasCompras/NotaCompraVisualizar.fxml", (NotaCompraVisualizarController controller) ->{
			controller.setServices(new NotasComprasService(), new FornecedorService(), new ProdutoService(), new EntradaProdutoService(), new EstoqueService());
			controller.updateTableViewNotasCompras();
		});
	}

	@FXML
	public void onBtMostrarTodos() {
		updateTableViewEntradaProduto();
	}

	@FXML
	public void onBtBuscar() {

		if (entradaProdutoService == null) {
			throw new IllegalStateException("Service null");
		}

		EntradaProduto buscaEntradaProduto = entradaProdutoService.findByCodEntrada(Utils.tryParseToInt(searchByCod.getText()));

		if (buscaEntradaProduto == null) {
			Alerts.showAlert("Busca de saidaProduto", null, "Nenhum saidaProduto com esse c�digo foi encontrado no sistema!",
					AlertType.INFORMATION);
		} else {
			obsList = FXCollections.observableArrayList(buscaEntradaProduto);
			tableViewEntradaProduto.setItems(obsList);
			searchByCod.setText("");
		}

	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnCodEntrada.setCellValueFactory(new PropertyValueFactory<>("codEntrada"));
		tableColumnProduto.setCellValueFactory(new PropertyValueFactory<>("codProduto"));
		tableColumnNumeroNF.setCellValueFactory(new PropertyValueFactory<>("numeroNF"));
		tableColumnDataEntrada.setCellValueFactory(new PropertyValueFactory<>("dataEntrada"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

		searchByCod.setPromptText("Insira o c�digo de sa�da");

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewEntradaProduto.prefHeightProperty().bind(stage.heightProperty());
		tableViewEntradaProduto.prefWidthProperty().bind(stage.widthProperty());
		
		Utils.formatTableColumnDate(tableColumnDataEntrada, "dd/MM/yyyy");

	}

	public void updateTableViewEntradaProduto() {
		if (entradaProdutoService == null) {
			throw new IllegalStateException("Service null");
		}

		List<EntradaProduto> list = entradaProdutoService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewEntradaProduto.setItems(obsList);
	}

	
	@Override
	public void onDataChanged() {
		updateTableViewEntradaProduto();
	}

	public synchronized <T> void loadNotasFiscaisVisualizar(String absoluteName, Consumer<T> initializeTable){
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