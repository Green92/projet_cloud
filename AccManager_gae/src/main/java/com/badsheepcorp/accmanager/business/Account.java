package com.badsheepcorp.accmanager.business;

public class Account {

	private String nom;
	private String prenom;
	private String risque;
	private Double solde;
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getPrenom() {
		return prenom;
	}
	
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	public String getRisque() {
		return risque;
	}
	
	public void setRisque(String risque) {
		this.risque = risque;
	}
	
	public Double getSolde() {
		return solde;
	}
	
	public void setSolde(Double solde) {
		this.solde = solde;
	}
	
}
