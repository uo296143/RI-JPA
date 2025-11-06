package uo.ri.cws.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TWORKORDERS", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "date", "vehicle_id" }) })

public class WorkOrder extends BaseEntity {
    public enum WorkOrderState {
        OPEN, ASSIGNED, FINISHED, INVOICED
    }

    // natural attributes
    private LocalDateTime date;
    private String description;
    private double amount = 0.0;
    @Enumerated(EnumType.STRING)
    private WorkOrderState state = WorkOrderState.OPEN;

    // accidental attributes
    @ManyToOne
    private Vehicle vehicle;
    @ManyToOne
    private Mechanic mechanic;
    @ManyToOne
    private Invoice invoice;
    @OneToMany(mappedBy = "workOrder")
    private Set<Intervention> interventions = new HashSet<>();

    WorkOrder() {

    }

    public WorkOrder(Vehicle vehicle, LocalDateTime date, String description) {
        super();
        ArgumentChecks.isNotNull(vehicle, "Invalid null vehicle");
        ArgumentChecks.isNotNull(date, "Invalid null date");
        ArgumentChecks.isNotBlank(description, "Invalid description");

        this.date = date.truncatedTo(ChronoUnit.MILLIS);
        this.description = description;
        Associations.Fixes.link(vehicle, this);

    }

    public WorkOrder(Vehicle vehicle, String description) {
        this(vehicle, LocalDateTime.now(), description);
    }

    public WorkOrder(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public WorkOrder(Vehicle vehicle, LocalDateTime date) {
        this.vehicle = vehicle;
        this.date = date;
    }

    /**
     * Changes it to INVOICED state given the right conditions This method is
     * called from Invoice.addWorkOrder(...)
     * 
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if - The work order is not FINISHED, or -
     *                               The work order is not linked with the
     *                               invoice
     */
    public void markAsInvoiced() {
        if (!state.equals(WorkOrderState.FINISHED)) {
            throw new IllegalStateException(
                    "The invoice must be finished to be invoiced");
        }
        ArgumentChecks.isNotNull(invoice);
        state = WorkOrderState.INVOICED;
        updatedNow();
    }

    /**
     * Given the right conditions unlinks the workorder and the mechanic,
     * changes the state to FINISHED and computes the amount
     *
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if - The work order is not in ASSIGNED
     *                               state, or
     */
    public void markAsFinished() {

    }

    /**
     * Changes it back to FINISHED state given the right conditions This method
     * is called from Invoice.removeWorkOrder(...)
     * 
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if - The work order is not INVOICED, or
     */
    public void markBackToFinished() {
        if (!state.equals(WorkOrderState.INVOICED)) {
            throw new IllegalStateException("The invoice must be invoiced");
        }
        state = WorkOrderState.FINISHED;
        updatedNow();
    }

    /**
     * Links (assigns) the work order to a mechanic and then changes its state
     * to ASSIGNED
     * 
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if - The work order is not in OPEN state,
     *                               or
     */
    public void assignTo(Mechanic mechanic) {

    }

    /**
     * Unlinks (deassigns) the work order and the mechanic and then changes its
     * state back to OPEN
     * 
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if - The work order is not in ASSIGNED
     *                               state
     */
    public void unassign() {

    }

    /**
     * In order to assign a work order to another mechanic it first have to be
     * moved back to OPEN state.
     * 
     * @see UML_State diagrams on the problem statement document
     * @throws IllegalStateException if - The work order is not in FINISHED
     *                               state
     */
    public void reopen() {

    }

    public Set<Intervention> getInterventions() {
        return new HashSet<>(interventions);
    }

    Set<Intervention> _getInterventions() {
        return interventions;
    }

    void _setVehicle(Vehicle vehicle) {
        updatedNow();
        this.vehicle = vehicle;
    }

    void _setMechanic(Mechanic mechanic) {
        updatedNow();
        this.mechanic = mechanic;
    }

    void _setInvoice(Invoice invoice) {
        updatedNow();
        this.invoice = invoice;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public boolean isFinished() {
        return state == WorkOrderState.FINISHED;
    }

    @Override
    public String toString() {
        return "WorkOrder [date=" + date + ", description=" + description
                + ", amount=" + amount + ", state=" + state + ", vehicle="
                + vehicle + ", mechanic=" + mechanic + ", invoice=" + invoice
                + ", interventions=" + interventions + "]";
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public double getAmount() {
        double total_amount = 0.0;
        for (Intervention i : interventions) {
            total_amount += i.getAmount();
        }
        return total_amount;
    }

    public boolean isAssigned() {
        return mechanic != null;
    }

    public boolean isOpen() {
        return state.equals(WorkOrderState.OPEN);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public boolean isInvoiced() {
        return state.equals(WorkOrderState.INVOICED);
    }

    public WorkOrderState getState() {
        return state;
    }

    public void setState(WorkOrderState state) {
        updatedNow();
        this.state = state;
    }

}
