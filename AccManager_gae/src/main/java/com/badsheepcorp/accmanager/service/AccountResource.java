package com.badsheepcorp.accmanager.service;

import java.net.URI;
import java.util.HashMap;

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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.badsheepcorp.accmanager.business.Account;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

@Path("account")
public class AccountResource {
	
	@Context
	private UriInfo uriInfo;
	
	private DatastoreService datastore = null;
	
	private DatastoreService getDataStore() {
		if (datastore == null) {
			datastore = DatastoreServiceFactory.getDatastoreService();
		}
		
		return datastore;
	}
	
	private Account entityToAccount(Entity e) throws NumberFormatException {
		Account account = new Account();
		
		account.setNom((String) e.getProperty("nom"));
		account.setPrenom((String)e.getProperty("prenom"));
		account.setRisque((String) e.getProperty("risque"));
		account.setSolde((Double)e.getProperty("solde"));
		
		return account;
	}
	
	private Entity accountToEntity(Account a) throws NumberFormatException {
		Entity entity = new Entity("Account");
		
		entity.setProperty("nom", a.getNom());
		entity.setProperty("prenom", a.getPrenom());
		entity.setProperty("risque", a.getRisque());
		entity.setProperty("solde", a.getSolde());
		
		return entity;
	}
	
	private boolean isNullOrEmpty(String property) {
		if (property == null) {
			return true;
		}
		
		if (property.trim().isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	private boolean isValidAccount(Account account) {
		if (isNullOrEmpty(account.getNom())) {
			return false;
		}
		
		if (isNullOrEmpty(account.getPrenom())) {
			return false;
		}
		
		if (account.getSolde() == null) {
			return false;
		}
		
		if (account.getRisque() == null) {
			return false;
		}
		
		return account.getRisque().equals("high") || account.getRisque().equals("low");
	}
	
	private Response getNotFoundEntityResponse() {
		return Response.status(404).entity("NOT FOUND").encoding("utf-8").build();
	}
	
	private Response getUnprocessableEntityResponse() {
		return Response.status(Status.BAD_REQUEST)
                .entity("Invalid data supplied for request")
                .encoding("utf-8")
                .build();
	}
	
	public AccountResource() {
		// TODO Auto-generated constructor stub
	}
	
	public AccountResource(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public Response listAllAccount() {
		Query q = new Query("Account");
		PreparedQuery pq = getDataStore().prepare(q);
		
		HashMap<Long, Account> results = new HashMap<>();
		
		for (Entity result : pq.asIterable()) {
			results.put(result.getKey().getId(), entityToAccount(result));
		}
		
		return Response.ok(results).encoding("utf-8").build();
    }
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
    public Response getAccount(@PathParam("id") Long id) {
		Entity e = null;
		
		try {
			e = getDataStore().get(KeyFactory.createKey("Account", id));
		} catch (EntityNotFoundException ex) {
			return getNotFoundEntityResponse();
		}
		
		return Response.ok(entityToAccount(e)).build();
    }
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createAccount(Account account) {
		
		if (!isValidAccount(account)) {
			return getUnprocessableEntityResponse();
		}
		
		Key key = getDataStore().put(accountToEntity(account));
		
		return Response.created(
				URI.create(
						String.format("%s/%s", uriInfo.getAbsolutePath(), key.getId())))
				.encoding("utf-8")
				.build();
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateAccount(@PathParam("id") Long id, Account account) {
		Entity e = null;
		
		try {
			e = getDataStore().get(KeyFactory.createKey("Account", id));
		} catch (EntityNotFoundException ex) {
			return getNotFoundEntityResponse();
		}
		
		if (account.getNom() != null) {
			if (account.getNom().trim().isEmpty()) {
				return getUnprocessableEntityResponse();
			}
			e.setProperty("nom", account.getNom());
		}
		
		if (account.getPrenom() != null) {
			if (account.getPrenom().trim().isEmpty()) {
				return getUnprocessableEntityResponse();
			}
			e.setProperty("prenom", account.getPrenom());
		}
		
		if (account.getSolde() != null) {
			e.setProperty("solde", account.getSolde());
		}
		
		if (account.getRisque() != null) {
			if (!account.getRisque().equals("high") && !account.getRisque().equals("low")) {
				return getUnprocessableEntityResponse();
			}
			e.setProperty("risque", account.getRisque());
		}
		
		getDataStore().put(e);
		
		return Response.noContent().encoding("utf-8").build();
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteAccount(@PathParam("id") Long id) {
		try {
			getDataStore().delete(KeyFactory.createKey("Account", id));
		} catch (Exception e) {
			return getNotFoundEntityResponse();
		}
		
		return Response.noContent().build();
	}
}
