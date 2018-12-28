package com.blah.recipes.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;

import java.util.Objects;
import java.util.Optional;

@DynamoDBDocument
public class Preparation {

    public enum Method {
        DICE, CHOP, MINCE, JUICE, GROUND, SLICE, GRATE, BEAT
    }

    public enum Style {
        FINE, COURSE
    }

    private Method method;

    private Optional<Style> style = Optional.empty();

    public Preparation(Method method, Style style) {
        this.method = method;
        this.style = Optional.ofNullable(style);
    }

    public Preparation(Method method) {
        this(method, null);
    }

    public Preparation() {
    }

    @DynamoDBAttribute
    @DynamoDBTypeConvertedEnum
    public Method getMethod() {
        return method;
    }

    @DynamoDBAttribute
    @DynamoDBTypeConvertedEnum
    public Style getStyle() {
        return style.orElse(null);
    }

    @DynamoDBIgnore
    public Optional<Style> getStyleSafe() {
        return style;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setStyle(Style style) {
        this.setStyle(Optional.ofNullable(style));
    }

    public void setStyle(Optional<Style> style) {
        this.style = style;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Preparation that = (Preparation) o;
        return method == that.method &&
                Objects.equals(style, that.style);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, style);
    }

    @Override
    public String toString() {
        return "Preparation{" +
                "method=" + method +
                ", style=" + style +
                '}';
    }
}
