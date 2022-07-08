package guiOrcamentoEmpresa;

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
import entities.services.EmpresaService;
import entities.services.FuncionarioService;
import entities.services.OrcamentoEmpresaService;
import entities.services.OrdemServicoEmpresaService;
import entities.services.ProdutoService;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import guiOrdemDeServicoEmpresa.GerarOrdemDeServicoEmpresaController;
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
import marcenaria.entities.Empresa;
import marcenaria.entities.OrcamentoEmpresa;

public class OrcamentoEmpresaVisualizarController implements Initializable, DataChangeListener {

	private OrcamentoEmpresaService orcamentoEmpresaService;
	private EmpresaService empresaService;
	private ProdutoService produtoService;
	
	@FXML
	private TableView<OrcamentoEmpresa> tableViewOrcamentoEmpresa;
	
	@FXML
	private TableColumn<OrcamentoEmpresa, Integer> tableColumnId;

	@FXML
	private TableColumn<OrcamentoEmpresa, Integer> tableColumnNumOrcamento;

	@FXML
	private TableColumn<Empresa, Integer> tableColumnCodEmpresa;
	
	@FXML
	private TableColumn<OrcamentoEmpresa, Integer> tableColumnNomeResponsavel;

	@FXML
	private TableColumn<Empresa, String> tableColumnTelefone;

	@FXML
	private TableColumn<Empresa, String> tableColumnCelular;

	@FXML
	private TableColumn<Empresa, String> tableColumnEmail;

	@FXML
	private TableColumn<OrcamentoEmpresa, String> tableColumnDescServico;

	@FXML
	private TableColumn<OrcamentoEmpresa, Date> tableColumnDataOrcamento;

	@FXML
	private TableColumn<OrcamentoEmpresa, Double> tableColumnValorTotal;

	@FXML
	private TableColumn<OrcamentoEmpresa, String> tableColumnObs;

	@FXML
	private TableColumn<OrcamentoEmpresa, OrcamentoEmpresa> tableColumnEditar;

	@FXML
	private TableColumn<OrcamentoEmpresa, OrcamentoEmpresa> tableColumnRemover;
	
	@FXML
	private TableColumn<OrcamentoEmpresa, OrcamentoEmpresa> tableColumnGerarOrdemServico;
	
	@FXML
	private TableColumn<OrcamentoEmpresa, OrcamentoEmpresa> tableColumnOrcamentoEmPDF;

	@FXML
	private TextField searchByCod;

	@FXML
	private Button btBuscar;

	@FXML
	private Button mostrarTodos;

	@FXML
	private Button btNovoOrcamentoEmpresa;

	private ObservableList<OrcamentoEmpresa> obsList;

	public void SetServices(OrcamentoEmpresaService orcamentoEmpresaService, EmpresaService empresaService,
			ProdutoService produtoService) {
		this.orcamentoEmpresaService = orcamentoEmpresaService;
		this.empresaService = empresaService;
		this.produtoService = produtoService;
	}

	@FXML
	public void onBtNovoOrcamentoEmpresa(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		OrcamentoEmpresa obj = new OrcamentoEmpresa();
		createCadastroOrcamentoEmpresaForm(obj, parentStage, "/guiOrcamentoEmpresa/GerarNovoOrcamentoEmpresa.fxml");
		
	}

	@FXML
	public void onBtMostrarTodos() {
		updateTableViewOrcamentoEmpresa();
	}

	@FXML
	public void onBtBuscar() {
		if (orcamentoEmpresaService == null) {
			throw new IllegalStateException("Orcamento Empresa null");
		}

		OrcamentoEmpresa buscaOrcamentoEmpresa = orcamentoEmpresaService
				.findByNumOrcamento(Integer.parseInt(searchByCod.getText()));

		if (buscaOrcamentoEmpresa == null) {
			Alerts.showAlert("Busca de orçamentos", null, "Nenhum orçamento encontrado no sistema", AlertType.ERROR);
		} else {
			obsList = FXCollections.observableArrayList(buscaOrcamentoEmpresa);
			tableViewOrcamentoEmpresa.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
			initGerarOrcamentoButtons();
		}
	}

	public void updateTableViewOrcamentoEmpresa() {
		if (orcamentoEmpresaService == null) {
			throw new IllegalStateException("Orcamento null");
		}

		List<OrcamentoEmpresa> list = orcamentoEmpresaService.findAllParaTabela();
		obsList = FXCollections.observableArrayList(list);
		tableViewOrcamentoEmpresa.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		initGerarOrcamentoButtons();
		initOrcamentoPDFButtons();
	}

	@Override
	public void onDataChanged() {
		updateTableViewOrcamentoEmpresa();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnNumOrcamento.setCellValueFactory(new PropertyValueFactory<>("numOrcamento"));
		tableColumnCodEmpresa.setCellValueFactory(new PropertyValueFactory<>("codEmpresa"));
		tableColumnNomeResponsavel.setCellValueFactory(new PropertyValueFactory<>("nomeResponsavel"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		tableColumnCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnDescServico.setCellValueFactory(new PropertyValueFactory<>("descServico"));
		tableColumnDataOrcamento.setCellValueFactory(new PropertyValueFactory<>("dataOrcamento"));
		tableColumnValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
		tableColumnObs.setCellValueFactory(new PropertyValueFactory<>("obs"));
		
		searchByCod.setPromptText("Insira o código do orçamento");
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewOrcamentoEmpresa.prefHeightProperty().bind(stage.heightProperty());
		tableViewOrcamentoEmpresa.prefWidthProperty().bind(stage.widthProperty());
		
		Utils.formatTableColumnDate(tableColumnDataOrcamento, "dd/MM/yyyy");
		Utils.formatTableColumnDouble(tableColumnValorTotal, 2);
		
	}

	private void createCadastroOrcamentoEmpresaForm(OrcamentoEmpresa obj, Stage parentStage, String string) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(string));
			VBox vBox = loader.load();
			
			GerarNovoOrcamentoEmpresaController orcamentoController = loader.getController();
			orcamentoController.setOrcamentoEmpresa(obj);
			orcamentoController.setServices(orcamentoEmpresaService, empresaService, produtoService);
			orcamentoController.loadEmpresas();
			orcamentoController.loadProdutos();
			orcamentoController.subscribeDataChangeListener(this);
			
			Stage orcamentoEmpresaStage = new Stage();
			orcamentoEmpresaStage.setTitle("Novo orçamento");
			orcamentoEmpresaStage.setScene(new Scene(vBox));
			orcamentoEmpresaStage.setResizable(false);
			orcamentoEmpresaStage.initOwner(parentStage);
			orcamentoEmpresaStage.initModality(Modality.WINDOW_MODAL);
			orcamentoEmpresaStage.showAndWait();
		}
		catch(IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}

	}
	
	private void createEditarOrcamentoEmpresaForm(OrcamentoEmpresa obj, Stage parentStage, String string) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(string));
			VBox vBox = loader.load();
			
			EditarOrcamentoEmpresaController editarController = loader.getController();
			editarController.setOrcamentoEmpresa(obj);
			editarController.setServices(orcamentoEmpresaService, empresaService, produtoService);
			editarController.loadEmpresas();
			editarController.updateOrcamentoEmpresaData();
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
	
	private void createGerarOrdemServicoEmpresaForm(OrcamentoEmpresa obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox vBox = loader.load();
			
			GerarOrdemDeServicoEmpresaController controller = loader.getController();
			controller.setServicos(new OrdemServicoEmpresaService(), empresaService, new FuncionarioService());
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
			
		}
		catch(IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void initGerarOrcamentoButtons() {
		tableColumnGerarOrdemServico.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnGerarOrdemServico.setCellFactory(param -> new TableCell<OrcamentoEmpresa, OrcamentoEmpresa>() {
			private final Button button = new Button("Gerar Ordem de Serviço");

			@Override
			protected void updateItem(OrcamentoEmpresa obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createGerarOrdemServicoEmpresaForm(obj, Utils.currentStage(event),
						"/guiOrdemDeServicoEmpresa/GerarOrdemDeServicoEmpresa.fxml"));
			}
		});
	}
	
	@FXML
	private void onBtGerarPDFAction(OrcamentoEmpresa obj) {
		try {
			JFileChooser fc = new JFileChooser("C:\\Users\\ew21\\Desktop\\Orçamentos\\Pessoa Juridica Empresas");
			File fileOrcamento = new File("");
			int returnValue = fc.showOpenDialog(null);
			if(returnValue == JFileChooser.APPROVE_OPTION) {
				fileOrcamento = fc.getSelectedFile();
			}
			String fileName = "Orcamento " + obj.getCodEmpresa().getNomeFantasia()+".pdf";
			PDDocument pDDocument = PDDocument.load(fileOrcamento);
			PDAcroForm pDAcroForm = pDDocument.getDocumentCatalog().getAcroForm();
			PDField field = pDAcroForm.getField("txtOrcamento");
			field.setValue(obj.getNumOrcamento().toString());
			field = pDAcroForm.getField("txtData");
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String dataFormatada = dateFormat.format(obj.getDataOrcamento());
			field.setValue(dataFormatada);
			field = pDAcroForm.getField("txtRazaoSocial");
			field.setValue(obj.getCodEmpresa().getRazaoSocial());
			field = pDAcroForm.getField("txtNomeFantasia");
			field.setValue(obj.getCodEmpresa().getNomeFantasia());
			field = pDAcroForm.getField("txtCnpj");
			field.setValue(obj.getCodEmpresa().getCnpj());
			field = pDAcroForm.getField("txtSoliciante");
			field.setValue(obj.getNomeResponsavel());
			field = pDAcroForm.getField("txtEndereco");
			field.setValue(obj.getCodEmpresa().getRua()+", Nº "+obj.getCodEmpresa().getNumero());
			field = pDAcroForm.getField("txtTelefone");
			field.setValue("("+obj.getCodEmpresa().getDdd()+")"+obj.getCodEmpresa().getTelefone());
			field = pDAcroForm.getField("txtEmail");
			field.setValue(obj.getCodEmpresa().getEmail());
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
		tableColumnEditar.setCellFactory(param -> new TableCell<OrcamentoEmpresa, OrcamentoEmpresa>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(OrcamentoEmpresa obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createEditarOrcamentoEmpresaForm(obj, Utils.currentStage(event),
						"/guiOrcamentoEmpresa/EditarOrcamentoEmpresa.fxml"));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<OrcamentoEmpresa, OrcamentoEmpresa>() {
			private final Button button = new Button("Deletar");

			@Override
			protected void updateItem(OrcamentoEmpresa obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> excluirOrcamentoEmpresa(obj));
			}
		});
	}
	
	private void excluirOrcamentoEmpresa(OrcamentoEmpresa obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("EXCLUIR ORÇAMENTO", "Tem certeza que deseja remover esse orçamento?");
			if(result.get() == ButtonType.OK) {
				if(orcamentoEmpresaService == null) {
					throw new IllegalStateException("Orcamento vazio");
				}
				try {
					orcamentoEmpresaService.removerOrcamento(obj);
					updateTableViewOrcamentoEmpresa();
				}
				catch(DbException e) {
					Alerts.showAlert("Erro ao excluir orçamento", null, e.getMessage(), AlertType.ERROR);
				}
			}
	}
	
	private void initOrcamentoPDFButtons() {
		tableColumnOrcamentoEmPDF.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnOrcamentoEmPDF.setCellFactory(param -> new TableCell<OrcamentoEmpresa, OrcamentoEmpresa>() {
			private final Button button = new Button("Gerar PDF");

			@Override
			protected void updateItem(OrcamentoEmpresa obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(e -> onBtGerarPDFAction(obj));
			}
		});
	}
}

