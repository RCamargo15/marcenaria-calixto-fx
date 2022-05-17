package marcenaria.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class OrdemServicoEmpresa implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer numeroPedido;
	private Empresa codEmpresa;
	private String nomeResponsavel;
	private String descServico;
	private Date dataOrdem;
	private Date dataInicio;
	private Date prazoEntrega;
	private Date dataEntrega;
	private String statusServico;
	private Double valorTotal;
	private Funcionario funcResponsavel;
	private String obs;

	public OrdemServicoEmpresa() {

	}

	public OrdemServicoEmpresa(Integer id, Integer numeroPedido, Empresa codEmpresa, String nomeResponsavel,
			String descServico, Date dataOrdem, Date dataInicio, Date prazoEntrega, 
			String statusServico, Double valorTotal, Funcionario funcResponsavel, String obs) {
		;
		this.numeroPedido = numeroPedido;
		this.codEmpresa = codEmpresa;
		this.nomeResponsavel = nomeResponsavel;
		this.descServico = descServico;
		this.dataOrdem = dataOrdem;
		this.dataInicio = dataInicio;
		this.prazoEntrega = prazoEntrega;
		this.statusServico = statusServico;
		this.valorTotal = valorTotal;
		this.funcResponsavel = funcResponsavel;
		this.obs = obs;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(Integer numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public Empresa getCodEmpresa() {
		return codEmpresa;
	}

	public void setCodEmpresa(Empresa codEmpresa) {
		this.codEmpresa = codEmpresa;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getDescServico() {
		return descServico;
	}

	public void setDescServico(String descServico) {
		this.descServico = descServico;
	}

	public Date getDataOrdem() {
		return dataOrdem;
	}

	public void setDataOrdem(Date dataOrdem) {
		this.dataOrdem = dataOrdem;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getPrazoEntrega() {
		return prazoEntrega;
	}

	public void setPrazoEntrega(Date prazoEntrega) {
		this.prazoEntrega = prazoEntrega;
	}

	public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public String getStatusServico() {
		return statusServico;
	}

	public void setStatusServico(String statusServico) {
		this.statusServico = statusServico;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Funcionario getFuncResponsavel() {
		return funcResponsavel;
	}

	public void setFuncResponsavel(Funcionario funcResponsavel) {
		this.funcResponsavel = funcResponsavel;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codEmpresa, funcResponsavel, id, nomeResponsavel, numeroPedido);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrdemServicoEmpresa other = (OrdemServicoEmpresa) obj;
		return Objects.equals(codEmpresa, other.codEmpresa) && Objects.equals(funcResponsavel, other.funcResponsavel)
				&& Objects.equals(id, other.id) && Objects.equals(nomeResponsavel, other.nomeResponsavel)
				&& Objects.equals(numeroPedido, other.numeroPedido);
	}

	@Override
	public String toString() {
		return "OrdemServicoEmpresa [id=" + id + ", numeroPedido=" + numeroPedido + ", codEmpresa=" + codEmpresa
				+ ", nomeResponsavel=" + nomeResponsavel + ", descServico=" + descServico + ", dataOrdem=" + dataOrdem
				+ ", dataInicio=" + dataInicio + ", prazoEntrega=" + prazoEntrega + ", dataEntrega=" + dataEntrega
				+ ", statusServico=" + statusServico + ", valorTotal=" + valorTotal + ", funcResponsavel="
				+ funcResponsavel + ", obs=" + obs + "]";
	}

}
