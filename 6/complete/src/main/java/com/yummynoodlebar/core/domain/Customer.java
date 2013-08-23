package com.yummynoodlebar.core.domain;

public class Customer {
  private String name;

  private String streetAdress;
  private String city;
  private String postalCode;

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStreetAdress() {
    return streetAdress;
  }

  public void setStreetAdress(String streetAdress) {
    this.streetAdress = streetAdress;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
