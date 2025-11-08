package uo.ri.cws.domain;

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

    public Set<Contract> getContracts() {
        return new HashSet<Contract>(contracts);
    }

    public Set<Contract> _getContracts() {
        return contracts;
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
        return new HashSet<Intervention>(interventions);
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

    public Set<WorkOrder> _getWorkOrders() {
        return assigned;
    }

    public void setName(String name) {
        ArgumentChecks.isNotBlank(name, "Invalid null or blank name");
        updatedNow();
        this.name = name;
    }

    public void setSurname(String surName) {
        ArgumentChecks.isNotBlank(surName, "Invalid null or blank surname");
        updatedNow();
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

}
