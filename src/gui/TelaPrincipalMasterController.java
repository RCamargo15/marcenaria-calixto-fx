package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import entities.services.ClienteService;
import entities.services.EmpresaService;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import guiCliente.CadastroClienteTelaPrincipalController;
import guiCliente.ClienteVisualizarController;
import guiEmpresa.CadastroEmpresaTelaPrincipalController;
import guiEmpresa.EmpresaVisualizarController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import marcenaria.entities.Cliente;
import marcenaria.entities.Empresa;

public class TelaPrincipalMasterController implements Initializable, DataChangeListener {

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

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
	private MenuItem menuItemSaidaProdutoVisualizar;

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
		Cliente obj = new Cliente();
		loadCadastroCliente("/guiCliente/CadastroClienteTelaPrincipal.fxml", obj);
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
		Empresa empresa = new Empresa();
		loadCadastroEmpresa("/guiEmpresa/CadastroEmpresaTelaPrincipal.fxml", empresa);
	}

	@FXML
	public void onMenuItemEmpresaVisualizarAction() {
		loadEmpresaVisualizar("/guiEmpresa/EmpresaVisualizar.fxml", (EmpresaVisualizarController controller) -> {
			controller.setEmpresaService(new EmpresaService());
			controller.updateTableViewEmpresa();
		});
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
	public void onMenuItemSaidaProdutoVisualizarAction() {

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

	private synchronized void loadCadastroCliente(String absoluteName, Cliente obj) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			CadastroClienteTelaPrincipalController controller = loader.getController();

			controller.setCliente(obj);
			controller.setClienteService(new ClienteService());
			controller.subscribeDataChangeListener(this);

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

	private synchronized <T> void loadClienteVisualizar(String absoluteName, Consumer<T> initializeTable) {
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

		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	public synchronized <T> void loadEmpresaVisualizar(String absoluteName, Consumer<T> initializeTable) {
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

		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}

	}

	public synchronized void loadCadastroEmpresa(String absoluteName, Empresa empresa) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			CadastroEmpresaTelaPrincipalController controller = loader.getController();

			controller.setEmpresa(empresa);
			controller.setEmpresaService(new EmpresaService());
			controller.subscribeDataChangeListener(this);

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

	@Override
	public void onDataChanged() {
		for (DataChangeListener listener : dataChangeListener) {
			dataChangeListener.add(listener);
		}

	}

}
