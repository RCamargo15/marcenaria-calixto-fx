package entities.services;

import java.util.List;

import marcenaria.dao.DaoFactory;
import marcenaria.dao.FornecedorDao;
import marcenaria.entities.Fornecedor;

public class FornecedorService {

	private FornecedorDao fornecedorDao = DaoFactory.createFornecedorDao();

	public List<Fornecedor> findAll() {
		return fornecedorDao.findAll();
	}
	
	public void saveOrUpdate(Fornecedor obj) {
		if (obj.getCodFornecedor() == null) {
			fornecedorDao.insert(obj);
		}
		else {
			fornecedorDao.update(obj);
		}
	}
	
	public void removerFornecedor(Fornecedor obj) {
		fornecedorDao.deleteByCodFornecedor(obj.getCodFornecedor());
	}
	
	public Fornecedor findByCodFornecedor(Integer codFornecedor) {
		List<Fornecedor> list = fornecedorDao.findAll();
		
		for (Fornecedor fornecedor: list) {
			if (fornecedor.getCodFornecedor() == codFornecedor) {
				return fornecedor;
			}
		}
		return null;
	}
}
