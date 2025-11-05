package uo.ri.cws.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TSUBSTITUTIONS",
        uniqueConstraints= {
                @UniqueConstraint(columnNames= {"sparePart_id", "intervention_id"})
        })
public class Substitution extends BaseEntity{
	// natural attributes
	private int quantity;

	// accidental attributes
	@ManyToOne
	private SparePart sparePart;
	@ManyToOne
	private Intervention intervention;

	Substitution(){
	    
	}

	public Substitution(SparePart sparePart, Intervention intervention,
			int cantidad) {
		ArgumentChecks.isNotNull(intervention, "INvalid null intervention");
		ArgumentChecks.isNotNull(sparePart, "Invalid null spare part");
		ArgumentChecks.isTrue(cantidad >= 0, "Invalid negative quantity");
		this.intervention =intervention;
		this.quantity = cantidad;
		this.sparePart = sparePart;
		Associations.Substitutes.link(sparePart, this, intervention);
		sparePart._getSubstitutions().add(this);
	}

	public int getQuantity() {
		return quantity;
	}

	public SparePart getSparePart() {
		return sparePart;
	}

	public Intervention getIntervention() {
		return intervention;
	}

	void _setSparePart(SparePart sparePart) {
		this.sparePart = sparePart;
	}

	void _setIntervention(Intervention intervention) {
		this.intervention = intervention;
	}

	@Override
	public String toString() {
		return "Substitution [quantity=" + quantity + ", sparePart=" + sparePart
				+ ", intervention=" + intervention + "]";
	}

	public double getAmount() {
		return quantity * sparePart.getPrice();
	}
	
	

}
