package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import uo.ri.util.assertion.ArgumentChecks;

public class Client {
	
	/*
	 * Atributos naturales
	 */
	private String nif;
	private String name;
	private String surname;
	private String email;
	private String phone;
	private Address address;
	
	/*
	 * Atributos accidentales
	 */
	private Set<Vehicle> vehicles = new HashSet<Vehicle>();
	
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

	public Client(String nif, String name, String surname) {
		this(nif, name, surname, "no@email", "no-phone", null);
	}

	@Override
	public String toString() {
		return "Client [nif=" + nif + ", name=" + name + ", surname=" + surname
				+ ", email=" + email + ", phone=" + phone + ", address="
				+ address + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(nif);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		return Objects.equals(nif, other.nif);
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

}

