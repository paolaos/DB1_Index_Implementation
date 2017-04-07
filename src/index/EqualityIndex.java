package index;

import java.util.*;
import query.*;

/**
 * The EqualityIndex provides a way to index fields from the table with Data Types that only support equality
 * queries such as String and boolean. It does so by using a Hashtable which has fast access times to the values
 * stored in it. The Hashtable stores the objects of the field as keys and a list of pointers to the row where they
 * appear as values.
 */
public class EqualityIndex implements Index {
    Hashtable<String, List<Integer>> table = new Hashtable<>();


    /**
     * This method searches if the value or range of values associated with the query received are stored in it,
     * it then returns a list of integers each of which is a row of the table where the value appears. This
     * implementation simply returns the value of the key entry, or the whole table minus one entry for the inequality
     * query.
     * @param query with the value or range of values to look for.
     * @return list of "pointers" to the rows where the values appear.
     */
    @Override
    public List<Integer> getObjects(Query query) {
        List<Integer> result = new LinkedList<>();
        switch (query.getQueryType()){
            case EQUALITY:
                result = table.get(query.getValue1());
                break;
            case INEQUALITY:
                Hashtable<String, List<Integer>> copy = table;
                copy.remove(query.getValue1());
                for(List<Integer> temp: copy.values())
                    result.addAll(temp);

                break;
        }
        return result;
    }

    /**
     * This method adds an new object to the data structure it uses to store value appearances in the table.
     * If the value wasn't previously stored it creates a new list containing the row received as parameter.
     * If the value was already in the structure it only adds the row to the already existing list.
     * @param o value to be added
     * @param row "pointer" to the row where the value appears
     */
    @Override
    public void addObject(Object o, Integer row) {
        String string = (String) o;
        if(!table.contains(string))
            table.put(string, new LinkedList<>(Collections.singleton(row)));

        else table.get(string).add(row);

    }

}
