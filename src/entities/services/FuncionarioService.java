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
	
	public Funcionario findByCodFuncionario(Integer codFuncionario) {
		List<Funcionario> list = funcionarioDao.findAll();
		
		for (Funcionario funcionario: list) {
			if (funcionario.getRegistroFunc() == codFuncionario) {
				return funcionario;
			}
		}
		return null;
	}
}
