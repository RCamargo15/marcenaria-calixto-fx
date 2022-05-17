package marcenaria.dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Db.Db;
import Db.DbException;
import marcenaria.dao.OrcamentoEmpresaDao;
import marcenaria.entities.Empresa;
import marcenaria.entities.OrcamentoEmpresa;
import marcenaria.entities.Produto;

public class OrcamentoEmpresaDaoJDBC implements OrcamentoEmpresaDao {
	
	private Connection conn;
	
	public OrcamentoEmpresaDaoJDBC(Connection conn) {
		this.conn = conn; 
	}

	@Override
	public void insert(OrcamentoEmpresa obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO MARCENARIA.ORCAMENTO_EMPRESA(NUM_ORCAMENTO, COD_EMPRESA, NOME_RESPONSAVEL, TELEFONE, CELULAR, EMAIL, "
					+ "DESC_SERVICO, DATA_ORCAMENTO, COD_PRODUTO, QUANTIDADE, VALOR, VALOR_QUAD, VALOR_OBRA, VALOR_TOTAL, OBS)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			
	
			
			st.setInt(1, obj.getNumOrcamento());
			st.setInt(2, obj.getCodEmpresa().getCodEmpresa());
			st.setString(3, obj.getNomeResponsavel().toUpperCase());
			st.setString(4, obj.getTelefone());
			st.setString(5, obj.getCelular());
			st.setString(6, obj.getEmail());
			st.setString(7, obj.getDescServico().toUpperCase());
			st.setDate(8, new java.sql.Date(obj.getDataOrcamento().getTime()));
			st.setInt(9, obj.getCodProduto().getCodProduto());
			st.setInt(10, obj.getQuantidade());
			st.setDouble(11, obj.getValor());
			st.setDouble(12, obj.getValorMetroQuad());
			st.setDouble(13, obj.getValorObra());
			st.setDouble(14, obj.getValorTotal());
			st.setString(15, obj.getObs());
			if (obj.getObs() != null) {
				st.setString(15, obj.getObs().toUpperCase());
			}
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int numOrcamentoGerado = rs.getInt(1);
					obj.setNumOrcamento(numOrcamentoGerado);
				}
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			Db.closeStatement(st);
		}	
	}

	@Override
	public void update(OrcamentoEmpresa obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE MARCENARIA.ORCAMENTO_EMPRESA SET NUM_ORCAMENTO = ?, COD_EMPRESA = ?, NOME_RESPONSAVEL = ?, TELEFONE = ?, CELULAR = ?,"
					+ " EMAIL = ?, DESC_SERVICO = ?, DATA_ORCAMENTO = ?, COD_PRODUTO = ?, QUANTIDADE = ?, VALOR = ?, VALOR_QUAD = ?, VALOR_OBRA = ?, VALOR_TOTAL = ?, OBS = ? "
					+ " WHERE ID = ? ");
			
			st.setInt(1, obj.getNumOrcamento());
			st.setInt(2, obj.getCodEmpresa().getCodEmpresa());
			st.setString(3, obj.getNomeResponsavel().toUpperCase());
			st.setString(4, obj.getTelefone());
			st.setString(5, obj.getCelular());
			st.setString(6, obj.getEmail());
			st.setString(7, obj.getDescServico().toUpperCase());
			st.setDate(8, new java.sql.Date(obj.getDataOrcamento().getTime()));
			st.setInt(9, obj.getCodProduto().getCodProduto());
			st.setInt(10, obj.getQuantidade());
			st.setDouble(11, obj.getValor());
			st.setDouble(12, obj.getValorMetroQuad());
			st.setDouble(13, obj.getValorObra());
			st.setDouble(14, obj.getValorTotal());
			st.setString(15, obj.getObs());
			if (obj.getObs() != null) {
				st.setString(15, obj.getObs().toUpperCase());
			}
			st.setInt(16, obj.getId());
			
			st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			Db.closeStatement(st);
		}
	}
	
	@Override
	public void updateOrcamento(OrcamentoEmpresa obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE MARCENARIA.ORCAMENTO_EMPRESA SET NUM_ORCAMENTO = ?, COD_EMPRESA = ?, NOME_RESPONSAVEL = ?, TELEFONE = ?, CELULAR = ?,"
					+ " EMAIL = ?, DESC_SERVICO = ?, DATA_ORCAMENTO = ?, VALOR_TOTAL = ?, OBS = ? "
					+ " WHERE NUM_ORCAMENTO = ? ");
			
			st.setInt(1, obj.getNumOrcamento());
			st.setInt(2, obj.getCodEmpresa().getCodEmpresa());
			st.setString(3, obj.getNomeResponsavel());
			st.setString(4, obj.getTelefone());
			st.setString(5, obj.getCelular());
			st.setString(6, obj.getEmail());
			st.setString(7, obj.getDescServico());
			st.setDate(8, new java.sql.Date(obj.getDataOrcamento().getTime()));
			st.setDouble(9, obj.getValorTotal());
			st.setString(10, obj.getObs());
			st.setInt(11, obj.getNumOrcamento());
			
			st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public void deleteByNumOrcamento(Integer numOrcamento) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM MARCENARIA.ORCAMENTO_EMPRESA WHERE ORCAMENTO_EMPRESA.NUM_ORCAMENTO = ?");
			
			st.setInt(1, numOrcamento);
			st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			Db.closeStatement(st);
		}
	}
	
	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM MARCENARIA.ORCAMENTO_EMPRESA WHERE ORCAMENTO_EMPRESA.ID = ?");
			
			st.setInt(1, id);
			st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			Db.closeStatement(st);
		}
	}
	
	@Override
	public List<OrcamentoEmpresa> findByNumOrcamentoList(Integer numOrcamento) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.ORCAMENTO_EMPRESA "
					+ "INNER JOIN EMPRESA ON EMPRESA.COD_EMPRESA = ORCAMENTO_EMPRESA.COD_EMPRESA "
					+ "INNER JOIN PRODUTO ON PRODUTO.COD_PRODUTO = ORCAMENTO_EMPRESA.COD_EMPRESA "
					+ "WHERE NUM_ORCAMENTO = ?");
			
			st.setInt(1, numOrcamento);
			
			rs = st.executeQuery();
			
			Map<Integer, Empresa> empresaMap = new HashMap<>();
			Map<Integer, Produto> produtoMap = new HashMap<>();
			List<OrcamentoEmpresa> list = new ArrayList<>();
			
			while(rs.next()) {
				
				Empresa empresa = empresaMap.get(rs.getInt("COD_EMPRESA"));
				if(empresa == null) {
					empresa = criarEmpresa(rs);
					empresaMap.put(rs.getInt("COD_EMPRESA"), empresa);
				}
				
				Produto produto = produtoMap.get(rs.getInt("COD_PRODUTO"));
				if(produto == null) {
					produto = criarProduto(rs);
					produtoMap.put(rs.getInt("COD_PRODUTO"), produto);
				}
				
				OrcamentoEmpresa obj = criarOrcamentoEmpresa(rs, empresa, produto);
				list.add(obj);
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			Db.closeResultSet(rs);
			Db.closeStatement(st);
		}
	}

	@Override
	public OrcamentoEmpresa findByNumOrcamento(Integer numOrcamento) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.ORCAMENTO_EMPRESA "
					+ "INNER JOIN EMPRESA ON EMPRESA.COD_EMPRESA = ORCAMENTO_EMPRESA.COD_EMPRESA "
					+ "INNER JOIN PRODUTO ON PRODUTO.COD_PRODUTO = ORCAMENTO_EMPRESA.COD_EMPRESA "
					+ "WHERE NUM_ORCAMENTO = ?");
			
			st.setInt(1, numOrcamento);
			
			rs = st.executeQuery();
			
			Map<Integer, Empresa> empresaMap = new HashMap<>();
			Map<Integer, Produto> produtoMap = new HashMap<>();
			
			if(rs.next()) {
				
				Empresa empresa = empresaMap.get(rs.getInt("COD_EMPRESA"));
				if(empresa == null) {
					empresa = criarEmpresa(rs);
					empresaMap.put(rs.getInt("COD_EMPRESA"), empresa);
				}
				
				Produto produto = produtoMap.get(rs.getInt("COD_PRODUTO"));
				if(produto == null) {
					produto = criarProduto(rs);
					produtoMap.put(rs.getInt("COD_PRODUTO"), produto);
				}
				
				OrcamentoEmpresa obj = criarOrcamentoEmpresa(rs, empresa, produto);
				return obj;
			}
			return null;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			Db.closeResultSet(rs);
			Db.closeStatement(st);
		}
	}
	
	@Override
	public OrcamentoEmpresa findById(Integer numOrcamento) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.ORCAMENTO_EMPRESA "
					+ "INNER JOIN EMPRESA ON EMPRESA.COD_EMPRESA = ORCAMENTO_EMPRESA.COD_EMPRESA "
					+ "INNER JOIN PRODUTO ON PRODUTO.COD_PRODUTO = ORCAMENTO_EMPRESA.COD_EMPRESA "
					+ "WHERE ID = ?");
			
			st.setInt(1, numOrcamento);
			
			rs = st.executeQuery();
			
			Map<Integer, Empresa> empresaMap = new HashMap<>();
			Map<Integer, Produto> produtoMap = new HashMap<>();
			
			if(rs.next()) {
				
				Empresa empresa = empresaMap.get(rs.getInt("COD_EMPRESA"));
				if(empresa == null) {
					empresa = criarEmpresa(rs);
					empresaMap.put(rs.getInt("COD_EMPRESA"), empresa);
				}
				
				Produto produto = produtoMap.get(rs.getInt("COD_PRODUTO"));
				if(produto == null) {
					produto = criarProduto(rs);
					produtoMap.put(rs.getInt("COD_PRODUTO"), produto);
				}
				
				OrcamentoEmpresa obj = criarOrcamentoEmpresa(rs, empresa, produto);
				return obj;
			}
			return null;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			Db.closeResultSet(rs);
			Db.closeStatement(st);
		}
	}
	
	@Override
	public List<OrcamentoEmpresa> findAllParaTabela() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.ORCAMENTO_EMPRESA "
					+ "INNER JOIN EMPRESA ON EMPRESA.COD_EMPRESA = ORCAMENTO_EMPRESA.COD_EMPRESA "
					+ "INNER JOIN PRODUTO ON PRODUTO.COD_PRODUTO = ORCAMENTO_EMPRESA.COD_EMPRESA GROUP BY NUM_ORCAMENTO "
					);
			
			rs = st.executeQuery();
			
			Map<Integer, Empresa> empresaMap = new HashMap<>();
			Map<Integer, Produto> produtoMap = new HashMap<>();
			List<OrcamentoEmpresa> listEmpresa = new ArrayList<>();
			
			while(rs.next()) {
				
				Empresa empresa = empresaMap.get(rs.getInt("COD_EMPRESA"));
				if(empresa == null) {
					empresa = criarEmpresa(rs);
					empresaMap.put(rs.getInt("COD_EMPRESA"), empresa);
				}
				
				Produto produto = produtoMap.get(rs.getInt("COD_PRODUTO"));
				if(produto == null) {
					produto = criarProduto(rs);
					produtoMap.put(rs.getInt("COD_PRODUTO"), produto);
				}
				
				OrcamentoEmpresa obj = criarOrcamentoEmpresa(rs, empresa, produto);
				
				listEmpresa.add(obj);
			}
			return listEmpresa;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			Db.closeResultSet(rs);
			Db.closeStatement(st);
		}
	}

	@Override
	public List<OrcamentoEmpresa> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.ORCAMENTO_EMPRESA "
					+ "INNER JOIN EMPRESA ON EMPRESA.COD_EMPRESA = ORCAMENTO_EMPRESA.COD_EMPRESA "
					+ "INNER JOIN PRODUTO ON PRODUTO.COD_PRODUTO = ORCAMENTO_EMPRESA.COD_EMPRESA "
					);
			
			rs = st.executeQuery();
			
			Map<Integer, Empresa> empresaMap = new HashMap<>();
			Map<Integer, Produto> produtoMap = new HashMap<>();
			List<OrcamentoEmpresa> listEmpresa = new ArrayList<>();
			
			while(rs.next()) {
				
				Empresa empresa = empresaMap.get(rs.getInt("COD_EMPRESA"));
				if(empresa == null) {
					empresa = criarEmpresa(rs);
					empresaMap.put(rs.getInt("COD_EMPRESA"), empresa);
				}
				
				Produto produto = produtoMap.get(rs.getInt("COD_PRODUTO"));
				if(produto == null) {
					produto = criarProduto(rs);
					produtoMap.put(rs.getInt("COD_PRODUTO"), produto);
				}
				
				OrcamentoEmpresa obj = criarOrcamentoEmpresa(rs, empresa, produto);
				
				listEmpresa.add(obj);
			}
			return listEmpresa;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			Db.closeResultSet(rs);
			Db.closeStatement(st);
		}
	}
	
	private OrcamentoEmpresa criarOrcamentoEmpresa(ResultSet rs, Empresa empresa, Produto produto) throws SQLException {
		OrcamentoEmpresa obj = new OrcamentoEmpresa();
		obj.setId(rs.getInt("ID"));
		obj.setNumOrcamento(rs.getInt("NUM_ORCAMENTO"));
		obj.setCodEmpresa(empresa);
		obj.setNomeResponsavel(rs.getString("NOME_RESPONSAVEL"));
		obj.setTelefone(rs.getString("TELEFONE"));
		obj.setCelular(rs.getString("CELULAR"));
		obj.setEmail(rs.getString("EMAIL"));
		obj.setDescServico(rs.getString("DESC_SERVICO"));
		obj.setDataOrcamento(new java.util.Date(rs.getTimestamp("DATA_ORCAMENTO").getTime()));
		obj.setQuantidade(rs.getInt("QUANTIDADE"));
		obj.setValor(produto);
		obj.setValorObra(rs.getDouble("VALOR_OBRA"));
		obj.setValorMetroQuad(rs.getDouble("VALOR_QUAD"));
		obj.setValorTotal(rs.getDouble("VALOR_TOTAL"));
		obj.setObs(rs.getString("OBS"));
		return obj;
	}
	
	private Empresa criarEmpresa(ResultSet rs) throws SQLException {
		Empresa obj = new Empresa();
		obj.setCodEmpresa(rs.getInt("COD_EMPRESA"));
		obj.setRazaoSocial(rs.getString("RAZAO_SOCIAL"));
		obj.setNomeFantasia(rs.getString("NOME_FANTASIA"));
		obj.setCnpj(rs.getString("CNPJ"));
		obj.setAtividadeFim(rs.getString("ATIVIDADE_FIM"));
		obj.setRua(rs.getString("RUA"));
		obj.setNumero(rs.getInt("NUMERO"));
		obj.setComplemento(rs.getString("COMPLEMENTO"));
		obj.setBairro(rs.getString("BAIRRO"));
		obj.setCep(rs.getString("CEP"));
		obj.setCidade(rs.getString("CIDADE"));
		obj.setEstado(rs.getString("ESTADO"));
		obj.setUf(rs.getString("UF"));
		obj.setDdd(rs.getInt("DDD"));
		obj.setTelefone(rs.getString("TELEFONE"));
		obj.setSite(rs.getString("SITE"));
		obj.setEmail(rs.getString("EMAIL"));
		obj.setObs(rs.getString("OBS"));
		return obj;
	}
	
	private Produto criarProduto(ResultSet rs) throws SQLException {
		Produto obj = new Produto();
		obj.setCodProduto(rs.getInt("COD_PRODUTO"));
		obj.setDescProduto(rs.getString("DESC_PRODUTO"));
		obj.setPrecoUnit(rs.getDouble("PRECO_UNIT"));
		return obj;
	}


}
