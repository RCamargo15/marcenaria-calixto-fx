package marcenaria.dao;

import Db.Db;
import marcenaria.dao.implementacao.ClienteDaoJDBC;
import marcenaria.dao.implementacao.EmpresaDaoJDBC;
import marcenaria.dao.implementacao.EntradaProdutoDaoJDBC;
import marcenaria.dao.implementacao.EstoqueDaoJDBC;
import marcenaria.dao.implementacao.FornecedorDaoJDBC;
import marcenaria.dao.implementacao.FuncionarioDaoJDBC;
import marcenaria.dao.implementacao.NotasComprasDaoJDBC;
import marcenaria.dao.implementacao.OrcamentoClienteDaoJDBC;
import marcenaria.dao.implementacao.OrcamentoEmpresaDaoJDBC;
import marcenaria.dao.implementacao.OrdemServicoClienteDaoJDBC;
import marcenaria.dao.implementacao.OrdemServicoEmpresaDaoJDBC;
import marcenaria.dao.implementacao.ProdutoDaoJDBC;
import marcenaria.dao.implementacao.SaidaProdutoDaoJDBC;

public class DaoFactory {

	public static ClienteDao createClienteDao() {
		return new ClienteDaoJDBC(Db.getConnection());
	}
	
	public static EmpresaDao createEmpresaDao() {
		return new EmpresaDaoJDBC(Db.getConnection());
	}
	
	public static FornecedorDao createFornecedorDao() {
		return new FornecedorDaoJDBC(Db.getConnection());
	}
	
	public static FuncionarioDao createFuncionarioDao() {
		return new FuncionarioDaoJDBC(Db.getConnection());
	}
	
	public static ProdutoDao createProdutoDao() {
		return new ProdutoDaoJDBC(Db.getConnection());
	}
	
	public static NotasComprasDao createNotasCompras() {
		return new NotasComprasDaoJDBC(Db.getConnection());
	}
	
	public static EstoqueDao createEstoqueDao() {
		return new EstoqueDaoJDBC(Db.getConnection());
	}
	public static EntradaProdutoDao createEntradaProduto() {
		return new EntradaProdutoDaoJDBC(Db.getConnection());
	}
	
	public static SaidaProdutoDao createSaidaProduto() {
		return new SaidaProdutoDaoJDBC(Db.getConnection());
	}
	
	public static OrdemServicoClienteDao createOrdemServicoClienteDao() {
		return new OrdemServicoClienteDaoJDBC(Db.getConnection());
	}
	
	public static OrdemServicoEmpresaDao createOrdemServicoEmpresaDao() {
		return new OrdemServicoEmpresaDaoJDBC(Db.getConnection());
	}
	
	public static OrcamentoClienteDao createOrcamentoClienteDao() {
		return new OrcamentoClienteDaoJDBC(Db.getConnection());
	}
	
	public static OrcamentoEmpresaDao createOrcamentoEmpresaDao() {
		return new OrcamentoEmpresaDaoJDBC(Db.getConnection());
	}
}
