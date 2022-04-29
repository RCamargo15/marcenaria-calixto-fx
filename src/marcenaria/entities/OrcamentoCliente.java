package marcenaria.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class OrcamentoCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer numOrcamento;
	private Cliente codCliente;
	private String telefone;
	private String celular;
	private String email;
	private String descServico;
	private Date dataOrcamento;
	private Produto codProduto;
	private Integer quantidade;
	private Produto valor;
	private Double valorObra;
	private Double valorMetroQuad;
	private Double valorTotal;
	private String obs;

	public OrcamentoCliente() {

	}

	public OrcamentoCliente(Integer numOrcamento, Cliente codCliente, String telefone, String celular, String email,
			String descServico, Date dataOrcamento, Produto codProduto, Integer quantidade, Double valorTotal,
			String obs) {
		super();
		this.numOrcamento = numOrcamento;
		this.codCliente = codCliente;
		this.telefone = telefone;
		this.celular = celular;
		this.email = email;
		this.descServico = descServico;
		this.dataOrcamento = dataOrcamento;
		this.codProduto = codProduto;
		this.quantidade = quantidade;
		this.valorTotal = realValorTotal(valor, quantidade);
		this.obs = obs;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNumOrcamento() {
		return numOrcamento;
	}

	public void setNumOrcamento(Integer numOrcamento) {
		this.numOrcamento = numOrcamento;
	}

	public Cliente getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(Cliente codCliente) {
		this.codCliente = codCliente;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescServico() {
		return descServico;
	}

	public void setDescServico(String descServico) {
		this.descServico = descServico;
	}

	public Date getDataOrcamento() {
		return dataOrcamento;
	}

	public void setDataOrcamento(Date dataOrcamento) {
		this.dataOrcamento = dataOrcamento;
	}

	public Produto getCodProduto() {
		return codProduto;
	}

	public void setCodProduto(Produto codProduto) {
		this.codProduto = codProduto;
	}

	public Double getValor() {
		return valor.getPrecoUnit();
	}

	public void setValor(Produto valor) {
		this.valor = valor;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Double realValorTotal(Produto prod, Integer quantidade) {
		return prod.getPrecoUnit() * quantidade;
	}

	public Double getValorObra() {
		return valorObra;
	}

	public void setValorObra(Double valorObra) {
		this.valorObra = valorObra;
	}

	public Double getValorMetroQuad() {
		return valorMetroQuad;
	}

	public void setValorMetroQuad(Double valorMetroQuad) {
		this.valorMetroQuad = valorMetroQuad;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, numOrcamento);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrcamentoCliente other = (OrcamentoCliente) obj;
		return Objects.equals(id, other.id) && Objects.equals(numOrcamento, other.numOrcamento);
	}

	@Override
	public String toString() {
		return "OrcamentoCliente [id=" + id + ", numOrcamento=" + numOrcamento + ", codCliente=" + codCliente
				+ ", telefone=" + telefone + ", celular=" + celular + ", email=" + email + ", descServico="
				+ descServico + ", dataOrcamento=" + dataOrcamento + ", codProduto=" + codProduto + ", quantidade="
				+ quantidade + ", valor=" + valor + ", valorTotal=" + valorTotal + ", obs=" + obs + "]";
	}

}
