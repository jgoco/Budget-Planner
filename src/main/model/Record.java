package model;

import persistence.Reader;
import persistence.SaveableData;

import java.io.PrintWriter;

// Represents a record (income or purchase)
// having a title,
// and a category (where "income" specifies a positive transaction (adding money to balance)
//                and other categories (bills, necessities, shopping, dining out, recreation, and other)
//                specify a negative transaction (removing money from balance))
// and a value (in dollars)
public class Record implements SaveableData {
    private String title;
    private String category;
    private double value;    // REQUIRES: value >= 0.01

    // REQUIRES: title cannot contain ","
    //           category cannot contain ","
    //           value >= 0.01
    // EFFECTS:  creates a record
    //           with title set to title, category set to category, and
    //           value set to value (>= 0.01)
    public Record(String title, String category, double value) {
        this.title = title;
        this.category = category;
        this.value = value;
    }

    // --------------------------------------------------------

    // Record functions

    // REQUIRES: newTitle cannot contain ","
    //           newCategory cannot contain ","
    //           newValue >= 0.01
    // MODIFIES: this
    // EFFECTS:  edits the record to have a title of newTitle,
    //           a category of newCategory, and
    //           a value of newValue
    public void editThisRecord(String newTitle, String newCategory, double newValue) {
        setTitle(newTitle);
        setCategory(newCategory);
        setValue(newValue);
    }

    // --------------------------------------------------------

    // Balance and sum functions

    // EFFECTS:  if category is "income", adds value to balance,
    //           and return the new balance;
    //           otherwise, subtracts value from balance
    //           and return the new balance
    public double addToBalance(double balance) {
        double newBalance;
        if (category.equals("income")) {
            newBalance = balance + value;
        } else {
            newBalance = balance - value;
        }
        return newBalance;
    }

    // REQUIRES: sum >= 0
    // EFFECTS:  adds value to sum and
    //           returns the new total sum of the category
    public double addToCategory(double sum) {
        double newTotalSum = sum + value;
        return newTotalSum;
    }

    // --------------------------------------------------------

    // Persistence methods

    // MODIFIES: printWriter
    // EFFECTS:  writes the record to printWriter (one line)
    @Override
    public void save(PrintWriter printWriter) {
        printWriter.print(title);
        printWriter.print(Reader.DELIMITER);
        printWriter.print(category);
        printWriter.print(Reader.DELIMITER);
        printWriter.print(value);
    }

    // --------------------------------------------------------

    // Getters and Setters

    // EFFECTS:  returns the title of the record
    public String getTitle() {
        return title;
    }

    // EFFECTS:  returns the category of the record
    public String getCategory() {
        return category;
    }

    // EFFECTS:  returns the value of the record
    public double getValue() {
        return value;
    }

    // MODIFIES: this
    // EFFECTS:  sets the title of the record to newTitle
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    // MODIFIES: this
    // EFFECTS:  sets the category of the record to newCategory
    public void setCategory(String newCategory) {
        this.category = newCategory;
    }

    // REQUIRES: newValue >= 0.01
    // MODIFIES: this
    // EFFECTS:  sets the value of the record to newValue
    public void setValue(double newValue) {
        this.value = newValue;
    }

}
