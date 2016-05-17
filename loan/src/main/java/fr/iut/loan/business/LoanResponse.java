package fr.iut.loan.business;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

public class LoanResponse {
	
	@NotNull
	private String reponseManuelle;
	
	public LoanResponse() {
		
	}
	
	public LoanResponse(String reponseManuelle) {
		this.reponseManuelle = reponseManuelle;
	}
	
	public String getReponseManuelle() {
		return reponseManuelle;
	}

	public void setReponseManuelle(String responseManuelle) {
		this.reponseManuelle = responseManuelle;
	}
	
	@XmlTransient
	@AssertTrue(message="Property reponseManuelle only accepts null, \"accepted\" and \"refused\" values.")
	public boolean isReponseManuelleCorrecte() {
		return reponseManuelle == null ? true :
				reponseManuelle.equals("accepted") || 
				reponseManuelle.equals("refused") || 
				reponseManuelle.equals("pending");
	}
}
