package uo.ri.cws.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;

@Entity
@Table(name = "TCONTRACTS", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "contractType_id", "mechanic_id",
                "professionalGroup_id" }) })

public class Contract extends BaseEntity {
    public enum ContractState {
        IN_FORCE, TERMINATED
    }

    // natural attributes
    private double annualBaseSalary;
    private double settlement;
    @Enumerated(EnumType.STRING)

    private ContractState state;
    private double taxRate;

    // accidental attributes
    @ManyToOne
    private ContractType contractType;
    @ManyToOne
    private Mechanic mechanic;
    @ManyToOne
    private ProfessionalGroup professionalGroup;

    Contract() {

    }

    public double getAnnualBaseSalary() {
        return annualBaseSalary;
    }

    public double getSettlement() {
        return settlement;
    }

    public String getState() {
        return state;
    }

    public double getTaxRate() {
        return taxRate;
    }

    void _setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    void _setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    void _setProfessionalGroup(ProfessionalGroup pg) {
        this.professionalGroup = pg;
    }

}
