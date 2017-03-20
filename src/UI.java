import javax.management.*;
import java.io.File;
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

        System.out.println("Please type 1 if you want a simple query, and a 2 if you want a complex query. ");
        int query = reader.nextInt();
        String queryResult;
        if(query == 1){
            System.out.println("Simple Query");
             queryResult = simpleQuery();
        }
        else{
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
        return null;
    }

    private String complexQuery(){
        Query query1 = queryInput();
        Query query2 = queryInput();
        return null;
    }

    private Query queryInput(){
        System.out.println("Type 1 if you want to ask for a specific value of a field or 2 if you want to ask for a range of values.");
        int typeInput = reader.nextInt();
        if(typeInput == 1){
            System.out.println("Type the field you want to ask for: ");
            String field = reader.next();
            //TODO: validar que existe y devolver tipo
            Class clase = queryAdministrator.validateField();
            switch (clase.getName()){
                case "String":
                    break;
                case "Integer":
                    break;
                case "Double":
                    break;
                case "Date":
                    break;
                case "Boolean":
                    break;
            }

        }
        else{

        }
        return null;
    }

    private boolean categoryValidator(){
        return true;
    }

}
