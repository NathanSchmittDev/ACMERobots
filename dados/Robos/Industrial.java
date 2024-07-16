package dados.Robos;

public class Industrial extends Robo {

	private String setor;

	public Industrial(int id, String modelo, double valorDiario, String setor){
		super(id, modelo, valorDiario);
		this.setor=setor;
	}

	public double calculaLocacao(int dias) {
		return dias*90;
	}

	public String getSetor() {
		return this.setor;
	}
}
