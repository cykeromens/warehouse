package com.cluster.warehouse.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A RecordCount.
 */
@Entity
@Table(name = "record_count")
public class RecordCount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_iso_code")
    private String currencyISOCode;

    @Column(name = "deals_count")
    private Long dealsCount;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrencyISOCode() {
        return currencyISOCode;
    }

    public RecordCount currencyISOCode(String currencyISOCode) {
        this.currencyISOCode = currencyISOCode;
        return this;
    }

    public void setCurrencyISOCode(String currencyISOCode) {
        this.currencyISOCode = currencyISOCode;
    }

    public Long getDealsCount() {
        return dealsCount;
    }

    public RecordCount dealsCount(Long dealsCount) {
        this.dealsCount = dealsCount;
        return this;
    }

    public void setDealsCount(Long dealsCount) {
        this.dealsCount = dealsCount;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecordCount recordCount = (RecordCount) o;
        if (recordCount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), recordCount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RecordCount{" +
                "id=" + getId() +
                ", currencyISOCode='" + getCurrencyISOCode() + "'" +
                ", dealsCount=" + getDealsCount() +
                "}";
    }
}
