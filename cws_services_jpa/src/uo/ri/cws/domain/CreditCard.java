package uo.ri.cws.domain;

import java.time.LocalDate;
import java.util.Objects;

public class CreditCard extends PaymentMean {
	private String number;
	private String type;
	private LocalDate validThru;
	
	
	/**
	 * A credit card can pay if is not outdated 
	 */
	@Override
	public boolean canPay(Double amount) {
		// TODO Auto-generated method stub
		return false;
	}


	public CreditCard(String number, String type, LocalDate validThru) {
		super();
		this.number = number;
		this.type = type;
		this.validThru = validThru;
	}


	public String getNumber() {
		return number;
	}


	public String getType() {
		return type;
	}


	public LocalDate getValidThru() {
		return validThru;
	}


	@Override
	public int hashCode() {
		return Objects.hash(number);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreditCard other = (CreditCard) obj;
		return Objects.equals(number, other.number);
	}


	@Override
	public String toString() {
		return "CreditCard [number=" + number + ", type=" + type
				+ ", validThru=" + validThru + "]";
	}
	
	

}
