package fr.iut.checkaccount;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

@Path("/checkaccount")
public class CheckAccount {
	
	private final static String ACC_MANAGER_URL = "http://1.accmanager-1310.appspot.com/account";

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Path("risk/{account}")
    @Produces(MediaType.TEXT_PLAIN)
    public String risk(@PathParam("account") String account) {
    	Client c = ClientBuilder.newClient();
    	WebTarget target = c.target(ACC_MANAGER_URL);
    	
    	String responseMsg = target.path("").request().get(String.class);
    	
    	
    	
        return responseMsg;
    }
}
