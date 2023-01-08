package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CategoryInfoTest {
    CategoryInfo cat;
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

        cat = new CategoryInfo(acc);
    }

    @Test
    void testConstructorEmptyAccount() {
        checkCategoryNamesCorrect();
        assertEquals(7, cat.getCategoriesHashMap().size());
        checkCategorySumsMatch(0, 0, 0, 0, 0, 0, 0);
    }

    @Test
    void testUpdateCategory() {
        cat.updateCategory("income");
        checkCategorySumsMatch(0, 0, 0, 0, 0, 0, 0);

        acc.addRecord(recordPurchase);
        cat.updateCategory("other");
        checkCategorySumsMatch(0, 0, 0, 0, 0, 0, 75);

        acc.addRecord(recordIncome);
        cat.updateCategory("income");
        checkCategorySumsMatch(120, 0, 0, 0, 0, 0, 75);

        acc.addRecord(recordLowerLimit);
        cat.updateCategory("bills");
        checkCategorySumsMatch(120, 0.01, 0, 0, 0, 0, 75);

        cat.updateCategory("shopping");
        checkCategorySumsMatch(120, 0.01, 0, 0, 0, 0, 75);
    }

    @Test
    void testIterator() {
        int total = 0;
        for (String category : cat) {
            total = total + 1;
        }
        assertEquals(7, total);
    }



    // --------------------------------------------------------

    // Private methods to help with testing

    private void checkCategoryNamesCorrect() {
        ArrayList<String> categoryNames = cat.getCategories();

        assertEquals("income", categoryNames.get(0));
        assertEquals("bills", categoryNames.get(1));
        assertEquals("necessities", categoryNames.get(2));
        assertEquals("shopping", categoryNames.get(3));
        assertEquals("dining", categoryNames.get(4));
        assertEquals("recreation", categoryNames.get(5));
        assertEquals("other", categoryNames.get(6));
    }

    private void checkCategorySumsMatch(double income, double bills, double necessities,
                                        double shopping, double dining, double recreation, double other) {
        assertEquals(income, cat.getCategorySum("income"));
        assertEquals(bills, cat.getCategorySum("bills"));
        assertEquals(necessities, cat.getCategorySum("necessities"));
        assertEquals(shopping, cat.getCategorySum("shopping"));
        assertEquals(dining, cat.getCategorySum("dining"));
        assertEquals(recreation, cat.getCategorySum("recreation"));
        assertEquals(other, cat.getCategorySum("other"));
    }

}
