package index;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by Paola Ortega S on 3/16/2017.
 */
public class ComparableIndex implements Index {
    private TreeMap<tuple, Integer> tree;

    public List<Integer> getObjects(String string){
        return null;
    }

    @Override
    public void addObject(Object o, Integer row) {

    }

    private class tuple<T extends Comparable> implements Comparable {
        T value;
        Integer row;
        public tuple(T value, Integer row){
            this.value = value;
            this.row = row;
        }

        @Override
        public int compareTo(Object o) {
            return value.compareTo(o);
        }
    }

}
