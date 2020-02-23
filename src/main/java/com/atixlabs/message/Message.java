package com.atixlabs.message;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class Message {

    private BigDecimal measurement;

    public Message() {
    }

    public Message(BigDecimal measurement) {
        this.measurement = measurement;
    }

    public BigDecimal getMeasurement() {
        return measurement;
    }

    public void setMeasurement(BigDecimal measurement) {
        this.measurement = measurement;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
