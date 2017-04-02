package query;

/**
 * Created by Rodrigo on 3/19/2017.
 */
public class Query {
    private QueryType queryType;
    private String field;
    private String value;
    private Comparable value1;
    private Comparable value2;

    //Constructor for Comparable Equality.
    public Query(QueryType type, String field,Comparable value1){
        queryType = type;
        this.value1 = value1;
        this.field = field;
    }

    //Constructor for Comparable Range.
    public Query(QueryType type,String field, Comparable value1, Comparable value2){
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

    public Comparable getValue1() {
        return value1;
    }

    public Comparable getValue2() {
        assert(queryType==QueryType.RANGE);
        return value2;
    }

    @Override
    public String toString( ){
        String result = "Query: "+ field + " ";
        switch (queryType){
            case RANGE:
                result += "entries in range: ["+ value1 + " , "+ value2+"]";
                break;
            case EQUALITY:
                result += "values equal to: ";
                if(value!=null) result += value;
                else result += value1;
                break;
            case INEQUALITY:
                result += "values  not equal to: ";
                if(value!=null) result += value;
                else result += value1;
                break;

        }
        return result;
    }


}
