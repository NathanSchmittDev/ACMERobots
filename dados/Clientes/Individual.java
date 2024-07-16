package dados.Clientes;

public class Individual extends Cliente {

	private String cpf;

	public Individual(int codigo, String nome, String cpf){
		super(codigo, nome);
		this.cpf = cpf;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public double calculaDesconto(int qtdeRobos) {
		if(qtdeRobos>1){
			return 0.05;
		}else{
			return 0;
		}
	}

	@Override
	public String toString() {
		return "Codigo: " + getCodigo() + ", nome: " + getNome() + ", Tipo: Individual, cpf: " + cpf;
	}

}
