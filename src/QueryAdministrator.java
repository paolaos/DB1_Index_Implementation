import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Paola Ortega S on 3/16/2017.
 */
public class QueryAdministrator<T>{
    /*
    //Version eficiente
    private Hashtable<Integer, String> data;
    private Hashtable<Integer, Index> indexes;
    */

    //Version ineficiente
    private List<LinkedList> data;
    private UI ui;
    private File file;
    private int rows;
    private int columns;
    private boolean validFile;
    private String[] fields;
    private String[] fieldsDataTypes;
    private SimpleDateFormat dateFormat;
    public QueryAdministrator(File file){
        this.file = file;
        validFile = false;
        data = new LinkedList();
        dateFormat = new SimpleDateFormat("dd/MM/yy");
    }


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

    public void storeData() throws IOException, ParseException {
        assert(validFile);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        //Skip first two lines.
        reader.readLine();reader.readLine();

        String currentLine;
        while((currentLine= reader.readLine()) != null){
            List row = new LinkedList();
            String[] lineArray = currentLine.split(",");
            for (int i = 0; i < columns ; i++) {
                switch (fieldsDataTypes[i]){
                    case "String":
                        row.add(lineArray[i]);
                        break;
                    case "Integer":
                        Integer intNumber = Integer.parseInt(lineArray[i]);
                        row.add(intNumber);
                        break;
                    case "Double":
                        Double doubleNumber = Double.parseDouble(lineArray[i]);
                        row.add(doubleNumber);
                        break;
                    case "Date":
                        Date date = dateFormat.parse(lineArray[i]);
                        row.add(date);
                        break;
                    case "Boolean":
                        Boolean bool = Boolean.parseBoolean(lineArray[i]);
                        row.add(bool);
                        break;
                }
            }
            data.add(row);
            rows++;
        }


    }




    public List<Integer> simpleRangeQueryExecutor(Query query){

        return null;
    }

    public List<Integer> simpleInequalityQueryExecutor(Query query){
        List<Integer> result = new LinkedList<>();
        int column=0;
        String queryField = query.getField();
        for (int i = 0; i < columns ; i++) {
            if(queryField.equals(fields[i])) column=i;
        }
        for (int i = 0; i < rows ; i++) {
            Comparable value = (Comparable) data.get(i).get(column);
            if(query.getQueryType() == QueryType.EQUALITY){
                if(query.getValue1().compareTo(value)==0) result.add(i);
            }else{
                if(query.getValue1())
            }

        }


        return null;
    }

    public List<Integer> complexQueryExecutor(Query query1, Query query2, boolean isDisjunctive){
        return null;
    }

    public String resultBuilder(String specifiedColumns, List<Integer> finalQuery){
        return null;
    }




    public void createIndex(){

    }

    public List<Integer> consultIndex(){
        return null;
    }

}
