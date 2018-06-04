package io.reflectoring.dsl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class Root {

  private String nullField = null;
  private String stringField = "string";
  private Boolean booleanField = Boolean.TRUE;
  private Integer integerField = 1;
  private Double doubleField = 1d;
  private Float floatField = 1f;
  private BigDecimal bigDecimalField = BigDecimal.ONE;
  private boolean primitiveBooleanField = true;
  private int primitiveIntegerField = 1;
  private double primitiveDoubleField = 1d;
  private float primitiveFloatField = 1f;
  private Number numberField = BigInteger.valueOf(1L);
  private Nested nested = new Nested();
  private List<Nested> complexListField = Arrays.asList(new Nested(), new Nested());
  private List<Integer> simpleListField = Arrays.asList(1,2);

  public String getNullField() {
    return nullField;
  }

  public void setNullField(String nullField) {
    this.nullField = nullField;
  }

  public String getStringField() {
    return stringField;
  }

  public void setStringField(String stringField) {
    this.stringField = stringField;
  }

  public Boolean getBooleanField() {
    return booleanField;
  }

  public void setBooleanField(Boolean booleanField) {
    this.booleanField = booleanField;
  }

  public Integer getIntegerField() {
    return integerField;
  }

  public void setIntegerField(Integer integerField) {
    this.integerField = integerField;
  }

  public Double getDoubleField() {
    return doubleField;
  }

  public void setDoubleField(Double doubleField) {
    this.doubleField = doubleField;
  }

  public Float getFloatField() {
    return floatField;
  }

  public void setFloatField(Float floatField) {
    this.floatField = floatField;
  }

  public BigDecimal getBigDecimalField() {
    return bigDecimalField;
  }

  public void setBigDecimalField(BigDecimal bigDecimalField) {
    this.bigDecimalField = bigDecimalField;
  }

  public boolean isPrimitiveBooleanField() {
    return primitiveBooleanField;
  }

  public void setPrimitiveBooleanField(boolean primitiveBooleanField) {
    this.primitiveBooleanField = primitiveBooleanField;
  }

  public int getPrimitiveIntegerField() {
    return primitiveIntegerField;
  }

  public void setPrimitiveIntegerField(int primitiveIntegerField) {
    this.primitiveIntegerField = primitiveIntegerField;
  }

  public double getPrimitiveDoubleField() {
    return primitiveDoubleField;
  }

  public void setPrimitiveDoubleField(double primitiveDoubleField) {
    this.primitiveDoubleField = primitiveDoubleField;
  }

  public float getPrimitiveFloatField() {
    return primitiveFloatField;
  }

  public void setPrimitiveFloatField(float primitiveFloatField) {
    this.primitiveFloatField = primitiveFloatField;
  }

  public Number getNumberField() {
    return numberField;
  }

  public void setNumberField(Number numberField) {
    this.numberField = numberField;
  }

  public Nested getNested() {
    return nested;
  }

  public void setNested(Nested nested) {
    this.nested = nested;
  }

  public List<Nested> getComplexListField() {
    return complexListField;
  }

  public void setComplexListField(List<Nested> complexListField) {
    this.complexListField = complexListField;
  }

  public List<Integer> getSimpleListField() {
    return simpleListField;
  }

  public void setSimpleListField(List<Integer> simpleListField) {
    this.simpleListField = simpleListField;
  }
}