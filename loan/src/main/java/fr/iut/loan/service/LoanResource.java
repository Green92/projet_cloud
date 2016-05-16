package fr.iut.loan.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.JerseyClientBuilder;

import fr.iut.loan.business.LoanRequest;
import fr.iut.loan.business.Risque;


@Path("loan")
public class LoanResource {
	
	public static final long SEUIL = 10000;
	
	public static final String CHECK_ACCOUNT_SERVICE_URI_TEMPLATE =
			"https://murmuring-hamlet-27164.herokuapp.com/checkaccount/risk/{id}";
	
	public static final String APP_MANAGER_SERVICE_URI =
			"http://1.accmanager-1310.appspot.com/account";
	
	private Boolean isRiskyToLendTo(Long accountId) {
		
		WebTarget target = new JerseyClientBuilder().build().
				target(CHECK_ACCOUNT_SERVICE_URI_TEMPLATE)
				.resolveTemplate("id", accountId);
		
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		
		if (response.getStatus() != 200) {
			throw new RuntimeException("Requested service unvailable.");
		}
		
		Risque risk = response.readEntity(Risque.class);
		
		return risk.getRisque().equals("high");
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response requestLoan(LoanRequest loanRequest) {
		
		//TODO Terminer ce code.
		
		//Si Si le compte a deja une demande en cours 
			//Si elle est disponible
				//on revoi (return) la réponse.
			//Sinon
				//on previent (return) que c'est en attente.
		//Sinon 
			//Si le client est serieux
			if (!isRiskyToLendTo(loanRequest.getAccountId())) {
				//et si la somme demandee est en dessous du seuil fixe.
				if (loanRequest.getAmount() < SEUIL) {
					//On ouvre le robinet.
				}
			}
			
		//on enregistre une demande dans app manager.
		
		//TODO Enlever ce test.
		return Response.status(Status.ACCEPTED)
				.entity(isRiskyToLendTo(loanRequest.getAccountId()).toString()).build();
		
	}
	
}
