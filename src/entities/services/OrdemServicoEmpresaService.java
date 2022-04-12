package entities.services;

import java.util.List;

import marcenaria.dao.DaoFactory;
import marcenaria.dao.OrdemServicoEmpresaDao;
import marcenaria.entities.OrdemServicoEmpresa;

public class OrdemServicoEmpresaService {

	private OrdemServicoEmpresaDao ordemServicoEmpresaDao = DaoFactory.createOrdemServicoEmpresaDao();

	public List<OrdemServicoEmpresa> findAll() {
		return ordemServicoEmpresaDao.findAll();
	}
	
	public void saveOrUpdate(OrdemServicoEmpresa obj) {
		if (obj.getId() == null) {
			ordemServicoEmpresaDao.insert(obj);
		}
		else {
			ordemServicoEmpresaDao.update(obj);
		}
	}
	
	public void removerOrdemServicoEmpresa(OrdemServicoEmpresa obj) {
		ordemServicoEmpresaDao.deleteByNumPedido(obj.getNumeroPedido());
	}
	
	public OrdemServicoEmpresa findByCodOrdemServicoEmpresa(Integer codOrdemServicoEmpresa) {
		List<OrdemServicoEmpresa> list = ordemServicoEmpresaDao.findAll();
		
		for (OrdemServicoEmpresa produto: list) {
			if (produto.getNumeroPedido() == codOrdemServicoEmpresa) {
				return produto;
			}
		}
		return null;
	}
}
