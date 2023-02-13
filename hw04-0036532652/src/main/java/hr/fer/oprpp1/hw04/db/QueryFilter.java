package hr.fer.oprpp1.hw04.db;

import java.util.List;

/**
 * filters each student through a list of conditions given by the parser
 */
public class QueryFilter implements IFilter{

    private List<ConditionalExpression> conditions;

    /**
     *
     * @param record student on which we filter the expression
     * @return true if satisfies all conditions
     */
    @Override
    public boolean accepts(StudentRecord record) {

        String student_fn = record.getFirstName();
        String student_ln = record.getLastName();
        String student_jm = record.getJmbag();

        //runs through all conditions and checks the method satisfied from comparison operator
        for(ConditionalExpression condition: conditions){
            IFieldValueGetter field = condition.getFieldValueGetter();
            IComparisonOperator operator = condition.getOperator();
            String literal = condition.getLiteral();

            if(field == FieldValueGetters.FIRST_NAME){
                if(!operator.satisfied(student_fn, literal)) return false;
            }else if(field == FieldValueGetters.JMBAG){
                if(!operator.satisfied(student_jm, literal)) return false;
            }else if(field == FieldValueGetters.LAST_NAME){
                if(!operator.satisfied(student_ln, literal)) return false;
            }
        }

        return true;
    }

    public QueryFilter(List<ConditionalExpression> conditions) {
        this.conditions = conditions;
    }
}
