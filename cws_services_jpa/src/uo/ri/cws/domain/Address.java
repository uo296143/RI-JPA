package uo.ri.cws.domain;

import java.util.Objects;

import uo.ri.util.assertion.ArgumentChecks;

/**
 * This class is a Value Type, thus
 *    - no setters
 *	  - hashcode and equals over all attributes
 */
public class Address {
	
	private String street;
	private String city;
	private String zipCode;
	
	public Address(String street, String city, String zipCode) {
		super();
		ArgumentChecks.isNotBlank(street, "Invalid street");
		ArgumentChecks.isNotBlank(city, "Invalid city");
		ArgumentChecks.isNotBlank(zipCode, "Invalid zipCode");

		this.street = street;
		this.city = city;
		this.zipCode = zipCode;
	}

	public String getStreet() {
		return street;
	}

	@Override
	public int hashCode() {
		return Objects.hash(city, street, zipCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		return Objects.equals(city, other.city)
				&& Objects.equals(street, other.street)
				&& Objects.equals(zipCode, other.zipCode);
	}

	public String getCity() {
		return city;
	}

	public String getZipCode() {
		return zipCode;
	}

	@Override
	public String toString() {
		return "Address [street=" + street + ", city=" + city + ", zipCode="
				+ zipCode + "]";
	}

}
