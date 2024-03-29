package guiOrcamentoCliente;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import Db.DbException;
import application.Main;
import entities.services.ClienteService;
import entities.services.FuncionarioService;
import entities.services.OrcamentoClienteService;
import entities.services.OrdemServicoClienteService;
import entities.services.ProdutoService;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import guiOrdemDeServicoCliente.GerarOrdemDeServicoClienteController;
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
import marcenaria.entities.Cliente;
import marcenaria.entities.OrcamentoCliente;
import marcenaria.entities.OrdemServicoCliente;

public class OrcamentoClienteVisualizarController implements Initializable, DataChangeListener {

	private OrcamentoClienteService orcamentoClienteService;
	private ClienteService clienteService;
	private ProdutoService produtoService;
	private OrdemServicoClienteService ordemServicoClienteService;

	@FXML
	private TableView<OrcamentoCliente> tableViewOrcamentoCliente;

	@FXML
	private TableColumn<OrcamentoCliente, Integer> tableColumnId;

	@FXML
	private TableColumn<OrcamentoCliente, Integer> tableColumnNumOrcamento;

	@FXML
	private TableColumn<Cliente, Integer> tableColumnCodCliente;

	@FXML
	private TableColumn<Cliente, String> tableColumnTelefone;

	@FXML
	private TableColumn<Cliente, String> tableColumnCelular;

	@FXML
	private TableColumn<Cliente, String> tableColumnEmail;

	@FXML
	private TableColumn<OrcamentoCliente, String> tableColumnDescServico;

	@FXML
	private TableColumn<OrcamentoCliente, Date> tableColumnDataOrcamento;

	@FXML
	private TableColumn<OrcamentoCliente, Double> tableColumnValorTotal;

	@FXML
	private TableColumn<OrcamentoCliente, String> tableColumnObs;

	@FXML
	private TableColumn<OrcamentoCliente, OrcamentoCliente> tableColumnEditar;

	@FXML
	private TableColumn<OrcamentoCliente, OrcamentoCliente> tableColumnGerarOS;

	@FXML
	private TableColumn<OrcamentoCliente, OrcamentoCliente> tableColumnRemover;

	@FXML
	private TableColumn<OrcamentoCliente, OrcamentoCliente> tableColumnOrcamentoEmPDF;

	@FXML
	private TextField searchByCod;

	@FXML
	private Button btBuscar;

	@FXML
	private Button mostrarTodos;

	@FXML
	private Button btNovoOrcamentoCliente;

	private ObservableList<OrcamentoCliente> obsList;

	public void setServices(OrcamentoClienteService orcamentoClienteService, ClienteService clienteService,
			ProdutoService produtoService, OrdemServicoClienteService ordemServicoClienteService) {
		this.orcamentoClienteService = orcamentoClienteService;
		this.clienteService = clienteService;
		this.produtoService = produtoService;
		this.ordemServicoClienteService = ordemServicoClienteService;
	}

	@FXML
	public void onBtNovoOrcamentoCliente(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		OrcamentoCliente obj = new OrcamentoCliente();
		createCadastroOrcamentoClienteForm(obj, parentStage, "/guiOrcamentoCliente/GerarNovoOrcamentoCliente.fxml");

	}

	@FXML
	public void onBtMostrarTodos() {
		updateTableViewOrcamentoCliente();
	}

	@FXML
	public void onBtBuscar() {
		if (orcamentoClienteService == null) {
			throw new IllegalStateException("Orcamento Cliente null");
		}

		OrcamentoCliente buscaOrcamentoCliente = orcamentoClienteService
				.findByNumOrcamento(Integer.parseInt(searchByCod.getText()));

		if (buscaOrcamentoCliente == null) {
			Alerts.showAlert("Busca de orçamentos", null, "Nenhum orçamento encontrado no sistema", AlertType.ERROR);
		} else {
			obsList = FXCollections.observableArrayList(buscaOrcamentoCliente);
			tableViewOrcamentoCliente.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
			initGerarOrcamentoButtons();
		}
	}

	public void updateTableViewOrcamentoCliente() {
		if (orcamentoClienteService == null) {
			throw new IllegalStateException("Orcamento null");
		}

		List<OrcamentoCliente> list = orcamentoClienteService.findAllParaTabela();
		obsList = FXCollections.observableArrayList(list);
		tableViewOrcamentoCliente.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		initGerarOrcamentoButtons();
	}

	@Override
	public void onDataChanged() {
		updateTableViewOrcamentoCliente();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
		initEditButtons();
		initRemoveButtons();
		initGerarOrcamentoButtons();
		initOrcamentoPDFButtons();
	}

	private void initializeNodes() {
		tableColumnNumOrcamento.setCellValueFactory(new PropertyValueFactory<>("numOrcamento"));
		tableColumnCodCliente.setCellValueFactory(new PropertyValueFactory<>("codCliente"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		tableColumnCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnDescServico.setCellValueFactory(new PropertyValueFactory<>("descServico"));
		tableColumnDataOrcamento.setCellValueFactory(new PropertyValueFactory<>("dataOrcamento"));
		tableColumnValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
		tableColumnObs.setCellValueFactory(new PropertyValueFactory<>("obs"));

		searchByCod.setPromptText("Insira o código de orçamento");

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewOrcamentoCliente.prefHeightProperty().bind(stage.heightProperty());
		tableViewOrcamentoCliente.prefWidthProperty().bind(stage.widthProperty());

		Utils.formatTableColumnDate(tableColumnDataOrcamento, "dd/MM/yyyy");
		Utils.formatTableColumnDouble(tableColumnValorTotal, 2);
	}

	private void createCadastroOrcamentoClienteForm(OrcamentoCliente obj, Stage parentStage, String string) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(string));
			VBox vBox = loader.load();

			GerarNovoOrcamentoClienteController orcamentoController = loader.getController();
			orcamentoController.setOrcamentoCliente(obj);
			orcamentoController.setServices(orcamentoClienteService, clienteService, produtoService);
			orcamentoController.loadClientes();
			orcamentoController.loadProdutos();
			orcamentoController.subscribeDataChangeListener(this);

			Stage orcamentoClienteStage = new Stage();
			orcamentoClienteStage.setTitle("Novo orçamento");
			orcamentoClienteStage.setScene(new Scene(vBox));
			orcamentoClienteStage.setResizable(false);
			orcamentoClienteStage.initOwner(parentStage);
			orcamentoClienteStage.initModality(Modality.WINDOW_MODAL);
			orcamentoClienteStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}

	}

	private void createEditarOrcamentoClienteForm(OrcamentoCliente obj, Stage parentStage, String string) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(string));
			VBox vBox = loader.load();

			EditarOrcamentoClienteController editarController = loader.getController();
			editarController.setOrcamentoCliente(obj);
			editarController.setServices(orcamentoClienteService, clienteService, produtoService);
			editarController.loadClientes();
			editarController.updateOrcamentoClienteData();
			editarController.subscribeDataChangeListener(this);

			Stage editarOrcamentoStage = new Stage();
			editarOrcamentoStage.setTitle("Editar orçamento");
			editarOrcamentoStage.setScene(new Scene(vBox));
			editarOrcamentoStage.setResizable(false);
			editarOrcamentoStage.initOwner(parentStage);
			editarOrcamentoStage.initModality(Modality.WINDOW_MODAL);
			editarOrcamentoStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void createGerarOrdemServicoClienteForm(OrcamentoCliente obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox vBox = loader.load();

			GerarOrdemDeServicoClienteController controller = loader.getController();
			controller.setServicos(new OrdemServicoClienteService(), new ClienteService(), new FuncionarioService());
			controller.loadFuncionariosOrcamento();
			controller.loadStatusServico();
			controller.receberDadosParaCriarOS(obj);

			Stage stage = new Stage();
			stage.setTitle("Gerar ordem de serviço");
			stage.setScene(new Scene(vBox));
			stage.setResizable(false);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(parentStage);
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void initGerarOrcamentoButtons() {
		tableColumnGerarOS.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnGerarOS.setCellFactory(param -> new TableCell<OrcamentoCliente, OrcamentoCliente>() {
			private final Button button = new Button("Gerar Ordem de Serviço");

			@Override
			protected void updateItem(OrcamentoCliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createGerarOrdemServicoClienteForm(obj, Utils.currentStage(event),
						"/guiOrdemDeServicoCliente/GerarOrdemDeServicoCliente.fxml"));
			}
		});
	}

	@FXML
	private void onBtGerarPDFaction(OrcamentoCliente obj) {
		try {
			JFileChooser fc = new JFileChooser("C:\\Users\\ew21\\Desktop\\Orçamentos\\Pessoa Fisica");
			File fileOrcamento = new File("");
			int returnValue = fc.showOpenDialog(null);
			if(returnValue == JFileChooser.APPROVE_OPTION) {
				fileOrcamento = fc.getSelectedFile();
			}
			String fileName = "Orcamento " + obj.getCodCliente().getNome()+".pdf";
			PDDocument pDDocument = PDDocument.load(fileOrcamento);
			PDAcroForm pDAcroForm = pDDocument.getDocumentCatalog().getAcroForm();
			PDField field = pDAcroForm.getField("txtOrcamento");
			field.setValue(obj.getNumOrcamento().toString());
			field = pDAcroForm.getField("txtData");
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String dataFormatada = dateFormat.format(obj.getDataOrcamento());
			field.setValue(dataFormatada);
			field = pDAcroForm.getField("txtNome");
			field.setValue(obj.getCodCliente().getNome());
			field = pDAcroForm.getField("txtRg");
			field.setValue(obj.getCodCliente().getRg());
			field = pDAcroForm.getField("txtTelefone");
			field.setValue("("+obj.getCodCliente().getDdd()+")"+obj.getCodCliente().getTelefone());
			field = pDAcroForm.getField("txtEmail");
			field.setValue(obj.getCodCliente().getEmail());
			field = pDAcroForm.getField("txtDescricao");
			field.setValue(obj.getDescServico());
			field = pDAcroForm.getField("txtValorTotal");
			field.setValue("R$"+String.format("%.2f", obj.getValorTotal()));
			pDDocument.save(fileOrcamento.getParentFile()+"/"+fileName);
			pDDocument.close();
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<OrcamentoCliente, OrcamentoCliente>() {
			private final Button button = new Button("Editar/ Visualizar");

			@Override
			protected void updateItem(OrcamentoCliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createEditarOrcamentoClienteForm(obj, Utils.currentStage(event),
						"/guiOrcamentoCliente/EditarOrcamentoCliente.fxml"));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<OrcamentoCliente, OrcamentoCliente>() {
			private final Button button = new Button("Deletar");

			@Override
			protected void updateItem(OrcamentoCliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> excluirOrcamentoCliente(obj));
			}
		});
	}

	private void excluirOrcamentoCliente(OrcamentoCliente obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("EXCLUIR ORÇAMENTO",
				"Tem certeza que deseja remover esse orçamento?");
		if (result.get() == ButtonType.OK) {
			if (orcamentoClienteService == null) {
				throw new IllegalStateException("Orcamento vazio");
			}
			try {
				OrdemServicoCliente os = ordemServicoClienteService.findByNumPedido(obj.getNumOrcamento());
				if(os != null) ordemServicoClienteService.removerOrdemServicoCliente(os);
				orcamentoClienteService.removerOrcamento(obj);
				updateTableViewOrcamentoCliente();
			} catch (DbException e) {
				Alerts.showAlert("Erro ao excluir orçamento", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

	private void initOrcamentoPDFButtons() {
		tableColumnOrcamentoEmPDF.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnOrcamentoEmPDF.setCellFactory(param -> new TableCell<OrcamentoCliente, OrcamentoCliente>() {
			private final Button button = new Button("Gerar PDF");

			@Override
			protected void updateItem(OrcamentoCliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(e -> onBtGerarPDFaction(obj));
			}
		});
	}
}
