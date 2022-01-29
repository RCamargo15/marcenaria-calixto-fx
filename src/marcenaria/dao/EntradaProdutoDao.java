package marcenaria.dao;

import java.util.List;

import marcenaria.entities.EntradaProduto;

public interface EntradaProdutoDao {

	void insert(EntradaProduto obj);

	void update(EntradaProduto obj);

	void deleteByCodEntradaProduto(Integer codEntradaProduto);

	EntradaProduto findByCodEntradaProduto(Integer codEntradaProduto);

	List<EntradaProduto> findAll();
	
	List<EntradaProduto> findByNumeroNF(String numeroNF);
	
	List<EntradaProduto> findByCodProd(Integer codProduto);
}
