package fr.iut.loan.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.JerseyClientBuilder;

import exception.ServiceErrorException;
import fr.iut.loan.business.Account;
import fr.iut.loan.business.Approval;
import fr.iut.loan.business.LoanRequest;
import fr.iut.loan.business.LoanResponse;
import fr.iut.loan.business.Risque;


@Path("loan")
public class LoanResource {
	
	public static final long SEUIL = 10000;
	
	public static final String CHECK_ACCOUNT_SERVICE_URI_TEMPLATE =
			"https://checkaccount.herokuapp.com/checkaccount/risk/{id}";
	
	public static final String ACC_MANAGER_SERVICE_URI =
			"http://1.accmanager-1310.appspot.com/account";
	
	public static final String APP_MANAGER_SERVICE_URI =
			"http://1.appmanager-1311.appspot.com/approval";
	
	public static final String APP_MANAGER_SERVICE_URI_TEMPLATE =
			APP_MANAGER_SERVICE_URI + "/{id}";
	
	public static final String CHECK_ACCOUNT = "checkAccount";
	public static final String ACCOUNT_MANAGER = "accountManager";
	public static final String APPROVAL_MANAGER = "approvalManager";
	
	private Boolean isRiskyToLendTo(Long accountId) throws ServiceErrorException {
		
		WebTarget target = new JerseyClientBuilder().build().
				target(CHECK_ACCOUNT_SERVICE_URI_TEMPLATE)
				.resolveTemplate("id", accountId);
		
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		
		if (response.getStatus() != 200) {
			throw new ServiceErrorException(CHECK_ACCOUNT, response);
		}
		
		Risque risk = response.readEntity(Risque.class);
		
		return risk.getRisque().equals("high");
	}
	
	private void creditAccount(Long accountId, int amount) throws ServiceErrorException {
		WebTarget target = new JerseyClientBuilder().build().target(String.format("%s/%s", ACC_MANAGER_SERVICE_URI, accountId));
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		
		if (response.getStatus() != 200) {
			throw new ServiceErrorException(ACCOUNT_MANAGER, response);
		}
		
		Account account = response.readEntity(Account.class);
		account.setSolde(account.getSolde() + amount);
		
		response = target.request(MediaType.APPLICATION_JSON).put(Entity.json(account));
		
		if (response.getStatus() != 204) {
			throw new ServiceErrorException(ACCOUNT_MANAGER, response);
		}
	}
	
	private void creerApproval(LoanRequest lr) throws ServiceErrorException {
		Approval app = new Approval();
		app.setAccountId(lr.getAccountId());
		app.setNomResponsable(null);
		app.setReponseManuelle("pending");
		app.setAmount(lr.getAmount());
		
		WebTarget target = new JerseyClientBuilder().build().
				target(APP_MANAGER_SERVICE_URI);
		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(app));
		
		if (response.getStatus() != 201) {
			throw new ServiceErrorException(APPROVAL_MANAGER, response);
		}
	}
	
	private Approval getApproval(Long accountId) throws ServiceErrorException {
		WebTarget target = new JerseyClientBuilder().build().
				target(APP_MANAGER_SERVICE_URI_TEMPLATE)
				.resolveTemplate("id", accountId);
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		
		if (response.getStatus() == 404) {
			return null;
		}
		
		if (response.getStatus() != 200) {
			throw new ServiceErrorException(APPROVAL_MANAGER, response);
		}
		
		Approval approval = response.readEntity(Approval.class);
		
		return approval;
	}
	
	private void deleteApproval(Long accountId) throws ServiceErrorException {
		WebTarget target = new JerseyClientBuilder().build().
				target(APP_MANAGER_SERVICE_URI_TEMPLATE)
				.resolveTemplate("id", accountId);
		Response response = target.request(MediaType.APPLICATION_JSON).delete();
		
		if (response.getStatus() != 204) {
			throw new ServiceErrorException(APPROVAL_MANAGER, response);
		}
	}
	
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response requestLoan(LoanRequest loanRequest) {
		LoanResponse response = new LoanResponse();
		
		try {
		
			Approval approval = getApproval(loanRequest.getAccountId());
			
			if (approval != null) {
				if (approval.getReponseManuelle().equals("pending")) {
					return Response.status(Status.ACCEPTED)
							.entity(new LoanResponse(approval.getReponseManuelle()))
							.build();
				} else {
					if (approval.getReponseManuelle().equals("accepted")) {
						creditAccount(loanRequest.getAccountId(), approval.getAmount());
					}
					deleteApproval(approval.getAccountId());
					return Response.status(Status.ACCEPTED)
							.entity(new LoanResponse(approval.getReponseManuelle()))
							.build();
				}
			}
			
			if (loanRequest.getAmount() == null) {
				return Response.status(Status.BAD_REQUEST).build();
			}
			
			if (loanRequest.getAmount() < SEUIL) {
				if (!isRiskyToLendTo(loanRequest.getAccountId())) {
					response.setReponseManuelle("approved");
					creditAccount(loanRequest.getAccountId(), loanRequest.getAmount());
					return Response.status(Status.ACCEPTED)
							.entity(new LoanResponse("accepted"))
							.build();
				}
			}
				
			creerApproval(loanRequest);
			
			response.setReponseManuelle("pending");
			
			return Response.status(Status.ACCEPTED)
					.entity(response).build();
			
		} catch (ServiceErrorException e) {
			return e.getResponse();
		}
	}
	
}
