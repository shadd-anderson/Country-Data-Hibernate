package com.countrydata.model;

import com.countrydata.dao.CountryDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.ArrayList;
import java.util.List;

public class DAOImplementation implements CountryDAO {
    public static final SessionFactory factory = buildSessionFactory();

    @Override
    @SuppressWarnings("unchecked")
    public List<Country> allCountries() {
        Session session = factory.openSession();
        Criteria criteria = session.createCriteria(Country.class);
        List<Country> allCountries = criteria.list();
        session.close();
        return allCountries;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public Country getCountryByCode(List<Country> countries, String code) {
        return countries.stream()
                .filter(country -> country.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Country getCountryByName(List<Country> countries, String name) {
        return countries.stream()
                .filter(country -> country.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    @Override
    public void closeFactory() {
        factory.close();
    }

    @Override
    public void add(Country country) {
        Session session = factory.openSession();
        session.beginTransaction();
        session.save(country);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void edit(Country country) {
        Session session = factory.openSession();
        session.beginTransaction();
        session.update(country);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(Country country) {
        Session session = factory.openSession();
        session.beginTransaction();
        session.delete(country);
        session.getTransaction().commit();
        session.close();
    }
}
