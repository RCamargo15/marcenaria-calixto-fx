package marcenaria.dao;

import java.util.List;

import marcenaria.entities.Cliente;

public interface ClienteDao {

	void insert(Cliente obj);

	void update(Cliente obj);

	void deleteByCodCliente(Integer codCliente);

	List<Cliente> findByNomeCliente(String nomeCliente);

	List<Cliente> findAll();

	Cliente findByCodCliente(Integer codCliente);
}
