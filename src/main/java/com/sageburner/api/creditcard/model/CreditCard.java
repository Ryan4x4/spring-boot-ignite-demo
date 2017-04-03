package com.sageburner.api.creditcard.model;

import com.sageburner.base.dao.Model;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import javax.persistence.*;

@Entity
@Table(name="CREDIT_CARD",
        uniqueConstraints=@UniqueConstraint(columnNames={"NUMBER"}))
public class CreditCard extends Model {
    @Id
    @Column(name="NUMBER")
    @QuerySqlField
    private String number;
    @Column(name="TYPE")
	private String type;
    @Column(name="EXPIRE_MONTH")
    @QuerySqlField
	private int expireMonth;
    @Column(name="EXPIRE_YEAR")
    @QuerySqlField
	private int expireYear;
    @Column(name="CVV2")
	private Integer cvv2;
    @Column(name="FIRST_NAME")
	private String firstName;
    @Column(name="LAST_NAME")
	private String lastName;
    @Embedded
    @Column(name="BILLING_ADDRESS")
    private Address billingAddress;
    @Column(name="EXTERNAL_CUSTOMER_ID")
	private String externalCustomerId;
    @Column(name="FUNDING_STATE")
	private String fundingState;
    @Column(name="VALID_UNTIL")
	private String validUntil;
    @Column(name="PAYER_ID")
	private String payerId;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(int expireMonth) {
        this.expireMonth = expireMonth;
    }

    public int getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(int expireYear) {
        this.expireYear = expireYear;
    }

    public Integer getCvv2() {
        return cvv2;
    }

    public void setCvv2(Integer cvv2) {
        this.cvv2 = cvv2;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getExternalCustomerId() {
        return externalCustomerId;
    }

    public void setExternalCustomerId(String externalCustomerId) {
        this.externalCustomerId = externalCustomerId;
    }

    public String getFundingState() {
        return fundingState;
    }

    public void setFundingState(String fundingState) {
        this.fundingState = fundingState;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }
}
