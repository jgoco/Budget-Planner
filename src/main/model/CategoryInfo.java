package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// Represents a category information storage, containing
// a list containing the following category names:
// income, bills, necessities, shopping, dining out, recreation, and other,
// and a categories HashMap (key: category name, value: category's total sum (in dollars))
public class CategoryInfo implements Iterable<String> {
    private Account acc;
    private ArrayList<String> categoryNames;
    private Map<String, Double> categoryMap;

    // EFFECTS:  creates a category information storage based on the acc,
    //           with a list of category names and
    //           a categories HashMap with key: category name
    //                                     value: total sum of category (initially zero)
    public CategoryInfo(Account acc) {
        this.acc = acc;
        createCategoryNames();
        createCategoryHashMap();
    }

    // MODIFIES: this
    // EFFECTS:  creates a categories list containing the following categories:
    //           income, bills, necessities, shopping, dining out, recreation, and other
    private void createCategoryNames() {
        categoryNames = new ArrayList<>(7);

        categoryNames.add("income");
        categoryNames.add("bills");
        categoryNames.add("necessities");
        categoryNames.add("shopping");
        categoryNames.add("dining");
        categoryNames.add("recreation");
        categoryNames.add("other");
    }

    // MODIFIES: this
    // EFFECTS:  creates the categories HashMap with key: category name (based on the categoryNames list)
    //                                               value: total sum of category (initially zero)
    private void createCategoryHashMap() {
        categoryMap = new HashMap<>();
        for (String category : categoryNames) {
            categoryMap.put(category, 0.00);
        }
    }

    // MODIFIES: this
    // EFFECTS:  calculates category's total sum (in dollars) based on records in the log
    //           and updates the total sum in the categories HashMap;
    //           if log is empty or no records in the log have this category,
    //           category total is zero
    public void updateCategory(String category) {
        double sum = 0;
        if (! acc.isLogEmpty()) {
            for (Record record : acc.getLog()) {
                if (record.getCategory().equals(category)) {
                    sum = record.addToCategory(sum);
                }
            }
        }
        categoryMap.replace(category, sum);
    }

    // --------------------------------------------------------

    // Iterable<String> method

    // EFFECTS:  returns an iterator over elements of type String
    @Override
    public Iterator<String> iterator() {
        return categoryNames.iterator();
    }

    // --------------------------------------------------------

    // Getters

    // REQUIRES: category exists in categoryNames
    // EFFECTS:  returns the category's total sum (in dollars)
    public double getCategorySum(String category) {
        return categoryMap.get(category);
    }

    // EFFECTS:  returns the category names list
    public ArrayList<String> getCategories() {
        return categoryNames;
    }

    // EFFECTS:  returns the categories HashMap
    public Map<String, Double> getCategoriesHashMap() {
        return categoryMap;
    }

}
