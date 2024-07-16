package dados.Robos;

public class Agricola extends Robo {

	private double area;
	private String uso;

	public Agricola(int id, String modelo, double valorDiario, double area, String uso){
		super(id, modelo, valorDiario);
		this.area=area;
		this.uso=uso;
	}

	public double calculaLocacao(int dias) {
		return dias*(10*area);
	}

    public double getArea() {
        return this.area;
    }

    public String getUso() {
        return this.uso;
    }
}
