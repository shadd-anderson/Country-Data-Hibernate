package com.countrydata;

import com.countrydata.model.Country;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static com.countrydata.Calculations.CorrelationCoefficient;

public class Application {
    private static Map<Integer, String> menu = new HashMap<>();
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private static final SessionFactory factory = buildSessionFactory();

    public static void main(String[] args) {
        menu.put(1,"View all data");
        menu.put(2,"View maximum and minimum values for literacy rate");
        menu.put(3,"View maximum and minimum values for internet usage");
        menu.put(4,"View the correlation coefficient between literacy rate and internet usage");
        menu.put(5,"Edit a country's data");
        menu.put(6,"Add a country");
        menu.put(7,"Delete a country");
        menu.put(8,"Quit");
        getUserChoice();
    }

    private static void getUserChoice() {
        System.out.println("Welcome, user! Please select an option from the list below:");
        for(Map.Entry<Integer, String> option: menu.entrySet()) {
            System.out.printf("%d) %s%n",option.getKey(),option.getValue());
        }
        int choice = 0;
        do {
            choice = Integer.parseInt(readLine().replaceAll("[\\D]", ""));
            switch (choice) {
                case 1:
                    printData();
                    enter();
                    break;
//                case 2:
//                    maxMin("literacy");
//                    enter();
//                    break;
//                case 3:
//                    maxMin("internet");
//                    enter();
//                    break;
//                case 4:
//                    correlationCoefficient();
//                    enter();
//                    break;
//                case 5:
//                    editCountry();
//                    enter();
//                    break;
//                case 6:
//                    addCountry();
//                    enter();
//                    break;
//                case 7:
//                    deleteCountry();
//                    enter();
//                    break;
                default:
                    System.out.println("Please enter a valid choice.");
                    enter();
                    break;
            }
        } while(choice != 8);
        factory.close();
    }

    @SuppressWarnings("unchecked")
    private static void printData() {
        Session session = factory.openSession();
        Criteria criteria = session.createCriteria(Country.class);
        List<Country> allCountries = criteria.list();
        session.close();
        System.out.println("Country name____________________Internet Users______Literacy Rate");
        allCountries.forEach(System.out::println);
    }

    private static void enter() {
        System.out.println("Please press enter to continue");
        readLine();
    }

    private static String readLine() {
        String result = "";
        try {
            result = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }
}