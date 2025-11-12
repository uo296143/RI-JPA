package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TCONTRACTTYPES")
public class ContractType extends BaseEntity {

    // natural atributtes
    @Column(unique = true)
    private String name;
    private double compensationDaysPerYear;

    // accidental atributtes
    @OneToMany(mappedBy = "contractType")
    private Set<Contract> contracts = new HashSet<>();

    ContractType() {

    }

    public ContractType(String name, double compensationDaysPerYear) {
        this.name = name;
        this.compensationDaysPerYear = compensationDaysPerYear;
    }

    public String getName() {
        return name;
    }

    public double getCompensationDaysPerYear() {
        return compensationDaysPerYear;
    }

    public Set<Contract> getContracts() {
        return new HashSet<Contract>(contracts);
    }

    Set<Contract> _getContracts() {
        return contracts;
    }

    public void setCompensationDaysPerYear(double compensationDaysPerYear) {
        ArgumentChecks.isTrue(compensationDaysPerYear > 0);
        updatedNow();
        this.compensationDaysPerYear = compensationDaysPerYear;
    }

}
