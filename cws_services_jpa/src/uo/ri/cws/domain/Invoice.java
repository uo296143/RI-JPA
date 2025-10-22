package uo.ri.cws.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;

import uo.ri.util.assertion.ArgumentChecks;

public class Invoice {
	public enum InvoiceState { NOT_YET_PAID, PAID }

	// natural attributes
	private Long number;
	private LocalDate date;
	private double amount;
	private double vat;
	private InvoiceState state = InvoiceState.NOT_YET_PAID;

	// accidental attributes
	private Set<WorkOrder> workOrders = new HashSet<>();
	private Set<Charge> charges = new HashSet<>();

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
		for(WorkOrder wo : workOrders) {
			addWorkOrder(wo);
		}
	}

	/**
	 * Computes amount and vat (vat depends on the date)
	 */
	private void computeAmount() {

	}

	/**
	 * Adds (double links) the workOrder to the invoice and updates the amount and vat
	 * @param workOrder
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if the invoice status is not NOT_YET_PAID
	 * @throws IllegalStateException if the workorder status is not FINISHED 
	 */
	public void addWorkOrder(WorkOrder workOrder) {

	}

	/**
	 * Removes a work order from the invoice, updates the workorder state
	 * and recomputes amount and vat
	 * @param workOrder
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if the invoice status is not NOT_YET_PAID
	 * @throws IllegalArgumentException if the invoice does not contain the workorder
	 */
	public void removeWorkOrder(WorkOrder workOrder) {

	}

	/**
	 * Marks the invoice as PAID, but
	 * @throws IllegalStateException if
	 * 	- Is already settled
	 *  - Or the amounts paid with charges to payment means do not cover
	 *  	the total of the invoice
	 */
	public void settle() {

	}

	public Set<WorkOrder> getWorkOrders() {
		return new HashSet<>( workOrders );
	}

	Set<WorkOrder> _getWorkOrders() {
		return workOrders;
	}

	public Set<Charge> getCharges() {
		return new HashSet<>( charges );
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

	@Override
	public int hashCode() {
		return Objects.hash(number);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Invoice other = (Invoice) obj;
		return Objects.equals(number, other.number);
	}
	
	

}
