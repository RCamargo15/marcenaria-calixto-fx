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
	
	public List<NotasCompras> findAllParaTabela(){
		return notasComprasDao.findAllParaTabela();
	}
	
	public List<NotasCompras> findByNumeroNFList(String numeroNF){
		return notasComprasDao.findByNumeroNF(numeroNF);
	}
	
	public void saveOrUpdate(NotasCompras obj) {
			if (obj.getCodNota() == null) {
				notasComprasDao.insert(obj);
			} else {
				notasComprasDao.update(obj);
			}
	}
	
	public void saveNotaCompra(NotasCompras obj) {
		notasComprasDao.updateNotaCompra(obj);
	}
	
	public void updateProdutos(NotasCompras obj) {
		notasComprasDao.updateProduto(obj);
	}

	public void removerNotaCompra(NotasCompras obj) {
		notasComprasDao.deleteByNumeroNF(obj.getNumeroNF());
	}
	
	public void removerProduto(NotasCompras obj) {
		notasComprasDao.deleteByCodNotasCompras(obj.getCodNota());
	}
	
	public NotasCompras findByCodNota(Integer codNota) {
		List<NotasCompras> list = notasComprasDao.findAll();
		for (NotasCompras saida : list) {
			if (saida.getCodNota().equals(codNota)) {
			  return saida;
			}
		}
		return null;
	}
	
	public NotasCompras findByNumeroNFSingle(String numOrcamento) {
		List<NotasCompras> list = notasComprasDao.findAll();

		for (NotasCompras orc : list) {
			if (orc.getNumeroNF().equals(numOrcamento)) {
				return orc;
			}
		}
		return null;
	}
}
