package marcenaria.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Funcionario implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer registroFunc;
	private String nome;
	private String rg;
	private String cpf;
	private String ctps;
	private String rua;
	private Integer numero;
	private String complemento;
	private String bairro;
	private String cep;
	private String cidade;
	private String estado;
	private String uf;
	private Integer ddd;
	private String telefone;
	private String celular;
	private Date dataNasc;
	private Date dataAdmissao;
	private String tipoSang;
	private String funcao;
	private String setor;
	private Double salario;
	private String obs;

	public Funcionario() {

	}

	public Funcionario(Integer registroFunc, String nome, String rg, String cpf, String ctps, String rua,
			Integer numero, String complemento, String bairro, String cep, String cidade, String estado, String uf,
			Integer ddd, String telefone, String celular, Date dataNasc, Date dataAdmissao, String tipoSang,
			String funcao, String setor, Double salario, String obs) {
		super();
		this.registroFunc = registroFunc;
		this.nome = nome;
		this.rg = rg;
		this.cpf = cpf;
		this.ctps = ctps;
		this.rua = rua;
		this.numero = numero;
		this.complemento = complemento;
		this.bairro = bairro;
		this.cep = cep;
		this.cidade = cidade;
		this.estado = estado;
		this.uf = uf;
		this.ddd = ddd;
		this.telefone = telefone;
		this.celular = celular;
		this.dataNasc = dataNasc;
		this.dataAdmissao = dataAdmissao;
		this.tipoSang = tipoSang;
		this.funcao = funcao;
		this.setor = setor;
		this.salario = salario;
		this.obs = obs;
	}

	public Integer getRegistroFunc() {
		return registroFunc;
	}

	public void setRegistroFunc(Integer registroFunc) {
		this.registroFunc = registroFunc;
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

	public String getCtps() {
		return ctps;
	}

	public void setCtps(String ctps) {
		this.ctps = ctps;
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

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
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

	public Date getDataNasc() {
		return dataNasc;
	}

	public void setDataNasc(Date dataNasc) {
		this.dataNasc = dataNasc;
	}

	public Date getDataAdmissao() {
		return dataAdmissao;
	}

	public void setDataAdmissao(Date dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}

	public String getTipoSang() {
		return tipoSang;
	}

	public void setTipoSang(String tipoSang) {
		this.tipoSang = tipoSang;
	}

	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}

	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cpf, registroFunc);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Funcionario other = (Funcionario) obj;
		return Objects.equals(cpf, other.cpf) && Objects.equals(registroFunc, other.registroFunc);
	}

	@Override
	public String toString() {
		return "Funcionario [registroFunc=" + registroFunc + ", nome=" + nome + ", rg=" + rg + ", cpf=" + cpf
				+ ", ctps=" + ctps + ", rua=" + rua + ", numero=" + numero + ", complemento=" + complemento
				+ ", bairro=" + bairro + ", cep=" + cep + ", cidade=" + cidade + ", estado=" + estado + ", uf=" + uf
				+ ", ddd=" + ddd + ", telefone=" + telefone + ", celular=" + celular + ", dataNasc=" + dataNasc
				+ ", dataAdmissao=" + dataAdmissao + ", tipoSang=" + tipoSang + ", funcao=" + funcao + ", setor="
				+ setor + ", salario=" + salario + ", obs=" + obs + "]";
	}

}
