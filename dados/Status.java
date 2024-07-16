package dados;

public enum Status {
	CADASTRADA,
	EXECUTANDO,
	CONCLUIDA,
	CANCELADA;

	public boolean podeMudarPara(Status novoStatus) {
		// Lógica para verificar se é possível mudar de um status para outro
		switch (this) {
			case CADASTRADA:
				return novoStatus == EXECUTANDO;
			case EXECUTANDO:
				return novoStatus == CONCLUIDA || novoStatus == CANCELADA;
			case CONCLUIDA:
			case CANCELADA:
			default:
				return false;
		}
	}

	
}
