package com.atixlabs.message;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class Message implements Serializable {

    private String id;
    private BigDecimal measurement;

    public Message() {
    }

    public Message(BigDecimal measurement, String id) {
        this.measurement = measurement;
        this.id = id;
    }

    public BigDecimal getMeasurement() {
        return measurement;
    }

    public void setMeasurement(BigDecimal measurement) {
        this.measurement = measurement;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
