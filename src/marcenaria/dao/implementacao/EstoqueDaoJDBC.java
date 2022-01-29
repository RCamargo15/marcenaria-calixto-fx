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
import marcenaria.dao.EstoqueDao;
import marcenaria.entities.Estoque;
import marcenaria.entities.Produto;

public class EstoqueDaoJDBC implements EstoqueDao {

	private Connection conn;

	public EstoqueDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Estoque obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO MARCENARIA.ESTOQUE(COD_PRODUTO, ESTOQUE_ATUAL, ESTOQUE_MINIMO) " + "VALUES(?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getCodProduto().getCodProduto());
			st.setInt(2, obj.getEstoqueAtual());
			st.setInt(3, obj.getEstoqueMinimo());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int idGerado = rs.getInt(1);
					obj.setId(idGerado);
				}
			} else {
				throw new DbException("Nenhum produto foi inserido no estoque");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}

	}

	@Override
	public void update(Estoque obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE MARCENARIA.ESTOQUE "
					+ "SET COD_PRODUTO = ?, ESTOQUE_ATUAL = ?, ESTOQUE_MINIMO = ? WHERE ESTOQUE.ID = ?");

			st.setInt(1, obj.getCodProduto().getCodProduto());
			st.setInt(2, obj.getEstoqueAtual());
			st.setInt(3, obj.getEstoqueMinimo());
			st.setInt(4, obj.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

	}

	@Override
	public void deleteByCodEstoque(Integer codEstoque) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM MARCENARIA.ESTOQUE WHERE ESTOQUE.ID = ?");

			st.setInt(1, codEstoque);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public Estoque findByCodEstoque(Integer codEstoque) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM MARCENARIA.ESTOQUE INNER JOIN PRODUTO ON PRODUTO.COD_PRODUTO = ESTOQUE.COD_PRODUTO WHERE ESTOQUE.ID=?");
			st.setInt(1, codEstoque);
			rs = st.executeQuery();

			Map<Integer, Produto> produtoMap = new HashMap<>();

			if (rs.next()) {

				Produto produto = produtoMap.get(rs.getInt("COD_PRODUTO"));
				if (produto == null) {
					produto = criarProduto(rs);
					produtoMap.put(rs.getInt("COD_PRODUTO"), produto);
				}

				Estoque obj = criarEstoque(rs, produto);
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
	public Estoque findByCodProduto(Integer codProduto) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM MARCENARIA.ESTOQUE INNER JOIN PRODUTO ON PRODUTO.COD_PRODUTO = ESTOQUE.COD_PRODUTO WHERE ESTOQUE.COD_PRODUTO = ?");
			st.setInt(1, codProduto);

			rs = st.executeQuery();

			Map<Integer, Produto> produtoMap = new HashMap<>();

			if (rs.next()) {
				Produto produto = produtoMap.get(rs.getInt("COD_PRODUTO"));
				if (produto == null) {
					produto = criarProduto(rs);
					produtoMap.put(rs.getInt("COD_PRODUTO"), produto);
				}

				Estoque obj = criarEstoque(rs, produto);
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
	public List<Estoque> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM MARCENARIA.ESTOQUE INNER JOIN PRODUTO ON PRODUTO.COD_PRODUTO = ESTOQUE.COD_PRODUTO ORDER BY ID");

			rs = st.executeQuery();

			List<Estoque> listaEstoque = new ArrayList<>();
			Map<Integer, Produto> produtoMap = new HashMap<>();

			while (rs.next()) {

				Produto produto = produtoMap.get(rs.getInt("COD_PRODUTO"));

				if (produto == null) {
					produto = criarProduto(rs);
					produtoMap.put(rs.getInt("COD_PRODUTO"), produto);
				}

				Estoque obj = criarEstoque(rs, produto);
				listaEstoque.add(obj);
			}
			return listaEstoque;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
			Db.closeResultSet(rs);
		}
	}

	private Estoque criarEstoque(ResultSet rs, Produto prod) throws SQLException {
		Estoque obj = new Estoque();
		obj.setId(rs.getInt("ID"));
		obj.setCodProduto(prod);
		obj.setEstoqueAtual(rs.getInt("ESTOQUE_ATUAL"));
		obj.setEstoqueMinimo(rs.getInt("ESTOQUE_MINIMO"));
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
