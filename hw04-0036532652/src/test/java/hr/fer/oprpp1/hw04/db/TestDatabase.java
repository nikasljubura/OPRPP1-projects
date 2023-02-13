package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestDatabase {

    @Test
    public void testForJMBAG() throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get("src/main/Database.txt"),
                StandardCharsets.UTF_8
        );

        StudentDatabase db = new StudentDatabase(lines);

        StudentRecord tstRecord = new StudentRecord("0000000021", "Jakobušić",	"Antonija",2);

        assertEquals(tstRecord, db.forJMBAG("0000000021"));
    }

    private class Filter1 implements IFilter {

        @Override
        public boolean accepts(StudentRecord record){
            return true;
        }

    }

    private class Filter2 implements IFilter {

        @Override
        public boolean accepts(StudentRecord record){
            return false;
        }

    }

    @Test
    public void testFilter2() throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get("src/main/Database.txt"),
                StandardCharsets.UTF_8
        );

        StudentDatabase db = new StudentDatabase(lines);

       Filter2 f2 = new Filter2();

       List<StudentRecord> list = db.filter(f2);

       assertEquals(0,list.size());


    }

    @Test
    public void testFilter1() throws IOException {
        List<String> lines = Files.readAllLines(
                Paths.get("src/main/Database.txt"),
                StandardCharsets.UTF_8
        );

        StudentDatabase db = new StudentDatabase(lines);

        Filter1 f1 = new Filter1();

        List<StudentRecord> list = db.filter(f1);

        assertEquals(db.getListOfStudents().size(),list.size());


    }

    @Test
    public void testEqualsComparison() {

        String a = "Ana";
        String b = "Ana";

        assertEquals(true, ComparisonOperators.EQUALS.satisfied(a,b));
    }

    @Test
    public void testGreaterComparison() {

        String a = "Anna";
        String b = "Ana";

        assertEquals(true, ComparisonOperators.GREATER.satisfied(a,b));
    }

    @Test
    public void testLessOrEqualsComparison() {

        String a = "An";
        String b = "Ana";

        assertEquals(true, ComparisonOperators.LESS_OR_EQUALS.satisfied(a,b));
    }

    @Test
    public void testNotEqualsComparison() {

        String a = "Ana";
        String b = "Ana";

        assertEquals(false, ComparisonOperators.NOT_EQUALS.satisfied(a,b));
    }

    @Test
    public void testLikeCase1(){
        String a = "Ana";
        String b = "Ana";
        assertEquals(false, ComparisonOperators.NOT_EQUALS.satisfied(a,b));
    }

    @Test
    public void testLikeCase2(){
        String a = "Ana";
        String b = "A*";
        assertEquals(true, ComparisonOperators.LIKE.satisfied(a,b));
    }

    @Test
    public void testLikeCase3(){
        String a = "Ana";
        String b = "ABCF*";
        assertEquals(false, ComparisonOperators.LIKE.satisfied(a,b));
    }

    @Test
    public void testLikeCase4(){
        String a = "BABABA";
        String b = "*BABA";
        assertEquals(true, ComparisonOperators.LIKE.satisfied(a,b));
    }

    @Test
    public void testLikeCase5(){
        String a = "BABABA";
        String b = "BA*BA";
        assertEquals(true, ComparisonOperators.LIKE.satisfied(a,b));
    }

    @Test
    public void testLikeCase6(){
        String a = "AAA";
        String b = "AA*AA";
        assertEquals(false, ComparisonOperators.LIKE.satisfied(a,b));
    }


    @Test
    public void testFieldValueGetter() {

        StudentRecord rec = new StudentRecord("00354", "Lala", "Baba", 3);

        assertEquals("Baba", FieldValueGetters.FIRST_NAME.get(rec));
    }

    @Test
    public void testLikeWithB() {
        String a = "Brundija";
        String b = "B*";
        assertEquals(true, ComparisonOperators.LIKE.satisfied(a,b));
    }

}
