package marcenaria.entities;

import java.io.Serializable;
import java.util.Objects;

public class Fornecedor implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer codFornecedor;
	private String razaoSocial;
	private String nomeFantasia;
	private String cnpj;
	private String atividadeFim;
	private String rua;
	private Integer numero;
	private String complemento;
	private String bairro;
	private String cep;
	private Integer ddd;
	private String telefone;
	private String site;
	private String email;
	private String obs;

	public Fornecedor() {

	}

	public Fornecedor(Integer codFornecedor, String razaoSocial, String nomeFantasia, String cnpj, String atividadeFim,
			String rua, Integer numero, String complemento, String bairro, String cep, Integer ddd, String  telefone,
			String site, String email, String obs) {
		this.codFornecedor = codFornecedor;
		this.razaoSocial = razaoSocial;
		this.nomeFantasia = nomeFantasia;
		this.cnpj = cnpj;
		this.atividadeFim = atividadeFim;
		this.rua = rua;
		this.numero = numero;
		this.complemento = complemento;
		this.bairro = bairro;
		this.cep = cep;
		this.ddd = ddd;
		this.telefone = telefone;
		this.site = site;
		this.email = email;
		this.obs = obs;
	}

	public Integer getCodFornecedor() {
		return codFornecedor;
	}

	public void setCodFornecedor(Integer codFornecedor) {
		this.codFornecedor = codFornecedor;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getAtividadeFim() {
		return atividadeFim;
	}

	public void setAtividadeFim(String atividadeFim) {
		this.atividadeFim = atividadeFim;
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

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cnpj, codFornecedor, nomeFantasia);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fornecedor other = (Fornecedor) obj;
		return Objects.equals(cnpj, other.cnpj) && Objects.equals(codFornecedor, other.codFornecedor)
				&& Objects.equals(nomeFantasia, other.nomeFantasia);
	}

	@Override
	public String toString() {
		return "Fornecedor [codFornecedor=" + codFornecedor + ", razaoSocial=" + razaoSocial + ", nomeFantasia="
				+ nomeFantasia + ", cnpj=" + cnpj + ", atividadeFim=" + atividadeFim + ", rua=" + rua + ", numero="
				+ numero + ", complemento=" + complemento + ", bairro=" + bairro + ", cep=" + cep + ", ddd=" + ddd
				+ ", telefone=" + telefone + ", site=" + site + ", email=" + email + ", obs=" + obs + "]";
	}

}
