package queryAdministrator;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.List;

import index.*;
import query.Query;

/**
 * Created by Rodrigo on 3/23/2017.
 */
public class indexedQA extends QueryAdministrator {
    //Version eficiente
    private Hashtable<Integer, String> data;
    private Hashtable<Integer, Index> indexes;

    public indexedQA(File file) {

    }

    public void createIndex(){

    }

    public List<Integer> consultIndex(){
        return null;
    }

    @Override
    public void storeData() throws IOException, ParseException {

    }

    @Override
    public List<Integer> simpleEqualityQueryExecutor(Query query) {
        return null;
    }

    @Override
    public List<Integer> simpleRangeQueryExecutor(Query query) {
        return null;
    }

    @Override
    public List<Integer> complexQueryExecutor(Query query1, Query query2, boolean isDisjunctive) {
        return null;
    }
}
