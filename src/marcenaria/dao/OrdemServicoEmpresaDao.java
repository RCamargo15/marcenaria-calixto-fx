package marcenaria.dao;

import java.util.List;

import marcenaria.entities.OrdemServicoEmpresa;

public interface OrdemServicoEmpresaDao {

	void insert(OrdemServicoEmpresa obj);

	void update(OrdemServicoEmpresa obj);

	void deleteByNumPedido(Integer numPedido);

	OrdemServicoEmpresa findByNumPedido(Integer NumPedido);
	
	List<OrdemServicoEmpresa> findByCodEmpresa(Integer codEmpresa);
	
	List<OrdemServicoEmpresa> findByStatus(String status);
	
	List<OrdemServicoEmpresa> findAll();
}
