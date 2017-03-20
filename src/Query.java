import com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator;

/**
 * Created by Rodrigo on 3/19/2017.
 */
public class Query<T extends Comparable> {
    private enum QueryType{EQUALITY,INEQUALITY, RANGE};
    QueryType queryType;
    String value;
    T value1;
    T value2;

    //Constructor for Comparable Equality.
    public Query(QueryType type, T value1){
        queryType = type;
        this.value1 = value1;
    }

    //Constructor for Comparable Range.
    public Query(QueryType type, T value1, T value2 ){
        queryType = type;
        this.value1 = value1;
        this.value2 = value2;
    }

    //Constructor for String Equality.
    public Query(QueryType type, String value){
        queryType = type;
        this.value = value;
    }



}
