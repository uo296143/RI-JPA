package uo.ri.cws.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TCHARGES")
public class Charge extends BaseEntity {
    // natural attributes
    private double amount = 0.0;

    // accidental attributes
    @ManyToOne
    private Invoice invoice;
    @ManyToOne
    private PaymentMean paymentMean;

    Charge() {

    }

    /**
     * Store the amount increment the paymentMean accumulated ->
     * paymentMean.pay( amount ) link invoice, this and paymentMean
     * 
     * @param invoice
     * @param paymentMean
     * @param amount
     */
    public Charge(Invoice invoice, PaymentMean paymentMean, double amount) {
        ArgumentChecks.isNotNull(invoice, "Invoice´s charge can´t be null");
        ArgumentChecks.isNotNull(paymentMean, "Payment mean can´t be null");
        if (!paymentMean.canPay(amount)) {
            throw new IllegalStateException();
        }
        this.amount = amount;
        paymentMean.pay(amount);
        Associations.Settles.link(invoice, this, paymentMean);
    }

    /**
     * Unlinks this charge and restores the accumulated to the payment mean
     * 
     * @throws IllegalStateException if the invoice is already settled
     */
    public void rewind() {
        // asserts the invoice is not in PAID status
        // decrements the payment mean accumulated ( paymentMean.pay( -amount) )
        // unlinks invoice, this and paymentMean
    }

    public Invoice getInvoice() {
        return invoice;
    }

    @SuppressWarnings("unused")
    private void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public PaymentMean getPaymentMean() {
        return paymentMean;
    }

    @SuppressWarnings("unused")
    private void setPaymentMean(PaymentMean paymentMean) {
        this.paymentMean = paymentMean;
    }

    public double getAmount() {
        return amount;
    }

    void _setPaymentMean(PaymentMean mp) {
        this.paymentMean = mp;

    }

    void _setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

}
