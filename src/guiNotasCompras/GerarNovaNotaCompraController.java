package guiNotasCompras;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import Db.DbException;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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
import javafx.util.Callback;
import marcenaria.entities.EntradaProduto;
import marcenaria.entities.Estoque;
import marcenaria.entities.Fornecedor;
import marcenaria.entities.NotasCompras;
import marcenaria.entities.Produto;
import marcenaria.entities.auxiliar.ProdutoOrcamento;
import model.exceptions.ValidationException;

public class GerarNovaNotaCompraController implements Initializable {

	private NotasCompras notasCompras;
	private NotasComprasService notasComprasService;
	private FornecedorService fornecedorService;
	private ProdutoService produtoService;
	private EntradaProdutoService entradaProdutoService;
	private EstoqueService estoqueService;

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
	private ComboBox<ProdutoOrcamento> cbCodProduto;
	@FXML
	private Label erroProduto;

	@FXML
	private TextField txtQuantidade;
	@FXML
	private Label erroQuantidade;
	
	@FXML
	private TextField txtValorUnit;
	@FXML
	private Label erroValorUnit;
	
	@FXML
	private TextField txtValorDesconto;
	@FXML
	private Label erroValorDesconto;

	@FXML
	private TableColumn<ProdutoOrcamento, ProdutoOrcamento> tableColumnEditar;

	@FXML
	private Button btInserirProduto;

	@FXML
	private TableView<ProdutoOrcamento> tableViewNotasCompras;

	@FXML
	private TableColumn<ProdutoOrcamento, String> tableColumnDescProduto;

	@FXML
	private TableColumn<ProdutoOrcamento, Double> tableColumnValorUnit;
	
	@FXML
	private TableColumn<ProdutoOrcamento, Double> tableColumnValorDesconto;
	
	@FXML
	private TableColumn<ProdutoOrcamento, Double> tableColumnValorTotal;
	
	@FXML
	private TableColumn<ProdutoOrcamento, Double> tableColumnValorTotalFinal;

	@FXML
	private TableColumn<ProdutoOrcamento, Integer> tableColumnQuantidade;

	@FXML
	private TextField txtValorTotalNotaFiscal;

	@FXML
	private Button btInserirNota;

	@FXML
	private Button btCancelar;

	private ObservableList<Fornecedor> obsListFornecedor;

	private ObservableList<ProdutoOrcamento> obsListProduto;

	private ObservableList<ProdutoOrcamento> updateTableView;

	private List<ProdutoOrcamento> prodOrcamento = new ArrayList<>();

	private List<NotasCompras> listaParaCadastro = new ArrayList<>();

	private List<NotasCompras> listaParaInserir = new ArrayList<>();

	public void setNotasCompras(NotasCompras notasCompras) {
		this.notasCompras = notasCompras;
	}

	public void setServices(NotasComprasService notasComprasService, FornecedorService fornecedorService,
			ProdutoService produtoService, EntradaProdutoService entradaProdutoService, EstoqueService estoqueService) {
		this.notasComprasService = notasComprasService;
		this.fornecedorService = fornecedorService;
		this.produtoService = produtoService;
		this.entradaProdutoService = entradaProdutoService;
		this.estoqueService = estoqueService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	private void notificarDataChangeListener() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	private NotasCompras getNotasComprasData() {

		NotasCompras obj = new NotasCompras();
		Produto prod = new Produto();
		
		double valorTotal = 1;
		double valorUnit = 1;
		double valorTotalNota = 1;
		double valorDesconto = 1;

		obj.setNumeroNF(txtNumeroNF.getText());
		obj.setCodFornecedor(cbCodFornecedor.getValue());
		obj.setCodProduto(prod);
		obj.setQuantidade(Integer.parseInt(txtQuantidade.getText()));
		obj.setValorUnit(valorUnit);
		obj.setValorTotal(valorTotal);
		obj.setValorDesconto(valorDesconto);
		obj.setValorTotalNota(valorTotalNota);
		Instant instant = Instant.from(dpDataEmissao.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setDataEmissao(Date.from(instant));
		Instant instant2 = Instant.from(dpDataEntrada.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setDataEntrada(Date.from(instant2));
		obj.setChaveNF(txtChaveNF.getText());
		obj.setObs(txtObs.getText());

		return obj;
	}

	public ProdutoOrcamento criarProdutoOrcamento() {

		ValidationException exception = new ValidationException("Erro de validação");

		Double valorDesconto = 0.0;
		ProdutoOrcamento produtoTemp = new ProdutoOrcamento();
		produtoTemp = cbCodProduto.getValue();
		produtoTemp.setCodProduto(cbCodProduto.getValue().getCodProduto());
		produtoTemp.setDescProduto(cbCodProduto.getValue().getDescProduto());
		produtoTemp.setPrecoProd(Double.parseDouble(txtValorUnit.getText()));
		produtoTemp.setQuantidade(Integer.parseInt(txtQuantidade.getText()));
		if(txtValorDesconto.getText().equals("") || txtValorDesconto.getText() == null) {
			produtoTemp.setValorDesconto(0.0);
		} else {
			produtoTemp.setValorDesconto(Double.parseDouble(txtValorDesconto.getText()));
		}
		
		Double valorTotal = Integer.parseInt(txtQuantidade.getText()) * Double.parseDouble(txtValorUnit.getText());
		
		if(txtValorDesconto.getText() == null || txtValorDesconto.getText().equals("")) {
			valorDesconto = 0.0;
		} else {
			valorDesconto = Double.parseDouble(txtValorDesconto.getText());
		}
		
		produtoTemp.setValorTotal(valorTotal);
		produtoTemp.setValorTotalFinal(valorTotal - valorDesconto);

		prodOrcamento.add(produtoTemp);
		updateTableViewNotasCompras();

		ObservableList<ProdutoOrcamento> obsList = cbCodProduto.getItems();
		for (ProdutoOrcamento prod : prodOrcamento) {
			if (obsList.contains(prod)) {
				obsList.remove(prod);
			}
		}
		cbCodProduto.setItems(obsList);

		if (cbCodProduto.getValue() == null) {
			exception.addError("Produto", "Escolha um produto da lista para cadastrar na NF");
		}

		if (txtQuantidade.getText() == null || txtQuantidade.getText().trim().equals("")) {
			exception.addError("Qtd", "Insira a quantidade de material que está sendo comprado");
		}
		return produtoTemp;
	}

	public void updateTableViewNotasCompras() {
		List<ProdutoOrcamento> list = prodOrcamento;
		updateTableView = FXCollections.observableArrayList(list);
		tableViewNotasCompras.setItems(updateTableView);
	}

	@FXML
	public List<NotasCompras> onBtInserirAction() {
		
		Map<String, String> errors = validateExceptions();
		
			if(errors.size() > 0) {
				Alerts.showAlert("Erro ao inserir produto", null, "Preencha as informações da nota fiscal antes de cadastrar os produtos", AlertType.INFORMATION);
			}
			else {
			double valorTotalNota = 0;
			double valorTotal = 0;
			double valorTotalFinal = 0;
			double valorDesconto = 0;
			NotasCompras notaCompra = getNotasComprasData();
			ProdutoOrcamento produtoTemp = criarProdutoOrcamento();

			Produto produto = produtoService.findByCodProduto(produtoTemp.getCodProduto());

			notaCompra.setCodProduto(produto);
			notaCompra.setValorUnit(Double.parseDouble(txtValorUnit.getText()));
			
			if(txtValorDesconto.getText() == null || txtValorDesconto.getText().equals("")) {
				valorDesconto = 0.0;
				notaCompra.setValorDesconto(0.0);
			}
			else {
				valorDesconto = Double.parseDouble(txtValorDesconto.getText());
				notaCompra.setValorDesconto(Double.parseDouble(txtValorDesconto.getText()));
			}
			
			valorTotal = Double.parseDouble(txtValorUnit.getText()) * Integer.parseInt(txtQuantidade.getText());
			valorTotalFinal = valorTotal - valorDesconto;
			
			for (ProdutoOrcamento prod : prodOrcamento) {
				double valorMoment = prod.getPrecoProd() * prod.getQuantidade() - prod.getValorDesconto();
				valorTotalNota = valorTotalNota + valorMoment;
			}
			notaCompra.setValorTotal(valorTotalFinal);
			txtValorTotalNotaFiscal.setText("R$ " + String.format("%.2f",valorTotalNota));
			listaParaCadastro.add(notaCompra);
			
			txtQuantidade.setText("");
			txtValorUnit.setText("");
			txtValorDesconto.setText("");
			}
		
		return listaParaCadastro;
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
			exception.addError("DataEntrada", "É necessário inserir a data de entrada no sistema dessa nota fiscal");
		}

		if (cbCodFornecedor.getValue() == null) {
			exception.addError("Fornecedor", "Selecione o fornecedor");
		}
		if (cbCodProduto.getValue() == null) {
			exception.addError("Produto", "Escolha um produto cadastrado em seu sistema");
		}
		if (txtQuantidade.getText() == null || txtQuantidade.getText().trim().equals("")) {
			exception.addError("Qtd", "Insira a quantidade");
		}
		if (txtValorUnit.getText() == null || txtQuantidade.getText().trim().equals("")) {
			exception.addError("ValorUnit", "Insira o valor unitário conforme nota fiscal");
		}

		setErrorMessages(exception.getErrors());

		return exception.getErrors();
	}

	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		if (notasCompras == null) {
			throw new IllegalStateException("notasCompras null");
		}
		if (notasComprasService == null) {
			throw new IllegalStateException("notasComprasService null");
		}
		List<Estoque> listaEstoque = estoqueService.findAll();
		listaParaInserir.addAll(listaParaCadastro);
		try {
			for (NotasCompras nf : listaParaInserir) {
				nf.setValorTotalNota(Double.parseDouble(Utils.getValorTotalNota(txtValorTotalNotaFiscal.getText())));
				EntradaProduto entradaProduto = new EntradaProduto();
				Estoque estoque = new Estoque();
				int qtdEstoque = 0;
				entradaProduto.setNumeroNF(nf);
				entradaProduto.setCodProduto(nf.getCodProduto());
				entradaProduto.setDataEntrada(nf.getDataEntrada());
				entradaProduto.setQuantidade(nf);
				entradaProduto.setValorUnit(nf);
				entradaProduto.setValorTotal(nf);
				entradaProduto.setValorTotalNota(nf);
				estoque = estoqueService.findByCodProduto(entradaProduto.getCodProduto().getCodProduto());
				qtdEstoque = entradaProduto.getQuantidade().getQuantidade();
				for (Estoque stock : listaEstoque) {
					if (stock.getId().equals(estoque.getId())) {
						stock.setEstoqueAtual(stock.getEstoqueAtual() + qtdEstoque);
						estoqueService.saveOrUpdate(stock);
					}
				}
				notasComprasService.saveOrUpdate(nf);
				entradaProdutoService.saveOrUpdate(entradaProduto);
			}
				notificarDataChangeListener();
				Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Erro ao cadastrar nota fiscal", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		errorNumeroNF.setText(fields.contains("NumeroNF") ? errors.get("NumeroNF") : "");
		erroDataEmissao.setText(fields.contains("DataEmissao") ? errors.get("DataEmissao") : "");
		erroDataEntrada.setText(fields.contains("DataEntrada") ? errors.get("DataEntrada") : "");
		erroQuantidade.setText(fields.contains("Qtd") ? errors.get("Qtd") : "");
		errorCodFornecedor.setText(fields.contains("Fornecedor") ? errors.get("Fornecedor") : "");
		erroProduto.setText(fields.contains("Produto") ? errors.get("Produto") : "");
		erroValorUnit.setText(fields.contains("ValorUnit")? errors.get("ValorUnit")  : "" );
	}

	public void loadFornecedores() {
		if (fornecedorService == null) {
			throw new IllegalStateException("Fornecedor Service null");
		}

		List<Fornecedor> list = fornecedorService.findAll();
		obsListFornecedor = FXCollections.observableArrayList(list);
		cbCodFornecedor.setItems(obsListFornecedor);
	}

	public void loadProdutos() {
		if (produtoService == null) {
			throw new IllegalStateException("Produto service null");
		}

		List<Produto> list = produtoService.findAll();
		List<ProdutoOrcamento> list2 = new ArrayList<>();

		for (Produto prod : list) {
			ProdutoOrcamento produto = new ProdutoOrcamento();
			produto.setCodProduto(prod.getCodProduto());
			produto.setDescProduto(prod.getDescProduto());
			produto.setPrecoProd(prod.getPrecoUnit());
			list2.add(produto);
		}

		Comparator<ProdutoOrcamento> compare = Comparator.comparing(ProdutoOrcamento::getDescProduto);

		obsListProduto = FXCollections.observableArrayList(list2);
		FXCollections.sort(obsListProduto, compare);

		cbCodProduto.setItems(obsListProduto);
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

	private void initializeComboBoxProduto() {
		Callback<ListView<ProdutoOrcamento>, ListCell<ProdutoOrcamento>> factory = lv -> new ListCell<ProdutoOrcamento>() {
			@Override
			protected void updateItem(ProdutoOrcamento item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getDescProduto());
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
		initializeComboBoxFornecedor();
		initializeComboBoxProduto();
		initRemoverButtons();
		initializeTables();
	}

	public void initializeTables() {
		tableColumnDescProduto.setCellValueFactory(new PropertyValueFactory<>("descProduto"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tableColumnValorUnit.setCellValueFactory(new PropertyValueFactory<>("precoProd"));
		tableColumnValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
		tableColumnValorTotalFinal.setCellValueFactory(new PropertyValueFactory<>("valorTotalFinal"));
		tableColumnValorDesconto.setCellValueFactory(new PropertyValueFactory<>("valorDesconto"));
		
		Constraints.setTextFieldInteger(txtNumeroNF);
		Constraints.setTextFieldInteger(txtChaveNF);
		Constraints.setTextFieldInteger(txtQuantidade);
		Constraints.setTextFieldDouble(txtValorUnit);
	
		Utils.formatTableColumnDouble(tableColumnValorUnit, 2);
		Utils.formatTableColumnDouble(tableColumnValorTotal, 2);
		Utils.formatTableColumnDouble(tableColumnValorTotalFinal, 2);
		Utils.formatTableColumnDouble(tableColumnValorDesconto, 2);
		Utils.formatDatePicker(dpDataEmissao, "dd/MM/yyyy");
		Utils.formatDatePicker(dpDataEntrada, "dd/MM/yyyy");
	}

	private void initRemoverButtons() {

		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<ProdutoOrcamento, ProdutoOrcamento>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(ProdutoOrcamento obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction((ActionEvent t) -> {
					prodOrcamento.remove(obj);
					updateTableViewNotasCompras();

					double valorTotal = Double.parseDouble(Utils.getValorTotalNota(txtValorTotalNotaFiscal.getText()));
					double valorRem = obj.getPrecoProd() * obj.getQuantidade() - obj.getValorDesconto();
					valorTotal = valorTotal - valorRem;
					txtValorTotalNotaFiscal.setText("R$ " + String.format("%.2f",valorTotal));
					double verify = Double.parseDouble(Utils.getValorTotalNota(txtValorTotalNotaFiscal.getText()));
					
					//Safety verification
					if(verify == 0.0 && !prodOrcamento.isEmpty()) {
						double valorTotal2 = 0;
						double valorTemp = 0;
						for(ProdutoOrcamento prod : prodOrcamento) {
							valorTemp = prod.getPrecoProd() * prod.getQuantidade() - prod.getValorDesconto();
							valorTotal2 = valorTotal2 + valorTemp;
						}
						txtValorTotalNotaFiscal.setText("R$ " + String.format("%.2f",valorTotal2));
					}

					ObservableList<ProdutoOrcamento> list = cbCodProduto.getItems();
					Comparator<ProdutoOrcamento> compare = Comparator.comparing(ProdutoOrcamento::getDescProduto);

					if (!list.contains(obj)) {
						list.add(obj);
					}
					FXCollections.sort(list, compare);
					cbCodProduto.setItems(list);
				});

			};
		});
	}
}
