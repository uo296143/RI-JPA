package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TCLIENTS")
public class Client extends BaseEntity {
    @Column(unique = true)
    private String nif;
    private String name;
    private String surname;
    private String email;
    private String phone;
    @Embedded
    private Address address;

    /*
     * Atributos accidentales
     */
    @OneToMany(mappedBy = "client")
    private Set<Vehicle> vehicles = new HashSet<Vehicle>();
    @OneToMany(mappedBy = "client")
    private Set<PaymentMean> paymentMeans = new HashSet<PaymentMean>();

    // PaymentsMeans

    public Client(String nif, String name, String surname, String email,
            String phone, Address address) {
        super();
        ArgumentChecks.isNotBlank(nif, "Invalid nif");
        ArgumentChecks.isNotBlank(name, "Invalid name");
        ArgumentChecks.isNotBlank(surname, "Invalid surname");
        ArgumentChecks.isNotBlank(email, "Invalid email");
        ArgumentChecks.isNotBlank(phone, "Invalid phone");

        this.nif = nif;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    Client() {

    }

    public Client(String nif, String name, String surname) {
        this(nif, name, surname, "no@email", "no-phone", null);
    }

    public Client(String nif) {
        this.nif = nif;
    }

    @Override
    public String toString() {
        return "Client [nif=" + nif + ", name=" + name + ", surname=" + surname
                + ", email=" + email + ", phone=" + phone + ", address="
                + address + "]";
    }

    public String getNif() {
        return nif;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    public Set<Vehicle> getVehicles() {
        return new HashSet<Vehicle>(vehicles);
    }

    protected Set<Vehicle> _getVehicles() {
        return vehicles;
    }

    public Set<PaymentMean> getPaymentMeans() {
        return new HashSet<PaymentMean>(paymentMeans);
    }

    Set<PaymentMean> _getPaymentMeans() {
        return paymentMeans;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}
