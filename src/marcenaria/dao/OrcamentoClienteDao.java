package marcenaria.dao;

import java.util.List;

import marcenaria.entities.OrcamentoCliente;

public interface OrcamentoClienteDao {

	void insert(OrcamentoCliente obj);

	void update(OrcamentoCliente obj);

	void deleteByNumOrcamento(Integer numOrcamento);

	List <OrcamentoCliente> findByNumOrcamentoList(Integer numOrcamento);
	
	OrcamentoCliente findByNumOrcamento(Integer numOrcamento);

	List<OrcamentoCliente> findAll();
	
	List<OrcamentoCliente> findAllParaTabela();
}
