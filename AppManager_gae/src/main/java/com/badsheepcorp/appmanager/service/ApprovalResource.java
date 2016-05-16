package com.badsheepcorp.appmanager.service;

import java.net.URI;
import java.util.HashMap;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.server.validation.ValidationError;

import com.badsheepcorp.appmanager.business.Approval;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

@Path("approval")
public class ApprovalResource {
	
	@Context
	private UriInfo uriInfo;
	
	private DatastoreService datastore = null;
	
	private DatastoreService getDataStore() {
		if (datastore == null) {
			datastore = DatastoreServiceFactory.getDatastoreService();
		}
		
		return datastore;
	}
	
	private Approval entityToApproval(Entity e) {
		Approval app = new Approval();
		
		app.setAccountId((Long) e.getProperty("accountId"));
		app.setAmount(((Long) e.getProperty("amount")).intValue());
		app.setNomResponsable((String) e.getProperty("nom"));
		app.setReponseManuelle((String) e.getProperty("reponseManuelle"));
		
		return app;
	}
	
	private Entity approvalToEntity(Approval app) {
		Entity e = new Entity("Approval");
		
		e.setProperty("accountId", app.getAccountId());
		e.setProperty("amount", app.getAmount());
		e.setProperty("nom", app.getNomResponsable());
		e.setProperty("reponseManuelle", app.getReponseManuelle());
		
		return e;
	}
	
	private Response getNotFoundEntityResponse() {
		return Response.status(404).entity(new ValidationError("Not found", "", "", "")).encoding("utf-8").build();
	}
	
	public ApprovalResource() {
		
	}
	
	public ApprovalResource(UriInfo uriInfo, DatastoreService datastore) {
		this.uriInfo = uriInfo;
		this.datastore = datastore;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response listAllApproval() {
		Query q = new Query("Approval");
		PreparedQuery pq = getDataStore().prepare(q);
		
		HashMap<Long, Approval> results = new HashMap<>();
		
		for (Entity result : pq.asIterable()) {
			results.put(result.getKey().getId(), entityToApproval(result));
		}
		
		return Response.ok(results).encoding("utf-8").build();
	}

	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
	public Response getApproval(@PathParam("id") Long id) {
		Entity e = null;
		
		try {
			e = getDataStore().get(KeyFactory.createKey("Approval", id));
		} catch (EntityNotFoundException ex) {
			return getNotFoundEntityResponse();
		}
		
		return Response.ok(entityToApproval(e)).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
	public Response createAccount(@Valid Approval approval) {
		
		Key key = getDataStore().put(approvalToEntity(approval));
		
		StringBuffer uri = new StringBuffer(uriInfo.getAbsolutePath().toString());
		
		if (uri.lastIndexOf("/") != uri.length() - 1) {
			uri.append("/");
		}
		
		uri.append(key.getId());
		
		return Response.created(
				URI.create(uri.toString()))
				.encoding("utf-8")
				.build();
	}
	
	@PUT
	@Path("{id}")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
	public Response createAccount(@PathParam("id") Long id, @Valid Approval approval) {
		
		Entity e = null;
		
		try {
			e = getDataStore().get(KeyFactory.createKey("Approval", id));
		} catch (EntityNotFoundException ex) {
			return getNotFoundEntityResponse();
		}
		
		e.setProperty("accountId", approval.getAccountId());
		e.setProperty("amount", approval.getAmount());
		e.setProperty("nom", approval.getNomResponsable());
		e.setProperty("reponseManuelle", approval.getReponseManuelle());
		
		getDataStore().put(e);
		
		return Response.noContent().encoding("utf-8").build();
	}
	
	@DELETE
	@Path("{id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
	public Response deleteAccount(@PathParam("id") Long id) {
		try {
			getDataStore().delete(KeyFactory.createKey("Approval", id));
		} catch (Exception e) {
			return getNotFoundEntityResponse();
		}
		
		return Response.noContent().build();
	}
}
