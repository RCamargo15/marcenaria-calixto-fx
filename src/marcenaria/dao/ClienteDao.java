package marcenaria.dao;

import java.util.List;

import marcenaria.entities.Cliente;

public interface ClienteDao {

	void insert(Cliente obj);

	void update(Cliente obj);

	void deleteByCodCliente(Integer codCliente);

	Cliente findByCodCliente(Integer codCliente);

	List<Cliente> findAll();
}
