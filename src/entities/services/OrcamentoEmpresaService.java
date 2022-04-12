package entities.services;

import java.util.ArrayList;
import java.util.List;

import marcenaria.dao.DaoFactory;
import marcenaria.dao.OrcamentoEmpresaDao;
import marcenaria.entities.OrcamentoEmpresa;

public class OrcamentoEmpresaService {

	private OrcamentoEmpresaDao orcamentoEmpresaDao = DaoFactory.createOrcamentoEmpresaDao();

	public List<OrcamentoEmpresa> findAll() {
		return orcamentoEmpresaDao.findAll();
	}
	
	public List<OrcamentoEmpresa> findAllParaTabela(){
		return orcamentoEmpresaDao.findAllParaTabela();
	}
	
	public List<OrcamentoEmpresa> findByNumOrcamentoList(Integer numOrcamento){
		return orcamentoEmpresaDao.findByNumOrcamentoList(numOrcamento);
	}

	public void saveOrUpdate(OrcamentoEmpresa obj) {
			if (obj.getId() == null) {
				orcamentoEmpresaDao.insert(obj);
			} else {
				orcamentoEmpresaDao.update(obj);
			}
	}
	
	public void saveOrcamento(OrcamentoEmpresa obj) {
		orcamentoEmpresaDao.updateOrcamento(obj);
	}

	public void removerOrcamento(OrcamentoEmpresa obj) {
		orcamentoEmpresaDao.deleteByNumOrcamento(obj.getNumOrcamento());
	}
	
	public void removerProduto(OrcamentoEmpresa obj) {
		orcamentoEmpresaDao.deleteById(obj.getId());
	}


	public List<OrcamentoEmpresa> findByCodEmpresa(Integer codCliente) {
		List<OrcamentoEmpresa> list = orcamentoEmpresaDao.findAll();
		List<OrcamentoEmpresa> listCodProd = new ArrayList<>();

		for (OrcamentoEmpresa saida : list) {
			if (saida.getCodEmpresa().equals(codCliente)) {
				listCodProd.add(saida);
			}
		}

		return listCodProd;
	}
	
	public OrcamentoEmpresa findById(Integer id) {
		List<OrcamentoEmpresa> list = orcamentoEmpresaDao.findAll();
		for (OrcamentoEmpresa saida : list) {
			if (saida.getId().equals(id)) {
			  return saida;
			}
		}
		return null;
	}
	
	public OrcamentoEmpresa findByNumOrcamento(Integer numOrcamento) {
		List<OrcamentoEmpresa> list = orcamentoEmpresaDao.findAll();

		for (OrcamentoEmpresa orc : list) {
			if (orc.getNumOrcamento() == numOrcamento) {
				return orc;
			}
		}
		return null;
	}
}
