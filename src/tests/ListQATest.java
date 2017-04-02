package tests;

import query.Query;
import query.QueryType;
import queryAdministrator.QueryAdministrator;
import queryAdministrator.listQA;


import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Rodrigo on 4/1/2017.
 */
public class ListQATest {
    public static void main(String[] args) throws IOException, ParseException {
        int[] allColumns = {0,1,2,3,4};
        File file = new File("./src/stats.csv");
        assert(file.exists());
        QueryAdministrator queryAdministrator = new listQA(file);

        System.out.println("Validating data types...");
        queryAdministrator.validateDataTypes();

        System.out.println("These are the file fields: ");
        String[] fileFields = queryAdministrator.getFields();
        for (int i = 0; i < fileFields.length ; i++) {
            System.out.print(fileFields[i]+ " ");
        }
        System.out.println("\n Storing data...");
        queryAdministrator.storeData();


        System.out.println("Testing some queries...");
        //Sirven
        Query q1 = new Query(QueryType.EQUALITY,"Height",72);
        Query q2 = new Query(QueryType.RANGE,"Weight",100.0,150.0);
        //No sirven
        Query q3 = new Query(QueryType.INEQUALITY,"Male","false");
        Query q4 = new Query(QueryType.EQUALITY,"Name","Neil");



        Query[] queries = {q1,q2};
        for (int i = 0; i < queries.length; i++) {
            Query query = queries[i];
            System.out.println(query);
            List<Integer> result = queryAdministrator.simpleQueryExecutor(query);
            System.out.println(queryAdministrator.resultBuilder(allColumns,result));
        }




    }
}
