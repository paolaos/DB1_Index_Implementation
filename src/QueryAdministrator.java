import java.io.File;
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

    public QueryAdministrator(File file){
        this.file = file;
    }

    public QueryAdministrator(){

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


    public  Class validateField() {
        return "".getClass();
    }
}
