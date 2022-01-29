package marcenaria.dao;

import java.util.List;

import marcenaria.entities.Fornecedor;

public interface FornecedorDao {

	void insert(Fornecedor obj);

	void update(Fornecedor obj);

	void deleteByCodFornecedor(Integer codFornecedor);

	Fornecedor findByCodFornecedor(Integer codFornecedor);

	List<Fornecedor> findAll();
}
