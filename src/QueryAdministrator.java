import java.io.*;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Paola Ortega S on 3/16/2017.
 */
public class QueryAdministrator<T>{
    private Hashtable<Integer, String> data;
    private Hashtable<Integer, Index> indexes;
    private UI ui;
    private File file;
    private String[] dataCategories;
    private String[] dataTypes;
    public QueryAdministrator(File file){
        this.file = file;
    }

    public QueryAdministrator(){

    }

    public void validateDataTypes(){
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

        dataCategories = categories.split(",");
        dataTypes = types.split(",");
        //secciono en dos arreglos. Uno de categorías (primera línea) y el otro de tipos (segunda línea).

        if(dataCategories.length != dataTypes.length)
            throw new StringIndexOutOfBoundsException("The amount of categories do not match with the amount of data types. Please check the file and try again later. ");
        //valido que sean del mismo tamaño

        for(int i = 0; i < dataTypes.length; i++){
            if(!(dataTypes[i].equals("String") || dataTypes[i].equals("int")
                    || dataTypes[i].equals("double") || dataTypes[i].equals("date")
                    || dataTypes[i].equals("bool")))
                throw new StringIndexOutOfBoundsException("One of the data types isn't recognized by the program. Please check the file and try again. ");
        }
        //valido que sean de un tipo aceptable
    }

    /*este metodo esta hecho para que devuelva el tipo de dato asociado a una categoria. No encontre la forma de tener un
    arreglo con diferentes tipos de clases, asi que mantuve el String array. Si encontras otra forma de hacerlo me avisas.
     */
    private String getDataType(String category){
        for(int i = 0; i < dataCategories.length; i++){
            if(dataCategories[i].equals(category))
                return dataTypes[i];

        }

        throw new IndexOutOfBoundsException("Invalid category. ");

    }

    public void storeData(File file){

    }

    public void createIndex(){

    }

    public List<Integer> consultIndex(){
        return null;
    }

    public boolean validateQuery(String input){
        return true;
    }

    public List<Integer> simpleRangeQueryExecutor(Query query){
        return null;
    }

    public List<Integer> simpleInequalityQueryExecutor(Query query){
        return null;
    }

    public List<Integer> complexQueryExecutor(Query query1, Query query2, boolean isDisjunctive){
        return null;
    }

    public String resultBuilder(String specifiedColumns, List<Integer> finalQuery){
        return null;
    }

    private String fetchRow(int rowNumber){
        return null;
    }


    /*public Class getField(String field) {
        Class result;
        return "".getClass();
    }*/
}
