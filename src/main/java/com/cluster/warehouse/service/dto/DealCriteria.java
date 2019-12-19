package com.cluster.warehouse.service.dto;

import io.github.jhipster.service.filter.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the Deal entity. This class is used in DealResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /deals?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DealCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tagId;

    private StringFilter fromIsoCode;

    private StringFilter toIsoCode;

    private ZonedDateTimeFilter time;

    private BigDecimalFilter amount;

    private StringFilter source;

    private StringFilter sourceFormat;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTagId() {
        return tagId;
    }

    public void setTagId(StringFilter tagId) {
        this.tagId = tagId;
    }

    public StringFilter getFromIsoCode() {
        return fromIsoCode;
    }

    public void setFromIsoCode(StringFilter fromIsoCode) {
        this.fromIsoCode = fromIsoCode;
    }

    public StringFilter getToIsoCode() {
        return toIsoCode;
    }

    public void setToIsoCode(StringFilter toIsoCode) {
        this.toIsoCode = toIsoCode;
    }

    public ZonedDateTimeFilter getTime() {
        return time;
    }

    public void setTime(ZonedDateTimeFilter time) {
        this.time = time;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public StringFilter getSource() {
        return source;
    }

    public void setSource(StringFilter source) {
        this.source = source;
    }

    public StringFilter getSourceFormat() {
        return sourceFormat;
    }

    public void setSourceFormat(StringFilter sourceFormat) {
        this.sourceFormat = sourceFormat;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DealCriteria that = (DealCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(tagId, that.tagId) &&
            Objects.equals(fromIsoCode, that.fromIsoCode) &&
            Objects.equals(toIsoCode, that.toIsoCode) &&
            Objects.equals(time, that.time) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(source, that.source) &&
            Objects.equals(sourceFormat, that.sourceFormat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        tagId,
        fromIsoCode,
        toIsoCode,
        time,
        amount,
        source,
        sourceFormat
        );
    }

    @Override
    public String toString() {
        return "DealCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (tagId != null ? "tagId=" + tagId + ", " : "") +
                (fromIsoCode != null ? "fromIsoCode=" + fromIsoCode + ", " : "") +
                (toIsoCode != null ? "toIsoCode=" + toIsoCode + ", " : "") +
                (time != null ? "time=" + time + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (source != null ? "source=" + source + ", " : "") +
                (sourceFormat != null ? "sourceFormat=" + sourceFormat + ", " : "") +
            "}";
    }

}
