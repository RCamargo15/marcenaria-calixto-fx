package marcenaria.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer codCliente;
	private String nome;
	private String rg;
	private String cpf;
	private String rua;
	private Integer numero;
	private String complemento;
	private String bairro;
	private String cep;
	private Integer ddd;
	private String telefone;
	private String celular;
	private String email;
	private Date dataCadastro;
	private String obs;

	public Cliente() {

	}

	public Cliente(Integer codCliente, String nome, String rg, String cpf, String rua, Integer numero,
			String complemento, String bairro, String cep, Integer ddd, String telefone, String celular, String email,
			Date dataCadastro, String obs) {
		this.codCliente = codCliente;
		this.nome = nome;
		this.rg = rg;
		this.cpf = cpf;
		this.rua = rua;
		this.numero = numero;
		this.complemento = complemento;
		this.bairro = bairro;
		this.cep = cep;
		this.ddd = ddd;
		this.telefone = telefone;
		this.celular = celular;
		this.email = email;
		this.dataCadastro = dataCadastro;
		this.obs = obs;
	}

	public Integer getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(Integer codCliente) {
		this.codCliente = codCliente;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Integer getDdd() {
		return ddd;
	}

	public void setDdd(Integer ddd) {
		this.ddd = ddd;
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

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codCliente, cpf);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(codCliente, other.codCliente) && Objects.equals(cpf, other.cpf);
	}

	@Override
	public String toString() {
		return "Cliente [codCliente=" + codCliente + ", nome=" + nome + ", rg=" + rg + ", cpf=" + cpf + ", rua=" + rua
				+ ", numero=" + numero + ", complemento=" + complemento + ", bairro=" + bairro + ", cep=" + cep
				+ ", ddd=" + ddd + ", telefone=" + telefone + ", celular=" + celular + ", email=" + email
				+ ", dataCadastro=" + dataCadastro + ", obs=" + obs + "]";
	}

}
