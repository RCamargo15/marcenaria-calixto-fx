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
import marcenaria.dao.FornecedorDao;
import marcenaria.entities.Fornecedor;

public class FornecedorDaoJDBC implements FornecedorDao {

	private Connection conn;

	public FornecedorDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Fornecedor obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO MARCENARIA.FORNECEDOR"
					+ "(RAZAO_SOCIAL, NOME_FANTASIA, CNPJ, ATIVIDADE_FIM, RUA, NUMERO, COMPLEMENTO, BAIRRO, CEP, DDD, TELEFONE, SITE, EMAIL, OBS)"
					+ "VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getRazaoSocial().toUpperCase());
			st.setString(2, obj.getNomeFantasia().toUpperCase());
			st.setString(3, obj.getCnpj());
			st.setString(4, obj.getAtividadeFim().toUpperCase());
			st.setString(5, obj.getRua().toUpperCase());
			st.setInt(6, obj.getNumero());
			st.setString(7, obj.getComplemento());
			if (obj.getComplemento() != null) {
				st.setString(7, obj.getComplemento().toUpperCase());
			}
			st.setString(8, obj.getBairro().toUpperCase());
			st.setString(9, obj.getCep());
			st.setInt(10, obj.getDdd());
			st.setString(11, obj.getTelefone());
			st.setString(12, obj.getSite());
			st.setString(13, obj.getEmail());
			st.setString(14, obj.getObs());
			if (obj.getObs() != null) {
				st.setString(14, obj.getObs().toUpperCase());
			}

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codFornecedorGerado = rs.getInt(1);
					obj.setCodFornecedor(codFornecedorGerado);
				}
			} else {
				throw new DbException("Erro. Nenhum fornecedor foi cadastrado!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public void update(Fornecedor obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE MARCENARIA.FORNECEDOR "
					+ "SET RAZAO_SOCIAL = ?, NOME_FANTASIA = ?, CNPJ = ?, ATIVIDADE_FIM = ?, RUA = ?, NUMERO = ?, COMPLEMENTO = ?, BAIRRO = ?, CEP = ?, "
					+ "DDD = ?, TELEFONE = ?, SITE= ?, EMAIL = ?, OBS = ?" + " WHERE COD_FORNECEDOR = ?");

			st.setString(1, obj.getRazaoSocial().toUpperCase());
			st.setString(2, obj.getNomeFantasia().toUpperCase());
			st.setString(3, obj.getCnpj());
			st.setString(4, obj.getAtividadeFim().toUpperCase());
			st.setString(5, obj.getRua().toUpperCase());
			st.setInt(6, obj.getNumero());
			st.setString(7, obj.getComplemento());
			if (obj.getComplemento() != null) {
				st.setString(7, obj.getComplemento().toUpperCase());
			}
			st.setString(8, obj.getBairro().toUpperCase());
			st.setString(9, obj.getCep());
			st.setInt(10, obj.getDdd());
			st.setString(11, obj.getTelefone());
			st.setString(12, obj.getSite());
			st.setString(13, obj.getEmail());
			st.setString(14, obj.getObs());
			if (obj.getObs() != null) {
				st.setString(14, obj.getObs().toUpperCase());
			}
			st.setInt(15, obj.getCodFornecedor());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public void deleteByCodFornecedor(Integer codFornecedor) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM MARCENARIA.FORNECEDOR WHERE COD_FORNECEDOR = ?");
			st.setInt(1, codFornecedor);
			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public Fornecedor findByCodFornecedor(Integer codFornecedor) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.FORNECEDOR WHERE COD_FORNECEDOR= ? ");

			st.setInt(1, codFornecedor);
			rs = st.executeQuery();
			if (rs.next()) {
				Fornecedor obj = criarFornecedor(rs);
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
	public List<Fornecedor> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.FORNECEDOR ORDER BY COD_FORNECEDOR");
			rs = st.executeQuery();

			List<Fornecedor> lista = new ArrayList<>();

			while (rs.next()) {
				Fornecedor obj = criarFornecedor(rs);
				lista.add(obj);
			}
			return lista;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
			Db.closeResultSet(rs);
		}
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
		obj.setDdd(rs.getInt("DDD"));
		obj.setTelefone(rs.getString("TELEFONE"));
		obj.setSite(rs.getString("SITE"));
		obj.setEmail(rs.getString("EMAIL"));
		obj.setObs(rs.getString("OBS"));
		return obj;
	}
}
