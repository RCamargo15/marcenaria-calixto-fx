package marcenaria.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class OrdemServicoCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer numeroPedido;
	private Cliente codCliente;
	private String descServico;
	private Date dataOrdem;
	private Date dataInicio;
	private Date prazoEntrega;
	private Date dataEntrega;
	private String statusServico;
	private Double valorTotal;
	private Funcionario funcResponsavel;
	private String obs;

	public OrdemServicoCliente() {

	}

	public OrdemServicoCliente(Integer numeroPedido, Cliente codCliente, String descServico, Date dataOrdem,
			Date dataInicio, Date prazoEntrega, Date dataEntrega, String statusServico, Double valorTotal,
			Funcionario funcResponsavel, String obs) {
		super();
		this.numeroPedido = numeroPedido;
		this.codCliente = codCliente;
		this.descServico = descServico;
		this.dataOrdem = dataOrdem;
		this.dataInicio = dataInicio;
		this.prazoEntrega = prazoEntrega;
		this.dataEntrega = dataEntrega;
		this.statusServico = statusServico;
		this.valorTotal = valorTotal;
		this.funcResponsavel = funcResponsavel;
		this.obs = obs;
	}

	public Integer getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(Integer numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public Cliente getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(Cliente codCliente) {
		this.codCliente = codCliente;
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
		return Objects.hash(codCliente, numeroPedido, statusServico);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrdemServicoCliente other = (OrdemServicoCliente) obj;
		return Objects.equals(codCliente, other.codCliente) && Objects.equals(numeroPedido, other.numeroPedido)
				&& Objects.equals(statusServico, other.statusServico);
	}

	@Override
	public String toString() {
		return "OrdemServicoCliente [numeroPedido=" + numeroPedido + ", codCliente=" + codCliente + ", descServico="
				+ descServico + ", dataOrdem=" + dataOrdem + ", dataInicio=" + dataInicio + ", prazoEntrega="
				+ prazoEntrega + ", dataEntrega=" + dataEntrega + ", statusServico=" + statusServico + ", valorTotal="
				+ valorTotal + ", funcResponsavel=" + funcResponsavel + ", obs=" + obs + "]";
	}

}