package com.countrydata;

import java.util.List;

/*A number of calculations used - in particular - for the correlation coefficient. The calculations ignore corresponding
sets that have any null values. For example, consider a list of coordinates as such:

    (1, 2)
    (3, 4)
    (5, null)
    (6, 7)
    (null, 8)

The 3rd and 5th set of coordinates would be ignored.*/

public class Calculations {
    /*Calculates the mean of a set of values, ignoring values that have a corresponding value of null*/
    public static double adjustedMean(List<Double> numbers, List<Double> corresponding) {
        double sum = 0;
        double countable = 0;
        for(Double number: numbers) {
            if(number != null && corresponding.get(numbers.indexOf(number)) != null) {
                sum += number;
                countable++;
            } else {}
        }
        return sum/countable;
    }

    /*Calculates the standard deviation of a set of values, ignoring values that have a corresponding value of null*/
    public static double adjustedStandardDeviation(List<Double> numbers, List<Double> corresponding) {
        double x = 0;
        double countable = 0;
        for(Double number: numbers) {
            if(number != null && corresponding.get(numbers.indexOf(number)) != null) {
                x += ((number - adjustedMean(numbers,corresponding)) * (number - adjustedMean(numbers,corresponding)));
                countable++;
            } else {}
        }
        double y = x/(countable-1);
        return Math.sqrt(y);
    }

    /*Used to calculate the correlation coefficient. Split from correlation coefficient calculation for clarity*/
    public static double individualZ(List<Double> a, List<Double> b) {
        double x = 0;
        for(int i = 0; i<a.size(); i++) {
            if(a.get(i) == null || b.get(i) == null){} else {
                double y = (a.get(i) - adjustedMean(a,b)) / adjustedStandardDeviation(a,b);
                double z = (b.get(i) - adjustedMean(b,a)) / adjustedStandardDeviation(b,a);
                x += y * z;
            }
        }
        return x;
    }

    public static double numberOfNulls(List<Double> a) {
        double x = 0;
        for(Double d: a) {
            if(d == null) {
                x++;
            }
        }
        return x;
    }

    public static double CorrelationCoefficient(List<Double> a, List<Double> b) {
        assert a.size() == b.size();
        List<Double> mostNull;
        if(numberOfNulls(a)<=numberOfNulls(b)) { mostNull = b; } else { mostNull = a; }
        return individualZ(a,b)/(mostNull.size()-numberOfNulls(mostNull)-1);
    }
}