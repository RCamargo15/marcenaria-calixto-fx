package entities.services;

import java.util.ArrayList;
import java.util.List;

import marcenaria.dao.DaoFactory;
import marcenaria.dao.OrcamentoEmpresaDao;
import marcenaria.entities.OrcamentoEmpresa;

public class OrcamentoEmpresaService {

	private OrcamentoEmpresaDao orcamentoClienteDao = DaoFactory.createOrcamentoEmpresaDao();

	public List<OrcamentoEmpresa> findAll() {
		return orcamentoClienteDao.findAll();
	}
	
	public List<OrcamentoEmpresa> findAllParaTabela(){
		return orcamentoClienteDao.findAllParaTabela();
	}
	
	public List<OrcamentoEmpresa> findByNumOrcamentoList(Integer numOrcamento){
		return orcamentoClienteDao.findByNumOrcamentoList(numOrcamento);
	}

	public void saveOrUpdate(OrcamentoEmpresa obj) {
			if (obj.getId() == null) {
				orcamentoClienteDao.insert(obj);
			} else {
				orcamentoClienteDao.update(obj);
			}
	}
	
	public void saveOrcamento(OrcamentoEmpresa obj) {
		orcamentoClienteDao.updateOrcamento(obj);
	}

	public void removerOrcamento(OrcamentoEmpresa obj) {
		orcamentoClienteDao.deleteByNumOrcamento(obj.getNumOrcamento());
	}
	
	public void removerProduto(OrcamentoEmpresa obj) {
		orcamentoClienteDao.deleteById(obj.getId());
	}


	public List<OrcamentoEmpresa> findByCodEmpresa(Integer codCliente) {
		List<OrcamentoEmpresa> list = orcamentoClienteDao.findAll();
		List<OrcamentoEmpresa> listCodProd = new ArrayList<>();

		for (OrcamentoEmpresa saida : list) {
			if (saida.getCodEmpresa().equals(codCliente)) {
				listCodProd.add(saida);
			}
		}

		return listCodProd;
	}
	
	public OrcamentoEmpresa findById(Integer id) {
		List<OrcamentoEmpresa> list = orcamentoClienteDao.findAll();
		for (OrcamentoEmpresa saida : list) {
			if (saida.getId().equals(id)) {
			  return saida;
			}
		}
		return null;
	}
	
	public OrcamentoEmpresa findByNumOrcamento(Integer numOrcamento) {
		List<OrcamentoEmpresa> list = orcamentoClienteDao.findAll();

		for (OrcamentoEmpresa orc : list) {
			if (orc.getNumOrcamento() == numOrcamento) {
				return orc;
			}
		}
		return null;
	}
}
