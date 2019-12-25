package com.cluster.warehouse.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Report.
 */
@Document(collection = "report")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Report implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@NotNull
	@Size(min = 3, max = 3)
	@Field("from_iso_code")
	private String fromIsoCode;

	@NotNull
	@Size(min = 3, max = 3)
	@Field("to_iso_code")
	private String toIsoCode;

	@Field("total")
	private Long total;

	@Field("last_updated")
	private LocalDate lastUpdated;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromIsoCode() {
		return fromIsoCode;
	}

	public Report fromIsoCode(String fromIsoCode) {
		this.fromIsoCode = fromIsoCode;
		return this;
	}

	public void setFromIsoCode(String fromIsoCode) {
		this.fromIsoCode = fromIsoCode;
	}

	public String getToIsoCode() {
		return toIsoCode;
	}

	public Report toIsoCode(String toIsoCode) {
		this.toIsoCode = toIsoCode;
		return this;
	}

	public void setToIsoCode(String toIsoCode) {
		this.toIsoCode = toIsoCode;
	}

	public Long getTotal() {
		return total;
	}

	public Report total(Long total) {
		this.total = total;
		return this;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public LocalDate getLastUpdated() {
		return lastUpdated;
	}

	public Report lastUpdated(LocalDate lastUpdated) {
		this.lastUpdated = lastUpdated;
		return this;
	}

	public void setLastUpdated(LocalDate lastUpdated) {
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
		Report report = (Report) o;
		if (report.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), report.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Report{" +
				"id=" + getId() +
				", fromIsoCode='" + getFromIsoCode() + "'" +
				", toIsoCode='" + getToIsoCode() + "'" +
				", total=" + getTotal() +
				", lastUpdated='" + getLastUpdated() + "'" +
				"}";
	}
}
