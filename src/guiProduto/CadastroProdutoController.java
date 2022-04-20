package guiProduto;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import marcenaria.entities.Estoque;
import marcenaria.entities.Produto;
import model.exceptions.ValidationException;

public class CadastroProdutoController implements Initializable {

	private Produto produto;
	private EstoqueService estoqueService;
	private ProdutoService produtoService;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtCodProduto;

	@FXML
	private TextField txtDescProduto;
	@FXML
	private Label errorDescProduto;

	@FXML
	private TextField txtEstoqueAtual;
	@FXML
	private Label errorEstoqueAtual;

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

	public void setProdutoServices(ProdutoService produtoService, EstoqueService estoqueService) {
		this.produtoService = produtoService;
		this.estoqueService = estoqueService;
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
			Estoque estoque = new Estoque();
			produto = getProdutoData();
			estoque.setCodProduto(produto);
			estoque.setEstoqueAtual(Integer.parseInt(txtEstoqueAtual.getText()));
			estoque.setEstoqueMinimo(Integer.parseInt(txtEstoqueMinimo.getText()));
			produtoService.saveOrUpdate(produto);
			estoqueService.saveOrUpdate(estoque);
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
		obj.setDescProduto(txtDescProduto.getText());

		if (txtDescProduto.getText() == null || txtDescProduto.getText().trim().equals("")) {
			exception.addError("Desc", "Insira uma descrição para esse produto");
		}

		if (txtPrecoUnit.getText() == null || txtPrecoUnit.getText().trim().equals("")) {
			exception.addError("Preco", "Insira um valor a ser cobrado por esse produto");
		} else {
			obj.setPrecoUnit(Double.parseDouble(txtPrecoUnit.getText()));
		}
		
		if(txtEstoqueAtual.getText() == null || txtEstoqueAtual.getText().trim().equals("")) {
			exception.addError("estoqueAtual", "Caso possua esse produto em estoque, informe a quantidade. Caso contrário, quantidade deve ser 0");
		}
		if(txtEstoqueMinimo.getText() == null || txtEstoqueMinimo.getText().trim().equals("")) {
			exception.addError("estoqueMin", "Estabeleça uma quantidade mínima desse produto no estoque para controle");
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
		errorEstoqueAtual.setText(fields.contains("estoqueAtual") ? errors.get("estoqueAtual") : "");
		errorEstoqueMinimo.setText(fields.contains("estoqueMin") ? errors.get("estoqueMin") : "");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldDouble(txtPrecoUnit);
		Constraints.setTextFieldInteger(txtEstoqueAtual);
		Constraints.setTextFieldInteger(txtEstoqueMinimo);
	}
}
