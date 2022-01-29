package marcenaria.dao;

import java.util.List;

import marcenaria.entities.SaidaProduto;

public interface SaidaProdutoDao {

	void insert(SaidaProduto obj);

	void update(SaidaProduto obj);

	void deleteByCodSaidaProduto(Integer codSaidaProduto);

	SaidaProduto findByCodSaidaProduto(Integer codSaidaProduto);
	
	List<SaidaProduto> findByCodProduto(Integer codProduto);

	List<SaidaProduto> findAll();
}
