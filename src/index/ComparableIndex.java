package index;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Paola Ortega S on 3/16/2017.
 */
public class ComparableIndex implements Index {
    private TreeMap<Comparable, Integer> tree;

    public List<Integer> getObjects(){
        List<Integer> result = new LinkedList<>();
        Iterator<Comparable> it;

        return null;
    }



    @Override
    public void addObject(Object o, Integer row) {

    }

    /*private class tuple<T extends Comparable> implements Comparable {
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
    }*/

}
