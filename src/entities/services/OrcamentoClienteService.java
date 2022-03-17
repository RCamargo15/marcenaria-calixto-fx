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

	public void removerOrcamento(OrcamentoCliente obj) {
		orcamentoClienteDao.deleteByNumOrcamento(obj.getNumOrcamento());
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
	
	public OrcamentoCliente findByNumOrcamento(Integer numOrcamento) {
		List<OrcamentoCliente> list = orcamentoClienteDao.findAll();

		for (OrcamentoCliente orc : list) {
			if (orc.getNumOrcamento() == numOrcamento) {
				return orc;
			}
		}
		return null;
	}
}
