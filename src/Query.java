import com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator;

/**
 * Created by Rodrigo on 3/19/2017.
 */
public class Query<T extends Comparable> {
    enum QueryType{EQUALITY,INEQUALITY, RANGE};
    private QueryType queryType;
    private String field;
    private String value;
    private T value1;
    private T value2;

    //Constructor for Comparable Equality.
    public Query(QueryType type, String field,T value1){
        queryType = type;
        this.value1 = value1;
        this.field = field;
    }

    //Constructor for Comparable Range.
    public Query(QueryType type,String field, T value1, T value2, int inequality ){
        queryType = type;
        this.value1 = value1;
        this.value2 = value2;
        this.field = field;
    }

    //Constructor for String Equality.
    public Query(QueryType type,String field, String value){
        queryType = type;
        this.value = value;
        this.field = field;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    public T getValue1() {
        return value1;
    }

    public T getValue2() {
        assert(queryType==QueryType.RANGE);
        return value2;
    }



}
