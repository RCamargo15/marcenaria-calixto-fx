package guiOrdemDeServicoEmpresa;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Db.DbException;
import application.Main;
import entities.services.EmpresaService;
import entities.services.FuncionarioService;
import entities.services.OrdemServicoEmpresaService;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import marcenaria.entities.Empresa;
import marcenaria.entities.Funcionario;
import marcenaria.entities.OrdemServicoEmpresa;

public class OrdemServicoEmpresaVisualizarController implements DataChangeListener, Initializable {

	private OrdemServicoEmpresaService ordemServicoEmpresaService;
	private FuncionarioService funcionarioService;
	private EmpresaService empresaService;

	@FXML
	private TableView<OrdemServicoEmpresa> tableViewOrdemServicoEmpresa;

	@FXML
	private TableColumn<OrdemServicoEmpresa, Integer> tableColumnNumOrcamento;

	@FXML
	private TableColumn<Empresa, Integer> tableColumnCodEmpresa;
	
	@FXML
	private TableColumn<OrdemServicoEmpresa, String> tableColumnNomeResponsavel;

	@FXML
	private TableColumn<OrdemServicoEmpresa, String> tableColumnDescServico;

	@FXML
	private TableColumn<OrdemServicoEmpresa, Date> tableColumnDataOrdem;

	@FXML
	private TableColumn<OrdemServicoEmpresa, Date> tableColumnDataInicio;

	@FXML
	private TableColumn<OrdemServicoEmpresa, Date> tableColumnPrazoEntrega;

	@FXML
	private TableColumn<OrdemServicoEmpresa, Date> tableColumnDataEntrega;

	@FXML
	private TableColumn<OrdemServicoEmpresa, String> tableColumnStatusServico;

	@FXML
	private TableColumn<OrdemServicoEmpresa, Double> tableColumnValorTotal;

	@FXML
	private TableColumn<Funcionario, Integer> tableColumnFuncResponsavel;

	@FXML
	private TableColumn<OrdemServicoEmpresa, String> tableColumnObs;

	@FXML
	private TableColumn<OrdemServicoEmpresa, OrdemServicoEmpresa> tableColumnEditar;

	@FXML
	private TableColumn<OrdemServicoEmpresa, OrdemServicoEmpresa> tableColumnRemover;
	
	@FXML
	private Button btBuscar;
	
	@FXML
	private Button btMostrarTodos;
	
	@FXML
	private TextField txtBuscarCodOrdem;
	
	ObservableList<OrdemServicoEmpresa> obsListOrdemServico;
	ObservableList<OrdemServicoEmpresa> obsListBuscar;
	
	@FXML
	public void onBtBuscarAction() {
		
		if(txtBuscarCodOrdem == null || txtBuscarCodOrdem.getText().equals("")) {
			Alerts.showAlert("Erro ao buscar", null, "Campo de busca não pode estar vazio. Insira o número de orçamento", AlertType.INFORMATION);
		}
		OrdemServicoEmpresa osc = ordemServicoEmpresaService.findByNumPedido(Integer.parseInt(txtBuscarCodOrdem.getText()));
		obsListBuscar.add(osc);
		tableViewOrdemServicoEmpresa.setItems(obsListBuscar);
	}

	@FXML
	public void onBtMostrarTodosAction() {
		updateTableViewOrdemEmpresaVisualizar();
	}
	

	public void setServices(OrdemServicoEmpresaService ordemEmpresaServicoService,
			FuncionarioService funcionarioService, EmpresaService empresaService) {
		this.ordemServicoEmpresaService = ordemEmpresaServicoService;
		this.funcionarioService = funcionarioService;
		this.empresaService = empresaService;
	}

	public void updateTableViewOrdemEmpresaVisualizar() {
		List<OrdemServicoEmpresa> list = ordemServicoEmpresaService.findAll();
		obsListOrdemServico = FXCollections.observableArrayList(list);
		tableViewOrdemServicoEmpresa.setItems(obsListOrdemServico);
	}

	@Override
	public void onDataChanged() {
		updateTableViewOrdemEmpresaVisualizar();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
		initEditButtons();
		initRemoveButtons();
		txtBuscarCodOrdem.setPromptText("Insira Nº de orçamento");
	}

	public void initializeNodes() {
		tableColumnNumOrcamento.setCellValueFactory(new PropertyValueFactory<>("numeroPedido"));
		tableColumnCodEmpresa.setCellValueFactory(new PropertyValueFactory<>("codEmpresa"));
		tableColumnNomeResponsavel.setCellValueFactory(new PropertyValueFactory<>("nomeResponsavel"));
		tableColumnDescServico.setCellValueFactory(new PropertyValueFactory<>("descServico"));
		tableColumnDataOrdem.setCellValueFactory(new PropertyValueFactory<>("dataOrdem"));
		tableColumnDataInicio.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
		tableColumnPrazoEntrega.setCellValueFactory(new PropertyValueFactory<>("prazoEntrega"));
		tableColumnStatusServico.setCellValueFactory(new PropertyValueFactory<>("statusServico"));
		tableColumnValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
		tableColumnFuncResponsavel.setCellValueFactory(new PropertyValueFactory<>("funcResponsavel"));
		tableColumnObs.setCellValueFactory(new PropertyValueFactory<>("obs"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewOrdemServicoEmpresa.prefWidthProperty().bind(stage.widthProperty());
		tableViewOrdemServicoEmpresa.prefHeightProperty().bind(stage.heightProperty());
		
	
		Utils.formatTableColumnDate(tableColumnDataInicio, "dd/MM/yyyy");
		Utils.formatTableColumnDate(tableColumnDataOrdem, "dd/MM/yyyy");
		Utils.formatTableColumnDate(tableColumnPrazoEntrega, "dd/MM/yyyy");
		Utils.formatTableColumnDouble(tableColumnValorTotal, 2);
		
		tableColumnDataEntrega.setCellValueFactory(new PropertyValueFactory<>("dataEntrega"));
		tableColumnDataEntrega.setCellFactory(coluna -> {
		    return new TableCell<OrdemServicoEmpresa, Date>(){
		        @Override
		        protected void updateItem(Date item, boolean empty) {
		            super.updateItem(item, empty);
		            if(item != null && !empty) {
		                 DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		                 String dataFormatada = dateFormat.format(item);
		                setText(dataFormatada);
		            } else {
		                setText("");
		            }
		        }
		    };
		 });
	}

	private void createEditarOrdemServicoEmpresaForm(OrdemServicoEmpresa obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox vBox = loader.load();

			EditarOrdemDeServicoEmpresaController controller = loader.getController();
			controller.setOrdemServicoEmpresa(obj);
			controller.setServicos(ordemServicoEmpresaService, empresaService, funcionarioService);
			controller.loadEmpresaOS();
			controller.loadFuncionariosOS();
			controller.loadStatusServico();
			controller.receberDadosParaEditarOS();
			controller.subscribeDataChangeListener(this);

			Stage stage = new Stage();
			stage.setTitle("Gerar ordem de serviço");
			stage.setScene(new Scene(vBox));
			stage.setResizable(false);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(parentStage);
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<OrdemServicoEmpresa, OrdemServicoEmpresa>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(OrdemServicoEmpresa obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createEditarOrdemServicoEmpresaForm(obj, Utils.currentStage(event),
						"/guiOrdemDeServicoEmpresa/EditarOrdemDeServicoEmpresa.fxml"));

			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<OrdemServicoEmpresa, OrdemServicoEmpresa>() {
			private final Button button = new Button("Deletar");

			@Override
			protected void updateItem(OrdemServicoEmpresa obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction((event -> excluirOrdemServicoEmpresa(obj)));

			}
		});
	}

	private void excluirOrdemServicoEmpresa(OrdemServicoEmpresa obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("EXCLUIR ORÇAMENTO",
				"Tem certeza que deseja remover esse orçamento?");
		if (result.get() == ButtonType.OK) {
			if (ordemServicoEmpresaService == null) {
				throw new IllegalStateException("Orcamento vazio");
			}
			try {
				ordemServicoEmpresaService.removerOrdemServicoEmpresa(obj);
				updateTableViewOrdemEmpresaVisualizar();

			} catch (DbException e) {
				Alerts.showAlert("Erro ao excluir orçamento", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
