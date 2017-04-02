package query;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rodrigo on 3/19/2017.
 */
public class Query {
    private QueryType queryType;
    private String field;
    private Comparable value1;
    private Comparable value2;
    SimpleDateFormat dateFormat;

    //Constructor for Comparable Equality.
    public Query(QueryType type, String field,Comparable value1){
        assert (type != QueryType.RANGE);
        queryType = type;
        this.value1 = value1;
        this.field = field;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    }

    //Constructor for Comparable Range.
    public Query(QueryType type,String field, Comparable value1, Comparable value2){
        queryType = type;
        this.value1 = value1;
        this.value2 = value2;
        this.field = field;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    }


    public QueryType getQueryType() {
        return queryType;
    }

    public String getField() {
        return field;
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
        String value1 = "";
        String value2 = "";
        if(this.value1 instanceof Date) {
            value1 = dateFormat.format(this.value1);

            if(this.value2 != null) value2 = dateFormat.format(this.value2);
        }
        else{
            value1 = this.value1.toString();
            if(this.value2 != null) value2 = this.value2.toString();
        }

        String result = field + " ";
        switch (queryType){
            case RANGE:
                result += "entries in range: ["+ value1 + " , "+ value2+"]";
                break;
            case EQUALITY:
                result += "values equal to: ";
                result += value1;
                break;
            case INEQUALITY:
                result += "values not equal to: ";
                result += value1;
                break;

        }
        return result;
    }


}
