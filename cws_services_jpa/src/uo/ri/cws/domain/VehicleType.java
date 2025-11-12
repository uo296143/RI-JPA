package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;

@Entity
@Table(name = "TVEHICLETYPES")
public class VehicleType extends BaseEntity {
    @Column(unique = true)
    private String name;
    private double pricePerHour;

    @OneToMany(mappedBy = "vehicleType")
    private Set<Vehicle> vehicles = new HashSet<>();

    VehicleType() {

    }

    public VehicleType(String name, double pricePerHour) {
        super();
        this.name = name;
        this.pricePerHour = pricePerHour;
    }

    public VehicleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public Set<Vehicle> getVehicles() {
        return new HashSet<Vehicle>(vehicles);
    }

    Set<Vehicle> _getVehicles() {
        return vehicles;
    }

    @Override
    public String toString() {
        return "VehicleType [name=" + name + ", pricePerHour=" + pricePerHour
                + ", vehicles=" + vehicles + "]";
    }

}
