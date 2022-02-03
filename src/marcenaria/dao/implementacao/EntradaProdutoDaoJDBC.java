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
import marcenaria.dao.EntradaProdutoDao;
import marcenaria.dao.EstoqueDao;
import marcenaria.entities.EntradaProduto;
import marcenaria.entities.Estoque;
import marcenaria.entities.Fornecedor;
import marcenaria.entities.Funcionario;
import marcenaria.entities.NotasCompras;
import marcenaria.entities.Produto;

public class EntradaProdutoDaoJDBC implements EntradaProdutoDao {

	private Connection conn;

	public EntradaProdutoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(EntradaProduto obj) {
		int codProdutoEstoque = 0;
		int qtdRecebida = 0;
		int qtdAtual = 0;
		PreparedStatement st = null;
		PreparedStatement atualizaEstoque = null;
		EstoqueDao estoqueDao = DaoFactory.createEstoqueDao();
		try {
			st = conn.prepareStatement(
					"INSERT INTO MARCENARIA.ENTRADA_PRODUTO(NUMERO_NF, COD_PRODUTO, DATA_ENTRADA, QUANTIDADE, VALOR_UNIT, VALOR_TOTAL, VALOR_TOTAL_NOTA, "
							+ "RESP_RECEB) " + "VALUES(?, ? ,?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNumeroNF().getNumeroNF());
			st.setInt(2, obj.getCodProduto().getCodProduto());
			st.setDate(3, new java.sql.Date(obj.getDataEntrada().getTime()));
			st.setInt(4, obj.getQuantidade().getQuantidade());
			st.setDouble(5, obj.getValorUnit().getValorUnit());
			st.setDouble(6, obj.getValorTotal().getValorTotal());
			st.setDouble(7, obj.getValorTotalNota().getValorTotalNota());
			st.setInt(8, obj.getRespRecebimento().getRegistroFunc());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int codEntradaProdutoGerado = rs.getInt(1);
					obj.setCodEntrada(codEntradaProdutoGerado);
				} else {
					throw new DbException("Nenhuma entrada de nota foi cadastrada no sistema!");
				}
			}

			// ATUALIZAÇÃO AUTOMÁTICA DO ESTOQUE ATRAVÉS DO PRODUTO E QUANTIDADE RECEBIDA
			qtdRecebida = obj.getQuantidade().getQuantidade();
			codProdutoEstoque = obj.getCodProduto().getCodProduto();

			Estoque estoque = estoqueDao.findByCodProduto(codProdutoEstoque);
			qtdAtual = estoque.getEstoqueAtual();

			atualizaEstoque = conn.prepareStatement(
					"UPDATE MARCENARIA.ESTOQUE SET ESTOQUE.ESTOQUE_ATUAL = ? WHERE ESTOQUE.COD_PRODUTO = ?");

			atualizaEstoque.setInt(1, qtdAtual + qtdRecebida);
			atualizaEstoque.setInt(2, codProdutoEstoque);

			atualizaEstoque.executeUpdate();
			System.out.println("Estoque atualizado automaticamente!");

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public void update(EntradaProduto obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE MARCENARIA.ENTRADA_PRODUTO"
					+ " SET NUMERO_NF=?, COD_PRODUTO = ?, DATA_ENTRADA = ?, QUANTIDADE = ?, VALOR_UNIT = ?, VALOR_TOTAL = ?, VALOR_TOTAL_NOTA = ?, RESP_RECEB = ? "
					+ "WHERE COD_ENTRADA = ?");

			st.setString(1, obj.getNumeroNF().getNumeroNF());
			st.setInt(2, obj.getCodProduto().getCodProduto());
			st.setDate(3, new java.sql.Date(obj.getDataEntrada().getTime()));
			st.setInt(4, obj.getQuantidade().getQuantidade());
			st.setDouble(5, obj.getValorUnit().getValorUnit());
			st.setDouble(6, obj.getValorTotal().getValorTotal());
			st.setDouble(7, obj.getValorTotalNota().getValorTotalNota());
			st.setInt(8, obj.getRespRecebimento().getRegistroFunc());
			st.setInt(9, obj.getCodEntrada());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public void deleteByCodEntradaProduto(Integer codEntradaProduto) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM MARCENARIA.ENTRADA_PRODUTO WHERE COD_ENTRADA = ?");
			st.setInt(1, codEntradaProduto);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
		}
	}

	@Override
	public EntradaProduto findByCodEntradaProduto(Integer codEntradaProduto) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT * FROM ENTRADA_PRODUTO INNER JOIN PRODUTO ON PRODUTO.COD_PRODUTO = ENTRADA_PRODUTO.COD_PRODUTO "
							+ "INNER JOIN nota_compra_material ON nota_compra_material.NUMERO_NF = entrada_produto.NUMERO_NF "
							+ "INNER JOIN FORNECEDOR ON FORNECEDOR.COD_FORNECEDOR = NOTA_COMPRA_MATERIAL.COD_FORNECEDOR "
							+ "INNER JOIN FUNCIONARIO ON funcionario.REGISTRO_FUNC = entrada_produto.RESP_RECEB WHERE COD_ENTRADA = ?  GROUP BY entrada_produto.COD_ENTRADA");

			st.setInt(1, codEntradaProduto);

			rs = st.executeQuery();

			Map<Integer, Produto> produtoMap = new HashMap<>();
			Map<Integer, NotasCompras> notasComprasMap = new HashMap<>();
			Map<Integer, Funcionario> funcionarioMap = new HashMap<>();
			Map<Integer, Fornecedor> fornecedorMap = new HashMap<>();

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

				NotasCompras notaCompra = notasComprasMap.get(rs.getInt("COD_NOTA"));
				if (notaCompra == null) {
					notaCompra = criarNotasCompras(rs, fornecedor, produto);
					notasComprasMap.put(rs.getInt("COD_NOTA"), notaCompra);
				}

				Funcionario funcionario = funcionarioMap.get(rs.getInt(("REGISTRO_FUNC")));
				if (funcionario == null) {
					funcionario = criarFuncionario(rs);
					funcionarioMap.put(rs.getInt("REGISTRO_FUNC"), funcionario);
				}

				EntradaProduto obj = criarEntradaProduto(rs, produto, notaCompra, funcionario);
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
	public List<EntradaProduto> findByNumeroNF(String numeroNF) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT * FROM ENTRADA_PRODUTO INNER JOIN PRODUTO ON PRODUTO.COD_PRODUTO = ENTRADA_PRODUTO.COD_PRODUTO "
							+ "INNER JOIN nota_compra_material ON nota_compra_material.NUMERO_NF = entrada_produto.NUMERO_NF "
							+ "INNER JOIN FORNECEDOR ON FORNECEDOR.COD_FORNECEDOR = NOTA_COMPRA_MATERIAL.COD_FORNECEDOR "
							+ "INNER JOIN FUNCIONARIO ON funcionario.REGISTRO_FUNC = entrada_produto.RESP_RECEB WHERE ENTRADA_PRODUTO.NUMERO_NF = ?  GROUP BY entrada_produto.COD_ENTRADA");

			st.setString(1, numeroNF);

			rs = st.executeQuery();

			List<EntradaProduto> listaEntrada = new ArrayList<>();
			Map<Integer, Produto> produtoMap = new HashMap<>();
			Map<Integer, NotasCompras> notasComprasMap = new HashMap<>();
			Map<Integer, Funcionario> funcionarioMap = new HashMap<>();
			Map<Integer, Fornecedor> fornecedorMap = new HashMap<>();

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

				NotasCompras notaCompra = notasComprasMap.get(rs.getInt("COD_NOTA"));
				if (notaCompra == null) {
					notaCompra = criarNotasCompras(rs, fornecedor, produto);
					notasComprasMap.put(rs.getInt("COD_NOTA"), notaCompra);
				}

				Funcionario funcionario = funcionarioMap.get(rs.getInt(("REGISTRO_FUNC")));
				if (funcionario == null) {
					funcionario = criarFuncionario(rs);
					funcionarioMap.put(rs.getInt("REGISTRO_FUNC"), funcionario);
				}

				EntradaProduto obj = criarEntradaProduto(rs, produto, notaCompra, funcionario);
				listaEntrada.add(obj);
			}
			return listaEntrada;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
			Db.closeResultSet(rs);
		}
	}

	@Override
	public List<EntradaProduto> findByCodProd(Integer codProduto) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT * FROM ENTRADA_PRODUTO INNER JOIN PRODUTO ON PRODUTO.COD_PRODUTO = ENTRADA_PRODUTO.COD_PRODUTO "
							+ "INNER JOIN nota_compra_material ON nota_compra_material.NUMERO_NF = entrada_produto.NUMERO_NF "
							+ "INNER JOIN FORNECEDOR ON FORNECEDOR.COD_FORNECEDOR = NOTA_COMPRA_MATERIAL.COD_FORNECEDOR "
							+ "INNER JOIN FUNCIONARIO ON funcionario.REGISTRO_FUNC = entrada_produto.RESP_RECEB WHERE ENTRADA_PRODUTO.COD_PRODUTO = ?  GROUP BY entrada_produto.COD_ENTRADA");

			st.setInt(1, codProduto);
			rs = st.executeQuery();

			List<EntradaProduto> listaEntrada = new ArrayList<>();
			Map<Integer, Produto> produtoMap = new HashMap<>();
			Map<Integer, NotasCompras> notasComprasMap = new HashMap<>();
			Map<Integer, Funcionario> funcionarioMap = new HashMap<>();
			Map<Integer, Fornecedor> fornecedorMap = new HashMap<>();

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

				NotasCompras notaCompra = notasComprasMap.get(rs.getInt("COD_NOTA"));
				if (notaCompra == null) {
					notaCompra = criarNotasCompras(rs, fornecedor, produto);
					notasComprasMap.put(rs.getInt("COD_NOTA"), notaCompra);
				}

				Funcionario funcionario = funcionarioMap.get(rs.getInt(("REGISTRO_FUNC")));
				if (funcionario == null) {
					funcionario = criarFuncionario(rs);
					funcionarioMap.put(rs.getInt("REGISTRO_FUNC"), funcionario);
				}

				EntradaProduto obj = criarEntradaProduto(rs, produto, notaCompra, funcionario);
				listaEntrada.add(obj);
			}
			return listaEntrada;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
			Db.closeResultSet(rs);
		}
	}

	@Override
	public List<EntradaProduto> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT * FROM ENTRADA_PRODUTO "
					+ "INNER JOIN nota_compra_material ON nota_compra_material.NUMERO_NF = ENTRADA_PRODUTO.NUMERO_NF "
					+ "INNER JOIN PRODUTO ON PRODUTO.COD_PRODUTO = ENTRADA_PRODUTO.COD_PRODUTO "
					+ "INNER JOIN FORNECEDOR ON nota_compra_material.COD_FORNECEDOR = FORNECEDOR.COD_FORNECEDOR "
					+ "INNER JOIN FUNCIONARIO ON FUNCIONARIO.REGISTRO_FUNC = ENTRADA_PRODUTO.RESP_RECEB GROUP BY COD_ENTRADA");

			rs = st.executeQuery();

			List<EntradaProduto> listaEntrada = new ArrayList<>();
			Map<Integer, Produto> produtoMap = new HashMap<>();
			Map<Integer, NotasCompras> notasComprasMap = new HashMap<>();
			Map<Integer, Funcionario> funcionarioMap = new HashMap<>();
			Map<Integer, Fornecedor> fornecedorMap = new HashMap<>();

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

				NotasCompras notaCompra = notasComprasMap.get(rs.getInt("COD_NOTA"));
				if (notaCompra == null) {
					notaCompra = criarNotasCompras(rs, fornecedor, produto);
					notasComprasMap.put(rs.getInt("COD_NOTA"), notaCompra);
				}

				Funcionario funcionario = funcionarioMap.get(rs.getInt(("REGISTRO_FUNC")));
				if (funcionario == null) {
					funcionario = criarFuncionario(rs);
					funcionarioMap.put(rs.getInt("REGISTRO_FUNC"), funcionario);
				}

				EntradaProduto obj = criarEntradaProduto(rs, produto, notaCompra, funcionario);
				listaEntrada.add(obj);
			}

			return listaEntrada;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			Db.closeStatement(st);
			Db.closeResultSet(rs);
		}

	}

	private EntradaProduto criarEntradaProduto(ResultSet rs, Produto prod, NotasCompras notaCompra, Funcionario func)
			throws SQLException {
		EntradaProduto obj = new EntradaProduto();
		obj.setCodEntrada(rs.getInt("COD_ENTRADA"));
		obj.setNumeroNF(notaCompra);
		obj.setCodProduto(prod);
		obj.setDataEntrada(rs.getDate("DATA_ENTRADA"));
		obj.setQuantidade(notaCompra);
		obj.setValorUnit(notaCompra);
		obj.setValorTotal(notaCompra);
		obj.setValorTotalNota(notaCompra);
		obj.setRespRecebimento(func);
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

}
