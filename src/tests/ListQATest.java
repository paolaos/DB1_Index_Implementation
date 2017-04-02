package tests;

import query.Query;
import query.QueryType;
import queryAdministrator.QueryAdministrator;
import queryAdministrator.listQA;


import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Rodrigo on 4/1/2017.
 */
public class ListQATest {
    public static void main(String[] args) throws IOException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        int[] allColumns = {0,1,2,3,4};
        File file = new File("./src/stats.csv");
        assert(file.exists());
        QueryAdministrator queryAdministrator = new listQA(file);

        System.out.println("\nValidating data types...");
        queryAdministrator.validateDataTypes();

        System.out.println("\nThese are the file fields: ");
        String[] fileFields = queryAdministrator.getFields();
        for (int i = 0; i < fileFields.length ; i++) {
            System.out.print(fileFields[i]+ " ");
        }
        System.out.println("\n\nStoring data...");
        queryAdministrator.storeData();


        System.out.println("\nTesting some queries...");
        //Sirven
        Query q1 = new Query(QueryType.EQUALITY,"Height",66);
        Query q2 = new Query(QueryType.RANGE,"Weight",100.0,150.0);
        Query q3 = new Query(QueryType.INEQUALITY,"Male","TRUE");
        Query q4 = new Query(QueryType.EQUALITY,"Name","Neil");
        Query q5 = new Query(QueryType.EQUALITY,"Birth", dateFormat.parse("26/02/2006"));
        Query q6 = new Query(QueryType.RANGE,"Birth", dateFormat.parse("01/01/2000"),dateFormat.parse("01/01/2010"));



        Query[] queries = {q1,q2,q3,q4,q5,q6};
        for (int i = 0; i < queries.length; i++) {
            Query query = queries[i];
            System.out.println(query);
            List<Integer> result = queryAdministrator.simpleQueryExecutor(query);
            System.out.println(queryAdministrator.resultBuilder(allColumns,result));
        }

        System.out.println("\nComplex queries...");

        System.out.println(queryAdministrator.resultBuilder(allColumns,queryAdministrator.complexQueryExecutor(q3,q2,true)));
        System.out.println(queryAdministrator.resultBuilder(allColumns,queryAdministrator.complexQueryExecutor(q4,q5,false)));








    }
}
