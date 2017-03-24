package queryAdministrator;

import query.Query;
import query.QueryType;

import java.io.*;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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


    @Override
    public String resultBuilder(int[] specifiedColumns, List results) {
        String resultDisplay = "These are the matching results to your query: \n";
        for (int i = 0; i < specifiedColumns.length; i++) {
            resultDisplay += fieldsDataTypes[specifiedColumns[i]] + "\t";
        }
        Iterator<Integer> it = results.iterator();
        while (it.hasNext()){
            LinkedList row = data.get(it.next());
            for (int i = 0; i <specifiedColumns.length ; i++) {
                resultDisplay += row.get(specifiedColumns[i]) + "\t";
            }
        }

        return resultDisplay;
    }

    public void storeData() throws IOException, ParseException {
        assert(validFile);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        //Skip first two lines.
        reader.readLine();reader.readLine();

        String currentLine;
        while((currentLine= reader.readLine()) != null){
            LinkedList<Comparable> row = new LinkedList();
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

    public List<Integer> simpleEqualityQueryExecutor(Query query){
        List<Integer> result = new LinkedList<>();
        int column=0;
        String queryField = query.getField();
        for (int i = 0; i < columns ; i++) {
            if(queryField.equals(getFields()[i])) column=i;
        }
        for (int i = 0; i < rows ; i++) {
            Comparable value = data.get(i).get(column);
            if(query.getQueryType() == QueryType.EQUALITY){
                if(query.getValue1().compareTo(value)==0) result.add(i);
            }
            else{
                if(query.getValue1().compareTo(value)!=0) result.add(i);
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



}
