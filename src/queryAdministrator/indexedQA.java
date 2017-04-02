package queryAdministrator;

import java.io.*;
import java.text.ParseException;
import java.util.*;

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
        indexes = new Hashtable<>();
        try{
            this.validateDataTypes();
            this.storeData();
        } catch(IOException e){
            e.fillInStackTrace();
        } catch (ParseException e){
            e.getErrorOffset();
        }

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

    @Override
    public Object[] getRow(int rowNumber) throws IOException {
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
        createIndexes();
        assert(validFile);
        BufferedReader br = new BufferedReader(new FileReader(file));
        int position = br.readLine().length() + br.readLine().length() + 2;
        String line;
        List<Integer> rowStart = new ArrayList<Integer>();
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
            position += line.length()+2;
            rows++;
        }
        rowStart.add(position);
        this.rowStart = new Integer[rowStart.size()];
        this.rowStart = rowStart.toArray(this.rowStart);
    }

    @Override
    public List<Integer> simpleEqualityQueryExecutor(Query query) {
        Index index = indexes.get(query.getField());
        return index.getObjects(query);
    }

    @Override
    public List<Integer> simpleRangeQueryExecutor(Query query) {
        Index index = indexes.get(query.getField());
        return index.getObjects(query);
    }



}


