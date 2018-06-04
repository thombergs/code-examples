package io.reflectoring.dsl;

public class Nested {

  private String stringField = "nested string";
  private Integer integerField = 42;
  private String nullField = null;

  public String getStringField() {
    return stringField;
  }

  public void setStringField(String stringField) {
    this.stringField = stringField;
  }

  public Integer getIntegerField() {
    return integerField;
  }

  public void setIntegerField(Integer integerField) {
    this.integerField = integerField;
  }

  public String getNullField() {
    return nullField;
  }

  public void setNullField(String nullField) {
    this.nullField = nullField;
  }
}
