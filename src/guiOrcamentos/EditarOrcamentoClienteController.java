package guiOrcamentos;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import application.Main;
import entities.services.ClienteService;
import entities.services.OrcamentoClienteService;
import entities.services.ProdutoService;
import gui.listeners.DataChangeListener;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import marcenaria.entities.Cliente;
import marcenaria.entities.OrcamentoCliente;
import marcenaria.entities.Produto;
import marcenaria.entities.auxiliar.ProdutoOrcamento;
import model.exceptions.ValidationException;

public class EditarOrcamentoClienteController implements Initializable {

	private OrcamentoCliente orcamentoCliente;
	private OrcamentoClienteService orcamentoClienteService;
	private ClienteService clienteService;
	private ProdutoService produtoService;
	
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
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
	private GridPane gpProduto;
	
	@FXML
	private ComboBox<Produto> cbCodProduto;
	@FXML
	private Label erroProduto;
	
	@FXML
	private TextField txtQuantidade;
	@FXML
	private Label erroQuantidade;
	
	@FXML
	private Button btInserirProduto;
	
	@FXML
	private TableView<OrcamentoCliente> tableViewOrcamentoCliente;
	
	@FXML
	private TableColumn<ProdutoOrcamento, Integer> tableColumnDescProduto;
	
	@FXML
	private TableColumn<ProdutoOrcamento, Double> tableColumnValorUnit;
	
	@FXML
	private TableColumn<ProdutoOrcamento, Integer> tableColumnQuantidade;
	
	@FXML
	private TextField txtValorTotalOrcamento;
	
	@FXML
	private Button btGerarOrcamento;
	
	@FXML
	private Button btCancelar;
	
	private ObservableList<Cliente> obsListCliente;
	
	private ObservableList<Produto> obsListProduto;
	
	private ObservableList<OrcamentoCliente> obsListProdutoOrcamento;
	
	private List<OrcamentoCliente> listaProdutos = new ArrayList<>();
	
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
	
	private void notificarDataChangeListener() {
		for(DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}
	
	private OrcamentoCliente getOrcamentoClienteData() {
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
		}else {
			Instant instant = Instant.from(dpDataOrcamento.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataOrcamento(Date.from(instant));
		}
		
		obj.setCodProduto(cbCodProduto.getValue());
		obj.setQuantidade(Integer.parseInt(txtQuantidade.getText()));
		obj.setValor(cbCodProduto.getValue());
		obj.setValorTotal(Double.parseDouble(txtValorTotalOrcamento.getText()));
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
	
	//PARA TERMINAR
	public void updateOrcamentoClienteData() {
		if(orcamentoCliente == null) {
			throw new IllegalStateException("Orcamento Cliente Service null");
		}
		
		txtNumOrcamento.setText(String.valueOf(orcamentoCliente.getNumOrcamento()));
		
		if(orcamentoCliente.getCodCliente() == null) {
			cbCodCliente.getSelectionModel().selectFirst();
		}
		else {
			cbCodCliente.setValue(orcamentoCliente.getCodCliente());
		}
		
		txtTelefoneCliente.setText(orcamentoCliente.getTelefone());
		txtCelularCliente.setText(orcamentoCliente.getCelular());
		txtEmailCliente.setText(orcamentoCliente.getEmail());
		txtDescricaoServico.setText(orcamentoCliente.getDescServico());
		txtValorTotalOrcamento.setText(String.valueOf(orcamentoCliente.getValorTotal()));
		
		
		List<OrcamentoCliente> listOrcamentos = orcamentoClienteService.findAll();
		List<OrcamentoCliente> listIguais = new ArrayList<>();
		
		for(OrcamentoCliente oc : listOrcamentos) {
			if(oc.getNumOrcamento().equals(orcamentoCliente.getNumOrcamento())) {
				listIguais.add(oc);
			}
		}
		
		cbCodProduto.getSelectionModel().selectFirst();
		
		obsListProdutoOrcamento = FXCollections.observableArrayList(listIguais);
		tableViewOrcamentoCliente.setItems(obsListProdutoOrcamento);
		
		txtObs.setText(orcamentoCliente.getObs());
		
	}
	
	@FXML
	public void onBtInserirAction() {
		
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
	}
	
	
	public void loadClientes() {
		if(clienteService == null) {
			throw new IllegalStateException("Cliente Service null");
		}
		
		List<Cliente> list = clienteService.findAll();
		obsListCliente = FXCollections.observableArrayList(list);
		cbCodCliente.setItems(obsListCliente);
	}
	
	public void loadProdutos() {
		if(produtoService == null) {
			throw new IllegalStateException("Produto service null");
		}
		
		List<Produto> list = produtoService.findAll();
		obsListProduto = FXCollections.observableArrayList(list);
		cbCodProduto.setItems(obsListProduto);
	}
	
	private void initializeComboBoxCliente() {
		Callback<ListView<Cliente>, ListCell<Cliente>> factory = lv -> new ListCell<Cliente>() {
			@Override
			protected void updateItem(Cliente item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : String.valueOf(item.getCodCliente()));
			}
		};
		cbCodCliente.setCellFactory(factory);
		cbCodCliente.setButtonCell(factory.call(null));
	}
	
	private void initializeComboBoxProduto() {
		Callback<ListView<Produto>, ListCell<Produto>> factory = lv -> new ListCell<Produto>() {
			@Override
			protected void updateItem(Produto item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : String.valueOf(item.getCodProduto()));
			}
		};
		cbCodProduto.setCellFactory(factory);
		cbCodProduto.setButtonCell(null);
	}
	
	
	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeComboBoxCliente();
		initializeComboBoxCliente();
		initializeTables();
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewOrcamentoCliente.prefHeightProperty().bind(stage.heightProperty());
		tableViewOrcamentoCliente.prefWidthProperty().bind(stage.widthProperty());
		
		gpInfoCliente.prefHeightProperty().bind(stage.heightProperty());
		gpInfoCliente.prefWidthProperty().bind(stage.widthProperty());
		
	}
	
	public void initializeTables() {
		tableColumnDescProduto.setCellValueFactory(new PropertyValueFactory<>("descProduto"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tableColumnValorUnit.setCellValueFactory(new PropertyValueFactory<>("precoProd"));
	}

}
