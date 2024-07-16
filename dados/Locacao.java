package dados;

import dados.Clientes.Cliente;
import dados.Robos.Robo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Locacao {

	private Set<Robo> robos;
	private Set<Cliente> clientes;
	private int numero;
	private Status situacao;
	private Date dataInicio;
	private Date dataFim;

	public Locacao(int numero, Status situacao, Date dataInicio, Date dataFim){
		this.numero = numero;
		this.situacao = situacao;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.robos = new HashSet<>();
		this.clientes = new HashSet<>();
	}

	public void addRobo(Robo robo) {
		this.robos.add(robo);
	}

	public void addCliente(Cliente cliente) {
		this.clientes.add(cliente);
	}

	public Set<Robo> getRobos() {
		return robos;
	}

	public void setRobos(Set<Robo> robos) {
		this.robos = robos;
	}

	public Set<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(Set<Cliente> clientes) {
		this.clientes = clientes;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public Status getSituacao() {
		return situacao;
	}

	public void setSituacao(Status situacao) {
		this.situacao = situacao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public void setCliente(Cliente cliente) {
		this.clientes.clear();
		this.clientes.add(cliente);
	}

	public Cliente getCliente() {
		if (!clientes.isEmpty()) {
			return clientes.iterator().next();
		}
		return null;
	}

	public String getRobosIds() {
		StringBuilder ids = new StringBuilder();
		for (Robo robo : robos) {
			if (ids.length() > 0) {
				ids.append(",");
			}
			ids.append(robo.getId());
		}
		return ids.toString();
	}

}