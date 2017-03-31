package index;

import query.*;

import java.util.*;

/**
 * Created by Paola Ortega S on 3/16/2017.
 */
public class ComparableIndex implements Index {

    private TreeMap<Comparable, List<Integer>> tree;

    @Override
    public List<Integer> getObjects(Query query){
        List<Integer> result = new LinkedList<>();
        // Porcion del arbol que nos interesa en forma de llaves.
        NavigableMap<Comparable,List<Integer>> submap =  new TreeMap<>();
        switch (query.getQueryType()){
            case EQUALITY:
                //" If fromKey and toKey are equal, the returned map is empty unless fromInclusive and toInclusive are both true".
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



    @Override
    public void addObject(Object o, Integer row) {
        Comparable key = (Comparable) o;
        List<Integer> list = tree.get(o);
        if(list != null) list.add(row);
        else tree.put(key, new LinkedList<Integer>(Collections.singleton(row)));
    }


}
