package guiNotasCompras;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import Db.DbException;
import entities.services.FornecedorService;
import entities.services.NotasComprasService;
import entities.services.ProdutoService;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import marcenaria.entities.Fornecedor;
import marcenaria.entities.NotasCompras;
import marcenaria.entities.Produto;
import model.exceptions.ValidationException;

public class CadastroNotasComprasController implements Initializable {

	private NotasCompras notasCompras;

	private NotasComprasService notasComprasService;
	
	private FornecedorService fornecedorService;

	private ProdutoService produtoService;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtCodNotasCompras;

	@FXML
	private ComboBox<Produto> comboBoxProduto;
	@FXML
	private Label errorComboBoxProduto;
	
	@FXML
	private ComboBox<Fornecedor> comboBoxFornecedor;
	@FXML
	private Label errorComboBoxFornecedor;

	@FXML
	private TextField txtNumeroNF;
	@FXML
	private Label errorNumeroNF;

	@FXML
	private TextField txtQuantidade;
	@FXML
	private Label errorQuantidade;
	
	@FXML
	private TextField txtValorUnit;
	@FXML
	private Label errorValorUnit;
	
	@FXML
	private TextField txtValorTotal;
	@FXML
	private Label errorValorTotal;
	
	@FXML
	private TextField txtValorTotalNota;
	@FXML
	private Label errorValorTotalNota;
	
	@FXML
	private TextField txtChaveNF;
	@FXML
	private Label errorChaveNF;
	
	@FXML
	private DatePicker dpDataEmissao;
	@FXML
	private Label errorDataEmissao;
	
	@FXML
	private DatePicker dpDataEntrada;
	@FXML
	private Label errorDataEntrada;
	
	@FXML
	private TextField txtObs;

	@FXML
	private Button btCadastrar;

	@FXML
	private Button btCancelar;

	@FXML
	private GridPane gpNotasCompras;

	private ObservableList<Produto> obsListProduto;
	
	private ObservableList<Fornecedor> obsListFornecedor;

	public void setNotasCompras(NotasCompras notasCompras) {
		this.notasCompras = notasCompras;
	}

	public void setServices(FornecedorService fornecedorService, ProdutoService produtoService) {
		this.fornecedorService = fornecedorService;
		this.produtoService = produtoService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		if (notasCompras == null) {
			throw new IllegalStateException("NotasCompras vazio");
		}
		if (notasComprasService == null) {
			throw new IllegalStateException("NotasComprasService vazio");
		}
		try {
			notasCompras = getNotasComprasData();
			notasComprasService.saveOrUpdate(notasCompras);
			notificarDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Erro ao cadastrar notasCompras", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notificarDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	private NotasCompras getNotasComprasData() {

		ValidationException exception = new ValidationException("Erro de validação");
		NotasCompras obj = new NotasCompras();

		obj.setCodNota(Integer.parseInt(txtCodNotasCompras.getText()));
		obj.setCodFornecedor(comboBoxFornecedor.getValue());
		obj.setNumeroNF(txtNumeroNF.getText());
		obj.setCodProduto(comboBoxProduto.getValue());
		obj.setQuantidade(Integer.parseInt(txtQuantidade.getText()));
		obj.setValorUnit(Double.parseDouble(txtValorUnit.getText()));
		obj.setValorTotal(Double.parseDouble(txtValorTotal.getText()));
		obj.setValorTotalNota(Double.parseDouble(txtValorTotalNota.getText()));
		obj.setChaveNF(txtChaveNF.getText());
		if(dpDataEmissao == null) {
			exception.addError("dataEmissao", "Insira a data de emissão da nota fiscal");
		}
		else {
			Instant instant = Instant.from(dpDataEmissao.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataEmissao(Date.from(instant));
		}
		
		if(dpDataEntrada == null) {
			exception.addError("dataEntrada", "Insira a data de cadastro dessa nota fiscal");
		}
		else {
			Instant instant = Instant.from(dpDataEntrada.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataEmissao(Date.from(instant));
		}
		obj.setObs(txtObs.getText());

//		if (txtNotasComprasAtual.getText() == null || txtNotasComprasAtual.getText().trim().equals("")) {
//			exception.addError("NotasComprasAtual", "Insira a quantidade atual de produtos antes de inserir no notasCompras");
//		}
//
//		if (txtNotasComprasMinimo.getText() == null || txtNotasComprasMinimo.getText().trim().equals("")) {
//			exception.addError("NotasComprasMinimo", "Estabeleça um valor mínimo de produtos em seu notasCompras");
//		}

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void updateNotasComprasData() {
		if (notasCompras == null) {
			throw new IllegalStateException("NotasCompras vazio");
		}
		
		txtCodNotasCompras.setText(String.valueOf(notasCompras.getCodNota()));
		txtNumeroNF.setText(notasCompras.getNumeroNF());
		if(notasCompras.getCodFornecedor() == null) {
			comboBoxFornecedor.getSelectionModel().selectFirst();
		} else {
			comboBoxFornecedor.setValue(notasCompras.getCodFornecedor());
		}
		
		if (notasCompras.getCodProduto() == null) {
			comboBoxProduto.getSelectionModel().selectFirst();
		} else {
			comboBoxProduto.setValue(notasCompras.getCodProduto());
		}
		txtQuantidade.setText(String.valueOf(notasCompras.getQuantidade()));
		txtValorUnit.setText(String.valueOf(notasCompras.getValorUnit()));
		txtValorTotal.setText(String.valueOf(notasCompras.getValorTotal()));
		txtValorTotalNota.setText(String.valueOf(notasCompras.getValorTotalNota()));
		txtChaveNF.setText(notasCompras.getChaveNF());
		
		if(notasCompras.getDataEmissao() != null) {
			dpDataEmissao.setValue(LocalDate.ofInstant(notasCompras.getDataEmissao().toInstant(), ZoneId.systemDefault()));
		}
		
		if(notasCompras.getDataEntrada() != null) {
			dpDataEntrada.setValue(LocalDate.ofInstant(notasCompras.getDataEntrada().toInstant(), ZoneId.systemDefault()));
		}
		
		txtObs.setText(notasCompras.getObs());
		
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
//
//		errorNotasComprasAtual.setText(fields.contains("NotasComprasAtual") ? errors.get("NotasComprasAtual") : "");
//		errorNotasComprasMinimo.setText(fields.contains("NotasComprasMinimo") ? errors.get("NotasComprasMinimo") : "");

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(txtQuantidade);
		initializeComboBoxProduto();
		initializeComboBoxFornecedor();
	}

	public void loadProdutos() {
		if (produtoService == null) {
			throw new IllegalStateException("ProdutoService null. Precisa ser injetado");
		}
		List<Produto> list = produtoService.findAll();
		obsListProduto = FXCollections.observableArrayList(list);
		comboBoxProduto.setItems(obsListProduto);
	}
	
	public void loadFornecedor() {
		if(fornecedorService == null) {
			throw new IllegalStateException("FornecedorService null.");
		}
		List<Fornecedor> list = fornecedorService.findAll();
		obsListFornecedor = FXCollections.observableArrayList(list);
		comboBoxFornecedor.setItems(obsListFornecedor);
	}

	private void initializeComboBoxProduto() {
		Callback<ListView<Produto>, ListCell<Produto>> factory = lv -> new ListCell<Produto>() {
			@Override
			protected void updateItem(Produto item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getDescProduto());
			}
		};
		comboBoxProduto.setCellFactory(factory);
		comboBoxProduto.setButtonCell(factory.call(null));
	}
	
	private void initializeComboBoxFornecedor() {
		Callback<ListView<Fornecedor>, ListCell<Fornecedor>> factory = lv -> new ListCell<Fornecedor>() {
			@Override
			protected void updateItem(Fornecedor item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : String.valueOf(item.getCodFornecedor()));
			}
		};
		comboBoxFornecedor.setCellFactory(factory);
		comboBoxFornecedor.setButtonCell(factory.call(null));
	}
}
