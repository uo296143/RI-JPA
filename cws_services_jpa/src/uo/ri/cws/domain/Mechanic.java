package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Mechanic {
	// natural attributes
	private String nif;
	private String surname;
	private String name;

	// accidental attributes
	private Set<WorkOrder> assigned = new HashSet<>();
	private Set<Intervention> interventions = new HashSet<>();

	public Set<WorkOrder> getAssigned() {
		return new HashSet<>( assigned );
	}

	Set<WorkOrder> _getAssigned() {
		return Set.copyOf(assigned);
	}

	public Set<Intervention> getInterventions() {
		return new HashSet<>( interventions );
	}

	Set<Intervention> _getInterventions() {
		return new HashSet<Intervention>(interventions);
	}

	public Mechanic(String nif, String surname, String name) {
		super();
		this.nif = nif;
		this.surname = surname;
		this.name = name;
	}

	public String getNif() {
		return nif;
	}

	public String getSurname() {
		return surname;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nif);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mechanic other = (Mechanic) obj;
		return Objects.equals(nif, other.nif);
				
	}

	@Override
	public String toString() {
		return "Mechanic [nif=" + nif + ", surname=" + surname + ", name="
				+ name + ", assigned=" + assigned + ", interventions="
				+ interventions + "]";
	}

	public Set<WorkOrder> _getWorkOrders() {
		return assigned;
	}
	
	

}
