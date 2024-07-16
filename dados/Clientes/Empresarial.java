package dados.Clientes;

public class Empresarial extends Cliente {

	private int ano;

	public Empresarial(int codigo, String nome, int ano){
		super(codigo, nome);
		this.ano = ano;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public double calculaDesconto(int qtdeRobos) {
		if(qtdeRobos>=2 && qtdeRobos <=9){
			return 0.03;
		}else if(qtdeRobos>10){
			return 0.07;
		}else{
			return 0;
		}
	}

	@Override
	public String toString() {
		return "Codigo: " + getCodigo() + ", nome: " + getNome() + ", Tipo: Empresarial, ano: " + ano;
	}

}
