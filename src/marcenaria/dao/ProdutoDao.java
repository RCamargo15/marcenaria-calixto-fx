package marcenaria.dao;

import java.util.List;

import marcenaria.entities.Produto;

public interface ProdutoDao {

	void insert(Produto obj);

	void update(Produto obj);

	void deleteByCodProduto(Integer codProduto);

	Produto findByCodProduto(Integer codProduto);

	List<Produto> findAll();
}
