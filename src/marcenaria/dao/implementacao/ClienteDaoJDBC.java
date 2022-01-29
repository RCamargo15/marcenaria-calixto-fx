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
import marcenaria.dao.ClienteDao;
import marcenaria.entities.Cliente;

public class ClienteDaoJDBC implements ClienteDao {

	private Connection conn;

	public ClienteDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Cliente obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO MARCENARIA.CLIENTE"
					+ "(NOME, RG, CPF, RUA, NUMERO, COMPLEMENTO, BAIRRO, CEP, DDD, TELEFONE, CELULAR, EMAIL, DATA_CADASTRO, OBS)"
					+ "VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome().toUpperCase());
			st.setString(2, obj.getRg());
			st.setString(3, obj.getCpf());
			st.setString(4, obj.getRua().toUpperCase());
			st.setInt(5, obj.getNumero());
			st.setString(6, obj.getComplemento());
			if (obj.getComplemento() != null) {
				st.setString(6, obj.getComplemento().toUpperCase());
			}
			st.setString(7, obj.getBairro().toUpperCase());
			st.setString(8, obj.getCep());
			st.setInt(9, obj.getDdd());
			st.setString(10, obj.getTelefone());
			st.setString(11, obj.getCelular());
			st.setString(12, obj.getEmail());
			st.setDate(13, new java.sql.Date(obj.getDataCadastro().getTime()));
			st.setString(14, obj.getObs());
			if (obj.getObs() != null) {
				st.setString(14, obj.getObs().toUpperCase());
			}

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codClienteGerado = rs.getInt(1);
					obj.setCodCliente(codClienteGerado);
				}
			} else {
				throw new DbException("Nenhum cliente foi cadastrado!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public void update(Cliente obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE MARCENARIA.CLIENTE "
					+ "SET NOME = ?, RG = ?, CPF = ?, RUA = ?, NUMERO = ?, COMPLEMENTO = ?, BAIRRO = ?, CEP = ?, DDD = ?, "
					+ "TELEFONE = ?, CELULAR = ?, EMAIL = ?, DATA_CADASTRO = ?, OBS = ?" + "WHERE COD_CLIENTE = ?");

			st.setString(1, obj.getNome().toUpperCase());
			st.setString(2, obj.getRg());
			st.setString(3, obj.getCpf());
			st.setString(4, obj.getRua().toUpperCase());
			st.setInt(5, obj.getNumero());
			st.setString(6, obj.getComplemento());
			if (obj.getComplemento() != null) {
				st.setString(6, obj.getComplemento().toUpperCase());
			}
			st.setString(7, obj.getBairro());
			st.setString(8, obj.getCep());
			st.setInt(9, obj.getDdd());
			st.setString(10, obj.getTelefone());
			st.setString(11, obj.getCelular());
			st.setString(12, obj.getEmail());
			st.setDate(13, new java.sql.Date(obj.getDataCadastro().getTime()));
			st.setString(14, obj.getObs());
			if (obj.getObs() != null) {
				st.setString(14, obj.getObs().toUpperCase());
			}
			st.setInt(15, obj.getCodCliente());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public void deleteByCodCliente(Integer codCliente) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM MARCENARIA.CLIENTE WHERE COD_CLIENTE = ?");
			st.setInt(1, codCliente);
			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public Cliente findByCodCliente(Integer codCliente) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.CLIENTE WHERE COD_CLIENTE = ? ");

			st.setInt(1, codCliente);
			rs = st.executeQuery();
			if (rs.next()) {
				Cliente obj = criarCliente(rs);
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
	public List<Cliente> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.CLIENTE ORDER BY COD_CLIENTE");
			rs = st.executeQuery();

			List<Cliente> lista = new ArrayList<>();

			while (rs.next()) {
				Cliente obj = criarCliente(rs);
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

	private Cliente criarCliente(ResultSet rs) throws SQLException {
		Cliente obj = new Cliente();
		obj.setCodCliente(rs.getInt("COD_CLIENTE"));
		obj.setNome(rs.getString("NOME"));
		obj.setRg(rs.getString("RG"));
		obj.setCpf(rs.getString("CPF"));
		obj.setRua(rs.getString("RUA"));
		obj.setNumero(rs.getInt("NUMERO"));
		obj.setComplemento(rs.getString("COMPLEMENTO"));
		obj.setBairro(rs.getString("BAIRRO"));
		obj.setCep(rs.getString("CEP"));
		obj.setDdd(rs.getInt("DDD"));
		obj.setTelefone(rs.getString("TELEFONE"));
		obj.setCelular(rs.getString("CELULAR"));
		obj.setEmail(rs.getString("EMAIL"));
		obj.setDataCadastro(new java.util.Date(rs.getTimestamp("DATA_CADASTRO").getTime()));
		obj.setObs(rs.getString("OBS"));
		return obj;
	}

}
