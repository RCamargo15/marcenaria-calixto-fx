package entities.services;

import java.util.ArrayList;
import java.util.List;

import marcenaria.dao.DaoFactory;
import marcenaria.dao.OrcamentoClienteDao;
import marcenaria.entities.OrcamentoCliente;

public class OrcamentoClienteService {

	private OrcamentoClienteDao orcamentoClienteDao = DaoFactory.createOrcamentoClienteDao();

	public List<OrcamentoCliente> findAll() {
		return orcamentoClienteDao.findAll();
	}
	
	public List<OrcamentoCliente> findAllParaTabela(){
		return orcamentoClienteDao.findAllParaTabela();
	}
	
	public List<OrcamentoCliente> findByNumOrcamentoList(Integer numOrcamento){
		return orcamentoClienteDao.findByNumOrcamentoList(numOrcamento);
	}

	public void saveOrUpdate(OrcamentoCliente obj) {
			if (obj.getId() == null) {
				orcamentoClienteDao.insert(obj);
			} else {
				orcamentoClienteDao.update(obj);
			}
	}
	
	public void saveOrcamento(OrcamentoCliente obj) {
		orcamentoClienteDao.updateOrcamento(obj);
	}

	public void removerOrcamento(OrcamentoCliente obj) {
		orcamentoClienteDao.deleteByNumOrcamento(obj.getNumOrcamento());
	}
	
	public void removerProduto(OrcamentoCliente obj) {
		orcamentoClienteDao.deleteById(obj.getId());
	}


	public List<OrcamentoCliente> findByCodCliente(Integer codCliente) {
		List<OrcamentoCliente> list = orcamentoClienteDao.findAll();
		List<OrcamentoCliente> listCodProd = new ArrayList<>();

		for (OrcamentoCliente saida : list) {
			if (saida.getCodCliente().equals(codCliente)) {
				listCodProd.add(saida);
			}
		}

		return listCodProd;
	}
	
	public OrcamentoCliente findById(Integer id) {
		return orcamentoClienteDao.findById(id);
	}
	
	public OrcamentoCliente findByNumOrcamento(Integer numOrcamento) {
		return orcamentoClienteDao.findByNumOrcamento(numOrcamento);
	}
}
