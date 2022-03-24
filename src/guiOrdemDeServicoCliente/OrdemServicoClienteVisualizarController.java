package guiOrdemDeServicoCliente;

import Db.DbException;
import entities.services.OrdemServicoClienteService;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import guiOrcamentoEmpresa.ButtonType;
import guiOrcamentoEmpresa.IllegalStateException;
import guiOrcamentoEmpresa.Optional;
import marcenaria.entities.OrdemServicoCliente;

public class OrdemServicoClienteVisualizarController implements DataChangeListener, Initializable {

	private OrdemServicoClienteService ordemClienteServicoService;
	private OrdemServicoCliente ordemServicoCliente;
	
	@FXML
	private TableView<OrdemServicoCliente> tableViewOrdemServicoCliente;
	
	@FXML
	private TableColumn<OrdemServicoCliente, Integer> tableColumnNumOrcamento;
	
	@FXML
	private TableColumn<OrdemServicoCliente, String> tableColumnCodCliente;
	
	@FXML
	private TableColumn<OrdemServicoCliente, String> tableColumnDescServico;
	
	@FXML
	private TableColumn<OrdemServicoCliente, Date> tableColumnDataOrdem;
	
	@FXML
	private TableColumn<OrdemServicoCliente, Date> tableColumnDataInicio;
	
	@FXML
	private TableColumn<OrdemServicoCliente, Date> tableColumnPrazoEntrega;
	
	@FXML
	private TableColumn<OrdemServicoCliente, Date> tableColumnDataEntrega;
	
	@FXML
	private TableColumn<OrdemServicoCliente, String> tableColumnStatusServico;
	
	@FXML
	private TableColumnn<OrdemServicoCliente, Double> tableColumnValorTotal;
	
	@FXML
	private TableColumn<OrdemServicoCliente, String> tableColumnFuncResponsavel;
	
	@FXML
	private TableColumn<OrdemServicoCliente, String> tableColumnObs;
	
	@FXML
	private TableColumn<OrdemServicoCliente, OrdemServicoCliente> tableColumnEditar;
	
	@FXML
	private TableColumn<OrdemServicoCliente, OrdemServicoCliente> tableColumnRemover;
	
	ObservableList<OrdemServicoCliente> obsListOrdemServico;
	
	public void setOrdemServicoCliente(OrdemServicoCliente ordemServicoCliente) {
	 this.ordemClienteServico = ordemServicoCliente;	
	}
	
	
	public void updateTableViewOrdemClienteVisualizar() {
		List<OrdemServicoCliente> list = ordemClienteServicoService.findAll();
		obsListOrdemServico = FXCollections.observableArrayList(list);
		tableViewOrdemServicoCliente.setItems(obsListOrdemServico);
	}
	
	@Override
	public void onDataChanged(){
		
	}
	
	
	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<OrdemServicoCliente, OrdemServicoCliente>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(OrdemServicoCliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createEditarProdutoOrcamentoForm(obj, Utils.currentStage(event),
						"/guiOrdemDeServicoCliente/GerarOrdemDeServicoCliente.fxml"));
				
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<OrdemServicoCliente, OrdemServicoCliente>() {
			private final Button button = new Button("Deletar");

			@Override
			protected void updateItem(OrdemServicoCliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction( (event -> excluirOrcamentoEmpresa(obj)));
				
			}
		});
	}
	
	private void excluirOrdemServicoCliente(OrdemServicoCliente obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("EXCLUIR ORÇAMENTO", "Tem certeza que deseja remover esse orçamento?");
			if(result.get() == ButtonType.OK) {
				if(ordemServicoClienteService == null) {
					throw new IllegalStateException("Orcamento vazio");
				}
				try {
					ordemClienteServicoService.removerProduto(obj);
					updateTableViewOrdemClienteVisualizar();

				}
				catch(DbException e) {
					Alerts.showAlert("Erro ao excluir orçamento", null, e.getMessage(), AlertType.ERROR);
				}
			}
	}


	
	
	
	
	}
	

}
