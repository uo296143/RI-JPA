package uo.ri.cws.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.math.Rounds;

@Entity
@Table(name = "TINVOICES")
public class Invoice extends BaseEntity {
    public enum InvoiceState {
        NOT_YET_PAID, PAID
    }

    @Column(unique = true)
    private Long number;
    private LocalDate date;
    private double amount;
    private double vat;
    @Enumerated(EnumType.STRING)
    private InvoiceState state = InvoiceState.NOT_YET_PAID;

    // accidental attributes
    @OneToMany(mappedBy = "invoice")
    private Set<WorkOrder> workOrders = new HashSet<>();
    @OneToMany(mappedBy = "invoice")
    private Set<Charge> charges = new HashSet<>();

    Invoice() {

    }

    public Invoice(Long number) {
        // call full constructor with sensible defaults
        this(number, LocalDate.now(), List.of());
    }

    public Invoice(Long number, LocalDate date) {
        // call full constructor with sensible defaults
        this(number, date, List.of());
    }

    public Invoice(Long number, List<WorkOrder> workOrders) {
        this(number, LocalDate.now(), workOrders);
    }

    // full constructor
    public Invoice(Long number, LocalDate date, List<WorkOrder> workOrders) {
        // check arguments (always), through IllegalArgumentException
        // store the number
        // add every work order calling addWorkOrder( w )
        ArgumentChecks.isNotNull(number, "Invalid null number");
        ArgumentChecks.isNotNull(date, "Invalid null date");
        ArgumentChecks.isNotNull(workOrders, "Invalid null workorders");
        this.date = date;
        this.number = number;
        for (WorkOrder wo : workOrders) {
            addWorkOrder(wo);
        }
    }

    /**
     * Computes amount and vat (vat depends on the date)
     */
    private void computeAmount() {

        double total_without_vat = workOrders.stream()
            .mapToDouble(WorkOrder::getAmount)
            .sum();

        amount = Rounds
            .toCents(total_without_vat + calculateVatAmount(total_without_vat));

    }

    private double calculateVatAmount(double total_without_vat) {

        double percentage = LocalDate.parse("2012-07-01").isBefore(date) ? 21.0
                : 18.0;

        return total_without_vat * percentage / 100;

    }

    /**
     * Adds (double links) the workOrder to the invoice and updates the amount
     * and vat
     * 
     * @param workOrder
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if the invoice status is not NOT_YET_PAID
     * @throws IllegalStateException if the workorder status is not FINISHED
     */
    public void addWorkOrder(WorkOrder workOrder) {
        ArgumentChecks.isNotNull(workOrder);
        ArgumentChecks.isTrue(state.equals(InvoiceState.NOT_YET_PAID));
        ArgumentChecks.isTrue(workOrder.isFinished());
        Associations.Bills.link(this, workOrder);
        workOrder.markAsInvoiced();
        computeAmount();
    }

    /**
     * Removes a work order from the invoice, updates the workorder state and
     * recomputes amount and vat
     * 
     * @param workOrder
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException    if the invoice status is not
     *                                  NOT_YET_PAID
     * @throws IllegalArgumentException if the invoice does not contain the
     *                                  workorder
     */
    public void removeWorkOrder(WorkOrder workOrder) {
        ArgumentChecks.isTrue(workOrders.contains(workOrder));
        if (!state.equals(InvoiceState.NOT_YET_PAID)) {
            throw new IllegalStateException();
        }
        workOrder.markBackToFinished();
        workOrders.remove(workOrder);
        computeAmount();
    }

    /**
     * Marks the invoice as PAID, but
     * 
     * @throws IllegalStateException if - Is already settled - Or the amounts
     *                               paid with charges to payment means do not
     *                               cover the total of the invoice
     */
    public void settle() {
        if (isSettled()) {
            throw new IllegalStateException();
        }
        if (getAmountAlreadyPaid() < amount) {
            throw new IllegalStateException("All the amount hasn`t paid yet");
        }
        state = InvoiceState.PAID;
    }

    public Set<WorkOrder> getWorkOrders() {
        return new HashSet<>(workOrders);
    }

    Set<WorkOrder> _getWorkOrders() {
        return workOrders;
    }

    public Set<Charge> getCharges() {
        return new HashSet<>(charges);
    }

    Set<Charge> _getCharges() {
        return charges;
    }

    public Long getNumber() {
        return number;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public double getVat() {
        return vat;
    }

    /*
     * Retunr the full amount alrady paid by all the charges
     */
    private double getAmountAlreadyPaid() {
        double totalAmountAlreadyPaid = 0;
        for (Charge charges : charges) {
            totalAmountAlreadyPaid += charges.getAmount();
        }
        return totalAmountAlreadyPaid;
    }

    /*
     * Hay que hacer isFinished y borrar este get
     */
    public InvoiceState getState() {
        return state;
    }

    public boolean isSettled() {
        return state == InvoiceState.PAID;
    }

    public boolean isNotSettled() {
        return state == InvoiceState.NOT_YET_PAID;
    }

    @Override
    public String toString() {
        return "Invoice [number=" + number + ", date=" + date + ", amount="
                + amount + ", vat=" + vat + ", state=" + state + "]";
    }

}
