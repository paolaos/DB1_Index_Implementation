package index;

import java.util.Hashtable;
import java.util.List;
import query.*;

/**
 * Created by Paola Ortega S on 3/16/2017.
 */
public class EqualityIndex implements Index {
    Hashtable<String, List<Integer>> table;



    @Override
    public List<Integer> getObjects(Query query) {
        return null;
    }

    @Override
    public void addObject(Object o, Integer row) {

    }

}
