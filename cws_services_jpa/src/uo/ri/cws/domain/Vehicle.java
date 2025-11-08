package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;

@Entity
@Table(name = "TVEHICLES")
public class Vehicle extends BaseEntity {
    @Column(unique = true)
    private String plateNumber;
    private String make;
    private String model;

    @ManyToOne
    private Client client;
    @ManyToOne
    private VehicleType vehicleType;
    @OneToMany(mappedBy = "vehicle")
    private Set<WorkOrder> workOrders = new HashSet<WorkOrder>();

    Vehicle() {

    }

    protected void _setClient(Client client) {
        this.client = client;
    }

    public Set<WorkOrder> getWorkOrders() {
        return new HashSet<>(workOrders);
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public Vehicle(String plateNumber, String make, String model) {
        super();
        this.plateNumber = plateNumber;
        this.make = make;
        this.model = model;
    }

    public Vehicle(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    @Override
    public String toString() {
        return "Vehicle [plateNumber=" + plateNumber + ", make=" + make
                + ", model=" + model + "]";
    }

    public Client getClient() {
        return client;
    }

    void _setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    Set<WorkOrder> _getWorkOrders() {
        return workOrders;
    }

}
