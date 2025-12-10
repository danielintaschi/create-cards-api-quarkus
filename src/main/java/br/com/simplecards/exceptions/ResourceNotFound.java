package br.com.simplecards.exceptions;

import jakarta.ws.rs.NotFoundException;

@SuppressWarnings("serial")
public class ResourceNotFound extends NotFoundException {
	public ResourceNotFound(String msg) {
		super(msg);
	}
}
