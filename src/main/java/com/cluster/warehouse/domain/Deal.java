package com.cluster.warehouse.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.cluster.warehouse.config.Constants.*;
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

    @NotNull(message = FROM_ISO_CODE + FAILED_MUST_NOT_BE_NULL)
    @Field("from_iso_code")
    @Size(min = 3, max = 3, message = FROM_ISO_CODE + FAILED_REQUIRED_SIZE)
    private String fromIsoCode;

    @NotNull(message = TO_ISO_CODE + FAILED_MUST_NOT_BE_NULL)
    @Size(min = 3, max = 3, message = TO_ISO_CODE + FAILED_REQUIRED_SIZE)
    @Field("to_iso_code")
    private String toIsoCode;

    @NotNull(message = TIME + FAILED_MUST_NOT_BE_NULL)
    @Field("time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime time;

    @NotNull(message = AMOUNT + FAILED_MUST_NOT_BE_NULL)
    @Field("amount")
    private Double amount;

    @NotNull(message = FILE_SOURCE + FAILED_MUST_NOT_BE_NULL)
    @Field("source")
    private String source;

    @Field("file_type")
    private String fileType;

    @Field("uploaded_on")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private LocalDateTime uploadedOn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Deal id(String id) {
        this.id = id;
        return this;
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

    public LocalDateTime getTime() {
        return time;
    }

    public Deal time(LocalDateTime time) {
        this.time = time;
        return this;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Double getAmount() {
        return amount;
    }

    public Deal amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
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

    public LocalDateTime getUploadedOn() {
        return uploadedOn;
    }

    public Deal uploadedOn(LocalDateTime uploadedOn) {
        this.uploadedOn = uploadedOn;
        return this;
    }

    public void setUploadedOn(LocalDateTime uploadedOn) {
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
                "" +
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
