package guiEstoque;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import Db.DbException;
import entities.services.EstoqueService;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import marcenaria.entities.Estoque;
import marcenaria.entities.Produto;
import model.exceptions.ValidationException;

public class CadastroEstoqueController implements Initializable {

	private Estoque estoque;

	private EstoqueService estoqueService;

	private ProdutoService produtoService;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtCodEstoque;

	@FXML
	private ComboBox<Produto> comboBoxProduto;
	@FXML
	private Label errorComboBox;

	@FXML
	private TextField txtEstoqueAtual;
	@FXML
	private Label errorEstoqueAtual;

	@FXML
	private TextField txtEstoqueMinimo;
	@FXML
	private Label errorEstoqueMinimo;

	@FXML
	private Button btCadastrar;

	@FXML
	private Button btCancelar;

	@FXML
	private GridPane gpEstoque;

	private ObservableList<Produto> obsList;

	public void setEstoque(Estoque estoque) {
		this.estoque = estoque;
	}

	public void setServices(EstoqueService estoqueService, ProdutoService produtoService) {
		this.estoqueService = estoqueService;
		this.produtoService = produtoService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		if (estoque == null) {
			throw new IllegalStateException("Estoque vazio");
		}
		if (estoqueService == null) {
			throw new IllegalStateException("EstoqueService vazio");
		}
		try {
			estoque = getEstoqueData();
			estoqueService.saveOrUpdate(estoque);
			notificarDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Erro ao cadastrar estoque", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notificarDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	private Estoque getEstoqueData() {

		ValidationException exception = new ValidationException("Erro de validação");
		Estoque obj = new Estoque();

		obj.setId(Utils.tryParseToInt(txtCodEstoque.getText()));
		obj.setCodProduto(comboBoxProduto.getValue());
		obj.setEstoqueAtual(Integer.parseInt(txtEstoqueAtual.getText()));
		obj.setEstoqueMinimo(Integer.parseInt(txtEstoqueMinimo.getText()));

		if (txtEstoqueAtual.getText() == null || txtEstoqueAtual.getText().trim().equals("")) {
			exception.addError("EstoqueAtual", "Insira a quantidade atual de produtos antes de inserir no estoque");
		}

		if (txtEstoqueMinimo.getText() == null || txtEstoqueMinimo.getText().trim().equals("")) {
			exception.addError("EstoqueMinimo", "Estabeleça um valor mínimo de produtos em seu estoque");
		}

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void updateEstoqueData() {
		if (estoque == null) {
			throw new IllegalStateException("Estoque vazio");
		}
		txtCodEstoque.setText(String.valueOf(estoque.getId()));
		if (estoque.getCodProduto() == null) {
			comboBoxProduto.getSelectionModel().selectFirst();
		} else {
			comboBoxProduto.setValue(estoque.getCodProduto());
		}
		txtEstoqueAtual.setText(String.valueOf(estoque.getEstoqueAtual()));
		txtEstoqueMinimo.setText(String.valueOf(estoque.getEstoqueMinimo()));
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		errorEstoqueAtual.setText(fields.contains("EstoqueAtual") ? errors.get("EstoqueAtual") : "");
		errorEstoqueMinimo.setText(fields.contains("EstoqueMinimo") ? errors.get("EstoqueMinimo") : "");

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(txtEstoqueAtual);
		Constraints.setTextFieldInteger(txtEstoqueMinimo);
		initializeComboBoxProduto();
	}

	public void loadProdutos() {
		if (produtoService == null) {
			throw new IllegalStateException("ProdutoService null. Precisa ser injetado");
		}
		List<Produto> list = produtoService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxProduto.setItems(obsList);
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
}
