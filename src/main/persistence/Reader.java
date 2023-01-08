package persistence;

import model.Account;
import model.Record;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// NOTICE:
// Structure of this persistence function is made with the guidance of, and therefore based around,
// the "Teller" application from CPSC 210, which can be found and downloaded at:
// https://github.students.cs.ubc.ca/CPSC210/TellerApp

// Represent a reader that can read account data from file
public class Reader {
    public static final String DELIMITER = ",";

    // EFFECTS:  dummy constructor for purposes of code coverage
    public Reader() {

    }

    // EFFECTS:  returns an account parsed from file;
    //           throws IOException if an exception is raised
    //           when opening/reading from file
    public static Account readAccount(File file) throws IOException {
        List<String> fileContent = readFile(file);
        Account account = parseContent(fileContent);
        return account;
    }

    // EFFECTS:  returns content of file as a list of strings,
    //           with each string containing the content of
    //           one row of the file
    private static List<String> readFile(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

    // EFFECTS:  returns an account parsed from list of strings;
    //           where each string contains data for one record and
    //           the records are made into a records log for the account
    private static Account parseContent(List<String> fileContent) {
        ArrayList<Record> log = new ArrayList<>();

        for (String line : fileContent) {
            ArrayList<String> lineComponents = splitString(line);
            log.add(parseRecord(lineComponents));
        }

        return new Account(log);
    }

    // EFFECTS:  returns a list of strings obtained by
    //           splitting a line at each DELIMITER
    private static ArrayList<String> splitString(String line) {
        String[] splits = line.split(DELIMITER);
        return new ArrayList<>(Arrays.asList(splits));
    }

    // REQUIRES:  components has size 3 where
    //            - element 0 represents the title (String)
    //            - element 1 represents the category (String), and
    //            - element 2 represents the value (double)
    //            of the record to be constructed
    // EFFECTS:  returns a record constructed from components of a line
    private static Record parseRecord(List<String> components) {
        String title = components.get(0);
        String category = components.get(1);
        double value = Double.parseDouble(components.get(2));
        return new Record(title, category, value);
    }

}
