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
	private Integer amount;
	
	@NotNull
	@NotEmpty
	private String nomResponsable;
	
	@NotNull
	private String reponseManuelle;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getNomResponsable() {
		return nomResponsable;
	}

	public void setNomResponsable(String nom) {
		this.nomResponsable = nom;
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
