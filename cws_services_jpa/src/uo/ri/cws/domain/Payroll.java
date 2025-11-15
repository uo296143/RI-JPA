package uo.ri.cws.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TPAYROLLS")
public class Payroll extends BaseEntity {

    // Constante para el redondeo a 3 decimales (round half-up)
    private static final int DECIMAL_PLACES_EARNINGS_DEDUCTIONS = 3;
    // Constante para el redondeo a 2 decimales (total neto)
    private static final int DECIMAL_PLACES_NET_SALARY = 2;
    // Tasa de deducción por NIC (Seguridad Social)
    private static final double NIC_RATE = 0.05;

    private double baseSalary;
    private LocalDate date;
    private double extraSalary;
    private double nicDeduction;
    private double productivityEarning;
    private double taxDeduction;
    private double trienniumEarning;

    @ManyToOne
    private Contract contract;

    Payroll() {

    }

    public Payroll(Contract c, LocalDate date) {
        ArgumentChecks.isNotNull(c);
        ArgumentChecks.isNotNull(date);
        ArgumentChecks.isFalse(date.isBefore(c.getStartDate()));
        this.contract = c;
        this.date = date;
        // 1. Abonos (Earnings)
        this.baseSalary = round(c.getAnnualBaseSalary() / 14,
                DECIMAL_PLACES_EARNINGS_DEDUCTIONS);
        this.extraSalary = calculateExtraSalary(date, this.baseSalary);
        // El plus de productividad (productivityEarning) se calcula fuera y se
        // pasa como argumento,
        // ya que depende de workorders facturadas (lógica de
        // servicio/repositorio).
        double productivityBonus = computeProductivityBonus();
        this.productivityEarning = round(productivityBonus,
                DECIMAL_PLACES_EARNINGS_DEDUCTIONS);
        double trienniums = computeTrienniums();
        this.trienniumEarning = trienniums;

        // 2. Cálculo del Salario Bruto (Gross Salary)
        double grossSalary = getGrossSalary();

        // 3. Descuentos (Deductions)
        this.nicDeduction = calculateNicDeduction(c.getAnnualBaseSalary());
        this.taxDeduction = calculateTaxDeduction(c.getTaxRate(), grossSalary);
        Associations.Generates.link(this, c);
    }

    /*
     * 
     */
    private double computeTrienniums() {

        long fullYearsElapsed = ChronoUnit.YEARS
            .between(contract.getStartDate(), date);
        int numberOfTrienniums = (int) (fullYearsElapsed / 3);
        return contract.getProfessionalGroup().getTrienniumPayment()
                * numberOfTrienniums;
    }

    /*
     * 
     */
    private double computeProductivityBonus() {

        double percentage = contract.getProfessionalGroup()
            .getProductivityRate();
        double total_amount = contract.getMechanic()
            .getSumOfWorkOrdersAlreadyInvoiced(date.getMonth());

        return percentage * total_amount;
    }

    private double calculateExtraSalary(LocalDate payrollDate,
            double monthlyBaseSalary) {
        // Paga extra en junio (mes 6) y diciembre (mes 12)
        int month = payrollDate.getMonthValue();
        if (month == 6 || month == 12) {
            return round(monthlyBaseSalary, DECIMAL_PLACES_EARNINGS_DEDUCTIONS);
        }
        return 0.0;
    }

    private double calculateNicDeduction(double annualBaseSalary) {
        // NIC es un 5% del salario base anual prorrateado entre 12 meses
        double monthlyAnnualBaseSalary = annualBaseSalary / 12;
        return round(monthlyAnnualBaseSalary * NIC_RATE,
                DECIMAL_PLACES_EARNINGS_DEDUCTIONS);
    }

    private double calculateTaxDeduction(double taxRate, double grossSalary) {
        // Deducción por IRPF = taxRate sobre el total bruto
        return round(grossSalary * taxRate, DECIMAL_PLACES_EARNINGS_DEDUCTIONS);
    }

    // --- Redondeo (Función Helper) ---

    private double round(double value, int scale) {
        // Usa BigDecimal para un redondeo preciso 'round half-up'
        return BigDecimal.valueOf(value)
            .setScale(scale, RoundingMode.HALF_UP)
            .doubleValue();
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getExtraSalary() {
        return extraSalary;
    }

    public double getNicDeduction() {
        return nicDeduction;
    }

    public double getProductivityEarning() {
        return productivityEarning;
    }

    public double getTaxDeduction() {
        return taxDeduction;
    }

    public double getTrienniumEarning() {
        return trienniumEarning;
    }

    public Contract getContract() {
        return contract;
    }

    void _setContract(Contract contract) {
        this.contract = contract;
    }

    public double getGrossSalary() {
        return baseSalary + extraSalary + productivityEarning
                + trienniumEarning;
    }

    /**
     * 
     * @return
     */
    public double getMonthlyBaseSalary() {
        return contract.getAnnualBaseSalary() / 14;
    }

    /**
     * Return net salary that is composed by :
     * 
     * @return
     */
    public double getNetSalary() {
        double netSalary = getGrossSalary() - getTotalDeductions();
        // El total neto debe redondearse al segundo decimal.
        return round(netSalary, DECIMAL_PLACES_NET_SALARY);
    }

    /**
     * Return the hole deductions of mechanic`s salary
     * 
     * @return
     */
    public double getTotalDeductions() {
        return round(taxDeduction + nicDeduction,
                DECIMAL_PLACES_EARNINGS_DEDUCTIONS);
    }

}
