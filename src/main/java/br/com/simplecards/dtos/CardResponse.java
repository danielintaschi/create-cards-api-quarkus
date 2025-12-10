package br.com.simplecards.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.simplecards.enums.CardStatus;
import br.com.simplecards.enums.CardType;

public record CardResponse (
		UUID id,
		
		String cpf,
		
		@JsonProperty("numeroCartao")
		String cardNumber,
		
		@JsonProperty("nomeImpresso")
		String namePrinted,
		
		@JsonProperty("produto")
		String product,
		
		@JsonProperty("subproduto")
		String subproduct,
		
		@JsonProperty("cartaoTipo")
		CardType cardType,
		
		@JsonProperty("cartaoStatus")
		CardStatus cardStatus,
		
		@JsonProperty("criadoEm")
		LocalDateTime createdAt
		){

}
