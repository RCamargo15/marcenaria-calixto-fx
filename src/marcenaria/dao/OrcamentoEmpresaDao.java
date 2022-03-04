package marcenaria.dao;

import java.util.List;

import marcenaria.entities.OrcamentoEmpresa;

public interface OrcamentoEmpresaDao {

	void insert(OrcamentoEmpresa obj);

	void update(OrcamentoEmpresa obj);

	void deleteByNumOrcamento(Integer numOrcamento);

	OrcamentoEmpresa findByNumOrcamento(Integer numOrcamento);

	List<OrcamentoEmpresa> findAll();
}
