package com.countrydata;

import com.countrydata.dao.CountryDAO;
import com.countrydata.model.Country;
import com.countrydata.model.Country.CountryBuilder;
import com.countrydata.model.DAOImplementation;
import org.hibernate.SessionFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static com.countrydata.Calculations.CorrelationCoefficient;

public class Application {
    private static Map<Integer, String> menu = new HashMap<>();
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static CountryDAO dao = new DAOImplementation();
    private static List<Country> allCountries = dao.allCountries();
    private static List<Double> literacyRates;
    private static List<Double> internetUsage;

    public static void main(String[] args) {
        menu.put(1,"View all data");
        menu.put(2,"View maximum and minimum values for literacy rate");
        menu.put(3,"View maximum and minimum values for internet usage");
        menu.put(4,"View the correlation coefficient between literacy rate and internet usage");
        menu.put(5,"Edit a country's data");
        menu.put(6,"Add a country");
        menu.put(7,"Delete a country");
        menu.put(8,"Quit");
        literacyRates = new ArrayList<>();
        internetUsage = new ArrayList<>();
        allCountries.forEach(country -> literacyRates.add(country.getAdultLiteracyRate()));
        allCountries.forEach(country -> internetUsage.add(country.getInternetUsers()));
        run();
    }

    private static void run() {
        int choice = 0;
        do {
            System.out.println("Welcome, user! Please select the number of an option from the list below:");
            for(Map.Entry<Integer, String> option: menu.entrySet()) {
                System.out.printf("%d) %s%n",option.getKey(),option.getValue());
            }
            choice = Integer.parseInt(readLine().replaceAll("[\\D]", ""));
            switch (choice) {
                case 1:
                    printData();
                    enter();
                    break;
                case 2:
                    maxMin("literacy rate");
                    enter();
                    break;
                case 3:
                    maxMin("internet usage");
                    enter();
                    break;
                case 4:
                    correlationCoefficient();
                    enter();
                    break;
                case 5:
                    editCountry();
                    enter();
                    break;
                case 6:
                    addCountry();
                    enter();
                    break;
                case 7:
                    deleteCountry();
                    enter();
                    break;
                case 8:
                    System.out.println("Please come again!");
                    dao.closeFactory();
                    break;
                default:
                    System.out.println("Please enter a valid choice.");
                    enter();
                    break;
            }
        } while(choice != 8);
    }

    private static void deleteCountry() {
        System.out.println("Please enter the country code for the country you would like to delete:");
        String code = readLine().toUpperCase();
        Country country = dao.getCountryByCode(allCountries, code);
        System.out.printf("Are you sure you'd like to delete %s and all its information? (type yes to confirm)%n",country.getName());
        String confirm = readLine().toLowerCase();
        if(!confirm.equals("yes")) {
            System.out.printf("%s not deleted.%n",country.getName());
        } else {
            dao.delete(country);
            System.out.printf("%s deleted successfully.%n",country.getName());
            allCountries = dao.allCountries();
        }
    }

    private static void editCountry() {
        System.out.println("Please enter the code of the country you would like to edit:");
        String code = readLine();
        Country country = dao.getCountryByCode(allCountries,code);
        if (country == null) {
            System.out.println("Country does not exist. Please enter a valid country code.");
        } else {
            Double internetUsage;
            Double literacyRate;
            int choice = 0;
            do {
                System.out.printf("Please enter the number of the value you would like to change for %s:%n",country.getName());
                System.out.println("1) Internet Usage");
                System.out.println("2) Adult Literacy Rate");
                System.out.println("3) Finished editing");
                choice = Integer.parseInt(readLine().replaceAll("[\\D]",""));
                switch (choice) {
                    case 1:
                        System.out.printf("Please enter the new Internet Usage Rate for %s:%n",country.getName());
                        internetUsage = Double.valueOf(readLine());
                        country.setInternetUsers(internetUsage);
                        System.out.println("New Internet Usage Rate captured");
                        break;
                    case 2:
                        System.out.printf("Please enter the new Adult Literacy Rate for %s:%n",country.getName());
                        literacyRate = Double.valueOf(readLine());
                        country.setAdultLiteracyRate(literacyRate);
                        System.out.println("New Adult Literacy Rate captured");
                        break;
                    case 3:
                        break;
                    default:
                        System.out.println("Please enter the number of the choice you would like to make");
                }
            } while (choice != 3);
            dao.edit(country);
            System.out.printf("%s updated successfully!%n",country.getName());
            allCountries = dao.allCountries();
        }
    }

    private static void addCountry() {
        Double internetUsage = null;
        Double literacyRate = null;
        String code = "";
        System.out.println("Please enter the name of the country you would like to add:");
        String name = readLine();
        if(dao.getCountryByName(allCountries,name) != null) {
            System.out.printf("%s already has information. Please select 'Edit a country's data' from the menu.%n",name);
        } else {
            System.out.printf("Please enter the country code of %s:%n",name);
            do {
                code = readLine().toUpperCase();
                if (code.length() != 3) {
                    System.out.println("Country code must be 3 characters. Please enter a 3 character code.");
                }
                if(dao.getCountryByCode(allCountries, code) != null) {
                    System.out.printf("There is already a country with %s as a country code. " +
                            "Please enter a different one:%n",code);
                }
            } while (code.length() != 3 || dao.getCountryByCode(allCountries, code) != null);
            System.out.printf("Enter the internet usage rate for %s, or type 'na' if no information%n",name);
            String internet = readLine().toLowerCase();
            switch(internet) {
                case "na":
                    break;
                default:
                    internetUsage = Double.valueOf(internet);
                    break;
            }
            System.out.printf("Enter the literacy rate for %s, or type 'na' if no information%n",name);
            String literacy = readLine().toLowerCase();
            switch(literacy) {
                case "na":
                    break;
                default:
                    literacyRate = Double.valueOf(literacy);
                    break;
            }
            if(literacyRate != null && internetUsage != null) {
                Country country = new CountryBuilder(name,code)
                                    .withInternetUsers(internetUsage)
                                    .withLiteracyRate(literacyRate)
                                    .build();
                dao.add(country);
            } else if(literacyRate == null && internetUsage != null) {
                Country country = new CountryBuilder(name,code)
                                    .withInternetUsers(internetUsage)
                                    .build();
                dao.add(country);
            } else if(literacyRate != null) {
                Country country = new CountryBuilder(name,code)
                                    .withLiteracyRate(literacyRate)
                                    .build();
                dao.add(country);
            } else {
                Country country = new CountryBuilder(name,code)
                                    .build();
                dao.add(country);
            }
            System.out.printf("%s added to list of countries succesfully.%n",name);
            allCountries = dao.allCountries();
        }
    }

    private static void correlationCoefficient() {
        System.out.printf("r = %.6f%n", CorrelationCoefficient(literacyRates,internetUsage));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private static void maxMin(String column) {
        Country max;
        Country min;

        if(column.equals("literacy rate")){
            max = allCountries
                    .stream()
                    .filter(country -> country.getAdultLiteracyRate() != null)
                    .max((o1, o2) -> o1.getAdultLiteracyRate().compareTo(o2.getAdultLiteracyRate()))
                    .get();
            min = allCountries
                    .stream()
                    .filter(country -> country.getAdultLiteracyRate() != null)
                    .min((o1, o2) -> o1.getAdultLiteracyRate().compareTo(o2.getAdultLiteracyRate()))
                    .get();
        } else {
            max = allCountries
                    .stream()
                    .filter(country -> country.getInternetUsers() != null)
                    .max((o1, o2) -> o1.getInternetUsers().compareTo(o2.getInternetUsers()))
                    .get();
            min = allCountries
                    .stream()
                    .filter(country -> country.getInternetUsers() != null)
                    .min((o1, o2) -> o1.getInternetUsers().compareTo(o2.getInternetUsers()))
                    .get();
        }

        System.out.printf("Max %s: %s, %s.%nMinimum %s: %s, %s.%n",
                column,max.getName(),
                (column.equals("literacy rate"))? max.getAdultLiteracyRate() : max.getInternetUsers(),
                column,min.getName(),
                (column.equals("literacy rate"))? min.getAdultLiteracyRate() : min.getInternetUsers());
    }

    @SuppressWarnings("unchecked")
    private static void printData() {
        System.out.println("Country name______________Country Code____Internet Users_______Literacy Rate");
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
}