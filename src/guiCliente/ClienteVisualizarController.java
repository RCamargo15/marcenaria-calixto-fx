package guiCliente;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import marcenaria.entities.Cliente;
import model.services.ClienteService;

public class ClienteVisualizarController implements Initializable {
	
	private ClienteService clienteService;

	@FXML
	private TableView<Cliente> tableViewCliente;
	
	@FXML
	private TableColumn<Cliente, Integer> tableColumnCodCliente;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnNome;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnRg;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnCpf;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnRua;
	
	@FXML
	private TableColumn<Cliente, Integer> tableColumnNumero;

	@FXML
	private TableColumn<Cliente, String> tableColumnComplemento;

	@FXML
	private TableColumn<Cliente, String> tableColumnBairro;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnCep;
	
	@FXML
	private TableColumn<Cliente, Integer> tableColumnDDD;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnTelefone;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnCelular;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Cliente, Date> tableColumnDataCadastro;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnObs;
	
	@FXML
	private Button btNovoCliente;
	
	private ObservableList<Cliente> obsList;
	
	@FXML
	public void onBtNovoCliente(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Cliente obj = new Cliente();
		createCadastroClienteForm(obj, parentStage,"/guiCliente/CadastroCliente.fxml");
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnCodCliente.setCellValueFactory(new PropertyValueFactory<>("codCliente"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnRg.setCellValueFactory(new PropertyValueFactory<>("rg"));
		tableColumnCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		tableColumnRua.setCellValueFactory(new PropertyValueFactory<>("rua"));
		tableColumnNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
		tableColumnComplemento.setCellValueFactory(new PropertyValueFactory<>("complemento"));
		tableColumnBairro.setCellValueFactory(new PropertyValueFactory<>("bairro"));
		tableColumnCep.setCellValueFactory(new PropertyValueFactory<>("cep"));
		tableColumnDDD.setCellValueFactory(new PropertyValueFactory<>("ddd"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		tableColumnCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnDataCadastro.setCellValueFactory(new PropertyValueFactory<>("dataCadastro"));
		tableColumnObs.setCellValueFactory(new PropertyValueFactory<>("obs"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCliente.prefHeightProperty().bind(stage.heightProperty());	
		tableViewCliente.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	
	}
	
	
	public void updateTableViewCliente() {
		if(clienteService == null) {
			throw new IllegalStateException("Service null");
		}
		
		List<Cliente> list = clienteService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewCliente.setItems(obsList);
	}
	
	private void createCadastroClienteForm(Cliente obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox vBox = loader.load();
			
			CadastroClienteController cadastroController = loader.getController();
			cadastroController.setCliente(obj);
			cadastroController.updateClienteData();
			cadastroController.setClienteService(new ClienteService());
			
			Stage cadastroClienteStage = new Stage();
			cadastroClienteStage.setTitle("Cadastro de cliente");
			cadastroClienteStage.setScene(new Scene(vBox));
			cadastroClienteStage.setResizable(false);
			cadastroClienteStage.initOwner(parentStage);
			cadastroClienteStage.initModality(Modality.WINDOW_MODAL);
			cadastroClienteStage.showAndWait();
		}
		catch(IOException e) {
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}

}
