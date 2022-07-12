package entities.services;

import java.util.List;

import marcenaria.dao.DaoFactory;
import marcenaria.dao.ProdutoDao;
import marcenaria.entities.Produto;

public class ProdutoService {

	private ProdutoDao produtoDao = DaoFactory.createProdutoDao();

	public List<Produto> findAll() {
		return produtoDao.findAll();
	}
	
	public void saveOrUpdate(Produto obj) {
		if (obj.getCodProduto() == null) {
			produtoDao.insert(obj);
		}
		else {
			produtoDao.update(obj);
		}
	}
	
	public void removerProduto(Produto obj) {
		produtoDao.deleteByCodProduto(obj.getCodProduto());
	}
	
	public Produto findByCodProduto(Integer codProduto) {
		return produtoDao.findByCodProduto(codProduto);
	}
	
	public List<Produto> findByNomeProduto(String nomeProduto) {
		return produtoDao.findByNomeProduto(nomeProduto);
	}
}
