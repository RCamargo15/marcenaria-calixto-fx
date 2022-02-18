package entities.services;

import java.util.ArrayList;
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
		List<SaidaProduto> list = saidaProdutoDao.findAll();
		
		for(SaidaProduto saida : list) {
			if (saida.getCodSaida() == codSaida) {
				return saida;
			}
		}
		return null;
	}
	
	public List<SaidaProduto> findByCodProduto(Integer codProduto) {
		List<SaidaProduto> list = saidaProdutoDao.findAll();
		List<SaidaProduto> listCodProd = new ArrayList<>();
		
		for(SaidaProduto saida : list) {
			if(saida.getCodProduto().equals(codProduto)) {
				listCodProd.add(saida);
			}
		}
		
		return listCodProd;
	}
	
}
