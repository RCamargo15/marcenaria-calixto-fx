package marcenaria.entities.auxiliar;

import java.io.Serializable;
import java.util.Objects;

public class ProdutoOrcamento implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer codProduto;
	private String descProduto;
	private Double precoProd;
	private Double valorDesconto;
	private Double valorTotal;
	private Double valorTotalFinal;
	private Integer quantidade;

	public ProdutoOrcamento() {

	}

	public ProdutoOrcamento(Integer codProduto, String descProduto, Double precoProd, Integer quantidade) {
		super();
		this.codProduto = codProduto;
		this.descProduto = descProduto;
		this.precoProd = precoProd;
		this.quantidade = quantidade;
	}

	public Integer getCodProduto() {
		return codProduto;
	}

	public void setCodProduto(Integer codProduto) {
		this.codProduto = codProduto;
	}

	public String getDescProduto() {
		return descProduto;
	}

	public void setDescProduto(String descProduto) {
		this.descProduto = descProduto;
	}

	public Double getPrecoProd() {
		return precoProd;
	}

	public void setPrecoProd(Double precoProd) {
		this.precoProd = precoProd;
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

	public Double getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public Double getValorTotalFinal() {
		return valorTotalFinal;
	}

	public void setValorTotalFinal(Double valorTotalFinal) {
		this.valorTotalFinal = valorTotalFinal;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codProduto, descProduto, precoProd, quantidade);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoOrcamento other = (ProdutoOrcamento) obj;
		return Objects.equals(codProduto, other.codProduto) && Objects.equals(descProduto, other.descProduto)
				&& Objects.equals(precoProd, other.precoProd) && Objects.equals(quantidade, other.quantidade);
	}

	@Override
	public String toString() {
		return descProduto;
	}
}
