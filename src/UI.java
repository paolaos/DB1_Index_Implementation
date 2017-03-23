import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Paola Ortega S on 3/16/2017.
 */
public class UI {
    private QueryAdministrator queryAdministrator;
    private Scanner reader;
    private boolean exit = false;
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
        queryAdministrator = new QueryAdministrator(file);

        System.out.println("Please type 1 if you want a simple query or 2 if you want a complex query. ");
        int query = reader.nextInt();
        String queryResult;
        if(query == 1){
            System.out.println("Simple Query");
             queryResult = simpleQuery();
        }
        else{
            System.out.println("Complex Query");
            queryResult = complexQuery();
        }
        System.out.println(queryResult);



    }

    private boolean isValid(File spreadsheet){
        String fileName = spreadsheet.getName();
        String extension = fileName.substring(fileName.length()-4,fileName.length());
        return (spreadsheet.exists() && extension.equals(".csv"));


    }

    private String simpleQuery(){
        Query query1 = queryInput();
        //TODO llamar al metodo respectivo en QueryAdministrator
        return null;
    }

    private String complexQuery(){
        Query query1 = queryInput();
        Query query2 = queryInput();
        boolean validated = false;
        while(!validated){
            System.out.println("Please type 0 to use an AND operator, or a 1 for an OR operator");
            int operator = reader.nextInt();
            if(operator == 0) {
                queryAdministrator.complexQueryExecutor(query1, query2, false);
                validated = true;
            } else if(operator == 1){
                queryAdministrator.complexQueryExecutor(query1, query2, true);
                validated = true;
            } else
                System.out.println("Invalid input. Please try again. ");
        }
        //TODO falta llamar al metodo respectivo y el string builder con la cantidad de cols que quiere desplegar.
        return null;
    }

    //TODO poner en el metodo el caso de que no sea igual.

    private Query queryInput(){
        boolean isSolved = false;
        Query query = null;
        while(!isSolved) {
            System.out.println("Type 1 if you want to base your search on a specific number, or 2 if you want to search through a range of values.");
            int typeInput = reader.nextInt();
            if (typeInput == 1) {
                System.out.println("Type the field you want to ask for: ");
                String type = reader.next();
                queryAdministrator.getFieldDataType(type);
                switch (type) {
                    case "int": //el enunciado dice que la persona deberia poner la categoria como int, no Integer.
                        System.out.println("Type 1 if you'd like to do an equality search (==), or 2 if you'd like to do an inequality search (!=) ");
                        int equality = reader.nextInt();
                        System.out.println("Please type the specific number. ");
                        int tempInt = reader.nextInt();
                        if(equality == 1){
                            query = new Query(Query.QueryType.EQUALITY,type, tempInt);
                            isSolved = true;
                        } else if(equality == 2){
                            query = new Query(Query.QueryType.INEQUALITY,type, tempInt);
                            isSolved = true;
                        } else
                            System.out.println("Invalid input. Please try again. ");

                        break;

                    case "double":
                        System.out.println("Type 1 if you'd like to do an equality search (==), or 2 if you'd like to do an inequality search (!=) ");
                        equality = reader.nextInt();
                        System.out.println("Please type the specific number. ");
                        double tempDouble = reader.nextDouble();
                        if(equality == 1){
                            query = new Query(Query.QueryType.EQUALITY,type, tempDouble);
                            isSolved = true;
                        } else if(equality == 2){
                            query = new Query(Query.QueryType.INEQUALITY,type, tempDouble);
                            isSolved = true;
                        } else
                            System.out.println("Invalid input. Please try again. ");

                        break;

                    case "date":
                        System.out.println("Type 1 if you'd like to do an equality search (==), or 2 if you'd like to do an inequality search (!=) ");
                        equality = reader.nextInt();
                        System.out.println("Please type the specific date. ");
                        String date = reader.next();
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        Date tempDate = null;
                        try{
                            tempDate = df.parse(date);
                        } catch (ParseException exception) {
                            exception.printStackTrace();
                        }
                        if(equality == 1){
                            query = new Query(Query.QueryType.EQUALITY,type, tempDate);
                            isSolved = true;
                        } else if(equality == 2){
                            query = new Query(Query.QueryType.INEQUALITY,type, tempDate);
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
                            query = new Query(Query.QueryType.EQUALITY,type, tempString);
                            isSolved = true;
                        } else if(equality == 2){
                            query = new Query(Query.QueryType.INEQUALITY,type, tempString);
                            isSolved = true;
                        } else
                            System.out.println("Invalid input. Please try again. ");

                        break;
                    case "bool":
                        System.out.println("Type 1 if you'd like to do an equality search (==), or 2 if you'd like to do an inequality search (!=) ");
                        equality = reader.nextInt();
                        System.out.println("Please type \"true\" or \"false\". ");
                        String tempBool = reader.next();
                        if(tempBool.equals("true") || tempBool.equals("false")) {
                            if(equality == 1){
                                query = new Query(Query.QueryType.EQUALITY,type, tempBool);
                                isSolved = true;
                            }
                            else if(equality == 2){
                                query = new Query(Query.QueryType.INEQUALITY,type, tempBool);
                                isSolved = true;
                            }
                            else
                                System.out.println("Invalid input. Please try again. ");
                        } else
                            System.out.println("Invalid input. Please try again. ");
                        break;
                }

            } else if (typeInput == 2) {
                System.out.println("Type the field you want to ask for: ");
                String type = reader.next();
                queryAdministrator.getFieldDataType(type);
                switch (type) {
                    case "int": //el enunciado dice que la persona deberia poner la categoria como int, no Integer.
                        System.out.println("Would you like your range to be defined by 2 numbers or by 1 number? Please type the quantity. ");
                        int intQuantity = reader.nextInt();
                        if(intQuantity == 1){
                            System.out.println("For the inequality, please select a number accordingly: \n\t1: Greater than x" +
                                    "\n\t2: Greater than or equal to x \n\t3: Less than x \n\t4: Less than or equal to x");
                            int inequality = reader.nextInt();
                            if(inequality <= 4 && inequality > 0){
                                System.out.println("Now that you typed the inequality, please type the fixed value (x) you want to search with");
                                int tempInt = reader.nextInt();
                                if(inequality == 1 || inequality == 2)
                                    query = new Query(Query.QueryType.RANGE, type, tempInt, Integer.MAX_VALUE, inequality);
                                else
                                    query = new Query(Query.QueryType.RANGE,type, Integer.MIN_VALUE, tempInt, inequality);

                                isSolved = true;
                            }


                        } else if(intQuantity == 2){
                            System.out.println("Please type the smallest value (x) for your range. ");
                            int minInt = reader.nextInt();
                            System.out.println("Please type the biggest value (y) for your range. ");
                            int maxInt = reader.nextInt();
                            if(minInt < maxInt) {
                                query = new Query(Query.QueryType.RANGE, type, minInt, maxInt, 2); //no estoy segura de esto.
                                isSolved = true;
                            } else
                                System.out.println("The smallest date you entered is bigger or equal to the biggest date. Please try again.");


                        } else
                            System.out.println("Invalid number. Please try again. ");

                        break;

                    case "double":
                        System.out.println("Would you like your range to be defined by 2 numbers or by 1 number? Please type the quantity. ");
                        double doubleQuantity = reader.nextDouble();
                        if(doubleQuantity == 1){
                            System.out.println("For the inequality, please select a number accordingly: \n\t1: Greater than x" +
                                    "\n\t2: Greater than or equal to x \n\t3: Less than x \n\t4: Less than or equal to x");
                            int inequality = reader.nextInt();
                            if(inequality <= 4 && inequality > 0){
                                System.out.println("Now that you typed the inequality, please type the fixed value (x) you want to search with");
                                double tempDouble = reader.nextInt();
                                if(inequality == 1 || inequality == 2)
                                    query = new Query(Query.QueryType.RANGE, type, tempDouble, Double.MAX_VALUE, inequality);
                                else
                                    query = new Query(Query.QueryType.RANGE, type, Double.MIN_VALUE, tempDouble, inequality);

                                isSolved = true;
                            }


                        } else if(doubleQuantity == 2){
                            System.out.println("Please type the smallest value (x) for your range. ");
                            double minDouble = reader.nextInt();
                            System.out.println("Please type the biggest value (y) for your range. ");
                            double maxDouble = reader.nextInt();
                            if(minDouble < maxDouble) {
                                query = new Query(Query.QueryType.RANGE, type, minDouble, maxDouble, 2); //no estoy segura de esto.
                                isSolved = true;
                            } else
                                System.out.println("The smallest date you entered is bigger or equal to the biggest date. Please try again.");


                        } else
                            System.out.println("Invalid number. Please try again. ");
                        break;

                    case "date":
                        System.out.println("Would you like your range to be defined by 2 dates or by 1 date? Please type the quantity. ");
                        int quantity = reader.nextInt();
                        if(quantity == 1){
                            System.out.println("For the inequality, please select a number accordingly: \n\t1: Greater than x" +
                                    "\n\t2: Greater than or equal to x \n\t3: Less than x \n\t4: Less than or equal to x");
                            int inequality = reader.nextInt();
                            if(inequality <= 4 && inequality > 0){
                                System.out.println("Now that you typed the inequality, please type the fixed date (dd/MM/yyyy) you want to search with");
                                String date = reader.next();
                                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                Date tempDate = null;
                                try{
                                    tempDate = df.parse(date);
                                } catch (ParseException exception) {
                                    exception.printStackTrace();
                                }
                                if(inequality == 1 || inequality == 2)
                                    query = new Query(Query.QueryType.RANGE, type, tempDate, new Date(), inequality); //revisar luego
                                else
                                    query = new Query(Query.QueryType.RANGE, type, new Date(00/00/0000), tempDate, inequality);

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
                                query = new Query(Query.QueryType.RANGE, type, minDate, maxDate, 2); //no estoy segura de esto.
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


}
