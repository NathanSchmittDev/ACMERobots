package dados.Robos;

public abstract class Robo {

	private int id;
	private String modelo;
	private double valorDiario;

	public Robo(int id, String modelo, double valorDiario){
		this.id=id;
		this.modelo=modelo;
		this.valorDiario=valorDiario;
	}

	public abstract double calculaLocacao(int dias);

	public double calculaValorLocacao(int dias) {
		return valorDiario * dias;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public double getValorDiario() {
		return valorDiario;
	}

	public void setValorDiario(double valorDiario) {
		this.valorDiario = valorDiario;
	}

	@Override
	public String toString() {
		return "ID: " + id + ", Modelo: " + modelo + ", Valor Diário: " + valorDiario;
	}

}
