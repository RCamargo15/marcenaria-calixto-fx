package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import guiCliente.ClienteVisualizarController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.services.ClienteService;

public class TelaPrincipalMasterController implements Initializable {

	@FXML
	private MenuItem menuItemClienteCadastro;

	@FXML
	private MenuItem menuItemClienteVisualizar;

	@FXML
	private MenuItem menuItemEmpresaCadastrar;

	@FXML
	private MenuItem menuItemEmpresaVisualizar;

	@FXML
	private MenuItem menuItemFornecedorCadastro;

	@FXML
	private MenuItem menuItemFornecedorVisualizar;

	@FXML
	private MenuItem menuItemFuncionarioCadastrar;

	@FXML
	private MenuItem menuItemFuncionarioVisualizar;

	@FXML
	private MenuItem menuItemNotaCompraCadastro;

	@FXML
	private MenuItem menuItemNotaCompraVisualizar;

	@FXML
	private MenuItem menuItemEntradaProdutoCadastrar;

	@FXML
	private MenuItem menuItemEntradaProdutoVisualizar;

	@FXML
	private MenuItem menuItemEstoqueProdutoCadastrar;

	@FXML
	private MenuItem menuItemEstoqueVisualizar;

	@FXML
	private MenuItem menuItemEstoqueSaidaProduto;

	@FXML
	private MenuItem menuItemProdutoCadastrar;

	@FXML
	private MenuItem menuItemProdutoVisualizar;

	@FXML
	private MenuItem menuItemOrdemServicoClienteCadastrar;

	@FXML
	private MenuItem menuItemOrdemServicoClienteVisualizar;

	@FXML
	private MenuItem menuItemOrdemServicoEmpresaCadastrar;

	@FXML
	private MenuItem menuItemOrdemServicoEmpresaVisualizar;

	@FXML
	private MenuItem menuItemOrcamentoCadastrar;

	@FXML
	private MenuItem menuItemOrcamentoVisualizar;

	@FXML
	private MenuItem menuItemUsuarioCadastrar;

	@FXML
	private MenuItem menuItemUsuarioVisualizar;

	@FXML
	public void onMenuItemClienteCadastroAction() {
		loadCadastroCliente("/guiCliente/CadastroClienteTelaPrincipal.fxml");
	}

	@FXML
	public void onMenuItemClienteVisualizarAction() {
		loadClienteVisualizar("/guiCliente/ClienteVisualizar.fxml", (ClienteVisualizarController controller) -> {
			controller.setClienteService(new ClienteService());
			controller.updateTableViewCliente();
		});
	}

	@FXML
	public void onMenuItemEmpresaCadastrarAction() {

	}

	@FXML
	public void onMenuItemEmpresaVisualizarAction() {

	}

	@FXML
	public void onMenuItemFornecedorCadastrarAction() {

	}

	@FXML
	public void onMenuItemFornecedorVisualizarAction() {

	}

	@FXML
	public void onMenuItemFuncionarioCadastrarAction() {

	}

	@FXML
	public void onMenuItemFuncionarioVisualizarAction() {

	}

	@FXML
	public void onMenuItemNotaCompraCadastrarAction() {

	}

	@FXML
	public void onMenuItemNotaCompraVisualizarrAction() {

	}

	@FXML
	public void onMenuItemEntradaProdutoCadastroAction() {

	}

	@FXML
	public void onMenuItemEntradaProdutoVisualizarAction() {

	}

	@FXML
	public void onMenuItemEstoqueCadastrarAction() {

	}

	@FXML
	public void onMenuItemEstoqueVisualizarAction() {

	}

	@FXML
	public void onMenuItemEstoqueSaidaProdutoAction() {

	}

	@FXML
	public void onMenuItemProdutoCadastrarAction() {

	}

	@FXML
	public void onMenuItemProdutoVisualizarAction() {

	}

	@FXML
	public void onMenuItemOrdemServicoClienteGerarAction() {

	}

	@FXML
	public void onMenuItemOrdemServicoClienteExcluirAction() {

	}

	@FXML
	public void onMenuItemOrdemServicoClienteVisualizarAction() {

	}

	@FXML
	public void onMenuItemOrdemServicoEmpresaGerarAction() {

	}

	@FXML
	public void onMenuItemOrdemServicoEmpresaVisualizarAction() {

	}

	@FXML
	public void onMenuItemOrcamentoGerarAction() {

	}

	@FXML
	public void onMenuItemOrcamentoVisualizarAction() {

	}

	@FXML
	public void onMenuItemUsuarioCadastrarAction() {

	}

	@FXML
	public void onMenuItemUsuarioVisualizarAction() {

	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {

	}

	private synchronized void loadCadastroCliente(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());

		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	private synchronized <T> void loadClienteVisualizar(String absoluteName, Consumer<T> initializaTable) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainManu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainManu);
			mainVBox.getChildren().addAll(newVBox.getChildren());

			T controller = loader.getController();
			initializaTable.accept(controller);

		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
