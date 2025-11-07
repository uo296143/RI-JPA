package uo.ri.cws.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.date.Dates;

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
    private LocalDate startDate;
    private LocalDate endDate;

    // accidental attributes
    @ManyToOne
    private ContractType contractType;
    @ManyToOne
    private Mechanic mechanic;
    @ManyToOne
    private ProfessionalGroup professionalGroup;
    @OneToMany(mappedBy = "contract")
    private Set<Payroll> payrolls = new HashSet<Payroll>();

    // Para calcular el porcentaje de IRPF según el salario.
    private static double segments[] = { 12450, 20200, 35200, 60000, 300000 };
    private static double rates[] = { 0.19, 0.24, 0.30, 0.37, 0.45, 0.47 };

    Contract() {

    }

    public Contract(Mechanic mechanic, ContractType contractType,
            ProfessionalGroup professionalGroup, double annualBaseSalary) {
        this.mechanic = mechanic;
        this.annualBaseSalary = annualBaseSalary;
        this.contractType = contractType;
        this.professionalGroup = professionalGroup;
        // Default Values
        this.state = ContractState.IN_FORCE;
        this.startDate = Dates.firstDayOfNextMonth();
        this.taxRate = forSalary(annualBaseSalary);
    }

    public Contract(Mechanic mechanic, ContractType contractType,
            ProfessionalGroup professionalGroup, double annualBaseSalary,
            LocalDate endDate) {
        this.mechanic = mechanic;
        this.annualBaseSalary = annualBaseSalary;
        this.contractType = contractType;
        this.professionalGroup = professionalGroup;
        this.endDate = endDate;
        // Default Values
        this.state = ContractState.IN_FORCE;
        this.startDate = Dates.firstDayOfNextMonth();
        this.taxRate = forSalary(annualBaseSalary);
    }

    private double forSalary(double annualSalary) {
        for (int i = 0; i < segments.length; i++) {
            if (annualSalary < segments[i]) {
                return rates[i];
            }
        }
        return rates[rates.length - 1];
    }

    public Set<Payroll> getPayrolls() {
        return new HashSet<Payroll>(payrolls);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public ProfessionalGroup getProfessionalGroup() {
        return professionalGroup;
    }

    public double getAnnualBaseSalary() {
        return annualBaseSalary;
    }

    public double getSettlement() {
        return settlement;
    }

    public ContractState getState() {
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
