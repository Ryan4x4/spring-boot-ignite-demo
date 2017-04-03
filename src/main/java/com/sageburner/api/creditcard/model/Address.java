package com.sageburner.api.creditcard.model;

import com.sageburner.base.dao.Model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
@Table(name="ADDRESS")
public class Address extends Model {
    @Column(name="LINE_1")
	private String line1;
    @Column(name="LINE_2")
    private String line2;
    @Column(name="CITY")
	private String city;
    @Column(name="COUNTRY_CODE")
	private String countryCode;
    @Column(name="POSTAL_CODE")
	private String postalCode;
    @Column(name="STATE")
	private String state;
    @Column(name="PHONE")
	private String phone;
    @Column(name="STATUS")
	private String status;

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
