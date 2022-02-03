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
import marcenaria.dao.FuncionarioDao;
import marcenaria.entities.Funcionario;

public class FuncionarioDaoJDBC implements FuncionarioDao {

	private Connection conn;

	public FuncionarioDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Funcionario obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO MARCENARIA.FUNCIONARIO"
					+ "(NOME, RG, CPF, CTPS, RUA, NUMERO, COMPLEMENTO, BAIRRO, CEP,  CIDADE, ESTADO, UF, DDD, TELEFONE, CELULAR, DATA_NASC, "
					+ " DATA_ADMISSAO, TIPO_SANG, FUNCAO, SETOR, SALARIO, OBS)" + " VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome().toUpperCase());
			st.setString(2, obj.getRg());
			st.setString(3, obj.getCpf());
			st.setString(4, obj.getCtps());
			st.setString(5, obj.getRua().toUpperCase());
			st.setInt(6, obj.getNumero());
			st.setString(7, obj.getComplemento());
			if (obj.getComplemento() != null) {
				st.setString(7, obj.getComplemento().toUpperCase());
			}
			st.setString(8, obj.getBairro().toUpperCase());
			st.setString(9, obj.getCep());
			st.setString(10, obj.getCidade().toUpperCase());
			st.setString(11, obj.getEstado().toUpperCase());
			st.setString(12, obj.getUf().toUpperCase());
			st.setInt(13, obj.getDdd());
			st.setString(14, obj.getTelefone());
			st.setString(15, obj.getCelular());
			st.setDate(16, new java.sql.Date(obj.getDataNasc().getTime()));
			st.setDate(17, new java.sql.Date(obj.getDataAdmissao().getTime()));
			st.setString(18, obj.getTipoSang());
			st.setString(19, obj.getFuncao().toUpperCase());
			st.setString(20, obj.getSetor().toUpperCase());
			st.setDouble(21, obj.getSalario());
			st.setString(22, obj.getObs());
			if (obj.getObs() != null) {
				st.setString(22, obj.getObs().toUpperCase());
			}

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codFuncionarioGerado = rs.getInt(1);
					obj.setRegistroFunc(codFuncionarioGerado);
				}
			} else {
				throw new DbException("Erro. Nenhum funcionário foi cadastrado!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public void update(Funcionario obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE MARCENARIA.FUNCIONARIO "
					+ "SET NOME = ?, RG = ?, CPF = ?, CTPS = ?, RUA = ?, NUMERO = ?, COMPLEMENTO = ?, BAIRRO = ?, CEP = ?, CIDADE = ?, ESTADO = ?, UF = ?, DDD = ?, TELEFONE = ?, "
					+ "CELULAR = ?, DATA_NASC = ?, DATA_ADMISSAO = ?, TIPO_SANG = ?, FUNCAO = ?, SETOR = ?, SALARIO = ?, OBS = ? "
					+ "WHERE REGISTRO_FUNC = ?");

			st.setString(1, obj.getNome().toUpperCase());
			st.setString(2, obj.getRg());
			st.setString(3, obj.getCpf());
			st.setString(4, obj.getCtps());
			st.setString(5, obj.getRua().toUpperCase());
			st.setInt(6, obj.getNumero());
			st.setString(7, obj.getComplemento());
			if (obj.getComplemento() != null) {
				st.setString(7, obj.getComplemento().toUpperCase());
			}
			st.setString(8, obj.getBairro().toUpperCase());
			st.setString(9, obj.getCep());
			st.setString(10, obj.getCidade().toUpperCase());
			st.setString(11, obj.getEstado().toUpperCase());
			st.setString(12, obj.getUf().toUpperCase());
			st.setInt(13, obj.getDdd());
			st.setString(14, obj.getTelefone());
			st.setString(15, obj.getCelular());
			st.setDate(16, new java.sql.Date(obj.getDataNasc().getTime()));
			st.setDate(17, new java.sql.Date(obj.getDataAdmissao().getTime()));
			st.setString(18, obj.getTipoSang());
			st.setString(19, obj.getFuncao().toUpperCase());
			st.setString(20, obj.getSetor().toUpperCase());
			st.setDouble(21, obj.getSalario());
			st.setString(22, obj.getObs());
			if (obj.getObs() != null) {
				st.setString(22, obj.getObs().toUpperCase());
			}
			st.setInt(23, obj.getRegistroFunc());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public void deleteByCodFuncionario(Integer codFuncionario) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM MARCENARIA.FUNCIONARIO WHERE REGISTRO_FUNC = ?");
			st.setInt(1, codFuncionario);
			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public Funcionario findByCodFuncionario(Integer codFuncionario) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.FUNCIONARIO WHERE REGISTRO_FUNC = ? ");

			st.setInt(1, codFuncionario);
			rs = st.executeQuery();
			if (rs.next()) {
				Funcionario obj = criarFuncionario(rs);
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
	public List<Funcionario> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.FUNCIONARIO ORDER BY REGISTRO_FUNC");
			rs = st.executeQuery();

			List<Funcionario> lista = new ArrayList<>();

			while (rs.next()) {
				Funcionario obj = criarFuncionario(rs);
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

	private Funcionario criarFuncionario(ResultSet rs) throws SQLException {
		Funcionario obj = new Funcionario();
		obj.setRegistroFunc(rs.getInt("REGISTRO_FUNC"));
		obj.setNome(rs.getString("NOME"));
		obj.setRg(rs.getString("RG"));
		obj.setCpf(rs.getString("CPF"));
		obj.setCtps(rs.getString("CTPS"));
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
		obj.setCelular(rs.getString("CELULAR"));
		obj.setDataNasc(new java.util.Date(rs.getTimestamp("DATA_NASC").getTime()));
		obj.setDataAdmissao(new java.util.Date(rs.getTimestamp("DATA_ADMISSAO").getTime()));
		obj.setTipoSang(rs.getString("TIPO_SANG"));
		obj.setFuncao(rs.getString("FUNCAO"));
		obj.setSetor(rs.getString("SETOR"));
		obj.setSalario(rs.getDouble("SALARIO"));
		obj.setObs(rs.getString("OBS"));
		return obj;
	}

}
