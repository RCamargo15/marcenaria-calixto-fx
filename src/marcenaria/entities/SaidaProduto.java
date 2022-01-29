package marcenaria.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class SaidaProduto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer codSaida;
	private Estoque idEstoque;
	private Estoque codProduto;
	private Date dataSaida;
	private Integer quantidade;
	private Funcionario respSaida;

	public SaidaProduto() {

	}

	public SaidaProduto(Integer codSaida, Estoque idEstoque, Estoque codProduto, Date dataSaida, Integer quantidade,
			Funcionario respSaida) {
		this.codSaida = codSaida;
		this.codProduto = codProduto;
		this.idEstoque = idEstoque;
		this.dataSaida = dataSaida;
		this.quantidade = quantidade;
		this.respSaida = respSaida;
	}

	public Integer getCodSaida() {
		return codSaida;
	}

	public void setCodSaida(Integer codSaida) {
		this.codSaida = codSaida;
	}
	
	public Estoque getIdEstoque() {
		return idEstoque;
	}

	public void setIdEstoque(Estoque idEstoque) {
		this.idEstoque = idEstoque;
	}

	public Estoque getCodProduto() {
		return codProduto;
	}

	public void setCodProduto(Estoque codProduto) {
		this.codProduto = codProduto;
	}

	public Date getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(Date dataSaida) {
		this.dataSaida = dataSaida;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Funcionario getRespSaida() {
		return respSaida;
	}

	public void setRespSaida(Funcionario respSaida) {
		this.respSaida = respSaida;
	}

	

	@Override
	public int hashCode() {
		return Objects.hash(codProduto, codSaida, idEstoque);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SaidaProduto other = (SaidaProduto) obj;
		return Objects.equals(codProduto, other.codProduto) && Objects.equals(codSaida, other.codSaida)
				&& Objects.equals(idEstoque, other.idEstoque);
	}

	@Override
	public String toString() {
		return "SaidaProduto [codSaida=" + codSaida + ", idEstoque" + idEstoque + ", codProduto=" + codProduto + ", dataSaida=" + dataSaida
				+ ", quantidade=" + quantidade + ", respSaida=" + respSaida + "]";
	}

}
