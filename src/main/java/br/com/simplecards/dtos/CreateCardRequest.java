package br.com.simplecards.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CreateCardRequest (
		
		@NotBlank String cpf,
		
		@JsonProperty("nomeImpresso")
		@NotBlank String namePrinted,
		
		@JsonProperty("produto")
		@NotBlank String product,
		
		@JsonProperty("subproduto")
		@NotBlank String subproduct
		) {	 
}
