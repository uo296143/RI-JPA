package uo.ri.cws.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import uo.ri.util.assertion.ArgumentChecks;
@Entity
@Table(name = "TCASHES")
public class Cash extends PaymentMean {

    Cash(){
        
    }
    
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

	public Client getClient() {
		return getClient();
	}

}
