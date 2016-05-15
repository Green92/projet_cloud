package com.badsheepcorp.appmanager.business;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.NotEmpty;

@XmlRootElement
public class Approval {
	
	@NotNull
	private Long accountId;
	
	@NotNull
	@NotEmpty
	private String nom;
	
	private String reponseManuelle;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
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
				reponseManuelle.equals("accepted") || reponseManuelle.equals("refused");
	}
}
