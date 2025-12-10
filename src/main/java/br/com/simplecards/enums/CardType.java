package br.com.simplecards.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CardType {
	
	@JsonProperty("Fisico")
	PHYSICAL,
	
	@JsonProperty("Online")
	ONLINE
}
