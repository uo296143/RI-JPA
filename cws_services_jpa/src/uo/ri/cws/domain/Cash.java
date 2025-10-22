package uo.ri.cws.domain;

import uo.ri.util.assertion.ArgumentChecks;

public class Cash extends PaymentMean {

	
	/**
	 * A cash can always pay
	 */
	@Override
	public boolean canPay(Double amount) {
		return true;
	}

	public Cash(Client client) {
		ArgumentChecks.isNotNull(client, "invalid null client");
		Associations.Holds.link(this, client);
	}


}
