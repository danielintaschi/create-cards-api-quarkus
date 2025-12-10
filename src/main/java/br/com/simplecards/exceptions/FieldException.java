package br.com.simplecards.exceptions;

@SuppressWarnings("serial")
public class FieldException extends BusinessException {
	public FieldException(String field) {
		super("O campo " + field + " é obrigatório");
	}
}
