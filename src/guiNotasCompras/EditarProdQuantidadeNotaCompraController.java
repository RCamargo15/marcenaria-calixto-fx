package guiNotasCompras;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Db.DbException;
import entities.services.EntradaProdutoService;
import entities.services.EstoqueService;
import entities.services.NotasComprasService;
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
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import marcenaria.entities.EntradaProduto;
import marcenaria.entities.Estoque;
import marcenaria.entities.NotasCompras;
import marcenaria.entities.Produto;

public class EditarProdQuantidadeNotaCompraController implements Initializable {

	private NotasCompras notasCompras;
	private NotasComprasService notasComprasService;
	private ProdutoService produtoService;
	private EstoqueService estoqueService;
	private EntradaProdutoService entradaProdutoService;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private ComboBox<Produto> cbProduto;

	@FXML
	private TextField txtQuantidade;

	@FXML
	private TextField txtValorUnit;

	@FXML
	private TextField txtNum;

	@FXML
	private Button btAtualizar;

	@FXML
	private Button btCancelar;

	@FXML
	private GridPane gpAlterar;

	public void setNotasCompras(NotasCompras notasCompras) {
		this.notasCompras = notasCompras;
	}

	public void setServices(NotasComprasService notasComprasService, ProdutoService produtoService,
			EstoqueService estoqueService, EntradaProdutoService entradaProdutoService) {
		this.notasComprasService = notasComprasService;
		this.produtoService = produtoService;
		this.estoqueService = estoqueService;
		this.entradaProdutoService = entradaProdutoService;
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
		cbProduto.setValue(notasCompras.getCodProduto());
		txtQuantidade.setText(String.valueOf(notasCompras.getQuantidade()));
		txtValorUnit.setText(String.valueOf("R$ " + notasCompras.getValorUnit()));
	}

	@FXML
	private void onBtAtualizarAction(ActionEvent event) {
		if (notasCompras == null || notasComprasService == null) {
			throw new IllegalStateException("Nota fiscal null");
		}

		double valorTotalNota = 0;
		List<EntradaProduto> listaEntrada = entradaProdutoService.findAll();
		try {
			Estoque estoque = estoqueService.findByCodProduto(cbProduto.getValue().getCodProduto());
			NotasCompras obj = notasCompras;
			
			List<NotasCompras> listaNotas = notasComprasService.findByNumeroNFList(obj.getNumeroNF());
			
			int qtdNota = obj.getQuantidade();

			obj.setCodNota(notasCompras.getCodNota());
			obj.setCodProduto(cbProduto.getValue());
			obj.setQuantidade(Integer.parseInt(txtQuantidade.getText()));
			obj.setValorUnit(Double.parseDouble(Utils.getValorTotalNota(txtValorUnit.getText())));

			int estoqueAtual = estoque.getEstoqueAtual();
			int qtdAtualizada = Integer.parseInt(txtQuantidade.getText());
			int qtdFinalNota = 0;

			qtdFinalNota = qtdAtualizada - qtdNota;

			estoque.setEstoqueAtual(estoqueAtual + qtdFinalNota);
	
			for(NotasCompras nc : listaNotas) {
				valorTotalNota = valorTotalNota + ((nc.getValorUnit()) * nc.getQuantidade());
			}
			
			obj.setValorTotalNota(valorTotalNota);
			
			for(EntradaProduto ep : listaEntrada) {
				if( (ep.getCodProduto().equals(obj.getCodProduto()) && (ep.getNumeroNF().getNumeroNF().equals(obj.getNumeroNF())))) {
					ep.setQuantidade(obj.getQuantidade());
					ep.setValorTotalNota(obj);
					ep.setValorTotal(obj);
					ep.setValorUnit(obj);
					entradaProdutoService.saveOrUpdate(ep);
				}
			}
			
			estoqueService.saveOrUpdate(estoque);
			notasComprasService.updateProdutos(obj);
			notificarDataChangeListener();
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("Erro ao editar quantidade de produto", null, e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
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
