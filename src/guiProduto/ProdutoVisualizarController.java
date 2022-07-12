package guiProduto;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Db.DbException;
import application.Main;
import entities.services.EstoqueService;
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
import marcenaria.entities.Produto;

public class ProdutoVisualizarController implements Initializable, DataChangeListener {

	private ProdutoService produtoService;

	@FXML
	private TableView<Produto> tableViewProduto;

	@FXML
	private TableColumn<Produto, Integer> tableColumnCodProduto;

	@FXML
	private TableColumn<Produto, String> tableColumnDescProduto;

	@FXML
	private TableColumn<Produto, Double> tableColumnPrecoUnit;
	
	@FXML
	private TableColumn<Produto, Produto> tableColumnEditar;

	@FXML
	private TableColumn<Produto, Produto> tableColumnRemover;

	@FXML
	private TextField searchByCod;

	@FXML
	private Button btBuscar;

	@FXML
	private Button mostrarTodos;

	@FXML
	private Button btNovoProduto;

	private ObservableList<Produto> obsList;
	
	public void setProdutoService(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}

	@FXML
	public void onBtNovoProduto(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Produto obj = new Produto();
		createCadastroProdutoForm(obj, parentStage, "/guiProduto/CadastroProduto.fxml");
	}

	@FXML
	public void onBtMostrarTodos() {
		searchByCod.setText("");
		updateTableViewProduto();
	}

	@FXML
	public void onBtBuscar() {

		if (produtoService == null) {
			throw new IllegalStateException("Service null");
		}
		List<Produto> buscaProduto = new ArrayList<>();
		if(searchByCod.getText().equals("") || searchByCod.getText() == null) {
			buscaProduto = Collections.emptyList();
		}
		else {
			buscaProduto = produtoService.findByNomeProduto(searchByCod.getText());
		}
		
		if (buscaProduto.isEmpty()) {
			Alerts.showAlert("Busca de produto", null, "Nenhum produto foi encontrado no sistema!",
					AlertType.INFORMATION);
		} else {
			obsList = FXCollections.observableArrayList(buscaProduto);
			tableViewProduto.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
		}
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnCodProduto.setCellValueFactory(new PropertyValueFactory<>("codProduto"));
		tableColumnDescProduto.setCellValueFactory(new PropertyValueFactory<>("descProduto"));
		tableColumnPrecoUnit.setCellValueFactory(new PropertyValueFactory<>("precoUnit"));

		searchByCod.setPromptText("Insira o código do produto");

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewProduto.prefHeightProperty().bind(stage.heightProperty());
		tableViewProduto.prefWidthProperty().bind(stage.widthProperty());
		
		Utils.formatTableColumnDouble(tableColumnPrecoUnit, 2);

	}

	public void updateTableViewProduto() {
		if (produtoService == null) {
			throw new IllegalStateException("Service null");
		}

		List<Produto> list = produtoService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewProduto.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createCadastroProdutoForm(Produto obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox vBox = loader.load();

			CadastroProdutoController cadastroController = loader.getController();
			cadastroController.setProduto(obj);
			cadastroController.setProdutoServices(new ProdutoService(), new EstoqueService());
			cadastroController.subscribeDataChangeListener(this);
			cadastroController.updateProdutoData();

			Stage cadastroProdutoStage = new Stage();
			cadastroProdutoStage.setTitle("Cadastro de produto");
			cadastroProdutoStage.setScene(new Scene(vBox));
			cadastroProdutoStage.setResizable(false);
			cadastroProdutoStage.initOwner(parentStage);
			cadastroProdutoStage.initModality(Modality.WINDOW_MODAL);
			cadastroProdutoStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void createEditarProdutoForm(Produto obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox vBox = loader.load();

			EditarProdutoController cadastroController = loader.getController();
			cadastroController.setProduto(obj);
			cadastroController.setProdutoService(new ProdutoService());
			cadastroController.subscribeDataChangeListener(this);
			cadastroController.updateProdutoData();

			Stage cadastroProdutoStage = new Stage();
			cadastroProdutoStage.setTitle("Editar produto");
			cadastroProdutoStage.setScene(new Scene(vBox));
			cadastroProdutoStage.setResizable(false);
			cadastroProdutoStage.initOwner(parentStage);
			cadastroProdutoStage.initModality(Modality.WINDOW_MODAL);
			cadastroProdutoStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableViewProduto();
	}

	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<Produto, Produto>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Produto obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createEditarProdutoForm(obj, Utils.currentStage(event),
						"/guiProduto/EditarProduto.fxml"));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<Produto, Produto>() {
			private final Button button = new Button("Deletar");

			@Override
			protected void updateItem(Produto obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> excluirProduto(obj));
			}
		});
	}

	private void excluirProduto(Produto obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("EXCLUIR PRODUTO",
				"Tem certeza que deseja remover esse produto?");
		if (result.get() == ButtonType.OK) {
			if (produtoService == null) {
				throw new IllegalStateException("Produto está vazio");
			}
			try {
				produtoService.removerProduto(obj);
				updateTableViewProduto();
			} catch (DbException e) {
				Alerts.showAlert("Erro ao excluir produto", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
