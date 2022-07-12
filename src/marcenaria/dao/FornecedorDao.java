package marcenaria.dao;

import java.util.List;

import marcenaria.entities.Fornecedor;

public interface FornecedorDao {

	void insert(Fornecedor obj);

	void update(Fornecedor obj);

	void deleteByCodFornecedor(Integer codFornecedor);

	List<Fornecedor> findByNomeFornecedor(String nomeFornecedor);

	List<Fornecedor> findAll();

	Fornecedor findByCodFornecedor(Integer codFornecedor);
}
