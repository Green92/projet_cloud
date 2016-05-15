package com.badsheepcorp.appmanager.exmapper;

import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.validation.ValidationError;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<Throwable> {

	public static final String GENERIC_ERROR_MESSAGE =
			"Invalid data supplied for request";
	
	public static final String UNEXPECTED_ERROR_MESSAGE =
			"An unexpected error occured. Please verify your request.";
	
	public static final String NOT_SUPPORTED_MEDIA_TYEP_MESSAGE = 
			"Not supported serialization format. If it is a usual format, you may have forgotten Content-Type HTTP header.";
	
	@Override
	public Response toResponse(Throwable e) {
		
		if (e instanceof UnrecognizedPropertyException) {
			
			UnrecognizedPropertyException upe = (UnrecognizedPropertyException) e;
			
			String errorMessage = String.format("%s : property \"%s\" doesn't exists (allowed properties : %s)",
				    				GENERIC_ERROR_MESSAGE,
				    				upe.getPropertyName(),
				    				upe.getKnownPropertyIds());
			
			return Response.status(Status.BAD_REQUEST)
	                .entity(new ValidationError(errorMessage, "", upe.getPathReference(), upe.getPropertyName()))
	                .encoding("utf-8")
	                .build();
			
		} else if (e instanceof JsonParseException) {
			
			JsonParseException jpe = (JsonParseException) e;
			
			String errorMessage = String.format("%s : %s", GENERIC_ERROR_MESSAGE, jpe.getOriginalMessage());
			
			return Response.status(Status.BAD_REQUEST)
	                .entity(new ValidationError(errorMessage, "", "", ""))
	                .encoding("utf-8")
	                .build();
		
		} else if (e instanceof InvalidFormatException) {
			
			InvalidFormatException ife = (InvalidFormatException) e;
			
			String errorMessage = String.format("%s : %s.", GENERIC_ERROR_MESSAGE, ife.getOriginalMessage());
			
			return Response.status(Status.BAD_REQUEST)
	                .entity(new ValidationError(errorMessage, "", ife.getPathReference(), ife.getValue().toString()))
	                .encoding("utf-8")
	                .build();
			
		} else if (e instanceof NotSupportedException) {
			
			return Response.status(Status.UNSUPPORTED_MEDIA_TYPE)
	                .entity(new ValidationError(NOT_SUPPORTED_MEDIA_TYEP_MESSAGE, "", "", ""))
	                .encoding("utf-8")
	                .build();
		} else if (e instanceof NotAcceptableException) {
			
			NotAcceptableException nae = (NotAcceptableException) e;
			
			return Response.status(Status.NOT_ACCEPTABLE)
	                .entity(new ValidationError(nae.getLocalizedMessage(), "", "", ""))
	                .encoding("utf-8")
	                .build();
			
		} else {
			
			e.printStackTrace();
			
			return Response.status(Status.INTERNAL_SERVER_ERROR)
	                .entity(new ValidationError(UNEXPECTED_ERROR_MESSAGE, "", "unknown", "unknown"))
	                .encoding("utf-8")
	                .build();
			
		}
	}

}