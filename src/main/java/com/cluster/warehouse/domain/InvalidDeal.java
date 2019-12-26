package com.cluster.warehouse.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A InvalidDeal.
 */
@Document(collection = "invalid_deal")
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvalidDeal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("from_iso_code")
    private String fromIsoCode;

    @Field("deal_id")
    private String dealId;

    @Field("to_iso_code")
    private String toIsoCode;

    @Field("time")
	private String time;

    @Field("amount")
	private String amount;

    @Field("source")
    private String source;

    @Field("extension")
    private String extension;

    @Field("uploaded_on")
	private String uploadedOn;

    @Field("reason")
    private String reason;

	public InvalidDeal() {
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public InvalidDeal id(String id) {
		this.id = id;
        return this;
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public InvalidDeal dealId(String dealId) {
        this.dealId = dealId;
        return this;
    }

    public String getFromIsoCode() {
        return fromIsoCode;
    }

    public InvalidDeal fromIsoCode(String fromIsoCode) {
        this.fromIsoCode = fromIsoCode;
        return this;
    }

    public void setFromIsoCode(String fromIsoCode) {
        this.fromIsoCode = fromIsoCode;
    }

    public String getToIsoCode() {
        return toIsoCode;
    }

    public InvalidDeal toIsoCode(String toIsoCode) {
        this.toIsoCode = toIsoCode;
        return this;
    }

    public void setToIsoCode(String toIsoCode) {
        this.toIsoCode = toIsoCode;
    }

	public String getTime() {
        return time;
    }

	public InvalidDeal time(String time) {
        this.time = time;
        return this;
    }

	public void setTime(String time) {
        this.time = time;
    }

	public String getAmount() {
        return amount;
    }

	public InvalidDeal amount(String amount) {
        this.amount = amount;
        return this;
    }

	public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSource() {
        return source;
    }

    public InvalidDeal source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getExtension() {
        return extension;
    }

    public InvalidDeal extension(String extension) {
        this.extension = extension;
        return this;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

	public String getUploadedOn() {
        return uploadedOn;
    }

	public InvalidDeal uploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
        return this;
    }

	public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public String getReason() {
        return reason;
    }

    public InvalidDeal reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvalidDeal invalidDeal = (InvalidDeal) o;
        if (invalidDeal.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invalidDeal.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvalidDeal{" +
                "id=" + getId() +
				"" +
                ", fromIsoCode='" + getFromIsoCode() + "'" +
                ", toIsoCode='" + getToIsoCode() + "'" +
                ", time='" + getTime() + "'" +
                ", amount=" + getAmount() +
                ", source='" + getSource() + "'" +
                ", extension='" + getExtension() + "'" +
                ", uploadedOn='" + getUploadedOn() + "'" +
                "}";
    }
}
