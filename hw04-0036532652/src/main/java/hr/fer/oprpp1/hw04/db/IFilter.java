package hr.fer.oprpp1.hw04.db;
@FunctionalInterface
/**
 * decides which student records are acceptable
 */
public interface IFilter {

    public boolean accepts(StudentRecord record);
}
