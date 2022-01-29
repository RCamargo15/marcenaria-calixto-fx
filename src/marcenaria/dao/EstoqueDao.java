package marcenaria.dao;

import java.util.List;

import marcenaria.entities.Estoque;

public interface EstoqueDao {

	void insert(Estoque obj);

	void update(Estoque obj);

	void deleteByCodEstoque(Integer codEstoque);

	Estoque findByCodEstoque(Integer codEstoque);
	
	Estoque findByCodProduto(Integer codProduto);

	List<Estoque> findAll();
}
