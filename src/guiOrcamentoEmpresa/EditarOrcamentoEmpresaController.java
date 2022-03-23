package guiOrcamentoEmpresa;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Db.DbException;
import application.Main;
import entities.services.EmpresaService;
import entities.services.OrcamentoEmpresaService;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import marcenaria.entities.Empresa;
import marcenaria.entities.OrcamentoEmpresa;
import model.exceptions.ValidationException;

public class EditarOrcamentoEmpresaController implements Initializable, DataChangeListener {

	private OrcamentoEmpresa orcamentoEmpresa;
	private OrcamentoEmpresaService orcamentoEmpresaService;
	private EmpresaService empresaService;
	private ProdutoService produtoService;
	
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();
	
	@FXML
	private TextField txtNumOrcamento;
	
	@FXML
	private GridPane gpInfoEmpresa;
	
	@FXML
	private ComboBox<Empresa> cbCodEmpresa;
	@FXML
	private Label errorCodEmpresa;
	
	@FXML
	private TextField txtNomeResponsavel;
	@FXML
	private Label errorNomeResponsavel;
	
	@FXML
	private TextField txtTelefoneEmpresa;
	@FXML
	private Label errorTelefone;
	
	@FXML
	private TextField txtCelularEmpresa;
	@FXML
	private Label errorCelular;
	
	@FXML
	private TextField txtEmailEmpresa;
	@FXML
	private Label errorEmail;
	
	@FXML
	private TextField txtDescricaoServico;
	@FXML
	private Label errorDescServico;
	
	@FXML
	private DatePicker dpDataOrcamento;
	@FXML
	private Label erroDataOrcamento;
	
	@FXML
	private TextField txtObs;
	
	@FXML
	private TextField txtQuantidade;
	@FXML
	private Label erroQuantidade;
	
	@FXML
	private TableView<OrcamentoEmpresa> tableViewOrcamentoEmpresa;
	
	@FXML
	private TableColumn<OrcamentoEmpresa, Integer> tableColumnId;

	@FXML
	private TableColumn<OrcamentoEmpresa, Integer> tableColumnNumOrcamento;

	@FXML
	private TableColumn<Empresa, Integer> tableColumnCodEmpresa;
	
	@FXML
	private TableColumn<OrcamentoEmpresa, String> tableColumnNomeResponsavel;

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
	private TableColumn<OrcamentoEmpresa, Integer> tableColumnDescProduto;
	
	@FXML
	private TableColumn<OrcamentoEmpresa, Double> tableColumnValorUnit;
	
	@FXML
	private TableColumn<OrcamentoEmpresa, Double> tableColumnValorTotal;

	@FXML
	private TableColumn<OrcamentoEmpresa, String> tableColumnObs;
	
	@FXML
	private TableColumn<OrcamentoEmpresa, Integer> tableColumnQuantidade;
	
	@FXML
	private TableColumn<OrcamentoEmpresa, OrcamentoEmpresa> tableColumnEditar;
	
	@FXML
	private TableColumn<OrcamentoEmpresa, OrcamentoEmpresa> tableColumnRemover;
	
	@FXML
	private TextField txtValorTotalOrcamento;
	
	@FXML
	private Button btGerarOrcamento;
	
	@FXML
	private Button btCancelar;
	
	private ObservableList<Empresa> obsListEmpresa;
	
	private ObservableList<OrcamentoEmpresa> obsListProdutoOrcamento;
	
	public void setOrcamentoEmpresa(OrcamentoEmpresa orcamentoEmpresa) {
		this.orcamentoEmpresa = orcamentoEmpresa;
	}
	
	public void setServices(OrcamentoEmpresaService orcamentoEmpresaService,EmpresaService empresaService, ProdutoService produtoService) {
		this.orcamentoEmpresaService = orcamentoEmpresaService;
		this.empresaService = empresaService;
		this.produtoService = produtoService;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	@Override
	public void onDataChanged() {
		updateOrcamentoEmpresaData();
	}
	
	private void notificarDataChangeListener() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}
	
	public OrcamentoEmpresa getOrcamentoEmpresaData() {
		ValidationException exception = new ValidationException("Erro de validação");
		
		OrcamentoEmpresa obj = new OrcamentoEmpresa();
		
		obj.setNumOrcamento(Integer.parseInt(txtNumOrcamento.getText()));
		obj.setCodEmpresa(cbCodEmpresa.getValue());
		obj.setNomeResponsavel(txtNomeResponsavel.getText());
		obj.setTelefone(txtTelefoneEmpresa.getText());
		obj.setCelular(txtCelularEmpresa.getText());
		obj.setEmail(txtEmailEmpresa.getText());
		obj.setDescServico(txtDescricaoServico.getText());
		
		if(dpDataOrcamento == null) {
			exception.addError("dataOrcamento", "Insira a data em que esse orçamento está sendo realizado");
		}else {
			Instant instant = Instant.from(dpDataOrcamento.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataOrcamento(Date.from(instant));
		}
		
		obj.setValorTotal(Double.parseDouble(txtValorTotalOrcamento.getText()));
		obj.setObs(txtObs.getText());
		
		if (cbCodEmpresa.getValue() == null) {
			exception.addError("codEmpresa", "Você deve selecionar um cliente para esse orçamento");
		}
		
		if (txtDescricaoServico.getText() == null || txtDescricaoServico.getText().trim().equals("")) {
			exception.addError("descServico", "Descreva o serviço solicitado pelo cliente");
		}
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;
	}
	
	@FXML
	public void onBtAtualizarAction(ActionEvent event) {
		if(orcamentoEmpresa == null) {
			throw new IllegalStateException("Orcamento null");
		}
		if(orcamentoEmpresaService == null) {
			throw new IllegalStateException("Orcamento null");
		}
			try{
				orcamentoEmpresa = getOrcamentoEmpresaData();
				orcamentoEmpresaService.saveOrcamento(orcamentoEmpresa);
				notificarDataChangeListener();
				Utils.currentStage(event).close();
			}
			catch(DbException e) {
				e.getMessage();
			}
		}
	
	
	public void updateOrcamentoEmpresaData() {
		
		if(orcamentoEmpresaService == null) {
			throw new IllegalStateException("Orcamentoservice null");
		}
		
		List<OrcamentoEmpresa> listOrcamentos = orcamentoEmpresaService.findByNumOrcamentoList(orcamentoEmpresa.getNumOrcamento());
		obsListProdutoOrcamento = FXCollections.observableArrayList(listOrcamentos);
		tableViewOrcamentoEmpresa.setItems(obsListProdutoOrcamento);
		initEditButtons();
		initRemoveButtons();
		double valorTotal = 0;
		
		
		txtNumOrcamento.setText(String.valueOf(orcamentoEmpresa.getNumOrcamento()));
		
		if(orcamentoEmpresa.getCodEmpresa() == null) {
			cbCodEmpresa.getSelectionModel().selectFirst();
		}
		else {
			cbCodEmpresa.setValue(orcamentoEmpresa.getCodEmpresa());
		}
		
		txtNomeResponsavel.setText(orcamentoEmpresa.getNomeResponsavel());
		txtTelefoneEmpresa.setText(orcamentoEmpresa.getTelefone());
		txtCelularEmpresa.setText(orcamentoEmpresa.getCelular());
		txtEmailEmpresa.setText(orcamentoEmpresa.getEmail());
		txtDescricaoServico.setText(orcamentoEmpresa.getDescServico());
		
		if(orcamentoEmpresa.getDataOrcamento() != null) {
			dpDataOrcamento.setValue(LocalDate.ofInstant(orcamentoEmpresa.getDataOrcamento().toInstant(), ZoneId.systemDefault()));
		}
		
		for(OrcamentoEmpresa orc : listOrcamentos) {
			double valorMoment = orc.getValor() * orc.getQuantidade();
			valorTotal = valorTotal + valorMoment;
		}
		txtValorTotalOrcamento.setText(String.valueOf(valorTotal));
		txtObs.setText(orcamentoEmpresa.getObs());
		
		
	}
	
//	private void setErrorMessages(Map<String, String> errors) {
//		Set<String> fields = errors.keySet();
//	}
	
	
	public void loadEmpresas() {
		if(empresaService == null) {
			throw new IllegalStateException("Empresa Service null");
		}
		
		List<Empresa> list = empresaService.findAll();
		obsListEmpresa = FXCollections.observableArrayList(list);
		cbCodEmpresa.setItems(obsListEmpresa);
	}
	
	private void initializeComboBoxEmpresa() {
		Callback<ListView<Empresa>, ListCell<Empresa>> factory = lv -> new ListCell<Empresa>() {
			@Override
			protected void updateItem(Empresa item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeFantasia());
			}
		};
		cbCodEmpresa.setCellFactory(factory);
		cbCodEmpresa.setButtonCell(factory.call(null));
	}
	
	
	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeComboBoxEmpresa();	
	
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewOrcamentoEmpresa.prefHeightProperty().bind(stage.heightProperty());
		tableViewOrcamentoEmpresa.prefWidthProperty().bind(stage.widthProperty());
		
		gpInfoEmpresa.prefHeightProperty().bind(stage.heightProperty());
		gpInfoEmpresa.prefWidthProperty().bind(stage.widthProperty());
		
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNumOrcamento.setCellValueFactory(new PropertyValueFactory<>("numOrcamento"));
		tableColumnCodEmpresa.setCellValueFactory(new PropertyValueFactory<>("codEmpresa"));
		tableColumnNomeResponsavel.setCellValueFactory(new PropertyValueFactory<>("nomeResponsavel"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		tableColumnCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnDescServico.setCellValueFactory(new PropertyValueFactory<>("descServico"));
		tableColumnDataOrcamento.setCellValueFactory(new PropertyValueFactory<>("dataOrcamento"));
		tableColumnDescProduto.setCellValueFactory(new PropertyValueFactory<>("codProduto"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tableColumnValorUnit.setCellValueFactory(new PropertyValueFactory<>("valor"));
		tableColumnValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
		tableColumnObs.setCellValueFactory(new PropertyValueFactory<>("obs"));
	}
	
	private void createEditarProdutoOrcamentoForm(OrcamentoEmpresa obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			EditarProdQuantidadeEmpresaController editProdController = loader.getController();
			editProdController.setOrcamentoEmpresa(obj);
			editProdController.setServices(orcamentoEmpresaService, produtoService);
			editProdController.loadProdutos();
			editProdController.updateEditarProdQuantidadeData();
			editProdController.subscribeDataChangeListener(this);

			Stage editarProdQtdStage = new Stage();
			editarProdQtdStage.setTitle("Editar produto e quantidade");
			editarProdQtdStage.setScene(new Scene(newVBox));
			editarProdQtdStage.setResizable(false);
			editarProdQtdStage.initOwner(parentStage);
			editarProdQtdStage.initModality(Modality.WINDOW_MODAL);
			editarProdQtdStage.showAndWait();
			
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
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
				button.setOnAction(event -> createEditarProdutoOrcamentoForm(obj, Utils.currentStage(event),
						"/guiOrcamentoEmpresa/EditarProdQuantidadeEmpresa.fxml"));
				
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
				button.setOnAction( (event -> excluirOrcamentoEmpresa(obj)));
				
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
					orcamentoEmpresaService.removerProduto(obj);
					updateOrcamentoEmpresaData();

				}
				catch(DbException e) {
					Alerts.showAlert("Erro ao excluir orçamento", null, e.getMessage(), AlertType.ERROR);
				}
			}
	}

}
