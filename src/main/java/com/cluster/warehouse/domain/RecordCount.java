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

    @Column(name = "from_iso_code")
    private String fromISOCode;

    @Column(name = "to_iso_code")
    private String toISOCode;

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

    public String getFromISOCode() {
        return fromISOCode;
    }

    public RecordCount fromISOCode(String currencyISOCode) {
        this.fromISOCode = currencyISOCode;
        return this;
    }

    public void setFromISOCode(String fromISOCode) {
        this.fromISOCode = fromISOCode;
    }

    public String getToISOCode() {
        return toISOCode;
    }

    public RecordCount toISOCOde(String toISOCode) {
        this.toISOCode = toISOCode;
        return this;
    }

    public void setToISOCode(String toISOCode) {
        this.toISOCode = toISOCode;
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
                ", fromISOCode='" + getFromISOCode() + "'" +
                ", toISOCode='" + getToISOCode() + "'" +
                ", dealsCount=" + getDealsCount() +
                "}";
    }
}
