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
import marcenaria.dao.OrdemServicoClienteDao;
import marcenaria.entities.Cliente;
import marcenaria.entities.Funcionario;
import marcenaria.entities.OrdemServicoCliente;

public class OrdemServicoClienteDaoJDBC implements OrdemServicoClienteDao {

	private Connection conn;

	public OrdemServicoClienteDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(OrdemServicoCliente obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO MARCENARIA.ORDEM_SERVICO_CLIENTE(COD_CLIENTE, DESC_SERVICO, DATA_ORDEM, DATA_INICIO, PRAZO_ENTREGA, DATA_ENTREGA, STATUS_SERVICO, VALOR_TOTAL, FUNC_RESPONSAVEL, OBS) "
							+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getCodCliente().getCodCliente());
			st.setString(2, obj.getDescServico().toUpperCase());
			st.setDate(3, new java.sql.Date(obj.getDataOrdem().getTime()));
			st.setDate(4, new java.sql.Date(obj.getDataInicio().getTime()));
			st.setDate(5, new java.sql.Date(obj.getPrazoEntrega().getTime()));
			st.setDate(6, new java.sql.Date(obj.getDataEntrega().getTime()));
			st.setString(7, obj.getStatusServico().toUpperCase());
			st.setDouble(8, obj.getValorTotal());
			st.setInt(9, obj.getFuncResponsavel().getRegistroFunc());
			st.setString(10, obj.getObs());
			if (obj.getObs() != null) {
				st.setString(10, obj.getObs().toUpperCase());
			}

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int numPedidoGerado = rs.getInt(1);
					obj.setNumeroPedido(numPedidoGerado);
				}
			} else {
				throw new DbException("Nenhuma ordem de serviço foi cadastrada no sistema!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public void update(OrdemServicoCliente obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE MARCENARIA.ORDEM_SERVICO_CLIENTE "
					+ "SET COD_CLIENTE = ?, DESC_SERVICO = ?, DATA_ORDEM = ?, DATA_INICIO = ?, PRAZO_ENTREGA = ?, DATA_ENTREGA = ?, STATUS_SERVICO = ?, VALOR_TOTAL = ?, FUNC_RESPONSAVEL = ?, OBS = ? "
					+ "WHERE NUM_PEDIDO = ?");

			st.setInt(1, obj.getCodCliente().getCodCliente());
			st.setString(2, obj.getDescServico().toUpperCase());
			st.setDate(3, new java.sql.Date(obj.getDataOrdem().getTime()));
			st.setDate(4, new java.sql.Date(obj.getDataInicio().getTime()));
			st.setDate(5, new java.sql.Date(obj.getPrazoEntrega().getTime()));
			st.setDate(6, new java.sql.Date(obj.getDataEntrega().getTime()));
			st.setString(7, obj.getStatusServico().toUpperCase());
			st.setDouble(8, obj.getValorTotal());
			st.setInt(9, obj.getFuncResponsavel().getRegistroFunc());
			st.setString(10, obj.getObs());
			if (obj.getObs() != null) {
				st.setString(10, obj.getObs().toUpperCase());
			}
			st.setInt(11, obj.getNumeroPedido());

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
			st = conn.prepareStatement("DELETE FROM MARCENARIA.ORDEM_SERVICO_CLIENTE WHERE NUM_PEDIDO = ?");

			st.setInt(1, numPedido);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}

	}

	@Override
	public OrdemServicoCliente findByNumPedido(Integer numPedido) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.ORDEM_SERVICO_CLIENTE "
					+ "INNER JOIN CLIENTE ON CLIENTE.COD_CLIENTE = ORDEM_SERVICO_CLIENTE.COD_CLIENTE "
					+ "INNER JOIN FUNCIONARIO ON FUNCIONARIO.REGISTRO_FUNC = ORDEM_SERVICO_CLIENTE.FUNC_RESPONSAVEL "
					+ " WHERE NUM_PEDIDO = ? GROUP BY ORDEM_SERVICO_CLIENTE.NUM_PEDIDO ORDER BY NUM_PEDIDO");

			st.setInt(1, numPedido);

			rs = st.executeQuery();

			Map<Integer, Cliente> clienteMap = new HashMap<>();
			Map<Integer, Funcionario> funcionarioMap = new HashMap<>();

			if (rs.next()) {

				Cliente cliente = clienteMap.get(rs.getInt("COD_CLIENTE"));
				if (cliente == null) {
					cliente = criarCliente(rs);
					clienteMap.put(rs.getInt("COD_CLIENTE"), cliente);
				}

				Funcionario funcionario = funcionarioMap.get(rs.getInt("FUNC_RESPONSAVEL"));
				if (funcionario == null) {
					funcionario = criarFuncionario(rs);
					funcionarioMap.put(rs.getInt("REGISTRO_FUNC"), funcionario);
				}

				OrdemServicoCliente obj = criarOrdemServicoCliente(rs, cliente, funcionario);

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
	public List<OrdemServicoCliente> findByCodCliente(Integer codCliente) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.ORDEM_SERVICO_CLIENTE "
					+ "INNER JOIN CLIENTE ON CLIENTE.COD_CLIENTE = ORDEM_SERVICO_CLIENTE.COD_CLIENTE "
					+ "INNER JOIN FUNCIONARIO ON FUNCIONARIO.REGISTRO_FUNC = ORDEM_SERVICO_CLIENTE.FUNC_RESPONSAVEL "
					+ "WHERE ORDEM_SERVICO_CLIENTE.COD_CLIENTE = ? GROUP BY ORDEM_SERVICO_CLIENTE.NUM_PEDIDO ORDER BY NUM_PEDIDO");

			st.setInt(1, codCliente);
			rs = st.executeQuery();

			List<OrdemServicoCliente> ordemServicoList = new ArrayList<>();
			Map<Integer, Cliente> clienteMap = new HashMap<>();
			Map<Integer, Funcionario> funcionarioMap = new HashMap<>();

			while (rs.next()) {

				Cliente cliente = clienteMap.get(rs.getInt("COD_CLIENTE"));
				if (cliente == null) {
					cliente = criarCliente(rs);
					clienteMap.put(rs.getInt("COD_CLIENTE"), cliente);
				}

				Funcionario funcionario = funcionarioMap.get(rs.getInt("FUNC_RESPONSAVEL"));
				if (funcionario == null) {
					funcionario = criarFuncionario(rs);
					funcionarioMap.put(rs.getInt("REGISTRO_FUNC"), funcionario);
				}

				OrdemServicoCliente obj = criarOrdemServicoCliente(rs, cliente, funcionario);

				if (obj.getCodCliente().getCodCliente() == codCliente) {
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
	public List<OrdemServicoCliente> findByStatus(String status) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.ORDEM_SERVICO_CLIENTE "
					+ "INNER JOIN CLIENTE ON CLIENTE.COD_CLIENTE = ORDEM_SERVICO_CLIENTE.COD_CLIENTE "
					+ "INNER JOIN FUNCIONARIO ON FUNCIONARIO.REGISTRO_FUNC = ORDEM_SERVICO_CLIENTE.FUNC_RESPONSAVEL "
					+ "WHERE STATUS_SERVICO = ? GROUP BY ORDEM_SERVICO_CLIENTE.NUM_PEDIDO ORDER BY NUM_PEDIDO");

			st.setString(1, status);
			rs = st.executeQuery();

			List<OrdemServicoCliente> ordemServicoList = new ArrayList<>();
			Map<Integer, Cliente> clienteMap = new HashMap<>();
			Map<Integer, Funcionario> funcionarioMap = new HashMap<>();

			while (rs.next()) {

				Cliente cliente = clienteMap.get(rs.getInt("COD_CLIENTE"));
				if (cliente == null) {
					cliente = criarCliente(rs);
					clienteMap.put(rs.getInt("COD_CLIENTE"), cliente);
				}

				Funcionario funcionario = funcionarioMap.get(rs.getInt("FUNC_RESPONSAVEL"));
				if (funcionario == null) {
					funcionario = criarFuncionario(rs);
					funcionarioMap.put(rs.getInt("REGISTRO_FUNC"), funcionario);
				}

				OrdemServicoCliente obj = criarOrdemServicoCliente(rs, cliente, funcionario);
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
	public List<OrdemServicoCliente> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.ORDEM_SERVICO_CLIENTE "
					+ "INNER JOIN CLIENTE ON CLIENTE.COD_CLIENTE = ORDEM_SERVICO_CLIENTE.COD_CLIENTE "
					+ "INNER JOIN FUNCIONARIO ON FUNCIONARIO.REGISTRO_FUNC = ORDEM_SERVICO_CLIENTE.FUNC_RESPONSAVEL GROUP BY ORDEM_SERVICO_CLIENTE.NUM_PEDIDO ORDER BY NUM_PEDIDO");

			rs = st.executeQuery();

			List<OrdemServicoCliente> ordemServicoList = new ArrayList<>();
			Map<Integer, Cliente> clienteMap = new HashMap<>();
			Map<Integer, Funcionario> funcionarioMap = new HashMap<>();

			while (rs.next()) {

				Cliente cliente = clienteMap.get(rs.getInt("COD_CLIENTE"));
				if (cliente == null) {
					cliente = criarCliente(rs);
					clienteMap.put(rs.getInt("COD_CLIENTE"), cliente);
				}

				Funcionario funcionario = funcionarioMap.get(rs.getInt("FUNC_RESPONSAVEL"));
				if (funcionario == null) {
					funcionario = criarFuncionario(rs);
					funcionarioMap.put(rs.getInt("REGISTRO_FUNC"), funcionario);
				}

				OrdemServicoCliente obj = criarOrdemServicoCliente(rs, cliente, funcionario);

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

	private OrdemServicoCliente criarOrdemServicoCliente(ResultSet rs, Cliente cliente, Funcionario funcionario)
			throws SQLException {
		OrdemServicoCliente obj = new OrdemServicoCliente();
		obj.setNumeroPedido(rs.getInt("NUM_PEDIDO"));
		obj.setCodCliente(cliente);
		obj.setDescServico(rs.getString("DESC_SERVICO"));
		obj.setDataOrdem(rs.getDate("DATA_ORDEM"));
		obj.setDataInicio(rs.getDate("DATA_INICIO"));
		obj.setPrazoEntrega(rs.getDate("PRAZO_ENTREGA"));
		obj.setDataEntrega(rs.getDate("DATA_ENTREGA"));
		obj.setStatusServico(rs.getString("STATUS_SERVICO"));
		obj.setValorTotal(rs.getDouble("VALOR_TOTAL"));
		obj.setFuncResponsavel(funcionario);
		obj.setObs(rs.getString("OBS"));

		return obj;
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
		obj.setCidade(rs.getString("CIDADE"));
		obj.setEstado(rs.getString("ESTADO"));
		obj.setUf(rs.getString("UF"));
		obj.setDdd(rs.getInt("DDD"));
		obj.setTelefone(rs.getString("TELEFONE"));
		obj.setCelular(rs.getString("CELULAR"));
		obj.setEmail(rs.getString("EMAIL"));
		obj.setDataCadastro(new java.util.Date(rs.getTimestamp("DATA_CADASTRO").getTime()));
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
