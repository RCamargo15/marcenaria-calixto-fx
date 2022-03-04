package marcenaria.dao;

import java.util.List;

import marcenaria.entities.OrcamentoCliente;
import marcenaria.entities.Produto;

public interface OrcamentoClienteDao {

	void insert(OrcamentoCliente obj);

	void update(OrcamentoCliente obj);

	void deleteByNumOrcamento(Integer numOrcamento);

	OrcamentoCliente findByNumOrcamento(Integer numOrcamento);

	List<OrcamentoCliente> findAll();
}
