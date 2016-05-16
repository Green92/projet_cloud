package fr.iut.loan.business;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Risque {
	
	private String risque;

	public String getRisque() {
		return risque;
	}

	public void setRisque(String risque) {
		this.risque = risque;
	}

}
