package queryAdministrator;

import java.io.*;
import java.text.ParseException;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import index.*;
import query.Query;

/**
 * Created by Rodrigo on 3/23/2017.
 */
public class indexedQA extends QueryAdministrator {
    //Version eficiente
    private Hashtable<String, Index> indexes;
    private Integer[] rowStart;

    public indexedQA(File file) {
        this.file = file;
        validFile = false;
        createIndexes();

    }

    private void createIndexes(){
        for (int i = 0; i < fields.length ; i++) {
            String field = fields[i];
            Index currentIndex;
            switch (fieldsDataTypes[i]){
                case "String":
                case "Boolean":
                    currentIndex = new EqualityIndex();
                    break;
                default:
                    currentIndex = new ComparableIndex();
            }
            indexes.put(field,currentIndex);
        }

    }

    private String[] getRow(int rowNumber) throws IOException {
        int offset = rowStart[rowNumber];
        int length = rowStart[rowNumber+1] - offset;
        byte[] line = new byte[length];
        FileInputStream fi = new FileInputStream(file);
        fi.skip(offset);
        fi.read(line,0,length);
        String lineAsString = "";
        for (int i = 0; i <length ; i++) {
            lineAsString += (char) line[i];
        }
        return lineAsString.split(",");
    }

    @Override
    public void storeData() throws IOException, ParseException {
        assert(validFile);
        BufferedReader br = new BufferedReader(new FileReader(file));
        int position = br.readLine().length() + br.readLine().length() + 2;
        String line;
        LinkedList<Integer> rowStart = new LinkedList<>();
        while((line = br.readLine()) != null){
            rowStart.add(position);

            String[] lineArray = line.split(",");
            for (int i = 0; i < columns ; i++) {
                Index index = indexes.get(fields[i]);
                switch (fieldsDataTypes[i]){
                    case "String":
                        index.addObject(lineArray[i],rows);
                        break;
                    case "int":
                        Integer intNumber = Integer.parseInt(lineArray[i]);
                        index.addObject(intNumber,rows);
                        break;
                    case "double":
                        Double doubleNumber = Double.parseDouble(lineArray[i]);
                        index.addObject(doubleNumber,rows);
                        break;
                    case "date":
                        Date date = dateFormat.parse(lineArray[i]);
                        index.addObject(date,rows);
                        break;
                    case "bool":
                        index.addObject(lineArray[i],rows);
                        break;
                }
            }
            position += line.length()+1;
            rows++;
        }
        rowStart.add(position);
        this.rowStart = (Integer[]) rowStart.toArray();
    }

    @Override
    public List<Integer> simpleEqualityQueryExecutor(Query query) {
        List<Integer> result;
        Index index = indexes.get(query.getField());
        return index.getObjects();

    }

    @Override
    public List<Integer> simpleRangeQueryExecutor(Query query) {
        return null;
    }

    @Override
    public List<Integer> complexQueryExecutor(Query query1, Query query2, boolean isDisjunctive) {
        return null;
    }

    @Override
    public String resultBuilder(int[] specifiedColumns, List results) {
        return null;
    }



}


