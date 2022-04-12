package guiProduto;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import Db.DbException;
import entities.services.ProdutoService;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import marcenaria.entities.Produto;
import model.exceptions.ValidationException;

public class EditarProdutoController implements Initializable {

	private Produto produto;
	private ProdutoService produtoService;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtCodProduto;

	@FXML
	private TextField txtDescProduto;
	@FXML
	private Label errorDescProduto;
	
	@FXML
	private TextField txtEstoqueMinimo;
	@FXML
	private Label errorEstoqueMinimo;

	@FXML
	private TextField txtPrecoUnit;
	@FXML
	private Label errorPrecoUnit;

	@FXML
	private Button btCadastrar;

	@FXML
	private Button btCancelar;

	@FXML
	private GridPane gpProduto;

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public void setProdutoService(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		if (produto == null) {
			throw new IllegalStateException("Produto vazio");
		}
		if (produtoService == null) {
			throw new IllegalStateException("ProdutoService vazio");
		}
		try {
			produto = getProdutoData();
			produtoService.saveOrUpdate(produto);
			notificarDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Erro ao cadastrar produto", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notificarDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	private Produto getProdutoData() {

		ValidationException exception = new ValidationException("Erro de validação");
		Produto obj = new Produto();

		obj.setCodProduto(Utils.tryParseToInt(txtCodProduto.getText()));
		obj.setDescProduto(txtDescProduto.getText());
		obj.setPrecoUnit(Double.parseDouble(txtPrecoUnit.getText()));

		if (txtDescProduto.getText() == null || txtDescProduto.getText().trim().equals("")) {
			exception.addError("Desc", "Insira uma descrição para esse produto");
		}

		if (txtPrecoUnit.getText() == null || txtPrecoUnit.getText().trim().equals("")) {
			exception.addError("Preco", "Insira um valor a ser cobrado por esse produto");
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

	public void updateProdutoData() {
		if (produto == null) {
			throw new IllegalStateException("Produto vazio");
		}
		txtCodProduto.setText(String.valueOf(produto.getCodProduto()));
		txtCodProduto.setText(String.valueOf(produto.getCodProduto()));
		txtDescProduto.setText(produto.getDescProduto());
		txtPrecoUnit.setText(String.valueOf(produto.getPrecoUnit()));
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		errorDescProduto.setText(fields.contains("Desc") ? errors.get("Desc") : "");
		errorPrecoUnit.setText(fields.contains("Preco") ? errors.get("Preco") : "");

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldDouble(txtPrecoUnit);
	}
}
