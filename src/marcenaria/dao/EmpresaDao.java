package marcenaria.dao;

import java.util.List;

import marcenaria.entities.Empresa;

public interface EmpresaDao {

	void insert(Empresa obj);

	void update(Empresa obj);

	void deleteByCodEmpresa(Integer codEmpresa);

	List<Empresa> findByNomeEmpresa(String nomeEmpresa);

	List<Empresa> findAll();

	Empresa findByCodEmpresa(Integer codEmpresa);
}
