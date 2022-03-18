package marcenaria.dao;

import java.util.List;

import marcenaria.entities.OrcamentoCliente;

public interface OrcamentoClienteDao {

	void insert(OrcamentoCliente obj);

	void update(OrcamentoCliente obj);
	
	void updateOrcamento(OrcamentoCliente obj);

	void deleteByNumOrcamento(Integer numOrcamento);
	
	void deleteById(Integer id);

	List <OrcamentoCliente> findByNumOrcamentoList(Integer numOrcamento);
	
	OrcamentoCliente findByNumOrcamento(Integer numOrcamento);
	
	OrcamentoCliente findById(Integer id);

	List<OrcamentoCliente> findAll();
	
	List<OrcamentoCliente> findAllParaTabela();
}
