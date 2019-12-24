package com.cluster.warehouse.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
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

    @Field("tag_id")
    private String tagId;

    @Field("from_iso_code")
    private String fromIsoCode;

    @Field("to_iso_code")
    private String toIsoCode;

    @Field("time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date time;

    @Field("amount")
    private BigDecimal amount;

    @Field("source")
    private String source;

    @Field("file_type")
    private String fileType;

    @Field("uploaded_on")
    private LocalDate uploadedOn;

    @Field("reason")
    private String reason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public InvalidDeal() {
    }

    public InvalidDeal tagId(String tagId) {
        this.tagId = tagId;
        return this;
    }

    public InvalidDeal(Deal deal, String reason) {
        this.tagId = deal.getTagId();
        this.fromIsoCode = deal.getFromIsoCode();
        this.toIsoCode = deal.getToIsoCode();
        this.time = deal.getTime();
        this.amount = deal.getAmount();
        this.source = deal.getSource();
        this.reason = reason;
        this.uploadedOn = deal.getUploadedOn();
        this.fileType = deal.getFileType();
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
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

    public Date getTime() {
        return time;
    }

    public InvalidDeal time(Date time) {
        this.time = time;
        return this;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public InvalidDeal amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
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

    public String getFileType() {
        return fileType;
    }

    public InvalidDeal fileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public LocalDate getUploadedOn() {
        return uploadedOn;
    }

    public InvalidDeal uploadedOn(LocalDate uploadedOn) {
        this.uploadedOn = uploadedOn;
        return this;
    }

    public void setUploadedOn(LocalDate uploadedOn) {
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
                ", tagId='" + getTagId() + "'" +
                ", fromIsoCode='" + getFromIsoCode() + "'" +
                ", toIsoCode='" + getToIsoCode() + "'" +
                ", time='" + getTime() + "'" +
                ", amount=" + getAmount() +
                ", source='" + getSource() + "'" +
                ", fileType='" + getFileType() + "'" +
                ", uploadedOn='" + getUploadedOn() + "'" +
                "}";
    }
}
