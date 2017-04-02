package index;

import java.util.*;
import query.*;

/**
 * Created by Paola Ortega S on 3/16/2017.
 */
public class EqualityIndex implements Index {
    Hashtable<String, List<Integer>> table = new Hashtable<>();

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

    @Override
    public void addObject(Object o, Integer row) {
        String string = (String) o;
        if(!table.contains(string))
            table.put(string, new LinkedList<>(Collections.singleton(row)));

        else table.get(string).add(row);

    }

}
