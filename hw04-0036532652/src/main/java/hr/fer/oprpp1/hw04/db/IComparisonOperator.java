package hr.fer.oprpp1.hw04.db;
/**
 * operator that decides the defined relation between string literals depeniding on what operator it is
 */
public interface IComparisonOperator {

    public boolean satisfied(String value1, String value2);

}
