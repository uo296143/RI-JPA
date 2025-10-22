package uo.ri.cws.domain;

public class Associations {

	public static class Owns {

		public static void link(Client client, Vehicle vehicle) {
			vehicle._setClient(client);
			client._getVehicles().add(vehicle);
		}

		public static void unlink(Client cliente, Vehicle vehicle) {
			cliente._getVehicles().remove(vehicle);
			vehicle._setClient(null);
		}

	}

	public static class Classifies {

		public static void link(VehicleType vehicleType, Vehicle vehicle) {
			vehicle._setVehicleType(vehicleType);
			vehicleType._getVehicles().add(vehicle);
		}

		public static void unlink(VehicleType tipoVehicle, Vehicle vehicle) {
			tipoVehicle._getVehicles().remove(vehicle);
			vehicle._setVehicleType(null);
		}
	}

	public static class Holds {

		public static void link(PaymentMean mean, Client client) {
		}

		public static void unlink(Client client, PaymentMean mean) {
		}
	}

	public static class Fixes {

		public static void link(Vehicle vehicle, WorkOrder workOrder) {
			workOrder._setVehicle(vehicle);
			vehicle._getWorkOrders().add(workOrder);
		}

		public static void unlink(Vehicle vehicle, WorkOrder workOrder) {
			vehicle._getWorkOrders().remove(workOrder);
			workOrder._setVehicle(null);
		}
	}

	public static class Bills {

		public static void link(Invoice invoice, WorkOrder workOrder) {
		}

		public static void unlink(Invoice invoice, WorkOrder workOrder) {
		}
	}

	public static class Settles {

		public static void link(Invoice invoice, Charge cargo, PaymentMean mp) {
		}

		public static void unlink(Charge cargo) {
		}
	}

	public static class Assigns {

		public static void link(Mechanic mechanic, WorkOrder workOrder) {
			workOrder._setMechanic(mechanic);
			mechanic._getWorkOrders().add(workOrder);
		}

		public static void unlink(Mechanic mechanic, WorkOrder workOrder) {
			mechanic._getWorkOrders().remove(workOrder);
			workOrder._setMechanic(null);
		}
	}

	public static class Intervenes {

		public static void link(WorkOrder workOrder,
				Intervention intervention, Mechanic mechanic) {
			intervention._setWorkOrder(workOrder);
			intervention._setMechanic(mechanic);
			mechanic._getInterventions().add(intervention);
			workOrder._getInterventions().add(intervention);
		}

		public static void unlink(Intervention intervention) {
			intervention.getMechanic()._getInterventions().remove(intervention);
			intervention.getWorkOrder()._getInterventions().remove(intervention);
			intervention._setMechanic(null);
			intervention._setWorkOrder(null);
		}
	}

	public static class Substitutes {

		static void link(SparePart sparePart, Substitution substitution,
				Intervention intervention) {
			substitution._setIntervention(intervention);
			substitution._setSparePart(sparePart);
			sparePart._getSubstitutions().add(substitution);
			intervention._getSubstitutions().add(substitution);
		}

		public static void unlink(Substitution substitution) {
			substitution.getSparePart()._getSubstitutions().remove(substitution);
			substitution.getIntervention().getSubstitutions().remove(substitution);
			
		}
	}

}
