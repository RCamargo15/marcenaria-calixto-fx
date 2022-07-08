package guiFuncionarios;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Db.DbException;
import application.Main;
import entities.services.FuncionarioService;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import marcenaria.entities.Funcionario;

public class FuncionarioVisualizarController implements Initializable, DataChangeListener {

	private FuncionarioService FuncionarioService;

	@FXML
	private TableView<Funcionario> tableViewFuncionario;

	@FXML
	private TableColumn<Funcionario, Integer> tableColumnCodFuncionario;

	@FXML
	private TableColumn<Funcionario, String> tableColumnNome;

	@FXML
	private TableColumn<Funcionario, String> tableColumnRg;

	@FXML
	private TableColumn<Funcionario, String> tableColumnCpf;
	
	@FXML
	private TableColumn<Funcionario, String> tableColumnCtps;

	@FXML
	private TableColumn<Funcionario, String> tableColumnRua;

	@FXML
	private TableColumn<Funcionario, Integer> tableColumnNumero;

	@FXML
	private TableColumn<Funcionario, String> tableColumnComplemento;

	@FXML
	private TableColumn<Funcionario, String> tableColumnBairro;

	@FXML
	private TableColumn<Funcionario, String> tableColumnCep;

	@FXML
	private TableColumn<Funcionario, String> tableColumnCidade;

	@FXML
	private TableColumn<Funcionario, String> tableColumnEstado;

	@FXML
	private TableColumn<Funcionario, String> tableColumnUF;

	@FXML
	private TableColumn<Funcionario, Integer> tableColumnDDD;

	@FXML
	private TableColumn<Funcionario, String> tableColumnTelefone;

	@FXML
	private TableColumn<Funcionario, String> tableColumnCelular;

	@FXML
	private TableColumn<Funcionario, Date> tableColumnDataNasc;

	@FXML
	private TableColumn<Funcionario, Date> tableColumnDataAdmissao;
	
	@FXML
	private TableColumn<Funcionario, String> tableColumnTipoSang;
	
	@FXML
	private TableColumn<Funcionario, String> tableColumnFuncao;
	
	@FXML
	private TableColumn<Funcionario, String> tableColumnSetor;
	
	@FXML
	private TableColumn<Funcionario, Double> tableColumnSalario;

	@FXML
	private TableColumn<Funcionario, String> tableColumnObs;

	@FXML
	private TableColumn<Funcionario, Funcionario> tableColumnEditar;

	@FXML
	private TableColumn<Funcionario, Funcionario> tableColumnRemover;

	@FXML
	private TextField searchByCod;

	@FXML
	private Button btBuscar;

	@FXML
	private Button mostrarTodos;

	@FXML
	private Button btNovoFuncionario;

	private ObservableList<Funcionario> obsList;

	@FXML
	public void onBtNovoFuncionario(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Funcionario obj = new Funcionario();
		createCadastroFuncionarioForm(obj, parentStage, "/guiFuncionarios/CadastroFuncionario.fxml");
	}

	@FXML
	public void onBtMostrarTodos() {
		updateTableViewFuncionario();
	}

	@FXML
	public void onBtBuscar() {

		if (FuncionarioService == null) {
			throw new IllegalStateException("FuncionarioService null");
		}

		Funcionario buscaFuncionario = FuncionarioService.findByCodFuncionario(Utils.tryParseToInt(searchByCod.getText()));

		if (buscaFuncionario == null) {
			Alerts.showAlert("Busca de Funcionário", null, "Nenhum funcionário com esse código foi encontrado no sistema!",
					AlertType.INFORMATION);
		} else {
			obsList = FXCollections.observableArrayList(buscaFuncionario);
			tableViewFuncionario.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
		}

	}

	public void setFuncionarioService(FuncionarioService FuncionarioService) {
		this.FuncionarioService = FuncionarioService;
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnCodFuncionario.setCellValueFactory(new PropertyValueFactory<>("registroFunc"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnRg.setCellValueFactory(new PropertyValueFactory<>("rg"));
		tableColumnCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		tableColumnCtps.setCellValueFactory(new PropertyValueFactory<>("ctps"));
		tableColumnRua.setCellValueFactory(new PropertyValueFactory<>("rua"));
		tableColumnNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
		tableColumnComplemento.setCellValueFactory(new PropertyValueFactory<>("complemento"));
		tableColumnBairro.setCellValueFactory(new PropertyValueFactory<>("bairro"));
		tableColumnCep.setCellValueFactory(new PropertyValueFactory<>("cep"));
		tableColumnCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
		tableColumnEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
		tableColumnUF.setCellValueFactory(new PropertyValueFactory<>("uf"));
		tableColumnDDD.setCellValueFactory(new PropertyValueFactory<>("ddd"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		tableColumnCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
		tableColumnDataNasc.setCellValueFactory(new PropertyValueFactory<>("dataNasc"));
		Utils.formatTableColumnDate(tableColumnDataNasc, "dd/MM/yyyy");
		tableColumnDataAdmissao.setCellValueFactory(new PropertyValueFactory<>("dataAdmissao"));
		Utils.formatTableColumnDate(tableColumnDataAdmissao, "dd/MM/yyyy");
		tableColumnTipoSang.setCellValueFactory(new PropertyValueFactory<>("tipoSang"));
		tableColumnFuncao.setCellValueFactory(new PropertyValueFactory<>("funcao"));
		tableColumnSetor.setCellValueFactory(new PropertyValueFactory<>("setor"));
		tableColumnSalario.setCellValueFactory(new PropertyValueFactory<>("salario"));
		Utils.formatTableColumnDouble(tableColumnSalario, 2);
		tableColumnObs.setCellValueFactory(new PropertyValueFactory<>("obs"));

		searchByCod.setPromptText("Insira o registro do funcionário");

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewFuncionario.prefHeightProperty().bind(stage.heightProperty());
		tableViewFuncionario.prefWidthProperty().bind(stage.widthProperty());

	}

	public void updateTableViewFuncionario() {
		if (FuncionarioService == null) {
			throw new IllegalStateException("Service null");
		}

		List<Funcionario> list = FuncionarioService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewFuncionario.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createCadastroFuncionarioForm(Funcionario obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox vBox = loader.load();

			CadastroFuncionarioController cadastroController = loader.getController();
			cadastroController.setFuncionario(obj);
			cadastroController.setFuncionarioService(new FuncionarioService());
			cadastroController.subscribeDataChangeListener(this);

			Stage cadastroFuncionarioStage = new Stage();
			cadastroFuncionarioStage.setTitle("Cadastro de funcionário");
			cadastroFuncionarioStage.setScene(new Scene(vBox));
			cadastroFuncionarioStage.setResizable(false);
			cadastroFuncionarioStage.initOwner(parentStage);
			cadastroFuncionarioStage.initModality(Modality.WINDOW_MODAL);
			cadastroFuncionarioStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void createEditarFuncionarioForm(Funcionario obj, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox vBox = loader.load();

			CadastroFuncionarioController cadastroController = loader.getController();
			cadastroController.setFuncionario(obj);
			cadastroController.setFuncionarioService(new FuncionarioService());
			cadastroController.subscribeDataChangeListener(this);
			cadastroController.updateFuncionarioData();

			Stage cadastroFuncionarioStage = new Stage();
			cadastroFuncionarioStage.setTitle("Cadastro de funcionário");
			cadastroFuncionarioStage.setScene(new Scene(vBox));
			cadastroFuncionarioStage.setResizable(false);
			cadastroFuncionarioStage.initOwner(parentStage);
			cadastroFuncionarioStage.initModality(Modality.WINDOW_MODAL);
			cadastroFuncionarioStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IOException", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableViewFuncionario();
	}

	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<Funcionario, Funcionario>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Funcionario obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createEditarFuncionarioForm(obj, Utils.currentStage(event),
						"/guiFuncionarios/CadastroFuncionario.fxml"));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemover.setCellFactory(param -> new TableCell<Funcionario, Funcionario>() {
			private final Button button = new Button("Deletar");

			@Override
			protected void updateItem(Funcionario obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> excluirFuncionario(obj));
			}
		});
	}

	private void excluirFuncionario(Funcionario obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("EXCLUIR FUNCIONÁRIO",
				"Tem certeza que deseja remover esse funcionário do seu banco de dados?");
		if (result.get() == ButtonType.OK) {
			if (FuncionarioService == null) {
				throw new IllegalStateException("Funcionario está vazio");
			}
			try {
				FuncionarioService.removerFuncionario(obj);
				updateTableViewFuncionario();
			} catch (DbException e) {
				Alerts.showAlert("Erro ao excluir funcionario", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
