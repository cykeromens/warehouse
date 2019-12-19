package com.cluster.warehouse.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Summary.
 */
@Entity
@Table(name = "summary")
public class Summary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "file_name", nullable = false, unique = true)
    private String fileName;

    @NotNull
    @Column(name = "process_duration", nullable = false)
    private Double processDuration;

    @NotNull
    @Column(name = "total_imported", nullable = false)
    private Integer totalImported;

    @NotNull
    @Column(name = "total_valid", nullable = false)
    private Integer totalValid;

    @Column(name = "total_not_valid")
    private Integer totalNotValid;

    @Column(name = "uploaded_date", nullable = false)
    private LocalDate uploadedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public Summary fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Double getProcessDuration() {
        return processDuration;
    }

    public Summary processDuration(Double processDuration) {
        this.processDuration = processDuration;
        return this;
    }

    public void setProcessDuration(Double processDuration) {
        this.processDuration = processDuration;
    }

    public Integer getTotalImported() {
        return totalImported;
    }

    public Summary totalImported(Integer totalImported) {
        this.totalImported = totalImported;
        return this;
    }

    public void setTotalImported(Integer totalImported) {
        this.totalImported = totalImported;
    }

    public Integer getTotalValid() {
        return totalValid;
    }

    public Summary totalValid(Integer totalValid) {
        this.totalValid = totalValid;
        return this;
    }

    public void setTotalValid(Integer totalValid) {
        this.totalValid = totalValid;
    }

    public Integer getTotalNotValid() {
        return totalNotValid;
    }

    public Summary totalNotValid(Integer totalNotValid) {
        this.totalNotValid = totalNotValid;
        return this;
    }

    public LocalDate getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(LocalDate uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public void setTotalNotValid(Integer totalNotValid) {
        this.totalNotValid = totalNotValid;
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
                ", fileName='" + getFileName() + "'" +
                ", processDuration=" + getProcessDuration() +
                ", totalImported=" + getTotalImported() +
                ", totalValid=" + getTotalValid() +
                ", totalNotValid=" + getTotalNotValid() +
                "},uploadedDate=" + getUploadedDate() +
                "}";
    }
}
