package index;

import query.Query;

import java.util.List;

/**
 * This class implements an interface for the indexes used to organize the data.
 * There is one index for each field of the table containing the data. Each index
 * implements an efficient data structure according to the data type associated with its corresponding
 * field. Indexes store a "pointer" to the row where a value appears, not the value itself.
 *
 * There are two classes that extend the Index interface:
 * -ComparableIndex: for comparable data types such as integer, double, Date.
 * -EqualityIndex: for dataTypes that only support equality queries such as boolean and String.
 *
 */
public interface Index {

    /**
     * This method searches if the value or range of values associated with the query received are stored in it,
     * it then returns a list of integers each of which is a row of the table where the value appears.
     * @param query with the value or range of values to look for.
     * @return list of "pointers" to the rows where the values appear.
     */
   public List<Integer> getObjects(Query query);

    /**
     * This method adds an new object to the data structure it uses to store value appearances in the table.
     * If the value wasn't previously stored it creates a new list containing the row received as parameter.
     * If the value was already in the structure it only adds the row to the already existing list.
     * @param o value to be added
     * @param row "pointer" to the row where the value appears
     */
   public void addObject(Object o,Integer row);

}
