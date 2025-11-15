package uo.ri.cws.domain;

import java.time.Month;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.Contract.ContractState;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TMECHANICS")
public class Mechanic extends BaseEntity {
    @Column(unique = true)
    private String nif;
    private String surname;
    private String name;

    @OneToMany(mappedBy = "mechanic")
    private Set<WorkOrder> assigned = new HashSet<>();
    @OneToMany(mappedBy = "mechanic")
    private Set<Intervention> interventions = new HashSet<>();
    @OneToMany(mappedBy = "mechanic")
    private Set<Contract> contracts = new HashSet<>();

    Mechanic() {

    }

    public Mechanic(String nif, String name, String surname) {
        super();
        this.nif = nif;
        this.name = name;
        this.surname = surname;
    }

    public Mechanic(String nif) {
        this.nif = nif;
    }

    public Set<Contract> getContracts() {
        return new HashSet<Contract>(contracts);
    }

    Set<Contract> _getContracts() {
        return contracts;
    }

    public Set<WorkOrder> getAssigned() {
        return new HashSet<>(assigned);
    }

    Set<WorkOrder> _getAssigned() {
        return assigned;
    }

    public Set<Intervention> getInterventions() {
        return new HashSet<>(interventions);
    }

    Set<Intervention> _getInterventions() {
        return interventions;
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
    public String toString() {
        return "Mechanic [nif=" + nif + ", surname=" + surname + ", name="
                + name + ", assigned=" + assigned + ", interventions="
                + interventions + "]";
    }

    public void setName(String name) {
        ArgumentChecks.isNotBlank(name, "Invalid null or blank name");
        this.name = name;
    }

    public void setSurname(String surName) {
        ArgumentChecks.isNotBlank(surName, "Invalid null or blank surname");
        this.surname = surName;
    }

    public Optional<Contract> getContractInForce() {
        Optional<Contract> contract = Optional.empty();
        for (Contract c : contracts) {
            if (c.getState().equals(ContractState.IN_FORCE)) {
                contract = Optional.of(c);
            }
        }
        return contract;
    }

    /*
     * Used for calculate payroll`s productivity bonus. Sums the total amounts
     * of unique WorkOrders repaired by the mechanic, opened in the target
     * month, and already invoiced.
     */
    public double getSumOfWorkOrdersAlreadyInvoiced(Month month) {
        double total_amount = 0.0;

        Set<WorkOrder> uniqueWorkOrders = new HashSet<>();

        for (Intervention intervention : interventions) {

            WorkOrder workOrder = intervention.getWorkOrder();

            if (workOrder.getDate().getMonth().equals(month)
                    && workOrder.isInvoiced()) {

                uniqueWorkOrders.add(workOrder);
            }
        }

        for (WorkOrder workOrder : uniqueWorkOrders) {
            total_amount += workOrder.getAmount();
        }

        return total_amount;
    }
}
