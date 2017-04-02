package queryAdministrator;

import query.Query;
import query.QueryType;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Rodrigo on 3/23/2017.
 */
public class listQA extends QueryAdministrator {
    //Version ineficiente
    private List<LinkedList<Comparable>> data;

    public listQA(File file) {
        this.file = file;
        validFile = false;
        data = new LinkedList();
    }





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

    public String resultBuilder(int[] specifiedColumns, List results) {
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
                LinkedList row = data.get(it.next());
                for (int i = 0; i <specifiedColumns.length ; i++) {
                    Object result = row.get(specifiedColumns[i]);
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



}
