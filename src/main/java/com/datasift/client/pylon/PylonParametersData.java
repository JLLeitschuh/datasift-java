package com.datasift.client.pylon;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonParametersData {
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected String interval;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    protected float span;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    protected int threshold;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected String target;

    public PylonParametersData() { }

    public PylonParametersData(String target, Integer threshold) {
        this.threshold = threshold;
        this.target = target;
    }

    public PylonParametersData(String interval, Float span, Integer threshold, String target) {
        this.interval = interval;
        this.target = target;
        if (span != null) { this.span = span; }
        if (threshold != null) { this.threshold = threshold; }
    }

    public float getSpan() {
        return this.span;
    }

    public int getThreshold() {
        return this.threshold;
    }

    public String getTarget() {
        return this.target;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public void setSpan(Float span) {
        this.span = span;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
