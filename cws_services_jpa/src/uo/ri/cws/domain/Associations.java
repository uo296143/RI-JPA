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
            mean._setClient(client);
            client._getPaymentMeans().add(mean);
        }

        public static void unlink(Client client, PaymentMean mean) {
            client._getPaymentMeans().remove(mean);
            mean._setClient(null);
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
            invoice._getWorkOrders().add(workOrder);
            workOrder._setInvoice(invoice);
        }

        public static void unlink(Invoice invoice, WorkOrder workOrder) {
            invoice._getWorkOrders().remove(workOrder);
            workOrder._setInvoice(null);
        }
    }

    public static class Settles {

        public static void link(Invoice invoice, Charge cargo, PaymentMean mp) {
            invoice._getCharges().add(cargo);
            cargo._setPaymentMean(mp);
            cargo._setInvoice(invoice);
            mp._getCharges().add(cargo);
        }

        public static void unlink(Charge cargo) {
            cargo.getInvoice()._getCharges().remove(cargo);
            cargo.getPaymentMean()._getCharges().remove(cargo);
            cargo._setPaymentMean(null);
            cargo._setInvoice(null);
        }
    }

    public static class Assigns {

        public static void link(Mechanic mechanic, WorkOrder workOrder) {
            workOrder._setMechanic(mechanic);
            mechanic._getAssigned().add(workOrder);
        }

        public static void unlink(Mechanic mechanic, WorkOrder workOrder) {
            mechanic._getAssigned().remove(workOrder);
            workOrder._setMechanic(null);
        }
    }

    public static class Intervenes {

        public static void link(WorkOrder workOrder, Intervention intervention,
                Mechanic mechanic) {
            intervention._setWorkOrder(workOrder);
            workOrder._getInterventions().add(intervention);
            intervention._setMechanic(mechanic);
            mechanic._getInterventions().add(intervention);
                 
        }

        public static void unlink(Intervention intervention) {
            intervention.getMechanic()._getInterventions().remove(intervention);
            intervention._setMechanic(null);
            intervention.getWorkOrder()
            ._getInterventions()
            .remove(intervention);
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
            substitution.getSparePart()
                ._getSubstitutions()
                .remove(substitution);
            substitution.getIntervention()
                ._getSubstitutions()
                .remove(substitution);
            substitution._setIntervention(null);
            substitution._setSparePart(null);
        }
    }

    public static class Binds {

        static void link(Mechanic mechanic, Contract contract) {
            mechanic._getContracts().add(contract);
            contract._setMechanic(mechanic);
        }

        public static void unlink(Contract contract) {
            contract.getMechanic()._getContracts().remove(contract);
            contract._setMechanic(null);
        }
    }

    public static class Categorizes {

        static void link(ProfessionalGroup professionalGroup,
                Contract contract) {
            professionalGroup._getContracts().add(contract);
            contract._setProfessionalGroup(professionalGroup);
        }

        public static void unlink(Contract contract) {
            contract.getProfessionalGroup()._getContracts().remove(contract);
            contract._setProfessionalGroup(null);
        }
    }

    public static class Defines {

        static void link(ContractType contractType, Contract contract) {
            contractType._getContracts().add(contract);
            contract._setContractType(contractType);
        }

        public static void unlink(Contract contract) {
            contract.getContractType()._getContracts().remove(contract);
            contract._setContractType(null);
        }
    }

    public static class Generates {

        static void link(Payroll payroll, Contract contract) {
            payroll._setContract(contract);
            contract._getPayrolls().add(payroll);
        }

        public static void unlink(Payroll payroll) {
            payroll.getContract()._getPayrolls().remove(payroll);
            payroll._setContract(null);
        }
    }

}
