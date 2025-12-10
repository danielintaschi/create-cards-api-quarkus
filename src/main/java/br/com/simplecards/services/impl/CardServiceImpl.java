package br.com.simplecards.services.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import br.com.simplecards.dtos.CreateCardRequest;
import br.com.simplecards.entity.Card;
import br.com.simplecards.enums.CardStatus;
import br.com.simplecards.enums.CardType;
import br.com.simplecards.services.interfaces.CardInterface;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CardServiceImpl implements CardInterface{

	@Inject
	@Channel("cards-event")
	Emitter<String> emitter;

	public List<Card> listAllCards() {
		return Card.listAll();
	}

	// Método utilitário para criar números pseudo-randômicos
	private static final Random RANDOM = new Random();
	public String generatedCardNumber() {
		StringBuilder sb = new StringBuilder();
		sb.append("1302");
		for(int i = 0; i < 10; i++) {
			sb.append(RANDOM.nextInt(10));
		}
		return sb.toString();
	}

	@Transactional
	public List<Card> createCard(CreateCardRequest request) {
		String cardNumber = generatedCardNumber();		
		List<Card> cards = Card.createCardFromRequest(request, cardNumber);
		for(Card card : cards) {
			card.persist();
		}

		//Publica o evento no tópico Kafka
		Card c = cards.get(0);
		String eventJson = """
				{
				"eventType":"card-created",
				"cardNumber":"%s",
				"cpf":"%s",
				"namePrinted":"%s",
				"product":"%s",
				"subproduct":"%s",
				"cardType":"%s",
				"cardStatus:"%s",
				"createdAt":"%s"
				}
				""".formatted(
						c.getCardNumber(),
						c.getCpf(),
						c.getNamePrinted(),
						c.getProduct(),
						c.getSubproduct(),
						c.getCardType(),
						c.getCardStatus(),
						c.getCreatedAt());
		emitter.send(eventJson);

		return cards;
	}

	@Transactional
	public Card activateCard(String cardNumber) {	
		Card card = Card.find("cardNumber", cardNumber).firstResult();
		if (card == null) {
			throw new NoSuchElementException("Cartão " + cardNumber + " não localizado!");
		}		
		if(card.getCardStatus() != CardStatus.PENDING_ACTIVATION) {
			throw new IllegalStateException("Cartão " + cardNumber + " somente poderá ser ativado quando for " + CardType.PHYSICAL + " e estiver com o status " + CardStatus.PENDING_ACTIVATION);
		}	
		if(card.getCardStatus() == CardStatus.CANCELED) {
			throw new IllegalStateException("Cartão " + cardNumber + " encontra-se " + CardStatus.CANCELED + " e não pode ser ativado.");
		}
		if (card.getCardStatus() == CardStatus.PENDING_ACTIVATION && card.getCardType() == CardType.PHYSICAL) {
			card.setCardStatus(CardStatus.ACTIVATED);
		}
		//Publica o evento no tópico Kafka
		String eventJson = """
				{
				"eventType":"card-activated",
				"cardNumber":"%s",
				"cpf":"%s",
				"namePrinted":"%s",
				"cardType":"%s",
				"cardStatus":"%s",
				"createdAt":"%s"
				}
				""".formatted(
						card.getCardNumber(),
						card.getCpf(),
						card.getNamePrinted(),
						card.getCardType(),
						card.getCardStatus(),
						card.getCreatedAt());
		emitter.send(eventJson);

		return card;
	}

	@Transactional
	public List<Card> cancelCard(String cardNumber) {	
		List<Card> cards = Card.find("cardNumber", cardNumber).list();
		if (cards == null || cards.isEmpty()) {
			throw new NoSuchElementException("Cartão " + cardNumber + " não localizado!");
		}	
		for (Card c : cards) {
			if(c.getCardStatus() != CardStatus.ACTIVATED) {
				throw new IllegalStateException("Cartão " + cardNumber + " somente poderá ser cancelado quando o status for " + CardStatus.ACTIVATED);
			}
		}
		for (Card c : cards) {
			if(c.getCardStatus() == CardStatus.ACTIVATED) {
				c.setCardStatus(CardStatus.CANCELED);
			}
		}
		//Publica o evento no tópico Kafka
		Card c = cards.get(0);
		String eventJson = """
				{
				"eventType":"card-canceled",
				"cardNumber":"%s",
				"cpf":"%s",
				"namePrinted":"%s",
				"cardType":"%s",
				"cardStatus":"%s",
				"createdAt":"%s"
				}
				""".formatted(
						c.getCardNumber(),
						c.getCpf(),
						c.getNamePrinted(),
						c.getCardType(),
						c.getCardStatus(),
						c.getCreatedAt());
		emitter.send(eventJson);

		return cards;
	}
}
