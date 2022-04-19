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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import marcenaria.entities.Estoque;
import marcenaria.entities.Funcionario;
import marcenaria.entities.SaidaProduto;
import model.exceptions.ValidationException;

public class SaidaDeProdutoEstoqueController implements Initializable {

	private SaidaProduto saidaProduto;

	private SaidaProdutoService saidaProdutoService;

	private EstoqueService estoqueService;

	private FuncionarioService funcionarioService;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtCodSaida;

	@FXML
	private ComboBox<Estoque> comboBoxIdEstoque;

	@FXML
	private ComboBox<Estoque> comboBoxProdutoEstoque;

	@FXML
	private DatePicker dpDataSaida;
	@FXML
	private Label errorDataSaida;

	@FXML
	private TextField txtQuantidade;

	@FXML
	private ComboBox<Funcionario> comboBoxFuncionario;
	@FXML
	private Label errorFuncionario;

	@FXML
	private Button btRegistrarSaida;

	@FXML
	private Button btCancelar;

	@FXML
	private Button btInserirProduto;

	@FXML
	private TableView<SaidaProduto> tableViewRetiradaProduto;

	@FXML
	private TableColumn<SaidaProduto, Integer> tableColumnQuantidade;

	@FXML
	private TableColumn<Estoque, Estoque> tableColumnProduto;

	private ObservableList<Estoque> obsListEstoque;

	private ObservableList<SaidaProduto> obsListRetiradaProdutoEstoque;

	private ObservableList<Funcionario> obsListFuncionario;

	private List<SaidaProduto> listaEstoque = new ArrayList<>();
	private List<SaidaProduto> listaParaCadastro = new ArrayList<>();

	public void setSaidaProduto(SaidaProduto saidaProduto) {
		this.saidaProduto = saidaProduto;
	}

	public void setServices(SaidaProdutoService saidaProdutoService, EstoqueService estoqueService,
			FuncionarioService funcionarioService) {
		this.saidaProdutoService = saidaProdutoService;
		this.estoqueService = estoqueService;
		this.funcionarioService = funcionarioService;
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

		Map<String, String> errors = ValidateException();

		if (errors.size() > 0) {
			setErrorMessages(errors);
		} else {
			listaParaCadastro.addAll(listaEstoque);
			try {
				for (SaidaProduto saida : listaParaCadastro) {
					saidaProdutoService.saveOrUpdate(saida);
					notificarDataChangeListener();
				}
				Utils.currentStage(event).close();
			}

			catch (DbException e) {
				Alerts.showAlert("Erro ao registrar saída do produto", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

	private void notificarDataChangeListener() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	private SaidaProduto getSaidaProdutoData() {

		SaidaProduto obj = new SaidaProduto();
		obj.setIdEstoque(comboBoxProdutoEstoque.getValue());
		obj.setCodProduto(comboBoxProdutoEstoque.getValue());
		Instant instant = Instant.from(dpDataSaida.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setDataSaida(Date.from(instant));
		obj.setQuantidade(Utils.tryParseToInt(txtQuantidade.getText()));
		obj.setRespSaida(comboBoxFuncionario.getValue());
		return obj;
	}

	public Map<String, String> ValidateException() {
		ValidationException exception = new ValidationException("Erro de validação");

		if (comboBoxFuncionario.getValue() == null) {
			exception.addError("func", "Selecione o funcionário que irá receber esse material");
		}

		if (dpDataSaida.getValue() == null) {
			exception.addError("dataSaida", "Insira a data de retirada desse produto");
		}

		if (comboBoxFuncionario.getValue() == null) {
			exception.addError("func", "Selecione o funcionário que irá receber esse material");
		}

		setErrorMessages(exception.getErrors());
		return exception.getErrors();

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

	@FXML
	public void onBtInserirAction() {
		SaidaProduto saidaProduto2 = new SaidaProduto();
		saidaProduto2 = getSaidaProdutoData();
		listaEstoque.add(saidaProduto2);
		loadEstoque();

		txtQuantidade.setText("");
		comboBoxProdutoEstoque.setValue(null);

		obsListRetiradaProdutoEstoque = FXCollections.observableArrayList(listaEstoque);
		tableViewRetiradaProduto.setItems(obsListRetiradaProdutoEstoque);
	}

	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeComboBoxIdEstoque();
		initializeComboBoxCodProduto();
		initializeComboBoxFuncionario();

		tableColumnProduto.setCellValueFactory(new PropertyValueFactory<>("codProduto"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		Utils.formatDatePicker(dpDataSaida, "dd/MM/yyyy");
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		errorDataSaida.setText(fields.contains("dataSaida") ? errors.get("dataSaida") : "");
		errorFuncionario.setText(fields.contains("func") ? errors.get("func") : "");
	}

	public void loadEstoque() {
		if (estoqueService == null) {
			throw new IllegalStateException("EstoqueService is null");
		}

		List<Estoque> list = estoqueService.findAll();
		for (SaidaProduto saida : listaEstoque) {
			if (list.contains(saida.getCodProduto())) {
				list.remove(saida.getCodProduto());
			}
		}
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

	private void initializeComboBoxIdEstoque() {
		Callback<ListView<Estoque>, ListCell<Estoque>> factory = lv -> new ListCell<Estoque>() {
			@Override
			protected void updateItem(Estoque item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : String.valueOf(item.getId()));
			}
		};
		comboBoxIdEstoque.setCellFactory(factory);
		comboBoxIdEstoque.setButtonCell(factory.call(null));
	}

	private void initializeComboBoxCodProduto() {
		Callback<ListView<Estoque>, ListCell<Estoque>> factory = lv -> new ListCell<Estoque>() {
			@Override
			protected void updateItem(Estoque item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getCodProduto().getDescProduto());
			}
		};
		comboBoxProdutoEstoque.setCellFactory(factory);
		comboBoxProdutoEstoque.setButtonCell(factory.call(null));
	}

	private void initializeComboBoxFuncionario() {
		Callback<ListView<Funcionario>, ListCell<Funcionario>> factory = lv -> new ListCell<Funcionario>() {
			@Override
			protected void updateItem(Funcionario item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		comboBoxFuncionario.setCellFactory(factory);
		comboBoxFuncionario.setButtonCell(factory.call(null));
	}
}
