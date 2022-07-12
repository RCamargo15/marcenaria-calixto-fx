package marcenaria.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class EntradaProduto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer codEntrada;
	private NotasCompras numeroNF;
	private Produto codProduto;
	private Date dataEntrada;
	private Integer quantidade;
	private NotasCompras valorUnit;
	private NotasCompras valorTotal;
	private NotasCompras valorTotalNota;

	public EntradaProduto() {

	}

	public EntradaProduto(Integer codEntrada, NotasCompras numeroNF, Produto codProduto, Date dataEntrada,
			Integer quantidade, NotasCompras valorUnit, NotasCompras valorTotal, NotasCompras valorTotalNota,
			Funcionario respRecebimento) {
		this.numeroNF = numeroNF;
		this.codProduto = codProduto;
		this.dataEntrada = dataEntrada;
		this.quantidade = quantidade;
		this.valorUnit = valorUnit;
		this.valorTotal = valorTotal;
		this.valorTotalNota = valorTotalNota;
	}

	public Integer getCodEntrada() {
		return codEntrada;
	}

	public void setCodEntrada(Integer codEntrada) {
		this.codEntrada = codEntrada;
	}

	public NotasCompras getNumeroNF() {
		return numeroNF;
	}

	public void setNumeroNF(NotasCompras numeroNF) {
		this.numeroNF = numeroNF;
	}

	public Produto getCodProduto() {
		return codProduto;
	}

	public void setCodProduto(Produto codProduto) {
		this.codProduto = codProduto;
	}

	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public NotasCompras getValorUnit() {
		return valorUnit;
	}

	public void setValorUnit(NotasCompras valorUnit) {
		this.valorUnit = valorUnit;
	}

	public NotasCompras getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(NotasCompras valorTotal) {
		this.valorTotal = valorTotal;
	}

	public NotasCompras getValorTotalNota() {
		return valorTotalNota;
	}

	public void setValorTotalNota(NotasCompras valorTotalNota) {
		this.valorTotalNota = valorTotalNota;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codEntrada, codProduto, numeroNF, quantidade, valorTotal, valorTotalNota, valorUnit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntradaProduto other = (EntradaProduto) obj;
		return Objects.equals(codEntrada, other.codEntrada) && Objects.equals(codProduto, other.codProduto)
				&& Objects.equals(numeroNF, other.numeroNF) && Objects.equals(quantidade, other.quantidade)
				&& Objects.equals(valorTotal, other.valorTotal) && Objects.equals(valorTotalNota, other.valorTotalNota)
				&& Objects.equals(valorUnit, other.valorUnit);
	}

	@Override
	public String toString() {
		return "EntradaProduto [codEntrada=" + codEntrada + ", numeroNF=" + numeroNF + ", codProduto=" + codProduto
				+ ", dataEntrada=" + dataEntrada + ", quantidade=" + quantidade + ", valorUnit=" + valorUnit
				+ ", valorTotal=" + valorTotal + ", valorTotalNota=" + valorTotalNota 
				+ "]";
	}

}
