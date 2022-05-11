package guiOrcamentoEmpresa;

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
import entities.services.EmpresaService;
import entities.services.OrcamentoEmpresaService;
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
import marcenaria.entities.Empresa;
import marcenaria.entities.OrcamentoEmpresa;
import marcenaria.entities.Produto;
import marcenaria.entities.auxiliar.ProdutoOrcamento;
import model.exceptions.ValidationException;

public class GerarNovoOrcamentoEmpresaController implements Initializable {

	private OrcamentoEmpresa orcamentoEmpresa;
	private OrcamentoEmpresaService orcamentoEmpresaService;
	private EmpresaService empresaService;
	private ProdutoService produtoService;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNumOrcamento;
	@FXML
	private Label errorNumOrcamento;

	@FXML
	private GridPane gpInfoEmpresa;

	@FXML
	private ComboBox<Empresa> cbCodEmpresa;
	@FXML
	private Label errorCodEmpresa;

	@FXML
	private TextField txtTelefoneEmpresa;
	@FXML
	private Label errorTelefone;
	
	@FXML
	private TextField txtNomeResponsavel;
	@FXML
	private Label errorNomeResponsavel;

	@FXML
	private TextField txtCelularEmpresa;
	@FXML
	private Label errorCelular;

	@FXML
	private TextField txtEmailEmpresa;
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
	private TableView<ProdutoOrcamento> tableViewOrcamentoEmpresa;

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

	private ObservableList<Empresa> obsListEmpresa;

	private ObservableList<ProdutoOrcamento> obsListProduto;

	private ObservableList<ProdutoOrcamento> updateTableView;

	private List<ProdutoOrcamento> prodOrcamento = new ArrayList<>();

	private List<OrcamentoEmpresa> listaParaCadastro = new ArrayList<>();

	private List<OrcamentoEmpresa> listaParaInserir = new ArrayList<>();

	public void setOrcamentoEmpresa(OrcamentoEmpresa orcamentoEmpresa) {
		this.orcamentoEmpresa = orcamentoEmpresa;
	}

	public void setServices(OrcamentoEmpresaService orcamentoEmpresaService, EmpresaService empresaService,
			ProdutoService produtoService) {
		this.orcamentoEmpresaService = orcamentoEmpresaService;
		this.empresaService = empresaService;
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

	private OrcamentoEmpresa getOrcamentoEmpresaData() {

		OrcamentoEmpresa obj = new OrcamentoEmpresa();
		Produto prod = new Produto();
		
		double valorTotal = 1;

		obj.setNumOrcamento(Integer.parseInt(txtNumOrcamento.getText()));
		obj.setCodEmpresa(cbCodEmpresa.getValue());
		obj.setNomeResponsavel(txtNomeResponsavel.getText());
		obj.setTelefone(txtTelefoneEmpresa.getText());
		obj.setCelular(txtCelularEmpresa.getText());
		obj.setEmail(txtEmailEmpresa.getText());
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
		updateTableViewOrcamentoEmpresa();

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

	public void updateTableViewOrcamentoEmpresa() {
		List<ProdutoOrcamento> list = prodOrcamento;
		updateTableView = FXCollections.observableArrayList(list);
		tableViewOrcamentoEmpresa.setItems(updateTableView);
	}

	@FXML
	public List<OrcamentoEmpresa> onBtInserirAction() {
		
		Map<String, String> errors = ValidateExceptions();
		
			if(errors.size() > 0) {
				Alerts.showAlert("Erro ao inserir produto", null, "Preencha os dados do empresa antes de inserir produtos no orçamento", AlertType.INFORMATION);
			}
			else {
			double valorTotal = 0;
			ProdutoOrcamento produtoTemp = criarProdutoOrcamento();
			OrcamentoEmpresa orcamento = getOrcamentoEmpresaData();
			

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

		if ((txtTelefoneEmpresa.getText() == null || txtTelefoneEmpresa.getText().trim().equals(""))
				&& (txtCelularEmpresa.getText() == null || txtCelularEmpresa.getText().trim().equals(""))) {
			exception.addError("Contato", "Insira pelo menos um telefone para contato");
		}

		if (txtEmailEmpresa.getText() == null || txtEmailEmpresa.getText().trim().equals("")) {
			exception.addError("Email", "Insira o e-mail do empresa");
		}

		if (txtDescricaoServico.getText() == null || txtDescricaoServico.getText().trim().equals("")) {
			exception.addError("DescServico", "Descreva o serviço a ser realizado");
		}

		if (cbCodEmpresa.getValue() == null) {
			exception.addError("Empresa", "Escolha um empresa do seu cadastro");
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
		if (orcamentoEmpresa == null) {
			throw new IllegalStateException("orcamentoEmpresa null");
		}
		if (orcamentoEmpresaService == null) {
			throw new IllegalStateException("orcamentoEmpresaService null");
		}
		Map<String, String> errors = ValidateExceptions();
		listaParaInserir.addAll(listaParaCadastro);
		try {
			if (errors.size() > 0) {
				setErrorMessages(errors);
				Alerts.showAlert("Erro ao cadastrar", null,
						"É necessário inserir todos os dados pendentes antes de cadastrar", AlertType.INFORMATION);
			} else {
				for (OrcamentoEmpresa empresa : listaParaInserir) {
					empresa.setValorTotal(Double.parseDouble(txtValorTotalOrcamento.getText()));
					orcamentoEmpresaService.saveOrUpdate(empresa);
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
		errorCodEmpresa.setText(fields.contains("Empresa") ? errors.get("Empresa") : "");
		erroProduto.setText(fields.contains("Produto") ? errors.get("Produto") : "");

	}

	public void loadEmpresas() {
		if (empresaService == null) {
			throw new IllegalStateException("Empresa Service null");
		}

		List<Empresa> list = empresaService.findAll();
		obsListEmpresa = FXCollections.observableArrayList(list);
		cbCodEmpresa.setItems(obsListEmpresa);
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

	private void initializeComboBoxEmpresa() {
		Callback<ListView<Empresa>, ListCell<Empresa>> factory = lv -> new ListCell<Empresa>() {
			@Override
			protected void updateItem(Empresa item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNomeFantasia());
			}
		};
		cbCodEmpresa.setCellFactory(factory);
		cbCodEmpresa.setButtonCell(factory.call(null));
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
		initializeComboBoxEmpresa();
		initializeComboBoxProduto();
		initRemoverButtons();
		initializeTables();

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewOrcamentoEmpresa.prefHeightProperty().bind(stage.heightProperty());
		tableViewOrcamentoEmpresa.prefWidthProperty().bind(stage.widthProperty());

		gpInfoEmpresa.prefHeightProperty().bind(stage.heightProperty());
		gpInfoEmpresa.prefWidthProperty().bind(stage.widthProperty());

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
					updateTableViewOrcamentoEmpresa();

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
