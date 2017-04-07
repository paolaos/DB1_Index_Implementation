package query;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The user-defined quest to be assessed, validated and resolved by the program. This class is
 * in charge of detailing all possible types of queries and defines the user-defined parameters
 * needed by its respective case.
 */
public class Query {
    private QueryType queryType;
    private String field;
    private Comparable value1;
    private Comparable value2;
    SimpleDateFormat dateFormat;

    /**
     * Query constructor for Comparable Equality and Inequality cases. This covers all cases
     * when a specific value is used in order to search for the answer to the query. In case
     * of an equality search, the user-defined value is expected to be returned. In case of an
     * inequality search, every possible value except the one defined by the user is expected
     * to be returned.
     *
     * @param type   defines whether the search is an equality case or an inequality case
     * @param field  the type of field/column where the value has to be searched.
     * @param value1 the target value that's going to be assessed.
     */
    public Query(QueryType type, String field, Comparable value1) {
        assert (type != QueryType.RANGE);
        queryType = type;
        this.value1 = value1;
        this.field = field;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    }

    /**
     * Query constructor for Range cases only. This covers all cases when the search is based on
     * more than one value. It could either be between a specific value and an inequality, or
     * between a range of two specific values. UI is in charge of treating the range input in
     * any of the previously mentioned cases.
     *
     * @param type   revises the case is essentially a range query type.
     * @param field  the type of field/column where the values have to be searched.
     * @param value1 the minimum (inclusive) target value that's going to be assessed.
     * @param value2 the maximum (inclusive) target value that's going to be assessed.
     */
    public Query(QueryType type, String field, Comparable value1, Comparable value2) {
        queryType = type;
        this.value1 = value1;
        this.value2 = value2;
        this.field = field;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    }

    /**
     * @return the type of assessment from a specific query.
     */
    public QueryType getQueryType() {
        return queryType;
    }

    /**
     * @return the user-defined field requested by the user.
     */
    public String getField() {
        return field;
    }

    /**
     * the first/only value that's treated in any query.
     *
     * @return
     */
    public Comparable getValue1() {
        return value1;
    }

    /**
     * @return the second value that's only treated in range-type queries.
     */
    public Comparable getValue2() {
        assert (queryType == QueryType.RANGE);
        return value2;
    }

    /**
     * @return string representation of this query.
     */
    @Override
    public String toString() {
        String value1 = "";
        String value2 = "";
        if (this.value1 instanceof Date) {
            value1 = dateFormat.format(this.value1);

            if (this.value2 != null) value2 = dateFormat.format(this.value2);
        } else {
            value1 = this.value1.toString();
            if (this.value2 != null) value2 = this.value2.toString();
        }

        String result = field + " ";
        switch (queryType) {
            case RANGE:
                result += "entries in range: [" + value1 + " , " + value2 + "]";
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
