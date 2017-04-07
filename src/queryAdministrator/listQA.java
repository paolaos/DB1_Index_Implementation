package queryAdministrator;

import query.Query;
import query.QueryType;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * This class extends the QueryAdministrator abstract class. It implements all its abstract methods to create and retrieve
 * information from a data structure, it implements no extra methods. The data structure used by this class is a
 * LinkedList of LinkedLists, each LinkedList corresponds to a row or data entry of the file the contructro method
 * receives as a parameter.
 */
public class listQA extends QueryAdministrator {

    private List<LinkedList<Comparable>> data;

    /**
     * ListQA class constructor, this method instantiates the appropiate structures to store data and then calls methods that
     * validate data types and build the data base.
     * @param file
     */
    public listQA(File file) {
        this.file = file;
        validFile = false;
        data = new LinkedList();
        try{
            this.validateDataTypes();
            this.storeData();
        } catch(IOException e){
            e.fillInStackTrace();
        } catch (ParseException e){
            e.getErrorOffset();
        }

    }

    /**
     * This method reads the file and stores each entry in the class Data structure.
     * For the listQA implementation each row entry is stored as a List of Objects.
     * For the indexedQA each row field is stored in the field's index.
     * @throws IOException
     * @throws ParseException
     */
    public void storeData() throws IOException, ParseException {
        assert(validFile);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        //Skip first two lines.
        reader.readLine();reader.readLine();

        String currentLine;
        while((currentLine= reader.readLine()) != null){
            currentLine = currentLine.replaceAll("\"", "");
            LinkedList<Comparable> row = new LinkedList();
            String[] lineArray = currentLine.split(",");
            for (int i = 0; i < columns ; i++) {
                switch (fieldsDataTypes[i]){
                    case "String":
                        row.add(lineArray[i]);
                        break;
                    case "int":
                        Integer intNumber = Integer.parseInt(lineArray[i]);
                        row.add(intNumber);
                        break;
                    case "double":
                        Double doubleNumber = Double.parseDouble(lineArray[i]);
                        row.add(doubleNumber);
                        break;
                    case "date":
                        Date date = dateFormat.parse(lineArray[i]);
                        row.add(date);
                        break;
                    case "bool":
                        //Boolean bool = Boolean.parseBoolean(lineArray[i]);
                        //row.add(bool);
                        row.add(lineArray[i]);
                        break;
                }
            }
            data.add(row);
            rows++;
        }


    }

    /**
     * This method processes a simple query, one with only one field, of Equality or Inequality
     * QueryType. It looks in the class's data structure for the rows that,in the searched field, have
     * the same value as the one of the query or different to it, in the Inequality case.
     * @param query query with desired field and value to look for
     * @return List of rows in which the value occurs.
     */
    public List<Integer> simpleEqualityQueryExecutor(Query query){
        List<Integer> result = new LinkedList<>();
        int column=0;
        String queryField = query.getField();
        for (int i = 0; i < columns ; i++) {
            if(queryField.equals(fields[i])) {column=i;break;}
        }

        QueryType queryType = query.getQueryType();
        Comparable queryValue = query.getValue1();
        for (int i = 0; i < rows ; i++) {
            Comparable value = data.get(i).get(column);

            if(queryType == QueryType.EQUALITY){
                if(queryValue.compareTo(value)==0) result.add(i);
            }
            else{
                if(queryValue.compareTo(value)!=0) result.add(i);
            }

        }
        return result;
    }

    /**
     * This method processes a simple query, one with only one field, of Range QueryType.
     * It looks in the class's data structure for all the rows that, in the query's field, have a value
     * in the range specified by the query's value1 and value2
     * @param query with the field to look for and the range of values.
     * @return List of rows in which the value is in the range.
     */
    @Override
    public List<Integer> simpleRangeQueryExecutor(Query query) {
        List<Integer> result = new LinkedList<>();
        int column=0;
        Comparable v1 = query.getValue1();
        Comparable v2 = query.getValue2();
        String queryField = query.getField();
        for (int i = 0; i < columns ; i++) {
            if(queryField.equals(getFields()[i])) column=i;
        }
        for (int i = 0; i < rows ; i++) {
            Comparable value =  data.get(i).get(column);
            if(value.compareTo(v1) >= 0 && value.compareTo(v2)<=0) result.add(i);

        }
        return result;

    }

    /**
     * Returns the table entry of the appropriate row(received as a parameter) as an array of Objects, one for each field.
     * Depending on the inheriting class it may be the actual object values of the appropriate dataType, or String
     * values.
     * @param rowNumber
     * @return Array of values one for each field.
     * @throws IOException
     */
    protected Object[] getRow(int rowNumber){return data.get(rowNumber).toArray();}





}
