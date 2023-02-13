package hr.fer.oprpp1.hw04.db;

import java.security.InvalidParameterException;

/**
 * models a specific type of operator: <, <=, >, >=,.... for string comparison
 *
 */
public class ComparisonOperators {

    public static final IComparisonOperator LESS  = (value1, value2) -> {

        if(value1.compareTo(value2) < 0){
            return true;
        }

        return false;
    };

    public static final IComparisonOperator LESS_OR_EQUALS  = (value1, value2) -> {

        if(value1.compareTo(value2) <= 0){
            return true;
        }

        return false;
    };

    public static final IComparisonOperator GREATER  = (value1, value2) -> {

        if(value1.compareTo(value2) > 0){
            return true;
        }

        return false;
    };


    public static final IComparisonOperator GREATER_OR_EQUALS  = (value1, value2) -> {

        if(value1.compareTo(value2) >= 0){
            return true;
        }

        return false;
    };


    public static final IComparisonOperator EQUALS  = (value1, value2) -> {

        if(value1.compareTo(value2) == 0){
            return true;
        }

        return false;
    };


    public static final IComparisonOperator NOT_EQUALS  = (value1, value2) -> {

        if(value1.compareTo(value2) != 0){
            return true;
        }

        return false;
    };


    public static final IComparisonOperator LIKE  = (value1, value2) -> {

        int i = 0;
        int cnt = 0;
        while(cnt < value2.length()){
            if(value2.charAt(cnt) == '*') i++;
            cnt++;
        }

        if(i != 0){
            if(i > 1) throw new InvalidParameterException("More than one wildcard members is not allowed");

            //has only one *
            if(value2.charAt(0) == '*'){
                String str = value2.substring(1);
                return value1.endsWith(str);
            }else if(value2.charAt(value2.length()-1) == '*'){
                String str = value2.substring(0, value2.length()-1);
                return value1.startsWith(str);
            }else{
                //* is in the middle
                String[] strings = value2.split("\\*");
                if(strings[0].length() + strings[1].length() > value1.length()) return false;
                return value1.startsWith(strings[0]) && value1.endsWith(strings[1]);

            }

        }else{
            return value1.equals(value2);
        }


    };




}
