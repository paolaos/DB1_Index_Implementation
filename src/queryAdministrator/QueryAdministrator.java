package queryAdministrator;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import ui.UI;
import query.*;

/**
 * This class implements a small simulator of a data base management system. It performs the basic functions a
 * typical DBMS would. It takes the data base "definition" from the cvs first two rows, which contain the field names and
 * data types respectively. It validates that the data types are recognized and the proceeds to build a structure that
 * stores data in orderly manner. It then allows the user to make queries about the stored data and shows the result. It
 * mainly differs from a DBMS in that it does not provide any means of sharing, protecting or mantaining the database to
 * evolve with the users demands.
 * This is an abstract class, there are two classes which inherit from it:
 * -listQA: which implements a list of lists to store and access the data.
 * -indexedQA: which instead uses an index for each field.
 */
public abstract class QueryAdministrator<T>{

    protected List<LinkedList> data;
    protected UI ui;
    protected File file;
    protected int rows;
    protected int columns;
    protected boolean validFile;
    protected String[] fields;
    protected String[] fieldsDataTypes;
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    /**
     * This method validates some restrictions on the structure of the stored file so that it can
     * be understood and worked with. It first checks that the number of field columns corresponds to the number of
     * columns indicating its dataType. It then checks that each data type is understood by the program:
     * String,int,double,date,bool.
     * @throws IOException
     */
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

    /**
     * Returns the DataType associated with the field it receives as parameter.
     * @param category file field.
     * @return corresponding DataType
     */
    public String getFieldDataType(String category){
        for(int i = 0; i < fields.length; i++){
            if(fields[i].equals(category))
                return fieldsDataTypes[i];

        }

        throw new IndexOutOfBoundsException("Invalid category. ");

    }

    /**
     * This method calls the appropriate method to process a simple query(one that looks for values of only one field).
     * It checks the QueryType and calls simpleEqualityQueryExecutor() or simpleRangeQueryExecutor(), and then
     * returns a list of pointers to rows as a result.
     * @param query consultation made by user.
     * @return List of rows
     */
    public List<Integer> simpleQueryExecutor(Query query){
        if(query.getQueryType() == QueryType.EQUALITY || query.getQueryType() == QueryType.INEQUALITY)
            return simpleEqualityQueryExecutor(query);

        return simpleRangeQueryExecutor(query);
    }

    /**
     * This method is in charge of processing complex queries received as parameters.
     * Since a complex query is simply the combination of 2 different queries it simply executes both separately
     * and then combines the results by joining them if the parameter isDisjunctive is false or by finding
     * their intersection if isDisjunctive is true.
     * It the returns the result.
     * @param query1
     * @param query2
     * @param isDisjunctive boolean indicating if the union or intersection of queries results is desired.
     * @return List of pointers to rows that satisfy the complex query.
     */
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

    /**
     * Returns an array consisting of all de file fields.
     * @return String[]
     */
    public String[] getFields() {
        return fields;
    }

    /**
     * The resultBuilder method takes the list of rows resulting from the query just processed and builds
     * a String that would presente them properly to the user. It takes an array containing the columns
     * the user wants to see displayed with the matching rows.
     * @param specifiedColumns coloumns of table wished to display
     * @param results The list of rows to include in the result
     * @return String with the appropiate rows and columns properly displayed.
     * @throws IOException
     */
    public String resultBuilder(int[] specifiedColumns, List results) throws IOException {
        int resultsSize = results.size();
        System.out.println("A total of "+resultsSize+" entries match your query." );
        String resultDisplay ="";
        if(resultsSize>0){
            resultDisplay += "These are the matching results to your query: \n";
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

    /**
     * Returns a string with the Fields names of the file separated by a comma ",".
     * @return String with field names.
     */
    public String showFields(){
        String result = "";
        for(String field: fields)
            result += field + ", ";
        result = result.substring(0, result.length() - 2);
        return result;
    }

    /**
     * Returns the number of columns, data fields, the inputted file has.
     * @return int with number of columns.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Returns the table entry of the appropriate row(received as a parameter) as an array of Objects, one for each field.
     * Depending on the inheriting class it may be the actual object values of the appropriate dataType, or String
     * values.
     * @param rowNumber
     * @return Array of values one for each field.
     * @throws IOException
     */
    protected abstract Object[] getRow(int rowNumber) throws IOException;

    /**
     * This method reads the file and stores each entry in the class Data structure.
     * For the listQA implementation each row entry is stored as a List of Objects.
     * For the indexedQA each row field is stored in the field's index.
     * @throws IOException
     * @throws ParseException
     */
    public abstract void storeData() throws IOException, ParseException;

    /**
     * This method processes a simple query, one with only one field, of Equality or Inequality
     * QueryType. It looks in the class's data structure for the rows that,in the searched field, have
     * the same value as the one of the query or different to it, in the Inequality case.
     * @param query query with desired field and value to look for
     * @return List of rows in which the value occurs.
     */
    protected abstract List<Integer> simpleEqualityQueryExecutor(Query query);

    /**
     * This method processes a simple query, one with only one field, of Range QueryType.
     * It looks int the class's data structure for all the rows that, in the query's field, have a value
     * in the range specified by the query's value1 and value2
     * @param query with the field to look for and the range of values.
     * @return List of rows in which the value is in the range.
     */
    protected abstract List<Integer> simpleRangeQueryExecutor(Query query);
}
