package marcenaria.entities;

import java.io.Serializable;
import java.util.Objects;

public class Estoque implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Produto codProduto;
	private Integer estoqueAtual;
	private Integer estoqueMinimo;

	public Estoque() {

	}

	public Estoque(Integer id, Produto codProduto, Integer estoqueAtual, Integer estoqueMinimo) {
		this.id = id;
		this.codProduto = codProduto;
		this.estoqueAtual = estoqueAtual;
		this.estoqueMinimo = estoqueMinimo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Produto getCodProduto() {
		return codProduto;
	}

	public void setCodProduto(Produto codProduto) {
		this.codProduto = codProduto;
	}

	public Integer getEstoqueAtual() {
		return estoqueAtual;
	}

	public void setEstoqueAtual(Integer estoqueAtual) {
		this.estoqueAtual = estoqueAtual;
	}

	public Integer getEstoqueMinimo() {
		return estoqueMinimo;
	}

	public void setEstoqueMinimo(Integer estoqueMinimo) {
		this.estoqueMinimo = estoqueMinimo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estoque other = (Estoque) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Estoque [id=" + id + ", codProduto=" + codProduto + ", estoqueAtual=" + estoqueAtual + ", estoqueMinimo="
				+ estoqueMinimo + "]";
	}
}
