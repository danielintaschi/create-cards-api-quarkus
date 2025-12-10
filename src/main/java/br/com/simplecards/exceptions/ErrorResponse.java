package br.com.simplecards.exceptions;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponse (
		
		@JsonProperty("erro")
		String error,
		
		@JsonProperty("mensagem")
		String message,
		
		@JsonProperty("dataHoraErro")
		LocalDateTime timestamp
		
		) {

}
