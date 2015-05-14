package org.eck.jgr;


public class Param {
    private String[] value;

    public Param(String[] value) {
        super();
        this.value = value;
    }

    private String first() {
        if (value != null && value.length > 0) {
            return value[0];
        }
        return null;
    }

    public Integer asInteger() {
        if (value != null) {
            return Integer.parseInt(first());
        }
        return null;
    }

    public String asString() {
        if (value != null) {
            return first();
        }
        return null;
    }

    public Long asLong() {
        if (value != null) {
            return Long.parseLong(first());
        }
        return null;
    }

    public Boolean asBoolean() {
        if (value != null) {
            return Boolean.parseBoolean(first());
        }
        return null;
    }

    public Float asFloat() {
        if (value != null) {
            return Float.parseFloat(first());
        }
        return null;
    }

    public Double asDouble() {
        if (value != null) {
            return Double.parseDouble(first());
        }
        return null;
    }
}