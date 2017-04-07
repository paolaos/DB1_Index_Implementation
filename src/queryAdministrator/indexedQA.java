package queryAdministrator;

import java.io.*;
import java.text.ParseException;
import java.util.*;

import index.*;
import query.Query;

/**
 * This class extends the QueryAdministrator abstract class. It implements all its abstract methods to create and retrieve
 * information from a data structure. It implements only one extra method createIndexes() which are the data structures used
 * to efficiently access information by data field. There are two different index types used by this class. Comparable index
 * which supports all kinds of queries and Equality index which only supports Equality and Inequality data types.
 */
public class indexedQA extends QueryAdministrator {
    //Version eficiente
    private Hashtable<String, Index> indexes;
    private Integer[] rowStart;

    /**
     * This is the class constructor method, it validates that it undestands the data types of the file received as
     * a parameter and then procceds to build the database and store pointers to value occurrences in the indexes.
     * @param file
     */
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

    /**
     * This method builds the data structures used by the class to store pointers to value occurrences.
     * It builds one index for each field, depending on the field data Type it either builds an Equality index(String and
     * boolean) or a Comparable index(int,double,Date).
     */
    private void createIndexes(){
        for (int i = 0; i < fields.length ; i++) {
            String field = fields[i];
            Index currentIndex;
            switch (fieldsDataTypes[i]){
                case "String":
                case "bool":
                    currentIndex = new EqualityIndex();
                    break;
                default:
                    currentIndex = new ComparableIndex();
            }
            indexes.put(field,currentIndex);
        }

    }

    /**
     * Returns the table entry of the appropriate row(received as a parameter) as an array of Objects, one for each field.
     * Depending on the inheriting class it may be the actual object values of the appropriate dataType, or String
     * values.
     * @param rowNumber
     * @return Array of values one for each field.
     * @throws IOException
     */
    @Override
    protected Object[] getRow(int rowNumber) throws IOException {
        int offset = rowStart[rowNumber];
        int length = rowStart[rowNumber+1] - offset-2;
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

    /**
     * This method reads the file and stores each entry in the class Data structure.
     * For the listQA implementation each row entry is stored as a List of Objects.
     * For the indexedQA each row field is stored in the field's index.
     * @throws IOException
     * @throws ParseException
     */
    @Override
    public void storeData() throws IOException, ParseException {
        createIndexes();
        assert(validFile);
        BufferedReader br = new BufferedReader(new FileReader(file));
        int position = br.readLine().length() + br.readLine().length() + 4;
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

    /**
     * This method processes a simple query, one with only one field, of Equality or Inequality
     * QueryType. It looks in the class's data structure for the rows that,in the searched field, have
     * the same value as the one of the query or different to it, in the Inequality case.
     * @param query query with desired field and value to look for
     * @return List of rows in which the value occurs.
     */
    @Override
    public List<Integer> simpleEqualityQueryExecutor(Query query) {
        Index index = indexes.get(query.getField());
        return index.getObjects(query);
    }

    /**
     * This method processes a simple query, one with only one field, of Range QueryType.
     * It looks in the class's data structure for all the rows that, in the query's field, have a value
     * in the range specified by the query's value1 and value2
     * @param query with the field to look for and the range of values.
     * @return List of rows in which the value is in the range.
     */
    @Override
    public List<Integer> simpleRangeQueryExecutor(Query query) {
        Index index = indexes.get(query.getField());
        return index.getObjects(query);
    }



}


