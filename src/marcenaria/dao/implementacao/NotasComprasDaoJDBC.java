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
import marcenaria.dao.NotasComprasDao;
import marcenaria.entities.Fornecedor;
import marcenaria.entities.NotasCompras;
import marcenaria.entities.Produto;

public class NotasComprasDaoJDBC implements NotasComprasDao {

	private Connection conn;

	public NotasComprasDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(NotasCompras obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO MARCENARIA.NOTA_COMPRA_MATERIAL(COD_FORNECEDOR, NUMERO_NF, COD_PRODUTO, QUANTIDADE, VALOR_UNIT, VALOR_TOTAL, VALOR_TOTAL_NOTA, "
							+ "CHAVE_NF, DATA_EMISSAO, OBS)" + "VALUES(?, ?, ? ,?, ?, ?, ?, ?, ? ,?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getCodFornecedor().getCodFornecedor());
			st.setString(2, obj.getNumeroNF().toUpperCase());
			st.setInt(3, obj.getCodProduto().getCodProduto());
			st.setInt(4, obj.getQuantidade());
			st.setDouble(5, obj.getValorUnit());
			st.setDouble(6, obj.getValorTotal());
			st.setDouble(7, obj.getValorTotalNota());
			st.setString(8, obj.getChaveNF());
			st.setDate(9, new java.sql.Date(obj.getDataEmissao().getTime()));
			st.setString(10, obj.getObs());
			if (obj.getObs() != null) {
				st.setString(10, obj.getObs().toUpperCase());
			}

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codNotasComprasGerado = rs.getInt(1);
					obj.setCodNota(codNotasComprasGerado);
				} else {
					throw new DbException("Nenhuma nota de compra foi cadastrada no sistema!");
				}
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public void update(NotasCompras obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE MARCENARIA.NOTA_COMPRA_MATERIAL"
					+ " SET COD_FORNECEDOR = ?, NUMERO_NF=?, COD_PRODUTO = ?, QUANTIDADE = ?, VALOR_UNIT = ?, VALOR_TOTAL = ?, VALOR_TOTAL_NOTA = ?, CHAVE_NF = ?, "
					+ "DATA_EMISSAO = ?, OBS = ? WHERE COD_NOTA = ?");

			st.setInt(1, obj.getCodFornecedor().getCodFornecedor());
			st.setString(2, obj.getNumeroNF());
			st.setInt(3, obj.getCodProduto().getCodProduto());
			st.setInt(4, obj.getQuantidade());
			st.setDouble(5, obj.getValorUnit());
			st.setDouble(6, obj.getValorTotal());
			st.setDouble(7, obj.getValorTotalNota());
			st.setString(8, obj.getChaveNF());
			st.setDate(9, new java.sql.Date(obj.getDataEmissao().getTime()));
			st.setString(10, obj.getObs());
			if (obj.getObs() != null) {
				st.setString(10, obj.getObs().toUpperCase());
			}
			st.setInt(11, obj.getCodNota());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}

	}

	@Override
	public void deleteByCodNotasCompras(Integer codNotasCompras) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM MARCENARIA.NOTA_COMPRA_MATERIAL WHERE COD_NOTA = ?");
			st.setInt(1, codNotasCompras);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public void deleteByNumeroNF(String numeroNF) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM MARCENARIA.NOTA_COMPRA_MATERIAL WHERE NUMERO_NF = ?");
			st.setString(1, numeroNF);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public NotasCompras findByCodNotasCompras(Integer codNotasCompras) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT * FROM NOTA_COMPRA_MATERIAL INNER JOIN FORNECEDOR ON FORNECEDOR.COD_FORNECEDOR = NOTA_COMPRA_MATERIAL.COD_FORNECEDOR "
							+ "INNER JOIN PRODUTO ON PRODUTO.COD_PRODUTO = NOTA_COMPRA_MATERIAL.COD_PRODUTO WHERE COD_NOTA = ?");

			st.setInt(1, codNotasCompras);

			rs = st.executeQuery();

			Map<Integer, Fornecedor> fornecedorMap = new HashMap<>();
			Map<Integer, Produto> produtoMap = new HashMap<>();

			if (rs.next()) {

				Fornecedor fornecedor = fornecedorMap.get(rs.getInt("COD_FORNECEDOR"));
				if (fornecedor == null) {
					fornecedor = criarFornecedor(rs);
					fornecedorMap.put(rs.getInt("COD_FORNECEDOR"), fornecedor);
				}

				Produto produto = produtoMap.get(rs.getInt("COD_PRODUTO"));
				if (produto == null) {
					produto = criarProduto(rs);
					produtoMap.put(rs.getInt("COD_PRODUTO"), produto);
				}

				NotasCompras obj = criarNotasCompras(rs, fornecedor, produto);
				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
			Db.closeResultSet(rs);
		}
	}

	@Override
	public List<NotasCompras> findByNumeroNF(String numeroNF) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT * FROM NOTA_COMPRA_MATERIAL INNER JOIN FORNECEDOR ON FORNECEDOR.COD_FORNECEDOR = NOTA_COMPRA_MATERIAL.COD_FORNECEDOR "
							+ "INNER JOIN PRODUTO ON PRODUTO.COD_PRODUTO = NOTA_COMPRA_MATERIAL.COD_PRODUTO WHERE NUMERO_NF = ?");

			st.setString(1, numeroNF);

			rs = st.executeQuery();

			List<NotasCompras> listaNotasCompras = new ArrayList<>();
			Map<Integer, Fornecedor> fornecedorMap = new HashMap<>();
			Map<Integer, Produto> produtoMap = new HashMap<>();

			while (rs.next()) {

				Fornecedor fornecedor = fornecedorMap.get(rs.getInt("COD_FORNECEDOR"));
				if (fornecedor == null) {
					fornecedor = criarFornecedor(rs);
					fornecedorMap.put(rs.getInt("COD_FORNECEDOR"), fornecedor);
				}

				Produto produto = produtoMap.get(rs.getInt("COD_PRODUTO"));
				if (produto == null) {
					produto = criarProduto(rs);
					produtoMap.put(rs.getInt("COD_PRODUTO"), produto);
				}

				NotasCompras obj = criarNotasCompras(rs, fornecedor, produto);
				listaNotasCompras.add(obj);
			}
			return listaNotasCompras;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
			Db.closeResultSet(rs);
		}
	}

	@Override
	public List<NotasCompras> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT * FROM NOTA_COMPRA_MATERIAL INNER JOIN FORNECEDOR ON FORNECEDOR.COD_FORNECEDOR = NOTA_COMPRA_MATERIAL.COD_FORNECEDOR "
							+ "INNER JOIN PRODUTO ON PRODUTO.COD_PRODUTO = NOTA_COMPRA_MATERIAL.COD_PRODUTO");

			rs = st.executeQuery();

			List<NotasCompras> listaNotasCompras = new ArrayList<>();
			Map<Integer, Fornecedor> fornecedorMap = new HashMap<>();
			Map<Integer, Produto> produtoMap = new HashMap<>();

			while (rs.next()) {

				Fornecedor fornecedor = fornecedorMap.get(rs.getInt("COD_FORNECEDOR"));
				if (fornecedor == null) {
					fornecedor = criarFornecedor(rs);
					fornecedorMap.put(rs.getInt("COD_FORNECEDOR"), fornecedor);
				}

				Produto produto = produtoMap.get(rs.getInt("COD_PRODUTO"));
				if (produto == null) {
					produto = criarProduto(rs);
					produtoMap.put(rs.getInt("COD_PRODUTO"), produto);
				}

				NotasCompras obj = criarNotasCompras(rs, fornecedor, produto);
				listaNotasCompras.add(obj);
			}
			return listaNotasCompras;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
			Db.closeResultSet(rs);
		}

	}

	private NotasCompras criarNotasCompras(ResultSet rs, Fornecedor fornecedor, Produto produto) throws SQLException {
		NotasCompras obj = new NotasCompras();
		obj.setCodNota(rs.getInt("COD_NOTA"));
		obj.setCodFornecedor(fornecedor);
		obj.setNumeroNF(rs.getString("NUMERO_NF"));
		obj.setCodProduto(produto);
		obj.setQuantidade(rs.getInt("QUANTIDADE"));
		obj.setValorUnit(rs.getDouble("VALOR_UNIT"));
		obj.setValorTotal(rs.getDouble("VALOR_TOTAL"));
		obj.setValorTotalNota(rs.getDouble("VALOR_TOTAL_NOTA"));
		obj.setChaveNF(rs.getString("CHAVE_NF"));
		obj.setDataEmissao(rs.getDate("DATA_EMISSAO"));
		obj.setObs(rs.getString("OBS"));
		return obj;
	}

	private Fornecedor criarFornecedor(ResultSet rs) throws SQLException {
		Fornecedor obj = new Fornecedor();
		obj.setCodFornecedor(rs.getInt("COD_FORNECEDOR"));
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
