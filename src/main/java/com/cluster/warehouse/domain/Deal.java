package com.cluster.warehouse.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import static com.cluster.warehouse.config.Constants.DATETIME_FORMAT;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * A Deal.
 */
@Document(collection = "deal")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_EMPTY)
public class Deal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("tag_id")
    @Indexed(unique = true)
    private String tagId;

    @NotNull
    @Field("from_iso_code")
    @Size(min = 3, max = 3)
    private String fromIsoCode;

    @NotNull
    @Size(min = 3, max = 3)
    @Field("to_iso_code")
    private String toIsoCode;

    @NotNull
    @Field("time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private Date time;

    @NotNull
    @Field(name = "amount", targetType = FieldType.DECIMAL128)
    private BigDecimal amount;

    @NotNull
    @Field("source")
    private String source;

    @Field("file_type")
    private String fileType;

    @NotNull
    @Field("uploaded_on")
    private LocalDate uploadedOn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Date getTime() {
        return time;
    }

    public Deal time(Date time) {
        this.time = time;
        return this;
    }

    public void setTime(Date time) {
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

    public String getFileType() {
        return fileType;
    }

    public Deal fileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public LocalDate getUploadedOn() {
        return uploadedOn;
    }

    public Deal uploadedOn(LocalDate uploadedOn) {
        this.uploadedOn = uploadedOn;
        return this;
    }

    public void setUploadedOn(LocalDate uploadedOn) {
        this.uploadedOn = uploadedOn;
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
