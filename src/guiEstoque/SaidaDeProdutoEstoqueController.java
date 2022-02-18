package guiEstoque;

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
import entities.services.EstoqueService;
import entities.services.FuncionarioService;
import entities.services.ProdutoService;
import entities.services.SaidaProdutoService;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
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
import javafx.scene.control.TextField;
import marcenaria.entities.Estoque;
import marcenaria.entities.Funcionario;
import marcenaria.entities.Produto;
import marcenaria.entities.SaidaProduto;
import model.exceptions.ValidationException;

public class SaidaDeProdutoEstoqueController implements Initializable {

	private SaidaProduto saidaProduto;

	private SaidaProdutoService saidaProdutoService;

	private EstoqueService estoqueService;

	private FuncionarioService funcionarioService;

	private ProdutoService produtoService;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtCodSaida;

	@FXML
	private ComboBox<Estoque> comboBoxIdEstoque;

	@FXML
	private ComboBox<Estoque> comboBoxProdutoEstoque;
	@FXML
	private Label errorComboBoxProdutoEstoque;

	@FXML
	private DatePicker dpDataSaida;

	@FXML
	private TextField txtQuantidade;
	@FXML
	private Label errorQuantidade;

	@FXML
	private ComboBox<Funcionario> comboBoxFuncionario;

	@FXML
	private Button btRegistrarSaida;

	@FXML
	private Button btCancelar;

	private ObservableList<Estoque> obsListEstoque;

	private ObservableList<Funcionario> obsListFuncionario;

	public void setSaidaProduto(SaidaProduto saidaProduto) {
		this.saidaProduto = saidaProduto;
	}

	public void setSaidaProdutoServices(SaidaProdutoService saidaProdutoService, EstoqueService estoqueService,
			FuncionarioService funcionarioService, ProdutoService produtoService) {
		this.saidaProdutoService = saidaProdutoService;
		this.estoqueService = estoqueService;
		this.funcionarioService = funcionarioService;
		this.produtoService = produtoService;
	}

	public void subscribeDataListenerChange(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	public void onBtRegistrarSaidaAction(ActionEvent event) {
		if (saidaProduto == null) {
			throw new IllegalStateException("SaidaProduto null");
		}
		if (saidaProdutoService == null) {
			throw new IllegalStateException("SaidaProdutoService is null");
		}
		try {
			saidaProduto = getSaidaProdutoData();
			saidaProdutoService.saveOrUpdate(saidaProduto);
			notificarDataChangeListener();
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("Erro ao cadastrar estoque", null, e.getMessage(), AlertType.ERROR);
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
	}

	private void notificarDataChangeListener() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	private SaidaProduto getSaidaProdutoData() {
		ValidationException exception = new ValidationException("Erro de validação");

		SaidaProduto obj = new SaidaProduto();

		obj.setCodSaida(Utils.tryParseToInt(txtCodSaida.getText()));
		obj.setIdEstoque(comboBoxIdEstoque.getValue());
		obj.setCodProduto(comboBoxProdutoEstoque.getValue());

		if (dpDataSaida.getValue() == null) {
			exception.addError("dataSaida", "Insira uma data de retirada desse produto");
		} else {
			Instant instant = Instant.from(dpDataSaida.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataSaida(Date.from(instant));
		}

		obj.setQuantidade(Utils.tryParseToInt(txtQuantidade.getText()));

		obj.setRespSaida(comboBoxFuncionario.getValue());

		return obj;
	}

	public void updateSaidaProdutoData() {
		if (saidaProduto == null) {
			throw new IllegalStateException("SaidaProduto null");
		}

		txtCodSaida.setText(String.valueOf(saidaProduto.getCodSaida()));

		if (saidaProduto.getIdEstoque() == null) {
			comboBoxIdEstoque.getSelectionModel().selectFirst();
		} else {
			comboBoxIdEstoque.setValue(saidaProduto.getIdEstoque());
		}

		if (saidaProduto.getCodProduto() == null) {
			comboBoxProdutoEstoque.getSelectionModel().selectFirst();
		} else {
			comboBoxProdutoEstoque.setValue(saidaProduto.getCodProduto());
		}

		if (saidaProduto.getDataSaida() != null) {
			dpDataSaida.setValue(LocalDate.ofInstant(saidaProduto.getDataSaida().toInstant(), ZoneId.systemDefault()));
		}

		txtQuantidade.setText(String.valueOf(saidaProduto.getQuantidade()));

		if (saidaProduto.getRespSaida() == null) {
			comboBoxFuncionario.getSelectionModel().selectFirst();
		} else {
			comboBoxFuncionario.setValue(saidaProduto.getRespSaida());
		}

	}

	private void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
	}

	public void loadEstoque() {
		if (estoqueService == null) {
			throw new IllegalStateException("EstoqueService is null");
		}

		List<Estoque> list = estoqueService.findAll();
		obsListEstoque = FXCollections.observableArrayList(list);
		comboBoxIdEstoque.setItems(obsListEstoque);
		comboBoxProdutoEstoque.setItems(obsListEstoque);

	}

	public void loadFuncionario() {
		if (funcionarioService == null) {
			throw new IllegalStateException("FuncionarioService is null");
		}

		List<Funcionario> list = funcionarioService.findAll();
		obsListFuncionario = FXCollections.observableArrayList(list);
		comboBoxFuncionario.setItems(obsListFuncionario);
	}

}
