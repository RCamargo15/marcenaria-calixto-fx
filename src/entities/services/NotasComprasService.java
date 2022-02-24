package entities.services;

import java.util.List;

import marcenaria.dao.DaoFactory;
import marcenaria.dao.NotasComprasDao;
import marcenaria.entities.NotasCompras;

public class NotasComprasService {

	private NotasComprasDao notasComprasDao = DaoFactory.createNotasCompras();

	public List<NotasCompras> findAll() {
		return notasComprasDao.findAll();
	}
	
	public void saveOrUpdate(NotasCompras obj) {
		if (obj.getCodNota() == null) {
			notasComprasDao.insert(obj);
		}
		else {
			notasComprasDao.update(obj);
		}
	}
	
	public void removerNotasCompras(NotasCompras obj) {
		notasComprasDao.deleteByCodNotasCompras(obj.getCodNota());
	}
	
	public NotasCompras findByCodNotasCompras(Integer codNotasCompras) {
		List<NotasCompras> list = notasComprasDao.findAll();
		
		for (NotasCompras produto: list) {
			if (produto.getCodNota() == codNotasCompras) {
				return produto;
			}
		}
		return null;
	}
}
