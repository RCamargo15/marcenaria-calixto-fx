package entities.services;

import java.util.List;

import marcenaria.dao.DaoFactory;
import marcenaria.dao.OrdemServicoClienteDao;
import marcenaria.entities.OrdemServicoCliente;

public class OrdemServicoClienteService {

	private OrdemServicoClienteDao ordemServicoClienteDao = DaoFactory.createOrdemServicoClienteDao();

	public List<OrdemServicoCliente> findAll() {
		return ordemServicoClienteDao.findAll();
	}
	
	public void saveOrUpdate(OrdemServicoCliente obj) {
		if (obj.getId() == null) {
			ordemServicoClienteDao.insert(obj);
		}
		else {
			ordemServicoClienteDao.update(obj);
		}
	}
	
	public void removerOrdemServicoCliente(OrdemServicoCliente obj) {
		ordemServicoClienteDao.deleteByNumPedido(obj.getNumeroPedido());
	}
	
	public OrdemServicoCliente findByNumPedido(Integer codOrdemServicoCliente) {
		return ordemServicoClienteDao.findByNumPedido(codOrdemServicoCliente);
	}
}
