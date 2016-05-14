package com.badsheepcorp.accmanager.service.test;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.badsheepcorp.accmanager.business.Account;
import com.badsheepcorp.accmanager.service.AccountResource;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import static org.junit.Assert.*;

import java.net.URI;

public class AccountResourceTest {

	private final LocalServiceTestHelper helper =
		      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
	private AccountResource accRes;
	
	private Account createValidAccount() {
		Account acc = new Account();
		acc.setNom("Johny");
		acc.setPrenom("Haliday");
		acc.setSolde(1000000D);
		acc.setRisque("low");
		
		return acc;
	}
	
	@Before
	public void setUp() throws Exception {
		helper.setUp();
		
		UriInfo uriInfo = Mockito.mock(UriInfo.class);
		Mockito.when(uriInfo.getBaseUri()).thenReturn(new URI(""));
		
		accRes = new AccountResource(uriInfo);
	}
	
	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testListAllAccount() {
		Response resp = accRes.listAllAccount();
		
		assertEquals("Response should be a 200 HTTP response.", 200, resp.getStatus());
	}
	
	@Test 
	public void testGetUnexistingAccount() {
		Response resp = accRes.getAccount(-7010L);
		
		assertEquals("Response should be a 404 HTTP error response when trying to get an inexisting account.", 404, resp.getStatus());
	}
	
	@Test
	public void testCreateInvalidAccount() {
		Response resp = accRes.createAccount(new Account());
		
		assertEquals("Response should be a 422 HTTP error response when trying to create an invalide account.", 422, resp.getStatus());
	}
	
	@Test
	public void testUpdateUnexistingAccount() {
		Response resp = accRes.updateAccount(-7010L, createValidAccount());
		
		assertEquals("Response should be a 404 HTTP error response when trying to update an unexisting account.", 404, resp.getStatus());
	}
	
	@Test
	public void testCreateAccount() {
		Response resp = accRes.createAccount(createValidAccount());
		
		assertEquals("Response should be a 201 HTTP response when creating an account.", 201, resp.getStatus());
		assertNotNull("Response should contain a HTTP header field specifying the URI of the created account.", resp.getHeaderString("Location"));
	}
}
