package com.countrydata.model;

import javax.persistence.*;

@Entity
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String code;

    @Column
    private String name;
    @Column
    private Number internetUsers;
    @Column
    private Number adultLiteracyRate;

    public Country() {}

    @Override
    public String toString() {
        return String.format("%-32s%20.8f%20.8f",name,(Double)internetUsers,(Double)adultLiteracyRate);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getInternetUsers() {
        return (double)internetUsers;
    }

    public void setInternetUsers(double internetUsers) {
        this.internetUsers = internetUsers;
    }

    public double getAdultLiteracyRate() {
        return (double)adultLiteracyRate;
    }

    public void setAdultLiteracyRate(double adultLiteractyRate) {
        this.adultLiteracyRate = adultLiteractyRate;
    }
}
