package br.com.simplecards.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.simplecards.dtos.CreateCardRequest;
import br.com.simplecards.enums.CardStatus;
import br.com.simplecards.enums.CardType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card extends PanacheEntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	private String cardNumber;
	
	private String cpf;
	
	@JsonProperty("nomeImpresso")
	private String namePrinted;
	
	@JsonProperty("produto")
	private String product;
	
	@JsonProperty("subproduct")
	private String subproduct;
	
	@JsonProperty("cartaoTipo")
	private CardType cardType;
	
	@JsonProperty("cartaoStatus")
	private CardStatus cardStatus;

	@JsonProperty("criadoEm")
	private LocalDateTime createdAt;
	
	public static List<Card> createCardFromRequest(CreateCardRequest request, String cardNumber) {
		
		Card physical = new Card();
		physical.cardNumber = cardNumber;
		physical.cpf = request.cpf();
		physical.namePrinted = request.namePrinted();
		physical.product = request.product();
		physical.subproduct = request.subproduct();
		physical.cardType = CardType.PHYSICAL;
		physical.cardStatus = CardStatus.PENDING_ACTIVATION;
		physical.createdAt = LocalDateTime.now();
		
		Card online = new Card();
		online.cardNumber = cardNumber;
		online.cpf = request.cpf();
		online.namePrinted = request.namePrinted();
		online.product = request.product();
		online.subproduct = request.subproduct();
		online.cardType = CardType.ONLINE;
		online.cardStatus = CardStatus.ACTIVATED;
		online.createdAt = LocalDateTime.now();

		return List.of(physical, online);
	}
	
}
