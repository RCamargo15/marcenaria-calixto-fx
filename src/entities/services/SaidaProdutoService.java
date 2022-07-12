package entities.services;

import java.util.List;

import marcenaria.dao.DaoFactory;
import marcenaria.dao.SaidaProdutoDao;
import marcenaria.entities.SaidaProduto;

public class SaidaProdutoService {

	private SaidaProdutoDao saidaProdutoDao = DaoFactory.createSaidaProduto();
	
	public List<SaidaProduto> findAll(){
		return saidaProdutoDao.findAll();
	}
	
	public void saveOrUpdate(SaidaProduto obj) {
		if(obj.getCodSaida() == null) {
			saidaProdutoDao.insert(obj);
		}
		else {
			saidaProdutoDao.update(obj);
		}
	}
	
	public void removerSaida(SaidaProduto obj) {
		saidaProdutoDao.deleteByCodSaidaProduto(obj.getCodSaida());
	}
	
	public SaidaProduto findByCodSaida(Integer codSaida) {
		return saidaProdutoDao.findByCodSaidaProduto(codSaida);
	}
	
	public List<SaidaProduto> findByCodProduto(Integer codProduto) {
		return saidaProdutoDao.findByCodProduto(codProduto);
	}
	
	public List<SaidaProduto> findByNomeProduto(String nomeProduto) {
		return saidaProdutoDao.findByNomeProduto(nomeProduto);
	}
	
}
