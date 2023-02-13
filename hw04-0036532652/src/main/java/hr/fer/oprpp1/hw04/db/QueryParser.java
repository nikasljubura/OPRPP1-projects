package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * parse query into a list of conditional expressions
 */
public class QueryParser {

    private List<ConditionalExpression> listOfCondExpressions = new ArrayList<>(); //lista aneurizama
    private String input; //query to parse


    public QueryParser(String input){
        this.input = input;
        parse();
    }



    private void parse(){

        while(this.input.length() > 0){


            IFieldValueGetter field_value;
            IComparisonOperator operator;
            String literal;


            Token token = getNextToken();

            if(token.type.equals("fieldValue")){ //COND EXP STARTS WITH FIELD VALUE
                if(token.value.equals("jmbag")){
                    field_value = FieldValueGetters.JMBAG;
                }else if(token.value.equals("firstName")){
                    field_value = FieldValueGetters.FIRST_NAME;
                }else if(token.value.equals("lastName")){
                    field_value = FieldValueGetters.LAST_NAME;
                }else{
                    throw new RuntimeException("invalid fieldvalue");
                }
            }else{
                throw new RuntimeException("Conditional Expression must start with fieldValue");
            }



            token = getNextToken(); //needs to be operator




            if(token.type.equals("operator")){

                if(token.value.equals("=")){
                    operator = ComparisonOperators.EQUALS;
                }else if(token.value.equals(">")){
                    operator = ComparisonOperators.GREATER;
                }else if(token.value.equals("<")){
                    operator = ComparisonOperators.LESS;
                }else if(token.value.equals(">=")){
                    operator = ComparisonOperators.GREATER_OR_EQUALS;
                }else if(token.value.equals("<=")){
                    operator = ComparisonOperators.LESS_OR_EQUALS;
                }else if(token.value.equals("LIKE")){
                    operator = ComparisonOperators.LIKE;
                }else if(token.value.equals("!=")){
                    operator = ComparisonOperators.NOT_EQUALS;
                }else{
                    throw new RuntimeException("Invalid operator");
                }

            }else{
                throw new RuntimeException("Conditional Expression has an operator after an field value");
            }

            token = getNextToken();

            if(token.type.equals("literal")){
                literal = token.value;
            }else{
                throw new RuntimeException("Conditional Expression has a literal after an operator");
            }


            //ADDING NEW COND EXPRESSION
            listOfCondExpressions.add(new ConditionalExpression(field_value, literal, operator));



            //next should be and if this is a multiple condition query
            token = getNextToken();

            if(!token.type.equals("and")){
               if(token.type.equals("end")){
                   break;
               }
            }


        }


    }

    private Token getNextToken(){
        this.input = this.input.trim();//trim all leading spaces

        if(this.input.length() == 0) return new Token(null, "end"); // we read all

        if(this.input.startsWith("jmbag")){
            this.input = this.input.substring(5);
            return new Token("jmbag", "fieldValue");
        }else if(this.input.startsWith("firstName")){
            this.input = this.input.substring(9);
            return new Token("firstName", "fieldValue");
        }else if(this.input.startsWith("lastName")){
            this.input = this.input.substring(8);
            return new Token("lastName", "fieldValue");
        }else if(this.input.startsWith("\"")){
            String literal = "";
            char[] inputCopy = this.input.toCharArray();
            int i = 1;
            while(inputCopy[i] != '\"'){
                literal+= inputCopy[i++];
            }
            this.input = this.input.substring(literal.length() + 2); //skip literal and continue
            return new Token(literal, "literal");
        }else if(this.input.startsWith("=")){
            this.input = this.input.substring(1);
            return new Token("=", "operator");
        }else if(this.input.startsWith(">")){
            this.input = this.input.substring(1);
            return new Token(">", "operator");
        }else if(this.input.startsWith("<")){
            this.input = this.input.substring(1);
            return new Token("<", "operator");
        }else if(this.input.startsWith(">=")){
            this.input = this.input.substring(2);
            return new Token(">=", "operator");
        }else if(this.input.startsWith("<=")){
            this.input = this.input.substring(2);
            return new Token("<=", "operator");
        }else if(this.input.startsWith("!=")){
            this.input = this.input.substring(2);
            return new Token("!=", "operator");
        }else if(this.input.startsWith("LIKE")){
            this.input = this.input.substring(4);
            return new Token("LIKE", "operator");
        }else if(this.input.toUpperCase().startsWith("AND")){
            this.input = this.input.substring(3);
            return new Token("AND", "and");
        }else{
            throw new RuntimeException("Invalid token");
        }
    }


    private class Token {
        String value;
        String type; //operator, literal, fieldvalue, and, error(something unexpected), end(everything is read)

        public Token(String value, String type) {
            this.value = value;
            this.type = type;
        }
    }


    public boolean isDirectQuery() {

        if(this.listOfCondExpressions.size() != 1) {
            return false;
        }else{
            ConditionalExpression exp = this.listOfCondExpressions.get(0);
            if(exp.getFieldValueGetter() == FieldValueGetters.JMBAG &&
            exp.getOperator() == ComparisonOperators.EQUALS){
                return true;
            }
        }

        return false;
    }

    public String getQueriedJMBAG() {

            if(isDirectQuery()){
                return this.listOfCondExpressions.get(0).getLiteral();
            }else{
                throw new IllegalStateException("No jmbag was queried");
            }

    }


    public List<ConditionalExpression> getQuery() {
            return this.listOfCondExpressions;
    } //gets sent to queryfilter - filter


    public static void main(String[] args) {

        QueryParser qp1 = new QueryParser(" jmbag =\"0123456789\" ");
        System.out.println("isDirectQuery(): " + qp1.isDirectQuery()); // true
        System.out.println("jmbag was: " + qp1.getQueriedJMBAG()); // 0123456789
        System.out.println("size: " + qp1.getQuery().size()); // 1
        QueryParser qp2 = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
        System.out.println("isDirectQuery(): " + qp2.isDirectQuery()); // false
        // System.out.println(qp2.getQueriedJMBAG()); // would throw!
        System.out.println("size: " + qp2.getQuery().size()); // 2



    }

}
