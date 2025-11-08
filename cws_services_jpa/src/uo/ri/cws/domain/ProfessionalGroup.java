package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;

@Entity
@Table(name = "TPROFESSIONALGROUPS")
public class ProfessionalGroup extends BaseEntity {
    // natural attributes
    @Column(unique = true)
    private String name;
    private double productivityRate;
    private double trienniumPayment;

    // accidental attributes
    @OneToMany(mappedBy = "professionalGroup")
    private Set<Contract> contracts = new HashSet<>();

    ProfessionalGroup() {

    }

    public ProfessionalGroup(String name, double trienniumSalary,
            double productivityPlus) {
        this.name = name;
        this.trienniumPayment = trienniumSalary;
        this.productivityRate = productivityPlus;
    }

    public String getName() {
        return name;
    }

    public double getProductivityRate() {
        return productivityRate;
    }

    public double getTrienniumPayment() {
        return trienniumPayment;
    }

    public Set<Contract> getContracts() {
        return contracts;
    }

}
