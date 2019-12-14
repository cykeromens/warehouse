package com.cluster.data.warehouse.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Deal.
 */
@Entity
@Table(name = "deal")
public class Deal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "tag_id", nullable = false, unique = true)
    private String tagId;

    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "from_iso_code", length = 3, nullable = false)
    private String fromIsoCode;

    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "to_iso_code", length = 3, nullable = false)
    private String toIsoCode;

    @NotNull
    @Column(name = "time", nullable = false)
    private ZonedDateTime time;

    @NotNull
    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "fromCountry", nullable = false)
    private String fromCountry;

    @Column(name = "toCountry", nullable = false)
    private String toCountry;

    @NotNull
    @Column(name = "source_format", nullable = false)
    private String sourceFormat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public Deal tagId(String tagId) {
        this.tagId = tagId;
        return this;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getFromIsoCode() {
        return fromIsoCode;
    }

    public Deal fromIsoCode(String fromIsoCode) {
        this.fromIsoCode = fromIsoCode;
        return this;
    }

    public void setFromIsoCode(String fromIsoCode) {
        this.fromIsoCode = fromIsoCode;
    }

    public String getToIsoCode() {
        return toIsoCode;
    }

    public Deal toIsoCode(String toIsoCode) {
        this.toIsoCode = toIsoCode;
        return this;
    }

    public void setToIsoCode(String toIsoCode) {
        this.toIsoCode = toIsoCode;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public Deal time(ZonedDateTime time) {
        this.time = time;
        return this;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Deal amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSource() {
        return source;
    }

    public Deal source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceFormat() {
        return sourceFormat;
    }

    public Deal sourceFormat(String sourceFormat) {
        this.sourceFormat = sourceFormat;
        return this;
    }

    public void setSourceFormat(String sourceFormat) {
        this.sourceFormat = sourceFormat;
    }

    public String getFromCountry() {
        return fromCountry;
    }

    public Deal fromCountry(String fromCountry) {
        this.fromCountry = fromCountry;
        return this;
    }

    public void setFromCountry(String fromCountry) {
        this.fromCountry = fromCountry;
    }

    public String getToCountry() {
        return toCountry;
    }

    public Deal toCountry(String toCountry) {
        this.toCountry = toCountry;
        return this;
    }

    public void setToCountry(String toCountry) {
        this.toCountry = toCountry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Deal deal = (Deal) o;
        if (deal.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deal.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Deal{" +
                "id=" + getId() +
                ", tagId=" + getTagId() +
                ", fromIsoCode='" + getFromIsoCode() + "'" +
                ", fromCountry='" + getFromCountry() + "'" +
                ", toIsoCode='" + getToIsoCode() + "'" +
                ", toCountry='" + getToCountry() + "'" +
                ", time='" + getTime() + "'" +
                ", amount=" + getAmount() +
                ", source='" + getSource() + "'" +
                ", sourceFormat='" + getSourceFormat() + "'" +
                "}";
    }
}
