package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Vehicle {
	
	/*
	 * Atributos naturales
	 */
	private String plateNumber;
	private String make;
	private String model;
	
	/*
	 * Atributos accidentales
	 */
	private Client client;
	private VehicleType vehicleType;
	private Set<WorkOrder> workOrders = new HashSet<WorkOrder>();
	
	protected void _setClient(Client client) {
		this.client = client;
	}

	public Set<WorkOrder> getWorkOrders() {
		return new HashSet<>(workOrders);
	}

	public String getPlateNumber() {
		return plateNumber;
	}
	
	public String getMake() {
		return make;
	}
	
	public String getModel() {
		return model;
	}

	public Vehicle(String plateNumber, String make, String model) {
		super();
		this.plateNumber = plateNumber;
		this.make = make;
		this.model = model;
	}

	@Override
	public int hashCode() {
		return Objects.hash(make, model, plateNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vehicle other = (Vehicle) obj;
		return Objects.equals(make, other.make)
				&& Objects.equals(model, other.model)
				&& Objects.equals(plateNumber, other.plateNumber);
	}

	@Override
	public String toString() {
		return "Vehicle [plateNumber=" + plateNumber + ", make=" + make
				+ ", model=" + model + "]";
	}

	public Client getClient() {
		return client;
	}

	void _setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	Set<WorkOrder> _getWorkOrders() {
		return workOrders;
	}
	
	

}
