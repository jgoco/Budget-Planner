package persistence;

import model.Account;
import model.Record;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ReaderTest {
    @Test
    void testConstructor() {
        Reader reader = new Reader();
    }

    @Test
    void testParseAccountsFile1() {
        try {
            Account acc = Reader.readAccount(new File("./data/testAccount1.txt"));

            assertEquals(2, acc.getLog().size());
            Record rec0 = acc.getRecord(0);
            checkRecordMatches(rec0, "Testing", "income", 100);
            Record rec1 = acc.getRecord(1);
            checkRecordMatches(rec1, "Deduct", "bills", 23.50);

            assertEquals(100-23.50, acc.getBalance());
            checkCategorySumsMatch(acc,100, 23.50, 0, 0, 0, 0, 0);

        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testParseAccountsFile2() {
        try {
            Account acc = Reader.readAccount(new File("./data/testAccount2.txt"));

            assertEquals(10, acc.getLog().size());
            Record rec0 = acc.getRecord(0);
            checkRecordMatches(rec0, "pay cheque", "income", 1897.23);
            Record rec1 = acc.getRecord(1);
            checkRecordMatches(rec1, "water bill", "bills", 230.05);
            Record rec5 = acc.getRecord(5);
            checkRecordMatches(rec5, "McDonald's", "dining", 10.05);
            Record rec8 = acc.getRecord(8);
            checkRecordMatches(rec8, "gift", "other", 150);
            Record rec9 = acc.getRecord(9);
            checkRecordMatches(rec9, "Castle Fun Park", "recreation", 100);

            assertEquals(3278.95, acc.getBalance());
            checkCategorySumsMatch(acc, 4197.27, 560.14, 64.05, 34.08, 10.05, 100, 150);

        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testIOExceptionNoFile() {
        try {
            Reader.readAccount(new File("./data/noFile.txt"));
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testIOExceptionNotText() {
        try {
            Reader.readAccount(new File("./data/tobs.jpg"));
        } catch (IOException e) {
            // expected
        }
    }

    // --------------------------------------------------------

    // Private methods to help with testing

    private void checkRecordMatches(Record rec, String expectedTitle, String expectedCategory, double expectedValue) {
        assertEquals(expectedTitle, rec.getTitle());
        assertEquals(expectedCategory, rec.getCategory());
        assertEquals(expectedValue, rec.getValue());
    }

    private void checkCategorySumsMatch(Account acc, double income, double bills, double necessities,
                                        double shopping, double dining, double recreation, double other) {
        assertEquals(income, acc.getCategorySum("income"));
        assertEquals(bills, acc.getCategorySum("bills"));
        assertEquals(necessities, acc.getCategorySum("necessities"));
        assertEquals(shopping, acc.getCategorySum("shopping"));
        assertEquals(dining, acc.getCategorySum("dining"));
        assertEquals(recreation, acc.getCategorySum("recreation"));
        assertEquals(other, acc.getCategorySum("other"));
    }

}
