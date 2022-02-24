package guiNotasFiscais;

import java.net.URL;
import java.util.ResourceBundle;

import entities.services.FornecedorService;
import entities.services.NotasComprasService;
import entities.services.ProdutoService;
import gui.listeners.DataChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import marcenaria.entities.NotasCompras;

public class NotaFiscalVisualizarController implements Initializable, DataChangeListener {

	private NotasCompras notasCompras;
	private NotasComprasService notasComprasService;
	private FornecedorService fornecedorService;
	private ProdutoService produtoService;

	@FXML
	private TableView<NotasCompras> tableViewNotasCompras;

	@FXML
	private TableColumn<NotasCompras, Integer> tableColumnCodNota;

	@FXML
	private TableColumn<NotasCompras, Integer> tableColumnCodFornecedor;
	
	@FXML
	private TableColumn<NotasCompras, Integer> tableColumnCodNota;
	
	@FXML
	private TableColumn<NotasCompras, Integer> tableColumnCodNota;
	
	@FXML
	private TableColumn<NotasCompras, Integer> tableColumnCodNota;
	
	@FXML
	private TableColumn<NotasCompras, Integer> tableColumnCodNota;
	
	@FXML
	private TableColumn<NotasCompras, Integer> tableColumnCodNota;
	
	@FXML
	private TableColumn<NotasCompras, Integer> tableColumnCodNota;
	
	@FXML
	private TableColumn<NotasCompras, Integer> tableColumnCodNota;

	public void setNotasCompras(NotasCompras notasCompras) {
		this.notasCompras = notasCompras;
	}

	public void setServices(NotasComprasService notasComprasService, FornecedorService fornecedorService,
			ProdutoService produtoService) {
		this.notasComprasService = notasComprasService;
		this.fornecedorService = fornecedorService;
		this.produtoService = produtoService;
	}

	@Override
	public void onDataChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

}
