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
import marcenaria.dao.DaoFactory;
import marcenaria.dao.EstoqueDao;
import marcenaria.dao.SaidaProdutoDao;
import marcenaria.entities.Estoque;
import marcenaria.entities.Funcionario;
import marcenaria.entities.Produto;
import marcenaria.entities.SaidaProduto;

public class SaidaProdutoDaoJDBC implements SaidaProdutoDao {

	private Connection conn;

	public SaidaProdutoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(SaidaProduto obj) {
		int codProdutoEstoque = 0;
		int qtdRemovida = 0;
		int qtdAtual = 0;
		PreparedStatement st = null;
		PreparedStatement atualizaEstoque = null;
		EstoqueDao estoqueDao = DaoFactory.createEstoqueDao();
		try {
			st = conn.prepareStatement(
					"INSERT INTO MARCENARIA.SAIDA_PRODUTO(ID_ESTOQUE, COD_PRODUTO, DATA_SAIDA, QUANTIDADE, RESP_SAIDA) "
							+ "VALUES(?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setInt(1, obj.getIdEstoque().getId());
			st.setInt(2, obj.getCodProduto().getCodProduto().getCodProduto());
			st.setDate(3, new java.sql.Date(obj.getDataSaida().getTime()));
			st.setInt(4, obj.getQuantidade());
			st.setInt(5, obj.getRespSaida().getRegistroFunc());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int idSaidaGerado = rs.getInt(1);
					obj.setCodSaida(idSaidaGerado);
				} else {
					throw new DbException("Nenhuma saída foi registrada no sistema");
				}
			}

			// ATUALIZAÇÃO AUTOMÁTICA DO ESTOQUE ATRAVÉS DO PRODUTO E QUANTIDADE RECEBIDA
			qtdRemovida = obj.getQuantidade();
			codProdutoEstoque = obj.getCodProduto().getCodProduto().getCodProduto();

			Estoque estoque = estoqueDao.findByCodProduto(codProdutoEstoque);
			qtdAtual = estoque.getEstoqueAtual();

			atualizaEstoque = conn
					.prepareStatement("UPDATE MARCENARIA.ESTOQUE SET ESTOQUE.ESTOQUE_ATUAL = ? WHERE COD_PRODUTO = ?");

			atualizaEstoque.setInt(1, qtdAtual - qtdRemovida);
			atualizaEstoque.setInt(2, codProdutoEstoque);

			atualizaEstoque.executeUpdate();
			System.out.println("Estoque atualizado automaticamente!");

			Estoque estoquePosAtt = estoqueDao.findByCodProduto(codProdutoEstoque);
			if (estoquePosAtt.getEstoqueAtual() <= estoquePosAtt.getEstoqueMinimo()) {
				System.out.println("ATENÇÃO! O ESTOQUE ATUAL ATINGIU O ESTOQUE MÍNIMO OU ESTÁ ABAIXO!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}

	}

	@Override
	public void update(SaidaProduto obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE MARCENARIA.SAIDA_PRODUTO "
					+ "SET ID_ESTOQUE = ?, COD_PRODUTO = ?, DATA_SAIDA = ?, QUANTIDADE = ?, RESP_SAIDA = ? WHERE SAIDA_PRODUTO.COD_SAIDA = ?");

			st.setInt(1, obj.getIdEstoque().getId());
			st.setInt(2, obj.getCodProduto().getCodProduto().getCodProduto());
			st.setDate(3, new java.sql.Date(obj.getDataSaida().getTime()));
			st.setInt(4, obj.getQuantidade());
			st.setInt(5, obj.getRespSaida().getRegistroFunc());
			st.setInt(6, obj.getCodSaida());

			st.executeUpdate();
			System.out.println("Atualizado com sucesso");
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public void deleteByCodSaidaProduto(Integer codSaidaProduto) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM MARCENARIA.SAIDA_PRODUTO WHERE COD_SAIDA = ?");
			st.setInt(1, codSaidaProduto);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public SaidaProduto findByCodSaidaProduto(Integer codSaidaProduto) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.SAIDA_PRODUTO "
					+ "INNER JOIN ESTOQUE ON ESTOQUE.ID = SAIDA_PRODUTO.ID_ESTOQUE "
					+ "INNER JOIN PRODUTO ON PRODUTO.COD_PRODUTO = SAIDA_PRODUTO.COD_PRODUTO "
					+ "INNER JOIN FUNCIONARIO ON FUNCIONARIO.REGISTRO_FUNC = SAIDA_PRODUTO.RESP_SAIDA "
					+ "WHERE COD_SAIDA = ? GROUP BY COD_SAIDA");

			st.setInt(1, codSaidaProduto);

			rs = st.executeQuery();

			Map<Integer, Estoque> estoqueMap = new HashMap<>();
			Map<Integer, Produto> produtoMap = new HashMap<>();
			Map<Integer, Funcionario> funcionarioMap = new HashMap<>();

			if (rs.next()) {

				Produto produto = produtoMap.get(rs.getInt("COD_PRODUTO"));
				if (produto == null) {
					produto = criarProduto(rs);
					produtoMap.put(rs.getInt("COD_PRODUTO"), produto);
				}

				Estoque estoque = estoqueMap.get(rs.getInt("ID"));
				if (estoque == null) {
					estoque = criarEstoque(rs, produto);
					estoqueMap.put(rs.getInt("ID"), estoque);
				}

				Funcionario funcionario = funcionarioMap.get(rs.getInt("REGISTRO_FUNC"));
				if (funcionario == null) {
					funcionario = criarFuncionario(rs);
					funcionarioMap.put(rs.getInt("REGISTRO_FUNC"), funcionario);
				}

				SaidaProduto obj = criarSaida(rs, estoque, funcionario);
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
	public List<SaidaProduto> findByCodProduto(Integer codProduto) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.SAIDA_PRODUTO "
					+ "INNER JOIN ESTOQUE ON ESTOQUE.ID = SAIDA_PRODUTO.ID_ESTOQUE "
					+ "INNER JOIN PRODUTO ON PRODUTO.COD_PRODUTO = SAIDA_PRODUTO.COD_PRODUTO "
					+ "INNER JOIN FUNCIONARIO ON FUNCIONARIO.REGISTRO_FUNC = SAIDA_PRODUTO.RESP_SAIDA "
					+ "WHERE SAIDA_PRODUTO.COD_PRODUTO = ? GROUP BY COD_SAIDA");

			st.setInt(1, codProduto);

			rs = st.executeQuery();

			List<SaidaProduto> saidaProduto = new ArrayList<>();
			Map<Integer, Estoque> estoqueMap = new HashMap<>();
			Map<Integer, Produto> produtoMap = new HashMap<>();
			Map<Integer, Funcionario> funcionarioMap = new HashMap<>();

			while (rs.next()) {

				Produto produto = produtoMap.get(rs.getInt("COD_PRODUTO"));
				if (produto == null) {
					produto = criarProduto(rs);
					produtoMap.put(rs.getInt("COD_PRODUTO"), produto);
				}

				Estoque estoque = estoqueMap.get(rs.getInt("ID"));
				if (estoque == null) {
					estoque = criarEstoque(rs, produto);
					estoqueMap.put(rs.getInt("ID"), estoque);
				}

				Funcionario funcionario = funcionarioMap.get(rs.getInt("REGISTRO_FUNC"));
				if (funcionario == null) {
					funcionario = criarFuncionario(rs);
					funcionarioMap.put(rs.getInt("REGISTRO_FUNC"), funcionario);
				}

				SaidaProduto obj = criarSaida(rs, estoque, funcionario);
				saidaProduto.add(obj);
			}
			return saidaProduto;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
			Db.closeResultSet(rs);
		}

	}

	@Override
	public List<SaidaProduto> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM MARCENARIA.SAIDA_PRODUTO "
					+ "INNER JOIN ESTOQUE ON ESTOQUE.ID = SAIDA_PRODUTO.ID_ESTOQUE "
					+ "INNER JOIN PRODUTO ON PRODUTO.COD_PRODUTO = SAIDA_PRODUTO.COD_PRODUTO "
					+ "INNER JOIN FUNCIONARIO ON FUNCIONARIO.REGISTRO_FUNC = SAIDA_PRODUTO.RESP_SAIDA "
					+ "GROUP BY COD_SAIDA");

			rs = st.executeQuery();

			List<SaidaProduto> saidaProduto = new ArrayList<>();
			Map<Integer, Estoque> estoqueMap = new HashMap<>();
			Map<Integer, Produto> produtoMap = new HashMap<>();
			Map<Integer, Funcionario> funcionarioMap = new HashMap<>();

			while (rs.next()) {

				Produto produto = produtoMap.get(rs.getInt("COD_PRODUTO"));
				if (produto == null) {
					produto = criarProduto(rs);
					produtoMap.put(rs.getInt("COD_PRODUTO"), produto);
				}

				Estoque estoque = estoqueMap.get(rs.getInt("ID"));
				if (estoque == null) {
					estoque = criarEstoque(rs, produto);
					estoqueMap.put(rs.getInt("ID"), estoque);
				}

				Funcionario funcionario = funcionarioMap.get(rs.getInt("REGISTRO_FUNC"));
				if (funcionario == null) {
					funcionario = criarFuncionario(rs);
					funcionarioMap.put(rs.getInt("REGISTRO_FUNC"), funcionario);
				}

				SaidaProduto obj = criarSaida(rs, estoque, funcionario);
				saidaProduto.add(obj);
			}
			return saidaProduto;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
			Db.closeResultSet(rs);
		}
	}

	private SaidaProduto criarSaida(ResultSet rs, Estoque stock, Funcionario func) throws SQLException {
		SaidaProduto obj = new SaidaProduto();

		obj.setCodSaida(rs.getInt("COD_SAIDA"));
		obj.setIdEstoque(stock);
		obj.setCodProduto(stock);
		obj.setDataSaida(rs.getDate("DATA_SAIDA"));
		obj.setQuantidade(rs.getInt("QUANTIDADE"));
		obj.setRespSaida(func);

		return obj;
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
