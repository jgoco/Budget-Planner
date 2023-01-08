package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecordTest {
    Record record;

    @BeforeEach
    void runBefore() {
        record = new Record("test", "other", 100);
    }

    @Test
    void testConstructor() {
        checkRecordMatches(record, "test", "other", 100);

        Record lowerLimit = new Record("lower limit", "income", 0.01);
        checkRecordMatches(lowerLimit, "lower limit", "income", 0.01);
    }

    @Test
    void testEditThisRecord() {
        checkRecordMatches(record, "test", "other", 100);

        record.editThisRecord("change1", "income", 3.89);
        checkRecordMatches(record, "change1", "income", 3.89);

        record.editThisRecord("change2", "bills", 0.01);
        checkRecordMatches(record, "change2", "bills", 0.01);
    }

    @Test
    void testAddToBalance() {
        double balance = 0;

        double newBalance = record.addToBalance(balance);
        assertEquals(-100, newBalance);

        Record income = new Record("add", "income", 155.34);
        newBalance = income.addToBalance(newBalance);
        assertEquals(55.34, newBalance);
    }

    @Test
    void testAddToCategory() {
        double newSum = record.addToCategory(0);
        assertEquals(100, newSum);

        Record income = new Record("add", "income", 155.34);
        newSum = income.addToCategory(newSum);
        assertEquals(255.34, newSum);
    }

    @Test
    void testSetTitle() {
        checkRecordMatches(record, "test", "other", 100);

        record.setTitle("changed");
        checkRecordMatches(record, "changed", "other", 100);
    }

    @Test
    void testSetCategory() {
        checkRecordMatches(record, "test", "other", 100);

        record.setCategory("income");
        checkRecordMatches(record, "test", "income", 100);
    }

    @Test
    void testSetValue() {
        checkRecordMatches(record, "test", "other", 100);

        record.setValue(13.55);
        checkRecordMatches(record, "test", "other", 13.55);

        record.setValue(0.01);
        checkRecordMatches(record, "test", "other", 0.01);
    }

    // --------------------------------------------------------

    // Private methods to help with testing

    private void checkRecordMatches(Record rec, String expectedTitle, String expectedCategory, double expectedValue) {
        assertEquals(expectedTitle, rec.getTitle());
        assertEquals(expectedCategory, rec.getCategory());
        assertEquals(expectedValue, rec.getValue());
    }

}
