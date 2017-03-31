package index;

import query.Query;

import java.util.List;

/**
 * Created by Paola Ortega S on 3/16/2017.
 */
public interface Index {
    //TODO hablar sobre por qué recibiría un String.
   public List<Integer> getObjects(Query query);
   public void addObject(Object o,Integer row);

}
