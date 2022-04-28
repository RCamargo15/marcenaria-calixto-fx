package entities.services;

import java.util.List;

import marcenaria.dao.DaoFactory;
import marcenaria.dao.EmpresaDao;
import marcenaria.entities.Empresa;

public class EmpresaService {

	private EmpresaDao empresaDao = DaoFactory.createEmpresaDao();

	public List<Empresa> findAll() {
		return empresaDao.findAll();
	}
	
	public void saveOrUpdate(Empresa obj) {
		if (obj.getCodEmpresa() == null) {
			empresaDao.insert(obj);
		}
		else {
			empresaDao.update(obj);
		}
	}
	
	public void removerEmpresa(Empresa obj) {
		empresaDao.deleteByCodEmpresa(obj.getCodEmpresa());
	}
	
	public Empresa findByCodEmpresa(Integer codEmpresa) {
		return empresaDao.findByCodEmpresa(codEmpresa);
	}
}
