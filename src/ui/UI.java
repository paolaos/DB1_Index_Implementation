package ui;

import query.*;
import queryAdministrator.QueryAdministrator;
import queryAdministrator.listQA;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Paola Ortega S on 3/16/2017.
 */
public class UI {
    private QueryAdministrator queryAdministrator;
    private Scanner reader;
    private boolean exit = false;
    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    public UI(){
        reader = new Scanner(System.in);
        System.out.println("Type complete file path to the .csv archive");
        String filePath = reader.next();
        File file = new File(filePath);
        while(!isValid(file)){
            System.out.println("There was an error recognizing the path. Please try again. ");
            filePath = reader.next();
            file = new File(filePath);
        }
        queryAdministrator = new listQA(file);

        while(!exit) {
            System.out.println("Please type 1 if you want a simple query or 2 if you want a complex query. ");
            int query = reader.nextInt();
            String queryResult;
            if (query == 1) {
                System.out.println("Simple query:\n");
                queryResult = simpleQuery();
            } else {
                System.out.println("Complex query:\n");
                queryResult = complexQuery();
            }
            System.out.println(queryResult);

            boolean correctAnswer = false;
            while(!correctAnswer) {
                System.out.println("Would you like to make another query? [y/n]");
                String answer = reader.next();
                if (answer.equals("n")) {
                    exit = true;
                    correctAnswer = true;
                } else if(answer.equals("y"))
                    correctAnswer = true;

                else
                    System.out.println("Invalid input. Please try again.");

            }
        }

        System.exit(0);
    }

    private boolean isValid(File spreadsheet){
        String fileName = spreadsheet.getName();
        String extension = fileName.substring(fileName.length()-4,fileName.length());
        return (spreadsheet.exists() && extension.equals(".csv"));


    }

    private String simpleQuery(){
        Query query1 = queryInput();

        List<Integer> resultantList = queryAdministrator.simpleQueryExecutor(query1);
        int[] columns = this.columnSpecificationValidator();

        return queryAdministrator.resultBuilder(columns, resultantList);
    }

    private int[] columnSpecificationValidator(){
        boolean solved = false;
        int[] finalArray = null;
        while(!solved) {
            System.out.println("Please use the format \"number,number,...\" to specify which columns you'd like to have displayed. The options go as follows: ");
            for (int i = 1; i < queryAdministrator.getFields().length; i++)
                System.out.println("\tType " + i + "to display by " + queryAdministrator.getFields()[i]); //TODO ver si esta bien el i del # con el de getFields.

            String columns = reader.next();
            String[] stringArray = columns.split(",");
            finalArray = new int[stringArray.length];
            if (stringArray.length <= queryAdministrator.getFields().length) {
                for (int i = 0; i < stringArray.length; i++) {
                    if (stringArray[i].matches("[0,9]+")) {
                        int temp = Integer.parseInt(stringArray[i]);
                        if(temp <= stringArray.length){
                            finalArray[i] = temp;
                        }
                    } else
                        throw new ArrayIndexOutOfBoundsException("Fatal error: one of the inputs doesn't match the format ");

                }
                solved = true;

            } else
                System.out.println("Invalid input. Please try again. ");

        }
        return finalArray;
    }

    private String complexQuery(){
        Query query1 = queryInput();
        Query query2 = queryInput();
        List<Integer> finalList = null;
        boolean validated = false;
        while(!validated){
            System.out.println("Please type 1 to use an AND operator, or a 0 for an OR operator");
            int operator = reader.nextInt();
            if(operator == 0) {
                finalList = queryAdministrator.complexQueryExecutor(query1, query2, false);
                validated = true;
            } else if(operator == 1){
                finalList = queryAdministrator.complexQueryExecutor(query1, query2, true);
                validated = true;
            } else
                System.out.println("Invalid input. Please try again. ");

        }

        int[] columns = this.columnSpecificationValidator();

        return queryAdministrator.resultBuilder(columns, finalList);
    }

    private Query queryInput(){
        //IF QUERY PASSES THROUGH ALL VALIDATION TESTS AND THE USER TYPES DATA CORRECTLY, THE QUERY IS ACCEPTED AND RETURNED.
        boolean isSolved = false;
        Query query = null;
        while(!isSolved) {
            System.out.println("Type 1 if you want to base your search on a specific number, or 2 if you want to search through a range of values.");
            int typeInput = reader.nextInt();
            //SPECIFIC NUMBER/ EQUALITY OR INEQUALITY:
            if (typeInput == 1) {
                System.out.println("Type the field you want to ask for: ");
                String type = reader.next();
                queryAdministrator.getFieldDataType(type);
                switch (type) {
                    case "int":
                        //USER DEFINES THE TYPE OF SPECIFIC SEARCH
                        System.out.println("Type 1 if you'd like to do an equality search (==), or 2 if you'd like to do an inequality search (!=) ");
                        int equality = reader.nextInt();
                        System.out.println("Please type the specific number. ");
                        int tempInt = reader.nextInt();
                        if(equality == 1){
                            query = new Query(QueryType.EQUALITY,type, tempInt);
                            isSolved = true;
                        } else if(equality == 2){
                            query = new Query(QueryType.INEQUALITY,type, tempInt);
                            isSolved = true;
                        } else
                            System.out.println("Invalid input. Please try again. ");

                        break;

                    case "double":
                        //USER DEFINES THE TYPE OF SPECIFIC SEARCH
                        System.out.println("Type 1 if you'd like to do an equality search (==), or 2 if you'd like to do an inequality search (!=) ");
                        equality = reader.nextInt();
                        System.out.println("Please type the specific number. ");
                        double tempDouble = reader.nextDouble();
                        if(equality == 1){
                            query = new Query(QueryType.EQUALITY,type, tempDouble);
                            isSolved = true;
                        } else if(equality == 2){
                            query = new Query(QueryType.INEQUALITY,type, tempDouble);
                            isSolved = true;
                        } else
                            System.out.println("Invalid input. Please try again. ");

                        break;

                    case "date":
                        System.out.println("Type 1 if you'd like to do an equality search (==), or 2 if you'd like to do an inequality search (!=) ");
                        equality = reader.nextInt();
                        System.out.println("Please type the specific date. ");
                        String date = reader.next();
                        Date tempDate = null;
                        //SYSTEM VALIDES IF THE FORMAT IS CORRECT.
                        try{
                            tempDate = df.parse(date);
                        } catch (ParseException exception) {
                            exception.printStackTrace();
                        }
                        if(equality == 1){
                            query = new Query(QueryType.EQUALITY,type, tempDate);
                            isSolved = true;
                        } else if(equality == 2){
                            query = new Query(QueryType.INEQUALITY,type, tempDate);
                            isSolved = true;
                        } else
                            System.out.println("Invalid input. Please try again. ");

                        break;

                    case "String":
                        System.out.println("Type 1 if you'd like to do an equality search (==), or 2 if you'd like to do an inequality search (!=) ");
                        equality = reader.nextInt();
                        System.out.println("Please type what you're looking for. ");
                        String tempString = reader.next();
                        if(equality == 1){
                            query = new Query(QueryType.EQUALITY, type, tempString);
                            isSolved = true;
                        } else if(equality == 2){
                            query = new Query(QueryType.INEQUALITY, type, tempString);
                            isSolved = true;
                        } else
                            System.out.println("Invalid input. Please try again. ");

                        break;

                    case "bool":
                        System.out.println("Type 1 if you'd like to do an equality search (==), or 2 if you'd like to do an inequality search (!=) ");
                        equality = reader.nextInt();
                        System.out.println("Please type \"true\" or \"false\". ");
                        String tempBool = reader.next();
                        if(tempBool.equals("True") || tempBool.equals("False")) {
                            if(equality == 1){
                                query = new Query(QueryType.EQUALITY, type, tempBool);
                                isSolved = true;
                            }
                            else if(equality == 2){
                                query = new Query(QueryType.INEQUALITY, type, tempBool);
                                isSolved = true;
                            }
                            else
                                System.out.println("Invalid input. Please try again. ");
                        } else
                            System.out.println("Invalid input. Please try again. ");
                        break;
                }

                //RANGE OF VALUES.
            } else if (typeInput == 2) {
                System.out.println("Type the field you want to ask for: ");
                String type = reader.next();
                queryAdministrator.getFieldDataType(type);
                switch (type) {
                    case "int":
                        System.out.println("Would you like your range to be defined by 2 numbers or by 1 number? Please type the quantity. ");
                        int quantity = reader.nextInt();
                        //RANGE DEFINED BY 1 NUMBER
                        if(quantity == 1){
                            //CONSULTS WHICH TYPE OF INEQUALITY THE USER WANTS TO CONSULT
                            System.out.println("For the inequality sign, please select a number accordingly: \n\t1: Greater than x" +
                                    "\n\t2: Greater than or equal to x \n\t3: Less than x \n\t4: Less than or equal to x");
                            int inequality = reader.nextInt();
                            //VERIFIES THE INEQUALITY SIGN
                            if(inequality <= 4 && inequality > 0){
                                System.out.println("Now that you typed the inequality, please type the fixed value (x) you want to search with");
                                int tempInt = reader.nextInt();
                                switch (inequality){
                                    case(1):
                                        query = new Query(QueryType.RANGE, type, tempInt + 1, Integer.MAX_VALUE, inequality);
                                        break;
                                    case(2):
                                        query = new Query(QueryType.RANGE, type, tempInt, Integer.MAX_VALUE, inequality);
                                        break;
                                    case(3):
                                        query = new Query(QueryType.RANGE,type, Integer.MIN_VALUE, tempInt - 1, inequality);
                                        break;
                                    case(4):
                                        query = new Query(QueryType.RANGE,type, Integer.MIN_VALUE, tempInt, inequality);
                                        break;

                                }
                                isSolved = true;
                            }

                            //RANGE DEFINED BY 2 NUMBERS
                        } else if(quantity == 2){
                            System.out.println("Please type the smallest value (x) for your range. ");
                            int minInt = reader.nextInt();
                            System.out.println("Please type the biggest value (y) for your range. ");
                            int maxInt = reader.nextInt();
                            if(minInt < maxInt) {
                                query = new Query(QueryType.RANGE, type, minInt, maxInt, 2); //no estoy segura de esto.
                                isSolved = true;
                            } else
                                System.out.println("The smallest date you entered is bigger or equal to the biggest date. Please try again.");


                        } else
                            System.out.println("Invalid number. Please try again. ");

                        break;

                    case "double":
                        System.out.println("Would you like your range to be defined by 2 numbers or by 1 number? Please type the quantity. ");
                        quantity = reader.nextInt();
                        if(quantity == 1){
                            System.out.println("For the inequality, please select a number accordingly: \n\t1: Greater than x" +
                                    "\n\t2: Greater than or equal to x \n\t3: Less than x \n\t4: Less than or equal to x");
                            int inequality = reader.nextInt();
                            if(inequality <= 4 && inequality > 0){
                                System.out.println("Now that you typed the inequality, please type the fixed value (x) you want to search with");
                                double tempDouble = reader.nextDouble();
                                switch (inequality){
                                    case(1):
                                        query = new Query(QueryType.RANGE, type, tempDouble + 0.01, Double.MAX_VALUE, inequality);
                                        break;
                                    case(2):
                                        query = new Query(QueryType.RANGE, type, tempDouble, Double.MAX_VALUE, inequality);
                                        break;
                                    case(3):
                                        query = new Query(QueryType.RANGE,type, Double.MIN_VALUE, tempDouble - 0.01, inequality);
                                        break;
                                    case(4):
                                        query = new Query(QueryType.RANGE,type, Double.MIN_VALUE, tempDouble, inequality);
                                        break;

                                }
                                isSolved = true;
                            }


                        } else if(quantity == 2){
                            System.out.println("Please type the smallest value (x) for your range. ");
                            double minDouble = reader.nextInt();
                            System.out.println("Please type the biggest value (y) for your range. ");
                            double maxDouble = reader.nextInt();
                            if(minDouble < maxDouble) {
                                query = new Query(QueryType.RANGE, type, minDouble, maxDouble, 2); //no estoy segura de esto.
                                isSolved = true;
                            } else
                                System.out.println("The smallest date you entered is bigger or equal to the biggest date. Please try again.");

                        } else
                            System.out.println("Invalid number. Please try again. ");
                        break;

                    case "date":
                        System.out.println("Would you like your range to be defined by 2 dates or by 1 date? Please type the quantity. ");
                        quantity = reader.nextInt();
                        if(quantity == 1){
                            System.out.println("For the inequality, please select a number accordingly: \n\t1: Greater than x" +
                                    "\n\t2: Greater than or equal to x \n\t3: Less than x \n\t4: Less than or equal to x");
                            int inequality = reader.nextInt();
                            if(inequality <= 4 && inequality > 0){
                                System.out.println("Now that you typed the inequality, please type the fixed date (dd/MM/yyyy) you want to search with");
                                String date = reader.next();
                                Date tempDate = null;
                                try{
                                    tempDate = df.parse(date);
                                } catch (ParseException exception) {
                                    exception.printStackTrace();
                                }
                                switch (inequality){
                                    case(1):
                                        query = new Query(QueryType.RANGE, type, this.changeDay(tempDate, 1), Double.MAX_VALUE, inequality);
                                        break;
                                    case(2):
                                        query = new Query(QueryType.RANGE, type, tempDate, new Date(), inequality);
                                        break;
                                    case(3):
                                        query = new Query(QueryType.RANGE,type, new Date(Long.MIN_VALUE), this.changeDay(tempDate, -1), inequality); //revisar
                                        break;
                                    case(4):
                                        query = new Query(QueryType.RANGE,type, Double.MIN_VALUE, tempDate, inequality);
                                        break;

                                }
                                isSolved = true;
                            }


                        } else if(quantity == 2){
                            System.out.println("Please type the smallest date (x) for your range. ");
                            String date = reader.next();
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            Date minDate = null;
                            try{
                                minDate = df.parse(date);
                            } catch (ParseException exception) {
                                exception.printStackTrace();
                            }
                            System.out.println("Please type the biggest date (y) for your range. ");
                            date = reader.next();
                            Date maxDate = null;
                            try{
                                maxDate = df.parse(date);
                            } catch (ParseException exception) {
                                exception.printStackTrace();
                            }
                            if(minDate.compareTo(maxDate) < 0) {
                                query = new Query(QueryType.RANGE, type, minDate, maxDate, 2);
                                isSolved = true;
                            } else
                                System.out.println("The smallest date you entered is bigger or equal to the biggest date. Please try again.");


                        } else
                            System.out.println("Invalid number. Please try again. ");
                        break;

                    case "String":
                    case "bool":
                        System.out.println("It is not possible to search by range with the category you selected. Please try again. ");
                        break;


                }
            }
        }

        return query;
    }

    //DISCLAIMER> I found this implementation in stack overflow in order to change the given date.
    private Date changeDay(Date date, int amount){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, amount); //minus number would decrement the days
        return cal.getTime();
    }

}
