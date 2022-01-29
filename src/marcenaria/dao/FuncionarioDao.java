package marcenaria.dao;

import java.util.List;

import marcenaria.entities.Funcionario;

public interface FuncionarioDao {

	void insert(Funcionario obj);

	void update(Funcionario obj);

	void deleteByCodFuncionario(Integer codFuncionario);

	Funcionario findByCodFuncionario(Integer codFuncionario);

	List<Funcionario> findAll();
}
