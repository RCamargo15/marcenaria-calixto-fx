package marcenaria.dao;

import java.util.List;

import marcenaria.entities.Funcionario;

public interface FuncionarioDao {

	void insert(Funcionario obj);

	void update(Funcionario obj);

	void deleteByCodFuncionario(Integer codFuncionario);

	List<Funcionario> findByNomeFuncionario(String nomeFuncionario);

	List<Funcionario> findAll();

	Funcionario findByCodFuncionario(Integer codFuncionario);
}
