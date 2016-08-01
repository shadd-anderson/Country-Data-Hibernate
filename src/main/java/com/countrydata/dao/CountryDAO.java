package com.countrydata.dao;

import com.countrydata.model.Country;
import org.hibernate.SessionFactory;

import java.util.List;

public interface CountryDAO {
    List<Country> allCountries();
    Country getCountryByCode(List<Country> countries, String code);
    Country getCountryByName(List<Country> countries, String name);
    void closeFactory();
    void add(Country country);
    void edit(Country country);
    void delete(Country country);
}
