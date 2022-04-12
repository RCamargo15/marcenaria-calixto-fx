package entities.services;

import java.util.ArrayList;
import java.util.List;

import marcenaria.dao.DaoFactory;
import marcenaria.dao.EntradaProdutoDao;
import marcenaria.entities.EntradaProduto;

public class EntradaProdutoService {

	private EntradaProdutoDao entradaProdutoDao = DaoFactory.createEntradaProduto();
	
	public List<EntradaProduto> findAll(){
		return entradaProdutoDao.findAll();
	}
	
	public void saveOrUpdate(EntradaProduto obj) {
		if(obj.getCodEntrada() == null) {
			entradaProdutoDao.insert(obj);
		}
		else {
			entradaProdutoDao.update(obj);
		}
	}
	
	public void removerSaida(EntradaProduto obj) {
		entradaProdutoDao.deleteByCodEntradaProduto(obj.getCodEntrada());
	}
	
	public EntradaProduto findByCodEntrada(Integer codEntrada) {
		List<EntradaProduto> list = entradaProdutoDao.findAll();
		
		for(EntradaProduto saida : list) {
			if (saida.getCodEntrada() == codEntrada) {
				return saida;
			}
		}
		return null;
	}
	
	public List<EntradaProduto> findByCodProduto(Integer codProduto) {
		List<EntradaProduto> list = entradaProdutoDao.findAll();
		List<EntradaProduto> listCodProd = new ArrayList<>();
		
		for(EntradaProduto saida : list) {
			if(saida.getCodProduto().equals(codProduto)) {
				listCodProd.add(saida);
			}
		}
		
		return listCodProd;
	}
	
}
