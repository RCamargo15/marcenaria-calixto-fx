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
import marcenaria.dao.OrdemServicoEmpresaDao;
import marcenaria.entities.Empresa;
import marcenaria.entities.Funcionario;
import marcenaria.entities.OrdemServicoEmpresa;

public class OrdemServicoEmpresaDaoJDBC implements OrdemServicoEmpresaDao {

	private Connection conn;

	public OrdemServicoEmpresaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(OrdemServicoEmpresa obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO MARCENARIA.ORDEM_SERVICO_EMPRESA(NUM_PEDIDO, COD_EMPRESA, NOME_RESPONSAVEL, DESC_SERVICO, DATA_ORDEM, DATA_INICIO, PRAZO_ENTREGA, DATA_ENTREGA, STATUS_SERVICO, VALOR_TOTAL, REGISTRO_FUNC, OBS) "
							+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getNumeroPedido());
			st.setInt(2, obj.getCodEmpresa().getCodEmpresa());
			st.setString(3, obj.getNomeResponsavel().toUpperCase());
			st.setString(4, obj.getDescServico().toUpperCase());
			st.setDate(5, new java.sql.Date(obj.getDataOrdem().getTime()));
			
			if (obj.getDataInicio() != null) {
				st.setDate(6, new java.sql.Date(obj.getDataInicio().getTime()));
			} else {
				st.setDate(6, null);
			}

			if (obj.getPrazoEntrega() != null) {
				st.setDate(7, new java.sql.Date(obj.getPrazoEntrega().getTime()));
			} else {
				st.setDate(7, null);
			}
			if (obj.getDataEntrega() != null) {
				st.setDate(8, new java.sql.Date(obj.getDataEntrega().getTime()));
			} else {
				st.setDate(8, null);
			}
			st.setString(9, obj.getStatusServico().toUpperCase());
			st.setDouble(10, obj.getValorTotal());
			st.setInt(11, obj.getRegistroFunc().getRegistroFunc());
			st.setString(12, obj.getObs());
			if (obj.getObs() != null) {
				st.setString(12, obj.getObs().toUpperCase());
			}

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int numPedidoGerado = rs.getInt(1);
					obj.setId(numPedidoGerado);
				}
			} else {
				throw new DbException("Nenhuma ordem de servi�o foi cadastrada no sistema!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public void update(OrdemServicoEmpresa obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE MARCENARIA.ORDEM_SERVICO_EMPRESA "
					+ "SET NUM_PEDIDO = ?, COD_EMPRESA = ?, NOME_RESPONSAVEL = ?, DESC_SERVICO = ?, DATA_ORDEM = ?, DATA_INICIO = ?, PRAZO_ENTREGA = ?, DATA_ENTREGA = ?, STATUS_SERVICO = ?, VALOR_TOTAL = ?, REGISTRO_FUNC = ?, OBS = ? "
					+ "WHERE ORDEM_SERVICO_EMPRESA.ID = ?");

			st.setInt(1, obj.getNumeroPedido());
			st.setInt(2, obj.getCodEmpresa().getCodEmpresa());
			st.setString(3, obj.getNomeResponsavel().toUpperCase());
			st.setString(4, obj.getDescServico().toUpperCase());
			st.setDate(5, new java.sql.Date(obj.getDataOrdem().getTime()));
			
			if (obj.getDataInicio() != null) {
				st.setDate(6, new java.sql.Date(obj.getDataInicio().getTime()));
			} else {
				st.setDate(6, null);
			}

			if (obj.getPrazoEntrega() != null) {
				st.setDate(7, new java.sql.Date(obj.getPrazoEntrega().getTime()));
			} else {
				st.setDate(7, null);
			}
			if (obj.getDataEntrega() != null) {
				st.setDate(8, new java.sql.Date(obj.getDataEntrega().getTime()));
			} else {
				st.setDate(8, null);
			}
			st.setString(9, obj.getStatusServico().toUpperCase());
			st.setDouble(10, obj.getValorTotal());
			st.setInt(11, obj.getRegistroFunc().getRegistroFunc());
			st.setString(12, obj.getObs());
			if (obj.getObs() != null) {
				st.setString(12, obj.getObs().toUpperCase());
			}
			st.setInt(13, obj.getId());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}

	}

	@Override
	public void deleteByNumPedido(Integer numPedido) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM MARCENARIA.ORDEM_SERVICO_EMPRESA WHERE NUM_PEDIDO = ?");

			st.setInt(1, numPedido);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}

	}

	@Override
	public OrdemServicoEmpresa findByNumPedido(Integer numPedido) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.ORDEM_SERVICO_EMPRESA "
					+ "INNER JOIN EMPRESA ON EMPRESA.COD_EMPRESA = ORDEM_SERVICO_EMPRESA.COD_EMPRESA "
					+ "INNER JOIN FUNCIONARIO ON FUNCIONARIO.REGISTRO_FUNC = ORDEM_SERVICO_EMPRESA.REGISTRO_FUNC "
					+ " WHERE NUM_PEDIDO = ? GROUP BY ORDEM_SERVICO_EMPRESA.NUM_PEDIDO ORDER BY NUM_PEDIDO");

			st.setInt(1, numPedido);

			rs = st.executeQuery();

			Map<Integer, Empresa> empresaMap = new HashMap<>();
			Map<Integer, Funcionario> funcionarioMap = new HashMap<>();

			if (rs.next()) {

				Empresa empresa = empresaMap.get(rs.getInt("COD_EMPRESA"));
				if (empresa == null) {
					empresa = criarEmpresa(rs);
					empresaMap.put(rs.getInt("COD_EMPRESA"), empresa);
				}

				Funcionario funcionario = funcionarioMap.get(rs.getInt("REGISTRO_FUNC"));
				if (funcionario == null) {
					funcionario = criarFuncionario(rs);
					funcionarioMap.put(rs.getInt("REGISTRO_FUNC"), funcionario);
				}

				OrdemServicoEmpresa obj = criarOrdemServicoEmpresa(rs, empresa, funcionario);

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
	public List<OrdemServicoEmpresa> findByCodEmpresa(Integer codEmpresa) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.ORDEM_SERVICO_EMPRESA "
					+ "INNER JOIN EMPRESA ON EMPRESA.COD_EMPRESA = ORDEM_SERVICO_EMPRESA.COD_EMPRESA "
					+ "INNER JOIN FUNCIONARIO ON FUNCIONARIO.REGISTRO_FUNC = ORDEM_SERVICO_EMPRESA.REGISTRO_FUNC "
					+ "WHERE ORDEM_SERVICO_EMPRESA.COD_EMPRESA = ? GROUP BY ORDEM_SERVICO_EMPRESA.NUM_PEDIDO ORDER BY NUM_PEDIDO");

			st.setInt(1, codEmpresa);
			rs = st.executeQuery();

			List<OrdemServicoEmpresa> ordemServicoList = new ArrayList<>();
			Map<Integer, Empresa> empresaMap = new HashMap<>();
			Map<Integer, Funcionario> funcionarioMap = new HashMap<>();

			while (rs.next()) {

				Empresa empresa = empresaMap.get(rs.getInt("COD_EMPRESA"));
				if (empresa == null) {
					empresa = criarEmpresa(rs);
					empresaMap.put(rs.getInt("COD_EMPRESA"), empresa);
				}

				Funcionario funcionario = funcionarioMap.get(rs.getInt("REGISTRO_FUNC"));
				if (funcionario == null) {
					funcionario = criarFuncionario(rs);
					funcionarioMap.put(rs.getInt("REGISTRO_FUNC"), funcionario);
				}

				OrdemServicoEmpresa obj = criarOrdemServicoEmpresa(rs, empresa, funcionario);

				if (obj.getCodEmpresa().getCodEmpresa() == codEmpresa) {
					ordemServicoList.add(obj);
				}
			}
			return ordemServicoList;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
			Db.closeResultSet(rs);
		}
	}

	@Override
	public List<OrdemServicoEmpresa> findByStatus(String status) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.ORDEM_SERVICO_EMPRESA "
					+ "INNER JOIN EMPRESA ON EMPRESA.COD_EMPRESA = ORDEM_SERVICO_EMPRESA.COD_EMPRESA "
					+ "INNER JOIN FUNCIONARIO ON FUNCIONARIO.REGISTRO_FUNC = ORDEM_SERVICO_CLIENTE.REGISTRO_FUNC "
					+ "WHERE STATUS_SERVICO = ? GROUP BY ORDEM_SERVICO_EMPRESA.NUM_PEDIDO ORDER BY NUM_PEDIDO");

			st.setString(1, status);
			rs = st.executeQuery();

			List<OrdemServicoEmpresa> ordemServicoList = new ArrayList<>();
			Map<Integer, Empresa> empresaMap = new HashMap<>();
			Map<Integer, Funcionario> funcionarioMap = new HashMap<>();

			while (rs.next()) {

				Empresa empresa = empresaMap.get(rs.getInt("COD_EMPRESA"));
				if (empresa == null) {
					empresa = criarEmpresa(rs);
					empresaMap.put(rs.getInt("COD_EMPRESA"), empresa);
				}

				Funcionario funcionario = funcionarioMap.get(rs.getInt("REGISTRO_FUNC"));
				if (funcionario == null) {
					funcionario = criarFuncionario(rs);
					funcionarioMap.put(rs.getInt("REGISTRO_FUNC"), funcionario);
				}

				OrdemServicoEmpresa obj = criarOrdemServicoEmpresa(rs, empresa, funcionario);
				ordemServicoList.add(obj);
			}

			return ordemServicoList;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
			Db.closeResultSet(rs);
		}
	}

	@Override
	public List<OrdemServicoEmpresa> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.ORDEM_SERVICO_EMPRESA "
					+ "INNER JOIN EMPRESA ON EMPRESA.COD_EMPRESA = ORDEM_SERVICO_EMPRESA.COD_EMPRESA "
					+ "INNER JOIN FUNCIONARIO ON FUNCIONARIO.REGISTRO_FUNC = ORDEM_SERVICO_EMPRESA.REGISTRO_FUNC");

			rs = st.executeQuery();

			List<OrdemServicoEmpresa> ordemServicoList = new ArrayList<>();
			Map<Integer, Empresa> empresaMap = new HashMap<>();
			Map<Integer, Funcionario> funcionarioMap = new HashMap<>();

			while (rs.next()) {

				Empresa empresa = empresaMap.get(rs.getInt("COD_EMPRESA"));
				if (empresa == null) {
					empresa = criarEmpresa(rs);
					empresaMap.put(rs.getInt("COD_EMPRESA"), empresa);
				}

				Funcionario funcionario = funcionarioMap.get(rs.getInt("REGISTRO_FUNC"));
				if (funcionario == null) {
					funcionario = criarFuncionario(rs);
					funcionarioMap.put(rs.getInt("REGISTRO_FUNC"), funcionario);
				}

				OrdemServicoEmpresa obj = criarOrdemServicoEmpresa(rs, empresa, funcionario);

				ordemServicoList.add(obj);
			}
			return ordemServicoList;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
			Db.closeResultSet(rs);
		}
	}

	private OrdemServicoEmpresa criarOrdemServicoEmpresa(ResultSet rs, Empresa empresa, Funcionario funcionario)
			throws SQLException {
		OrdemServicoEmpresa obj = new OrdemServicoEmpresa();
		obj.setId(rs.getInt("ID"));
		obj.setNumeroPedido(rs.getInt("NUM_PEDIDO"));
		obj.setCodEmpresa(empresa);
		obj.setNomeResponsavel(rs.getString("NOME_RESPONSAVEL"));
		obj.setDescServico(rs.getString("DESC_SERVICO"));
		obj.setDataOrdem(new java.util.Date(rs.getTimestamp("DATA_ORDEM").getTime()));
		if(rs.getTimestamp("DATA_INICIO") != null) {
			obj.setDataInicio(new java.util.Date(rs.getTimestamp("DATA_INICIO").getTime()));
		}
		if(rs.getTimestamp("PRAZO_ENTREGA") != null ) {
			obj.setPrazoEntrega(new java.util.Date(rs.getTimestamp("PRAZO_ENTREGA").getTime()));
		}
		if(rs.getTimestamp("DATA_ENTREGA") != null) {
			obj.setDataEntrega(new java.util.Date(rs.getTimestamp("DATA_ENTREGA").getTime()));
		}
		obj.setStatusServico(rs.getString("STATUS_SERVICO"));
		obj.setValorTotal(rs.getDouble("VALOR_TOTAL"));
		obj.setRegistroFunc(funcionario);
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
