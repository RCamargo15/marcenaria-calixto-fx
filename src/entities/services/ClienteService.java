package entities.services;

import java.util.List;

import marcenaria.dao.ClienteDao;
import marcenaria.dao.DaoFactory;
import marcenaria.entities.Cliente;

public class ClienteService {

	private ClienteDao clienteDao = DaoFactory.createClienteDao();

	public List<Cliente> findAll() {
		return clienteDao.findAll();
	}
	
	public void saveOrUpdate(Cliente obj) {
		if (obj.getCodCliente() == null) {
			clienteDao.insert(obj);
		}
		else {
			clienteDao.update(obj);
		}
	}
	
	public void removerCliente(Cliente obj) {
		clienteDao.deleteByCodCliente(obj.getCodCliente());
	}
	
	public Cliente findByCodCliente(Integer codCliente) {
		return clienteDao.findByCodCliente(codCliente);
	}
}
