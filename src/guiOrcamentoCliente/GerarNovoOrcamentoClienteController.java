package guiOrcamentoCliente;

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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import marcenaria.entities.Cliente;
import marcenaria.entities.OrcamentoCliente;
import marcenaria.entities.Produto;
import marcenaria.entities.auxiliar.ProdutoOrcamento;
import model.exceptions.ValidationException;

public class GerarNovoOrcamentoClienteController implements Initializable {

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
	private Label errorNumOrcamento;

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
	private ComboBox<ProdutoOrcamento> cbCodProduto;
	@FXML
	private Label erroProduto;

	@FXML
	private TextField txtQuantidade;
	@FXML
	private Label erroQuantidade;

	@FXML
	private TableColumn<ProdutoOrcamento, ProdutoOrcamento> tableColumnEditar;

	@FXML
	private Button btInserirProduto;

	@FXML
	private TableView<ProdutoOrcamento> tableViewOrcamentoCliente;

	@FXML
	private TableColumn<ProdutoOrcamento, String> tableColumnDescProduto;

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

	private ObservableList<ProdutoOrcamento> obsListProduto;

	private ObservableList<ProdutoOrcamento> updateTableView;

	private List<ProdutoOrcamento> prodOrcamento = new ArrayList<>();

	private List<OrcamentoCliente> listaParaCadastro = new ArrayList<>();

	private List<OrcamentoCliente> listaParaInserir = new ArrayList<>();

	public void setOrcamentoCliente(OrcamentoCliente orcamentoCliente) {
		this.orcamentoCliente = orcamentoCliente;
	}

	public void setServices(OrcamentoClienteService orcamentoClienteService, ClienteService clienteService,
			ProdutoService produtoService) {
		this.orcamentoClienteService = orcamentoClienteService;
		this.clienteService = clienteService;
		this.produtoService = produtoService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	private void notificarDataChangeListener() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	private OrcamentoCliente getOrcamentoClienteData() {

		OrcamentoCliente obj = new OrcamentoCliente();
		Produto prod = new Produto();
		
		double valorTotal = 1;

		obj.setNumOrcamento(Integer.parseInt(txtNumOrcamento.getText()));
		obj.setCodCliente(cbCodCliente.getValue());
		obj.setTelefone(txtTelefoneCliente.getText());
		obj.setCelular(txtCelularCliente.getText());
		obj.setEmail(txtEmailCliente.getText());
		obj.setDescServico(txtDescricaoServico.getText());

		Instant instant = Instant.from(dpDataOrcamento.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setDataOrcamento(Date.from(instant));

		obj.setCodProduto(prod);
		obj.setQuantidade(Integer.parseInt(txtQuantidade.getText()));
		obj.setValor(prod);
		obj.setValorTotal(valorTotal);
		obj.setObs(txtObs.getText());

		return obj;
	}

	public ProdutoOrcamento criarProdutoOrcamento() {

		ValidationException exception = new ValidationException("Erro de validação");

		ProdutoOrcamento produtoTemp = new ProdutoOrcamento();
		produtoTemp = cbCodProduto.getValue();
		produtoTemp.setCodProduto(cbCodProduto.getValue().getCodProduto());
		produtoTemp.setDescProduto(cbCodProduto.getValue().getDescProduto());
		produtoTemp.setPrecoProd(cbCodProduto.getValue().getPrecoProd());
		produtoTemp.setQuantidade(Integer.parseInt(txtQuantidade.getText()));

		prodOrcamento.add(produtoTemp);
		updateTableViewOrcamentoCliente();

		ObservableList<ProdutoOrcamento> obsList = cbCodProduto.getItems();
		for (ProdutoOrcamento prod : prodOrcamento) {
			if (obsList.contains(prod)) {
				obsList.remove(prod);
			}
		}
		cbCodProduto.setItems(obsList);

		if (cbCodProduto.getValue() == null) {
			exception.addError("Produto", "Escolha um produto da lista para compor o orçamento");
		}

		if (txtQuantidade.getText() == null || txtQuantidade.getText().trim().equals("")) {
			exception.addError("Qtd", "Insira a quantidade de material a ser utilizado");
		}

		return produtoTemp;

	}

	public void updateTableViewOrcamentoCliente() {
		List<ProdutoOrcamento> list = prodOrcamento;
		updateTableView = FXCollections.observableArrayList(list);
		tableViewOrcamentoCliente.setItems(updateTableView);
	}

	@FXML
	public List<OrcamentoCliente> onBtInserirAction() {
		
		Map<String, String> errors = ValidateExceptions();
		
			if(errors.size() > 0) {
				Alerts.showAlert("Erro ao inserir produto", null, "Preencha os dados do cliente antes de inserir produtos no orçamento", AlertType.INFORMATION);
			}
			else {
			double valorTotal = 0;
			ProdutoOrcamento produtoTemp = criarProdutoOrcamento();
			OrcamentoCliente orcamento = getOrcamentoClienteData();
			

			Produto produto = produtoService.findByCodProduto(produtoTemp.getCodProduto());

			orcamento.setCodProduto(produto);
			orcamento.setValor(produto);
			orcamento.setValorTotal(valorTotal);
			listaParaCadastro.add(orcamento);
			
			for (ProdutoOrcamento prod : prodOrcamento) {
				double valorMoment = prod.getPrecoProd() * prod.getQuantidade();
				valorTotal = valorTotal + valorMoment;
			}
			
			txtValorTotalOrcamento.setText(String.valueOf(valorTotal));
			}
		
		return listaParaCadastro;
	}

	private Map<String, String> ValidateExceptions() {
		ValidationException exception = new ValidationException("Erro de validação");

		if (txtNumOrcamento.getText() == null || txtNumOrcamento.getText().trim().equals("")) {
			exception.addError("NumOrcamento", "Insira um número de orçamento");
		}

		if (dpDataOrcamento.getValue() == null) {
			exception.addError("DataOrcamento", "É necessário inserir uma data");
		}

		if ((txtTelefoneCliente.getText() == null || txtTelefoneCliente.getText().trim().equals(""))
				&& (txtCelularCliente.getText() == null || txtCelularCliente.getText().trim().equals(""))) {
			exception.addError("Contato", "Insira pelo menos um telefone para contato");
		}

		if (txtEmailCliente.getText() == null || txtEmailCliente.getText().trim().equals("")) {
			exception.addError("Email", "Insira o e-mail do cliente");
		}

		if (txtDescricaoServico.getText() == null || txtDescricaoServico.getText().trim().equals("")) {
			exception.addError("DescServico", "Descreva o serviço a ser realizado");
		}

		if (cbCodCliente.getValue() == null) {
			exception.addError("Cliente", "Escolha um cliente do seu cadastro");
		}
		if (cbCodProduto.getValue() == null) {
			exception.addError("Produto", "Escolha um produto");
		}
		if (txtQuantidade.getText() == null || txtQuantidade.getText().trim().equals("")) {
			exception.addError("Qtd", "Insira a quantidade");
		}

		setErrorMessages(exception.getErrors());

		return exception.getErrors();
	}

	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		if (orcamentoCliente == null) {
			throw new IllegalStateException("orcamentoCliente null");
		}
		if (orcamentoClienteService == null) {
			throw new IllegalStateException("orcamentoClienteService null");
		}
		Map<String, String> errors = ValidateExceptions();
		listaParaInserir.addAll(listaParaCadastro);
		try {
			if (errors.size() > 0) {
				setErrorMessages(errors);
				Alerts.showAlert("Erro ao cadastrar", null,
						"É necessário inserir todos os dados pendentes antes de cadastrar", AlertType.INFORMATION);
			} else {
				for (OrcamentoCliente cliente : listaParaInserir) {
					cliente.setValorTotal(Double.parseDouble(txtValorTotalOrcamento.getText()));
					orcamentoClienteService.saveOrUpdate(cliente);
				}
				notificarDataChangeListener();
				Utils.currentStage(event).close();
			}
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Erro ao gerar orçamento", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		errorNumOrcamento.setText(fields.contains("NumOrcamento") ? errors.get("NumOrcamento") : "");
		errorCelular.setText(fields.contains("Contato") ? errors.get("Contato") : "");
		erroQuantidade.setText(fields.contains("Qtd") ? errors.get("Qtd") : "");
		errorEmail.setText(fields.contains("Email") ? errors.get("Email") : "");
		errorDescServico.setText(fields.contains("DescServico") ? errors.get("DescServico") : "");
		erroDataOrcamento.setText(fields.contains("DataOrcamento") ? errors.get("DataOrcamento") : "");
		errorCodCliente.setText(fields.contains("Cliente") ? errors.get("Cliente") : "");
		erroProduto.setText(fields.contains("Produto") ? errors.get("Produto") : "");

	}

	public void loadClientes() {
		if (clienteService == null) {
			throw new IllegalStateException("Cliente Service null");
		}

		List<Cliente> list = clienteService.findAll();
		obsListCliente = FXCollections.observableArrayList(list);
		cbCodCliente.setItems(obsListCliente);
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
		initializeComboBoxCliente();
		initializeComboBoxProduto();
		initRemoverButtons();
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
					updateTableViewOrcamentoCliente();

					double valorTotal = Double.parseDouble(txtValorTotalOrcamento.getText());
					double valorRem = obj.getPrecoProd() * obj.getQuantidade();
					valorTotal = valorTotal - valorRem;
					txtValorTotalOrcamento.setText(String.valueOf(valorTotal));
					
					double verify = Double.parseDouble(txtValorTotalOrcamento.getText());
					
					//Safety verification
					if(verify == 0.0 && !prodOrcamento.isEmpty()) {
						double valorTotal2 = 0;
						double valorTemp = 0;
						for(ProdutoOrcamento prod : prodOrcamento) {
							valorTemp = prod.getPrecoProd() * prod.getQuantidade();
							valorTotal2 = valorTotal2 + valorTemp;
						}
						txtValorTotalOrcamento.setText(String.valueOf(valorTotal2));
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
