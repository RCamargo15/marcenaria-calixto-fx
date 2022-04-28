package entities.services;

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
	
	public void removerEntrada(EntradaProduto obj) {
		entradaProdutoDao.deleteByCodEntradaProduto(obj.getCodEntrada());
	}
	
	public EntradaProduto findByCodEntrada(Integer codEntrada) {
		return entradaProdutoDao.findByCodEntradaProduto(codEntrada);
	}
	
	public List<EntradaProduto> findByCodProduto(Integer codProduto) {
		return entradaProdutoDao.findByCodProd(codProduto);
	
	}
	
	public List<EntradaProduto> findByNumeroNF(String numeroNF){
		return entradaProdutoDao.findByNumeroNF(numeroNF);
	}
	
}
