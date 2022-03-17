package guiOrcamentos;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Db.DbException;
import entities.services.OrcamentoClienteService;
import entities.services.ProdutoService;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import marcenaria.entities.OrcamentoCliente;
import marcenaria.entities.Produto;

public class EditarProdQuantidadeController implements Initializable {

	private OrcamentoCliente orcamentoCliente;
	private OrcamentoClienteService orcamentoClienteService;
	private ProdutoService produtoService;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private ComboBox<Produto> cbProduto;

	@FXML
	private TextField txtQuantidade;

	@FXML
	private Button btAtualizar;

	@FXML
	private Button btCancelar;
	
	public void setOrcamentoCliente(OrcamentoCliente orcamentoCliente) {
		this.orcamentoCliente = orcamentoCliente;
	}
	
	public void setServices(OrcamentoClienteService orcamentoClienteService, ProdutoService produtoService) {
		this.orcamentoClienteService = orcamentoClienteService;
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
	
	public void updateEditarProdQuantidadeData() {
		cbProduto.setValue(orcamentoCliente.getCodProduto());
		txtQuantidade.setText(String.valueOf(orcamentoCliente.getQuantidade()));
	}
	
	private OrcamentoCliente getEditProdQuantidadeData() {
		OrcamentoCliente orcamento = new OrcamentoCliente();
		
		orcamento.setId(orcamentoCliente.getId());
		orcamento.setCodProduto(cbProduto.getValue());
		orcamento.setQuantidade(Integer.parseInt(txtQuantidade.getText()));
		
		return orcamento;
	}

	@FXML
	private void onBtAtualizarAction(ActionEvent event) {
		if (orcamentoCliente == null || orcamentoClienteService == null) {
			throw new IllegalStateException("Orcamentos null");
		}
		try {
			orcamentoCliente = getEditProdQuantidadeData();
			orcamentoClienteService.saveOrUpdate(orcamentoCliente);
			notificarDataChangeListener();
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("Erro ao gerar orçamento", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@Override 
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeComboBoxProduto();
	}
	
	public void loadProdutos() {
		List<Produto> list = produtoService.findAll();
		ObservableList<Produto> listProd = FXCollections.observableArrayList(list);
		cbProduto.setItems(listProd);
	}
	
	private void initializeComboBoxProduto() {
		Callback<ListView<Produto>, ListCell<Produto>> factory = lv -> new ListCell<Produto>() {
			@Override
			protected void updateItem(Produto item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getDescProduto());
			}
		};
		cbProduto.setCellFactory(factory);
		cbProduto.setButtonCell(null);
	}

}
