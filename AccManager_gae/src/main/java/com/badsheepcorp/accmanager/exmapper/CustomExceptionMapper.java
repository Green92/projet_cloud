package com.badsheepcorp.accmanager.exmapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<Throwable> {

	public static final String GENERIC_ERROR_MESSAGE = "Invalid data supplied for request";
	
	@Override
	public Response toResponse(Throwable e) {
		
		if (e instanceof UnrecognizedPropertyException) {
			
			UnrecognizedPropertyException upe = (UnrecognizedPropertyException) e;
			
			return Response.status(Status.BAD_REQUEST)
	                .entity(
	                		String.format("%s : property \"%s\" doesn't exists (allowed properties : %s)",
	                				GENERIC_ERROR_MESSAGE,
	                				upe.getPropertyName(),
	                				upe.getKnownPropertyIds()))
	                .encoding("utf-8")
	                .build();
			
		} else if (e instanceof JsonParseException) {
			
			JsonParseException jpe = (JsonParseException) e;
			
			return Response.status(Status.BAD_REQUEST)
	                .entity(String.format("%s : %s", GENERIC_ERROR_MESSAGE, jpe.getOriginalMessage()))
	                .encoding("utf-8")
	                .build();
			
		} else { 
			
			//IT'S NEVER MY FAULT !!! MOUHAHAHAHAH !!!
			return Response.status(Status.BAD_REQUEST)
	                .entity(String.format("%s.", GENERIC_ERROR_MESSAGE))
	                .encoding("utf-8")
	                .build();
			
		}
	}

}
