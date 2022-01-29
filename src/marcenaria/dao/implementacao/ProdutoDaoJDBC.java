package marcenaria.dao.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Db.Db;
import Db.DbException;
import marcenaria.dao.ProdutoDao;
import marcenaria.entities.Produto;

public class ProdutoDaoJDBC implements ProdutoDao {

	private Connection conn;

	public ProdutoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Produto obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO MARCENARIA.PRODUTO(DESC_PRODUTO, PRECO_UNIT) " + "VALUES(?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getDescProduto().toUpperCase());
			st.setDouble(2, obj.getPrecoUnit());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codProdutoGerado = rs.getInt(1);
					obj.setCodProduto(codProdutoGerado);
				} else {
					throw new DbException("Nenhum produto foi cadastrado!");
				}
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public void update(Produto obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE MARCENARIA.PRODUTO SET " + "DESC_PRODUTO = ?, PRECO_UNIT = ? WHERE PRODUTO.COD_PRODUTO = ?");
			st.setString(1, obj.getDescProduto().toUpperCase());
			st.setDouble(2, obj.getPrecoUnit());
			st.setInt(3, obj.getCodProduto());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}

	}

	@Override
	public void deleteByCodProduto(Integer codProduto) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM MARCENARIA.PRODUTO WHERE PRODUTO.COD_PRODUTO = ?");
			st.setInt(1, codProduto);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}

	}

	@Override
	public Produto findByCodProduto(Integer codProduto) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.PRODUTO WHERE COD_PRODUTO = ? ORDER BY COD_PRODUTO");

			st.setInt(1, codProduto);

			rs = st.executeQuery();
			if (rs.next()) {
				Produto obj = criarProduto(rs);
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
	public List<Produto> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.PRODUTO");

			rs = st.executeQuery();

			List<Produto> listaProdutos = new ArrayList<>();

			while (rs.next()) {
				Produto obj = criarProduto(rs);
				listaProdutos.add(obj);
			}
			return listaProdutos;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
			Db.closeResultSet(rs);
		}

	}

	private Produto criarProduto(ResultSet rs) throws SQLException {
		Produto obj = new Produto();
		obj.setCodProduto(rs.getInt("COD_PRODUTO"));
		obj.setDescProduto(rs.getString("DESC_PRODUTO"));
		obj.setPrecoUnit(rs.getDouble("PRECO_UNIT"));
		return obj;
	}

}
