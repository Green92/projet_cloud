package exception;
import javax.ws.rs.core.Response;

public class ServiceErrorException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 54564564654564L;
	
	private Response response;
	
	public ServiceErrorException(String serviceName, Response response) {
		this.response = response;
		this.response.getHeaders().add("x-service-name", serviceName);
	}
	
	public Response getResponse() {
		return this.response;
	}
}
