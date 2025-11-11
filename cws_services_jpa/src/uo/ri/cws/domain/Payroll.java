package uo.ri.cws.domain;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;

@Entity
@Table(name = "TPAYROLLS")
public class Payroll extends BaseEntity {

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

    public Payroll(String id) {
        // Associations.
    }

    public Payroll(Contract c, LocalDate date) {
        this.contract = c;
        this.date = date;
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

    public double getGrossSalary() {
        return baseSalary + extraSalary + productivityEarning
                + trienniumEarning;
    }

    /**
     * 
     * @return
     */
    public double getMonthlyBaseSalary() {
        return baseSalary;
    }

    /**
     * Return net salary that is composed by :
     * 
     * @return
     */
    public double getNetSalary() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * Return the hole deductions of mechanic`s salary
     * 
     * @return
     */
    public double getTotalDeductions() {
        // TODO Auto-generated method stub
        return 0;
    }

}
