package uo.ri.cws.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TINTERVENTIONS", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "mechanic", "workOrder", "date" }) })
public class Intervention extends BaseEntity {

    // natural attributes
    private LocalDateTime date;
    private int minutes;

    // accidental attributes
    @ManyToOne
    private WorkOrder workOrder;
    @ManyToOne
    private Mechanic mechanic;
    @OneToMany(mappedBy = "intervention")
    private Set<Substitution> substitutions = new HashSet<>();

    Intervention() {

    }

    public Intervention(Mechanic mechanic, WorkOrder workOrder,
            LocalDateTime date, int minutes) {

        super();
        ArgumentChecks.isNotNull(mechanic, "Invalid null mechanic");
        ArgumentChecks.isNotNull(date, "Invalid null date");
        ArgumentChecks.isTrue(minutes >= 0, "Invalid negatives minutes");
        ArgumentChecks.isNotNull(workOrder, "Invalid null workOrder");

        this.date = date.truncatedTo(ChronoUnit.MILLIS);
        this.minutes = minutes;
        Associations.Intervenes.link(workOrder, this, mechanic);

    }

    public Intervention(Mechanic mechanic, WorkOrder workOrder, int minutes) {
        this(mechanic, workOrder, LocalDateTime.now(), minutes);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getMinutes() {
        return minutes;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    void _setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    void _setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    public Set<Substitution> getSubstitutions() {
        return new HashSet<>(substitutions);
    }

    Set<Substitution> _getSubstitutions() {
        return substitutions;
    }

    /**
     * Calcula la cantidad total a pagar por la intervención.
     * 
     * @return el precio de la intervención
     */
    public double getAmount() {
        double time_amount = 0.0;
        double sparepart_amount = minutes / 60
                * workOrder.getVehicle().getVehicleType().getPricePerHour();
        for (Substitution s : substitutions) {
            sparepart_amount += s.getAmount();
        }
        double total_amount = time_amount + sparepart_amount;
        return total_amount;
    }

}
