package ui;

import query.*;
import queryAdministrator.QueryAdministrator;
import queryAdministrator.indexedQA;
import queryAdministrator.listQA;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * This front-end class interacts with the user, validates every single input and makes its
 * respective method calls in order to run the program fluently.
 */
public class UI {
    private QueryAdministrator queryAdministrator;
    private Scanner reader;
    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * The class constructor. Asks and validates the user-defined file path, finds the path
     * and initializes a new queryAdministrator with the given file. It also asks for the type
     * of query a user wants and if it wants to make another query once the previous one is
     * done.
     *
     * @throws IOException corresponding to simple and complex queries.
     */
    public UI() throws IOException {
        boolean exit = false;
        boolean validatedAnswer = false;
        reader = new Scanner(System.in);
        System.out.println("Type complete file path to the .csv archive");
        String filePath = reader.next();
        File file = new File(filePath);
        while (!isValid(file)) {
            System.out.println("There was an error recognizing the path. Please try again. \n");
            filePath = reader.next();
            file = new File(filePath);
        }

        while (!validatedAnswer) {
            System.out.println("Type 1 if you'd like to use our inefficient version, or 2 for our efficient version. ");
            int number = reader.nextInt();
            if (number == 1) {
                queryAdministrator = new listQA(file);
                validatedAnswer = true;
            } else if (number == 2) {
                queryAdministrator = new indexedQA(file);
                validatedAnswer = true;
            } else {
                System.out.println("Invalid input. Please try again. ");
            }
        }

        while (!exit) {
            validatedAnswer = false;
            String resultantQuery = "";
            while (!validatedAnswer) {
                System.out.println("Please type 1 if you want a simple query or 2 if you want a complex query. ");
                int query = reader.nextInt();
                if (query == 1) {
                    System.out.println("Simple query:\n");
                    resultantQuery = this.simpleQuery();
                    validatedAnswer = true;
                } else if (query == 2) {
                    System.out.println("Complex query:\n");
                    resultantQuery = this.complexQuery();
                    validatedAnswer = true;
                } else
                    System.out.println("Not a valid input. Please try again. \n");
            }

            System.out.println(resultantQuery);
            validatedAnswer = false;
            while (!validatedAnswer) {
                System.out.println("Would you like to make another query? [y/n]");
                String answer = reader.next();
                if (answer.equals("n")) {
                    exit = true;
                    validatedAnswer = true;
                } else if (answer.equals("y"))
                    validatedAnswer = true;

                else
                    System.out.println("Invalid input. Please try again.");

            }
        }

        System.exit(0);
    }

    /**
     * Verifies if the file sent by the user is accessible and has a ".csv" extension.
     *
     * @param spreadsheet file to be validated
     * @return true if it is acceptable, false otherwise.
     */
    private boolean isValid(File spreadsheet) {
        String fileName = spreadsheet.getName();
        String extension = fileName.substring(fileName.length() - 4, fileName.length());
        return (spreadsheet.exists() && extension.equals(".csv"));


    }

    /**
     * Treats the case of a user-defined simple query. Calls for a new query, sends
     * the query for execution, retrieves the resultant list, sends the user to select
     * the columns to be displayed and finally returns the result.
     *
     * @return a String containing the user-defined columns of the user-defined query.
     * @throws IOException obtained straight from the resultBuilder method
     */
    private String simpleQuery() throws IOException {
        Query query1 = queryInput();

        List<Integer> resultantList = queryAdministrator.simpleQueryExecutor(query1);
        int[] columns = this.columnSpecificationValidator();

        return queryAdministrator.resultBuilder(columns, resultantList);
    }

    /**
     * Interacts with the user in order to define which columns are going to be displayed in the
     * resultant query and validates the user's answer through casting and regex expressions. If
     * the user input is not redacted properly, UI rejects the answer and asks the user to try
     * again.
     *
     * @return the correctly validated fields that the user wants to get displayed.
     */
    private int[] columnSpecificationValidator() {
        boolean solved = false;
        int[] finalArray = null;
        while (!solved) {
            System.out.println("Please use the format \"number,number,...\" to specify which columns you'd like to have displayed. The options go as follows: ");
            for (int i = 0; i < queryAdministrator.getFields().length; i++)
                System.out.println("\tType " + (i + 1) + " to display by " + queryAdministrator.getFields()[i]); //TODO ver si esta bien el i del # con el de getFields.

            String columns = reader.next();
            String[] stringArray = columns.split(",");
            finalArray = new int[stringArray.length];
            if (stringArray.length <= queryAdministrator.getFields().length) {
                for (int i = 0; i < stringArray.length; i++) {
                    if (stringArray[i].matches("[0-9]+")) {
                        int temp = Integer.parseInt(stringArray[i]);
                        if (temp - 1 <= queryAdministrator.getRows()) {
                            finalArray[i] = (temp - 1);
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

    /**
     * Treats the case of a user-defined complex query. Calls for two new querie, sends
     * both queries for execution, retrieves the resultant lists, asks the user whether they want
     * conjunction or disjunction between the results, sends the user to define the columns to be
     * displayed and finally returns the result.
     *
     * @return a String containing the user-defined columns of the user-defined complex query.
     * @throws IOException handled by the resultBuilder method.
     */
    private String complexQuery() throws IOException {
        System.out.println("First query: ");
        Query query1 = queryInput();
        System.out.println("\nSecond query: ");
        Query query2 = queryInput();
        List<Integer> finalList = null;
        boolean validated = false;
        while (!validated) {
            System.out.println("Please type 1 to use an AND operator, or a 0 for an OR operator");
            int operator = reader.nextInt();
            if (operator == 0) {
                finalList = queryAdministrator.complexQueryExecutor(query1, query2, false);
                validated = true;
            } else if (operator == 1) {
                finalList = queryAdministrator.complexQueryExecutor(query1, query2, true);
                validated = true;
            } else
                System.out.println("Invalid input. Please try again. ");

        }

        int[] columns = this.columnSpecificationValidator();

        return queryAdministrator.resultBuilder(columns, finalList);
    }

    /**
     * Interacts with the user through the process of generating a query, validates every single
     * input by the user and builds the necessary parameters in order to create a new query and
     * send it to be executed. The method is mainly divided into two sections: a search based on
     * an exact value or a search based on a range of values. Each of these sections asks and vali-
     * dates based on the field the user asks for. Everything is held inside a loop in order to
     * start all over again in case the user types an invalid input.
     *
     * @return the newly generated query with all its validated parameters.
     */
    private Query queryInput() {
        //IF QUERY PASSES THROUGH ALL VALIDATION TESTS AND THE USER TYPES DATA CORRECTLY, THE QUERY IS ACCEPTED AND RETURNED.
        boolean isSolved = false;
        Query query = null;
        while (!isSolved) {
            System.out.println("Type 1 if you want to make your search based on a specific value, or 2 if you want to search through a range of values.");
            int typeInput = reader.nextInt();
            //SPECIFIC NUMBER/ EQUALITY OR INEQUALITY:
            if (typeInput == 1) {
                System.out.println("Type the field you want to ask for: ");
                System.out.println(queryAdministrator.showFields());
                String field = reader.next();
                String type = queryAdministrator.getFieldDataType(field);
                switch (type) {
                    case "int":
                        //USER DEFINES THE TYPE OF SPECIFIC SEARCH
                        System.out.println("Type 1 if you'd like to do an equality search (==), or 2 if you'd like to do an inequality search (!=) ");
                        int equality = reader.nextInt();
                        System.out.println("Please type the specific number. ");
                        int tempInt = reader.nextInt();
                        if (equality == 1) {
                            query = new Query(QueryType.EQUALITY, field, tempInt);
                            isSolved = true;
                        } else if (equality == 2) {
                            query = new Query(QueryType.INEQUALITY, field, tempInt);
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
                        if (equality == 1) {
                            query = new Query(QueryType.EQUALITY, field, tempDouble);
                            isSolved = true;
                        } else if (equality == 2) {
                            query = new Query(QueryType.INEQUALITY, field, tempDouble);
                            isSolved = true;
                        } else
                            System.out.println("Invalid input. Please try again. ");

                        break;

                    case "date":
                        System.out.println("Type 1 if you'd like to do an equality search (==), or 2 if you'd like to do an inequality search (!=) ");
                        equality = reader.nextInt();
                        System.out.println("Please type the specific date (Format: dd/MM/yyyy) ");
                        String date = reader.next();
                        Date tempDate = null;
                        //SYSTEM VALIDES IF THE FORMAT IS CORRECT.
                        try {
                            tempDate = df.parse(date);
                        } catch (ParseException exception) {
                            exception.printStackTrace();
                        }
                        if (equality == 1) {
                            query = new Query(QueryType.EQUALITY, field, tempDate);
                            isSolved = true;
                        } else if (equality == 2) {
                            query = new Query(QueryType.INEQUALITY, field, tempDate);
                            isSolved = true;
                        } else
                            System.out.println("Invalid input. Please try again. ");

                        break;

                    case "String":
                        System.out.println("Type 1 if you'd like to do an equality search (==), or 2 if you'd like to do an inequality search (!=) ");
                        equality = reader.nextInt();
                        System.out.println("Please type what you're looking for. ");
                        String tempString = reader.next();
                        if (equality == 1) {
                            query = new Query(QueryType.EQUALITY, field, tempString);
                            isSolved = true;
                        } else if (equality == 2) {
                            query = new Query(QueryType.INEQUALITY, field, tempString);
                            isSolved = true;
                        } else
                            System.out.println("Invalid input. Please try again. ");

                        break;

                    case "bool":
                        System.out.println("Type 1 if you'd like to do an equality search (==), or 2 if you'd like to do an inequality search (!=) ");
                        equality = reader.nextInt();
                        System.out.println("Please type \"True\" or \"False\". ");
                        String tempBool = reader.next();
                        if (tempBool.equals("True") || tempBool.equals("False")) {
                            if (equality == 1) {
                                query = new Query(QueryType.EQUALITY, field, tempBool);
                                isSolved = true;
                            } else if (equality == 2) {
                                query = new Query(QueryType.INEQUALITY, field, tempBool);
                                isSolved = true;
                            } else
                                System.out.println("Invalid input. Please try again. ");
                        } else
                            System.out.println("Invalid input. Please try again. ");
                        break;
                }

                //RANGE OF VALUES.
            } else if (typeInput == 2) {
                System.out.println("Type the field you want to ask for: ");
                System.out.println(queryAdministrator.showFields());
                String field = reader.next();
                String type = queryAdministrator.getFieldDataType(field);
                switch (type) {
                    case "int":
                        System.out.println("Would you like your range to be defined by 1 or 2 numbers? Please type the quantity. ");
                        int quantity = reader.nextInt();
                        //RANGE DEFINED BY 1 NUMBER
                        if (quantity == 1) {
                            //CONSULTS WHICH TYPE OF INEQUALITY THE USER WANTS TO CONSULT
                            System.out.println("For the inequality sign, please select a number accordingly: \n\t1: Greater than x" +
                                    "\n\t2: Greater than or equal to x \n\t3: Less than x \n\t4: Less than or equal to x");
                            int inequality = reader.nextInt();
                            //VERIFIES THE INEQUALITY SIGN
                            if (inequality <= 4 && inequality > 0) {
                                System.out.println("Now that you typed the inequality, please type the fixed value (x) you want to search with");
                                int tempInt = reader.nextInt();
                                switch (inequality) {
                                    case (1):
                                        query = new Query(QueryType.RANGE, field, tempInt + 1, Integer.MAX_VALUE);
                                        break;
                                    case (2):
                                        query = new Query(QueryType.RANGE, field, tempInt, Integer.MAX_VALUE);
                                        break;
                                    case (3):
                                        query = new Query(QueryType.RANGE, field, Integer.MIN_VALUE, tempInt - 1);
                                        break;
                                    case (4):
                                        query = new Query(QueryType.RANGE, field, Integer.MIN_VALUE, tempInt);
                                        break;

                                }
                                isSolved = true;
                            }

                            //RANGE DEFINED BY 2 NUMBERS
                        } else if (quantity == 2) {
                            System.out.println("Please type the smallest value (x) for your range. ");
                            int minInt = reader.nextInt();
                            System.out.println("Please type the biggest value (y) for your range. ");
                            int maxInt = reader.nextInt();
                            if (minInt < maxInt) {
                                query = new Query(QueryType.RANGE, field, minInt, maxInt); //no estoy segura de esto.
                                isSolved = true;
                            } else
                                System.out.println("The smallest date you entered is bigger or equal to the biggest date. Please try again.");


                        } else
                            System.out.println("Invalid number. Please try again. ");

                        break;

                    case "double":
                        System.out.println("Would you like your range to be defined by 2 numbers or by 1 number? Please type the quantity. ");
                        quantity = reader.nextInt();
                        if (quantity == 1) {
                            System.out.println("For the inequality, please select a number accordingly: \n\t1: Greater than x" +
                                    "\n\t2: Greater than or equal to x \n\t3: Less than x \n\t4: Less than or equal to x");
                            int inequality = reader.nextInt();
                            if (inequality <= 4 && inequality > 0) {
                                System.out.println("Now that you typed the inequality, please type the fixed value (x) you want to search with");
                                double tempDouble = reader.nextDouble();
                                switch (inequality) {
                                    case (1):
                                        query = new Query(QueryType.RANGE, field, tempDouble + 0.01, Double.MAX_VALUE);
                                        break;
                                    case (2):
                                        query = new Query(QueryType.RANGE, field, tempDouble, Double.MAX_VALUE);
                                        break;
                                    case (3):
                                        query = new Query(QueryType.RANGE, field, Double.MIN_VALUE, tempDouble - 0.01);
                                        break;
                                    case (4):
                                        query = new Query(QueryType.RANGE, field, Double.MIN_VALUE, tempDouble);
                                        break;

                                }
                                isSolved = true;
                            }


                        } else if (quantity == 2) {
                            System.out.println("Please type the smallest value (x) for your range. ");
                            double minDouble = reader.nextInt();
                            System.out.println("Please type the biggest value (y) for your range. ");
                            double maxDouble = reader.nextInt();
                            if (minDouble < maxDouble) {
                                query = new Query(QueryType.RANGE, field, minDouble, maxDouble); //no estoy segura de esto.
                                isSolved = true;
                            } else
                                System.out.println("The smallest date you entered is bigger or equal to the biggest date. Please try again.");

                        } else
                            System.out.println("Invalid number. Please try again. ");
                        break;

                    case "date":
                        System.out.println("Would you like your range to be defined by 2 dates or by 1 date? Please type the quantity. ");
                        quantity = reader.nextInt();
                        if (quantity == 1) {
                            System.out.println("For the inequality, please select a number accordingly: \n\t1: Younger than x" +
                                    "\n\t2: Younger than or equal to x \n\t3: Older than x \n\t4: Older than or equal to x");
                            int inequality = reader.nextInt();
                            if (inequality <= 4 && inequality > 0) {
                                System.out.println("Now that you typed the inequality, please type the fixed date (dd/MM/yyyy) you want to search with");
                                String date = reader.next();
                                Date tempDate = null;
                                try {
                                    tempDate = df.parse(date);
                                } catch (ParseException exception) {
                                    exception.printStackTrace();
                                }
                                switch (inequality) {
                                    case (1):
                                        query = new Query(QueryType.RANGE, field, this.changeDay(tempDate, 1), new Date(Long.MAX_VALUE));
                                        break;
                                    case (2):
                                        query = new Query(QueryType.RANGE, field, tempDate, new Date());
                                        break;
                                    case (3):
                                        query = new Query(QueryType.RANGE, field, new Date(Long.MIN_VALUE), this.changeDay(tempDate, -1)); //revisar
                                        break;
                                    case (4):
                                        query = new Query(QueryType.RANGE, field, new Date(Long.MIN_VALUE), tempDate);
                                        break;

                                }
                                isSolved = true;
                            }


                        } else if (quantity == 2) {
                            System.out.println("Please type the smallest date (x) for your range. ");
                            String date = reader.next();
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            Date minDate = null;
                            try {
                                minDate = df.parse(date);
                            } catch (ParseException exception) {
                                exception.printStackTrace();
                            }
                            System.out.println("Please type the biggest date (y) for your range. ");
                            date = reader.next();
                            Date maxDate = null;
                            try {
                                maxDate = df.parse(date);
                            } catch (ParseException exception) {
                                exception.printStackTrace();
                            }
                            if (minDate.compareTo(maxDate) < 0) {
                                query = new Query(QueryType.RANGE, field, minDate, maxDate);
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

    /**
     * Increases or decreases a given date by an exact amount of dates.
     * DISCLAIMER> I (B55204) based this method on a stack overflow thread in order to
     * consider inclusion/exclusion cases from whenever a user asks for a range of dates
     * in the queryInput method.
     *
     * @param date   the date that the user wants to alter.
     * @param amount the exact amount of days to increase/decrease from the original date.
     * @return the newly modified date
     */
    private Date changeDay(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, amount); //minus number would decrement the days
        return cal.getTime();
    }

}
