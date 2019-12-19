package com.cluster.warehouse.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A InvalidDeal.
 */
@Entity
@Table(name = "invalid_deal")
public class InvalidDeal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_id")
    private String tagId;

    @Column(name = "from_iso_code")
    private String fromIsoCode;

    @Column(name = "to_iso_code")
    private String toIsoCode;

    @Column(name = "deal_time")
    private ZonedDateTime time;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "source")
    private String source;

    @Column(name = "source_format")
    private String sourceFormat;

    @Column(name = "reason")
    private String reason;

    @Column(name = "uploaded_date", nullable = false)
    private LocalDate uploadedDate;

    public InvalidDeal() {
    }

    public InvalidDeal(Deal deal, String reason) {
        this.tagId = deal.getTagId();
        this.fromIsoCode = deal.getFromIsoCode();
        this.toIsoCode = deal.getToIsoCode();
        this.time = deal.getTime();
        this.amount = deal.getAmount();
        this.source = deal.getSource();
        this.sourceFormat = deal.getSourceFormat();
        this.reason = reason;
        this.uploadedDate = deal.getUploadedDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public InvalidDeal tagId(String tagId) {
        this.tagId = tagId;
        return this;
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

    public ZonedDateTime getTime() {
        return time;
    }

    public InvalidDeal time(ZonedDateTime time) {
        this.time = time;
        return this;
    }

    public void setTime(ZonedDateTime time) {
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

    public String getSourceFormat() {
        return sourceFormat;
    }

    public InvalidDeal sourceFormat(String sourceFormat) {
        this.sourceFormat = sourceFormat;
        return this;
    }

    public void setSourceFormat(String sourceFormat) {
        this.sourceFormat = sourceFormat;
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

    public LocalDate getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(LocalDate uploadedDate) {
        this.uploadedDate = uploadedDate;
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
                ", tagId=" + getTagId() +
                ", fromIsoCode='" + getFromIsoCode() + "'" +
                ", toIsoCode='" + getToIsoCode() + "'" +
                ", time='" + getTime() + "'" +
                ", amount=" + getAmount() +
                ", source='" + getSource() + "'" +
                ", sourceFormat='" + getSourceFormat() + "'" +
                ", reason='" + getReason() + "'" +
                ", uploadedDate='" + getUploadedDate() + "'" +
                "}";
    }
}
