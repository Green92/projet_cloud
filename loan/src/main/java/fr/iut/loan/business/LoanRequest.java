package fr.iut.loan.business;

import javax.validation.constraints.NotNull;

public class LoanRequest {
	
	@NotNull
	private Long accountId;
	
	private Integer amount;

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
}
