package marcenaria.entities;

import java.io.Serializable;
import java.util.Objects;

public class Produto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer codProduto;
	private String descProduto;
	private Double precoUnit;

	public Produto() {

	}

	public Produto(String descProduto, Double precoUnit) {
	
		this.descProduto = descProduto;
		this.precoUnit = precoUnit;
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

	public Double getPrecoUnit() {
		return precoUnit;
	}

	public void setPrecoUnit(Double precoUnit) {
		this.precoUnit = precoUnit;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codProduto, descProduto, precoUnit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		return Objects.equals(codProduto, other.codProduto) && Objects.equals(descProduto, other.descProduto)
				&& Objects.equals(precoUnit, other.precoUnit);
	}

	@Override
	public String toString() {
		return descProduto;
	}

}
