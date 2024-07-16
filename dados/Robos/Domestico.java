package dados.Robos;

public class Domestico extends Robo {

	private int nivel;

	public Domestico(int id, String modelo, double valorDiario, int nivel){
		super(id, modelo, valorDiario);
		this.nivel=nivel;
	}

	public double calculaLocacao(int dias) {
		double valDiario=0;
		switch(nivel){
			case 1:
				valDiario = dias*10;
				break;
			case 2:
				valDiario = dias*20;
				break;
			case 3:
				valDiario = dias*50;
				break;
			default:
				break;
		}
		return valDiario;
	}

    public int getNivel() {
        return this.nivel;
    }

}
