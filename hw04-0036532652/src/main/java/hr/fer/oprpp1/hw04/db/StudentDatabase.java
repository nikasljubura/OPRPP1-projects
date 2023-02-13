package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Models a database of student records
 */
public class StudentDatabase {

    private List<StudentRecord> listOfStudents;
    private HashMap<String, StudentRecord> recordHashMap; //map: jmbag, record

    /**
     * checks the given lines of database and creates the list and map of student records
     * @param databaseList
     * @throws RuntimeException
     */
    StudentDatabase(List<String> databaseList) {

        listOfStudents = new ArrayList<StudentRecord>();
        recordHashMap = new HashMap<>();

        for(String line : databaseList){
            String[] arr = line.split("\t");
            String jmbag = arr[0];
            String lastName = arr[1];
            String firstName = arr[2];
            String finalGrade = arr[3];

            if(recordHashMap.containsKey(jmbag) ||
             Integer.parseInt(finalGrade) < 1 || Integer.parseInt(finalGrade) > 5){
                throw new RuntimeException("Duplicate jmbags and inappropriate grades are not allowed!");

            }

            StudentRecord newRecord = new StudentRecord(jmbag, lastName, firstName, Integer.parseInt(finalGrade));

            recordHashMap.put(jmbag, newRecord);
            listOfStudents.add(newRecord);

        }
    }

    /**
     * gets the student in O(1) complexity
     * @param jmbag of the student we want to get
     * @return the wanted student
     */
    public StudentRecord forJMBAG(String jmbag){
        return recordHashMap.get(jmbag);
    }


    public List<StudentRecord> filter(IFilter filter) {

        List<StudentRecord> acceptedStudents = new ArrayList<>();

        for(StudentRecord record: listOfStudents){
            if(filter.accepts(record)) acceptedStudents.add(record);
        }

        return acceptedStudents;
    }

    public List<StudentRecord> getListOfStudents() {
        return listOfStudents;
    }

    public HashMap<String, StudentRecord> getRecordHashMap() {
        return recordHashMap;
    }
}
