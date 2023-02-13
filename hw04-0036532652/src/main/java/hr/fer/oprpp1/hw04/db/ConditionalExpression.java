package hr.fer.oprpp1.hw04.db;

/**
 * models complete conditional expression
 */
public class ConditionalExpression {

    private IFieldValueGetter fieldValueGetter;
    private String literal;
    private IComparisonOperator operator;

    public ConditionalExpression(IFieldValueGetter fieldValueGetter, String literal, IComparisonOperator operator) {
        this.fieldValueGetter = fieldValueGetter;
        this.literal = literal;
        this.operator = operator;
    }

    public IFieldValueGetter getFieldValueGetter() {
        return fieldValueGetter;
    }


    public String getLiteral() {
        return literal;
    }


    public IComparisonOperator getOperator() {
        return operator;
    }

}
