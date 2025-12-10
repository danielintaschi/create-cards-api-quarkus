package br.com.simplecards.services.interfaces;

import java.util.List;

import br.com.simplecards.dtos.CreateCardRequest;
import br.com.simplecards.entity.Card;

public interface CardInterface {
	List<Card> listAllCards();
	String generatedCardNumber();
	List<Card> createCard(CreateCardRequest request);
	Card activateCard(String cardNumber);
	List<Card> cancelCard(String cardNumber);
}
