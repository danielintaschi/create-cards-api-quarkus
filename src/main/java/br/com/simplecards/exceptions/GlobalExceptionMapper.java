package br.com.simplecards.exceptions;

import java.time.LocalDateTime;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.NotFoundException;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception e) {
		if (e instanceof BusinessException be) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorResponse(
							"Regra de negócio inválida",
							be.getMessage(),
							LocalDateTime.now()))
					.build();
		}
		if (e instanceof NotFoundException ne) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(new ErrorResponse(
							"Recurso não localizado",
							ne.getMessage(),
							LocalDateTime.now()))
					.build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(new ErrorResponse(
						"Erro interno",
						e.getMessage(),
						LocalDateTime.now()))
				.build();
	}
}
