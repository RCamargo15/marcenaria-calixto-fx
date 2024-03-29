package guiOrcamentoCliente;

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
import entities.services.ClienteService;
import entities.services.OrcamentoClienteService;
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
import marcenaria.entities.Cliente;
import marcenaria.entities.OrcamentoCliente;
import model.exceptions.ValidationException;

public class EditarOrcamentoClienteController implements Initializable, DataChangeListener {

	private OrcamentoCliente orcamentoCliente;
	private OrcamentoClienteService orcamentoClienteService;
	private ClienteService clienteService;
	private ProdutoService produtoService;
	
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();
	
	@FXML
	private TextField txtNumOrcamento;
	
	@FXML
	private GridPane gpInfoCliente;
	
	@FXML
	private ComboBox<Cliente> cbCodCliente;
	@FXML
	private Label errorCodCliente;
	
	@FXML
	private TextField txtTelefoneCliente;
	@FXML
	private Label errorTelefone;
	
	@FXML
	private TextField txtCelularCliente;
	@FXML
	private Label errorCelular;
	
	@FXML
	private TextField txtEmailCliente;
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
	private TableView<OrcamentoCliente> tableViewOrcamentoCliente;
	
	@FXML
	private TableColumn<OrcamentoCliente, Integer> tableColumnDescProduto;
	
	@FXML
	private TableColumn<OrcamentoCliente, Double> tableColumnValorUnit;
	
	@FXML
	private TableColumn<OrcamentoCliente, Integer> tableColumnQuantidade;
	
	@FXML
	private TableColumn<OrcamentoCliente, OrcamentoCliente> tableColumnEditar;
	
	@FXML
	private TableColumn<OrcamentoCliente, OrcamentoCliente> tableColumnRemover;
	
	@FXML
	private TextField txtValorTotalOrcamento;
	
	@FXML
	private Button btGerarOrcamento;
	
	@FXML
	private Button btCancelar;
	
	private ObservableList<Cliente> obsListCliente;
	
	private ObservableList<OrcamentoCliente> obsListProdutoOrcamento;
	
	public void setOrcamentoCliente(OrcamentoCliente orcamentoCliente) {
		this.orcamentoCliente = orcamentoCliente;
	}
	
	public void setServices(OrcamentoClienteService orcamentoClienteService,ClienteService clienteService, ProdutoService produtoService) {
		this.orcamentoClienteService = orcamentoClienteService;
		this.clienteService = clienteService;
		this.produtoService = produtoService;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	@Override
	public void onDataChanged() {
		updateOrcamentoClienteData();
	}
	
	private void notificarDataChangeListener() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}
	
	public OrcamentoCliente getOrcamentoClienteData() {
		ValidationException exception = new ValidationException("Erro de validação");
		OrcamentoCliente obj = new OrcamentoCliente();
		obj.setNumOrcamento(Integer.parseInt(txtNumOrcamento.getText()));
		obj.setCodCliente(cbCodCliente.getValue());
		obj.setTelefone(txtTelefoneCliente.getText());
		obj.setCelular(txtCelularCliente.getText());
		obj.setEmail(txtEmailCliente.getText());
		obj.setDescServico(txtDescricaoServico.getText());
		if(dpDataOrcamento == null) {
			exception.addError("dataOrcamento", "Insira a data em que esse orçamento está sendo realizado");
		} else {
			Instant instant = Instant.from(dpDataOrcamento.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataOrcamento(Date.from(instant));
		}
		obj.setValorTotal(Double.parseDouble(Utils.getValorTotalNota(txtValorTotalOrcamento.getText())));
		obj.setObs(txtObs.getText());
		
		if (cbCodCliente.getValue() == null) {
			exception.addError("codCliente", "Você deve selecionar um cliente para esse orçamento");
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
		if (orcamentoCliente == null) {
			throw new IllegalStateException("Orcamento null");
		}
		if (orcamentoClienteService == null) {
			throw new IllegalStateException("Orcamento null");
		}
		try {
			orcamentoCliente = getOrcamentoClienteData();
			orcamentoClienteService.saveOrcamento(orcamentoCliente);
			notificarDataChangeListener();
			Utils.currentStage(event).close();
		} catch (DbException e) {
			e.getMessage();
		}
	}
	
	
	public void updateOrcamentoClienteData() {
		if(orcamentoClienteService == null) {
			throw new IllegalStateException("Orcamentoservice null");
		}
		List<OrcamentoCliente> listOrcamentos = orcamentoClienteService.findByNumOrcamentoList(orcamentoCliente.getNumOrcamento());
		obsListProdutoOrcamento = FXCollections.observableArrayList(listOrcamentos);
		tableViewOrcamentoCliente.setItems(obsListProdutoOrcamento);
		initEditButtons();
		initRemoveButtons();
		double valorTotal = 0;
		txtNumOrcamento.setText(String.valueOf(orcamentoCliente.getNumOrcamento()));
		if(orcamentoCliente.getCodCliente() == null) {
			cbCodCliente.getSelectionModel().selectFirst();
		} else {
			cbCodCliente.setValue(orcamentoCliente.getCodCliente());
		}
		txtTelefoneCliente.setText(orcamentoCliente.getTelefone());
		txtCelularCliente.setText(orcamentoCliente.getCelular());
		txtEmailCliente.setText(orcamentoCliente.getEmail());
		txtDescricaoServico.setText(orcamentoCliente.getDescServico());
		if(orcamentoCliente.getDataOrcamento() != null) {
			dpDataOrcamento.setValue(LocalDate.ofInstant(orcamentoCliente.getDataOrcamento().toInstant(), ZoneId.systemDefault()));
		}
		for(OrcamentoCliente orc : listOrcamentos) {
			double valorMoment = orc.getValor() * orc.getQuantidade();
			valorTotal = valorTotal + valorMoment;
		}
		double obra = orcamentoCliente.getValorMetroQuad() + orcamentoCliente.getValorObra();
		double valorFinal = valorTotal + obra;
		txtValorTotalOrcamento.setText("R$ " + String.format("%.2f",valorFinal));
		txtObs.setText(orcamentoCliente.getObs());
	}
	
	public void loadClientes() {
		if(clienteService == null) {
			throw new IllegalStateException("Cliente Service null");
		}
		List<Cliente> list = clienteService.findAll();
		obsListCliente = FXCollections.observableArrayList(list);
		cbCodCliente.setItems(obsListCliente);
	}
	
	private void initializeComboBoxCliente() {
		Callback<ListView<Cliente>, ListCell<Cliente>> factory = lv -> new ListCell<Cliente>() {
			@Override
			protected void updateItem(Cliente item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		cbCodCliente.setCellFactory(factory);
		cbCodCliente.setButtonCell(factory.call(null));
	}
	
	
	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeComboBoxCliente();	
	
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewOrcamentoCliente.prefHeightProperty().bind(stage.heightProperty());
		tableViewOrcamentoCliente.prefWidthProperty().bind(stage.widthProperty());
		gpInfoCliente.prefHeightProperty().bind(stage.heightProperty());
		gpInfoCliente.prefWidthProperty().bind(stage.widthProperty());
		tableColumnDescProduto.setCellValueFactory(new PropertyValueFactory<>("codProduto"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tableColumnValorUnit.setCellValueFactory(new PropertyValueFactory<>("valor"));
		Utils.formatDatePicker(dpDataOrcamento, "dd/MM/yyyy");
		Utils.formatTableColumnDouble(tableColumnValorUnit, 2);
	}
	
	private void createEditarProdutoOrcamentoForm(OrcamentoCliente obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			EditarProdQuantidadeClienteController editProdController = loader.getController();
			editProdController.setOrcamentoCliente(obj);
			editProdController.setServices(orcamentoClienteService, produtoService);
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
		tableColumnEditar.setCellFactory(param -> new TableCell<OrcamentoCliente, OrcamentoCliente>() {
			private final Button button = new Button("Editar");
			@Override
			protected void updateItem(OrcamentoCliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createEditarProdutoOrcamentoForm(obj, Utils.currentStage(event),
					"/guiOrcamentoCliente/EditarProdQuantidadeCliente.fxml"));
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
				button.setOnAction( (event -> excluirOrcamentoCliente(obj)));
				
			}
		});
	}
	
	private void excluirOrcamentoCliente(OrcamentoCliente obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("EXCLUIR ORÇAMENTO", "Tem certeza que deseja remover esse orçamento?");
			if(result.get() == ButtonType.OK) {
				if(orcamentoClienteService == null) {
					throw new IllegalStateException("Orcamento vazio");
				}
				try {
					orcamentoClienteService.removerProduto(obj);
					updateOrcamentoClienteData();

				}
				catch(DbException e) {
					Alerts.showAlert("Erro ao excluir orçamento", null, e.getMessage(), AlertType.ERROR);
				}
			}
	}

}
