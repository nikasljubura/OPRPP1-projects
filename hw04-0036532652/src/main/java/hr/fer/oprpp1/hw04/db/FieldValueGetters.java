package hr.fer.oprpp1.hw04.db;

/**
 * models specific getters for each field of an object
 */
public class FieldValueGetters {

    public static final IFieldValueGetter FIRST_NAME = record -> (record.getFirstName());
    public static final IFieldValueGetter LAST_NAME = record -> (record.getLastName());
    public static final IFieldValueGetter JMBAG = record -> (record.getJmbag());


}
