package uo.ri.cws.domain;

import java.util.Objects;

import uo.ri.util.assertion.ArgumentChecks;

public class Substitution {
	// natural attributes
	private int quantity;

	// accidental attributes
	private SparePart sparePart;
	private Intervention intervention;



	public Substitution(SparePart sparePart, Intervention intervention,
			int cantidad) {
		ArgumentChecks.isNotNull(intervention, "INvalid null intervention");
		ArgumentChecks.isNotNull(sparePart, "Invalid null spare part");
		ArgumentChecks.isTrue(cantidad >= 0, "Invalid negative quantity");
		this.intervention =intervention;
		this.quantity = cantidad;
		this.sparePart = sparePart;
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
	public int hashCode() {
		return Objects.hash(intervention, sparePart);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Substitution other = (Substitution) obj;
		return Objects.equals(intervention, other.intervention)
				&& Objects.equals(sparePart, other.sparePart);
	}

	@Override
	public String toString() {
		return "Substitution [quantity=" + quantity + ", sparePart=" + sparePart
				+ ", intervention=" + intervention + "]";
	}
	
	

}
