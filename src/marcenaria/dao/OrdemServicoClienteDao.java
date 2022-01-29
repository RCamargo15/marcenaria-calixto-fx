package marcenaria.dao;

import java.util.List;

import marcenaria.entities.OrdemServicoCliente;

public interface OrdemServicoClienteDao {

	void insert(OrdemServicoCliente obj);

	void update(OrdemServicoCliente obj);

	void deleteByNumPedido(Integer numPedido);

	OrdemServicoCliente findByNumPedido(Integer NumPedido);
	
	List<OrdemServicoCliente> findByCodCliente(Integer codCliente);

	List<OrdemServicoCliente> findAll();
	
	List<OrdemServicoCliente> findByStatus(String status);
}
