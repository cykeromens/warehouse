package com.cluster.warehouse.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A Summary.
 */
@Document(collection = "summary")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Summary implements Serializable {

    private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@NotEmpty
	@Field("file_name")
	@Indexed(unique = true)
	private String source;

	@NotNull
	@Field("duration")
	private Double duration;

	@NotNull
	@Field("total")
	private Long total;

    @NotNull
	@Field("valid")
	private Long valid;

	@NotNull
	@Field("invalid")
	private Long invalid;

	@NotNull
	@Field("duplicate")
	private Long duplicate;

	@Field("date")
	private LocalDateTime date;

	public String getId() {
        return id;
    }

	public void setId(String id) {
        this.id = id;
    }

	public String getSource() {
		return source;
    }

	public Summary source(String source) {
		this.source = source;
        return this;
    }

	public void setSource(String source) {
		this.source = source;
    }

	public Double getDuration() {
		return duration;
    }

	public Summary duration(Double duration) {
		this.duration = duration;
        return this;
    }

	public void setDuration(Double duration) {
		this.duration = duration;
    }

	public Long getTotal() {
		return total;
    }

	public Summary total(Long total) {
		this.total = total;
        return this;
    }

	public void setTotal(Long total) {
		this.total = total;
    }

	public Long getValid() {
		return valid;
    }

	public Summary valid(Long valid) {
		this.valid = valid;
        return this;
    }

	public void setValid(Long valid) {
		this.valid = valid;
    }

	public Long getInvalid() {
		return invalid;
    }

	public Summary invalid(Long invalid) {
		this.invalid = invalid;
        return this;
    }

	public Long getDuplicate() {
		return duplicate;
	}

	public Summary duplicate(Long duplicate) {
		this.duplicate = duplicate;
		return this;
	}

	public void setDuplicate(Long duplicate) {
		this.duplicate = duplicate;
	}

	public void setInvalid(Long invalid) {
		this.invalid = invalid;
	}

	public LocalDateTime getDate() {
		return date;
    }

	public Summary date(LocalDateTime date) {
		this.date = date;
		return this;
    }

	public void setDate(LocalDateTime date) {
		this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Summary summary = (Summary) o;
        if (summary.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), summary.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Summary{" +
				"id=" + getId() +
				", source='" + getSource() + "'" +
				", duration=" + getDuration() +
				", total=" + getTotal() +
				", valid=" + getValid() +
				", invalid=" + getInvalid() +
				", duplicate=" + getDuplicate() +
				", date='" + getDate() + "'" +
				"}";
    }
}
