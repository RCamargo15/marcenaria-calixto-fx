package entities.services;

import java.util.List;

import marcenaria.dao.DaoFactory;
import marcenaria.dao.FuncionarioDao;
import marcenaria.entities.Funcionario;

public class FuncionarioService {

	private FuncionarioDao funcionarioDao = DaoFactory.createFuncionarioDao();

	public List<Funcionario> findAll() {
		return funcionarioDao.findAll();
	}
	
	public void saveOrUpdate(Funcionario obj) {
		if (obj.getRegistroFunc() == null) {
			funcionarioDao.insert(obj);
		}
		else {
			funcionarioDao.update(obj);
		}
	}
	
	public void removerFuncionario(Funcionario obj) {
		funcionarioDao.deleteByCodFuncionario(obj.getRegistroFunc());
	}
	
	public List<Funcionario> findByNomeFuncionario(String nomeFuncionario) {
		return funcionarioDao.findByNomeFuncionario(nomeFuncionario);
	}
	
	public Funcionario findByCodFuncionario(Integer codFuncionario) {
		return funcionarioDao.findByCodFuncionario(codFuncionario);
	}
}
