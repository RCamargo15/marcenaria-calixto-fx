package marcenaria.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class NotasCompras implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer codNota;
	private Fornecedor codFornecedor;
	private String numeroNF;
	private Produto codProduto;
	private Integer quantidade;
	private Double valorUnit;
	private Double valorTotal;
	private Double valorTotalNota;
	private String chaveNF;
	private Date dataEmissao;
	private String obs;

	public NotasCompras() {

	}

	public NotasCompras(Integer codNota, Fornecedor codFornecedor, String numeroNF, Produto codProduto,
			Integer quantidade, Double valorUnit, Double valorTotal, Double valorTotalNota, String chaveNF,
			Date dataEmissao, String obs) {
		this.codNota = codNota;
		this.codFornecedor = codFornecedor;
		this.numeroNF = numeroNF;
		this.codProduto = codProduto;
		this.quantidade = quantidade;
		this.valorUnit = valorUnit;
		this.valorTotal = valorTotal;
		this.valorTotalNota = valorTotalNota;
		this.chaveNF = chaveNF;
		this.dataEmissao = dataEmissao;
		this.obs = obs;
	}

	public Integer getCodNota() {
		return codNota;
	}

	public void setCodNota(Integer codNota) {
		this.codNota = codNota;
	}

	public Fornecedor getCodFornecedor() {
		return codFornecedor;
	}

	public void setCodFornecedor(Fornecedor codFornecedor) {
		this.codFornecedor = codFornecedor;
	}

	public String getNumeroNF() {
		return numeroNF;
	}

	public void setNumeroNF(String numeroNF) {
		this.numeroNF = numeroNF;
	}

	public Produto getCodProduto() {
		return codProduto;
	}

	public void setCodProduto(Produto codProduto) {
		this.codProduto = codProduto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Double getValorUnit() {
		return valorUnit;
	}

	public void setValorUnit(Double valorUnit) {
		this.valorUnit = valorUnit;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Double getValorTotalNota() {
		return valorTotalNota;
	}

	public void setValorTotalNota(Double valorTotalNota) {
		this.valorTotalNota = valorTotalNota;
	}

	public String getChaveNF() {
		return chaveNF;
	}

	public void setChaveNF(String chaveNF) {
		this.chaveNF = chaveNF;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	@Override
	public int hashCode() {
		return Objects.hash(chaveNF, codNota, numeroNF, quantidade, valorTotal, valorTotalNota, valorUnit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotasCompras other = (NotasCompras) obj;
		return Objects.equals(chaveNF, other.chaveNF) && Objects.equals(codNota, other.codNota)
				&& Objects.equals(numeroNF, other.numeroNF) && Objects.equals(quantidade, other.quantidade)
				&& Objects.equals(valorTotal, other.valorTotal) && Objects.equals(valorTotalNota, other.valorTotalNota)
				&& Objects.equals(valorUnit, other.valorUnit);
	}

	@Override
	public String toString() {
		return "NotasCompras [codNota=" + codNota + ", codFornecedor=" + codFornecedor + ", numeroNF=" + numeroNF
				+ ", codProduto=" + codProduto + ", quantidade=" + quantidade + ", valorUnit=" + valorUnit
				+ ", valorTotal=" + valorTotal + ", valorTotalNota=" + valorTotalNota + ", chaveNF=" + chaveNF
				+ ", dataEmissao=" + dataEmissao + ", obs=" + obs + "]";
	}

}
