package entities.services;

import java.util.List;

import marcenaria.dao.DaoFactory;
import marcenaria.dao.EstoqueDao;
import marcenaria.entities.Estoque;

public class EstoqueService {

	private EstoqueDao estoqueDao = DaoFactory.createEstoqueDao();

	public List<Estoque> findAll() {
		return estoqueDao.findAll();
	}
	
	public void saveOrUpdate(Estoque obj) {
		if (obj.getId() == null) {
			estoqueDao.insert(obj);
		}
		else {
			estoqueDao.update(obj);
		}
	}
	
	public void removerEstoque(Estoque obj) {
		estoqueDao.deleteByCodEstoque(obj.getId());
	}
	
	public Estoque findByCodEstoque(Integer codEstoque) {
		List<Estoque> list = estoqueDao.findAll();
		
		for (Estoque estoque: list) {
			if (estoque.getId() == codEstoque) {
				return estoque;
			}
		}
		return null;
	}
}