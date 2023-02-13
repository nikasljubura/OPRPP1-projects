package hr.fer.oprpp1.hw04.db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StudentDB {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(
                Paths.get("src/main/Database.txt"),
                StandardCharsets.UTF_8
        );

        StudentDatabase db = new StudentDatabase(lines);
        List<StudentRecord> allSatisfying = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String line;
        while(!(line = reader.readLine().trim()).equals("exit")) {

            if(line.startsWith("query ")){

                String query = line.substring(6);
                QueryParser parser = new QueryParser(query);
                int records_selected = 0;

                if(parser.isDirectQuery()){
                    StudentRecord r = db.forJMBAG(parser.getQueriedJMBAG());
                    allSatisfying.add(r);
                    records_selected++;
                    System.out.println("Using index for record retrieval.");

                }else{
                    for(StudentRecord r: db.filter(new QueryFilter(parser.getQuery()))) { //get query will return list of cond
                        allSatisfying.add(r);
                        records_selected++;
                    }

                }


                if(records_selected == 0){
                    numOfRecords(records_selected);
                }else{
                    format(allSatisfying);
                    numOfRecords(records_selected);

                }



            }else{
                System.out.println("Pogrešan unos!");
            }

            allSatisfying.clear();

        }
        System.out.println("Goodbye!");
    }

    public static void numOfRecords(int num){
        System.out.println("Number of records selected: " + num);
        System.out.println();
    }


    public static void format(List<StudentRecord> records){
        int maxFNLen = 0;
        int maxLNLen = 0;

        for(StudentRecord record: records){
            if(record.getFirstName().length() > maxFNLen){
                maxFNLen = record.getFirstName().length();
            }
            if(record.getLastName().length() > maxLNLen){
                maxLNLen = record.getLastName().length();
            }
        }

        System.out.print("+============+");
        for(int i = 0; i < maxLNLen + 2; i++) System.out.print("=");
        System.out.print("+");
        for(int i = 0; i < maxFNLen + 2; i++) System.out.print("=");
        System.out.print("+");
        System.out.print("===");
        System.out.println("+");

        for(StudentRecord record: records){
            System.out.print("| "+ record.getJmbag()
            + " | " + record.getLastName());
            for(int i = 0; i < (maxLNLen-record.getLastName().length() + 1); i++) System.out.print(" ");
            System.out.print("| ");
            System.out.print(record.getFirstName());
            for(int i = 0; i < (maxLNLen-record.getFirstName().length() + 1); i++) System.out.print(" ");
            System.out.print("| ");
            System.out.println(record.getFinalGrade() + " |");
        }

        System.out.print("+============+");
        for(int i = 0; i < maxLNLen + 2; i++) System.out.print("=");
        System.out.print("+");
        for(int i = 0; i < maxFNLen + 2; i++) System.out.print("=");
        System.out.print("+");
        System.out.print("===");
        System.out.println("+");

    }


}
