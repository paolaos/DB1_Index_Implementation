package index;

import query.*;

import java.util.*;

/**
 * The ComparableIndex class provides a way to index fields with Comparable Data Type such as Integer,Double,Date.
 * It supports both single value and range queries. It does so by using a ordered dictionary, specifically a
 * TreeMap where the object itself is the key and the value is a List with pointers to the row where it appears.
 */
public class ComparableIndex implements Index {

    private TreeMap<Comparable, List<Integer>> tree;

    /**
     * Constructor method for the ComparableIndex class.
     * Simply instantiates the data structure(TreeMap) where the list of rows for each value is stored.
     */
    public ComparableIndex(){
        tree = new TreeMap<>();
    }

    /**
     * This method searches if the value or range of values associated with the query received are stored in it,
     * it then returns a list of integers each of which is a row of the table where the value appears. This implementation
     * calls the submap method of TreeMap to improve access times for the range queries.
     * @param query with the value or range of values to look for.
     * @return list of "pointers" to the rows where the values appear.
     */
    @Override
    public List<Integer> getObjects(Query query){
        List<Integer> result = new LinkedList<>();
        // Porcion del arbol que nos interesa en forma de llaves.
        NavigableMap<Comparable,List<Integer>> submap =  new TreeMap<>();
        switch (query.getQueryType()){
            case EQUALITY:
                //" If fromKey and toKey are equal, the returned map is empty unless fromInclusive and toInclusive are both true". Treemap documentation for subMap method.
                Comparable value = query.getValue1();
                submap = tree.subMap(value,true,value,true);
                break;
            case INEQUALITY:
                submap = tree;
                submap.remove(query.getValue1());
                break;
            case RANGE:
                submap = tree.subMap(query.getValue1(),true,query.getValue2(),true);
                break;
        }
        for(List<Integer> rows: submap.values()){
            result.addAll(rows);
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
        Comparable key = (Comparable) o;
        List<Integer> list = tree.get(o);
        if(list != null) list.add(row);
        else tree.put(key, new LinkedList<Integer>(Collections.singleton(row)));
    }


}
