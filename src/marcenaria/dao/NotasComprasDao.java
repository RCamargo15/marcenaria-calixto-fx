package marcenaria.dao;

import java.util.List;

import marcenaria.entities.NotasCompras;

public interface NotasComprasDao {

	void insert(NotasCompras obj);

	void update(NotasCompras obj);
	
	void updateProduto(NotasCompras obj);
	
	void updateNumeroNF(NotasCompras obj, String novoNumeroNF, String velhoNumero);

	void deleteByCodNotasCompras(Integer codNota);
	
	void deleteByNumeroNF(String numeroNF);

	NotasCompras findByCodNotasCompras(Integer codNota);
	
	List<NotasCompras> findByNumeroNF(String numeroNF);

	List<NotasCompras> findAll();
	
	List<NotasCompras> findAllParaTabela();
	
	
}
