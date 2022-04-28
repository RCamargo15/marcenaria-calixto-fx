package guiNotasCompras;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import Db.DbException;
import application.Main;
import entities.services.EntradaProdutoService;
import entities.services.EstoqueService;
import entities.services.FornecedorService;
import entities.services.NotasComprasService;
import entities.services.ProdutoService;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import marcenaria.entities.EntradaProduto;
import marcenaria.entities.Fornecedor;
import marcenaria.entities.NotasCompras;
import model.exceptions.ValidationException;

public class EditarNotaCompraController implements Initializable, DataChangeListener {

	private NotasCompras notasCompras;
	private NotasComprasService notasComprasService;
	private FornecedorService fornecedorService;
	private ProdutoService produtoService;
	private EstoqueService estoqueService;
	private EntradaProdutoService entradaProdutoService;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtNumeroNF;
	@FXML
	private Label errorNumeroNF;

	@FXML
	private TextField txtChaveNF;
	@FXML
	private Label errorChaveNF;

	@FXML
	private ComboBox<Fornecedor> cbCodFornecedor;
	@FXML
	private Label errorCodFornecedor;

	@FXML
	private DatePicker dpDataEmissao;
	@FXML
	private Label erroDataEmissao;

	@FXML
	private DatePicker dpDataEntrada;
	@FXML
	private Label erroDataEntrada;

	@FXML
	private TextField txtObs;

	@FXML
	private TableColumn<NotasCompras, NotasCompras> tableColumnEditar;

	@FXML
	private TableColumn<NotasCompras, NotasCompras> tableColumnRemover;

	@FXML
	private TableView<NotasCompras> tableViewNotasCompras;

	@FXML
	private TableColumn<NotasCompras, String> tableColumnDescProduto;

	@FXML
	private TableColumn<NotasCompras, Double> tableColumnValorUnit;

	@FXML
	private TableColumn<NotasCompras, Double> tableColumnValorTotal;

	@FXML
	private TableColumn<NotasCompras, Integer> tableColumnQuantidade;

	@FXML
	private TextField txtValorTotalNotaFiscal;

	@FXML
	private Button btInserirNota;

	@FXML
	private Button btCancelar;

	private ObservableList<Fornecedor> obsListFornecedor;

	private ObservableList<NotasCompras> updateTableView;

	public void setNotasCompras(NotasCompras notasCompras) {
		this.notasCompras = notasCompras;
	}

	public void setServices(NotasComprasService notasComprasService, FornecedorService fornecedorService,
			ProdutoService produtoService, EstoqueService estoqueService, EntradaProdutoService entradaProdutoService) {
		this.notasComprasService = notasComprasService;
		this.fornecedorService = fornecedorService;
		this.produtoService = produtoService;
		this.estoqueService = estoqueService;
		this.entradaProdutoService = entradaProdutoService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	@Override
	public void onDataChanged() {
		updateNotasComprasData();
	}

	private void notificarDataChangeListener() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	public NotasCompras getNotasComprasData() {

		NotasCompras obj = new NotasCompras();
		obj.setNumeroNF(txtNumeroNF.getText());
		obj.setCodFornecedor(cbCodFornecedor.getValue());
		Instant instant = Instant.from(dpDataEmissao.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setDataEmissao(Date.from(instant));
		Instant instant2 = Instant.from(dpDataEntrada.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setDataEntrada(Date.from(instant2));
		obj.setChaveNF(txtChaveNF.getText());
		obj.setObs(txtObs.getText());

		return obj;
	}

	@FXML
	public void onBtAtualizarAction(ActionEvent event) {
		if (notasCompras == null) {
			throw new IllegalStateException("Orcamento null");
		}
		if (notasComprasService == null) {
			throw new IllegalStateException("Orcamento null");
		}
		String nf2 = notasCompras.getNumeroNF();
		List<NotasCompras> listaAtualizacao = notasComprasService.findByNumeroNFList(nf2);
		Map<String, String> errors = validateExceptions();

		if (errors.size() > 0) {
			setErrorMessages(errors);
		} else {
			try {
				for (NotasCompras notas : listaAtualizacao) {
					notas = getNotasComprasData();
					notas.setValorTotalNota(Double.parseDouble(Utils.getValorTotalNota(txtValorTotalNotaFiscal.getText())));
					notasComprasService.atualizarDados(notas, txtNumeroNF.getText(), nf2);
				}

				notificarDataChangeListener();
				Utils.currentStage(event).close();
			} catch (DbException e) {
				e.getMessage();
				e.printStackTrace();
			} catch (ValidationException e) {
				setErrorMessages(e.getErrors());
			}
		}
	}

	private Map<String, String> validateExceptions() {
		ValidationException exception = new ValidationException("Erro de validação");

		if (txtNumeroNF.getText() == null || txtNumeroNF.getText().trim().equals("")) {
			exception.addError("NumeroNF", "Insira o número da nota fiscal");
		}

		if (dpDataEmissao.getValue() == null) {
			exception.addError("DataEmissao", "É necessário inserir a data de emissão da nota fiscal");
		}

		if (dpDataEntrada.getValue() == null) {
			exception.addError("DataEntrada",
					"É necessário inserir a data em que está sendo cadastrada essa nota fiscal");
		}

		if (cbCodFornecedor.getValue() == null) {
			exception.addError("Fornecedor", "Selecione o fornecedor");
		}
		
		if(txtChaveNF.getText() == null || txtChaveNF.getText().trim().equals("")) {
			exception.addError("chave", "Informe a chave da Nota Fiscal");
		}
		setErrorMessages(exception.getErrors());

		return exception.getErrors();
	}

	public void updateNotasComprasData() {

		if (notasComprasService == null) {
			throw new IllegalStateException("notasComprasService null");
		}

		List<NotasCompras> listNotasFiscais = notasComprasService.findByNumeroNFList(notasCompras.getNumeroNF());
		updateTableView = FXCollections.observableArrayList(listNotasFiscais);
		tableViewNotasCompras.setItems(updateTableView);
		initEditButtons();
		initRemoveButtons();
		double valorTotal = 0;

		txtNumeroNF.setText(String.valueOf(notasCompras.getNumeroNF()));
		txtChaveNF.setText(String.valueOf(notasCompras.getChaveNF()));

		if (notasCompras.getObs() != null) {
			txtObs.setText(String.valueOf(notasCompras.getObs()));
		}

		if (notasCompras.getCodFornecedor() == null) {
			cbCodFornecedor.getSelectionModel().selectFirst();
		} else {
			cbCodFornecedor.setValue(notasCompras.getCodFornecedor());
		}

		if (notasCompras.getDataEmissao() != null) {
			dpDataEmissao
					.setValue(LocalDate.ofInstant(notasCompras.getDataEmissao().toInstant(), ZoneId.systemDefault()));
		}

		if (notasCompras.getDataEntrada() != null) {
			dpDataEntrada
					.setValue(LocalDate.ofInstant(notasCompras.getDataEntrada().toInstant(), ZoneId.systemDefault()));
		}

		for (NotasCompras orc : listNotasFiscais) {
			double valorMoment = orc.getValorUnit() * orc.getQuantidade();
			valorTotal = valorTotal + valorMoment;
		}

		txtValorTotalNotaFiscal.setText("R$ " + String.valueOf(valorTotal));
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		errorNumeroNF.setText(fields.contains("NumeroNF") ? errors.get("NumeroNF") : "");
		erroDataEmissao.setText(fields.contains("DataEmissao") ? errors.get("DataEmissao") : "");
		erroDataEntrada.setText(fields.contains("DataEntrada") ? errors.get("DataEntrada") : "");
		errorCodFornecedor.setText(fields.contains("Fornecedor") ? errors.get("Fornecedor") : "");
		errorChaveNF.setText(fields.contains("chave") ? errors.get("chave") : "");

	}

	public void loadFornecedores() {
		if (fornecedorService == null) {
			throw new IllegalStateException("Fornecedor Service null");
		}

		List<Fornecedor> list = fornecedorService.findAll();
		obsListFornecedor = FXCollections.observableArrayList(list);
		cbCodFornecedor.setItems(obsListFornecedor);
	}

	private void initializeComboBoxFornecedor() {
		Callback<ListView<Fornecedor>, ListCell<Fornecedor>> factory = lv -> new ListCell<Fornecedor>() {
			@Override
			protected void updateItem(Fornecedor item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeFantasia());
			}
		};
		cbCodFornecedor.setCellFactory(factory);
		cbCodFornecedor.setButtonCell(factory.call(null));
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeComboBoxFornecedor();

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewNotasCompras.prefHeightProperty().bind(stage.heightProperty());
		tableViewNotasCompras.prefWidthProperty().bind(stage.widthProperty());

		tableColumnDescProduto.setCellValueFactory(new PropertyValueFactory<>("codProduto"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tableColumnValorUnit.setCellValueFactory(new PropertyValueFactory<>("valorUnit"));
		tableColumnValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));

		Constraints.setTextFieldInteger(txtNumeroNF);
		Constraints.setTextFieldInteger(txtChaveNF);

		Utils.formatTableColumnDouble(tableColumnValorUnit, 2);
		Utils.formatTableColumnDouble(tableColumnValorTotal, 2);
		Utils.formatDatePicker(dpDataEmissao, "dd/MM/yyyy");
		Utils.formatDatePicker(dpDataEntrada, "dd/MM/yyyy");
	}

	private void createEditarProdutoOrcamentoForm(NotasCompras obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			EditarProdQuantidadeNotaCompraController editProdController = loader.getController();
			editProdController.setNotasCompras(obj);
			editProdController.setServices(notasComprasService, produtoService, estoqueService, entradaProdutoService);
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
		tableColumnEditar.setCellFactory(param -> new TableCell<NotasCompras, NotasCompras>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(NotasCompras obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createEditarProdutoOrcamentoForm(obj, Utils.currentStage(event),
						"/guiNotasCompras/EditarProdQuantidadeNotaCompra.fxml"));

			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<NotasCompras, NotasCompras>() {
			private final Button button = new Button("Deletar");

			@Override
			protected void updateItem(NotasCompras obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction((event -> excluirNotasCompras(obj)));

			}
		});
	}

	private void excluirNotasCompras(NotasCompras obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("EXCLUIR ORÇAMENTO",
				"Tem certeza que deseja remover esse orçamento?");
		if (result.get() == ButtonType.OK) {
			if (notasComprasService == null) {
				throw new IllegalStateException("Orcamento vazio");
			}

			List<EntradaProduto> listaEntrada = entradaProdutoService.findAll();
			try {
				notasComprasService.removerProduto(obj);
				updateNotasComprasData();
				for (EntradaProduto ep : listaEntrada) {
					if ((ep.getCodProduto().equals(obj.getCodProduto())
							&& (ep.getNumeroNF().getNumeroNF().equals(obj.getNumeroNF())))) {
						entradaProdutoService.removerEntrada(ep);
					}
				}

			} catch (DbException e) {
				Alerts.showAlert("Erro ao excluir orçamento", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
