package br.com.simplecards.controller;

import java.time.LocalDateTime;
import java.util.List;

import br.com.simplecards.dtos.CardResponse;
import br.com.simplecards.dtos.CardSituationResponse;
import br.com.simplecards.dtos.CreateCardRequest;
import br.com.simplecards.entity.Card;
import br.com.simplecards.services.impl.CardServiceImpl;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/cartoes")
public class CardController {

	private CardServiceImpl cardService;
	
	@Inject
	public CardController(CardServiceImpl cardService) {
		this.cardService = cardService;
	}

	@GET
	@Path("/listar")
	public List<CardResponse> listAllCards() {
		return cardService.listAllCards()
				.stream()
				.map(card -> new CardResponse(
						card.getId(),
						card.getCpf(),
						card.getCardNumber(),
						card.getNamePrinted(),
						card.getProduct(),
						card.getSubproduct(),
						card.getCardType(),
						card.getCardStatus(),
						card.getCreatedAt()))
				.toList();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<CardResponse> create(@Valid CreateCardRequest request) {		
		return cardService.createCard(request)
				.stream()
				.map(card -> new CardResponse(
						card.getId(),
						card.getCpf(),
						card.getCardNumber(),
						card.getNamePrinted(),
						card.getProduct(),
						card.getSubproduct(),
						card.getCardType(),
						card.getCardStatus(),
						card.getCreatedAt()))
				.toList();
	}

	@POST
	@Path("/{cardNumber}/ativar")
	public CardSituationResponse activateCard(@Valid @PathParam("cardNumber") String cardNumber) {
		Card card = cardService.activateCard(cardNumber);
		return new CardSituationResponse(
				card.getId(), 
				card.getCpf(),
				card.getCardNumber(),
				card.getNamePrinted(), 
				card.getProduct(), 
				card.getSubproduct(), 
				card.getCardType(), 
				card.getCardStatus(), 
				card.getCreatedAt(),
				"Cartão ativado com sucesso | Data da ativação: " + LocalDateTime.now());
	}

	@POST
	@Path("/{cardNumber}/cancelar")
	public List<CardSituationResponse> cancelCard(@Valid @PathParam("cardNumber") String cardNumber) {
		List<Card> cancelled = cardService.cancelCard(cardNumber);
		return cancelled.stream()
				.map(card -> new CardSituationResponse(
						card.getId(), 
						card.getCpf(),
						card.getCardNumber(),
						card.getNamePrinted(), 
						card.getProduct(), 
						card.getSubproduct(), 
						card.getCardType(), 
						card.getCardStatus(), 
						card.getCreatedAt(),
						"Cartão cancelado com sucesso | Data do cancelamento: " + LocalDateTime.now()))
				.toList();
	}

}
