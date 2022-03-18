package marcenaria.dao;

import java.util.List;

import marcenaria.entities.OrcamentoEmpresa;

public interface OrcamentoEmpresaDao {

	void insert(OrcamentoEmpresa obj);

	void update(OrcamentoEmpresa obj);
	
	void updateOrcamento(OrcamentoEmpresa obj);

	void deleteByNumOrcamento(Integer numOrcamento);
	
	void deleteById(Integer id);
	
	List<OrcamentoEmpresa> findByNumOrcamentoList(Integer numOrcamento);

	OrcamentoEmpresa findByNumOrcamento(Integer numOrcamento);
	
	OrcamentoEmpresa findById(Integer numOrcamento);

	List<OrcamentoEmpresa> findAll();
	
	List<OrcamentoEmpresa> findAllParaTabela();
}
