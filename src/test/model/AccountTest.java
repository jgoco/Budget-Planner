package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    Account acc;
    Record recordPurchase;
    Record recordIncome;
    Record recordLowerLimit;

    @BeforeEach
    void runBefore() {
        acc = new Account();
        recordPurchase = new Record("sub", "other", 75);
        recordIncome = new Record("add", "income", 120);
        recordLowerLimit = new Record("3", "bills", 0.01);
    }

    @Test
    void testNewConstructor() {
        assertEquals(0, acc.getLog().size());
        assertTrue(acc.isLogEmpty());
        assertEquals(0, acc.getBalance());
        assertEquals(7, acc.getCategories().getCategoriesHashMap().size());
        checkCategorySumsMatch(acc, 0, 0, 0, 0, 0, 0, 0);
    }

    @Test
    void testReadConstructor() {
        ArrayList<Record> log = new ArrayList<>();
        log.add(recordPurchase);
        log.add(recordIncome);
        log.add(recordLowerLimit);
        Account accLoaded = new Account(log);

        assertEquals(3, accLoaded.getLog().size());
        assertEquals(recordPurchase, accLoaded.getRecord(0));
        assertEquals(recordIncome, accLoaded.getRecord(1));
        assertEquals(recordLowerLimit, accLoaded.getRecord(2));
        assertEquals(44.99, accLoaded.getBalance());
        assertEquals(7, acc.getCategories().getCategoriesHashMap().size());
        checkCategorySumsMatch(accLoaded, 120, 0.01, 0, 0, 0,0, 75);
    }

    @Test
    void testAddRecords() {
        acc.addRecord(recordPurchase);
        assertEquals(1, acc.getLog().size());
        checkRecordMatches(acc.getRecord(0), "sub", "other", 75);
        assertEquals(-75, acc.getBalance());
        checkCategorySumsMatch(acc, 0, 0,0, 0, 0, 0, 75);

        acc.addRecord(recordIncome);
        assertEquals(2, acc.getLog().size());
        checkRecordMatches(acc.getRecord(0), "sub", "other", 75);
        checkRecordMatches(acc.getRecord(1), "add", "income", 120);
        assertEquals(45, acc.getBalance());
        checkCategorySumsMatch(acc, 120, 0,0, 0, 0, 0, 75);

        acc.addRecord(recordLowerLimit);
        assertEquals(3, acc.getLog().size());
        checkRecordMatches(acc.getRecord(0), "sub", "other", 75);
        checkRecordMatches(acc.getRecord(1), "add", "income", 120);
        checkRecordMatches(acc.getRecord(2), "3", "bills", 0.01);
        checkCategorySumsMatch(acc, 120, 0.01,0, 0, 0, 0, 75);
        assertEquals(44.99, acc.getBalance());
    }

    @Test
    void testEditRecords() {
        setUpAccount3Records();

        acc.editRecord(0, "add", "income", 0.01);
        assertEquals(3, acc.getLog().size());
        checkRecordMatches(acc.getRecord(0), "add", "income", 0.01);
        checkRecordMatches(acc.getRecord(1), "add", "income", 120);
        checkRecordMatches(acc.getRecord(2), "3", "bills", 0.01);
        assertEquals(120, acc.getBalance());
        checkCategorySumsMatch(acc, 120.01, 0.01, 0, 0, 0, 0, 0);

        acc.editRecord(2, "change", "dining", 0.56);
        assertEquals(3, acc.getLog().size());
        checkRecordMatches(acc.getRecord(0), "add", "income", 0.01);
        checkRecordMatches(acc.getRecord(1), "add", "income", 120);
        checkRecordMatches(acc.getRecord(2), "change", "dining", 0.56);
        assertEquals(119.45, acc.getBalance());
        checkCategorySumsMatch(acc, 120.01, 0, 0, 0, 0.56, 0, 0);
    }

    @Test
    void testEditOutsideLogNoChange() {
        setUpAccount3Records();

        acc.editRecord(3, "do nothing", "other", 30);
        assertEquals(3, acc.getLog().size());
        checkRecordMatches(acc.getRecord(0), "sub", "other", 75);
        checkRecordMatches(acc.getRecord(1), "add", "income", 120);
        checkRecordMatches(acc.getRecord(2), "3", "bills", 0.01);
        assertEquals(44.99, acc.getBalance());
        checkCategorySumsMatch(acc, 120, 0.01, 0, 0, 0, 0, 75);
    }

    @Test
    void testRemoveRecords() {
        setUpAccount3Records();

        acc.removeRecord(0);
        assertEquals(2, acc.getLog().size());
        checkRecordMatches(acc.getRecord(0), "add", "income", 120);
        checkRecordMatches(acc.getRecord(1), "3", "bills", 0.01);
        assertEquals(119.99, acc.getBalance());
        checkCategorySumsMatch(acc, 120, 0.01, 0, 0, 0, 0, 0);

        acc.removeRecord(1);
        assertEquals(1, acc.getLog().size());
        checkRecordMatches(acc.getRecord(0), "add", "income", 120);
        assertEquals(120, acc.getBalance());
        checkCategorySumsMatch(acc, 120, 0, 0, 0, 0, 0, 0);
    }

    @Test
    void testRemoveRecordsOutsideLogNoChange() {
        acc.removeRecord(0);
        assertEquals(0, acc.getLog().size());

        acc.addRecord(recordIncome);
        acc.addRecord(recordLowerLimit);
        assertEquals(119.99, acc.getBalance());
        checkCategorySumsMatch(acc, 120, 0.01, 0, 0, 0, 0, 0);

        acc.removeRecord(3);
        assertEquals(2, acc.getLog().size());
        checkRecordMatches(acc.getRecord(0), "add", "income", 120);
        checkRecordMatches(acc.getRecord(1), "3", "bills", 0.01);
        assertEquals(119.99, acc.getBalance());
        checkCategorySumsMatch(acc, 120, 0.01, 0, 0, 0, 0, 0);

    }

    @Test
    void testIsLogEmpty() {
        assertEquals(0, acc.getLog().size());
        assertTrue(acc.isLogEmpty());

        acc.addRecord(recordPurchase);
        assertEquals(1, acc.getLog().size());
        assertFalse(acc.isLogEmpty());
    }

    @Test
    void testIsInLog() {
        setUpAccount3Records();

        assertTrue(acc.isInLog(0));
        assertTrue(acc.isInLog(2));
        assertFalse(acc.isInLog(3));
    }

    @Test
    void testUpdateBalanceLogEmpty() {
        assertTrue(acc.isLogEmpty());
        acc.updateBalance();
        assertEquals(0, acc.getBalance());
    }


    @Test
    void testUpdateBalance() {
        acc.addRecord(recordPurchase);
        assertEquals(-75, acc.getBalance());

        acc.addRecord(recordIncome);
        assertEquals(45, acc.getBalance());

        acc.addRecord(recordLowerLimit);
        assertEquals(44.99, acc.getBalance());

        acc.editRecord(2, "change", "income", 30);
        assertEquals(75, acc.getBalance());
    }

    @Test
    void testUpdateCategoryLogEmpty() {
        assertTrue(acc.isLogEmpty());
        acc.updateCategory("income");
        acc.updateCategory("shopping");
        checkCategorySumsMatch(acc, 0, 0, 0, 0, 0, 0, 0);
    }


    @Test
    void testUpdateCategory() {
        acc.addRecord(recordPurchase);
        checkCategorySumsMatch(acc, 0, 0, 0, 0, 0, 0, 75);

        acc.addRecord(recordIncome);
        checkCategorySumsMatch(acc,  120, 0, 0, 0, 0, 0, 75);

        acc.addRecord(recordLowerLimit);
        checkCategorySumsMatch(acc,  120, 0.01, 0, 0, 0, 0, 75);

        acc.editRecord(2, "change", "income", 30);
        checkCategorySumsMatch(acc,  150, 0, 0, 0, 0, 0, 75);
    }

    @Test
    void testGetCategorySum() {
        setUpAccount3Records();

        double income = acc.getCategorySum("income");
        assertEquals(120, income);

        double other = acc.getCategorySum("other");
        assertEquals(75, other);

        double necessities = acc.getCategorySum("necessities");
        assertEquals(0, necessities);
    }




    // --------------------------------------------------------

    // Private methods to help with testing

    private void setUpAccount3Records() {
        acc.addRecord(recordPurchase);
        acc.addRecord(recordIncome);
        acc.addRecord(recordLowerLimit);
        assertEquals(44.99, acc.getBalance());
        checkCategorySumsMatch(acc, 120, 0.01, 0, 0, 0, 0, 75);
    }

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