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
import entities.services.EntradaProdutoService;
import entities.services.EstoqueService;
import entities.services.FornecedorService;
import entities.services.FuncionarioService;
import entities.services.NotasComprasService;
import entities.services.OrcamentoClienteService;
import entities.services.OrcamentoEmpresaService;
import entities.services.OrdemServicoClienteService;
import entities.services.OrdemServicoEmpresaService;
import entities.services.ProdutoService;
import entities.services.SaidaProdutoService;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import guiCliente.CadastroClienteTelaPrincipalController;
import guiCliente.ClienteVisualizarController;
import guiEmpresa.CadastroEmpresaTelaPrincipalController;
import guiEmpresa.EmpresaVisualizarController;
import guiEstoque.EstoqueVisualizarController;
import guiEstoque.SaidaProdutoVisualizarController;
import guiFornecedor.CadastroFornecedorTelaPrincipalController;
import guiFornecedor.FornecedorVisualizarController;
import guiFuncionarios.CadastroFuncionarioTelaPrincipalController;
import guiFuncionarios.FuncionarioVisualizarController;
import guiNotasCompras.EntradaProdutoVisualizarController;
import guiNotasCompras.NotaCompraVisualizarController;
import guiOrcamentoCliente.OrcamentoClienteVisualizarController;
import guiOrcamentoEmpresa.OrcamentoEmpresaVisualizarController;
import guiOrdemDeServicoCliente.OrdemServicoClienteVisualizarController;
import guiOrdemDeServicoEmpresa.OrdemServicoEmpresaVisualizarController;
import guiProduto.ProdutoVisualizarController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import marcenaria.entities.Cliente;
import marcenaria.entities.Empresa;
import marcenaria.entities.Fornecedor;
import marcenaria.entities.Funcionario;

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
	private MenuItem menuItemNotaCompraVisualizar;

	@FXML
	private MenuItem menuItemEntradaProdutoVisualizar;

	@FXML
	private MenuItem menuItemEstoqueVisualizar;

	@FXML
	private MenuItem menuItemEstoqueSaidaProduto;

	@FXML
	private MenuItem menuItemProdutoVisualizar;

	@FXML
	private MenuItem menuItemOrdemServicoEmpresas;

	@FXML
	private MenuItem menuItemOrdemServicoCliente;
	
	@FXML
	private MenuItem menuItemOrcamentoPessoaFisica;
	
	@FXML
	private MenuItem menuItemOrcamentoEmpresa;

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
		Fornecedor obj = new Fornecedor();
		loadFornecedorCadastro(obj, "/guiFornecedor/CadastroFornecedorTelaPrincipal.fxml");
	}

	@FXML
	public void onMenuItemFornecedorVisualizarAction() {
		loadFornecedorVisualizar("/guiFornecedor/FornecedorVisualizar.fxml",
				(FornecedorVisualizarController controller) -> {
					controller.setFornecedorService(new FornecedorService());
					controller.updateTableViewFornecedor();
				});
	}

	@FXML
	public void onMenuItemFuncionarioCadastrarAction() {
		Funcionario obj = new Funcionario();
		loadFuncionarioCadastro(obj, "/guiFuncionarios/CadastroFuncionarioTelaPrincipal.fxml");
	}

	@FXML
	public void onMenuItemFuncionarioVisualizarAction() {
		loadFuncionarioVisualizar("/guiFuncionarios/FuncionarioVisualizar.fxml",
				(FuncionarioVisualizarController controller) -> {
					controller.setFuncionarioService(new FuncionarioService());
					controller.updateTableViewFuncionario();
				});
	}

	@FXML
	public void onMenuItemNotaCompraVisualizarrAction() {
		loadNotasComprasVisualizar("/guiNotasCompras/NotaCompraVisualizar.fxml",
				(NotaCompraVisualizarController controller) -> {
					controller.setServices(new NotasComprasService(), new FornecedorService(), new ProdutoService(), new EntradaProdutoService(), new EstoqueService());
					controller.updateTableViewNotasCompras();
				});
	}

	@FXML
	public void onMenuItemEntradaProdutoVisualizarAction() {
		loadEntradaProdutoVisualizar("/guiNotasCompras/EntradaProdutoVisualizar.fxml",(EntradaProdutoVisualizarController controller) ->{
			controller.setServices(new EntradaProdutoService());
			controller.updateTableViewEntradaProduto();
		});
	}

	@FXML
	public void onMenuItemEstoqueVisualizarAction() {
		loadEstoqueVisualizar("/guiEstoque/EstoqueVisualizar.fxml", (EstoqueVisualizarController controller) -> {
			controller.setServices(new EstoqueService(), new ProdutoService());
			controller.updateTableViewEstoque();
		});
	}

	@FXML
	public void onMenuItemEstoqueSaidaProdutoAction() {
		loadSaidaProdutoVisualizar("/guiEstoque/SaidaProdutoVisualizar.fxml",
				(SaidaProdutoVisualizarController controller) -> {
					controller.setServices(new SaidaProdutoService(), new ProdutoService(), new FuncionarioService(),
							new EstoqueService());
					controller.updateTableViewSaidaProduto();
				});
	}

	@FXML
	public void onMenuItemProdutoVisualizarAction() {
		loadProdutoVisualizar("/guiProduto/ProdutoVisualizar.fxml", (ProdutoVisualizarController controller) -> {
			controller.setProdutoService(new ProdutoService());
			controller.updateTableViewProduto();
		});

	}

	@FXML
	public void onMenuItemOrdemServicoCliente() {
		loadOrdemServicoClienteVisualizar("/guiOrdemDeServicoCliente/OrdemServicoClienteVisualizar.fxml", (OrdemServicoClienteVisualizarController controller) ->{
			controller.setServices(new OrdemServicoClienteService(), new FuncionarioService(), new ClienteService());
			controller.updateTableViewOrdemClienteVisualizar();
		});
	}
	
	@FXML
	public void onMenuItemOrdemServicoEmpresas() {
		loadOrdemServicoEmpresaVisualizar("/guiOrdemDeServicoEmpresa/OrdemServicoEmpresaVisualizar.fxml", (OrdemServicoEmpresaVisualizarController controller) ->{
			controller.setServices(new OrdemServicoEmpresaService(), new FuncionarioService(), new EmpresaService());
			controller.updateTableViewOrdemEmpresaVisualizar();
		});
	}
	
	@FXML
	public void onMenuItemOrcamentoPessoaFisica() {
		loadOrcamentoClienteVisualizar("/guiOrcamentoCliente/OrcamentoClienteVisualizar.fxml", (OrcamentoClienteVisualizarController controller) ->{
    		controller.setServices(new OrcamentoClienteService(), new ClienteService(), new ProdutoService());
    		controller.updateTableViewOrcamentoCliente();
    	});
	}
	
	@FXML
	public void onMenuItemOrcamentoEmpresa() {
		loadOrcamentoEmpresaVisualizar("/guiOrcamentoEmpresa/OrcamentoEmpresaVisualizar.fxml", (OrcamentoEmpresaVisualizarController controller) ->{
    		controller.SetServices(new OrcamentoEmpresaService(), new EmpresaService(), new ProdutoService());
    		controller.updateTableViewOrcamentoEmpresa();
    	});
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
   // TODO Tela inicial sem metodos para inicio
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
			e.printStackTrace();
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

	public synchronized <T> void loadFornecedorVisualizar(String absoluteName, Consumer<T> initializeTable) {
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

	public synchronized void loadFornecedorCadastro(Fornecedor obj, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			CadastroFornecedorTelaPrincipalController controller = loader.getController();
			controller.setFornecedor(obj);
			controller.setFornecedorService(new FornecedorService());
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

	public synchronized void loadFuncionarioCadastro(Funcionario obj, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			CadastroFuncionarioTelaPrincipalController controller = loader.getController();
			controller.setFuncionario(obj);
			controller.setFuncionarioService(new FuncionarioService());
			controller.subscribeDataChangeListener(this);

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());

			Stage stage = (Stage) Main.getMainScene().getWindow();
			mainVBox.prefHeightProperty().bind(stage.heightProperty());
			mainVBox.prefWidthProperty().bind(stage.widthProperty());

		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	public synchronized <T> void loadFuncionarioVisualizar(String absoluteName, Consumer<T> initializeTable) {
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

	public synchronized <T> void loadProdutoVisualizar(String absoluteName, Consumer<T> initializeTable) {
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

	public synchronized <T> void loadEstoqueVisualizar(String absoluteName, Consumer<T> initializeTable) {
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
			e.printStackTrace();
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
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

		} catch (IOException e) {
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}

	public synchronized <T> void loadNotasComprasVisualizar(String absoluteName, Consumer<T> initializeTable) {
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
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	public synchronized <T> void loadOrcamentoClienteVisualizar(String absoluteName, Consumer<T> initializeTable) {
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
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	public synchronized <T> void loadOrcamentoEmpresaVisualizar(String absoluteName, Consumer<T> initializeTable) {
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
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	public synchronized <T> void loadOrdemServicoClienteVisualizar(String absoluteName, Consumer<T> initializeTable) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox)((ScrollPane) mainScene.getRoot()).getContent();
			
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
	
	public synchronized <T> void loadOrdemServicoEmpresaVisualizar(String absoluteName, Consumer <T> initializeTable) {
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
	
	public synchronized <T> void loadEntradaProdutoVisualizar(String absoluteName, Consumer<T> initializeTable) {
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

	@Override
	public void onDataChanged() {
		for (DataChangeListener listener : dataChangeListener) {
			dataChangeListener.add(listener);
		}

	}

}
