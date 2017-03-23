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
    protected int rows;
    protected int columns;
    protected boolean validFile;
    protected String[] fields;
    protected String[] fieldsDataTypes;
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");



    public void validateDataTypes() throws IOException {
        BufferedReader reader = null;
        String categories = null;
        String types = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            categories = reader.readLine();
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

    /*este metodo esta hecho para que devuelva el tipo de dato asociado a una categoria. No encontre la forma de tener un
    arreglo con diferentes tipos de clases, asi que mantuve el String array. Si encontras otra forma de hacerlo me avisas.
     */
    public String getFieldDataType(String category){
        for(int i = 0; i < fields.length; i++){
            if(fields[i].equals(category))
                return fieldsDataTypes[i];

        }

        throw new IndexOutOfBoundsException("Invalid category. ");

    }


    public String resultBuilder(String specifiedColumns, List<Integer> finalQuery){
        return null;
    }

    public abstract void storeData() throws IOException, ParseException;

    public abstract List<Integer> simpleEqualityQueryExecutor(Query query);

    public abstract List<Integer> simpleRangeQueryExecutor(Query query);

    public abstract List<Integer> complexQueryExecutor(Query query1, Query query2, boolean isDisjunctive);






}
