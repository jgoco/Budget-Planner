package model;

import persistence.SaveableData;

import java.io.PrintWriter;
import java.util.ArrayList;

// Represents the account having a records log,
// a balance (in dollars), and
// a category information storage
// (containing a list of category names and total sum of each category)
public class Account implements SaveableData {
    private ArrayList<Record> log;
    private double balance;
    private CategoryInfo categories;

    // EFFECTS:  creates an account with an empty records log,
    //           an initial balance of zero,
    //           a category information storage
    public Account() {
        log = new ArrayList<>();
        balance = 0;
        categories = new CategoryInfo(this);
    }

    // EFFECTS:  creates an account with a given records log,
    //           calculates/updates the balance based on the log,
    //           then creates a category information storage
    //           and calculates/updates the total sum of categories based on the log
    public Account(ArrayList<Record> log) {
        this.log = log;
        updateBalance();

        categories = new CategoryInfo(this);
        for (String category : categories) {
            updateCategory(category);
        }
    }

    // --------------------------------------------------------

    // Records log methods

    // MODIFIES: this
    // EFFECTS:  adds the record to the end of the records log
    //           and then updates the balance and
    //           record's category total sum
    public void addRecord(Record record) {
        log.add(record);
        updateBalance();
        updateCategory(record.getCategory());
    }

    // REQUIRES: newTitle cannot contain ,
    //           newCategory cannot contain ,
    //           newValue >= 0.01
    //           pos >= 0
    // MODIFIES: this, record
    // EFFECTS:  edits the record in position number pos (indexed at 0)
    //           to have a title of newTitle, a category of newCategory, and
    //           a value of newValue, and then updates the balance and
    //           the record's old and new categories' total sum;
    //           otherwise, if no record is at position number pos,
    //           do nothing
    public void editRecord(int pos, String newTitle, String newCategory, double newValue) {
        if (isInLog(pos)) {
            Record recordToEdit = getRecord(pos);
            String recordPreviousCategory = recordToEdit.getCategory();
            recordToEdit.editThisRecord(newTitle, newCategory, newValue);
            updateBalance();
            updateCategory(recordPreviousCategory);
            updateCategory(newCategory);
        }
    }

    // REQUIRES: pos >= 0
    // MODIFIES: this, record
    // EFFECTS:  removes the record in position number pos (indexed at 0)
    //           and then updates the balance and record's category total sum;
    //           otherwise, if no record is at position number pos,
    //           do nothing
    public void removeRecord(int pos) {
        if (isInLog(pos)) {
            String recordCategory = getRecord(pos).getCategory();
            log.remove(pos);
            updateBalance();
            updateCategory(recordCategory);
        }
    }

    // EFFECTS:  returns true if log is empty, otherwise false
    public boolean isLogEmpty() {
        return log.size() == 0;
    }

    // REQUIRES: pos >= 0
    // EFFECTS:  return true if there is a record in position number pos (indexed at 0);
    //           otherwise, if no record is found in pos, return false
    public boolean isInLog(int pos) {
        int numberOfRecords = log.size();
        int canBeCalled = numberOfRecords - 1;
        return pos <= canBeCalled;
    }

    // --------------------------------------------------------

    // Update balance and category methods

    // MODIFIES: this
    // EFFECTS:  calculates new balance based on records in the log
    //           and updates the balance;
    //           if log is empty, balance is zero
    public void updateBalance() {
        double sum = 0;
        if (! isLogEmpty()) {
            for (Record record : log) {
                sum = record.addToBalance(sum);
            }
        }
        balance = sum;
    }

    // MODIFIES: this
    // EFFECTS:  calculates and updates category's total sum (in dollars) based on records in the log
    public void updateCategory(String category) {
        categories.updateCategory(category);
    }

    // --------------------------------------------------------

    // Persistence methods

    // MODIFIES: printWriter
    // EFFECTS:  writes the records log to printWriter
    @Override
    public void save(PrintWriter printWriter) {
        for (Record r : log) {
            r.save(printWriter);
            printWriter.print("\n");
        }
    }

    // --------------------------------------------------------

    // Getters

    // EFFECTS:  returns the records log of the account
    public ArrayList<Record> getLog() {
        return log;
    }

    // REQUIRES: pos >= 0 and
    //           a record is found in position number pos of log
    // EFFECTS:  gets and returns the record in position number pos (indexed at 0)
    public Record getRecord(int pos) {
        return log.get(pos);
    }

    // EFFECTS:  returns the balance of the account
    public double getBalance() {
        return balance;
    }

    // EFFECTS:  returns the category names list
    public CategoryInfo getCategories() {
        return categories;
    }

    // REQUIRES: category exists in categories.categoryNames
    // EFFECTS:  returns the category's total sum (in dollars)
    public double getCategorySum(String category) {
        return categories.getCategorySum(category);
    }

}
