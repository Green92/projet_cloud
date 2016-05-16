package fr.iut.loan.service;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.JerseyClientBuilder;

import fr.iut.loan.business.LoanRequest;
import fr.iut.loan.business.Risque;


@Path("loan")
public class LoanResource {
	
	public static final String CHECK_ACCOUNT_SERVICE_URI_TEMPLATE =
			"https://murmuring-hamlet-27164.herokuapp.com/checkaccount/risk/{id}";
	
	private Boolean checkAccount(Long accountId) {
		
		WebTarget target = new JerseyClientBuilder().build().
				target(CHECK_ACCOUNT_SERVICE_URI_TEMPLATE)
				.resolveTemplate("id", accountId);
		
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		
		if (response.getStatus() != 200) {
			throw new RuntimeException("Requested service unvailable.");
		}
		
		Risque risk = response.readEntity(Risque.class);
		
		return risk.getRisque().equals("low");
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response requestLoan(LoanRequest loanRequest) {
		
		return Response.status(Status.ACCEPTED)
				.entity(checkAccount(loanRequest.getAccountId()).toString()).build();
		
	}
	
}
