package com.countrydata.model;

import javax.persistence.*;

@Entity
public class Country {
    @Id
    private String code;
    @Column
    private String name;
    @Column
    private Double internetUsers;
    @Column
    private Double adultLiteracyRate;

    public Country() {}

    public Country(CountryBuilder builder) {
        this.code = builder.code;
        this.name = builder.name;
        this.internetUsers = builder.internetUsers;
        this.adultLiteracyRate = builder.adultLiteracyRate;
    }

    public static class CountryBuilder {
        private String code;
        private String name;
        private Double internetUsers;
        private Double adultLiteracyRate;

        public CountryBuilder(String name, String code) {
            this.name = name;
            this.code = code;
        }

        public CountryBuilder withInternetUsers(Double users) {
            internetUsers = users;
            return this;
        }

        public CountryBuilder withLiteracyRate(Double literacyRate) {
            adultLiteracyRate = literacyRate;
            return this;
        }

        public Country build() {
            return new Country(this);
        }
    }

    @Override
    public String toString() {
        String users;
        String literacy;
        if(internetUsers == null) {
            users = "--";
        } else {
            users = internetUsers.toString();
        }
        if(adultLiteracyRate == null) {
            literacy = "--";
        } else {
            literacy = adultLiteracyRate.toString();
        }
        return String.format("%-32s %-3s%20s%20s",name,code,users,literacy);
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

    public Double getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(Double internetUsers) {
        this.internetUsers = internetUsers;
    }

    public Double getAdultLiteracyRate() {
        return adultLiteracyRate;
    }

    public void setAdultLiteracyRate(Double adultLiteractyRate) {
        this.adultLiteracyRate = adultLiteractyRate;
    }
}
