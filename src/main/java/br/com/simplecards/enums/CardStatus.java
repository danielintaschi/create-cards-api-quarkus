package br.com.simplecards.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CardStatus {
	
	@JsonProperty("Pendente_Ativacao")
	PENDING_ACTIVATION,
	
	@JsonProperty("Ativo")
	ACTIVATED,
	
	@JsonProperty("Cancelado")
	CANCELED
}
