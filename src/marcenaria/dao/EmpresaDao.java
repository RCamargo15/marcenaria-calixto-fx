package marcenaria.dao;

import java.util.List;

import marcenaria.entities.Empresa;

public interface EmpresaDao {

	void insert(Empresa obj);

	void update(Empresa obj);

	void deleteByCodEmpresa(Integer codEmpresa);

	Empresa findByCodEmpresa(Integer codEmpresa);

	List<Empresa> findAll();
}
