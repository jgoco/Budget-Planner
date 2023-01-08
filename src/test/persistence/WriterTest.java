package persistence;

import model.Account;
import model.Record;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class WriterTest {
    private static final String TEST_FILE = "./data/testAccount3.txt";
    private Writer testWriter;
    private Account acc;

    @BeforeEach
    void runBefore() throws FileNotFoundException, UnsupportedEncodingException {
        testWriter = new Writer(new File(TEST_FILE));
        acc = new Account();
    }

    @Test
    void testWriteAccounts() {
        acc.addRecord(new Record("First addition", "income", 150.16));
        acc.addRecord(new Record("Second addition", "other", 23));
        acc.addRecord(new Record("Third addition", "income", 0.23));

        testWriter.write(acc);
        testWriter.close();

        try {
            Account account = Reader.readAccount(new File(TEST_FILE));

            assertEquals(3, acc.getLog().size());
            Record rec0 = acc.getRecord(0);
            checkRecordMatches(rec0, "First addition", "income", 150.16);
            Record rec1 = acc.getRecord(1);
            checkRecordMatches(rec1, "Second addition", "other", 23);
            Record rec2 = acc.getRecord(2);
            checkRecordMatches(rec2, "Third addition", "income", 0.23);

            assertEquals(127.39, acc.getBalance());
            checkCategorySumsMatch(acc,150.39, 0, 0, 0, 0, 0, 23);

        } catch (IOException e) {
            fail("IOException should not have been thrown");
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
