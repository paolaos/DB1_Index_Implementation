package queryAdministrator;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import ui.UI;
import query.*;

/**
 * Created by Paola Ortega S on 3/16/2017.
 */
public abstract class QueryAdministrator<T>{
    //Version ineficiente
    protected List<LinkedList> data;
    protected UI ui;
    protected File file;
    public int rows;
    protected int columns;
    protected boolean validFile;
    protected String[] fields;
    protected String[] fieldsDataTypes;
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");



    public void validateDataTypes() throws IOException {
        BufferedReader reader = null;
        String categories = null;
        String types = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            categories = reader.readLine();
            categories = categories.replaceAll("\"","");
            types = reader.readLine();

        } catch(IOException exception) {
            exception.printStackTrace();
        }

        fields = categories.split(",");
        fieldsDataTypes = types.split(",");
        //secciono en dos arreglos. Uno de categorías (primera línea) y el otro de tipos (segunda línea).

        columns = fields.length;
        if(fields.length != fieldsDataTypes.length)
            throw new StringIndexOutOfBoundsException("The amount of categories do not match with the amount of data types. Please check the file and try again later. ");
        //valido que sean del mismo tamaño

        for(int i = 0; i < fieldsDataTypes.length; i++){
            if(!(fieldsDataTypes[i].equals("String") || fieldsDataTypes[i].equals("int")
                    || fieldsDataTypes[i].equals("double") || fieldsDataTypes[i].equals("date")
                    || fieldsDataTypes[i].equals("bool")))
                throw new StringIndexOutOfBoundsException("One of the data types isn't recognized by the program. Please check the file and try again. ");
        }
        //valido que sean de un tipo aceptable
        reader.close();
        validFile = true;
    }


    public String getFieldDataType(String category){
        for(int i = 0; i < getFields().length; i++){
            if(fields[i].equals(category))
                return fieldsDataTypes[i];

        }

        throw new IndexOutOfBoundsException("Invalid category. ");

    }

    public List<Integer> simpleQueryExecutor(Query query){
        if(query.getQueryType() == QueryType.EQUALITY || query.getQueryType() == QueryType.INEQUALITY)
            return simpleEqualityQueryExecutor(query);

        return simpleRangeQueryExecutor(query);
    }

    public  List<Integer> complexQueryExecutor(Query query1, Query query2, boolean isDisjunctive){
        System.out.println("First query:\n"+query1);
        List<Integer> result1 = simpleQueryExecutor(query1);
        if(isDisjunctive) System.out.println("AND");
        else {System.out.println("OR");}
        System.out.println("Second query:\n"+query2+"\n");
        List<Integer> result2 = simpleQueryExecutor(query2);

        List<Integer> finalResult = new LinkedList<>();
        Iterator<Integer> it1 = result1.iterator();
        if(isDisjunctive){
            while(it1.hasNext()){
                Integer currentInt = it1.next();
                if(result2.contains(currentInt)) finalResult.add(currentInt);
            }
        }
        else{
            while (it1.hasNext()) finalResult.add(it1.next());
            Iterator<Integer> it2 = result2.iterator();
            while (it2.hasNext()){
                Integer currentInt = it2.next();
                if(!finalResult.contains(currentInt)) finalResult.add(currentInt);
            }
        }
        return finalResult;
    }

    public String[] getFields() {
        return fields;
    }

    public String resultBuilder(int[] specifiedColumns, List results) throws IOException {
        String resultDisplay = "These are the matching results to your query: \n";
        if(results.size() == 0) resultDisplay += "No results match your query. \n";
        else{
            for (int i = 0; i < specifiedColumns.length; i++) {
                int column = specifiedColumns[i];
                resultDisplay += fields[column] + "\t\t";
            }
            resultDisplay += "\n";
            Iterator<Integer> it = results.iterator();
            while (it.hasNext()){
                Object[] row = getRow(it.next());
                for (int i = 0; i <specifiedColumns.length ; i++) {
                    Object result = row[specifiedColumns[i]];
                    if(result instanceof Date) resultDisplay += dateFormat.format((Date)result)+ "\t\t";
                    else {
                        resultDisplay += result + "\t\t";
                    }
                }
                resultDisplay += "\n";
            }
        }

        return resultDisplay;
    }

    public abstract Object[] getRow(int rowNumber) throws IOException;

    public abstract void storeData() throws IOException, ParseException;

    protected abstract List<Integer> simpleEqualityQueryExecutor(Query query);

    protected abstract List<Integer> simpleRangeQueryExecutor(Query query);



}
