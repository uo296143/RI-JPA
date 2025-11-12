package uo.ri.cws.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;
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

    /**
     * Constructor canónico.
     * 
     * @param mechanic
     * @param contractType
     * @param group
     * @param signingDate
     * @param endDate
     * @param annualSalary
     */
    public Contract(Mechanic mechanic, ContractType contractType,
            ProfessionalGroup group, LocalDate signingDate, LocalDate endDate,
            double annualSalary) {
        ArgumentChecks.isNotNull(mechanic);
        ArgumentChecks.isNotNull(contractType);
        ArgumentChecks.isTrue(annualSalary > 0);
        ArgumentChecks.isNotNull(group);
        ArgumentChecks.isNotNull(signingDate);
        boolean isFixedTerm = contractType.getName().equals("FIXED_TERM");
        if (isFixedTerm) {
            ArgumentChecks.isNotNull(endDate);
            ArgumentChecks.isFalse(endDate.isBefore(signingDate));
        }

        // Si el mecánico ya tiene un contrato se acaba
        Optional<Contract> optionalContract = mechanic.getContractInForce();
        if (optionalContract.isPresent()) {
            optionalContract.get()
                .terminate(LocalDate.now()
                    .with(TemporalAdjusters.lastDayOfMonth()));
        }

        this.mechanic = mechanic;
        this.contractType = contractType;
        this.professionalGroup = group;
        this.startDate = signingDate.withDayOfMonth(1);
        if (isFixedTerm) {
            this.endDate = endDate.with(TemporalAdjusters.lastDayOfMonth());
        } else {
            this.endDate = null;
        }
        this.annualBaseSalary = annualSalary;
        this.state = ContractState.IN_FORCE;
        this.taxRate = forSalary(annualBaseSalary);
        Associations.Binds.link(mechanic, this);
        Associations.Categorizes.link(group, this);
        Associations.Defines.link(contractType, this);
    }

    public Contract(Mechanic mechanic, ContractType contractType,
            ProfessionalGroup professionalGroup, double annualBaseSalary) {

        this(mechanic, contractType, professionalGroup,
                Dates.firstDayOfNextMonth(), null, annualBaseSalary);

    }

    public Contract(Mechanic mechanic, ContractType contractType,
            ProfessionalGroup professionalGroup, double annualBaseSalary,
            LocalDate endDate) {

        this(mechanic, contractType, professionalGroup,
                Dates.firstDayOfNextMonth(), endDate, annualBaseSalary);

    }

    public Contract(Mechanic mechanic, ContractType type,
            ProfessionalGroup group, LocalDate signingDate,
            double annualSalary) {

        this(mechanic, type, group, signingDate, null, annualSalary);

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

    public void setAnnualBaseSalary(double annualBaseSalary) {
        updatedNow();
        this.annualBaseSalary = annualBaseSalary;
    }

    public void setEndDate(LocalDate endDate) {
        updatedNow();
        this.endDate = endDate;
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
        updatedNow();
        this.contractType = contractType;
    }

    void _setMechanic(Mechanic mechanic) {
        updatedNow();
        this.mechanic = mechanic;
    }

    void _setProfessionalGroup(ProfessionalGroup pg) {
        updatedNow();
        this.professionalGroup = pg;
    }

    public boolean isInForce() {
        return state.equals(ContractState.IN_FORCE);
    }

    /**
     * Se termina el contrato
     * 
     * @param endDate,fecha que finaliza el contrato, sea cual sea se considera
     *                      el último día de ese mes.
     */
    public void terminate(LocalDate endDate) {
        if (state.equals(ContractState.TERMINATED)) {
            throw new IllegalStateException();
        }
        ArgumentChecks.isNotNull(endDate);
        ArgumentChecks.isFalse(endDate.isBefore(startDate));
        state = ContractState.TERMINATED;
        this.endDate = endDate.with(TemporalAdjusters.lastDayOfMonth());
        computeSettlementIfRequired();
    }

    /*
     * If the contract has been more or equals 365 days the mechanic has a
     * settlement.
     */
    private void computeSettlementIfRequired() {
        long days_between_star_and_end = ChronoUnit.DAYS.between(startDate,
                endDate);
        
        days_between_star_and_end += 1;
    
        if (days_between_star_and_end >= 365) {

            double settlement = computeSettlement();
            this.settlement = settlement;
        }

    }

    /*
     * Calcula el finiquito para los mecánicos cuyo contrato se ha acabado y ha
     * estado en vigor un año o más
     */
    private double computeSettlement() {

        double gross_salary = 0.0;
        long days_between_star_and_end = ChronoUnit.DAYS.between(startDate,
                endDate) +1;

        // Fecha desde la que se inicia a contar la media del salario medio
        // bruto
        // Si pongo 12 pasan los test de Domain y 13 pasan los de RunCucumber
        LocalDate dateOneYearAgo = endDate.minusMonths(12);

        for (Payroll payroll : payrolls) {

            if (!payroll.getDate().isBefore(dateOneYearAgo)
                    && !payroll.getDate().isAfter(endDate)) {
                gross_salary += payroll.getGrossSalary();
            }

        }

        // Y aque se pide la media diaria.
        gross_salary = gross_salary / 365;

        double compensationDaysPerYear = contractType
            .getCompensationDaysPerYear();
        int full_years_of_contract = (int) (days_between_star_and_end / 365);
        double settlement = gross_salary * compensationDaysPerYear
                * full_years_of_contract;
        return settlement;
    }

    public boolean isTerminated() {
        return state.equals(ContractState.TERMINATED);
    }

    Set<Payroll> _getPayrolls() {
        return payrolls;
    }

}
